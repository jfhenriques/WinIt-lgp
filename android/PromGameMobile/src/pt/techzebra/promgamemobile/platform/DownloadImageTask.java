package pt.techzebra.promgamemobile.platform;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.actionbarsherlock.R.style;

import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.ui.PromotionsActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

public class DownloadImageTask extends AsyncTask<Pair<String, String>, Void, ArrayList<Promotion>> {
	ArrayList<Promotion> promos = new ArrayList<Promotion>();
	private final WeakReference<PromotionsActivity> ref;
	private ProgressDialog progressDialog;
	Rect r = new Rect(100, 100, 100, 100);
	public DownloadImageTask(PromotionsActivity activity){
		ref = new WeakReference<PromotionsActivity>(activity);
	}
	
	BitmapFactory.Options options = new BitmapFactory.Options();
	

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = new ProgressDialog(ref.get());
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(style.Sherlock___Widget_Holo_Spinner);
		progressDialog.setMessage("Loading promotions...");
		progressDialog.show();
	}

	protected ArrayList<Promotion> doInBackground(Pair<String, String>... urls) {

		if(isCancelled()){
			return null;
		}
		     
		for(int i=0; i< urls.length; i++){
			Promotion tmp = new Promotion("", "");
			Pair<String, String> promo_display = urls[i];
			Bitmap mIcon11 = null;
			try {
				//TODO mudar o inSampleSize de acordo com o tamanho que a imagem tiver
				options.inSampleSize = 4;
				InputStream in = new java.net.URL(promo_display.second).openStream();
				mIcon11 = BitmapFactory.decodeStream(in, r, options);
				
				tmp.setName_(promo_display.first);
				tmp.setImage_url_(promo_display.second);
				tmp.set_image_bitmap(mIcon11);
				promos.add(tmp);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
		}

		return promos;
	}



	@Override
	protected void onPostExecute(ArrayList<Promotion> result){
		super.onPostExecute(result);
		PromotionsActivity act = ref.get();

		for(int i=0; i<result.size()-1; i+=2){
				act.promos.add(new Pair<Promotion, Promotion>(result.get(i), result.get(i+1)));
		}
		act.adapter_.notifyDataSetChanged();
		progressDialog.dismiss();
		ref.clear();
		act.isFinishing();
	}
	
	
	

}
