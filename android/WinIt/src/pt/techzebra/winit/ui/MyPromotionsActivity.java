package pt.techzebra.winit.ui;

import java.util.ArrayList;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class MyPromotionsActivity extends SherlockFragmentActivity implements OnItemClickListener {

	private ActionBar action_bar_;
	static ViewPager view_pager_;
	private MyPromotionsPagerAdapter pager_adapter_;
	StaggeredGridView staggered_grid_view_won;
	StaggeredGridView staggered_grid_view_in_trading;
	StaggeredAdapter adapter_won_;
	StaggeredAdapter adapter_in_trading_;
	TextView page_title_;
	String auth_token;
	Context mContext_;
	
	ArrayList<Promotion> promotions_in_trading_ = new ArrayList<Promotion>();
	ArrayList<Promotion> promotions_won_ = new ArrayList<Promotion>();
	
	private static final int NUM_STEPS_ = 2;
	

	protected void onCreate(Bundle savedInstanceState) {  

		super.onCreate(savedInstanceState);  
		setContentView(R.layout.my_promotions_activity); 
		ArrayList<ArrayList<Promotion>> tmp = new ArrayList<ArrayList<Promotion>>();
		tmp = (ArrayList<ArrayList<Promotion>>) getIntent().getSerializableExtra("Promotions");
		promotions_in_trading_ = tmp.get(0);
		promotions_won_ = tmp.get(1);
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.my_promotions);
		action_bar_.setDisplayHomeAsUpEnabled(true);

		
		pager_adapter_ = new MyPromotionsPagerAdapter(getSupportFragmentManager());

		view_pager_ = (ViewPager) findViewById(R.id.pager);
		view_pager_.setOffscreenPageLimit(1);
		view_pager_.setAdapter(pager_adapter_);
		
		adapter_in_trading_ = new StaggeredAdapter(this,R.id.list_image, promotions_in_trading_);
		adapter_won_ = new StaggeredAdapter(this,R.id.list_image, promotions_won_);
		
		auth_token = WinIt.getAuthToken();
		mContext_ = this;

	}  

	@Override
	public void onBackPressed(){
		super.onBackPressed();
		finish();
	}


	private static class MyPromotionsPagerAdapter extends FragmentStatePagerAdapter {

		public MyPromotionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return MyPromotionsStepFragment.newInstance(i);
		}

		@Override
		public int getCount() {
			return NUM_STEPS_;
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
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			step_ = getArguments() != null ? getArguments().getInt("step") : 0;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved_instance_state) {
			View v = inflater.inflate(step_ == 0 ? R.layout.my_promotions_fragment
                    : R.layout.my_promotions_fragment , container, false);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			SherlockFragmentActivity activity = getSherlockActivity();

			int margin = activity.getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
			if(step_ == 0){
				((MyPromotionsActivity) activity).page_title_ = (TextView) activity.findViewById(R.id.page_title);
				((MyPromotionsActivity) activity).page_title_.setText("Promotions In Trading");
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading = (StaggeredGridView) activity.findViewById(R.id.listMyPromotions);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setItemMargin(margin);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setPadding(margin, 0, margin, 0);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setSelector(R.drawable.highlight_overlay);
				((MyPromotionsActivity) activity).staggered_grid_view_in_trading.setAdapter(((MyPromotionsActivity) activity).adapter_in_trading_);
				((MyPromotionsActivity) activity).adapter_in_trading_.notifyDataSetChanged();
			}
			else{
				((MyPromotionsActivity) activity).page_title_ = (TextView) activity.findViewById(R.id.page_title);
				((MyPromotionsActivity) activity).page_title_.setText("Won Promotions");
				((MyPromotionsActivity) activity).staggered_grid_view_won = (StaggeredGridView) activity.findViewById(R.id.listMyPromotions);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setItemMargin(margin);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setPadding(margin, 0, margin, 0);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setSelector(R.drawable.highlight_overlay);
				((MyPromotionsActivity) activity).staggered_grid_view_won.setAdapter(((MyPromotionsActivity) activity).adapter_won_);
				((MyPromotionsActivity) activity).adapter_won_.notifyDataSetChanged();
			}
		
		}
		
	}


	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		//TODO
	}
}


