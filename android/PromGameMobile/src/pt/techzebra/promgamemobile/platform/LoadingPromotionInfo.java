package pt.techzebra.promgamemobile.platform;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.Utilities;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.ui.PromotionActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.actionbarsherlock.R.style;

public class LoadingPromotionInfo extends AsyncTask<Integer, Void, Promotion> {
	Promotion p_ = null;
	String auth_token;
	private ProgressDialog progressDialog;
	Context mContext = null;

	public LoadingPromotionInfo(Context c){
		mContext = c;
	}

	@Override
	protected Promotion doInBackground(Integer... params) {
		try {
			SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
			auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
			p_ = NetworkUtilities.fetchPromotionInformation(Integer.toString(params[0]), auth_token);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		if(p_ != null)
			return p_;
		return null;
	}

	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
		progressDialog.setMessage("Loading promotion information...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Promotion result){
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result != null){
			try {
				Intent i = new Intent(mContext, PromotionActivity.class);
				i.putExtra("Promotion", p_);
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
