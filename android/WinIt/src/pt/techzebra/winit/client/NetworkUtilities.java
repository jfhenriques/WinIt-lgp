package pt.techzebra.winit.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.games.quiz.QuizActivity;
import pt.techzebra.winit.ui.AuthenticationActivity;
import pt.techzebra.winit.ui.EditProfileActivity;
import pt.techzebra.winit.ui.SignupActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * Provides utility methods for communicating with the server.
 */
public class NetworkUtilities {
	private static final String TAG = "NetworkUtilities";

	// User
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_BIRTHDAY = "birth";
	public static final String PARAM_PC4 = "cp4";
	public static final String PARAM_PC3 = "cp3";
	public static final String PARAM_HOUSE_NUMBER = "portaAndar";
	public static final String PARAM_UPDATED = "timestamp";
	public static final String PARAM_AUTH_TOKEN = "token";

	// Promotion
	public static final String PARAM_PROMO_ID = "pid";
	public static final String PARAM_PROMO_ACTIVE = "active";
	public static final String PARAM_PROMO_NAME = "name";
	public static final String PARAM_PROMO_INIT_DATE = "init_date";
	public static final String PARAM_PROMO_END_DATE = "end_date";
	public static final String PARAM_PROMO_GRAND_LIMIT = "grand_limit";
	public static final String PARAM_PROMO_USER_LIMIT = "user_limit";
	public static final String PARAM_PROMO_VALID_COORD = "valid_coord";
	public static final String PARAM_PROMO_VALID_COORD_RADIUS = "valid_coord_radius";
	public static final String PARAM_PROMO_TRANSFERABLE = "transferable";
	public static final String PARAM_PROMO_WIN_POINTS = "win_points";
	public static final String PARAM_PROMO_FUNC_TYPE = "func_type";
	public static final String PARAM_PROMO_RETAILER_ID = "rid";
	public static final String PARAM_PROMO_TYPE_ID = "ptid";

	public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	public static final String HOST = "tlantic.techzebra.pt";
	public static final String SCHEME = "https";
	public static final int PORT = 443;
	public static final String BASE_URL = "https://tlantic.techzebra.pt/api";

	public static final String AUTH_URI = BASE_URL + "/session.json";
	public static final String USER_URI = BASE_URL + "/user.json";
	public static final String PROMOTION_URI = BASE_URL + "/promotion";
	public static final String TRADING_URI = BASE_URL + "/trading.json";

	public static final String MY_PROMOTIONS_URI = BASE_URL
			+ "/user/promotions/won.json";
	public static final String MY_PROMOTIONS_IN_TRADING_URI = BASE_URL
			+ "/user/promotions/trading.json";
	public static final String MY_PROMOTIONS_TO_TRADE_URI = BASE_URL
			+ "/user/promotions/tradable.json";
	public static final String ADDRESSES_URI = BASE_URL + "/address";
	public static final String MY_BADGES_URI = BASE_URL + "/user/badges.json";
	public static final String QUIZ_URI = "/quizgame.json";

	private static final String PARAM_ADDRESS_ID = "adid";

	private static final String PARAM_ADDRESS_2 = "address2";

	private static HttpClient http_client_;
	private static HttpHost http_host_;

	static private HttpClient sslClient(HttpClient client) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        };
        
        SSLContext ssl_context = SSLContext.getInstance("TLS");
        ssl_context.init(null, new TrustManager[]{ tm }, null);

        SSLSocketFactory ssl_socket_factory = new CustomSSLSocketFactory(ssl_context);

        ssl_socket_factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager connection_manager = client.getConnectionManager();
        SchemeRegistry scheme_registry = connection_manager.getSchemeRegistry();
        scheme_registry.register(new Scheme("https", ssl_socket_factory, 443));
        
        return new DefaultHttpClient(connection_manager, client.getParams());
	}
	
	/**
	 * Configures the HttpClient to connect to the URL provided.
	 */
	public static void maybeCreateHttpClient() {
		if (http_client_ == null) {
		    http_client_ = new DefaultHttpClient();
		    try {
                http_client_ = sslClient(http_client_);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}

	public static void maybeCreateHttpHost() {
		if (http_host_ == null) {
			http_host_ = new HttpHost(HOST, PORT, SCHEME);
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
		maybeCreateHttpHost();

		HttpResponse response;
		try {
			response = http_client_.execute(http_host_, request);
			int status_code = response.getStatusLine().getStatusCode();
			Log.d(TAG, "status code: " + status_code);
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

	public static JSONArray getResponseContentArray(JSONObject response) {
		JSONArray content = null;

		if (validResponse(response)) {
			try {
				content = response.getJSONArray("r");
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
			SharedPreferences.Editor preferences_editor = WinIt
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

	private static void sendAddressesToSignupActivity(final String[] addresses,
			final ArrayList<Integer> addresses_ids, final Handler handler,
			final Context context) {
		if (handler == null || context == null) {
			return;
		}

		handler.post(new Runnable() {
			@Override
			public void run() {
				((SignupActivity) context).onGetAddressesResult(addresses,
						addresses_ids);
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

	// public static Thread attemptFetchQuizGame(final String promotionid,
	// final String auth_token, final Handler handler,
	// final Context context) {
	//
	// final Runnable runnable = new Runnable() {
	// @Override
	// public void run() {
	// fetchQuizGame(promotionid, auth_token, handler, context);
	// }
	// };
	//
	// return NetworkUtilities.performOnBackgroundThread(runnable);
	// }

	public static Quiz fetchQuizGame(String promotionid, String auth_token) {
		String uri = PROMOTION_URI + "/" + promotionid + QUIZ_URI + "?token="
				+ auth_token;

		JSONObject response = get(uri);

		Quiz quiz = Quiz.valueOf(response);

		return quiz;
	}

	private static boolean register(String name, String email, String password,
			String birthday, String address_id, String address_2,
			Handler handler, final Context context) {
		final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME, name));
		params.add(new BasicNameValuePair(PARAM_EMAIL, email));
		params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
		params.add(new BasicNameValuePair(PARAM_BIRTHDAY, birthday));
		params.add(new BasicNameValuePair(PARAM_ADDRESS_ID, address_id));
		params.add(new BasicNameValuePair(PARAM_ADDRESS_2, address_2));

		JSONObject json_response = post(USER_URI, params);

		Log.d("RESPOSTA", getResponseMessage(json_response));
		if (validResponse(json_response)) {
			// attemptAuth(email, password, handler, context);
			Log.v(TAG, "Successful register");
			return true;
		} else {

			return false;
		}
	}

	public static void sendSignupResultToSignupActivity(final boolean result,
			final Handler handler, final Context context) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				((SignupActivity) context).onSignupResult(result);
			}
		});
	}

	public static Thread attemptRegister(final String name, final String email,
			final String password, final String birthday,
			final String address_id, final String address_2,
			final Handler handler, final Context context) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				boolean success = register(name, email, password, birthday,
						address_id, address_2, handler, context);
				if (success) {
					final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair(PARAM_EMAIL, email));
					params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

					JSONObject json_response = post(AUTH_URI, params);

					JSONObject r = getResponseContent(json_response);
					if (r == null) {
						sendSignupResultToSignupActivity(false, handler,
								context);
					} else {
						SharedPreferences.Editor preferences_editor = WinIt
								.getAppContext()
								.getSharedPreferences(
										Constants.USER_PREFERENCES,
										Context.MODE_PRIVATE).edit();
						try {
							preferences_editor.putString(
									Constants.PREF_AUTH_TOKEN,
									r.getString("token"));
							preferences_editor.putBoolean(
									Constants.PREF_LOGGED_IN, true);
							preferences_editor.commit();
							sendSignupResultToSignupActivity(true, handler,
									context);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {
					sendSignupResultToSignupActivity(false, handler, context);
				}

			}
		};

		return NetworkUtilities.performOnBackgroundThread(runnable);
	}

	public static Thread submitAnswersQuizGame(final String promotion_id,
			final String auth_token, final String userpromotionid,
			final ArrayList<Question> arrayList, final Handler handler,
			final Context context) {

		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				submitAnswersAction(promotion_id, auth_token, userpromotionid,
						arrayList, handler, context);
			}
		};

		return NetworkUtilities.performOnBackgroundThread(runnable);

	}

	public static void submitAnswersAction(String promotion_id,
			String auth_token, String user_promotion_id,
			ArrayList<Question> arrayList, Handler handler, Context context) {
		// TODO: Be careful when user go back without conclude quiz
		String uri = PROMOTION_URI + "/" + promotion_id + "/quizgame/"
				+ user_promotion_id + ".json";

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
			sendResponseToQuizGameActivity(getResponseContent(json_response),
					handler, context);
		} else {
			Log.i(TAG, "Error to get response: "
					+ getResponseMessage(json_response));
		}

	}

	public static String getUserPromotionId(final String promotion_id,
			final String auth_token) {
		String userpromotionid = null;
		String uritemp = PROMOTION_URI + "/" + promotion_id + "/enroll.json";
		ArrayList<NameValuePair> paramstemp = new ArrayList<NameValuePair>();
		paramstemp.add(new BasicNameValuePair("token", auth_token));
		JSONObject json_response_temp = post(uritemp, paramstemp);
		Log.i(TAG, json_response_temp.toString());
		if (validResponse(json_response_temp)) {
			json_response_temp = getResponseContent(json_response_temp);
			try {
				userpromotionid = json_response_temp.getString("upid");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return userpromotionid;
	}

	private static void sendResponseToQuizGameActivity(
			final JSONObject responseContent, Handler handler,
			final Context context) {
		if (handler == null || context == null) {
			return;
		}

		handler.post(new Runnable() {
			@Override
			public void run() {
				((QuizActivity) context)
						.getResultSubmitedAnswers(responseContent);
			}
		});
	}

	public static Promotion fetchPromotionInformation(String promotionid,
			String auth_token) {

		String uri = PROMOTION_URI + "/" + promotionid + ".json?token="
				+ auth_token;
		JSONObject response = get(uri);
		JSONObject r = getResponseContent(response);
		Promotion promo = Promotion.valueOf(r);

		return promo;
	}

	public static void getAddresses(String pc4, String pc3, Handler handler,
			final Context context) {
		String uri = ADDRESSES_URI + "/" + pc4 + "/" + pc3 + ".json";
		JSONObject response = get(uri);

		// 5000 402

		JSONArray r = getResponseContentArray(response);
		if (r == null) {
			sendAddressesToSignupActivity(null, null, handler, context);
			return;
		}

		JSONObject address;
		String address_temp;
		ArrayList<String> addresses = new ArrayList<String>();
		ArrayList<Integer> addresses_ids = new ArrayList<Integer>();
		for (int i = 0, size = r.length(); i < size; ++i) {
			try {
				address = r.getJSONObject(i);
				address_temp = address.getString("address") + ", "
						+ address.getString("locality") + ", "
						+ address.getString("district");
				addresses.add(address_temp);
				addresses_ids.add(address.getInt("adid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			Log.d("TAG", r.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String[] addresses_array = Arrays.copyOf(addresses.toArray(),
				addresses.size(), String[].class);
		sendAddressesToSignupActivity(addresses_array, addresses_ids, handler,
				context);
	}

	public static Thread attemptGetAddresses(final String pc4,
			final String pc3, final Handler handler, final Context context) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				getAddresses(pc4, pc3, handler, context);
			}
		};

		return NetworkUtilities.performOnBackgroundThread(runnable);
	}

	public static ArrayList<Promotion> fetchAvailablePromotions(String token) {
		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		String uri = PROMOTION_URI + ".json?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);

		if (r == null) {
			return null;
		}

		for (int i = 0; i < r.length(); i++) {
			try {
				promos.add(Promotion.valueOf(r.getJSONObject(i)));
			} catch (JSONException e) {
				return null;
			}
		}

		return promos;
	}

	public static ArrayList<Promotion> fetchOtherUsersTradings(String token) {

		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		String uri = TRADING_URI + "?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);

		if (r == null) {
			return null;
		}

		for (int i = 0; i < r.length(); i++) {
			try {
				promos.add(Promotion.valueOf(r.getJSONObject(i)));
			} catch (JSONException e) {
				return null;
			}
		}
		return promos;

	}

	public static ArrayList<Promotion> fetchMyPromotions(String token) {

		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		// TODO
		String uri = MY_PROMOTIONS_URI + "?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);
		for (int i = 0; i < r.length(); i++) {
			try {
				promos.add(Promotion.valueOfTrading(r.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
				return promos;
			}
		}
		return promos;
	}

	public static ArrayList<Promotion> fetchMyPromotionsTradeable(String token) {

		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		String uri = MY_PROMOTIONS_TO_TRADE_URI + "?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);
		for (int i = 0; i < r.length(); i++) {
			try {
				promos.add(Promotion.valueOf(r.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return promos;

	}

	public static ArrayList<Promotion> fetchMyPromotionsInTrading(String token) {

		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		String uri = MY_PROMOTIONS_IN_TRADING_URI + "?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);
		for (int i = 0; i < r.length(); i++) {
			try {
				promos.add(Promotion.valueOf(r.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return promos;

	}

	public static ArrayList<Badge> fetchMyBadges(String token) {
		ArrayList<Badge> badges = new ArrayList<Badge>();
		String uri = MY_BADGES_URI + "?token=" + token;
		JSONObject response = get(uri);
		JSONArray r = getResponseContentArray(response);
		for (int i = 0; i < r.length(); i++) {
			try {
				badges.add(Badge.valueOf(r.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return badges;
	}

	public static Thread attemptEditProfile(final String auth_token,
			final String name, final String email, final String new_password,
			final String old_password, final String birthday,
			final String address_id, final String address_2,
			final Handler handler, final Context context) {

		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				editProfile(auth_token, name, email, new_password,
						old_password, birthday, address_id, address_2, handler,
						context);
			}
		};
		return NetworkUtilities.performOnBackgroundThread(runnable);

	}

	public static void editProfile(String auth_token, String name,
			String email, String new_password, String old_password,
			String birthday, String address_id, String address_2,
			Handler handler, Context context) {
		String uri = USER_URI + "?token=" + auth_token;

		ArrayList<NameValuePair> profile_edited = new ArrayList<NameValuePair>();
		profile_edited.add(new BasicNameValuePair("name", name));
		profile_edited.add(new BasicNameValuePair("email", email));
		profile_edited.add(new BasicNameValuePair("password", new_password));
		profile_edited.add(new BasicNameValuePair("password", old_password));
		profile_edited.add(new BasicNameValuePair("birth", birthday));
		/*
		 * profile_edited.add(new BasicNameValuePair("adid", address_id));
		 * profile_edited.add(new BasicNameValuePair("address2", address_2));
		 */

		JSONObject response = put(uri, profile_edited);
		String r = getResponseMessage(response);

		sendResponseToEditProfileActivity(r, handler, context);
	}

	public static void sendResponseToEditProfileActivity(final String r,
			Handler handler, final Context context) {
		if (handler == null || context == null) {
			return;
		}

		handler.post(new Runnable() {
			@Override
			public void run() {
				((EditProfileActivity) context).getResponse(r);
			}
		});

	}
}
