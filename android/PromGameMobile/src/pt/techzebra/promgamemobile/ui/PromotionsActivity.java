package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class PromotionsActivity extends SherlockActivity {
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        setContentView(R.layout.promotions_activity);
    }
}
