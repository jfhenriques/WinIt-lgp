package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.ui.MyPromotionsActivity;
import pt.techzebra.winit.ui.MyPromotionsActivity.MyPromotionsPagerAdapter;
import pt.techzebra.winit.ui.MyPromotionsActivity.MyPromotionsStepFragment;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class LoadMyPromotionsTask extends ServerTask<Boolean, Void, ArrayList<ArrayList<Promotion>>>{
	private boolean refresh_view_;
    
    ArrayList<ArrayList<Promotion>> promotions_ = new ArrayList<ArrayList<Promotion>>();

	@Override
	protected ArrayList<ArrayList<Promotion>> doInBackground(Boolean... params) {
	    refresh_view_ = (params.length != 0) ? params[0] : false;

	    String auth_token = WinIt.getAuthToken();
	    
	    ArrayList<Promotion> temp = new ArrayList<Promotion>();
		temp = NetworkUtilities.fetchMyPromotions(auth_token);
		Log.d("won promos", temp.toString());
		promotions_.add(temp);
		temp = NetworkUtilities.fetchMyPromotionsTradeable(auth_token);
		promotions_.add(temp);
		temp = NetworkUtilities.fetchMyPromotionsInTrading(auth_token);
		promotions_.add(temp);
		return promotions_;
	}

	@Override
	protected void onPostExecute(ArrayList<ArrayList<Promotion>> result) {
		super.onPostExecute(result);

		if(result == null) {
			if (!Utilities.hasInternetConnection(context_)) {
			    Utilities.showInternetConnectionAlert(context_);
			}
		} else {
		    if (refresh_view_) {
		        MyPromotionsActivity my_promotions_activity = (MyPromotionsActivity) context_;
		        
		        my_promotions_activity.setPromotionsWon(promotions_.get(0));
		        my_promotions_activity.setPromotionsTradeable(promotions_.get(1));
		        my_promotions_activity.setPromotionsInTrading(promotions_.get(2));
		        
		        ViewPager view_pager = my_promotions_activity.getViewPager();
		        MyPromotionsPagerAdapter adapter = (MyPromotionsPagerAdapter) view_pager.getAdapter();
		        adapter.notifyDataSetChanged();
		        
	            MyPromotionsStepFragment fragment = (MyPromotionsStepFragment) adapter.getFragment(view_pager.getCurrentItem());
	            fragment.getAdapter().notifyDataSetChanged();		        
		    } else {
		        Intent intent = new Intent(context_, MyPromotionsActivity.class);
	            intent.putExtra("Promotions", result);
	            context_.startActivity(intent);
		    }
		}
	}
}
