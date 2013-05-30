package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.NetworkUtilities.SendAddressToActivity;
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

public class EditProfileActivity extends SherlockActivity implements
		SendAddressToActivity {

	private static final String TAG = "EditProfileActivity";

	private final Handler handler_ = new Handler();

	private ActionBar action_bar_edit_;

	// variables edit profile
	EditText name_edit_text_;
	EditText email_edit_text_;
	EditText pass_edit_text_;
	EditText pass_old_edit_text_;
	EditText birth_edit_text_;
	EditText cp4_edit_text_;
	EditText cp3_edit_text_;
	EditText house_edit_text_;
	TextView location_text_view_;

	String[] addresses_ = null;
	ArrayList<Integer> addresses_ids_ = null;

	String auth_token_;
	String address_;
	int address_id_ = -1;

	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.edit_profile_activity);

		action_bar_edit_ = getSupportActionBar();
		action_bar_edit_.setTitle(R.string.edit);

		// Edit profile
		name_edit_text_ = (EditText) findViewById(R.id.name_edit_text);
		email_edit_text_ = (EditText) findViewById(R.id.email_edit_text);
		pass_edit_text_ = (EditText) findViewById(R.id.pass_edit_text);
		pass_old_edit_text_ = (EditText) findViewById(R.id.pass_old_edit_text);
		birth_edit_text_ = (EditText) findViewById(R.id.birth_edit_text);
		cp4_edit_text_ = (EditText) findViewById(R.id.pc4_edit);
		cp3_edit_text_ = (EditText) findViewById(R.id.pc3_edit);
		house_edit_text_ = (EditText) findViewById(R.id.house_number_edit);
		location_text_view_ = (TextView) findViewById(R.id.location_text);

		Bundle bun = getIntent().getExtras();
		name_edit_text_.setText(bun.getString("name"));
		email_edit_text_.setText(bun.getString("email"));
		String birthString = Utilities.convertUnixTimestamp(bun.getInt("birthday"));
		birth_edit_text_.setText(birthString);
		cp4_edit_text_.setText(String.valueOf(bun.getInt("cp4")));
		cp3_edit_text_.setText(String.valueOf(bun.getInt("cp3")));
		address_ = bun.getString("address");
		auth_token_ = bun.getString("token");
	}

	public void handleSubmitEditProfile(View view) {

		String name = name_edit_text_.getText().toString();
		String email = email_edit_text_.getText().toString();
		String new_password = pass_edit_text_.getText().toString();
		String old_password = pass_old_edit_text_.getText().toString();
		String birthday = birth_edit_text_.getText().toString();
		String address_2 = house_edit_text_.getText().toString();

		if ((!new_password.equals("") && old_password.equals(""))
				|| (!old_password.equals("") && new_password.equals(""))) {
			Toast.makeText(this, "You must fill in the Password field",
					Toast.LENGTH_SHORT).show();
			return;
		}

		NetworkUtilities.attemptEditProfile(auth_token_, name, email,
				new_password, old_password, birthday,
				String.valueOf(address_id_), address_2, handler_, this);

		return;
	}

	public void getResponse(String r) {
		Utilities.showToast(this, r);
		if(r == null){
			Utilities.showToast(this, "Modifyed with success");
		}else{
			Utilities.showToast(this, r);
		}
		finish();
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
				location_text_view_.setText(addresses_[which]);
				address_id_ = addresses_ids_.get(which);
				dialog.dismiss();
			}
		});
		adb.setNegativeButton("Cancel", null);
		adb.setTitle("Which one?");
		adb.show();
	}
}