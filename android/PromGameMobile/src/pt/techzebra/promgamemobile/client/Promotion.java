package pt.techzebra.promgamemobile.client;

import org.json.JSONObject;

import android.util.Log;

public class Promotion {
	
	private final int promotion_id_;
    private final String name_;
    private final String description_;
    
	public Promotion(int pid, String name, String description){
		promotion_id_= pid;
    	name_ = name;
    	description_ = description;
	}

	public int getPromotion_id_() {
		return promotion_id_;
	}

	public String getName_() {
		return name_;
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
