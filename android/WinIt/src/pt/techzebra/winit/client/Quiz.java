package pt.techzebra.winit.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Quiz {
    private final String name_;
    private final ArrayList<Question> questions_;

    public Quiz(String name, ArrayList<Question> questions) {
        name_ = name;
        questions_ = questions;
    }

    public Quiz(String name) {
        name_ = name;
        questions_ = new ArrayList<Question>();
    }

    public String getName() {
        return name_;
    }

    public ArrayList<Question> getQuestions() {
        return questions_;
    }

    public boolean addQuestion(Question question) {
        if (questions_.contains(question)) {
            return false;
        } else {
            questions_.add(question);
            return true;
        }
    }

    public static Quiz valueOf(JSONObject quiz) {

        try {
            if (NetworkUtilities.validResponse(quiz)) {
                JSONObject value_quiz = NetworkUtilities
                        .getResponseContent(quiz);
                final String name = value_quiz.getString("name");
                final boolean is_quiz = value_quiz.getBoolean("is_quiz");
                if (is_quiz) {
                    Quiz new_quiz = new Quiz(name);
                    final JSONArray question_array = value_quiz
                            .getJSONArray("questions");
                    for (int i = 0; i < question_array.length(); i++) {
                        Question question = Question.valueOf(question_array
                                .getJSONObject(i));
                        new_quiz.addQuestion(question);
                    }

                    return new_quiz;
                } else {
                    Log.i("Quiz", "This is not a quiz");
                    return null;
                }
            } else {
                Log.i("Quiz", "Error parsing JSON: message: "
                        + NetworkUtilities.getResponseMessage(quiz));
                return null;
            }
        } catch (JSONException e) {
            Log.i("Quiz", "Error parsing JSON user object" + e.toString());
            return null;
        }
    }
}
