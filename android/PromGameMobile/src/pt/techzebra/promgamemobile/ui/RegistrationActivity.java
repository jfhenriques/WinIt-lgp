package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockActivity;


public class RegistrationActivity extends SherlockActivity {
	private static final String TAG = "RegistrationActivity";

	private String new_user_name_;
	private String new_user_email_;
	private String new_user_password_;
	private String new_user_cp3_;
	private String new_user_cp4_;
	private String new_user_floor_;
	private String new_user_birthday_;
	private String new_user_door_;

	private Button continue_button_;
	private Button submit_button_;

	private ViewFlipper view_flipper_;
	private EditText birthday_edit_;
	private EditText name_edit_;
	private EditText email_edit_;
	private EditText password_edit_;
	private EditText cp3_edit_;
	private EditText cp4_edit_;
	private EditText floor_edit_;
	private EditText door_edit_;

	private Thread registration_thread_;
	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.signup_step1_activity);

		view_flipper_ = (ViewFlipper) findViewById(R.id.ViewFlipper01);

		name_edit_ = (EditText) findViewById(R.id.name_text);
		email_edit_ = (EditText) findViewById(R.id.email_text);
		password_edit_= (EditText) findViewById(R.id.password_text);
		password_edit_.setTypeface(Typeface.DEFAULT);
		password_edit_.setTransformationMethod(new PasswordTransformationMethod());
		birthday_edit_ = (EditText) findViewById(R.id.birthday_text);
		cp4_edit_ = (EditText) findViewById(R.id.cp4_text);
		cp3_edit_ = (EditText) findViewById(R.id.cp3_text);
		floor_edit_ = (EditText) findViewById(R.id.floor_text);
		door_edit_ = (EditText) findViewById(R.id.door_text);

		continue_button_ = (Button) findViewById(R.id.continue_button);

	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent intent = new Intent(this, AuthenticationActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		return;
	}

	public void handleChangeStep(View view){
		new_user_name_ = name_edit_.getText().toString();
		new_user_email_ = email_edit_.getText().toString();
		new_user_password_ = password_edit_.getText().toString();

		if (new_user_name_.equals("") || new_user_email_.equals("") || new_user_password_.equals("")) {
			// TODO message saying fields are empty
			Log.i(TAG, "Empty fields");

			Toast.makeText(this, "Os campos encontram-se vazios",
					Toast.LENGTH_SHORT).show();
		} else {
			view_flipper_.showNext();
		}   
	}

	public void handleSubmit(View view){
		new_user_cp4_ = cp4_edit_.getText().toString();
		new_user_cp3_ = cp3_edit_.getText().toString();
		new_user_floor_ = floor_edit_.getText().toString();
		new_user_door_ = door_edit_.getText().toString();
		new_user_birthday_ = birthday_edit_.getText().toString();

		if (new_user_cp4_.equals("") || new_user_cp3_.equals("") || new_user_floor_.equals("") || new_user_door_.equals("") || new_user_birthday_.equals("")) {
			// TODO message saying fields are empty
			Log.i(TAG, "Empty fields");

			Toast.makeText(this, "Os campos encontram-se vazios",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
			//TODO fica a faltar mandar o registo com o andar - floor
			//registration_thread_ = NetworkUtilities.attemptRegister(new_user_name_, new_user_email_, new_user_password_, new_user_birthday_, new_user_cp4_, new_user_cp3_, new_user_door_, handler, this);
		}   

	}
}
