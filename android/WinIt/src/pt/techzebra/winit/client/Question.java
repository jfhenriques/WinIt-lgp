package pt.techzebra.winit.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Question {
    private final int id_;
    private final String title_;
    private Answer answer_;
    private int answered_ = -1;

    public Question(int id, String title, Answer answer) {
        id_ = id;
        title_ = title;
        answer_ = answer;
    }

    public Question(int id, String title) {
        id_ = id;
        title_ = title;
        answer_ = null;
    }

    public void setAnswer(Answer answer) {
        answer_ = answer;
    }

    public Answer getAnswer() {
        return answer_;
    }

    public int getAnswered() {
        return answered_;
    }

    public void setAnswered(int answered) {
        answered_ = answered;
    }

    public String getTitle() {
        return title_;
    }

    public boolean equals(Question question) {
        return (id_ == question.id_);
    }

    public int getId() {
        return id_;
    }

    public static Question valueOf(JSONObject question) {
        try {
            final int question_id = question.getInt("qid");
            final String title = question.getString("question");
            final int type = question.getInt("type");
            
            Answer answer = null;
            if (type != 1) {
                JSONArray answers_choices = question
                        .getJSONArray("answer_choices");
                answer = Answer.valueOf(type, answers_choices);
            } else {
                answer = Answer.valueOf(type, new JSONArray());
            }
            return new Question(question_id, title, answer);
        } catch (JSONException e) {
            Log.i("Question", "Error parsing JSON");
        }

        return null;
    }
}
