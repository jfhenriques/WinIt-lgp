package pt.techzebra.promgamemobile.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.promgamemobile.ui.AuthenticationActivity;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Provides utility methods for communicating with the server.
 */
public class NetworkUtilities {
    private static final String TAG = "NetworkUtilities";

    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_UPDATED = "timestamp";
    public static final String USER_AGENT = "AuthenticationService/1.0";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    // TODO: change to the correct URL
    public static final String BASE_URL =
            "https://api.tlantic.techzebra.pt";
    
    public static final String AUTH_URI = BASE_URL + "/session";
    public static final String FETCH_USER_URI = BASE_URL + "/users";
    
    private static HttpClient http_client_;
    
    /**
     * Configures the HttpClient to connect to the URL provided.
     */
    public static void maybeCreateHttpClient() {
        if (http_client_ == null) {
            http_client_ = new DefaultHttpClient();
            final HttpParams params = http_client_.getParams();
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
        }
    }
    
    /**
     * Executes the network requests on a separate thread.
     * 
     * @param runnable The runnable instance containing network operations_ to
     *        be executed.
     */    
    public static Thread performOnBackgroundThread(final Runnable runnable) {
       final Thread t = new Thread() {
           @Override
           public void run() {
               runnable.run();
           };
       };
       t.start();
       return t;  
    }
    
    /**
     * Connects to the server, authenticates the provided username and
     * password.
     * 
     * @param username The user's username
     * @param password The user's password
     * @param handler The hander instance from the calling UI thread.
     * @param context The context of the calling Activity.
     * @return boolean The boolean result indicating whether the user was
     *         successfully authenticated.
     */
    public static boolean authenticate(String username, String password,
            Handler handler, final Context context) {
        final HttpResponse http_response;
        
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, username));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
        
        final HttpPost post = new HttpPost(AUTH_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();
        
        try {
            http_response = http_client_.execute(post);
            if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Successful authentication");
                }
                sendResultToAuthenticationActivity(true, handler, context);
                return true;
            } else {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Error authenticating" + http_response.getStatusLine());
                }
                sendResultToAuthenticationActivity(false, handler, context);
                return false;
            }
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "IOException when getting authtoken", e);
            }
            sendResultToAuthenticationActivity(false, handler, context);
            return false;
        } finally {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "getAuthtoken completing");
            }
        }
    }
    
    /**
     * Sends the authentication response from server back to the caller main UI
     * thread through its handler.
     * 
     * @param result The boolean holding authentication result
     * @param handler The main UI thread's handler instance.
     * @param context The caller Activity's context.
     */
    private static void sendResultToAuthenticationActivity(final boolean result,
            final Handler handler, final Context context) {
        if (handler == null || context == null) {
            return;
        }
        
        handler.post(new Runnable() {
            @Override
            public void run() {
                ((AuthenticationActivity) context).onAuthenticationResult(result);
            }
        });
    }

    public static Thread attemptAuth(final String username, final String password, final Handler handler, final Context context) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                authenticate(username, password, handler, context);
            }
        };
        
        return NetworkUtilities.performOnBackgroundThread(runnable);
    }
    
    public static User fetchUserInformation(Account account, String auth_token,
            Date last_updated) throws ClientProtocolException, IOException,
            JSONException, AuthenticationException {
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, account.name));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, auth_token));
        if (last_updated != null) {
            final SimpleDateFormat formatter =
                    new SimpleDateFormat("dd/MM/yyyy HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            params.add(new BasicNameValuePair(PARAM_UPDATED, formatter
                    .format(last_updated)));
        }
        
        Log.i(TAG, params.toString());
        
        final HttpPost post = new HttpPost(FETCH_USER_URI);
        HttpEntity entity = new UrlEncodedFormEntity(params);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();
        
        final HttpResponse http_response = http_client_.execute(post);
        final String response = EntityUtils.toString(http_response.getEntity());
        
        User user = null;
        
        if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            user = User.valueOf(new JSONObject(response));
        } else {
            if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in fetching user information");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in fetching user information");
                throw new IOException();
            }
        }

        return user;
    }
}
