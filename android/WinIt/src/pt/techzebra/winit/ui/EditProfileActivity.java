package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.NetworkUtilities.SendAddressToActivity;
import pt.techzebra.winit.client.User;
import pt.techzebra.winit.platform.DatePickerFragment;
import pt.techzebra.winit.platform.DateUtilities;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class EditProfileActivity extends SherlockFragmentActivity implements
        SendAddressToActivity {

	private static final String TAG = "EditProfileActivity";

	private final Handler handler_ = new Handler();

	private ActionBar action_bar_;

	private SharedPreferences preferences_ = null;
	EditText name_edit_text_;
	EditText email_edit_text_;
	EditText password_edit_;
	EditText old_password_edit_;
	TextView birthday_text_;
	EditText cp4_edit_text_;
	EditText cp3_edit_text_;
	TextView address1_text_;
	EditText address2_edit_;

	String[] addresses_ = null;
	ArrayList<Integer> addresses_ids_ = null;

	private Time birthday_time_;
	private OnDateSetListener on_date_set_listener_;
	
	private User user_;

	String address_;
	int address_id_ = -1;

	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.edit_profile_activity);

		birthday_time_ = new Time();
		
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.edit);
		action_bar_.setDisplayHomeAsUpEnabled(true);

		name_edit_text_ = (EditText) findViewById(R.id.name_edit_text);
		email_edit_text_ = (EditText) findViewById(R.id.email_edit_text);
		password_edit_ = (EditText) findViewById(R.id.pass_edit_text);
		old_password_edit_ = (EditText) findViewById(R.id.pass_old_edit_text);
		birthday_text_ = (TextView) findViewById(R.id.birthday_text);
		cp4_edit_text_ = (EditText) findViewById(R.id.pc4_edit);
		cp3_edit_text_ = (EditText) findViewById(R.id.pc3_edit);
		address1_text_ = (TextView) findViewById(R.id.address1_text);
		address2_edit_ = (EditText) findViewById(R.id.address2_edit);

		preferences_ = getSharedPreferences(Constants.USER_PREFERENCES,
				Context.MODE_PRIVATE);

		if (preferences_.getBoolean(Constants.PREF_FB_LOGGED_IN, false)) {
		    findViewById(R.id.name_section).setVisibility(View.GONE);
			name_edit_text_.setVisibility(View.GONE);
			findViewById(R.id.email_section).setVisibility(View.GONE);
			email_edit_text_.setVisibility(View.GONE);
			findViewById(R.id.password_section).setVisibility(View.GONE);
			password_edit_.setVisibility(View.GONE);
			findViewById(R.id.old_password_section).setVisibility(View.GONE);
			old_password_edit_.setVisibility(View.GONE);
			findViewById(R.id.birthday_section).setVisibility(View.GONE);
			birthday_text_.setVisibility(View.GONE);
		}

		user_ = (User) getIntent().getSerializableExtra(
				ProfileActivity.KEY_USER_BUNDLE);
		address_ = user_.getAddress();
		address_id_ = user_.getAddressId();

		name_edit_text_.setText(user_.getName());
		email_edit_text_.setText(user_.getEmail());
		Log.d(TAG, Utilities.convertUnixTimestamp(user_.getBirthday()));
		
		birthday_time_.set(user_.getBirthday() * 1000);
		DateUtilities.setDate(birthday_text_, birthday_time_.toMillis(false));
		
		on_date_set_listener_ = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                    int day) {
                Time birthday = birthday_time_;
                
                long birthday_millis;
                
                birthday.year = year;
                birthday.month = month;
                birthday.monthDay = day;
                
                birthday_millis = birthday.normalize(true);
                    
                DateUtilities.setDate(birthday_text_, birthday_millis);
            }
        };
		
        birthday_text_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = DatePickerFragment
                        .newInstance(
                                on_date_set_listener_,
                                birthday_time_.year, birthday_time_.month, birthday_time_.monthDay);
                fragment.show(getSupportFragmentManager(),
                        "DatePickerFragment");
            }
        });
        
		if (!preferences_.getBoolean(Constants.PREF_FB_LOGGED_IN, false)) {
			cp4_edit_text_.setText(String.valueOf(user_.getCp4()));
			cp3_edit_text_.setText(String.valueOf(user_.getCp3()));
			address1_text_.setText(address_);
			if (user_.getAddress2() != null) {
				address2_edit_.setText(user_.getAddress2());
			}
		}
	}

	private void saveChanges() {
		String name = name_edit_text_.getText().toString();
		String email = email_edit_text_.getText().toString();
		String new_password = password_edit_.getText().toString();
		String old_password = old_password_edit_.getText().toString();
		String birthday = birthday_text_.getText().toString();
		String address2 = address2_edit_.getText().toString();

		if ((!new_password.equals("") && old_password.equals(""))
				|| (!old_password.equals("") && new_password.equals(""))) {
			Toast.makeText(this, "You must fill in the password field",
					Toast.LENGTH_SHORT).show();
			return;
		}

		NetworkUtilities.attemptEditProfile(WinIt.getAuthToken(), name, email,
				new_password, old_password, String.valueOf(birthday_time_.toMillis(false) / 1000),
				String.valueOf(address_id_), address2, handler_, this);
	}

	public void getResponse(String r) {
		if (r.equals("null")) {
			Utilities.showToast(this, "Modified with success");
			finish();
		} else {
			Utilities.showToast(this, r);
		}
	}

	public void handlePostalCode(View view) {
		String pc4 = cp4_edit_text_.getText().toString();
		String pc3 = cp3_edit_text_.getText().toString();

		if (pc4.isEmpty() || pc3.isEmpty()) {
			Utilities.showToast(this, "Fill in the postal code");
		} else {
			NetworkUtilities.attemptGetAddresses(pc4, pc3, handler_, this);
		}
	}

	@Override
	public void onGetAddressesResult(String[] addresses,
			ArrayList<Integer> addresses_ids) {
		addresses_ = addresses;
		addresses_ids_ = addresses_ids;
		if (addresses == null) {
			Utilities.showToast(this, "Invalid postal code");
		} else {
			showAddressesDialog();
		}
	}

	private void showAddressesDialog() {

		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setSingleChoiceItems(addresses_, -1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, String.valueOf(which));
				address1_text_.setText(addresses_[which]);
				address_id_ = addresses_ids_.get(which);
				dialog.dismiss();
			}
		});
		adb.setNegativeButton("Cancel", null);
		adb.setTitle("Which one?");
		adb.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_edit_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_submit:
			saveChanges();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}
}