package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class TagsActivity extends SherlockActivity{
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.tags_activity);
	}
}