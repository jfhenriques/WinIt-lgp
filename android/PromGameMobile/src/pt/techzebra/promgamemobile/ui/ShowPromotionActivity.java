package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class ShowPromotionActivity extends SherlockActivity {

	//int prom_id_;
	private ActionBar action_bar_;
	
	//private static final String TAG = "ShowPromotionActivity";
	
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotion);
		setContentView(R.layout.promotion_activity);
		//Bundle b = getIntent().getExtras();
		//int prom_id_ = b.getInt("key");
		//Log.v(TAG, Integer.toString(prom_id_));
		
	}
}
