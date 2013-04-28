package pt.techzebra.promgamemobile.client;

import android.graphics.Bitmap;

public class Promotion {
	
	
	
	private String name_;
	private String image_url_;
	private String image_width_;
	private String image_height_;
	private int id_;
	public int getId_() {
		return id_;
	}



	private Bitmap image_bitmap_;
	
	public Bitmap get_image_bitmap() {
		return image_bitmap_;
	}

	public void set_image_bitmap(Bitmap promo_image) {
		this.image_bitmap_ = promo_image;
	}

	public String getName_() {
		return name_;
	}

	public void setName_(String name_) {
		this.name_ = name_;
	}

	public String getImage_url_() {
		return image_url_;
	}

	public void setImage_url_(String image_url_) {
		this.image_url_ = image_url_;
	}

	
	
	public Promotion(String name, String image_url){
		//TODO change ID!!!
		id_ = 1;
		name_ = name;
		image_url_ = image_url;
	}
	
	
}
