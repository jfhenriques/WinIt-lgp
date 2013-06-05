package pt.techzebra.winit.ui;


import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.NetworkUtilities.SendAddressToActivity;
import pt.techzebra.winit.platform.DatePickerFragment;
import pt.techzebra.winit.platform.DateUtilities;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;


public class SignupActivity extends SherlockFragmentActivity implements AddressesDialogFragment.AddressesDialogListener, SendAddressToActivity {
	private static final String TAG = "SignupActivity";
	
	private ActionBar action_bar_;
	
	private static final int NUM_STEPS_ = 2;

	@SuppressWarnings("unused")
	private Thread registration_thread_;
	private final Handler handler_ = new Handler();

	private SignupPagerAdapter signup_pager_adapter_;
	private ViewPager signup_view_pager_;
	private PageIndicator page_indicator_;
    
	private EditText name_edit_;
    private EditText email_edit_;
    private EditText password_edit_;
    
    private TextView birthday_text_;
    private EditText pc4_edit_;
    private EditText pc3_edit_;
    private TextView location_text_;
    private EditText house_number_edit_;
    
    private Time birthday_time_;
	private OnDateSetListener on_date_set_listener_;
    
    @SuppressWarnings("unused")
	private Thread postal_code_thread_;
    
    String[] addresses_ = null;
    ArrayList<Integer> addresses_ids_ = null;
    
    private int address_id_ = -1;
    
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.signup_activity);

		birthday_time_ = new Time();
		
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle("Sign up");
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		signup_pager_adapter_ = new SignupPagerAdapter(getSupportFragmentManager());
		
		signup_view_pager_ = (ViewPager) findViewById(R.id.pager);
		signup_view_pager_.setAdapter(signup_pager_adapter_);
		
		page_indicator_ = (PageIndicator) findViewById(R.id.indicator);
		page_indicator_.setViewPager(signup_view_pager_);
		
		signup_view_pager_.invalidate();
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
	    private SignupActivity activity_;
	    
	    private int step_;
	    
	    static SignupStepFragment newInstance(int step) {
	        SignupStepFragment f = new SignupStepFragment();
	        
	        Bundle args = new Bundle();
	        args.putInt("step", step);
	        f.setArguments(args);
	        
	        return f;
	    }
	    
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        activity_ = (SignupActivity) activity;
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
	        
            if (step_ == 0) {
                activity_.name_edit_ = (EditText) activity_.findViewById(R.id.name_section);
                activity_.email_edit_ = (EditText) activity_.findViewById(R.id.email_section);
                activity_.password_edit_= (EditText) activity_.findViewById(R.id.password_edit);
                activity_.password_edit_.setTypeface(Typeface.DEFAULT);
                activity_.password_edit_.setTransformationMethod(new PasswordTransformationMethod());
            } else {
                activity_.birthday_text_ = (TextView) activity_.findViewById(R.id.birthday_text);
                activity_.on_date_set_listener_ = new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month,
                            int day) {
                        Time birthday = activity_.birthday_time_;
                        Long birthday_millis;
                        
                        birthday.year = year;
                        birthday.month = month;
                        birthday.monthDay = day;
                        
                        birthday_millis = birthday.normalize(true);
                        
                        DateUtilities.setDate(activity_.birthday_text_, birthday_millis);
                        
                    }
                };
                activity_.birthday_text_.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int year = activity_.birthday_time_.year;
                        int month = activity_.birthday_time_.month;
                        int day = activity_.birthday_time_.monthDay;
                        DatePickerFragment fragment = DatePickerFragment
                                .newInstance(
                                        activity_.on_date_set_listener_,
                                        year, month, day);
                        fragment.show(getFragmentManager(),
                                "DatePickerFragment");
                    }
                });
                
                activity_.pc4_edit_ = (EditText) activity_.findViewById(R.id.pc4_edit);
                activity_.pc3_edit_ = (EditText) activity_.findViewById(R.id.pc3_edit);
                activity_.location_text_ = (TextView) activity_.findViewById(R.id.address1_text);
                activity_.house_number_edit_ = (EditText) activity_.findViewById(R.id.address2_edit);
            }
	    }
	}
	
	private void handleSubmit() {
	    String name = name_edit_.getText().toString();
        String email = email_edit_.getText().toString();
        String password = password_edit_.getText().toString();
        String birthday = birthday_text_.getText().toString();
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
                    email, password,
                    String.valueOf(birthday_time_.toMillis(false) / 1000),
                    String.valueOf(address_id_), house_number, handler_, this);
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
            case android.R.id.home:
                onBackPressed();
                break;
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
