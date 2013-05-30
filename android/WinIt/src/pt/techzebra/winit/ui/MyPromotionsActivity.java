package pt.techzebra.winit.ui;

import java.util.ArrayList;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.origamilabs.library.views.StaggeredGridView;

public class MyPromotionsActivity extends SherlockFragmentActivity {
	private ActionBar action_bar_;
	
	private PagerSlidingTabStrip tabs_;
	private ViewPager pager_;
	private MyPromotionsPagerAdapter adapter_;
	
	Context context_;
	
	static ArrayList<Promotion> promotions_in_trading_ = new ArrayList<Promotion>();
	static ArrayList<Promotion> promotions_won_ = new ArrayList<Promotion>();
	static ArrayList<Promotion> promotions_tradeable_ = new ArrayList<Promotion>();
	
	protected void onCreate(Bundle saved_instance_state) {  
		super.onCreate(saved_instance_state);  
		setContentView(R.layout.my_promotions_activity);
		
		action_bar_ = getSupportActionBar();
		tabs_ = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager_ = (ViewPager) findViewById(R.id.pager);
		adapter_ = new MyPromotionsPagerAdapter(getSupportFragmentManager());
		
		ArrayList<ArrayList<Promotion>> tmp = new ArrayList<ArrayList<Promotion>>();
		tmp = (ArrayList<ArrayList<Promotion>>) getIntent().getSerializableExtra("Promotions");
		promotions_in_trading_ = tmp.get(0);
		promotions_won_ = tmp.get(1);
		promotions_tradeable_ = tmp.get(2);
		
		action_bar_.setTitle(R.string.my_promotions);
		action_bar_.setDisplayHomeAsUpEnabled(true);

		pager_.setAdapter(adapter_);

		tabs_.setViewPager(pager_);
		
		context_ = this;

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_my_promotions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    		case android.R.id.home:
    			onBackPressed();
    			break;
    		case R.id.menu_settings:
    			break;
    		case R.id.menu_log_out:
    			WinIt.logOut(this);
    			break;
    		default:
    			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private static class MyPromotionsPagerAdapter extends FragmentStatePagerAdapter {
	    private final String[] TITLES = {"Won", "In Trading", "Tradeable"};
	    
		public MyPromotionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
		    SherlockFragment fragment = new MyPromotionsStepFragment();
		    Bundle args = new Bundle();
            args.putInt("step", position);
            fragment.setArguments(args);
            return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
		    return TITLES[position];
		}
		
		@Override
		public int getCount() {
			return TITLES.length;
		}
		
		@Override
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
		    if (object != null) {
		        return ((Fragment) object).getView() == view;
		    }
		    
		    return false;
		}
	}


	public static class MyPromotionsStepFragment extends SherlockFragment {
	    private Activity activity_;
	    
	    private int step_;
	    private int affinity_;
	    
	    private StaggeredGridView staggered_view_;
	    private StaggeredAdapter staggered_adapter_;

	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        activity_ = activity;
	    }
	    
		@Override
		public void onCreate(Bundle saved_instance_state) {
			super.onCreate(saved_instance_state);

			step_ = getArguments() != null ? getArguments().getInt("step") : 0;
			
			ArrayList<Promotion> promotions = null;
			switch (step_) {
                case 0:
                    promotions = promotions_won_;
                    affinity_ = PromotionActivity.WON_PROMOTION;
                    break;
                case 1:
                    promotions = promotions_in_trading_;
                    affinity_ = PromotionActivity.IN_TRADING_PROMOTION;
                    break;
                case 2:
                    promotions = promotions_tradeable_;
                    affinity_ = PromotionActivity.TRADEABLE_PROMOTION;
                    break;
			}
			staggered_adapter_ = new StaggeredAdapter(getActivity(), R.id.list_image, promotions);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved_instance_state) {
			View root_view = inflater.inflate(R.layout.my_promotions_fragment, container, false);
			
			int margin = activity_.getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
			
			staggered_view_ = (StaggeredGridView) root_view.findViewById(R.id.staggered_view);
			staggered_view_.setItemMargin(margin);
			staggered_view_.setPadding(margin, 0, margin, 0);
			staggered_view_.setSelector(R.drawable.highlight_overlay);
			staggered_view_.setAdapter(staggered_adapter_);
			staggered_adapter_.notifyDataSetChanged();
			
			staggered_view_.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
                @Override
                public void onItemClick(StaggeredGridView parent, View view, int position,
                        long id) {
                    Promotion promotion = staggered_adapter_.getItem(position);
                    
                    Intent intent = new Intent(activity_, PromotionActivity.class);
                    intent.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, affinity_);
                    intent.putExtra(PromotionActivity.KEY_PROMOTION_ID, promotion.getPromotionID());
                    activity_.startActivity(intent);
                }
            });
			
			return root_view;
		}
	}
}
