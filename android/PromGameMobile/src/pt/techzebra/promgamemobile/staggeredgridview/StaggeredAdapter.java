package pt.techzebra.promgamemobile.staggeredgridview;
import java.util.ArrayList;

import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.Promotion;
import pt.techzebra.promgamemobile.staggeredgridview.ImageLoader;
import pt.techzebra.promgamemobile.staggeredgridview.ScaleImageView;
import pt.techzebra.promgamemobile.ui.PromotionActivity;

public class StaggeredAdapter extends ArrayAdapter<Promotion> {

	private ImageLoader mLoader;
	private ArrayList<Promotion> objects;
	private Context mContext;
	

	public StaggeredAdapter(Context context, int textViewResourceId,
			ArrayList<Promotion> objects) {
		super(context, textViewResourceId, objects);
		mLoader = new ImageLoader(context);
		this.objects = objects;
		
	
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(getContext());
			convertView = layoutInflator.inflate(R.layout.promotions_list_row,
					null);
			holder = new ViewHolder();
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.imageView1);
			holder.imageDescription = (TextView) convertView.findViewById(R.id.description);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		mLoader.DisplayImage(objects.get(position).getImageUrl(), holder.imageView);
		holder.imageDescription.setText(objects.get(position).getName_());
		holder.promotion_id = objects.get(position).getPromotion_id_();
		
		
		return convertView;
	}
	

	static class ViewHolder {
		ScaleImageView imageView;
		TextView imageDescription;
		int promotion_id;
	}
}
