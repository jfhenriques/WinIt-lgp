package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class PromotionCodeActivity extends SherlockActivity {
    public static final String KEY_EXTRA_CODE = "code";

    private ActionBar action_bar_;
    
    private TextView code_text_;
    
    private String code_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.promotion_code_activity);
        
        action_bar_ = getSupportActionBar();
        action_bar_.setTitle(R.string.promotion);
        action_bar_.setDisplayHomeAsUpEnabled(true);
        
        code_ = getIntent().getExtras().getString(KEY_EXTRA_CODE);
        code_ = code_.substring(0, 10);
        
        code_text_ = (TextView) findViewById(R.id.code_text);
        code_text_.setText(code_);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }
}
