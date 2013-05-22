package pt.techzebra.winit.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class CustomSSLSocketFactory implements SocketFactory, LayeredSocketFactory {
    private SSLContext ssl_context_ = null;
    
    private static SSLContext createCustomSSLContext() throws IOException {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] {
                new CustomX509TrustManager(null)
            }, null);
            return context;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
    
    private SSLContext getSSLContext() throws IOException {
        if (ssl_context_ == null) {
            ssl_context_ = createCustomSSLContext();
        }
        
        return ssl_context_;
    }
    
    @Override
    public Socket connectSocket(Socket sock, String host, int port,
            InetAddress local_address, int local_port, HttpParams params)
            throws IOException, UnknownHostException, ConnectTimeoutException {
        int connection_timeout = HttpConnectionParams.getConnectionTimeout(params);
        int socket_timeout = HttpConnectionParams.getSoTimeout(params);
        
        InetSocketAddress remote_address = new InetSocketAddress(host, port);
        SSLSocket ssl_socket = (SSLSocket) ((sock != null) ?  sock : createSocket());
        
        if ((local_address != null) || (local_port > 0)) {
            if (local_port < 0) {
                local_port = 0;
            }
            InetSocketAddress isa = new InetSocketAddress(local_address, local_port);
            ssl_socket.bind(isa);
        }
        
        ssl_socket.connect(remote_address, connection_timeout);
        ssl_socket.setSoTimeout(socket_timeout);
        
        return ssl_socket;
    }

    @Override
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    @Override
    public boolean isSecure(Socket sock) throws IllegalArgumentException {
        return true;
    }
    
    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {

        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public boolean equals(Object o) {
        return ((o != null) && o.getClass().equals(CustomSSLSocketFactory.class));
    }
    
    public int hashCode() {
        return CustomSSLSocketFactory.class.hashCode();
    }
}
