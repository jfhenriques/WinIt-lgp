package pt.techzebra.promgamemobile.ui;



import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.User;
import pt.techzebra.promgamemobile.platform.RoundedImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class ProfileActivity extends SherlockActivity {
	private static final String TAG = "ProfileActivity";

	private ActionBar action_bar_;

	//Activity variables
	RoundedImageView profile_image_;
	TextView name_text_;
	TextView email_text_;
	TextView level_text_;
	TextView points_text_;
	
	User user_ = null;
	String auth_token;

	/*
	 * Set up user data that is displayed on this activity 
	 */
	public void setUserData(User u) throws Exception{
		/*
		 * TODO Set user profile image (gravatar)
		 * */

		//Set user name
		name_text_.setText(u.getName());
		//Set email name
		email_text_.setText(u.getEmail());
		//set user level
		level_text_.setText("Level " + u.getLevel());
		//set user points
		points_text_.setText(u.getPoints()+"/500");
	}

    @SuppressWarnings("deprecation")
    @Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.profile_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.profile);

		profile_image_ = (RoundedImageView) findViewById(R.id.profile_image);
		//profile_image_.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.photo));
		profile_image_.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_picture));
	
		name_text_ = (TextView) findViewById(R.id.name_text);
		email_text_ = (TextView) findViewById(R.id.email_text);
		level_text_ = (TextView) findViewById(R.id.level_text);
		points_text_ = (TextView) findViewById(R.id.points_text);
		
		try {
			user_ = (User) getIntent().getSerializableExtra("User");
			setUserData(user_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleProfileSelection(View view) {
		Class<?> cls = null;
	    switch (view.getId()) {
    		 case R.id.badges_view:
    		     cls = BadgesActivity.class;
    			 break;
    		 case R.id.tags_view:
    			 cls = TagsActivity.class;
    			 break;
		 }
	    
	    Intent intent = new Intent(this, cls);
	    startActivity(intent);
	}
}


