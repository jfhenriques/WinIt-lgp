package pt.techzebra.winit;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utilities {
	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean hasGPSConnections(Context context) {
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER ))
			return false;
		return true;
	}
	
	public static void showToast(Context context, String text) {
	    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToast(Context context, int res_id) {
	    Toast.makeText(context, res_id, Toast.LENGTH_SHORT).show();
	}
	
	public static void addActivityAnimations(Activity activity){
		activity.overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);
	}
}
