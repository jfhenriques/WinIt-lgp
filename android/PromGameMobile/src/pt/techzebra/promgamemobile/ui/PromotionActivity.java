package pt.techzebra.promgamemobile.ui;

import java.io.InputStream;
import java.util.Date;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.games.quiz.QuizActivity;
import pt.techzebra.promgamemobile.platform.DownloadImageTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class PromotionActivity extends SherlockActivity {

	Promotion p;
	private String auth_token;
	private ActionBar action_bar_;
	private TextView name_text_;
	private TextView description_text_;
	private TextView end_date_text_;
	private TextView win_points_text_;

	private static final String TAG = "PromotionActivity";

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotion);
		setContentView(R.layout.promotion_activity);
		
		p = (Promotion) getIntent().getSerializableExtra("Promotion");
		if (p.getImageUrl() == null){
			p.setImageUrl("http://www.clker.com/cliparts/b/7/7/c/12247843801937558056schoolfreeware_Cancel.svg.med.png");
		}
		
		SharedPreferences preferences_editor = PromGame.getAppContext()
				.getSharedPreferences(Constants.USER_PREFERENCES,
						Context.MODE_PRIVATE);

		auth_token = preferences_editor.getString(Constants.PREF_AUTH_TOKEN,
				null);
		
		name_text_ = (TextView) findViewById(R.id.name_text);
		description_text_ = (TextView) findViewById(R.id.description_text);
		end_date_text_ = (TextView) findViewById(R.id.end_date_text);
		win_points_text_ = (TextView) findViewById(R.id.win_points_text);

		new DownloadImageTask((ImageView) findViewById(R.id.image_view))
		.execute(p.getImageUrl());

		name_text_.setText(p.getName());
		description_text_.setText(p.getDescription());
		//TODO tornar isto numa data
		//end_date_text_.setText(p.getEndDate());
		win_points_text_.setText(Integer.toString(p.getWinPoints()));


	}
	
	public void play(View view) {
	    Intent intent = new Intent(this, QuizActivity.class);
	    startActivity(intent);
	}
}
