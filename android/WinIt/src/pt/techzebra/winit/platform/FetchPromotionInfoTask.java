package pt.techzebra.winit.platform;


import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import android.util.Log;


public class FetchPromotionInfoTask extends ServerTask<Integer, Void, Promotion> {
    public static interface AsyncResponse {
        void processFinish(Promotion result); 
    }
    
	private static final String TAG = "FetchPromotionInfoTask";
	
    Promotion promotion_ = null;
	int affinity_;
	
	private AsyncResponse delegate_ = null;

	public FetchPromotionInfoTask(int affinity){
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

	@Override
	protected void onPostExecute(Promotion result) {
	    super.onPostExecute(result);
	    
	    if (result == null) {
	        //Utilities.requireInternetConnection(context_);
	    } else {
	        delegate_.processFinish(result);
	    }
	}
}
