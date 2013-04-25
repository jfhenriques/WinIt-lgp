package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class RegistrationActivity extends SherlockActivity {
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        setContentView(R.layout.signup_step1_activity);
    }
}
