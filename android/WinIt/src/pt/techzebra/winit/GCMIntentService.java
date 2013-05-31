package pt.techzebra.winit;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.GCMUtils;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.DashboardActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private SharedPreferences preferences_ ;
	private static String auth_token;
	private static String TAG = "GCMIntent";
	public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

	public GCMIntentService(){
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
        mNotificationManager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
        
        PendingIntent contentIntent = PendingIntent.getActivity(arg0, 0,
            new Intent(arg0, DashboardActivity.class), 0);
        
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(arg0)
            .setSmallIcon(R.drawable.ic_trading)
            .setContentTitle("WinIt")
            .setContentText("New trading proposal");
       mBuilder.setContentIntent(contentIntent);
       mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
