package pt.techzebra.winit.staggeredgridview;
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

import pt.techzebra.winit.R;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.staggeredgridview.ImageLoader;
import pt.techzebra.winit.staggeredgridview.ScaleImageView;
import pt.techzebra.winit.ui.PromotionActivity;

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
			holder.imageView = (ScaleImageView) convertView .findViewById(R.id.staggered_adapter);
			holder.imageDescription = (TextView) convertView.findViewById(R.id.description);
			convertView.setTag(holder);
		}

		holder = (ViewHolder) convertView.getTag();

		mLoader.DisplayImage(objects.get(position).getImageUrl(), holder.imageView);
		holder.imageDescription.setText(objects.get(position).getName());
		holder.promotion_id = objects.get(position).getPromotionID();
		
		
		return convertView;
	}
	

	public static class ViewHolder {
		public ScaleImageView imageView;
		public TextView imageDescription;
		public int promotion_id;
	}
}
