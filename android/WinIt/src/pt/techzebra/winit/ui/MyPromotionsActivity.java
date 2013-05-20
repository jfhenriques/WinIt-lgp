package pt.techzebra.winit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.PromGame;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.R.style;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class MyPromotionsActivity extends SherlockActivity {

	private ActionBar action_bar_;
	List<HashMap<String,String>> promotions = new ArrayList<HashMap<String,String>>();
	HashMap<String, String> map = new HashMap<String, String>();
	private BinderData bindingData;
	//private Promotion promotion_wanted;
	private Context mContext = null;
	View layout = null;
	ListView list = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.my_promotions);
		setContentView(R.layout.my_promotions_activity);
		mContext = MyPromotionsActivity.this;
		list = (ListView) findViewById(R.id.list);
		new LoadingMyPromotions(this).execute();
		bindingData = new BinderData(this, R.id.list_image, promotions);
		list.setAdapter(bindingData);

	}
	
	private class LoadingMyPromotions extends AsyncTask<Void, Void, ArrayList<Promotion>>{

		String auth_token;
		private ProgressDialog progressDialog;
		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		private Context mContext = null;

		public LoadingMyPromotions(Context mContext)
		{
			this.mContext = mContext;
		}

		@Override
		protected ArrayList<Promotion> doInBackground(Void... params) {
			try {
				SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
				auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
				promos = NetworkUtilities.fetchMyPromotions(auth_token);
			} catch (Exception e) {
				e.printStackTrace();
			}		
			if(promos != null)
				return promos;
			return null;
		}

		protected void onPreExecute(){
			super.onPreExecute();
			progressDialog = new ProgressDialog(mContext);
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
			progressDialog.setMessage("Loading promotions...");
			progressDialog.show();
		}

		
		@Override
		protected void onPostExecute(ArrayList<Promotion> result){
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result != null){
				for(int i=0; i < result.size(); i++){ // TODO
					//if(result.get(i).getPromotionID() != promotion_wanted.getPromotionID()){
						map = new HashMap<String, String>();
						map.put("id", Integer.toString(result.get(i).getPromotionID()));
						map.put("name", result.get(i).getName());
						map.put("end_date", Integer.toString(result.get(i).getEndDate()));
						map.put("win_points", Integer.toString(result.get(i).getWinPoints()));
						map.put("image", result.get(i).getImageUrl());
						promotions.add(map);
					//}
				}

				bindingData.notifyDataSetChanged();
			} else {
				if(!Utilities.hasInternetConnection(mContext)){
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage("No Internet connection. Do you wish to open Settings?");
					builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
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


	private class BinderData extends SimpleAdapter{

		List<HashMap<String,String>> objects;
		private ImageLoader mLoader;

		public BinderData(Context context, int textViewResourceId, List<HashMap<String,String>> objects) {
			super(context, objects, textViewResourceId, null, null);
			// TODO Auto-generated constructor stub
			this.objects = objects;
			mLoader = new ImageLoader(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			View vi=convertView;
			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(getBaseContext());;
				vi = inflater.inflate(R.layout.my_promotions_list_row, null);
				holder = new ViewHolder();

				holder.name =  (TextView)vi.findViewById(R.id.list_item_name); 
				holder.end_date =  (TextView)vi.findViewById(R.id.list_item_end_date); 
				holder.win_points =  (TextView)vi.findViewById(R.id.list_item_win_points); 
				holder.image =(ScaleImageView)vi.findViewById(R.id.list_image); 

				vi.setTag(holder);
			}
			else{
				holder = (ViewHolder)vi.getTag();
			}

			// Setting all values in listview

			holder.id = objects.get(position).get("id");
			holder.name.setText(objects.get(position).get("name"));
			mLoader.DisplayImage(objects.get(position).get("image"), holder.image);      

			return vi;
		}
		
		public int getPromotionID(int position){
			return Integer.parseInt(objects.get(position).get("id"));
		}
		
		public String getPromotionImageUrl(int position){
			return objects.get(position).get("image");
		}


		public class ViewHolder{
			String id;
			TextView name;
			TextView end_date;
			TextView win_points;
			ScaleImageView image;
		} 
	}
}
 