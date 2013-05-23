package pt.techzebra.winit.client;

import org.json.JSONObject;

import android.util.Log;

public class Badge{

	private int badge_id;
	private String name;
	private String image;
	private String description;
	private String date;

	public Badge(int bid, String name, String image, String desc, String date){
		this.badge_id = bid;
		this.name = name;
		this.image = image;
		this.description = desc;
		this.date = date;
	}

	public int getBadgeID() {
		return badge_id;
	}

	public void setBadgeID(int badge_id) {
		this.badge_id = badge_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
