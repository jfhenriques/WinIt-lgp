package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.PromotionActivity;
import pt.techzebra.winit.ui.PromotionsActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.actionbarsherlock.R.style;

public class LoadingAvailablePromotionsList extends AsyncTask<Void, Void, ArrayList<Promotion>> {
	ArrayList<Promotion> promos = null;
	String auth_token;
	private ProgressDialog progressDialog;
	Context mContext = null;

	public LoadingAvailablePromotionsList(Context c){
		mContext = c;
	}

	@Override
	protected ArrayList<Promotion> doInBackground(Void... params) {
		try {
			SharedPreferences preferences_ = WinIt.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
			auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
			promos = NetworkUtilities.fetchAvailablePromotions(auth_token);
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
		progressDialog.setMessage("Loading promotion information...");
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(ArrayList<Promotion> result){
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result != null){
			try {
				Intent i = new Intent(mContext, PromotionsActivity.class);
				i.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PromotionAffinity.UNOWNED_PROMOTION);
				i.putExtra("Promotions", promos);
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

