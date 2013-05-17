package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class BadgesActivity extends SherlockActivity {
    private static final String TAG = "BadgesActivity";

    private ActionBar action_bar_;
    
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.badges_activity);
		
		action_bar_ = getSupportActionBar();
        action_bar_.setTitle(R.string.badges);
	}
}
