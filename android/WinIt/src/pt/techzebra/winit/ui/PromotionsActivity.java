package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.platform.FetchPromotionsTask;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class PromotionsActivity extends SherlockActivity implements OnItemClickListener {
	private static final String TAG = "PromotionsActivity";

	public static String KEY_SHOWCASE_MODE = "showcase_mode";

	private PromotionsShowcase promotions_showcase_;

	public static final int SINGLE_PLAYER_SHOWCASE = 1;
    public static final int COOPERATIVE_SHOWCASE = 2;
    public static final int COMPETITIVE_SHOWCASE = 3;

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.promotions_activity);

		int mode = getIntent().getExtras().getInt(KEY_SHOWCASE_MODE);
		promotions_showcase_ = PromotionsShowcase.createShowcase(this, mode);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		

		promotions_showcase_.fetchPromotions();
		promotions_showcase_.populateFields();
	}

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		promotions_showcase_.onItemClick(parent, view, position, id);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private static abstract class PromotionsShowcase implements FetchPromotionsTask.AsyncResponse {
		protected SherlockActivity activity_;
		protected ActionBar action_bar_;

		protected ArrayList<Promotion> promotions_ = new ArrayList<Promotion>();
		
		protected StaggeredGridView staggered_grid_view_;
		protected StaggeredAdapter adapter_ = null;

		public PromotionsShowcase(SherlockActivity activity) {
			activity_ = activity;
			initializeActionBar();
			initializeFields();
		}

		public static PromotionsShowcase createShowcase(SherlockActivity activity, int showcase_mode) {
			PromotionsShowcase promotions_showcase = null;

			switch (showcase_mode) {
    			case SINGLE_PLAYER_SHOWCASE:
    				promotions_showcase = new SinglePlayerPromotionsShowcase(activity);
    				break;
    			case COOPERATIVE_SHOWCASE:
    			    // TODO
    			    break;
    			case COMPETITIVE_SHOWCASE:
    			    // TODO
    			    break;
			}

			return promotions_showcase;
		}

		protected void initializeActionBar() {
			action_bar_ = activity_.getSupportActionBar();
			action_bar_.setTitle(R.string.promotions);
			action_bar_.setDisplayHomeAsUpEnabled(true);
		}

		public void initializeFields() {
			staggered_grid_view_ = (StaggeredGridView) activity_.findViewById(R.id.promotions_staggered_view);
			int margin = activity_.getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
			staggered_grid_view_.setItemMargin(margin);
			staggered_grid_view_.setPadding(margin, 0, margin, 0);
			staggered_grid_view_.setSelector(R.drawable.highlight_overlay);
			staggered_grid_view_.setOnItemClickListener((OnItemClickListener) activity_);
		}

		public abstract void fetchPromotions();

		public void populateFields() {
			if( adapter_ != null)
				adapter_.clear();
			
			adapter_ = new StaggeredAdapter(activity_, R.id.staggered_adapter, promotions_);
			staggered_grid_view_.setAdapter(adapter_);
			adapter_.notifyDataSetChanged();
		}

		public abstract void onItemClick(StaggeredGridView parent, View view, int position,
				long id);
	}

	public static class SinglePlayerPromotionsShowcase extends PromotionsShowcase {
		public SinglePlayerPromotionsShowcase(SherlockActivity activity) {
			super(activity);
		}

		@Override
		protected void initializeActionBar() {
			super.initializeActionBar();

			action_bar_.setBackgroundDrawable(activity_.getResources().getDrawable(R.drawable.action_bar_bg_single_player));
		}

		@Override
		public void fetchPromotions() {
			FetchPromotionsTask fetch_promotions_task = new FetchPromotionsTask(activity_, FetchPromotionsTask.PLAYABLE_PROMOTIONS);
			fetch_promotions_task.setDelegate(this);
			fetch_promotions_task.execute();  
		}

		@Override
		public void onItemClick(StaggeredGridView parent, View view,
				int position, long id) {
			Promotion promotion = adapter_.getItem(position);
			Intent intent = new Intent(activity_, PromotionActivity.class);
			intent.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PLAYABLE_PROMOTION);
			intent.putExtra(PromotionActivity.KEY_PROMOTION_ID, promotion.getPromotionID());
			intent.putExtra(PromotionActivity.KEY_ACTIVE_UPID, promotion.getActiveUPID());
			activity_.startActivity(intent);
		}

		@Override
		public void processFinish(ArrayList<Promotion> result) {
			promotions_.addAll(result);
			adapter_.notifyDataSetChanged();
		}
	}
	
	/*
	 * TODO
	 */
	public static class CooperativePromotionsShowcase extends PromotionsShowcase {

        public CooperativePromotionsShowcase(SherlockActivity activity) {
            super(activity);
        }

        @Override
        public void processFinish(ArrayList<Promotion> result) {
            
        }

        @Override
        public void fetchPromotions() {
            
        }

        @Override
        public void onItemClick(StaggeredGridView parent, View view,
                int position, long id) {
            
        }
	
	}
	
	/*
	 * TODO
	 */
	public static class CompetitivePromotionsShowcase extends PromotionsShowcase {

        public CompetitivePromotionsShowcase(SherlockActivity activity) {
            super(activity);
        }

        @Override
        public void processFinish(ArrayList<Promotion> result) {            
        }

        @Override
        public void fetchPromotions() {
        }

        @Override
        public void onItemClick(StaggeredGridView parent, View view,
                int position, long id) {
            
        }
    }
}
