package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.client.User;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class PromotionActivity extends SherlockActivity {

	private static final String TAG = "PromotionActivity";
	
	//int prom_id_;
	private ActionBar action_bar_;
	Promotion promotion;
	
	//Activity variables
	ImageView image_view_;
	TextView name_text_;
	TextView description_text_;
	TextView end_date_text_;
	TextView win_points_text_;
	
	/*
	 * Set up promotion data that is displayed on this activity 
	 */
	public void setPromotionData(Promotion p) throws Exception{
		
		//image_view_.setImageURI(p.getImageUrl());
		
		//Set promotion name
		name_text_.setText(p.getName());
		Log.i(TAG, p.getName());
		//Set promotion description
		description_text_.setText(p.getDescription());
		Log.i(TAG, p.getDescription());
		//set promotion end date
		end_date_text_.setText(p.getEndDate());
		//set promotion win points
		win_points_text_.setText(p.getWinPoints());
	}
	
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotion);
		setContentView(R.layout.promotion_activity);
		
		String promotionid = "1"; 
		promotion = NetworkUtilities.fetchPromotionInformation(promotionid);
		
		name_text_ = (TextView) findViewById(R.id.name_text);
		description_text_ = (TextView) findViewById(R.id.description_text);
		end_date_text_ = (TextView) findViewById(R.id.end_date_text);
		win_points_text_ = (TextView) findViewById(R.id.win_points_text);
		
		if(promotion == null) {
			Log.i(TAG, "lajlsjljf");
		}
		
		try {
			setPromotionData(promotion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Bundle b = getIntent().getExtras();
		//int prom_id_ = b.getInt("key");
		//Log.v(TAG, Integer.toString(prom_id_));
		
		
	}
}
