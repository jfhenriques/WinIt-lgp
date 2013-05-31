package pt.techzebra.winit;

import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.AuthenticationActivity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.google.android.gcm.GCMRegistrar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class WinIt extends Application {
	private static Context context_;

	private static String auth_token_ = null;

	@Override
	public void onCreate() {
		super.onCreate();

		WinIt.context_ = getApplicationContext();
	}

	public static Context getAppContext() {
		return context_;
	}

	public static String getAuthToken() {
		if (auth_token_ == null) {
			SharedPreferences preferences = context_.getSharedPreferences(
					Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
			auth_token_ = preferences
					.getString(Constants.PREF_AUTH_TOKEN, null);
		}

		return auth_token_;
	}

	public static void clearUserData() {

		SharedPreferences preferences = context_.getSharedPreferences(
				Constants.USER_PREFERENCES, Context.MODE_PRIVATE);

		auth_token_ = preferences
				.getString(Constants.PREF_AUTH_TOKEN, null);

		SharedPreferences.Editor preferences_editor = context_
				.getSharedPreferences(Constants.USER_PREFERENCES,
						Context.MODE_PRIVATE).edit();

		auth_token_ = null;
		preferences_editor.remove(Constants.PREF_LOGGED_IN);
		preferences_editor.remove(Constants.PREF_FB_LOGGED_IN);
		preferences_editor.remove(Constants.PREF_AUTH_TOKEN);
		preferences_editor.remove(Constants.GCM_REG_ID);
		preferences_editor.commit();


		Session sess = AuthenticationActivity
				.forceGetActiveSession(getAppContext());

		if (sess != null) {
			sess.closeAndClearTokenInformation();
			sess.close();

			Session.setActiveSession(null);
		}
	}

	public static void logOut(Context context) {
		GCMRegistrar.unregister(context);
		clearUserData();
		Intent intent = new Intent(context, AuthenticationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
		if(context instanceof SherlockFragmentActivity)
			((SherlockFragmentActivity) context).finish();
		
		else
			((SherlockActivity) context).finish();
	
	}
}
