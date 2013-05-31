package pt.techzebra.winit.ui;

import java.util.Locale;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.User;
import pt.techzebra.winit.platform.DownloadImageTask;
import pt.techzebra.winit.platform.LoadMyPromotionsInfo;
import pt.techzebra.winit.platform.MD5Util;
import pt.techzebra.winit.platform.RoundedImageView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends SherlockActivity {
	private static final String TAG = "ProfileActivity";

	public static final String KEY_USER_BUNDLE = "user";
	
	private ActionBar action_bar_;

	// Activity variables
	RoundedImageView profile_image_;
	TextView name_text_;
	TextView email_text_;
	TextView level_text_;
	TextView points_text_;

	User user_ = null;
	String auth_token;

	/**
	 * Set up user data that is displayed on this activity
	 */
	public void setUserData(User u) {
		name_text_.setText(u.getName());
		email_text_.setText(u.getEmail());
		level_text_.setText("Level " + u.getLevel());
		points_text_.setText(u.getPoints() + "/500");

		String hash = MD5Util.md5Hex(u.getEmail().toLowerCase(Locale.getDefault()));
		String gravatar_url = "https://secure.gravatar.com/avatar/" + hash + "?s=320&d=identicon";
		new DownloadImageTask(profile_image_).execute(gravatar_url);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.profile_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.profile);
		action_bar_.setDisplayHomeAsUpEnabled(true);

		profile_image_ = (RoundedImageView) findViewById(R.id.profile_image);
		profile_image_.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.profile_picture));

		name_text_ = (TextView) findViewById(R.id.name_text);
		email_text_ = (TextView) findViewById(R.id.email_text);
		level_text_ = (TextView) findViewById(R.id.level_text);
		points_text_ = (TextView) findViewById(R.id.points_text);

		auth_token = WinIt.getAuthToken();
		Log.d(TAG, "auth token");
		try {
			user_ = (User) getIntent().getSerializableExtra("User");
			setUserData(user_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    		case android.R.id.home:
    			onBackPressed();
    			break;
    		case R.id.menu_edit_profile:
    			Intent intent = new Intent(this, EditProfileActivity.class);
    			intent.putExtra(KEY_USER_BUNDLE, user_);
    			startActivity(intent);
    			break;
    		case R.id.menu_log_out:
    			WinIt.logOut(this);
    			break;
    		default:
    			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	public void handleProfileSelection(View view) {
		Class<?> cls = null;
		switch (view.getId()) {
		case R.id.promotions_button:
			new LoadMyPromotionsInfo(this).execute();
			break;
		case R.id.badges_button:
			cls = BadgesActivity.class;
			break;
		case R.id.tags_button:
			cls = null;
			Toast.makeText(this, "Comming soon!", Toast.LENGTH_SHORT).show();
			break;
		}
		if(cls != null){
			Intent intent = new Intent(this, cls);
			startActivity(intent);
		}
	}


}
