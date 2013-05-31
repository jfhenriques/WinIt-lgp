package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.platform.FetchPromotionsTask;
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
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class TradingActivity extends SherlockFragmentActivity {
    private static final String TAG = "TradingActivity";
    
    public static final String KEY_EXTRA_PROMOTIONS = "promotions";
    
    private Context context_;
    
    private ActionBar action_bar_;
    
    private PagerSlidingTabStrip tabs_;
    private ViewPager pager_;
    private TradingPromotionsPagerAdapter adapter_;
    
    static ArrayList<Promotion> proposable_promotions_ = new ArrayList<Promotion>();
    static ArrayList<Promotion> received_proposals_ = new ArrayList<Promotion>();
    static ArrayList<Promotion> sent_proposals_ = new ArrayList<Promotion>();

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.promotions_list_activity);
        
        action_bar_ = getSupportActionBar();
        tabs_ = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager_ = (ViewPager) findViewById(R.id.pager);
        adapter_ = new TradingPromotionsPagerAdapter(getSupportFragmentManager());
        
        action_bar_.setTitle(R.string.trading);
        action_bar_.setDisplayHomeAsUpEnabled(true);
        action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_trading));
        
        ArrayList<ArrayList<Promotion>> temp = new ArrayList<ArrayList<Promotion>>();
        temp = (ArrayList<ArrayList<Promotion>>) getIntent().getSerializableExtra(KEY_EXTRA_PROMOTIONS);
        proposable_promotions_ = temp.get(0);
        //received_proposals_ = temp.get(1);
        //sent_proposals_ = temp.get(2);
        
        
        pager_.setAdapter(adapter_);
        
        tabs_.setViewPager(pager_);
        
        context_ = this;
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_log_out:
                WinIt.logOut(this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    
    private static class TradingPromotionsPagerAdapter extends FragmentStatePagerAdapter {
        private final String[] TITLES = {"Available", "Received Proprosals", "Sent Proposals"};
        
        public TradingPromotionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        
        @Override
        public Fragment getItem(int position) {
            SherlockFragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ProposablePromotions();
                    break;
                case 1:
                    fragment = new ReceivedProposals();
                    break;
                case 2:
                    fragment = new SentProposals();
                    break;
            }
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
    
    public static class ProposablePromotions extends SherlockFragment implements FetchPromotionsTask.AsyncResponse, OnItemClickListener {
        private Activity activity_;
        
        private StaggeredGridView staggered_grid_view_;
        private StaggeredAdapter adapter_;
        
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            activity_ = activity;
        }
        
        @Override
        public void onCreate(Bundle saved_instance_state) {
            super.onCreate(saved_instance_state);
            adapter_ = new StaggeredAdapter(activity_, R.id.staggered_adapter, proposable_promotions_);
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle saved_instance_state) {
            View view_root = inflater.inflate(R.layout.promotions_activity, container, false);
            
            int margin = activity_.getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
            
            staggered_grid_view_ = (StaggeredGridView) view_root.findViewById(R.id.promotions_staggered_view);
            staggered_grid_view_.setItemMargin(margin);
            staggered_grid_view_.setPadding(margin, 0, margin, 0);
            staggered_grid_view_.setSelector(R.drawable.highlight_overlay);
            staggered_grid_view_.setAdapter(adapter_);
            adapter_.notifyDataSetChanged();

            staggered_grid_view_.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
                @Override
                public void onItemClick(StaggeredGridView parent, View view, int position,
                        long id) {
                    Promotion promotion = adapter_.getItem(position);
                    
                    Intent intent = new Intent(activity_, PromotionActivity.class);
                    intent.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PROPOSABLE_PROMOTION);
                    intent.putExtra(PromotionActivity.KEY_PROMOTION_ID, promotion.getPromotionID());
                    intent.putExtra("pcid", promotion.getPcid());
                    activity_.startActivity(intent);
                }
            });
            
            return view_root;
        }
        
        @Override
        public void onActivityCreated(Bundle saved_instance_state) {
            super.onActivityCreated(saved_instance_state);
            
            adapter_.notifyDataSetChanged();
        }
        
        @Override
        public void processFinish(ArrayList<Promotion> result) {
            proposable_promotions_.addAll(result);
            adapter_.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(StaggeredGridView parent, View view,
                int position, long id) {
            Promotion promotion = adapter_.getItem(position);
            Intent intent = new Intent(activity_, PromotionActivity.class);
            intent.putExtra(PromotionActivity.KEY_PROMOTION_AFFINITY, PromotionActivity.PROPOSABLE_PROMOTION);
            intent.putExtra(PromotionActivity.KEY_PROMOTION_ID, promotion.getPromotionID());
            intent.putExtra("pcid", promotion.getPcid());
            activity_.startActivity(intent);
        }
    }
    
    public static class ReceivedProposals extends SherlockFragment {
        // TODO
    }
    
    public static class SentProposals extends SherlockFragment {
        // TODO
    }
}
