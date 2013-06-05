package pt.techzebra.winit;

import java.io.InputStream;

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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static SharedPreferences preferences_;
	private static String auth_token;
	private static String TAG = "GCMIntent";
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager = null;
	NotificationCompat.Builder builder;
	
	private static int not_width = -1,
					   not_height = -1;
	
	private static Integer notCounter = 0;

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
	
	private SharedPreferences getPreferences()
	{
		return getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1)
	{
		Log.d(TAG, "REGISTERED ON SERVICE");
		preferences_ = this.getPreferences();
		SharedPreferences.Editor editor = preferences_.edit();
		editor.putString(Constants.GCM_REG_ID, arg1);
		editor.commit();
		auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, null);
		NetworkUtilities.attemptRegisterGCMToken(auth_token, arg1);
		Log.i(TAG, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1)
	{
		Log.d(TAG, "UNREGISTERED OF SERVICE");
		NetworkUtilities.attemptUnRegisterGCMToken(auth_token, arg1);
	}

	
	private static void pushNotification(NotificationManager manager, NotificationCompat.Builder mBuilder)
	{
		Notification noti = mBuilder.build();
		
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		int notId;
		
		synchronized (notCounter) {
			notId = ++notCounter;
		}
		
		manager.notify(notId, noti);
	}
	
	private static int safeInt(Bundle b, String key, int d)
	{
		try {
			return Integer.valueOf( b.getString(key) );
		} catch(NumberFormatException e) {
			return d;
		}
	}
	
	private void generateNotification(Context arg0, Intent msg)
	{
		
		if ( arg0 == null || msg == null )
			return;
		
		if( preferences_ == null )
			preferences_ = this.getPreferences();

		Bundle extras = msg.getExtras();

		int uid = safeInt(extras, "uid", 0),
			uid_logged = preferences_.getInt(Constants.PREF_UID, 0);
			

		
		
		if (uid > 0 && uid == uid_logged) {
		
			int type = safeInt(extras, "type", 0);
			
			String name_my = extras.getString("name_my", ""),
					name_o = extras.getString("name_o", ""),
					image_o = extras.getString("image_o", null),
					message = "";

			
			if(type == 1)
				message = "Recebeu '" + name_o + "' em troca de '" + name_my + "'";

			else if (type == 2)
				message = "Rejeitada a troca de '" + name_o + "' por '" + name_my + "'";
			
			else if (type == 3)
				message = "Proposta de troca de '" + name_o + "' por '" + name_my + "'";
			
			else
			{
				Log.d(TAG, "Erro!");
				return;
			}
			

				
			if( mNotificationManager == null )
				mNotificationManager = (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Intent intent = new Intent(arg0, DashboardActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent contentIntent = PendingIntent.getActivity(arg0, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			
			final NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(arg0)
						.setSmallIcon(R.drawable.ic_trading)
						.setContentTitle("WinIt")
						.setContentIntent(contentIntent)
						.setContentText(message)
						//.addAction(android.R.drawable.arrow_up_float, "Call", contentIntent)
						.setDefaults(Notification.DEFAULT_SOUND | 
										Notification.DEFAULT_VIBRATE | 
										Notification.DEFAULT_LIGHTS);
			
			if( image_o == null )
				pushNotification(mNotificationManager, mBuilder);
			
			else
				(new AsyncTask<String, Void, Bitmap>() {
	
					@Override
					protected Bitmap doInBackground(String... params)
					{
						try {
							InputStream in = new java.net.URL(params[0]).openStream();
							return BitmapFactory.decodeStream(in);
						} catch (Exception e) { }
						
						return null;
					}

					@Override
					protected void onPostExecute(Bitmap result)
					{
						if( result != null )
						{
							if( not_width < 0 || not_height < 0 )
							{
								Resources res = getResources();
								
								not_height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
								not_width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
							}
							
							mBuilder.setLargeIcon(Bitmap.createScaledBitmap(result, not_width, not_height, false) );
						}
						
						pushNotification(mNotificationManager, mBuilder);
					}
					
				}).execute(image_o);
			


			


		}
	}
}
