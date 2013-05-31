package pt.techzebra.winit.ui;


import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.NetworkUtilities.SendAddressToActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class SignupActivity extends SherlockFragmentActivity implements AddressesDialogFragment.AddressesDialogListener, SendAddressToActivity {
	private static final String TAG = "SignupActivity";
	
	private ActionBar action_bar_;
	
	private static final int NUM_STEPS_ = 2;

	private Thread registration_thread_;
	private final Handler handler_ = new Handler();

	private SignupPagerAdapter signup_pager_adapter_;
	private ViewPager signup_view_pager_;
    
	private EditText name_edit_;
    private EditText email_edit_;
    private EditText password_edit_;
    
    private EditText birthday_edit_;
    private EditText pc4_edit_;
    private EditText pc3_edit_;
    private TextView location_text_;
    private EditText house_number_edit_;
	    
    private Thread postal_code_thread_;
    
    String[] addresses_ = null;
    ArrayList<Integer> addresses_ids_ = null;
    
    private int address_id_ = -1;
    
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.signup_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle("Join PromGameMobile");
		
		signup_pager_adapter_ = new SignupPagerAdapter(getSupportFragmentManager());
		
		signup_view_pager_ = (ViewPager) findViewById(R.id.pager);
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
                ((SignupActivity) activity).location_text_ = (TextView) activity.findViewById(R.id.address1_text);
                ((SignupActivity) activity).house_number_edit_ = (EditText) activity.findViewById(R.id.address2_edit);
            }
	    }
	}
	
	private void handleSubmit() {
	    String name = name_edit_.getText().toString();
        String email = email_edit_.getText().toString();
        String password = password_edit_.getText().toString();
        String birthday = birthday_edit_.getText().toString();
        String pc4 = pc4_edit_.getText().toString();
        String pc3 = pc3_edit_.getText().toString();
        String house_number = house_number_edit_.getText().toString();
        
        if (name.equals("") || email.equals("") || password.equals("")
                || birthday.equals("") || pc4.equals("") || pc3.equals("")
                || house_number.equals("") || address_id_ == -1) {
            Log.i(TAG, "Empty fields");
            Utilities.showToast(this, R.string.empty_fields);
        } else {
            registration_thread_ = NetworkUtilities.attemptRegister(name,
                    email, password, birthday, String.valueOf(address_id_), house_number,
                    handler_, this);
        }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_signup, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_signup:
                handleSubmit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
    
    public void handlePostalCode(View view) {
        String pc4 = pc4_edit_.getText().toString();
        String pc3 = pc3_edit_. getText().toString();
        
        if (pc4.isEmpty() || pc3.isEmpty()) {
            Utilities.showToast(this, "Fill in the postal code");
        } else {
            postal_code_thread_ = NetworkUtilities.attemptGetAddresses(pc4, pc3, handler_, this);
        }
    }

    @Override
    public void onGetAddressesResult(String[] addresses, ArrayList<Integer> addresses_ids) {
        addresses_ = addresses;
        addresses_ids_ = addresses_ids;
        if (addresses == null) {
            Utilities.showToast(this, "Invalid postal code");
        } else {
            showAddressesDialog();
        }
    }

    private void showAddressesDialog() {
        AddressesDialogFragment dialog = new AddressesDialogFragment();
        dialog.show(getSupportFragmentManager(), "addresses");
    }
    
    @Override
    public void onDialogPositiveClick(int which, DialogFragment dialog) {
        address_id_ = addresses_ids_.get(which);
        location_text_.setText(addresses_[which]);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        
    }
    
    public void onSignupResult(boolean result) {
        if (result == true) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish(); 
        } else {
            Log.d(TAG, "Error");
            Utilities.showToast(this, "An error occurred. Try again.");
        }
    }
}
