package pt.techzebra.winit.games.quiz;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.FacebookPublisher;
import pt.techzebra.winit.client.Promotion;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class QuizResultActivity extends SherlockActivity {
    public static final String KEY_QUIZ_RESULT = "result";
    public static final String KEY_QUIZ_NUM_CORRECT_ANSWERS = "num_correct_answers";
    public static final String KEY_QUIZ_POINTS = "points";
    
    private ActionBar action_bar_;
    
    private TextView result_text_;
    private TextView details_text_;
    Promotion promotion_;
    
    private boolean facebook_logged_in_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.quiz_result);
        
        action_bar_ = getSupportActionBar();
        action_bar_.setTitle("Quiz Game");
        action_bar_.setDisplayHomeAsUpEnabled(true);
        action_bar_.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.action_bar_bg_single_player));
        
        SharedPreferences preferences = getSharedPreferences(Constants.USER_PREFERENCES,
                Context.MODE_PRIVATE);
        facebook_logged_in_ = preferences.getBoolean(Constants.PREF_FB_LOGGED_IN, false);
        result_text_ = (TextView) findViewById(R.id.result_text);
        details_text_ = (TextView) findViewById(R.id.details_text);
        
        Bundle extras = getIntent().getExtras();
        boolean result = extras.getBoolean(KEY_QUIZ_RESULT);
        int num_correct_answers = extras.getInt(KEY_QUIZ_NUM_CORRECT_ANSWERS);
        @SuppressWarnings("unused")
		int points = extras.getInt(KEY_QUIZ_POINTS);
        promotion_ = (Promotion) extras.getSerializable("Promotion");
        result_text_.setText(result ? R.string.you_won : R.string.you_lost);
        details_text_.setText(getResources().getQuantityString(R.plurals.numberOfQuestionsCorrectlyAnswered, num_correct_answers, num_correct_answers));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
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
        		FacebookPublisher.publishStory("Promotion unlocked!", promotion_.getName(), promotion_.getDescription(), "http://techzebra.pt", promotion_.getImageUrl(), this);
        		Toast.makeText(this, "Publishing on Facebook...", Toast.LENGTH_SHORT).show();
            		break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
