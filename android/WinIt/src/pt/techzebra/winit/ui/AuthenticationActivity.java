package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.platform.FontUtils;
import pt.techzebra.winit.ui.ForgotPasswordDialogFragment.ForgotPasswordDialogListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class AuthenticationActivity extends SherlockFragmentActivity implements ForgotPasswordDialogListener {
    // Own Facebook APP ID
    private static final String APP_ID = "104075696465886";
    private static final String TAG = "AuthenticationActivity";

    private EditText email_edit_;
    private EditText password_edit_;

    private Handler handler_;
    private boolean doubleBackToExitPressedOnce = false;

    /******** FACEBOOK CON *****************/
    private Facebook facebook_;
    private AsyncFacebookRunner mAsyncRunner_;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs_;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        Log.i(TAG, "onCreate(" + saved_instance_state + ")");
        super.onCreate(saved_instance_state);

        setContentView(R.layout.authentication_activity);

        if (Build.VERSION.SDK_INT < 11) {
            Log.d(TAG, "Loading custom typeface");
            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            FontUtils.setRobotoFont(this, view);
        }
        
        getSupportActionBar().hide();

        email_edit_ = (EditText) findViewById(R.id.email_edit);
        password_edit_ = (EditText) findViewById(R.id.password_edit);

        password_edit_.setTypeface(Typeface.DEFAULT);
        password_edit_
                .setTransformationMethod(new PasswordTransformationMethod());

        TextView slogan = (TextView) findViewById(R.id.slogan);
        slogan.setText(Html.fromHtml(getString(R.string.slogan)));

        handler_ = new Handler();

        /******* FACEBOOK ************/
        facebook_ = new Facebook(APP_ID);
        mAsyncRunner_ = new AsyncFacebookRunner(facebook_);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

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
            Log.i(TAG, "Inicialize login with email: " + email + " password: "
                    + password);
            Utilities.showToast(this, "Loading...");
            NetworkUtilities.attemptAuth(email, password, handler_, this);
        }        

    }

    public void onAuthenticationResult(boolean result) {
        if (result == true) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "Error");
            Utilities.showToast(this,
                    "Please check your email and password and try again.");
        }
    }

    public void handleSignUp(View view) {
        Log.i(TAG, "Initialize Sign Up");

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void handleForgotPassword(View view) {
        Log.i(TAG, "Initialize Forgot Password");
        DialogFragment dialog = new ForgotPasswordDialogFragment();
        dialog.show(getSupportFragmentManager(), "ForgotPasswordDialogFragment");
    }

    @SuppressWarnings("deprecation")
    public void handleFacebookConnection(View view) {
        Log.i(TAG, "Initialize Facebook Connection");
        // http://www.androidhive.info/2012/03/android-facebook-connect-tutorial/

        mPrefs_ = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs_.getString("access_token", null);
        long experies = mPrefs_.getLong("access_expires", 0);

        if (access_token != null) {
            facebook_.setAccessToken(access_token);
        }

        if (experies != 0) {
            facebook_.setAccessExpires(experies);
        }

        if (!facebook_.isSessionValid()) {
            facebook_.authorize(this,
                    new String[] { "email", "publish_stream" },
                    new DialogListener() {

                        @Override
                        public void onFacebookError(final FacebookError arg0) {
                            // TODO Auto-generated method stub

                            Log.i(TAG, "onFacebookError");
                        }

                        @Override
                        public void onError(final DialogError arg0) {
                            // TODO Auto-generated method stub

                            Log.i(TAG, "onError");
                        }

                        @Override
                        public void onComplete(final Bundle arg0) {

                            Log.i(TAG, "onComplete");
                            SharedPreferences.Editor editor = mPrefs_.edit();
                            editor.putString("access_token",
                                    facebook_.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook_.getAccessExpires());
                            
                            editor.commit();
                        }

                        @Override
                        public void onCancel() {
                            Log.i(TAG, "onCancel");
                        }
                    });
        }
    }

    @Override
    public void onFinishForgotPasswordDialog(String email) {
        Utilities.showToast(this, "Yeah");
    }

}
