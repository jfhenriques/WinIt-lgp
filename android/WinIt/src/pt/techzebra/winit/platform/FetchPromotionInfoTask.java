package pt.techzebra.winit.platform;


import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class FetchPromotionInfoTask extends AsyncTask<Integer, Void, Promotion> {
    public static interface AsyncResponse {
        void processFinish(Promotion result); 
    }
    
	private static final String TAG = "FetchPromotionInfoTask";
	
    Promotion promotion_ = null;
	private ProgressDialog progress_dialog_;
	Context context_ = null;
	int affinity_;
	
	private AsyncResponse delegate_ = null;

	public FetchPromotionInfoTask(Context context, int affinity){
		context_ = context;
		affinity_ = affinity;
	}

	public void setDelegate(AsyncResponse delegate) {
        delegate_ = delegate;
    }
	
	@Override
	protected Promotion doInBackground(Integer... params) {
		String auth_token = WinIt.getAuthToken();
		promotion_ = NetworkUtilities.fetchPromotionInformation(Integer.toString(params[0]), auth_token);
		Log.d(TAG, Boolean.toString(promotion_ == null));
		return promotion_;
	}

	protected void onPreExecute() {
	    super.onPreExecute();
		progress_dialog_ = new ProgressDialog(context_);
		progress_dialog_.setIndeterminate(true);
		progress_dialog_.setMessage("Loading...");
		progress_dialog_.show();
	}

	@Override
	protected void onPostExecute(Promotion result) {
	    super.onPostExecute(result);
	    progress_dialog_.dismiss();
	    
	    if (result == null) {
	        //Utilities.requireInternetConnection(context_);
	    } else {
	        delegate_.processFinish(result);
	    }
	}
}
