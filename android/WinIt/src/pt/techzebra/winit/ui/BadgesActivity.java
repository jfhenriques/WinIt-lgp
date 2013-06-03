package pt.techzebra.winit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Badge;
import pt.techzebra.winit.client.FacebookPublisher;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
	String auth_token;
	private SharedPreferences preferences_ = null;

	//Popup
	private Dialog d;
	private TextView name;
	private TextView date;
	private TextView description;
	private String active_image;

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.badges_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.badges);
		action_bar_.setDisplayHomeAsUpEnabled(true);
		auth_token = WinIt.getAuthToken();
		context_ = BadgesActivity.this;
		mLoader = new ImageLoader(context_);
		list_ = (ListView) findViewById(R.id.listBadges);
		new LoadingBadgesInfo().execute();
		binder = new BinderDataBadges(this, R.id.badge_image, badges_);
		list_.setAdapter(binder);
		metrics = getResources().getDisplayMetrics();
		preferences_ = getSharedPreferences(Constants.USER_PREFERENCES,
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		list_.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				boolean fb_logged_in = preferences_.getBoolean(Constants.PREF_FB_LOGGED_IN, false);
				builder = new AlertDialog.Builder(BadgesActivity.this);
				LayoutInflater inflater = (LayoutInflater)context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layout = inflater.inflate(R.layout.badge_popup, null);
				builder.setView(layout);
				name = (TextView) layout.findViewById(R.id.badge_popup_name);
				date = (TextView) layout.findViewById(R.id.badge_popup_unlocked_text);
				description = (TextView) layout.findViewById(R.id.badge_description_text);
				name.setText(badges_.get(position).get("name"));
				date.setText(Utilities.convertUnixTimestamp(Long.parseLong(badges_.get(position).get("date"))));
				description.setText(badges_.get(position).get("desc"));
				mLoader.DisplayImage(binder.getBadgeImageUrl(position), (ScaleImageView) layout.findViewById(R.id.badge_image));
				active_image = binder.getBadgeImageUrl(position);
				Button button = (Button) layout.findViewById(R.id.badge_share_button);
				if(fb_logged_in){
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(FacebookPublisher.publishStory("WinIt", name.getText().toString(), description.getText().toString(), 
									"http://techzebra.pt/#tlantic", active_image , BadgesActivity.this)){
								d.dismiss();
							}
							
						}
					});
				}
				else{
					button.setEnabled(false);
					button.setBackgroundColor(Color.GRAY);
				}
				d = builder.create();
				d.show();
				d.getWindow().setLayout(metrics.widthPixels, metrics.heightPixels/2); //Controlling width and height.

			}
		});
	}


	private class LoadingBadgesInfo extends AsyncTask<Void, Void, ArrayList<Badge>>{

		ArrayList<Badge> badges = new ArrayList<Badge>();
		private ProgressDialog progress_dialog_ = new ProgressDialog(BadgesActivity.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress_dialog_.setIndeterminate(true);
			progress_dialog_.setMessage("Loading...");
			progress_dialog_.show();

		}
		@Override
		protected ArrayList<Badge> doInBackground(Void... params) {
			badges = NetworkUtilities.fetchMyBadges(auth_token);
			return badges;
		}

		@Override
		protected void onPostExecute(ArrayList<Badge> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress_dialog_.dismiss();

			if(result != null){
				if(result.size() != 0){
					for(int i=0; i < result.size(); i++){

						map_ = new HashMap<String, String>();
						map_.put("id", Integer.toString(result.get(i).getBadgeID()));
						map_.put("name", result.get(i).getName());
						map_.put("image", result.get(i).getImage());
						map_.put("desc", result.get(i).getDescription());
						map_.put("date", result.get(i).getDate());
						badges_.add(map_);

					}
				}
				else{
					builder = new AlertDialog.Builder(context_);
					builder.setMessage("You have no Badges!");
					builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}

				binder.notifyDataSetChanged();

			} else {
				if(!Utilities.hasInternetConnection(context_)){
					builder = new AlertDialog.Builder(context_);
					builder.setMessage("No Internet connection. Do you wish to open Settings?");
					builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							context_.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
						}
					});
					builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		}
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
				holder.name =  (TextView)vi.findViewById(R.id.badge_name); 
				holder.image = (ScaleImageView) vi.findViewById(R.id.badge_image); 

				vi.setTag(holder);
			}
			else{
				holder = (ViewHolder)vi.getTag();
			}
			holder.id = objects.get(position).get("id");
			holder.name.setText(objects.get(position).get("name"));
			mLoader.DisplayImage(objects.get(position).get("image"), holder.image);      
			return vi;
		}

		public String getBadgeImageUrl(int position){
			return objects.get(position).get("image");
		}

		public class ViewHolder{
			String id;
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
