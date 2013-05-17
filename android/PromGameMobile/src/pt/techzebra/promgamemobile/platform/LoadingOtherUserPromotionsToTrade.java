package pt.techzebra.promgamemobile.platform;

import java.util.ArrayList;

import com.actionbarsherlock.R.style;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.Utilities;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.ui.PromotionsActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public abstract class LoadingOtherUserPromotionsToTrade extends AsyncTask<Void, Void, ArrayList<Promotion>> {

	ArrayList<Promotion> promos = null;
	String auth_token;
	private ProgressDialog progressDialog;
	Context mContext = null;
	
	public LoadingOtherUserPromotionsToTrade(Context mContext){
		this.mContext = mContext;
	}

	@Override
	protected ArrayList<Promotion> doInBackground(Void... params) {
		try {
			SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
			auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
			promos = NetworkUtilities.fetchOtherUsersTradings(auth_token);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		if(promos != null)
			return promos;
		return null;
	}
	
	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
		progressDialog.setMessage("Loading promotions...");
		progressDialog.show();
	}
	
	@Override
	protected void onPostExecute(ArrayList<Promotion> result){
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result != null){
			callMainWindow(result);
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
	
	public abstract void callMainWindow(ArrayList<Promotion> result);

}