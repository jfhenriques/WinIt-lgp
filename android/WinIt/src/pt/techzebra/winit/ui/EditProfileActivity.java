package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.NetworkUtilities.SendAddressToActivity;
import pt.techzebra.winit.client.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class EditProfileActivity extends SherlockActivity implements
		SendAddressToActivity {

	private static final String TAG = "EditProfileActivity";

	private final Handler handler_ = new Handler();

	private ActionBar action_bar_;

	EditText name_edit_text_;
	EditText email_edit_text_;
	EditText password_edit__;
	EditText old_password_edit_;
	EditText birthday_edit_text_;
	EditText cp4_edit_text_;
	EditText cp3_edit_text_;
	TextView address1_text_;
	EditText address2_edit_;

	String[] addresses_ = null;
	ArrayList<Integer> addresses_ids_ = null;

	private User user_;
	
	String address_;
	int address_id_ = -1;

	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.edit_profile_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.edit);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		name_edit_text_ = (EditText) findViewById(R.id.name_edit_text);
		email_edit_text_ = (EditText) findViewById(R.id.email_edit_text);
		password_edit__ = (EditText) findViewById(R.id.pass_edit_text);
		old_password_edit_ = (EditText) findViewById(R.id.pass_old_edit_text);
		birthday_edit_text_ = (EditText) findViewById(R.id.birth_edit_text);
		cp4_edit_text_ = (EditText) findViewById(R.id.pc4_edit);
		cp3_edit_text_ = (EditText) findViewById(R.id.pc3_edit);
		address1_text_ = (TextView) findViewById(R.id.address1_text);
		address2_edit_ = (EditText) findViewById(R.id.address2_edit); 
		
		user_ = (User) getIntent().getSerializableExtra(ProfileActivity.KEY_USER_BUNDLE);
		address_ = user_.getAddress();
		address_id_ = user_.getAddressId();
		
		name_edit_text_.setText(user_.getName());
		email_edit_text_.setText(user_.getEmail());
		birthday_edit_text_.setText(Utilities.convertUnixTimestamp(user_.getBirthday()));
		cp4_edit_text_.setText(String.valueOf(user_.getCp4()));
		cp3_edit_text_.setText(String.valueOf(user_.getCp3()));
		address1_text_.setText(address_);
		if (user_.getAddress2() != null) {
		    address2_edit_.setText(user_.getAddress2());
		}
	}

	private void saveChanges() {
		String name = name_edit_text_.getText().toString();
		String email = email_edit_text_.getText().toString();
		String new_password = password_edit__.getText().toString();
		String old_password = old_password_edit_.getText().toString();
		String birthday = birthday_edit_text_.getText().toString();
		String address2 = address2_edit_.getText().toString();

		if ((!new_password.equals("") && old_password.equals(""))
				|| (!old_password.equals("") && new_password.equals(""))) {
			Toast.makeText(this, "You must fill in the Password field",
					Toast.LENGTH_SHORT).show();
			return;
		}

		NetworkUtilities.attemptEditProfile(WinIt.getAuthToken(), name, email,
				new_password, old_password, birthday,
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