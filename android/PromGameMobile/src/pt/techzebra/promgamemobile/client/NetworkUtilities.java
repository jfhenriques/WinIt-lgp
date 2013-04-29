package pt.techzebra.promgamemobile.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.ui.AuthenticationActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Provides utility methods for communicating with the server.
 */
public class NetworkUtilities {
    private static final String TAG = "NetworkUtilities";

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_BIRTHDAY = "date";
    public static final String PARAM_PC4 = "cp4";
    public static final String PARAM_PC3 = "cp3";
    public static final String PARAM_HOUSE_NUMBER = "portaAndar";
    public static final String PARAM_UPDATED = "timestamp";
    public static final String PARAM_AUTH_TOKEN = "token";

    public static final String USER_AGENT = "AuthenticationService/1.0";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    public static final String HOST = "lgptlantic.fe.up.pt";
    public static final String BASE_URL = "http://lgptlantic.fe.up.pt/api";

    public static final String AUTH_URI = BASE_URL + "/session.json";
    public static final String USER_URI = BASE_URL + "/user.json";
    public static final String PROMOTION_URI = BASE_URL + "/promotion";

    public static final String QUIZ_URI = "/quizgame.json";

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

    private static JSONObject executeRequest(HttpRequest request) {
        maybeCreateHttpClient();
        HttpHost host = new HttpHost(HOST);

        HttpResponse response;
        try {
            response = http_client_.execute(host, request);
            int status_code = response.getStatusLine().getStatusCode();

            if (status_code == HttpStatus.SC_OK) {
                JSONObject json_response = new JSONObject(
                        EntityUtils.toString(response.getEntity()));

                return json_response;
            } else {
                Log.i(TAG, "Request Error");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject simpleRequest(HttpRequestBase request) {
        return executeRequest(request);
    }

    private static JSONObject enclosingRequest(
            HttpEntityEnclosingRequestBase request,
            ArrayList<NameValuePair> parameters) {
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(parameters);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        request.addHeader(entity.getContentType());
        request.setEntity(entity);

        return executeRequest(request);
    }

    private static JSONObject get(String uri) {
        return simpleRequest(new HttpGet(uri));
    }

    private static JSONObject delete(String uri) {
        return simpleRequest(new HttpDelete(uri));
    }

    private static JSONObject post(String uri,
            ArrayList<NameValuePair> parameters) {
        return enclosingRequest(new HttpPost(uri), parameters);
    }

    private static JSONObject put(String uri,
            ArrayList<NameValuePair> parameters) {
        return enclosingRequest(new HttpPut(uri), parameters);
    }

    public static boolean validResponse(JSONObject response) {
        try {
            if (response == null) {
                return false;
            } else {
                return response.getString("s").equals("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getResponseMessage(JSONObject response) {
        String message = null;
        try {
            if (response != null) {
                message = response.getString("m");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    public static JSONObject getResponseContent(JSONObject response) {
        JSONObject content = null;

        if (validResponse(response)) {
            try {
                content = response.getJSONObject("r");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return content;
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
            Handler handler, final Context context) {
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_EMAIL, username));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

        JSONObject json_response = post(AUTH_URI, params);

        JSONObject r = getResponseContent(json_response);
        if (r == null) {
            sendResultToAuthenticationActivity(false, handler, context);
        } else {
            SharedPreferences.Editor preferences_editor = PromGame
                    .getAppContext()
                    .getSharedPreferences(Constants.USER_PREFERENCES,
                            Context.MODE_PRIVATE).edit();
            try {
                preferences_editor.putString(Constants.PREF_AUTH_TOKEN,
                        r.getString("token"));
                preferences_editor.putBoolean(Constants.PREF_LOGGED_IN, true);
                preferences_editor.commit();
                sendResultToAuthenticationActivity(true, handler, context);

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
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
                authenticate(username, password, handler, context);
            }
        };

        return NetworkUtilities.performOnBackgroundThread(runnable);
    }

    public static User fetchUserInformation(String auth_token, Date last_updated) {
        String uri = USER_URI + "?token=" + auth_token;
        JSONObject json_response = get(uri);

        User user = null;

        JSONObject r = getResponseContent(json_response);
        user = User.valueOf(r);

        return user;
    }

    public static Quiz fetchQuizGame(final String promotionid, String auth_token) {
        String uri = PROMOTION_URI + "/" + promotionid + QUIZ_URI + "/?token="
                + auth_token;

        JSONObject response = get(uri);

        Quiz new_quiz = Quiz.valueOf(response);

        return new_quiz;
    }

    private static boolean register(String name, String username,
            String password, String birthday, String pc4, String pc3,
            String house_number, Handler handler, final Context context) {
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_EMAIL, username));
        params.add(new BasicNameValuePair(PARAM_NAME, name));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        params.add(new BasicNameValuePair(PARAM_BIRTHDAY, birthday));
        params.add(new BasicNameValuePair(PARAM_PC4, pc4));
        params.add(new BasicNameValuePair(PARAM_PC3, pc3));
        params.add(new BasicNameValuePair(PARAM_HOUSE_NUMBER, house_number));

        JSONObject json_response = post(USER_URI, params);
        if (validResponse(json_response)) {
            attemptAuth(username, password, handler, context);
            Log.v(TAG, "Successful register");

            return true;
        } else {

            return false;
        }
    }

    public static Thread attemptRegister(final String name, final String email,
            final String password, final String birthday, final String pc4,
            final String pc3, final String house_number, final Handler handler,
            final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                register(name, email, password, birthday, pc4, pc3,
                        house_number, handler, context);
            }
        };

        return NetworkUtilities.performOnBackgroundThread(runnable);
    }

    public static void submitAnswersQuizGame(String promotion_id,
            String auth_token, ArrayList<Question> arrayList,
            final Context context) {

        String userpromotionid = "1";
        // TODO: change
        String uri = PROMOTION_URI + "/" + promotion_id + "/quizgame/"
                + userpromotionid + ".json";

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", auth_token));

        for (int j = 0; j < arrayList.size(); j++) {
            params.add(new BasicNameValuePair(String.format("answer[%d]",
                    arrayList.get(j).getId()), String.format("%d", arrayList
                    .get(j).getAnswered())));
        }

        JSONObject json_response = post(uri, params);

        Log.i(TAG, json_response.toString());
        if (validResponse(json_response)) {
            try {
                String won = getResponseContent(json_response).getString("won");
                String correct = getResponseContent(json_response).getString(
                        "correct");

                // TODO: change
                Toast.makeText(
                        context,
                        "Ganhaste: " + won + " com: " + correct
                                + " respostas correctas!", Toast.LENGTH_SHORT)
                        .show();

            } catch (JSONException e) {
                Log.i(TAG, "Error to get response: "
                        + getResponseContent(json_response));
            }
        } else {
            Log.i(TAG, "Error to get response: "
                    + getResponseContent(json_response));
        }
    }
}
