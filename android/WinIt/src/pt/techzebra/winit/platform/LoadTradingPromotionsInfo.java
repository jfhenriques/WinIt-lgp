package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.TradingActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadTradingPromotionsInfo extends AsyncTask<Void,Void,ArrayList<ArrayList<Promotion>>>{
	private ProgressDialog progress_dialog_;
	private ArrayList<ArrayList<Promotion>> promotions_ = new ArrayList<ArrayList<Promotion>>();
	private Context context_ = null;
	private String auth_token_ = null;

	public LoadTradingPromotionsInfo(Context context){
		context_ = context;
		auth_token_ = WinIt.getAuthToken();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}
	@Override
	protected ArrayList<ArrayList<Promotion>> doInBackground(Void... params) {
		ArrayList<Promotion> temp = new ArrayList<Promotion>();
		temp = NetworkUtilities.fetchProposableTradings(auth_token_);
		promotions_.add(temp);
		// TODO add to temp received proposals
		// TODO add to temp sent proposals
		return promotions_;
	}

	@Override
	protected void onPostExecute(ArrayList<ArrayList<Promotion>> result) {
		super.onPostExecute(result);
		progress_dialog_.dismiss();
		if(result == null) {
			if (!Utilities.hasInternetConnection(context_)) {
			    Utilities.showInternetConnectionAlert(context_);
			}
		} else {
		    Intent intent = new Intent(context_, TradingActivity.class);
            intent.putExtra(TradingActivity.KEY_EXTRA_PROMOTIONS, result);
            context_.startActivity(intent);
		}
	}
}
