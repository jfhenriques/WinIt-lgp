package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.PromGame;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.platform.DownloadImageTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class TradingPromotionActivity extends SherlockActivity{
	Promotion p;
	private String auth_token;
	private ActionBar action_bar_;
	private TextView name_text_;
	private TextView description_text_;
	ArrayList<String> promos = new ArrayList<String>();

	private static final String TAG = "TradingPromotionActivity";

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.trading);
		action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_trading));
		setContentView(R.layout.trading_promotion_activity);

		p = (Promotion) getIntent().getSerializableExtra("TradingPromotion");
		if (p.getImageUrl() == null){
			p.setImageUrl("http://www.clker.com/cliparts/b/7/7/c/12247843801937558056schoolfreeware_Cancel.svg.med.png");
		}

		SharedPreferences preferences_editor = PromGame.getAppContext()
				.getSharedPreferences(Constants.USER_PREFERENCES,
						Context.MODE_PRIVATE);

		auth_token = preferences_editor.getString(Constants.PREF_AUTH_TOKEN,
				null);

		name_text_ = (TextView) findViewById(R.id.name_text_trading);
		description_text_ = (TextView) findViewById(R.id.description_text_trading);

		new DownloadImageTask((ImageView) findViewById(R.id.image_view_trading))
		.execute(p.getImageUrl());

		name_text_.setText(p.getName());
		description_text_.setText(p.getDescription());


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.menu_trading, menu);
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            break;
	        case R.id.menu_propose:
	        	Intent i = new Intent(this, ShowPromotionsToTrade.class);
	    		i.putExtra("Promotion", p);
	    		this.startActivity(i);
	            break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    
	    return true;
	}

}
