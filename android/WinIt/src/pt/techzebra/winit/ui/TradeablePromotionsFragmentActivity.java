package pt.techzebra.winit.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import pt.techzebra.winit.ui.ProposePromotionDialogFragment.ProposePromotionListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class TradeablePromotionsFragmentActivity extends SherlockFragmentActivity implements ProposePromotionListener {
	private ActionBar action_bar_;

	List<HashMap<String,String>> promotions_ = new ArrayList<HashMap<String,String>>();
	HashMap<String, String> map_ = new HashMap<String, String>();
	private BinderData binding_data_;
	private Promotion promotion_wanted;
	private ImageView promotion_to_trade_image_;
	private Context context_= null;
	View layout_ = null;
	ListView list_ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trading_promotions_to_trade);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.trading);
		action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_trading));
		action_bar_.setDisplayHomeAsUpEnabled(true);

		context_ = TradeablePromotionsFragmentActivity.this;
		promotion_wanted = (Promotion) getIntent().getSerializableExtra("Promotion");
		list_ = (ListView) findViewById(R.id.list);
		new LoadingMyPromotionsInTrading(this).execute();
		binding_data_ = new BinderData(this, R.id.list_image, promotions_);
		list_.setAdapter(binding_data_);
	}

	@Override
	protected void onResume() {
		super.onResume();
		list_.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SherlockDialogFragment dialog = new ProposePromotionDialogFragment();
				dialog.show(getSupportFragmentManager(), "ProposePromotionDialogFragment");
			}
		});
	}

	public void sendTradeProposal(View view){

	}

	private class LoadingMyPromotionsInTrading extends AsyncTask<Void, Void, ArrayList<Promotion>> {
		private ProgressDialog progress_dialog_ = new ProgressDialog(TradeablePromotionsFragmentActivity.this);

		String auth_token;
		ArrayList<Promotion> promos = new ArrayList<Promotion>();
		private Context mContext = null;
		AlertDialog.Builder builder;
		public LoadingMyPromotionsInTrading(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		protected ArrayList<Promotion> doInBackground(Void... params) {
			try {
				SharedPreferences preferences_ = WinIt.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
				auth_token = preferences_.getString(Constants.PREF_AUTH_TOKEN, "");
				promos = NetworkUtilities.fetchMyPromotionsTradeable(auth_token);
			} catch (Exception e) {
				e.printStackTrace();
			}	

			if (promos != null) {
				return promos;
			}

			return null;
		}

		protected void onPreExecute(){
			super.onPreExecute();
			progress_dialog_.setIndeterminate(true);
			progress_dialog_.setMessage("Loading...");
			progress_dialog_.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Promotion> result){
			super.onPostExecute(result);
			progress_dialog_.dismiss();

			if(result != null){
				if(result.size() != 0){
					for(int i=0; i < result.size(); i++){
						if(result.get(i).getPromotionID() != promotion_wanted.getPromotionID()){
							map_ = new HashMap<String, String>();
							map_.put("id", Integer.toString(result.get(i).getPromotionID()));
							map_.put("name", result.get(i).getName());
							map_.put("image", result.get(i).getImageUrl());
							promotions_.add(map_);
						}
					}
					binding_data_.notifyDataSetChanged();
				}
				else{
					builder = new AlertDialog.Builder(mContext);
					builder.setMessage("You have no promotions to trade!");
					builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			} else {
				if(!Utilities.hasInternetConnection(mContext)){
					builder = new AlertDialog.Builder(mContext);
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


	private class BinderData extends SimpleAdapter {
		List<HashMap<String,String>> objects;
		private ImageLoader mLoader;

		public BinderData(Context context, int textViewResourceId, List<HashMap<String,String>> objects) {
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
				vi = inflater.inflate(R.layout.trading_list_row, null);
				holder = new ViewHolder();

				holder.name =  (TextView)vi.findViewById(R.id.list_item_name); 
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
			ScaleImageView image;
		} 
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {

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
