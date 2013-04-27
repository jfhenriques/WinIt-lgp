package pt.techzebra.promgamemobile.games.quiz;

import java.util.ArrayList;

import pt.techzebra.promgamemobile.Constants;
import pt.techzebra.promgamemobile.PromGame;
import pt.techzebra.promgamemobile.R;
import pt.techzebra.promgamemobile.client.NetworkUtilities;
import pt.techzebra.promgamemobile.client.Quiz;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

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

        String authen_token = preferences_editor.getString(
                Constants.PREF_AUTH_TOKEN, null);

        Log.i(TAG, "new quiz");

        quiz_ = NetworkUtilities.fetchQuizGame("1", authen_token);

        /*
         * Question p1 = new Question(1,
         * "Que nome se dá a alguém que nega a existência de Deus?");
         * MultipleChoiceAnswer p1r1 = new MultipleChoiceAnswer(1);
         * p1r1.addAnswer("Judeu"); p1r1.addAnswer("Ateu");
         * p1r1.addAnswer("Cristão"); p1r1.addAnswer("Pagão");
         * p1.setAnswer(p1r1); quiz_.addQuestion(p1);
         * 
         * Question p2 = new Question(2,
         * "Qual das seguintes musicas pertence a Michael Jackson?");
         * MultipleChoiceAnswer p1r2 = new MultipleChoiceAnswer(2);
         * p1r2.addAnswer("Edge of Glory"); p1r2.addAnswer("Thriller");
         * p1r2.addAnswer("Waka Waka"); p1r2.addAnswer("Paparazzi");
         * p2.setAnswer(p1r2); quiz_.addQuestion(p2);
         * 
         * Question p3 = new Question(3,
         * "Que ator foi personagem principal no filme \'O Exterminador\'?");
         * MultipleChoiceAnswer p1r3 = new MultipleChoiceAnswer(3);
         * p1r3.addAnswer("Arnold Schwarzenegger");
         * p1r3.addAnswer("Sylvestre Stallone"); p1r3.addAnswer("Vin Diesel");
         * p1r3.addAnswer("Van Damme"); p3.setAnswer(p1r3);
         * quiz_.addQuestion(p3);
         * 
         * Question p4 = new Question(4, "De quem é a musica \'Loca\'?");
         * MultipleChoiceAnswer p1r4 = new MultipleChoiceAnswer(3);
         * p1r4.addAnswer("Shakira"); p1r4.addAnswer("Carolina Herrera");
         * p1r4.addAnswer("Kat DeLuna"); p1r4.addAnswer("Naty Botero");
         * p4.setAnswer(p1r4); quiz_.addQuestion(p4);
         */

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

    private static class QuizCollectionPagerAdapter extends
            FragmentStatePagerAdapter {
        public QuizCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new QuestionObjectFragment();
            Bundle args = new Bundle();
            args.putString("question", quiz_.getQuestions().get(i).getTitle());

            @SuppressWarnings("unchecked")
            ArrayList<String> answerslist = ((ArrayList<String>) quiz_
                    .getQuestions().get(i).getAnswer().getContent());
            args.putInt("num_answers", answerslist.size());
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
            Bundle args = getArguments();
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
            return root_view;
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
