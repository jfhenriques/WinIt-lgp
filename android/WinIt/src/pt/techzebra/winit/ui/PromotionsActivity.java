package pt.techzebra.winit.ui;


import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.platform.LoadingPromotionInfo;
import pt.techzebra.winit.staggeredgridview.StaggeredAdapter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class PromotionsActivity extends SherlockActivity implements OnItemClickListener{
	private ActionBar action_bar_;
	
	private StaggeredGridView staggered_grid_view_;
	private StaggeredAdapter adapter_;
	private ArrayList<Promotion> promotions_;

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.promotions_activity);
		
		action_bar_ = getSupportActionBar();
		action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_single_player));
		action_bar_.setTitle(R.string.promotions);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		promotions_ = (ArrayList<Promotion>) getIntent().getSerializableExtra("Promotions");
		staggered_grid_view_ = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
		staggered_grid_view_.setItemMargin(margin); // set the GridView margin
		staggered_grid_view_.setPadding(margin, 0, margin, 0); // have the margin on the sides as well
		staggered_grid_view_.setSelector(R.drawable.highlight_overlay);
		staggered_grid_view_.setOnItemClickListener(this);
		
		adapter_ = new StaggeredAdapter(this, R.id.staggered_adapter, promotions_);
		staggered_grid_view_.setAdapter(adapter_);
		adapter_.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		Promotion p = adapter_.getItem(position);
		new LoadingPromotionInfo(this, 1).execute(p.getPromotionID());
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
