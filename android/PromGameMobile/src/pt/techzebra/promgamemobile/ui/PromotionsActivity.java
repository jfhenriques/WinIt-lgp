package pt.techzebra.promgamemobile.ui;

import java.util.ArrayList;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.platform.DownloadImageTask;
import pt.techzebra.promgamemobile.platform.PromotionsListAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class PromotionsActivity extends SherlockActivity {

	public volatile ArrayList<Pair<Promotion, Promotion>> promos = new ArrayList<Pair<Promotion, Promotion>>();
	public volatile PromotionsListAdapter adapter_;
	DownloadImageTask dit;
	public Context mContext;
	private ActionBar action_bar_;

	private void populateList(){
		Pair<String, String> p,q,r,z; 
		p = new Pair<String,String>("Promo 1", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSGXrUM4HEzocRE90gmC2k2IFe9kxgz1Xt02wgZAqw4znVaGm6S_w");
		q = new Pair<String,String>("Promo 2", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSGXrUM4HEzocRE90gmC2k2IFe9kxgz1Xt02wgZAqw4znVaGm6S_w");
		r = new Pair<String,String>("Promo 3", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSGXrUM4HEzocRE90gmC2k2IFe9kxgz1Xt02wgZAqw4znVaGm6S_w");
		z = new Pair<String,String>("Promo 4", "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcSGXrUM4HEzocRE90gmC2k2IFe9kxgz1Xt02wgZAqw4znVaGm6S_w");
		try {
			dit = new DownloadImageTask(this);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				dit.executeOnExecutor(DownloadImageTask.THREAD_POOL_EXECUTOR, p,q,r,z);
			else
				dit.execute(p,q,r,z);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.promotions);
		setContentView(R.layout.promotions_activity);
		ListView lv = (ListView) findViewById(R.id.listView);
		if(promos.isEmpty())
			populateList();
		adapter_ = new PromotionsListAdapter(this, promos);			
		lv.setAdapter(adapter_);			


	}

	@Override
	protected void onPause() {
		super.onPause();
		dit.cancel(true);
	}
	public void handlePromSelected(View view){
		/*//Bundle b = new Bundle();
		//Pair<Integer, Integer> pids = (Pair<Integer, Integer>) view.getTag();
		//int pid;
		Intent i;
		/*if(view.getId() == R.id.promo_image1){
			pid = pids.first;
		}
		else{
			pid = pids.second;
		}
		i = new Intent(this, ShowPromotionActivity.class);
		b.putInt("id", pid); //Your id
		i.putExtras(b); //Put your id to your next Intent
		i = new Intent(this, ShowPromotionActivity.class);
		startActivity(i);
		finish();*/

	}
}
