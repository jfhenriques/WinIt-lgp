package pt.techzebra.promgamemobile.ui;


import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.Utilities;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.platform.SignupViewPager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class SignupActivity extends SherlockFragmentActivity {
	private static final String TAG = "SignupActivity";
	
	private ActionBar action_bar_;
	
	private static final int NUM_STEPS_ = 2;

	private Thread registration_thread_;
	private final Handler handler_ = new Handler();

	private SignupPagerAdapter signup_pager_adapter_;
	private SignupViewPager signup_view_pager_;
    
	private EditText name_edit_;
    private EditText email_edit_;
    private EditText password_edit_;
    
    private EditText birthday_edit_;
    private EditText pc4_edit_;
    private EditText pc3_edit_;
    private EditText house_number_edit_;
	    
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.signup_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle("Join PromGameMobile");
		
		signup_pager_adapter_ = new SignupPagerAdapter(getSupportFragmentManager());
		
		signup_view_pager_ = (SignupViewPager) findViewById(R.id.pager);
		signup_view_pager_.setAdapter(signup_pager_adapter_);
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent intent = new Intent(this, AuthenticationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private static class SignupPagerAdapter extends FragmentPagerAdapter {

        public SignupPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {            
            return SignupStepFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return NUM_STEPS_;
        }
	    
	}
	
	public static class SignupStepFragment extends SherlockFragment {
	    private int step_;
	    
	    static SignupStepFragment newInstance(int step) {
	        SignupStepFragment f = new SignupStepFragment();
	        
	        Bundle args = new Bundle();
	        args.putInt("step", step);
	        f.setArguments(args);
	        
	        return f;
	    }
	    
	    @Override
	    public void onCreate(Bundle saved_instance_state) {
	        super.onCreate(saved_instance_state);
	        
	        step_ = getArguments() != null ? getArguments().getInt("step") : 0;
	    }
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle saved_instance_state) {
            View v = inflater.inflate(
                    step_ == 0 ? R.layout.signup_step1_fragment
                            : R.layout.signup_step2_fragment, container, false);
            
	        return v;
	    }
	    
	    @Override
	    public void onActivityCreated(Bundle saved_instance_state) {
	        super.onActivityCreated(saved_instance_state);
	        SherlockFragmentActivity activity = getSherlockActivity();
	        
            if (step_ == 0) {
                ((SignupActivity) activity).name_edit_ = (EditText) activity.findViewById(R.id.name_edit);
                ((SignupActivity) activity).email_edit_ = (EditText) activity.findViewById(R.id.email_edit);
                ((SignupActivity) activity).password_edit_= (EditText) activity.findViewById(R.id.password_edit);
                ((SignupActivity) activity).password_edit_.setTypeface(Typeface.DEFAULT);
                ((SignupActivity) activity).password_edit_.setTransformationMethod(new PasswordTransformationMethod());
            } else {
                ((SignupActivity) activity).birthday_edit_ = (EditText) activity.findViewById(R.id.birthday_edit);
                ((SignupActivity) activity).pc4_edit_ = (EditText) activity.findViewById(R.id.pc4_edit);
                ((SignupActivity) activity).pc3_edit_ = (EditText) activity.findViewById(R.id.pc3_edit);
                ((SignupActivity) activity).house_number_edit_ = (EditText) activity.findViewById(R.id.house_number_edit);
            }
	    }
	}
	
	public void handleSignupNavigation(View v) {
	    boolean valid_input = false;
	    int step = 0;
	    switch (v.getId()) {
	        case R.id.continue_button:
	            step = 0;
	            break;
	        case R.id.signup_button:
	            step = 1;
	            break;
	    }
	    
	    valid_input = validateInput(step);
	    
	    if (valid_input) {
	        if (step == 0) {
	            signup_view_pager_.setCurrentItem(1, true);
	        } else {
	            handleSubmit();
	        }
	    } else {
	        Log.i(TAG, "Empty fields");
            Utilities.showToast(this, R.string.empty_fields);
	    }
	}
	
	public boolean validateInput(int step) {
	    boolean empty_fields = false;
	    
	    if (step == 0) {
    	    String name = name_edit_.getText().toString();
    	    String email = email_edit_.getText().toString();
    	    String password = password_edit_.getText().toString();
    	    
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                empty_fields = true;   
            }
	    } else {
            String birthday = birthday_edit_.getText().toString();
            String pc4 = pc4_edit_.getText().toString();
            String pc3 = pc3_edit_.getText().toString();
            String house_number = house_number_edit_.getText().toString();
            
            if (birthday.isEmpty() || pc4.isEmpty() || pc3.isEmpty()
                    || house_number.isEmpty()) {
                empty_fields = true;
            }
        }
	    
	    return !empty_fields;
	}
	

	private void handleSubmit() {
	    String name = name_edit_.getText().toString();
        String email = email_edit_.getText().toString();
        String password = password_edit_.getText().toString();
        String birthday = birthday_edit_.getText().toString();
        String pc4 = pc4_edit_.getText().toString();
        String pc3 = pc3_edit_.getText().toString();
        String house_number = house_number_edit_.getText().toString();
        
        NetworkUtilities.attemptRegister(name, email, password, birthday, pc4, pc3, house_number, handler_, this);
	}
}
