package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.client.Proposal;
import pt.techzebra.winit.client.TradingContainer;
import pt.techzebra.winit.platform.FetchPromotionsTask;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class TradingActivity extends SherlockFragmentActivity {
    private static final String TAG = "TradingActivity";
    
    public static final String KEY_EXTRA_TRADING_CONTAINER = "trading_container";
    
    public static final String KEY_EXTRA_PROMOTIONS = "promotions";
    public static final String KEY_EXTRA_RECEIVED_PROPOSALS = "received_proposals";
    public static final String KEY_EXTRA_SENT_PROPOSALS = "sent_proposals";
        
    private ActionBar action_bar_;
    
    private PagerSlidingTabStrip tabs_;
    private ViewPager pager_;
    private TradingPromotionsPagerAdapter adapter_;
    
    static ArrayList<Promotion> proposable_promotions_ = new ArrayList<Promotion>();
    static ArrayList<Proposal> received_proposals_ = new ArrayList<Proposal>();
    static ArrayList<Proposal> sent_proposals_ = new ArrayList<Proposal>();

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
        
        TradingContainer container = (TradingContainer) getIntent().getSerializableExtra(KEY_EXTRA_TRADING_CONTAINER);
        
        proposable_promotions_ = container.PROPOSABLE_PROMOTIONS;
        received_proposals_ = container.RECEIVED_PROPOSALS;
        sent_proposals_ = container.SENT_PROPOSALS;
        
        pager_.setAdapter(adapter_);
        
        tabs_.setViewPager(pager_);
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
                    fragment = new ProposablePromotionsFragment();
                    break;
                case 1: // breakthrough
                case 2:
                    fragment = new ProposalsFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt(ProposalsFragment.KEY_VIEW_MODE, position);
                    fragment.setArguments(arguments);
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
    
    public static class ProposablePromotionsFragment extends SherlockFragment implements FetchPromotionsTask.AsyncResponse, OnItemClickListener {
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
    
    public static class ProposalsFragment extends SherlockFragment {
        public static final String KEY_VIEW_MODE = "view_mode";
        
        public static final int MODE_RECEIVED_PROPOSALS = 1;
        public static final int MODE_SENT_PROPOSALS = 2;
        
        private Context context_;
        private int mode_;
        private ListView list_view_;
        private ProposalsAdapter adapter_;
        private AdapterView.OnItemClickListener listener_;
        
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            context_ = activity;
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle saved_instance_state) {
            Bundle arguments = getArguments();
            mode_ = arguments.getInt(KEY_VIEW_MODE);
            if (mode_ == 0) {
                throw new IllegalArgumentException();
            }
            
            View view_root = inflater.inflate(R.layout.proposals_fragment, container, false);
            
            if (mode_ == MODE_RECEIVED_PROPOSALS) {
                listener_ = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                        
                    }
                };
            } else {
                listener_ = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                                                
                    }
                };
            }
            
            list_view_ = (ListView) view_root.findViewById(R.id.list);
            adapter_ = new ProposalsAdapter(context_, mode_ == MODE_RECEIVED_PROPOSALS ? received_proposals_ : sent_proposals_);
            list_view_.setAdapter(adapter_);
            list_view_.setOnItemClickListener(listener_);
            
            
            return view_root;
        }
        
        private static class ProposalsAdapter extends BaseAdapter {
            private ArrayList<Proposal> proposals_;
            private ImageLoader image_loader_;
            
            public ProposalsAdapter(Context context, ArrayList<Proposal> proposals) {
                proposals_ = proposals;
                image_loader_ = new ImageLoader(context);
            }
            
            @Override
            public int getCount() {
                return proposals_.size();
            }
            
            @Override
            public Object getItem(int position) {
                return proposals_.get(position);
            }
            
            @Override
            public long getItemId(int position) {
                return position;
            }
            
            @Override
            public View getView(int position, View convert_view, ViewGroup parent) {
                ViewHolder holder;
                
                View view = convert_view;
                
                if (convert_view == null) {
                    LayoutInflater inflater = LayoutInflater.from(WinIt.getAppContext());
                    
                    view = inflater.inflate(R.layout.proposal_list_row, null);
                    holder = new ViewHolder();
                    holder.my_image = (ImageView) view.findViewById(R.id.my_image);
                    holder.my_name = (TextView) view.findViewById(R.id.my_name);
                    holder.want_image = (ImageView) view.findViewById(R.id.want_image);
                    holder.want_name = (TextView) view.findViewById(R.id.want_name);
                    
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                
                Proposal proposal = proposals_.get(position);
                holder.my_name.setText(proposal.getMyName());
                image_loader_.DisplayImage(proposal.getMyImage(), holder.my_image);
                holder.want_name.setText(proposal.getWantName());
                image_loader_.DisplayImage(proposal.getWantImage(), holder.want_image);
                
                return view;
            }
            
            public class ViewHolder {
                TextView my_name;
                ImageView my_image;
                TextView want_name;
                ImageView want_image;
            }
        }
    }
}
