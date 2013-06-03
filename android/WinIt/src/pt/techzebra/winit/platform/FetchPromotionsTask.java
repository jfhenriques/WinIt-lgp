package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.DashboardActivity;
import pt.techzebra.winit.ui.PromotionsActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

public class FetchPromotionsTask extends AsyncTask<Void, Void, ArrayList<Promotion>> {
	private static final String TAG = "FetchPromotionsTask";

	public static interface AsyncResponse {
		void processFinish(ArrayList<Promotion> result); 
	}

	public static final int PLAYABLE_PROMOTIONS = 1;
//	public static final int PROPOSABLE_PROMOTIONS = 2;

	private ArrayList<Promotion> promotions_ = null;
	private ProgressDialog progress_dialog_;
	private Context context_ = null;
	private int option_;

	private AsyncResponse delegate_ = null;

	public FetchPromotionsTask(Context context, int option){
		context_ = context;
		option_ = option;
	}

	public void setDelegate(AsyncResponse delegate) {
		delegate_ = delegate;
	}

	@Override
	protected ArrayList<Promotion> doInBackground(Void... params) {
		String auth_token = WinIt.getAuthToken();

		switch (option_) {
		case PLAYABLE_PROMOTIONS:
			promotions_ = NetworkUtilities.fetchAvailablePromotions(auth_token);
			break;
//		case PROPOSABLE_PROMOTIONS:
//			promotions_ = NetworkUtilities.fetchProposableTradings();
//			break;
		}
		return promotions_;
	}

	protected void onPreExecute(){
		super.onPreExecute();

		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}

	@Override
	protected void onPostExecute(ArrayList<Promotion> result){
		super.onPostExecute(result);

		progress_dialog_.dismiss();
		if(Utilities.hasInternetConnection(context_)){
			if (result == null ) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context_);
				builder.setMessage("No promotions available to show!");
				builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(context_, DashboardActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context_.startActivity(intent);
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				delegate_.processFinish(result);
			}
		}
		else{
			Utilities.showInternetConnectionAlert(context_);
		}
	}
}

