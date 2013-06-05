package pt.techzebra.winit.platform;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Quiz;
import android.util.Log;

public class FetchQuizTask extends ServerTask<String, Void, Quiz> {
    public static interface AsyncResponse {
        void processFinish(Quiz result); 
    }

    private AsyncResponse delegate_ = null;
    
    public void setDelegate(AsyncResponse delegate) {
        delegate_ = delegate;
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

        if (result == null && !Utilities.hasInternetConnection(context_)) {
            Utilities.showInternetConnectionAlert(context_);
        } else {
            Log.d("fan fan fan", result.toString());
            delegate_.processFinish(result);
        }
        
    }
    
}