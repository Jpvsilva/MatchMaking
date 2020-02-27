package server;

import generalresources.Hero;
import generalresources.Tuple;
import generalresources.User;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

import static tlsContext.SSLContext.initSSLContext;

public class Server {

    static TreeMap<String, User> Users;
    static TreeMap<Integer, Hero> Heroes;


    public static void main(String[] args) {
        Users = new TreeMap<>();
        Heroes = new TreeMap<>();

        ArrayBlockingQueue<User> onlinePlayers = new ArrayBlockingQueue<>(50);

        PlayerManager pm = new PlayerManager(Users);
        // Thread Responsible for updating Users state
        TeamsManager teamsManager = new TeamsManager (pm, onlinePlayers);
        Thread tm = new Thread (teamsManager);
        tm.start();

        System.out.println("### SERVER ###\n");
        try {

            // init SSL Context
            Tuple<X509KeyManager, X509TrustManager> r = initSSLContext("server/server-certificate.p12","client/client-certificate.p12");

            // set up the SSL Context
            SSLServerSocket serverSocket = setupSSLContext(r);

            int counter = 0;
            while(true){
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                counter++;

                Thread worker = new Thread(new ServerWorker(socket,counter,pm,teamsManager,onlinePlayers));
                worker.start();
            }

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | NoSuchProviderException | UnrecoverableKeyException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates SSL Socket for secure connection
     * */
    private static SSLServerSocket setupSSLContext(Tuple<X509KeyManager, X509TrustManager> r)
            throws NoSuchAlgorithmException, KeyManagementException, IOException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(new KeyManager[]{r.x}, new TrustManager[]{r.y}, null);

        SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(1111);
        serverSocket.setNeedClientAuth(true);
        serverSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
        return serverSocket;
    }
}