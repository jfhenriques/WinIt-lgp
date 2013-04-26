package pt.techzebra.promgamemobile.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.ui.AuthenticationActivity;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * Provides utility methods for communicating with the server.
 */
public class NetworkUtilities {
    private static final String TAG = "NetworkUtilities";

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_BIRTHDAY = "date";
    public static final String PARAM_CP4 = "cp4";
    public static final String PARAM_CP3 = "cp3";
    public static final String PARAM_DOOR = "portaAndar";
    public static final String PARAM_FLOOR = "floor";
    public static final String PARAM_UPDATED = "timestamp";
    public static final String PARAM_AUTH_TOKEN = "token";
    
    public static final String USER_AGENT = "AuthenticationService/1.0";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    public static final String HOST = "lgptlantic.fe.up.pt";
    // TODO: change to the correct URL
    public static final String BASE_URL = "http://lgptlantic.fe.up.pt/api";

    public static final String AUTH_URI = BASE_URL + "/session.json";
    public static final String FETCH_USER_URI = BASE_URL + "/user.json";
    //TODO o de baixo nao serve para nada, em vez de se fazer get ao users, faz-se um post ao FETCH_USER_URI
    public static final String NEW_USER_URI = FETCH_USER_URI + "/new.json";

    private static HttpClient http_client_;

    /**
     * Configures the HttpClient to connect to the URL provided.
     */
    public static void maybeCreateHttpClient() {
        if (http_client_ == null) {
            http_client_ = new DefaultHttpClient();
            final HttpParams params = http_client_.getParams();
            HttpConnectionParams.setConnectionTimeout(params,
                    REGISTRATION_TIMEOUT);
            ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
        }
    }

    /**
     * Executes the network requests on a separate thread.
     * 
     * @param runnable
     *            The runnable instance containing network operations_ to be
     *            executed.
     */
    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
            	try {
            		runnable.run();
            	} finally {
            		
            	}
            };
        };
        t.start();
        return t;
    }

    /**
     * Connects to the server, authenticates the provided username and password.
     * 
     * @param username
     *            The user's username
     * @param password
     *            The user's password
     * @param handler
     *            The hander instance from the calling UI thread.
     * @param context
     *            The context of the calling Activity.
     * @return boolean The boolean result indicating whether the user was
     *         successfully authenticated.
     * @throws JSONException 
     */
    public static boolean authenticate(String username, String password,
            Handler handler, final Context context) throws JSONException {
        final HttpResponse http_response;
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_EMAIL, username));
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
                final String response = EntityUtils.toString(http_response.getEntity());
                JSONObject response_json = new JSONObject(response);
                int response_s = response_json.getInt("s");
                if (response_s != 0) {
                	sendResultToAuthenticationActivity(false, handler, context);
                	return false;
                } else {
                	JSONObject response_r = response_json.getJSONObject("r");
                    SharedPreferences.Editor preferences_editor = PromGame.getAppContext().getSharedPreferences(
                            Constants.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
                    preferences_editor.putString(Constants.PREF_AUTH_TOKEN, response_r.getString("token"));
                    preferences_editor.putBoolean(Constants.PREF_LOGGED_IN, true);
                    preferences_editor.commit();
                    sendResultToAuthenticationActivity(true, handler, context);
                    return true;
                }
                
            } else {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG,
                            "Error authenticating"
                                    + http_response.getStatusLine());
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
     * @param result
     *            The boolean holding authentication result
     * @param handler
     *            The main UI thread's handler instance.
     * @param context
     *            The caller Activity's context.
     */
    private static void sendResultToAuthenticationActivity(
            final boolean result, final Handler handler, final Context context) {
        if (handler == null || context == null) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                ((AuthenticationActivity) context)
                        .onAuthenticationResult(result);
            }
        });
    }

    public static Thread attemptAuth(final String username,
            final String password, final Handler handler, final Context context) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
					authenticate(username, password, handler, context);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };

        return NetworkUtilities.performOnBackgroundThread(runnable);
    }

    public static User fetchUserInformation(String email, String auth_token,
            Date last_updated) throws ClientProtocolException, IOException,
            JSONException, AuthenticationException {
        
        String URI = FETCH_USER_URI + "?token=" + auth_token;
        final HttpGet get = new HttpGet(URI);
        HttpHost host = new HttpHost(HOST);
        maybeCreateHttpClient();
        final HttpResponse http_response = http_client_.execute(host, get);
        final String response = EntityUtils.toString(http_response.getEntity());       
        User user = null;

        if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        	JSONObject response_json = new JSONObject(response);
        	JSONObject response_r = response_json.getJSONObject("r");
        	user = new User(response_r.getInt("uid"), response_r.getString("name"), response_r.getString("email"), response_r.getString("level"), response_r.getString("points") );
        } else {
            if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG,
                        "Authentication exception in fetching user information");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in fetching user information");
                throw new IOException();
            }
        }

        return user;
    }
    
    //TODO falta adicionar o floor à BD
    private static boolean register(String name, String username, String password,
            String birthday, String cp4, String cp3, String door, Handler handler,
            final Context context) {
        final HttpResponse resp;

        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_EMAIL, username));
        params.add(new BasicNameValuePair(PARAM_NAME, name));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        params.add(new BasicNameValuePair(PARAM_BIRTHDAY, birthday));
        params.add(new BasicNameValuePair(PARAM_CP4, cp4));
        params.add(new BasicNameValuePair(PARAM_CP3, cp3));
        params.add(new BasicNameValuePair(PARAM_DOOR, door));
        
        HttpEntity entity = null;
        
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        final HttpPost post = new HttpPost(FETCH_USER_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();
        
        try {
            resp = http_client_.execute(post);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                attemptAuth(username, password, handler, context);
                Log.v(TAG, "Successful register");
                return true;
            }else{
                //TODO ver JSONArray com os erros que o servidor deteta
                if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                    Log.e(TAG, "Register exception in fetching remote");
                    return false;
                }else{
                    Log.e(TAG, "Server error in fetching remote info" + resp.getStatusLine());
                    return false;
                }
            }
            
        } catch (IOException e) {
            Log.v(TAG, "IOException when getting authtoken", e);
            return false;
        } finally {
            Log.v(TAG, "getAuthtoken completing");
        }
        
    }

    //TODO falta o floor porque nao há campo na BD
    public static Thread attemptRegister(final String name, final String email,
            final String password, final String birthday, final String cp4,
            final String cp3, final String door, final Handler handler,
            final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                register(name, email, password, birthday, cp4, cp3, door, handler,
                        context);
            }
        };
        return NetworkUtilities.performOnBackgroundThread(runnable);
    }
}
