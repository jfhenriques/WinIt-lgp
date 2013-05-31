package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.games.quiz.QuizActivity;
import pt.techzebra.winit.platform.DownloadImageTask;
import pt.techzebra.winit.platform.FetchPromotionInfoTask;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PromotionActivity extends SherlockActivity {
    private static final String TAG = "PromotionActivity";
    
    public static String KEY_PROMOTION_AFFINITY = "promotion_affinity";
    public static String KEY_PROMOTION_ID = "promotion_id";
	
	private PromotionView promotion_view_;
	
    public static final int WON_PROMOTION = 1;
    public static final int IN_TRADING_PROMOTION = 2;
    public static final int TRADEABLE_PROMOTION = 3;
    public static final int PLAYABLE_PROMOTION = 4;
    public static final int PROPOSABLE_PROMOTION = 5;

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.promotion_activity);
		
		Bundle extras = getIntent().getExtras();
		int affinity = extras.getInt(KEY_PROMOTION_AFFINITY);
		int id = extras.getInt(KEY_PROMOTION_ID);
		
		promotion_view_ = PromotionView.createView(this, affinity, id);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    return promotion_view_.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            break;
	        default:
	            return promotion_view_.onOptionsItemSelected(item);
	    }
	    
	    return true;
	}
	
	public static abstract class PromotionView implements FetchPromotionInfoTask.AsyncResponse {
	    protected SherlockActivity activity_;
	    protected ActionBar action_bar_;
	    
	    protected Promotion promotion_;
	    
	    protected TextView name_text_;
        protected TextView description_text_;
	    
        private int title_id_;
        private int menu_id_;
        
        
	    public PromotionView(SherlockActivity activity, int title_id, int promotion_id, int menu_id) {
	        activity_ = activity;
	        title_id_ = title_id;
	        menu_id_ = menu_id;
	        
	        initializeActionBar();
	        initializeFields();

	        fetchPromotion(promotion_id);
        }
	    
	    protected void initializeActionBar() {
	        action_bar_ = activity_.getSupportActionBar();
	        action_bar_.setTitle(title_id_);
	        action_bar_.setDisplayHomeAsUpEnabled(true);
	    }
	    
	    protected abstract int getAffinity();
	    
	    public static PromotionView createView(SherlockActivity activity, int promotion_affinity, int promotion_id) {
	        PromotionView promotion_factory = null;
	        
	        switch (promotion_affinity) {
	            case WON_PROMOTION:
	                promotion_factory = new WonPromotion(activity, promotion_id);
	                break;
	            case IN_TRADING_PROMOTION:
	                promotion_factory = new InTradingPromotion(activity, promotion_id);
	                break;
	            case TRADEABLE_PROMOTION:
	                promotion_factory = new TradeablePromotion(activity, promotion_id);
	                break;
	            case PLAYABLE_PROMOTION:
	                promotion_factory = new PlayablePromotion(activity, promotion_id);
	                break;
	            case PROPOSABLE_PROMOTION:
	                promotion_factory = new ProposablePromotion(activity, promotion_id);
	                break;
	        }
	        
	        return promotion_factory;
	    }
	    
	    public void initializeFields() {
	        name_text_ = (TextView) activity_.findViewById(R.id.name_text);
            description_text_ = (TextView) activity_.findViewById(R.id.description_text);
	    }
	    
	    public void fetchPromotion(int id) {
	        FetchPromotionInfoTask fetch_promotion_info_task = new FetchPromotionInfoTask(activity_, getAffinity());
	        fetch_promotion_info_task.setDelegate(this);
	        fetch_promotion_info_task.execute(id);
	    }

	    public void populateFields() {
	        new DownloadImageTask((ImageView) activity_.findViewById(R.id.image_view))
            .execute(promotion_.getImageUrl());
	        
	        name_text_.setText(promotion_.getName());
            description_text_.setText(promotion_.getDescription());
	    }
	    
	    public boolean onCreateOptionsMenu(Menu menu) {
	        activity_.getSupportMenuInflater().inflate(menu_id_, menu);
            return true;
	    }
	    
	    public abstract boolean onOptionsItemSelected(MenuItem item);
	    
	    @Override
	    public void processFinish(Promotion result) {
	        promotion_ = result;
            populateFields();
	    }
	}
	
	public static class WonPromotion extends PromotionView {
	    public WonPromotion(SherlockActivity activity, int id) {
            super(activity, R.string.promotions, id, R.menu.menu_won_promotion);
        }

	    @Override
        public void initializeFields() {
            super.initializeFields();
        }
	    
        @Override
        public void populateFields() {
            super.populateFields();
        }
	    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_show:
                    Utilities.showToast(activity_, "Show");
                    break;
            }
            
            return true;
        }

        @Override
        protected int getAffinity() {
            return WON_PROMOTION;
        }    
	}
	
	public static class InTradingPromotion extends PromotionView {

        public InTradingPromotion(SherlockActivity activity, int id) {
            super(activity, R.string.promotion, id, R.menu.menu_in_trading);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_cancel:
                    Utilities.showToast(activity_, "Cancel");
                    break;
            }
            
            return true;
        }

        @Override
        protected int getAffinity() {
            return IN_TRADING_PROMOTION;
        }
	    
	}
	
	public static class TradeablePromotion extends PromotionView {
        public TradeablePromotion(SherlockActivity activity, int id) {
            super(activity, R.string.promotions, id, R.menu.menu_tradeable);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_redeem:
                    Utilities.showToast(activity_, "Redeem");
                    break;
                case R.id.menu_trade:
                    Utilities.showToast(activity_, "Trade");
                    break;
            }
            
            return true;
        }

        @Override
        protected int getAffinity() {
            return TRADEABLE_PROMOTION;
        }
	}
	
	public static class PlayablePromotion extends PromotionView {
        private TextView end_date_text_;
	    private TextView win_points_text_;
	    
	    public PlayablePromotion(SherlockActivity activity, int id) {
            super(activity, R.string.promotion, id, R.menu.menu_playable);
        }

	    @Override
	    protected void initializeActionBar() {
	        super.initializeActionBar();
	        action_bar_.setBackgroundDrawable(activity_.getResources().getDrawable(R.drawable.action_bar_bg_single_player));
	    }
	    
	    @Override
        public void initializeFields() {
	        super.initializeFields();
	        
	        end_date_text_ = (TextView) activity_.findViewById(R.id.end_date_text);
	        win_points_text_ = (TextView) activity_.findViewById(R.id.win_points_text);
	        
	        activity_.findViewById(R.id.playable_promotion_details).setVisibility(View.VISIBLE);
        }
	    
	    @Override
        public void populateFields() {
	        super.populateFields();
	        
	        long end_date = promotion_.getEndDate();
	        if (end_date == 0) {
	            end_date_text_.setVisibility(View.GONE);
	            activity_.findViewById(R.id.end_date_label).setVisibility(View.GONE);
	        } else {
	            end_date_text_.setText(Utilities.convertUnixTimestamp(end_date));
	        }
            
            win_points_text_.setText(Integer.toString(promotion_.getWinPoints()));
        }
	    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_play:
                    Intent intent = new Intent(activity_, QuizActivity.class);
                    intent.putExtra("Promotion", promotion_);
                    activity_.startActivity(intent);
                    break;
            }
            
            return true;
        }

        @Override
        protected int getAffinity() {
            return PLAYABLE_PROMOTION;
        }
	}
	
	public static class ProposablePromotion extends PromotionView {
	    private TextView owner_text_;
	    
	    public ProposablePromotion(SherlockActivity activity, int id) {
            super(activity, R.string.promotion, id, R.menu.menu_proposable);
        }
	    
	    @Override
	    protected void initializeActionBar() {
	        super.initializeActionBar();
	        action_bar_.setBackgroundDrawable(activity_.getResources().getDrawable(R.drawable.action_bar_bg_trading));
	    }

	    @Override
        public void initializeFields() {
	        super.initializeFields();
	        
	        owner_text_ = (TextView) activity_.findViewById(R.id.owner_name_text);
	        
	        activity_.findViewById(R.id.proposable_promotion_details).setVisibility(View.VISIBLE);
        }
	    
        @Override
        public void populateFields() {
            super.populateFields();
            
            owner_text_.setText("Hello World");
        }
	    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_propose:
                    Intent intent = new Intent(activity_, TradeablePromotionsFragmentActivity.class);
                    intent.putExtra("Promotion", promotion_);
                    activity_.startActivity(intent);
                    break;
            }

            return true;
        }

        @Override
        protected int getAffinity() {
            return PROPOSABLE_PROMOTION;
        }
	}
}
