package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.Utilities;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class AuthenticationActivity extends SherlockActivity {
    private static final String TAG = "AuthenticationActivity";

    private EditText email_edit_;
    private EditText password_edit_;

    private Handler handler_;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        Log.i(TAG, "onCreate(" + saved_instance_state + ")");
        super.onCreate(saved_instance_state);

        setContentView(R.layout.authentication_activity);
        
        getSupportActionBar().hide();
        
        email_edit_ = (EditText) findViewById(R.id.email_edit);
        password_edit_ = (EditText) findViewById(R.id.password_edit);
        
        password_edit_.setTypeface(Typeface.DEFAULT);
        password_edit_.setTransformationMethod(new PasswordTransformationMethod());
        
        TextView slogan = (TextView) findViewById(R.id.slogan);
        slogan.setText(Html.fromHtml(getString(R.string.slogan)));
        
        handler_ = new Handler();
    }
    @Override
    public void onBackPressed(){
    	 if (doubleBackToExitPressedOnce) {
             super.onBackPressed();
             Intent intent = new Intent(Intent.ACTION_MAIN);
             intent.addCategory(Intent.CATEGORY_HOME);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
             return;
         }
         this.doubleBackToExitPressedOnce = true;
         Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
         new Handler().postDelayed(new Runnable() {

             @Override
             public void run() {
              doubleBackToExitPressedOnce=false;   
              
             }
         }, 2000);
    }

    public void handleLogin(View view) {
        String email = email_edit_.getText().toString();
        String password = password_edit_.getText().toString();

        if (email.equals("") || password.equals("")) {
            Log.i(TAG, "Empty fields");
            Utilities.showToast(this, R.string.empty_fields);
        } else {
            Log.i(TAG, "Inicialize login with email: " + email
                    + " password: " + password);
            Utilities.showToast(this, "Loading...");
            NetworkUtilities.attemptAuth(email, password, handler_, this);
        }   
        /*
        SharedPreferences.Editor preferences_editor = PromGame.getAppContext().getSharedPreferences(
                Constants.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
        preferences_editor.putBoolean(Constants.PREF_LOGGED_IN, true);
        preferences_editor.commit();
        onAuthenticationResult(true);
        */
    }

    public void onAuthenticationResult(boolean result) {
        if (result == true) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish(); 
        } else {
        	Log.d(TAG, "Error");
            Utilities.showToast(this, "Please check your email and password and try again.");
        }
    }
    
    
    public void handleSignUp(View view) {
        Log.i(TAG, "Initialize Sign Up");
        
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    
    public void handleForgotPassword(View view) {
        Log.i(TAG, "Initialize Forgot Password");
        Utilities.showToast(this, "Coming soon");
    }
    
    public void handleFacebookConnection(View view) {
        Log.i(TAG, "Initialize Facebook Connection");
        Utilities.showToast(this, "Coming soon");
    }
}
