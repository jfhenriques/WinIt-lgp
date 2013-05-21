package pt.techzebra.winit.ui;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.platform.LoadingAvailablePromotionsList;
import pt.techzebra.winit.platform.LoadingUserInfo;
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
            	LoadingUserInfo lui = new LoadingUserInfo(this);
                lui.execute();
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_log_out:
            	WinIt.clearUserData();
                Intent i = new Intent(this, AuthenticationActivity.class);
                Toast.makeText(this, "Logout successful!", Toast.LENGTH_SHORT).show();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
    

    
    public void handleRoomSelection(View view) {
        switch (view.getId()) {
            case R.id.single_player_layout:
    //            Intent intent = new Intent(this, PromotionsActivity.class);
    //            startActivity(intent);
                LoadingAvailablePromotionsList apl = new LoadingAvailablePromotionsList(this);
                apl.execute();
                break;
            case R.id.cooperative_layout:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.competitive_layout:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.trading_layout:
                Intent intent = new Intent(this, TradingActivity.class);
                startActivity(intent);
                //TODO added transition animations, wainting feedback
                Utilities.addActivityAnimations(this);
                break;
        }
        
    }
}
