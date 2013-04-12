package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.R;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        onAuthenticationResult(true); // for debug purposes only!
    }

    public void onAuthenticationResult(boolean result) {
        if (result == true) {
            SharedPreferences.Editor preferences_editor = getSharedPreferences(
                    Constants.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
            preferences_editor.putBoolean(Constants.PREF_LOGGED_IN, true);
            preferences_editor.commit();
            
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            
        }
    }
}
