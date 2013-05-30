package pt.techzebra.winit.ui;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.platform.FontUtils;
import pt.techzebra.winit.ui.ForgotPasswordDialogFragment.ForgotPasswordDialogListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class AuthenticationActivity extends SherlockFragmentActivity implements ForgotPasswordDialogListener {
	
//	private static class PendingStateChange
//	{
//		Session session = null;
//		SessionState state = null;
//		Exception exception = null;
//		
//		public PendingStateChange(Session session, SessionState state, Exception exception)
//		{
//			this.session = session;
//			this.state = state;
//			this.exception = exception;
//		}
//	}
//	
	
	
	
	private static final String TAG = "AuthenticationActivity";
	
	private boolean isPaused = false;
	private EditText email_edit_;
	private EditText password_edit_;

	private Handler handler_;
	private boolean doubleBackToExitPressedOnce = false;
	
	private UiLifecycleHelper uiHelper;
	
//	private static LinkedList<PendingStateChange> stateQueue = new LinkedList<PendingStateChange>();
	

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state, Exception exception)
		{
			Log.i(TAG, "PAUSE: " + isPaused + ", Sess call: " + session + ", state: " + state + ", ex: " + exception);
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		Log.i(TAG, "onCreate(" + saved_instance_state + ")");
		super.onCreate(saved_instance_state);
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(saved_instance_state);
		
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "pt.techzebra.winit", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                	Log.d(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
        	e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        }

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

		LoginButton authButton = (LoginButton) findViewById(R.id.facebook_button);
		authButton.setOnErrorListener(new OnErrorListener() {
			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());
			}
		});

		TextView slogan = (TextView) findViewById(R.id.slogan);
		slogan.setText(Html.fromHtml(getString(R.string.slogan)));
		
		authButton.setReadPermissions(Arrays.asList("basic_info","email","user_birthday"));
		//authButton.setSessionStatusCallback(callback);

		handler_ = new Handler();

	}
	

	
	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		
//		if( isPaused )
//		{
//			Log.i(TAG, "App is paused, cannot process state change. Queued!");
//			stateQueue.add( new PendingStateChange(session, state, exception) );
//		}
//		else
//		{
			Log.i(TAG,"Access Token"+ session.getAccessToken());
			Log.i(TAG, "What: " + session.getState());
			Log.d(TAG, "session desc: " + session);
		
			
		    if (state.isOpened())
		    {
				NetworkUtilities.attemptFacebookLogin(session.getAccessToken(), handler_, AuthenticationActivity.this);
				Log.d(TAG, "okkkkk");
		    }
		    else if (state.isClosed())
		    {
		        Log.i(TAG, "Logged out...");
		    }
		    else
				Log.d(TAG, "merdinhas");
//		}
	   
	}
	


    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume()
    {
    	Log.d(TAG, "onResume() before");
    	
        super.onResume();
        uiHelper.onResume();
        
        Log.d(TAG, "onResume() after");
//        Log.d(TAG, "QUEUE SIZE: " + stateQueue.size());
        
        isPaused = false;
        
//        PendingStateChange pending = null;
//        while( null != ( pending = stateQueue.poll() ) )
//        {
//        	Log.d(TAG, "Processing pending state");
//        	onSessionStateChange(Session.getActiveSession(), Session.getActiveSession().getState(), null);
//        }
    }
    
    @Override
    public void onPause()
    {
    	Log.d(TAG, "onPause() before");
    	
        super.onPause();
        uiHelper.onPause();
        
        Log.d(TAG, "onPause() after");
        
        isPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }


    
    
    
    
    
    
    
    
    
    
	
	
	@Override
	public void onBackPressed()
	{
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

	@Override
	public void onFinishForgotPasswordDialog(String email) {
		Utilities.showToast(this, "Yeah");
	}




	public void getResultSentToAuthentication(String message) {
		Toast.makeText(this, "AQUI", Toast.LENGTH_LONG).show();

	}

}
