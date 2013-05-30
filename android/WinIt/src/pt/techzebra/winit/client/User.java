package pt.techzebra.winit.client;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final int user_id_;
    private final String name_;
    private String address_; // address/ street
    private int adid_;
    private int cp3_;
    private int cp4_;
    private String address2_; // porta/bloco/andar
    private String locality_;
    private String district_;
    private final int birthday_;
    private final String email_;
    private final String level_;
    private final String points_;

    public User(int user_id, String name, int adid, int cp4, int cp3, String address, String address2,
            String locality, String district, int birthday, String email,
            String level, String points) {
        adid_ = adid;
        cp4_ = cp4;
        cp3_ = cp3;
        user_id_ = user_id;
        email_ = email;
        name_ = name;
        address_ = address;
        address2_ = address2;
        locality_ = locality;
        district_ = district;
        birthday_ = birthday;
        level_ = level;
        points_ = points;
    }

    public int getUserId() {
        return user_id_;
    }

    public String getName() {
        return name_;
    }

    public int getAdid() {
        return adid_;
    }

    public void setAdid(int adid_) {
        this.adid_ = adid_;
    }

    public int getCp3() {
        return cp3_;
    }

    public void setCp3(int cp3_) {
        this.cp3_ = cp3_;
    }

    public int getCp4() {
        return cp4_;
    }

    public void setCp4(int cp4_) {
        this.cp4_ = cp4_;
    }

    public void setAddress(String address_) {
        this.address_ = address_;
    }

    public String getAddress2() {
        return address2_;
    }

    public void setAddress2(String address2_) {
        this.address2_ = address2_;
    }

    public String getLocality() {
        return locality_;
    }

    public void setLocality(String locality_) {
        this.locality_ = locality_;
    }

    public String getDistrict() {
        return district_;
    }

    public void setDistrict(String district_) {
        this.district_ = district_;
    }

    public String getAddress() {
        return address_;
    }

    public int getBirthday() {
        return birthday_;
    }

    public String getEmail() {
        return email_;
    }

    public String getLevel() {
        return level_;
    }

    public String getPoints() {
        return points_;
    }

    /**
     * Creates and returns an instance of the user from the provided JSON data.
     * 
     * @param user
     *            The JSONObject containing user data
     * @return user The new instance of user created from the JSON data
     */
    public static User valueOf(JSONObject user) {
        try {
            final int user_id = user.getInt("uid");
            final String name = user.getString("name");
            final String email = user.getString("email");
            final int adid = user.getInt("adid");
            final int cp4 = user.getInt("cp4");
            final int cp3 = user.getInt("cp3");
            final String address = user.getString("address"); // address/ street
            final String address2 = user.getString("address2"); // porta/bloco/andar
            final String locality = user.getString("locality");
            final String district = user.getString("district");
            final int birthday = user.getInt("birth");
            final String level = user.getString("level");
            final String points = user.getString("points");
           
            return new User(user_id, name, adid, cp4, cp3, address, address2, locality, district, birthday, email, level, points);
        } catch (final Exception e) {
            Log.i("User", "Error parsing JSON user object" + e.toString());
        }

        return null;
    }

}
