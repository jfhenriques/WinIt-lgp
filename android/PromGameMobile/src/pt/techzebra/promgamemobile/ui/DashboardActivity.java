package pt.techzebra.promgamemobile.ui;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.platform.LoadingAvailablePromotionsList;
import pt.techzebra.promgamemobile.platform.LoadingUserInfo;
import pt.techzebra.promgamemobile.games.quiz.QuizActivity;
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
            	//TODO sempre que ele vai ao profile tenta ir sacar as cenas ao servidor. Talvez se existir um db local no android guardar esta informação. Neste momento só tá a aceder ao perfil se tiver internet activa
                String auth = getSharedPreferences("auth_token", Context.MODE_PRIVATE).toString();
            	LoadingUserInfo lui = new LoadingUserInfo(this);
                lui.execute();
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_log_out:
                // TODO: erase user local information
            	SharedPreferences.Editor preferences_editor = PromGame.getAppContext().getSharedPreferences(
                        Constants.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
                preferences_editor.putBoolean(Constants.PREF_LOGGED_IN, false);
                preferences_editor.putString(Constants.PREF_AUTH_TOKEN, "");
                preferences_editor.commit();
                Intent i = new Intent(this, AuthenticationActivity.class);
                Toast.makeText(this, "Logout successful!", Toast.LENGTH_SHORT).show();
                startActivity(i);
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
//            Intent intent = new Intent(this, PromotionsActivity.class);
//            startActivity(intent);
            LoadingAvailablePromotionsList apl = new LoadingAvailablePromotionsList(this);
            apl.execute();
            return;
            //break;
        case R.id.cooperative_layout:
            msg = "Cooperative";
            break;
        case R.id.competitive_layout:
            msg = "Competitive";
            break;
        case R.id.trading_layout:
            msg = "Trading";
            Intent intent = new Intent(this, TradingHouseActivity.class);
            startActivity(intent);
            break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
