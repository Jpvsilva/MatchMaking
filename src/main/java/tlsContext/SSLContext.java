package tlsContext;

import generalresources.Tuple;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLContext {

    /**
     * Creates KeyManager and TrustManager from the certificates created previously
     * Uses both client and server certificates to generate keystore and truststore
     * */
    public static Tuple<X509KeyManager, X509TrustManager> initSSLContext(String serverFile, String clientFile)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String password = "password";

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(serverFile);
        keyStore.load(inputStream, password.toCharArray());

        String password2 = "password";
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
        InputStream inputStream1 = ClassLoader.getSystemClassLoader().getResourceAsStream(clientFile);
        trustStore.load(inputStream1, password2.toCharArray());
        trustManagerFactory.init(trustStore);

        X509TrustManager x509TrustManager = null;
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                x509TrustManager = (X509TrustManager) trustManager;
                break;
            }
        }

        if (x509TrustManager == null) throw new NullPointerException();

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
        keyManagerFactory.init(keyStore, password.toCharArray());
        X509KeyManager x509KeyManager = null;
        for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
            if (keyManager instanceof X509KeyManager) {
                x509KeyManager = (X509KeyManager) keyManager;
                break;
            }
        }
        if (x509KeyManager == null) throw new NullPointerException();

        return new Tuple<>(x509KeyManager, x509TrustManager);
    }
}
