package pt.techzebra.winit.client;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;

public class CustomHttpClient extends DefaultHttpClient {
    final Context context_;
    
    public CustomHttpClient(HttpParams params, Context context) {
        super(params);
        context_ = context;
    }
    
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", new CustomSSLSocketFactory(), 443));
        //return new SingleClientConnManager(getParams(), registry);
        return new ThreadSafeClientConnManager(getParams(), registry);
    }
}
