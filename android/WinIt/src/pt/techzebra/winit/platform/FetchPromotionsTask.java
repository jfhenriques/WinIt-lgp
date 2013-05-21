package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FetchPromotionsTask extends AsyncTask<Void, Void, ArrayList<Promotion>> {
    private static final String TAG = "FetchPromotionsTask";
    
    public static interface AsyncResponse {
        void processFinish(ArrayList<Promotion> result); 
    }
    
    public static final int AVAILABLE_PROMOTIONS = 1;
    public static final int OTHER_USERS_PROMOTIONS = 2;
    public static final int OWNED_PROMOTIONS = 3;
    
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
		if (result == null && !Utilities.hasInternetConnection(context_)) {
		    Utilities.showInternetConnectionAlert(context_);
		} else {
		    delegate_.processFinish(result);
		}
	}
}

