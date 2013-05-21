package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.games.quiz.QuizActivity;
import pt.techzebra.winit.platform.DownloadImageTask;
import pt.techzebra.winit.platform.FetchPromotionInfoTask;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	
	public static class PromotionAffinity {
	    public static final int AVAILABLE_PROMOTION = 1;
	    public static final int OWNED_PROMOTION = 2;
	    public static final int TRADEABLE_PROMOTION = 3;
	}
	
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
	
	public static abstract class PromotionView {
	    protected SherlockActivity activity_;
	    protected ActionBar action_bar_;
	    
	    protected Promotion promotion_;
	    
	    protected TextView name_text_;
        protected TextView description_text_;
	    
	    public PromotionView(SherlockActivity activity, int id) {
	        activity_ = activity;
	        
	        initializeActionBar();
	        initializeFields();

	        fetchPromotion(id);
        }
	    
	    protected void initializeActionBar() {
	        action_bar_ = activity_.getSupportActionBar();
	        action_bar_.setTitle(R.string.promotion);
	        action_bar_.setDisplayHomeAsUpEnabled(true);
	    }
	    
	    public static PromotionView createView(SherlockActivity activity, int promotion_affinity, int promotion_id) {
	        PromotionView promotion_factory = null;
	        
	        switch (promotion_affinity) {
	            case PromotionAffinity.AVAILABLE_PROMOTION:
	                Log.d(TAG, "creating an available promotion");
	                Log.d(TAG, Boolean.toString(activity != null));
	                promotion_factory = new AvailablePromotion(activity, promotion_id);
	                break;
	            case PromotionAffinity.OWNED_PROMOTION:
	                promotion_factory = new OwnedPromotion(activity, promotion_id);
	                break;
	            case PromotionAffinity.TRADEABLE_PROMOTION:
	                promotion_factory = new TradeablePromotion(activity, promotion_id);
	                break;
	        }
	        
	        return promotion_factory;
	    }
	    
	    public void initializeFields() {
	        name_text_ = (TextView) activity_.findViewById(R.id.name_text);
            description_text_ = (TextView) activity_.findViewById(R.id.description_text);
	    }
	    
	    public abstract void fetchPromotion(int id);
	    
	    public void populateFields() {
	        name_text_.setText(promotion_.getName());
            description_text_.setText(promotion_.getDescription());
	    }
	    
	    public abstract boolean onCreateOptionsMenu(Menu menu);
	    public abstract boolean onOptionsItemSelected(MenuItem item);
	}
	
	public static class OwnedPromotion extends PromotionView {
	    public OwnedPromotion(SherlockActivity activity, int id) {
            super(activity, id);
            Log.d(TAG, "OwnedPromotion constructor");
        }

	    @Override
        public void initializeFields() {
            
        }

	    @Override
	    public void fetchPromotion(int id) {
            new FetchPromotionInfoTask(activity_, PromotionAffinity.OWNED_PROMOTION) {
                @Override
                public void callMainWindow(Promotion result) {
                    promotion_ = result;
                    populateFields();
                }                
            }.execute(id);
	    }
	    
        @Override
        public void populateFields() {
            
        }   
	    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            return false;
        }
	    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return false;
        }    
	}
	
	public static class AvailablePromotion extends PromotionView {
        private TextView end_date_text_;
	    private TextView win_points_text_;
	    
	    public AvailablePromotion(SherlockActivity activity, int id) {
            super(activity, id);
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
	        
	        activity_.findViewById(R.id.tradeable_promotion_details).setVisibility(View.GONE);
        }
	    
	    @Override
	    public void fetchPromotion(int id) {
            new FetchPromotionInfoTask(activity_, PromotionAffinity.AVAILABLE_PROMOTION) {
                @Override
                public void callMainWindow(Promotion result) {
                    promotion_ = result;
                    populateFields();
                }
            }.execute(id);
	    }
	    
	    @Override
        public void populateFields() {
	        super.populateFields();
	        
            new DownloadImageTask((ImageView) activity_.findViewById(R.id.image_view))
            .execute(promotion_.getImageUrl());
            end_date_text_.setText(Utilities.convertUnixTimestamp(promotion_.getEndDate()));
            win_points_text_.setText(Integer.toString(promotion_.getWinPoints()));
        }
	    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
	        activity_.getSupportMenuInflater().inflate(R.menu.menu_promotion, menu);
	        return true;
        }
	    
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_play:
                    Intent intent = new Intent(activity_, QuizActivity.class);
                    activity_.startActivity(intent);
                    break;
            }
            
            return true;
        }
	}
	
	public static class TradeablePromotion extends PromotionView {
	    private TextView owner_text_;
	    
	    public TradeablePromotion(SherlockActivity activity, int id) {
            super(activity, id);
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
	        
	        activity_.findViewById(R.id.unowned_promotion_details).setVisibility(View.GONE);
        }

	    @Override
	    public void fetchPromotion(int id) {
	        new FetchPromotionInfoTask(activity_, PromotionAffinity.TRADEABLE_PROMOTION) {
	            @Override
	            public void callMainWindow(Promotion result) {
	                promotion_ = result;
	                populateFields();
	            }
	        }.execute(id);
	    }
	    
        @Override
        public void populateFields() {
            super.populateFields();
            
            new DownloadImageTask((ImageView) activity_.findViewById(R.id.image_view))
            .execute(promotion_.getImageUrl());
            
            owner_text_.setText("Hello World");
        }
	    
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            activity_.getSupportMenuInflater().inflate(R.menu.menu_trading, menu);
            return true;
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
	}
}
