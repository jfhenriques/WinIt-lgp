package pt.techzebra.winit.ui;

import java.util.ArrayList;

import pt.techzebra.winit.R;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Badge;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BadgesActivity extends SherlockActivity {
	private static final String TAG = "BadgesActivity";
	
	public static final String KEY_EXTRA_BADGES = "badges";

	private ActionBar action_bar_;
	
	private ArrayList<Badge> badges_;
	
	private ListView list_;
	private BadgesAdapter adapter_;

	@SuppressWarnings("unchecked")
    @Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.badges_activity);
		
		badges_ = (ArrayList<Badge>) getIntent().getSerializableExtra(KEY_EXTRA_BADGES);
		
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.badges);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		
		list_ = (ListView) findViewById(R.id.listBadges);
		adapter_ = new BadgesAdapter(this, badges_);
		list_.setAdapter(adapter_);
	}

	@Override
	protected void onResume() {
		super.onResume();
		list_.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
			    Intent intent = new Intent(BadgesActivity.this, BadgeActivity.class);
			    intent.putExtra(BadgeActivity.KEY_EXTRA_BADGE, badges_.get(position));
			    startActivity(intent);
			}
		});
	}

	private static class BadgesAdapter extends BaseAdapter {
		private ArrayList<Badge> badges_;
		private ImageLoader image_loader_;

		public BadgesAdapter(Context context, ArrayList<Badge> badges) {
			badges_ = badges;
			image_loader_ = new ImageLoader(context);
		}

		@Override
		public int getCount() {
		    return badges_.size();
		}
		
		@Override
		public Object getItem(int position) {
		    return badges_.get(position);
		}
		
		@Override
		public long getItemId(int position) {
		    return badges_.get(position).getBadgeID();
		}
		
		@Override
		public View getView(int position, View convert_view, ViewGroup parent) {
			ViewHolder holder;

			View view = convert_view;
			
			if (convert_view == null) {
				LayoutInflater inflater = LayoutInflater.from(WinIt.getAppContext());
				view = inflater.inflate(R.layout.badges_list_row, null);
				holder = new ViewHolder();
				holder.name =  (TextView) view.findViewById(R.id.badge_name); 
				holder.image = (ScaleImageView) view.findViewById(R.id.badge_image); 

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			Badge badge = badges_.get(position);
			holder.name.setText(badge.getName());
			image_loader_.DisplayImage(badge.getImage(), holder.image);      
			
			return view;
		}

		public class ViewHolder {
			TextView name;
			ScaleImageView image;
		} 
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
