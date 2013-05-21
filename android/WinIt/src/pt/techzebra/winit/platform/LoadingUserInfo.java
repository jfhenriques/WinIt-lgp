package pt.techzebra.winit.platform;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.User;
import pt.techzebra.winit.ui.ProfileActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadingUserInfo extends AsyncTask<Void, Void, User> {
	private Context context_ = null;
	private ProgressDialog progress_dialog_;

	public LoadingUserInfo(Context context){
		context_ = context;
	}

	@Override
	protected User doInBackground(Void... params) {
	    String auth_token = WinIt.getAuthToken();
		User user = NetworkUtilities.fetchUserInformation(auth_token, null);	
		return user;
	}

	protected void onPreExecute(){
		super.onPreExecute();
		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}

	@Override
	protected void onPostExecute(User result){
		super.onPostExecute(result);
		progress_dialog_.dismiss();
		if(result == null) {
			if (!Utilities.hasInternetConnection(context_)) {
			    Utilities.showInternetConnectionAlert(context_);
			}
		} else {
		    Intent intent = new Intent(context_, ProfileActivity.class);
            intent.putExtra("User", result);
            context_.startActivity(intent);
		}
	}
}