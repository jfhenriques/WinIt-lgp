package pt.techzebra.promgamemobile.client;

import org.json.JSONObject;

import android.util.Log;

public class User {
    private final int user_id_;
    private final String name_;
    private final String address_;
    private final String birthday_;
    private final String email_;
    
    public User(int user_id, String name, String address, String birthday,
            String email) {
        user_id_ = user_id;
        name_ = name;
        address_ = address;
        birthday_ = birthday;
        email_ = email;
    }
    
    public int getUserId() {
        return user_id_;
    }
    
    public String getName() {
        return name_;
    }

    public String getAddress() {
        return address_;
    }

    public String getBirthday() {
        return birthday_;
    }

    public String getEmail() {
        return email_;
    }
    
    /**
     * Creates and returns an instance of the user from the provided JSON data.
     * 
     * @param user The JSONObject containing user data
     * @return user The new instance of user created from the JSON data
     */
    public static User valueOf(JSONObject user) {
        try {
            // TODO: Adapt the code in accordance with API
            final int user_id = user.getInt("id");
            final String name = user.getString("name");
            final String address = user.getString("address");
            final String birthday = user.getString("birthday");
            final String email = user.getString("email");
            
            return new User(user_id, name, address, birthday, email);
        } catch (final Exception e) {
            Log.i("User", "Error parsing JSON user object" + e.toString());
        }
        
        return null;
    }
    
}
