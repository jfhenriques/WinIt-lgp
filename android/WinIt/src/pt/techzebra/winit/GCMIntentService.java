package pt.techzebra.winit;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.GCMUtils;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.DashboardActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private SharedPreferences preferences_;
	private static String auth_token;
	private static String TAG = "GCMIntent";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GCMIntentService() {
		super(GCMUtils.SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d(TAG, "Error: " + arg1);
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		Log.d(TAG, "RECIEVED A MESSAGE");
		generateNotification(arg0, arg1);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Log.d(TAG, "REGISTERED ON SERVICE");
		preferences_ = getSharedPreferences(Constants.USER_PREFERENCES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences_.edit();
		editor.putString(Constants.GCM_REG_ID, arg1);
		editor.commit();
		auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, null);
		NetworkUtilities.attemptRegisterGCMToken(auth_token, arg1);
		Log.i(TAG, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.d(TAG, "UNREGISTERED OF SERVICE");
		NetworkUtilities.attemptUnRegisterGCMToken(auth_token, arg1);
	}

	private void generateNotification(Context arg0, Intent msg) {

		if (preferences_ == null || msg == null)
			return;

		Bundle extras = msg.getExtras();

		int uid_logged = preferences_.getInt(Constants.PREF_UID, 0);
		int uid = extras.getInt("uid", 0);
		int type = extras.getInt("type");
		String message = "";
		if(type == 1){
			message += "This promotion was accepted!";
		}else if (type == 2) {
			message += "This promotion was not accepted!";
		}else if (type == 3) {
			message += "New promotion was suggested!";
		}else{
			Log.d(TAG, "Erro!");
		}
		
		if (uid > 0 && uid == uid_logged) {
			
			
			mNotificationManager = (NotificationManager) arg0
					.getSystemService(Context.NOTIFICATION_SERVICE);

			PendingIntent contentIntent = PendingIntent.getActivity(arg0, 0,
					new Intent(arg0, DashboardActivity.class), 0);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					arg0).setSmallIcon(R.drawable.ic_trading)
					.setContentTitle("WinIt")
					.setContentText(message);
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		}
	}
}
