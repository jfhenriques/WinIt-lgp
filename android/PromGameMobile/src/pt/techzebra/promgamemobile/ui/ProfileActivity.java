package pt.techzebra.promgamemobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends Activity {
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        TextView text = new TextView(this);
        text.setText("Profile!");
        setContentView(text);
        
    }
}
