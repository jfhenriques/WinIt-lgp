package pt.techzebra.winit.client;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

public class Badge implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int badge_id_;
	private String name_;
	private String image_;
	private String description_;
	private String date_;

	public Badge(int bid, String name, String image, String desc, String date) {
		badge_id_ = bid;
		name_ = name;
		image_ = image;
		description_ = desc;
		date_ = date;
	}

	public int getBadgeID() {
		return badge_id_;
	}

	public void setBadgeID(int badge_id) {
		badge_id_ = badge_id;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		name_ = name;
	}

	public String getImage() {
		return image_;
	}

	public void setImage(String image) {
		image_ = image;
	}

	public String getDescription() {
		return description_;
	}

	public void setDescription(String description) {
		description_ = description;
	}
	
	public String getDate() {
		return date_;
	}

	public void setDate(String date) {
		date_ = date;
	}

	public static Badge valueOf(JSONObject badge) {
		try{
			final int badge_id = badge.optInt("bid");
			final String name = badge.optString("name");
			final String description = badge.optString("description");
			final String image_url = badge.getString("image");
			final String date = badge.getString("aquis_date");
			
			return new Badge(badge_id,name,image_url,description,date);
		}
		catch (final Exception e) {
            Log.i("Badge", "Error parsing JSON badge object" + e.toString());
        }
		return null;
		
	}
}
