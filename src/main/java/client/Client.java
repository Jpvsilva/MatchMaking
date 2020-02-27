package client;

import generalresources.Tuple;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.*;
import java.security.cert.CertificateException;

import static client.Menu.menuHUB;
import static tlsContext.SSLContext.initSSLContext;

public class Client {

    public static void main(String[] args){
        System.out.println("### PLAYER ###\n");

        try {
            // init SSL Context
            Tuple<X509KeyManager, X509TrustManager> r = initSSLContext("client/client-certificate.p12","server/server-certificate.p12");

            // set up the SSL Context
            SSLSocket kkSocket = setupSSLContext(r);

            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));

            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            menuHUB(Menu.MENU.FIRST_MENU, out, in, false);

            in.close();
            out.close();
            sin.close();
            kkSocket.close();

        }catch (IOException | NoSuchProviderException | KeyManagementException | KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e){
            e.printStackTrace();
        }

    }

    private static SSLSocket setupSSLContext(Tuple<X509KeyManager, X509TrustManager> r)
            throws NoSuchAlgorithmException, KeyManagementException, IOException {

        // set up the SSL Context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(new KeyManager[]{r.x}, new TrustManager[]{r.y}, null);

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        SSLSocket kkSocket = (SSLSocket) socketFactory.createSocket("127.0.0.1", 1111);
        kkSocket.setEnabledProtocols(new String[]{"TLSv1.2"});

        return kkSocket;
    }

}