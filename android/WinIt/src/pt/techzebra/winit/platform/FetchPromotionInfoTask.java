package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.PromotionActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.actionbarsherlock.R.style;

public abstract class FetchPromotionInfoTask extends AsyncTask<Integer, Void, Promotion> {
	private static final String TAG = "FetchPromotionInfoTask";
	
    Promotion promotion_ = null;
	private ProgressDialog progress_dialog_;
	Context context_ = null;
	int affinity_;

	public FetchPromotionInfoTask(Context context, int affinity){
		context_ = context;
		affinity_ = affinity;
	}

	@Override
	protected Promotion doInBackground(Integer... params) {
		String auth_token = WinIt.getAuthToken();
		promotion_ = NetworkUtilities.fetchPromotionInformation(Integer.toString(params[0]), auth_token);
		Log.d(TAG, Boolean.toString(promotion_ == null));
		return promotion_;
	}

	protected void onPreExecute() {
		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}

	@Override
	protected void onPostExecute(Promotion result) {
		if(result != null){			
			callMainWindow(result);

		} else {
			Utilities.requireInternetConnection(context_);
		}
		
		progress_dialog_.dismiss();
	}

	public abstract void callMainWindow(Promotion result);
}
