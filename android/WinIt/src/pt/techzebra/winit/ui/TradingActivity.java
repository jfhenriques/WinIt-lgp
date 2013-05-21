package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.platform.LoadingOtherUserPromotionsToTrade;
import pt.techzebra.winit.platform.LoadingPromotionInfo;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class TradingActivity extends SherlockActivity implements OnItemClickListener {
	private ActionBar action_bar_;
	StaggeredGridView gridView;
	StaggeredAdapter adapter;
	ArrayList<Promotion> promos = new ArrayList<Promotion>();
	
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.trading_house_activity);
		
		action_bar_ = getSupportActionBar();
		action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_trading));
		action_bar_.setTitle(R.string.trading);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		(new LoadingOtherUserPromotionsToTrade(this){

			@Override
			public void callMainWindow(ArrayList<Promotion> result) {
				promos.addAll(result);
				adapter.notifyDataSetChanged();
			}
			
		}).execute();
		gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridViewTrading);

		int margin = getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
		gridView.setItemMargin(margin); // set the GridView margin
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well
		gridView.setSelector(R.drawable.highlight_overlay);
		adapter = new StaggeredAdapter(this, R.id.staggered_adapter, promos);

		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(this);
	}
	
	
	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		Promotion p = adapter.getItem(position);
		new LoadingPromotionInfo(this, 2).execute(p.getPromotionID());
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
}
