package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

public abstract class FetchPromotionsTask extends AsyncTask<Void, Void, ArrayList<Promotion>> {
    public static final int AVAILABLE_PROMOTIONS = 1;
    public static final int OTHER_USERS_PROMOTIONS = 2;
    public static final int OWNED_PROMOTIONS = 3;
    
    private ArrayList<Promotion> promotions_ = null;
	private ProgressDialog progress_dialog_;
	private Context context_ = null;
	private int option_;

	public FetchPromotionsTask(Context context, int option){
		context_ = context;
		option_ = option;
	}

	@Override
	protected ArrayList<Promotion> doInBackground(Void... params) {
		String auth_token = WinIt.getAuthToken();
		switch (option_) {
		    case AVAILABLE_PROMOTIONS:
		        promotions_ = NetworkUtilities.fetchAvailablePromotions(auth_token);
		        break;
		    case OTHER_USERS_PROMOTIONS:
		        promotions_ = NetworkUtilities.fetchOtherUsersTradings(auth_token);
		        break;
		    case OWNED_PROMOTIONS:
		        break;
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
		if(result != null){
			try {
			    callMainWindow(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(!Utilities.hasInternetConnection(context_)) {
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

    public abstract void callMainWindow(ArrayList<Promotion> result);

}

