package pt.techzebra.promgamemobile.ui;



import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.User;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class ProfileActivity extends SherlockActivity {
	//private static final String TAG = "ProfileActivity";

	private ActionBar action_bar_;

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
	public void setUserData(User u) throws Exception{
		/*
		 * TODO Set user profile image (gravatar)
		 * */

		//Set user name
		name_text_.setText(u.getName());
		//Set email name
		email_text_.setText(u.getEmail());
		//set user level
		level_text_.setText("Level " + u.getLevel());
		//set user points
		points_text_.setText(u.getPoints()+"/500");
	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap, int pixels) {
		return bitmap;
	}

	@Override
	protected void onCreate(Bundle saved_instance_state) {
		super.onCreate(saved_instance_state);
		setContentView(R.layout.profile_activity);

		action_bar_ = getSupportActionBar();
		action_bar_.setTitle(R.string.profile);

		profile_image_ = (ImageView) findViewById(R.id.profile_image);

		Resources r = getResources();

		Bitmap bitmap = BitmapFactory.decodeResource(r, R.drawable.photo);
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		final Rect rect = new Rect(0, 0, bitmap.getWidth(),
				bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2,
				bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		Drawable[] layers = new Drawable[2];
		layers[0] = new BitmapDrawable(r, output);
		layers[1] = r.getDrawable(R.drawable.circular_photo);
		LayerDrawable layerDrawable = new LayerDrawable(layers);

		//        profile_image_.setImageDrawable(layerDrawable);
		profile_image_.setImageBitmap(output);
		//		
		name_text_ = (TextView) findViewById(R.id.name_text);
		email_text_ = (TextView) findViewById(R.id.email_text);
		level_text_ = (TextView) findViewById(R.id.level_text);
		points_text_ = (TextView) findViewById(R.id.points_text);
		
		try {
			user_ = (User) getIntent().getSerializableExtra("User");
			setUserData(user_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleProfileSelection(View view){
		 switch (view.getId()) {
		 case R.id.badges_view:
			 //Intent i = new Intent(this, BadgesActivity.class);
			 //startActivity(i);
			 return;
		 case R.id.tags_view:
			 //Intent in = new Intent(this, TagsActivity.class);
			 //startActivity(in);
			 return;
		 }
	}
}


