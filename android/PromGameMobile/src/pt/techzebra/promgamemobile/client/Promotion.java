package pt.techzebra.promgamemobile.client;

import android.graphics.Bitmap;
import org.json.JSONObject;
import android.util.Log;

public class Promotion {
	
	private final int promotion_id_;
    private String name_;
    private final String description_;
	private String image_url_;
    
	private Bitmap image_bitmap_;
	
	public Bitmap get_image_bitmap() {
		return image_bitmap_;
	}

	public void set_image_bitmap(Bitmap promo_image) {
		this.image_bitmap_ = promo_image;
	}
	
	public Promotion(int pid, String name, String description){
		promotion_id_= pid;
    	name_ = name;
    	description_ = description;
	}
	
	public Promotion(String name, String image_url){
		//TODO change ID!!!
		promotion_id_ = 1;
		name_ = name;
		image_url_ = image_url;
		description_ = "";
	}

	public int getPromotion_id_() {
		return promotion_id_;
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


	public String getDescription_() {
		return description_;
	}
	
    /**
     * Creates and returns an instance of the promotion from the provided JSON data.
     * 
     * @param promotion The JSONObject containing promotion data
     * @return promotion The new instance of promotion created from the JSON data
     */
    public static Promotion valueOf(JSONObject promotion) {
        try {
            final int promotion_id = promotion.getInt("uid");
            final String name = promotion.getString("name");
            final String description = promotion.getString("description");
           
            return new Promotion(promotion_id, name, description);
        } catch (final Exception e) {
            Log.i("Promotion", "Error parsing JSON user object" + e.toString());
        }
        
        return null;
    }
}
