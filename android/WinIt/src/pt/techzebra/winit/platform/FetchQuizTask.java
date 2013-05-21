package pt.techzebra.winit.platform;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Quiz;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FetchQuizTask extends AsyncTask<String, Void, Quiz> {
    public static interface AsyncResponse {
        void processFinish(Quiz result); 
    }
    
    private Context context_;
    private AsyncResponse delegate_ = null;
    
    private ProgressDialog progress_dialog_;
    
    public FetchQuizTask(Context context) {
        context_ = context;
    }
    
    public void setDelegate(AsyncResponse delegate) {
        delegate_ = delegate;
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
    protected Quiz doInBackground(String... params) {
        Quiz quiz = null;
        String promotion_id = params[0];
        String auth_token = WinIt.getAuthToken();
        
        quiz = NetworkUtilities.fetchQuizGame(promotion_id, auth_token);

        return quiz;
    }
    
    @Override
    protected void onPostExecute(Quiz result) {
        super.onPostExecute(result);
        progress_dialog_.dismiss();
        if (result == null && !Utilities.hasInternetConnection(context_)) {
            Utilities.showInternetConnectionAlert(context_);
        } else {
            Log.d("fan fan fan", result.toString());
            delegate_.processFinish(result);
        }
        
    }
    
}