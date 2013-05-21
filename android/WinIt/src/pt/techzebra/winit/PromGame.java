package pt.techzebra.winit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PromGame extends Application {
	private static Context context_;
	
	private static String auth_token_ = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		PromGame.context_ = getApplicationContext();
	}
	
	public static Context getAppContext() {
		return context_;
	}
	
	public static String getAuthToken() {
	    if (auth_token_ == null) {
	        SharedPreferences preferences = context_.getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
	        auth_token_ = preferences.getString(Constants.PREF_AUTH_TOKEN, null);
	    }
	    
	    return auth_token_;
	}
}
