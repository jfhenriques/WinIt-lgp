package pt.techzebra.promgamemobile.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.User;
import pt.techzebra.promgamemobile.platform.RoundedImageView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends SherlockActivity {
    private static final String TAG = "ProfileActivity";

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
    public void setUserData(User u) throws Exception {
        /*
         * TODO Set user profile image (gravatar)
         */

        // Set user name
        name_text_.setText(u.getName());
        // Set email name
        email_text_.setText(u.getEmail());
        // set user level
        level_text_.setText("Level " + u.getLevel());
        // set user points
        points_text_.setText(u.getPoints() + "/500");

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.profile_activity);

        action_bar_ = getSupportActionBar();
        action_bar_.setTitle(R.string.profile);
        
        profile_image_ = (RoundedImageView) findViewById(R.id.profile_image);
        profile_image_.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.profile_picture));
        
        name_text_ = (TextView) findViewById(R.id.name_text);
        email_text_ = (TextView) findViewById(R.id.email_text);
        level_text_ = (TextView) findViewById(R.id.level_text);
        points_text_ = (TextView) findViewById(R.id.points_text);

        SharedPreferences preferences_editor = PromGame.getAppContext()
                .getSharedPreferences(Constants.USER_PREFERENCES,
                        Context.MODE_PRIVATE);
        auth_token = preferences_editor.getString(Constants.PREF_AUTH_TOKEN,
                null);
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
        case R.id.menu_edit_profile:
            Intent in = new Intent(this, EditProfileActivity.class);
            Bundle myb = new Bundle();
            myb.putString("name", user_.getName());
            myb.putString("email", user_.getEmail());
            myb.putString("birthday", user_.getBirthday());
            myb.putString("address", user_.getAddress());
            myb.putInt("cp4", user_.getCp4());
            myb.putInt("cp3", user_.getCp3());
            myb.putInt("id", user_.getUserId());
            myb.putString("token", auth_token);
            in.putExtras(myb);
            startActivity(in);
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void handleProfileSelection(View view) {
        Class<?> cls = null;
        switch (view.getId()) {
        case R.id.badges_view:
            cls = BadgesActivity.class;
            break;
//        case R.id.tags_view:
//            cls = TagsActivity.class;
//            break;
        }

        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
