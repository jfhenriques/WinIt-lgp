package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;

public class AuthenticationActivity extends SherlockActivity {
    private static final String TAG = "AuthenticationActivity";
    
    private AccountManager account_manager_;
    private Thread authentication_thread_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        setContentView(R.layout.authentication_activity);
    }
    
    public void handleLogin(View view) {
        // TODO: parse input and create new thread
    }

    public void onAuthenticationResult(boolean result) {
        
    }
}
