package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class AuthenticationActivity extends SherlockActivity {
    private static final String TAG = "AuthenticationActivity";

    private AccountManager account_manager_;
    private Thread authentication_thread_;

    private String user_email_;
    private String user_password_;

    private EditText email_edit_;
    private EditText password_edit_;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        Log.i(TAG, "onCreate(" + saved_instance_state + ")");
        super.onCreate(saved_instance_state);

        setContentView(R.layout.authentication_activity);
    }

    public void handleLogin(View view) {
        email_edit_ = (EditText) findViewById(R.id.email_edit);
        password_edit_ = (EditText) findViewById(R.id.password_edit);

        user_email_ = email_edit_.getText().toString();
        user_password_ = password_edit_.getText().toString();

        // TODO: parse input and create new thread
        if (user_email_.equals("") || user_password_.equals("")) {
            // TODO message saying fields are empty
            Log.i(TAG, "Empty fields");

            Toast.makeText(this, "Os campos encontram-se vazios",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Inicialize login with email: " + user_email_
                    + " password: " + user_password_);
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            //authentication_thread_ = NetworkUtilities.attemptAuth(user_email_, user_password_, handler, this);
            onAuthenticationResult(true); // for debug purposes only!    
        }   
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

    public void handleSignUp(View view) {
        Log.i(TAG, "Initialize Sign Up");
        Toast.makeText(this, "Sign UP", Toast.LENGTH_SHORT).show();
    }
    
    public void handleForgotPassword(View view){
        Log.i(TAG, "Initialize Forgot Password");
        Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show();
    }
    
    public void handleFacebookConnection(View view){
        Log.i(TAG, "Initialize Facebook Connection");
        Toast.makeText(this, "Facebook connection - Coming Soon", Toast.LENGTH_SHORT).show();
    }
}
