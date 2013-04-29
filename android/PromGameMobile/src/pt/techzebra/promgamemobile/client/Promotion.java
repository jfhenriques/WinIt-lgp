package pt.techzebra.promgamemobile.client;

import java.io.Serializable;

import org.json.JSONObject;
import android.util.Log;

public class Promotion implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int promotion_id_;
	private String name_;
	private String description_;
	private String image_url_;
	private int active_; //0 - not active | 1 - active
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
    private boolean transferable_;

    
	public Promotion(int pid, String name, String image_url){
		promotion_id_ = pid;
		name_ = name;
		image_url_ = image_url;
	}
	
	public Promotion(int pid, String name, String description, String image_url, int init_date, int end_date, int user_limit, boolean transferable, int win_points, int retailer_id, int promotion_type_id){
		promotion_id_= pid;
    	name_ = name;
    	description_ = description;
    	image_url_ = image_url;
    	//active_= active;
    	init_date_ = init_date;
    	end_date_ = end_date;
    	user_limit_ = user_limit;
    	win_points_ = win_points;
    	retailer_id_ = retailer_id;
    	promotion_type_id_ = promotion_type_id;
    	transferable_ = transferable;
	}
	

	public int getPromotionID() {
		return promotion_id_;
	}

	public String getName() {
		return name_;
	}

	public int getActive() {
		return active_;
	}

	public void setActive(int active_) {
		this.active_ = active_;
	}

	public int getGrandLimit() {
		return grand_limit_;
	}

	public void setGrandLimit(int grand_limit_) {
		this.grand_limit_ = grand_limit_;
	}

	public int getUserLimit() {
		return user_limit_;
	}

	public void setUserLimit(int user_limit_) {
		this.user_limit_ = user_limit_;
	}

	public int getWinPoints() {
		return win_points_;
	}

	public void setWinPoints(int win_points_) {
		this.win_points_ = win_points_;
	}

	public boolean getTransferable() {
		return transferable_;
	}

	public void setTransferable(boolean transferable_) {
		this.transferable_ = transferable_;
	}


	public String getDescription() {
		return description_;
	}

	public String getImageUrl() {
		return image_url_;
	}
	
	public void setImageUrl(String url) {
		this.image_url_ = url;
	}

	public int getInitDate() {
		return init_date_;
	}

	public int getEndDate() {
		return end_date_;
	}

	public int getFuncType() {
		return func_type_;
	}

	public int getRetailerId() {
		return retailer_id_;
	}

	public int getPromotionTypeId() {
		return promotion_type_id_;
	}


	/**
     * Creates and returns an instance of the promotion from the provided JSON data.
     * 
     * @param promotion The JSONObject containing promotion data
     * @return promotion The new instance of promotion created from the JSON data
     */
    public static Promotion valueOf(JSONObject promotion) {
        try {
            final int promotion_id = promotion.optInt("pid");
            final String name = promotion.optString("name");
            final String description = promotion.optString("desc");

            final String image_url = promotion.isNull("image") ? null : promotion.getString("image");
            
            //final int active = promotion.optInt("active");
            final int init_date = promotion.optInt("init_date");
            final int end_date = promotion.optInt("end_date");
            //final int grand_limit = promotion.optInt("grand_limit");
            final int user_limit = promotion.optInt("user_limit");
            final int win_points = promotion.optInt("win_points");
            //final int func_type = promotion.optInt("func_type");
            final int retailer_id = promotion.optInt("rid");
            final int promotion_type_id = promotion.optInt("ptid");
            final boolean transferable = promotion.optBoolean("transferable");
                       
            return new Promotion(promotion_id, name, description, image_url, init_date, end_date, user_limit, transferable, win_points, retailer_id, promotion_type_id);

        } catch (final Exception e) {
            Log.i("Promotion", "Error parsing JSON user object" + e.toString());
        }
        
        return null;
    }
}
