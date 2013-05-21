package pt.techzebra.winit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

	public static void requireInternetConnection(final Context context) {
	    if (!hasInternetConnection(context)) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("No Internet connection. Do you wish to open Settings?");
            builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                   }
               });
        builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
            AlertDialog dialog = builder.create();
            dialog.show();
	    }
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
	
	public static String convertUnixTimestamp(long millis) {
	    SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
	    return date_format.format(new Date(millis * 1000));
	}
}
