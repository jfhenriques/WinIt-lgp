package pt.techzebra.winit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.techzebra.winit.R;
import pt.techzebra.winit.platform.DownloadImageTask;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BadgesActivity extends SherlockActivity {
	private static final String TAG = "BadgesActivity";

	private ActionBar action_bar_;
	List<HashMap<String,String>> badges_ = new ArrayList<HashMap<String,String>>();
	HashMap<String, String> map_ = new HashMap<String, String>();
	private Context context_= null;
	BinderDataBadges binder = null;
	View layout_ = null;
	ListView list_ = null;
	View layout = null;
	PopupWindow windows = null;
	DisplayMetrics metrics;
	AlertDialog.Builder builder;
	private ImageLoader mLoader;

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.badges_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.badges);
		action_bar_.setDisplayHomeAsUpEnabled(true);

		context_ = BadgesActivity.this;
		mLoader = new ImageLoader(context_);
		list_ = (ListView) findViewById(R.id.listBadges);
		populateData();
		binder = new BinderDataBadges(this, R.id.badge_image, badges_);
		list_.setAdapter(binder);
		metrics = getResources().getDisplayMetrics();
	}

	@Override
	protected void onResume() {
		super.onResume();
		list_.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				builder = new AlertDialog.Builder(BadgesActivity.this);
				// Get the layout inflater
				LayoutInflater inflater = (LayoutInflater)context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = inflater.inflate(R.layout.badge_popup, null);

				// Inflate and set the layout for the dialog
				// Pass null as the parent view because its going in the dialog layout
				builder.setView(layout);
				// Add action buttons
				
				TextView name = (TextView) layout.findViewById(R.id.badge_popup_name);
				TextView date = (TextView) layout.findViewById(R.id.badge_popup_unlocked_text);
				TextView description = (TextView) layout.findViewById(R.id.badge_description_text);
				name.setText(badges_.get(position).get("name"));
				date.setText(badges_.get(position).get("date"));
				description.setText(badges_.get(position).get("description"));
				mLoader.DisplayImage(binder.getBadgeImageUrl(position), (ScaleImageView) layout.findViewById(R.id.badge_image));
				
				
				Dialog d = builder.create();
				
				d.show();
				d.getWindow().setLayout(metrics.widthPixels, metrics.heightPixels/2); //Controlling width and height.

			}
		});
	}

	private void populateData() {
		map_ = new HashMap<String, String>();
		map_.put("id", "1");
		map_.put("name", "Badge 1");
		map_.put("date", "15-10-2012");
		map_.put("description", "Super awsome Badge 1");
		map_.put("image", "http://aboutfoursquare.com/wp-content/uploads/2010/07/comiccon2010_big.png");
		badges_.add(map_);

		map_ = new HashMap<String, String>();
		map_.put("id", "2");
		map_.put("name", "Badge 2");
		map_.put("date", "2-11-2012");
		map_.put("description", "Super awsome Badge 2");
		map_.put("image", "http://foursquareguru.com/media/badges/jetsetter_big.png");
		badges_.add(map_);

		map_ = new HashMap<String, String>();
		map_.put("id", "3");
		map_.put("name", "Badge 3");
		map_.put("date", "27-11-2012");
		map_.put("description", "Super awsome Badge 3");
		map_.put("image", "http://aboutfoursquare.com/wp-content/uploads/2010/05/playground_big.png");
		badges_.add(map_);

		map_ = new HashMap<String, String>();
		map_.put("id", "4");
		map_.put("name", "Badge 4");
		map_.put("date", "11-12-2012");
		map_.put("description", "Super awsome Badge 4");
		map_.put("image", "http://aboutfoursquare.com/wp-content/uploads/2010/06/worldcup2010_big.png");
		badges_.add(map_);


		map_ = new HashMap<String, String>();
		map_.put("id", "5");
		map_.put("name", "Badge 5");
		map_.put("date", "1-1-2013");
		map_.put("description", "Super awsome Badge 5");
		map_.put("image", "http://foursquareguru.com/media/badges/jetsetter_big.png");
		badges_.add(map_);

		map_ = new HashMap<String, String>();
		map_.put("id", "6");
		map_.put("name", "Badge 6");
		map_.put("date", "12-3-2013");
		map_.put("description", "Super awsome Badge 6");
		map_.put("image", "http://aboutfoursquare.com/wp-content/uploads/2010/05/playground_big.png");
		badges_.add(map_);

		map_ = new HashMap<String, String>();
		map_.put("id", "7");
		map_.put("name", "Badge 7");
		map_.put("date", "11-2-2013");
		map_.put("description", "Super awsome Badge 7");
		map_.put("image", "http://aboutfoursquare.com/wp-content/uploads/2010/06/worldcup2010_big.png");
		badges_.add(map_);

	}

	private class BinderDataBadges extends SimpleAdapter {
		List<HashMap<String,String>> objects;
		private ImageLoader mLoader;
		
		public BinderDataBadges(Context context, int textViewResourceId, List<HashMap<String,String>> objects) {
			super(context, objects, textViewResourceId, null, null);
			this.objects = objects;
			mLoader = new ImageLoader(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			View vi=convertView;
			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(getBaseContext());;
				vi = inflater.inflate(R.layout.badges_list_row, null);
				holder = new ViewHolder();
				holder.description =  (TextView)vi.findViewById(R.id.badge_description); 
				holder.image = (ScaleImageView) vi.findViewById(R.id.badge_image); 

				vi.setTag(holder);
			}
			else{
				holder = (ViewHolder)vi.getTag();
			}

			// Setting all values in listview

			holder.id = objects.get(position).get("id");
			holder.description.setText(objects.get(position).get("description"));
			mLoader.DisplayImage(objects.get(position).get("image"), holder.image);      

			return vi;
		}

		public String getBadgeImageUrl(int position){
			return objects.get(position).get("image");
		}

		public class ViewHolder{
			String id;
			TextView description;
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
