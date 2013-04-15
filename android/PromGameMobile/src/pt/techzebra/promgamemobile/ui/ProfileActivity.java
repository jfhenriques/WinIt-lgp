package pt.techzebra.promgamemobile.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.User;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	//Activity variables
	ImageView profile_image_;
	TextView name_text_;
	TextView email_text_;
	TextView level_text_;
	TextView points_text_;
	
	User user_ = null;
	String auth_token;
	/*
	 * Set up user data that is displayed on this activity 
	 */
	public void setUserData(User u) throws NoSuchAlgorithmException{
		/*
		 * Set user profile image 
		 * */
		MessageDigest gravatar_hash = MessageDigest.getInstance("MD5");
		gravatar_hash.update(u.getEmail().getBytes());
	    byte byteData[] = gravatar_hash.digest();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < byteData.length; i++)
	        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	    
	    String gravatar_image = "http://www.gravatar.com/avatar/" + gravatar_hash;
	    Drawable drawable = LoadImageFromWebOperations(gravatar_image);
	    profile_image_.setImageDrawable(drawable);
	    
	    //Set user name
	    name_text_.setText(u.getName());
	    //Set email name
	    email_text_.setText(u.getEmail());
	    //set user level
	    level_text_.setText(u.getLevel());
	    //set user points
	    points_text_.setText(u.getPoints());
	}

	/*
	 * Auxiliar function to turn an url into a Drawable
	 */
     private Drawable LoadImageFromWebOperations(String url)
    {
         try
         {
             InputStream is = (InputStream) new URL(url).getContent();
             Drawable d = Drawable.createFromStream(is, "src name");
             return d;
         }catch (Exception e) {
             System.out.println("Exc="+e);
             return null;
         }
     }
	
	
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.profile_activity);
        
        profile_image_ = (ImageView) findViewById(R.id.profile_image);
        name_text_ = (TextView) findViewById(R.id.name_text);
        email_text_ = (TextView) findViewById(R.id.email_text);
        level_text_ = (TextView) findViewById(R.id.level_text);
        points_text_ = (TextView) findViewById(R.id.points_text);
        
        SharedPreferences preferences_ = PromGame.getAppContext().getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE);
        preferences_.getString(Constants.PREF_AUTH_TOKEN, auth_token);
        
        try {
			user_ = NetworkUtilities.fetchUserInformation("", auth_token, null);
			setUserData(user_);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
