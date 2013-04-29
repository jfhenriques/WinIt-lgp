package pt.techzebra.promgamemobile.client;

import android.graphics.Bitmap;
import org.json.JSONObject;
import android.util.Log;

public class Promotion {
	

	private int promotion_id_;
	private int active_; //0 - not active | 1 - active
    private String name_;
    //TODO check date types
    private int init_date_;
    private int end_date_;
    private int grand_limit_;
    private int user_limit_;
   // private String valid_coord_;
    //private int valid_coord_radius_;
    private int win_points_;
    private int func_type_;
    private int retailer_id_;
    private int promotion_type_id_;
    private int transferable_;
    private String description_;
	private String image_url_;
    
	public Promotion(int pid, String name, String image_url){
		promotion_id_ = pid;
		name_ = name;
		image_url_ = image_url;
	}
	
	public Promotion(int pid, String name, String description, int active, int init_date, int end_date, int grand_limit, int user_limit, int transferable, int win_points, int func_type, int retailer_id, int promotion_type_id){
		promotion_id_= pid;
    	name_ = name;
    	description_ = description;
    	active_ = active;
    	init_date_ = init_date;
    	end_date_ = end_date;
    	grand_limit_ = grand_limit;
    	user_limit_ = user_limit;
    	win_points_ = win_points;
    	func_type_ = func_type;
    	retailer_id_ = retailer_id;
    	promotion_type_id_ = promotion_type_id;
    	transferable_ = transferable;
    	
	}
	
	public int getPromotion_id_() {
		return promotion_id_;
	}

	public String getName_() {
		return name_;
	}
	
	public String getImage_url_() {
		return image_url_;
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
           
            //return new Promotion();
        } catch (final Exception e) {
            Log.i("Promotion", "Error parsing JSON user object" + e.toString());
        }
        
        return null;
    }
}
