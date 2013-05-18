package pt.techzebra.promgamemobile.ui;


import java.util.ArrayList;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.platform.LoadingPromotionInfo;
import pt.techzebra.promgamemobile.staggeredgridview.StaggeredAdapter;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class PromotionsActivity extends SherlockActivity implements OnItemClickListener{

	private ActionBar action_bar_;
	StaggeredGridView gridView;
	StaggeredAdapter adapter;
	ArrayList<Promotion> promos;
	

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotions);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.promotions_activity);
		
		promos = (ArrayList<Promotion>) getIntent().getSerializableExtra("Promotions");
		gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
		gridView.setItemMargin(margin); // set the GridView margin
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		adapter = new StaggeredAdapter(this, R.id.staggered_adapter, promos);

		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(this);
	}
	
	/*private ArrayList<Promotion> populateDataToShow() {
		
		
		
		Promotion p,q,r,z;
		
		p = new Promotion(1, "Sofia Vergara", "http://www.aceshowbiz.com/images/wennpic/sofia-vergara-2012-vanity-fair-oscar-party-02.jpg");
		q = new Promotion(1, "Mila Kunis", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQlICHZwFxuRE8YTg9wt2Jm3GVBcr7BQqAw7mzgWNchvnqqTCjU-w");
		r = new Promotion(1, "Katty Perry", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlHwDYKjKq8WNrVjnZ6C5uhgEgFVkIPwjJ2cLWXBGxjdJRwoZ-");
		z = new Promotion(1, "Rihanna", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMftXdrDhn_u5hIa6emJK0vtsazF-9rlcsCV_Mis-rM567qMJQEA");
		
		p = new Promotion(1, "Tania", "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-ash4/308038_3917650178087_1943293896_n.jpg");
		q = new Promotion(1, "Tania", "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-prn1/34768_1386719066391_3881235_n.jpg");
		r = new Promotion(1, "Tania", "https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-snc6/262519_1997202488095_6948481_n.jpg");
		z = new Promotion(1, "Tania", "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-ash4/207174_1732142541762_1225552_n.jpg");
		

		promos.add(p);
		promos.add(q);
		promos.add(r);
		promos.add(z);

		return promos;
	}*/

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		
		Promotion p = adapter.getItem(position);
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
