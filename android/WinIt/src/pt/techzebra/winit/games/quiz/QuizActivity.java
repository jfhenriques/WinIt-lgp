package pt.techzebra.winit.games.quiz;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.PromGame;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Quiz;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class QuizActivity extends SherlockFragmentActivity {
    private static final String TAG = "QuizActivity";
    private ActionBar action_bar_;
    /*
     * private static final int SWIPE_MIN_DISTANCE = 50; private static final
     * int SWIPE_MAX_OFF_PATH = 250; private static final int
     * SWIPE_THRESHOLD_VELOCITY = 200;
     * 
     * private GestureDetector gesture_detector_; private OnTouchListener
     * gesture_listener_;
     */
    QuizCollectionPagerAdapter quiz_collection_adapter_;
    ViewPager view_pager_;
    static Quiz quiz_;
    String authen_token;
    String promotion_id;
    private TextView points_text_;
    private TextView correct_answers_text_;

    Handler handler_ = new Handler();

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);

        setContentView(R.layout.quiz_pager);

        action_bar_ = getSupportActionBar();
        action_bar_.setTitle("Quiz Game");

        quiz_collection_adapter_ = new QuizCollectionPagerAdapter(
                getSupportFragmentManager());
        view_pager_ = (ViewPager) findViewById(R.id.pager);
        view_pager_.setAdapter(quiz_collection_adapter_);
        SharedPreferences preferences_editor = PromGame.getAppContext()
                .getSharedPreferences(Constants.USER_PREFERENCES,
                        Context.MODE_PRIVATE);
        authen_token = preferences_editor.getString(Constants.PREF_AUTH_TOKEN,
                null);
        promotion_id = "1";

        quiz_ = new Quiz("");
        NetworkUtilities.attemptFetchQuizGame(promotion_id, authen_token,
                handler_, this);

        // if(quiz_ == null){
        // Toast.makeText(this,
        // "Pedimos desculpa, o erro será corrigido em breve!",
        // Toast.LENGTH_SHORT).show();
        // finish();
        // }
        /*
         * gesture_detector_ = new GestureDetector(this, new
         * QuizGestureDetector()); gesture_listener_ = new OnTouchListener() {
         * 
         * @Override public boolean onTouch(View v, MotionEvent event) { return
         * gesture_detector_.onTouchEvent(event); } };
         * 
         * View question_layout_wrapper =
         * findViewById(R.id.question_wrapper_layout);
         * 
         * question_layout_wrapper.setOnTouchListener(gesture_listener_);
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_quiz, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_submit:
            submitAnswers();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void load(Quiz quiz) {
        quiz_ = quiz;
        ViewGroup vg = (ViewGroup) findViewById(R.id.pager);
        vg.invalidate();
    }

    private void submitAnswers() {

        for (int i = 0; i < quiz_.getQuestions().size(); i++) {
            if (quiz_.getQuestions().get(i).getAnswered() == -1) {
                Toast.makeText(this, "Responda a todas as questões",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        NetworkUtilities.submitAnswersQuizGame(promotion_id, authen_token,
                quiz_.getQuestions(), handler_, this);
    }

    private static class QuizCollectionPagerAdapter extends
            FragmentStatePagerAdapter {
        public QuizCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.d(TAG, "" + i);
            Fragment fragment = new QuestionObjectFragment();
            Bundle args = new Bundle();
            args.putString("question", quiz_.getQuestions().get(i).getTitle());
            args.putInt("num_question", i);

            @SuppressWarnings("unchecked")
            ArrayList<String> answerslist = ((ArrayList<String>) quiz_
                    .getQuestions().get(i).getAnswer().getContent());
            args.putInt("num_answers", answerslist.size());
            Log.d(TAG, "num answers: " + answerslist.size());
            for (int j = 0; j < answerslist.size(); j++) {
                args.putString("answer" + j, answerslist.get(j));
            }
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return quiz_.getQuestions().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf((position + 1));
        }

    }

    public static class QuestionObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "question";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle saved_instance_state) {
            View root_view = inflater.inflate(R.layout.question_fragment,
                    container, false);
            final Bundle args = getArguments();
            ((TextView) root_view.findViewById(R.id.question_text))
                    .setText(args.getString("question"));

            // TODO: mudar para vários tipos de resposta
            RadioGroup radio = (RadioGroup) root_view
                    .findViewById(R.id.answers_group);
            int size_answers = args.getInt("num_answers");
            for (int j = 0; j < size_answers; j++) {
                RadioButton r = new RadioButton(getActivity());
                String str = args.getString(("answer" + j));
                r.setText(str);
                r.setId(j);
                r.setGravity(Gravity.CENTER_VERTICAL);
                radio.addView(r);
            }

            radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    quiz_.getQuestions().get(args.getInt("num_question"))
                            .setAnswered(checkedId);
                }
            });

            return root_view;
        }
    }

    public void getResultSubmitedAnswers(JSONObject responseContent) {
        try {
            String won = NetworkUtilities.getResponseContent(responseContent)
                    .getString("won");
            String correct = NetworkUtilities.getResponseContent(
                    responseContent).getString("correct");

            // TODO: change
            /*Toast.makeText(
                    this,
                    "Ganhaste: " + won + " com: " + correct
                            + " respostas correctas!", Toast.LENGTH_SHORT)
                    .show();*/
            
            LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
            View popupView = layoutInflater.inflate(R.layout.popup_endquiz, null);  
            final PopupWindow popup_window = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
            
            points_text_ = (TextView) findViewById(R.id.points_text);
            points_text_.setText(won);
            
            correct_answers_text_ = (TextView) findViewById(R.id.correct_answers_text);
            correct_answers_text_.setText(correct);
            
            Button dismiss_button = (Button)popupView.findViewById(R.id.dismiss_button);
            dismiss_button.setOnClickListener(new Button.OnClickListener(){
            	@Override
            	public void onClick(View v) {
            		// TODO Auto-generated method stub
            		popup_window.dismiss();
            	}
            });
            
            
            //popup_window.showAsDropDown(btnOpenPopup, 50, -30);
            
             
            
        } catch (JSONException e) {
            Log.i(TAG,
                    "Error to get response: "
                            + NetworkUtilities
                                    .getResponseContent(responseContent));
        }
    }

    /*
     * private static class QuizGestureDetector extends SimpleOnGestureListener
     * {
     * 
     * @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float
     * velocity_x, float velocity_y) {
     * 
     * if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) { return false;
     * }
     * 
     * if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocity_x) >
     * SWIPE_THRESHOLD_VELOCITY) { // right to left Log.d(TAG, "Left Swipe");
     * return true; } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
     * Math.abs(velocity_x) > SWIPE_THRESHOLD_VELOCITY) { //left to right
     * Log.d(TAG, "Right Swipe"); return true; } return false; }
     * 
     * @Override public boolean onDown(MotionEvent e) { return true; } }
     */
}
