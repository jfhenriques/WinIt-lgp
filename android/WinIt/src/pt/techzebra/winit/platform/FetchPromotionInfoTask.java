package pt.techzebra.winit.platform;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.PromotionActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.actionbarsherlock.R.style;

public class FetchPromotionInfoTask extends AsyncTask<Integer, Void, Promotion> {
	Promotion promotion_ = null;
	private ProgressDialog progress_dialog_;
	Context context_ = null;
	int switcher_;

	public FetchPromotionInfoTask(Context context, int switcher){
		context_ = context;
		switcher_ = switcher;
	}

	@Override
	protected Promotion doInBackground(Integer... params) {
		String auth_token = WinIt.getAuthToken();
		promotion_ = NetworkUtilities.fetchPromotionInformation(Integer.toString(params[0]), auth_token);
		return promotion_;
	}

	protected void onPreExecute(){
		super.onPreExecute();
		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
		progress_dialog_.setMessage("Loading promotion information...");
		progress_dialog_.show();
	}

	@Override
	protected void onPostExecute(Promotion result){
		super.onPostExecute(result);
		progress_dialog_.dismiss();
		if(result != null){
			try {
				Intent i;
				switch (switcher_) {
				case 1:
					i = new Intent(context_, PromotionActivity.class);
					i.putExtra("Promotion", promotion_);
					i.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PromotionAffinity.AVAILABLE_PROMOTION);
					context_.startActivity(i);
					break;
				case 2: 
				    i = new Intent(context_, PromotionActivity.class);
				    i.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PromotionAffinity.TRADEABLE_PROMOTION);
					i.putExtra("Promotion", promotion_);
					context_.startActivity(i);

				default:
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(!Utilities.hasInternetConnection(context_)){
				AlertDialog.Builder builder = new AlertDialog.Builder(context_);
				builder.setMessage("No Internet connection. Do you wish to open Settings?");
				builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   context_.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
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
