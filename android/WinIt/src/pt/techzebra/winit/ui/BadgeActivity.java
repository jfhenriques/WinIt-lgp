package pt.techzebra.winit.ui;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Badge;
import pt.techzebra.winit.client.FacebookPublisher;
import pt.techzebra.winit.platform.DateUtilities;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BadgeActivity extends SherlockActivity {
    private static final String TAG = "BadgeActivity";
    
    public static final String KEY_EXTRA_BADGE = "badge";
    
    private ActionBar action_bar_;
    
    private ImageLoader image_loader_;
    
    private ImageView badge_image_;
    private TextView name_text_;
    private TextView unlocked_date_text_;
    private TextView description_text_;
    
    private boolean facebook_logged_in_;
    
    private Badge badge_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.badge_activity);
        
        action_bar_ = getSupportActionBar();
        action_bar_.setTitle(R.string.badges);
        action_bar_.setDisplayHomeAsUpEnabled(true);
        
        image_loader_ = new ImageLoader(this);
        
        badge_image_ = (ImageView) findViewById(R.id.badge_image);
        name_text_ = (TextView) findViewById(R.id.name_text);
        unlocked_date_text_ = (TextView) findViewById(R.id.unlocked_date_text);
        description_text_ = (TextView) findViewById(R.id.description_text);
        
        SharedPreferences preferences = getSharedPreferences(Constants.USER_PREFERENCES,
              Context.MODE_PRIVATE);
        facebook_logged_in_ = preferences.getBoolean(Constants.PREF_FB_LOGGED_IN, false);
        
        badge_ = (Badge) getIntent().getSerializableExtra(KEY_EXTRA_BADGE);
        
        image_loader_.DisplayImage(badge_.getImage(), badge_image_);
        name_text_.setText(badge_.getName());
        DateUtilities.setDate(unlocked_date_text_, Long.valueOf(badge_.getDate()) * 1000);
        description_text_.setText(badge_.getDescription());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        MenuItem item = menu.findItem(R.id.menu_share);
        item.setEnabled(facebook_logged_in_);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_share:
                share();
                break;
            default:
               return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
    
    private boolean share() {
        return FacebookPublisher.publishStory("WinIt", badge_.getName(),
                badge_.getDescription(), "http://techzebra.pt/#tlantic",
                badge_.getImage(), this);
    }
}
