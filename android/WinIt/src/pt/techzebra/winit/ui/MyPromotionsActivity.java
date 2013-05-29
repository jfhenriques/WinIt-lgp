package pt.techzebra.winit.ui;

import java.util.ArrayList;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
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
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class MyPromotionsActivity extends SherlockFragmentActivity implements OnItemClickListener {

	private ActionBar action_bar_;
	
	private PagerSlidingTabStrip tabs_;
	private ViewPager pager_;
	private MyPromotionsPagerAdapter adapter_;
	
	StaggeredGridView staggered_grid_view_won;
	StaggeredGridView staggered_grid_view_in_trading;
	StaggeredGridView staggered_grid_view_tradeable;
	StaggeredAdapter adapter_tradeable_;
	StaggeredAdapter adapter_won_;
	StaggeredAdapter adapter_in_trading_;

	Context context_;
	
	ArrayList<Promotion> promotions_in_trading_ = new ArrayList<Promotion>();
	ArrayList<Promotion> promotions_won_ = new ArrayList<Promotion>();
	ArrayList<Promotion> promotions_tradeable_ = new ArrayList<Promotion>();
	
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

		pager_.setOffscreenPageLimit(1);
		pager_.setAdapter(adapter_);
		
		adapter_tradeable_ = new StaggeredAdapter(this,R.id.list_image, promotions_tradeable_);
		adapter_in_trading_ = new StaggeredAdapter(this,R.id.list_image, promotions_in_trading_);
		adapter_won_ = new StaggeredAdapter(this,R.id.list_image, promotions_won_);
		
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
			WinIt.clearUserData();
			Intent i = new Intent(this, AuthenticationActivity.class);
			Toast.makeText(this, "Logout successful!", Toast.LENGTH_SHORT).show();
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
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
			return MyPromotionsStepFragment.newInstance(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
		    return TITLES[position];
		}
		
		@Override
		public int getCount() {
			return TITLES.length;
		}
	}


	public static class MyPromotionsStepFragment extends SherlockFragment{
	    private int step_;
	    
		static MyPromotionsStepFragment newInstance(int step) {
			MyPromotionsStepFragment f = new MyPromotionsStepFragment();

			Bundle args = new Bundle();
			args.putInt("step", step);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle saved_instance_state) {
			super.onCreate(saved_instance_state);
			step_ = getArguments() != null ? getArguments().getInt("step") : 0;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved_instance_state) {
			View v = null;
			switch(step_){
    			case 0:
    				v = inflater.inflate(R.layout.my_promotions_fragment_1, container, false);
    				break;
    			case 1:
    				v = inflater.inflate(R.layout.my_promotions_fragment_2, container, false);
    				break;
    			case 2:
    				v = inflater.inflate(R.layout.my_promotions_fragment_3, container, false);
    				break;
			}
			
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			SherlockFragmentActivity activity = getSherlockActivity();

			int margin = activity.getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
			if(step_ == 0) {
				((MyPromotionsActivity) activity).staggered_grid_view_won = (StaggeredGridView) activity.findViewById(R.id.listMyPromotions);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setItemMargin(margin);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setPadding(margin, 0, margin, 0);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setSelector(R.drawable.highlight_overlay);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setAdapter(((MyPromotionsActivity) activity).adapter_won_);
				((MyPromotionsActivity) activity).adapter_won_.notifyDataSetChanged();
			}
			else if(step_ == 1) {
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading = (StaggeredGridView) activity.findViewById(R.id.listMyPromotions2);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setItemMargin(margin);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setPadding(margin, 0, margin, 0);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setSelector(R.drawable.highlight_overlay);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setAdapter(((MyPromotionsActivity) activity).adapter_in_trading_);
				((MyPromotionsActivity) activity).adapter_in_trading_.notifyDataSetChanged();
			}
			else{
				((MyPromotionsActivity) activity).staggered_grid_view_tradeable = (StaggeredGridView) activity.findViewById(R.id.listMyPromotions3);
				((MyPromotionsActivity) activity).staggered_grid_view_tradeable.setItemMargin(margin);
				((MyPromotionsActivity) activity).staggered_grid_view_tradeable.setPadding(margin, 0, margin, 0);
				((MyPromotionsActivity) activity).staggered_grid_view_tradeable.setSelector(R.drawable.highlight_overlay);
				((MyPromotionsActivity) activity).staggered_grid_view_tradeable.setAdapter(((MyPromotionsActivity) activity).adapter_tradeable_);
				((MyPromotionsActivity) activity).adapter_tradeable_.notifyDataSetChanged();
			}
		
		}
		
	}


	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		//TODO
	}
}
