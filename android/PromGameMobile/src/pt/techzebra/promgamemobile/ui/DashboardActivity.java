package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class DashboardActivity extends SherlockActivity {
    private static final String TAG = "DashboardActivity";
    
    private SharedPreferences preferences_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        preferences_ = getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        
        boolean logged_in = preferences_.getBoolean(Constants.PREF_LOGGED_IN, false);
        if (!logged_in) {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.dashboard_activity);
    }
    
   
}
