package pt.techzebra.promgamemobile.platform;

import pt.techzebra.promgamemobile.ui.SignupActivity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SignupViewPager extends ViewPager {
    
    public SignupViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int step = getCurrentItem();
        boolean valid_input = ((SignupActivity) getContext()).validateInput(step);
        
        if (valid_input || step == 1) {
            return super.onTouchEvent(event);
        }
        
        return false;
    }
}
