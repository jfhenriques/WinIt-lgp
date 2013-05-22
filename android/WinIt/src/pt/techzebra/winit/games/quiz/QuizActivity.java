package pt.techzebra.winit.games.quiz;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.R;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Quiz;
import pt.techzebra.winit.platform.FetchQuizTask;
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
import com.viewpagerindicator.PageIndicator;

public class QuizActivity extends SherlockFragmentActivity implements FetchQuizTask.AsyncResponse {
    private static final String TAG = "QuizActivity";
    
    private ActionBar action_bar_;

    QuizCollectionPagerAdapter quiz_collection_adapter_;
    ViewPager view_pager_;
    PageIndicator page_indicator_;
    static Quiz quiz_;
    String authen_token_;
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
        action_bar_.setDisplayHomeAsUpEnabled(true);
        action_bar_.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_bg_single_player));

        authen_token_ = WinIt.getAuthToken();
        promotion_id = "1";
     
        FetchQuizTask fetch_quiz_task = new FetchQuizTask(this);
        fetch_quiz_task.setDelegate(this);
        fetch_quiz_task.execute(promotion_id);
    }
    
    
    
    public void initializeAndPopulateView() {
        
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
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_submit:
                submitAnswers();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        
        return true;
    }

    private void submitAnswers() {
        for (int i = 0; i < quiz_.getQuestions().size(); ++i) {
            if (quiz_.getQuestions().get(i).getAnswered() == -1) {
                Toast.makeText(this, "Responda a todas as questões",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        NetworkUtilities.submitAnswersQuizGame(promotion_id, authen_token_,
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
            RadioGroup radio_group = (RadioGroup) root_view
                    .findViewById(R.id.answers_group);
            int size_answers = args.getInt("num_answers");
            for (int j = 0; j < size_answers; j++) {
                View radio_button = inflater.inflate(R.layout.quiz_radio_button, radio_group, false);
                String str = args.getString(("answer" + j));
                ((RadioButton) radio_button).setText(str);
                ((RadioButton) radio_button).setId(j);
                ((RadioButton) radio_button).setGravity(Gravity.CENTER_VERTICAL);
                radio_group.addView(radio_button);
            }

            radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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



    @Override
    public void processFinish(Quiz result) {
        quiz_ = result;

        quiz_collection_adapter_ = new QuizCollectionPagerAdapter(
                getSupportFragmentManager());
        view_pager_ = (ViewPager) findViewById(R.id.pager);
        view_pager_.setAdapter(quiz_collection_adapter_);

        page_indicator_ = (PageIndicator) findViewById(R.id.indicator);
        page_indicator_.setViewPager(view_pager_);
        
        view_pager_.invalidate();
    }
}
