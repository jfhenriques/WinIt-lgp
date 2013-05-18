package pt.techzebra.winit.platform;

import com.actionbarsherlock.R.style;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.PromGame;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.User;
import pt.techzebra.winit.ui.ProfileActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class LoadingUserInfo extends AsyncTask<Void, Void, User> {
	User user_ = null;
	String auth_token;
	private ProgressDialog progressDialog;
	Context mContext = null;

	public LoadingUserInfo(Context c){
		mContext = c;
	}

	@Override
	protected User doInBackground(Void... params) {
		try {
			SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
			auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
			user_ = NetworkUtilities.fetchUserInformation(auth_token, null);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		if(user_ != null)
			return user_;
		return null;
	}

	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
		progressDialog.setMessage("Loading user information...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(User result){
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result != null){
			try {
				Intent i = new Intent(mContext, ProfileActivity.class);
				i.putExtra("User", user_);
				mContext.startActivity(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(!Utilities.hasInternetConnection(mContext)){
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("No Internet connection. Do you wish to open Settings?");
				builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
			           }
			       });
			builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			           }
			       });
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
}