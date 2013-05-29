package pt.techzebra.winit.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class CustomSSLSocketFactory extends SSLSocketFactory {
    private SSLContext ssl_context_ = SSLContext.getInstance("TLS");

    public CustomSSLSocketFactory(SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        ssl_context_ = context;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean auto_close) throws IOException, UnknownHostException {
        return ssl_context_.getSocketFactory().createSocket(socket, host, port,
                auto_close);
    }

    @Override
    public Socket createSocket() throws IOException {
        return ssl_context_.getSocketFactory().createSocket();
    }
}
