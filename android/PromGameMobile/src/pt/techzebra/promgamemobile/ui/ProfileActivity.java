package pt.techzebra.promgamemobile.ui;


import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.User;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	//private static final String TAG = "ProfileActivity";
	//Activity variables
	ImageView profile_image_;
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


	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.profile_activity);

		profile_image_ = (ImageView) findViewById(R.id.profile_image);
		name_text_ = (TextView) findViewById(R.id.name_text);
		email_text_ = (TextView) findViewById(R.id.email_text);
		level_text_ = (TextView) findViewById(R.id.level_text);
		points_text_ = (TextView) findViewById(R.id.points_text);
		SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
		auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");

		final Runnable r = new Runnable(){

			@Override
			public void run() {
				try {
					user_ = NetworkUtilities.fetchUserInformation("", auth_token, null);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		};
		NetworkUtilities.performOnBackgroundThread(r);
		ProfileActivity.this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					setUserData(user_);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		

		
	}
}
