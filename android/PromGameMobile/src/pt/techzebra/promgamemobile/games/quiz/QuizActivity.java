package pt.techzebra.promgamemobile.games.quiz;

import pt.techzebra.promgamemobile.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class QuizActivity extends SherlockFragmentActivity {
    private static final String TAG = "QuizActivity";
    private ActionBar action_bar_;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
    private GestureDetector gesture_detector_;
    private OnTouchListener gesture_listener_;
    
    QuizCollectionPagerAdapter quiz_collection_adapter_;
    ViewPager view_pager_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.quiz_pager);
        
        action_bar_ = getSupportActionBar();
        action_bar_.setTitle("Quiz Game");
        
        quiz_collection_adapter_ = new QuizCollectionPagerAdapter(getSupportFragmentManager());
        
        view_pager_ = (ViewPager) findViewById(R.id.pager);
        view_pager_.setAdapter(quiz_collection_adapter_);
        
        /*
        gesture_detector_ = new GestureDetector(this, new QuizGestureDetector());
        gesture_listener_ = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture_detector_.onTouchEvent(event);
            }
        };
        
        View question_layout_wrapper = findViewById(R.id.question_wrapper_layout);
        
        question_layout_wrapper.setOnTouchListener(gesture_listener_);
        */
    }
    
    private static class QuizCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public QuizCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new QuestionObjectFragment();
            Bundle args = new Bundle();
            args.putInt(QuestionObjectFragment.ARG_OBJECT, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO: return number of questions
            return 100;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position + 1);
        }
        
    }
    
    public static class QuestionObjectFragment extends Fragment {
        public static final String ARG_OBJECT =  "object";
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle saved_instance_state) {
            View root_view = inflater.inflate(R.layout.question_fragment, container, false);
            Bundle args = getArguments();
            //((TextView) root_view.findViewById(R.id.counter_text)).setText(Integer.toString(args.getInt(ARG_OBJECT)));
            
            return root_view;
        }
    }
    
    private static class QuizGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocity_x,
                float velocity_y) {
            
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocity_x) > SWIPE_THRESHOLD_VELOCITY) {
                // right to left
                Log.d(TAG, "Left Swipe");
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocity_x) > SWIPE_THRESHOLD_VELOCITY) {
                //left to right
                Log.d(TAG, "Right Swipe");
                return true;
            }
            
            return false;
        }
        
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
