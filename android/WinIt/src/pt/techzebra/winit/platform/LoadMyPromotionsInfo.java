package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.MyPromotionsActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadMyPromotionsInfo extends AsyncTask<Void,Void,ArrayList<ArrayList<Promotion>>>{
	private ProgressDialog progress_dialog_;
	ArrayList<ArrayList<Promotion>> promotions = new ArrayList<ArrayList<Promotion>>();
	AlertDialog.Builder builder;
	Context mContext_ = null;
	String auth_token = null;

	public LoadMyPromotionsInfo(Context mContext){
		this.mContext_ = mContext;
		auth_token = WinIt.getAuthToken();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress_dialog_ = new ProgressDialog(mContext_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}
	@Override
	protected ArrayList<ArrayList<Promotion>> doInBackground(Void... params) {
		ArrayList<Promotion> temp = new ArrayList<Promotion>();
		temp = NetworkUtilities.fetchMyPromotionsInTrading(auth_token);
		promotions.add(temp);
		temp = NetworkUtilities.fetchMyPromotions(auth_token);
		promotions.add(temp);
		return promotions;
	}

	@Override
	protected void onPostExecute(ArrayList<ArrayList<Promotion>> result) {
		super.onPostExecute(result);
		progress_dialog_.dismiss();
		if(result == null) {
			if (!Utilities.hasInternetConnection(mContext_)) {
			    Utilities.showInternetConnectionAlert(mContext_);
			}
		} else {
		    Intent intent = new Intent(mContext_, MyPromotionsActivity.class);
            intent.putExtra("Promotions", result);
            mContext_.startActivity(intent);
		}
	}
}
