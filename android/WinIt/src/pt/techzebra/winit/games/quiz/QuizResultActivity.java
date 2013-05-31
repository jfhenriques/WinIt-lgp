package pt.techzebra.winit.games.quiz;

import pt.techzebra.winit.R;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class QuizResultActivity extends SherlockActivity {
    public static final String KEY_QUIZ_RESULT = "result";
    public static final String KEY_QUIZ_NUM_CORRECT_ANSWERS = "num_correct_answers";
    public static final String KEY_QUIZ_POINTS = "points";
    
    private TextView result_text_;
    private TextView details_text_;
    
    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.quiz_result);
        
        result_text_ = (TextView) findViewById(R.id.result_text);
        details_text_ = (TextView) findViewById(R.id.details_text);
        
        Bundle extras = getIntent().getExtras();
        boolean result = extras.getBoolean(KEY_QUIZ_RESULT);
        int num_correct_answers = extras.getInt(KEY_QUIZ_NUM_CORRECT_ANSWERS);
        int points = extras.getInt(KEY_QUIZ_POINTS);
        
        result_text_.setText(result ? R.string.you_won : R.string.you_lost);
        details_text_.setText(getResources().getQuantityString(R.plurals.numberOfQuestionsCorrectlyAnswered, num_correct_answers, num_correct_answers));
    }
}
