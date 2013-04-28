package pt.techzebra.promgamemobile.platform;

import java.util.ArrayList;
import java.util.HashMap;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.Promotion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotionsListAdapter extends BaseAdapter{

	public ArrayList<Pair<Promotion, Promotion>> list;
	Activity activity;
	
	ImageView promo_image1_;
	TextView promo_name1_;
	ImageView promo_image2_;
	TextView promo_name2_;
	

	public PromotionsListAdapter(Activity activity, ArrayList<Pair<Promotion, Promotion>> list){
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	
	//TODO tratar casos em que só aparece um item numa linha inteira
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater =  activity.getLayoutInflater();
		arg1 = inflater.inflate(R.layout.promotions_list_row, null);
		promo_image1_ = (ImageView) arg1.findViewById(R.id.promo_image1);
		promo_name1_ = (TextView) arg1.findViewById(R.id.description1);
		promo_image2_ = (ImageView) arg1.findViewById(R.id.promo_image2);
		promo_name2_ = (TextView) arg1.findViewById(R.id.description2);
		Promotion tmp1, tmp2;
		tmp1 = list.get(arg0).first;
		tmp2 = list.get(arg0).second;
		Pair<Integer, Integer> ids = new Pair<Integer, Integer>(tmp1.getId_(), tmp2.getId_());
		arg1.setTag(ids);
		promo_image1_.setImageBitmap(tmp1.get_image_bitmap());
		promo_name1_.setText(tmp1.getName_());
		
		promo_image2_.setImageBitmap(tmp2.get_image_bitmap());
		promo_name2_.setText(tmp2.getName_());
		
		return arg1;
	}

}
