package pt.techzebra.winit;

import android.app.Application;
import android.content.Context;

public class PromGame extends Application {
	private static Context context_;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		PromGame.context_ = getApplicationContext();
	}
	
	public static Context getAppContext() {
		return context_;
	}
}
