package pt.techzebra.promgamemobile.ui;

import java.util.ArrayList;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.platform.LoadingOtherUserPromotionsToTrade;
import pt.techzebra.promgamemobile.platform.LoadingPromotionInfo;
import pt.techzebra.promgamemobile.staggeredgridview.StaggeredAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class TradingHouseActivity extends SherlockActivity implements OnItemClickListener {
	private ActionBar action_bar_;
	StaggeredGridView gridView;
	StaggeredAdapter adapter;
	ArrayList<Promotion> promos = new ArrayList<Promotion>();
	
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.trading);
		setContentView(R.layout.trading_house_activity);
		
		
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
		adapter = new StaggeredAdapter(this, R.id.imageView1, promos);

		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(this);
		

	}
	
	
	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		
		Promotion p = adapter.getItem(position);
		new LoadingPromotionInfo(this, 2).execute(p.getPromotionID());
		
	}
}