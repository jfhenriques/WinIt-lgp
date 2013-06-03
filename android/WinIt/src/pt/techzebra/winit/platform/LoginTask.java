package pt.techzebra.winit.platform;

import android.util.Log;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.AuthenticationActivity;

public class LoginTask extends ServerTask<Object, Object, Boolean> {    
    @Override
    protected Boolean doInBackground(Object... params) {
        String username = (String) params[0];
        String password = (String) params[1];
        return NetworkUtilities.authenticate(username, password);
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        Log.d("LOGIN", String.valueOf(result));
        super.onPostExecute(result);
        
        ((AuthenticationActivity) context_).onAuthenticationResult(result);
        if (result) {
            
        } else {
            
        }
    }

}
