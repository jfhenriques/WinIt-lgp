package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.Utilities;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class EditProfileActivity extends SherlockActivity {

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
    EditText port_edit_text_;

    String auth_token_;
    String address_;

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
        port_edit_text_ = (EditText) findViewById(R.id.house_number_edit);

        Bundle bun = getIntent().getExtras();
        name_edit_text_.setText(bun.getString("name"));
        email_edit_text_.setText(bun.getString("email"));
        birth_edit_text_.setText(bun.getString("birthday"));
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
        int address_id = -1;
        String address_2 = "";
        
        if((!new_password.isEmpty() && old_password.isEmpty()) || (!old_password.isEmpty() && new_password.isEmpty())){
            Toast.makeText(this, "Deve preencher o campo Password", Toast.LENGTH_SHORT).show();
            return;
        }
        
        //TODO: address change
        
        NetworkUtilities.attemptEditProfile(auth_token_, name, email, new_password,
                old_password, birthday, String.valueOf(address_id), address_2, handler_, this);

        return;
    }

    public void getResponse(String r) {
        Utilities.showToast(this, r);
    }
}