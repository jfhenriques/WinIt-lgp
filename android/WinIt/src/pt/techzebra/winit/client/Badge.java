package pt.techzebra.winit.client;

public class Badge{
	
	private int badge_id;
	private String name;
	private String image;
	private String description;
	
	public Badge(int bid, String name, String image, String desc){
		this.badge_id = bid;
		this.name = name;
		this.image = image;
		this.description = desc;
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
	
	
}
