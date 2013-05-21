package pt.techzebra.winit.ui;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.PromGame;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.games.quiz.QuizActivity;
import pt.techzebra.winit.platform.DownloadImageTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PromotionActivity extends SherlockActivity {
    private ActionBar action_bar_;
    
	private TextView name_text_;
	private TextView description_text_;
	private TextView end_date_text_;
	private TextView win_points_text_;
	
	private Promotion promotion_;
	private String auth_token_;

	private static final String TAG = "PromotionActivity";

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.promotion_activity);
		
		
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotion);
		action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_single_player));
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		promotion_ = (Promotion) getIntent().getSerializableExtra("Promotion");
		if (promotion_.getImageUrl() == null){
			promotion_.setImageUrl("http://www.clker.com/cliparts/b/7/7/c/12247843801937558056schoolfreeware_Cancel.svg.med.png");
		}
		
        SharedPreferences preferences_editor = PromGame.getAppContext()
                .getSharedPreferences(Constants.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

        auth_token_ = preferences_editor.getString(Constants.PREF_AUTH_TOKEN,
                null);
		
		name_text_ = (TextView) findViewById(R.id.name_text);
		description_text_ = (TextView) findViewById(R.id.description_text);
		end_date_text_ = (TextView) findViewById(R.id.end_date_text);
		win_points_text_ = (TextView) findViewById(R.id.win_points_text);

		new DownloadImageTask((ImageView) findViewById(R.id.image_view))
		.execute(promotion_.getImageUrl());

		name_text_.setText(promotion_.getName());
		description_text_.setText(promotion_.getDescription());
		//TODO tornar isto numa data
		//end_date_text_.setText(p.getEndDate());
		win_points_text_.setText(Integer.toString(promotion_.getWinPoints()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.menu_promotion, menu);
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            break;
	        case R.id.menu_play:
	            Intent intent = new Intent(this, QuizActivity.class);
	            startActivity(intent);
	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    
	    return true;
	}
}