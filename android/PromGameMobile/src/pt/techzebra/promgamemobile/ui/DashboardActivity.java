package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class DashboardActivity extends SherlockActivity {
    private static final String TAG = "DashboardActivity";

    private SharedPreferences preferences_;

    private ActionBar action_bar_;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);

        action_bar_ = getSupportActionBar();
        action_bar_.setTitle(R.string.dashboard);

        preferences_ = getSharedPreferences(Constants.USER_PREFERENCES,
                Context.MODE_PRIVATE);

        boolean logged_in = preferences_.getBoolean(Constants.PREF_LOGGED_IN,
                false);
        if (!logged_in) {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.dashboard_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_log_out:
                // TODO: erase user local information
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
    
    public void handleRoomSelection(View view) {
        String msg = null;
        switch (view.getId()) {
        case R.id.single_player_layout:
            msg = "Single Player";
            break;
        case R.id.cooperative_layout:
            msg = "Cooperative";
            break;
        case R.id.competitive_layout:
            msg = "Competitive";
            break;
        case R.id.trading_layout:
            msg = "Trading";
            break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
