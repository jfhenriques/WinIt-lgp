package pt.techzebra.promgamemobile.ui;


import java.util.ArrayList;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.staggeredgridview.StaggeredAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

public class PromotionsActivity extends SherlockActivity implements OnItemClickListener{

	private ActionBar action_bar_;
	StaggeredGridView gridView;
	StaggeredAdapter adapter;
	

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotions);
		setContentView(R.layout.promotions_activity);
		gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.promotions_list_margin);
		gridView.setItemMargin(margin); // set the GridView margin
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		adapter = new StaggeredAdapter(this, R.id.imageView1, populateDataToShow());

		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		gridView.setOnItemClickListener(this);
		

	}
	
	private ArrayList<Promotion> populateDataToShow() {
		ArrayList<Promotion> promos = new ArrayList<Promotion>();

		Promotion p,q,r,z;
		p = new Promotion(1, "Sofia Vergara", "http://www.aceshowbiz.com/images/wennpic/sofia-vergara-2012-vanity-fair-oscar-party-02.jpg");
		q = new Promotion(1, "Mila Kunis", "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQlICHZwFxuRE8YTg9wt2Jm3GVBcr7BQqAw7mzgWNchvnqqTCjU-w");
		r = new Promotion(1, "Katty Perry", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlHwDYKjKq8WNrVjnZ6C5uhgEgFVkIPwjJ2cLWXBGxjdJRwoZ-");
		z = new Promotion(1, "Rihanna", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMftXdrDhn_u5hIa6emJK0vtsazF-9rlcsCV_Mis-rM567qMJQEA");

		promos.add(p);
		promos.add(q);
		promos.add(r);
		promos.add(z);

		return promos;
	}

	@Override
	public void onItemClick(StaggeredGridView parent, View view, int position,
			long id) {
		Promotion p = adapter.getItem(position);
		Intent i = new Intent(this, PromotionActivity.class);
		Bundle b = new Bundle();
		b.putInt("pid", p.getPromotion_id_());
		i.putExtras(b);
		startActivity(i);
		
	}
}
