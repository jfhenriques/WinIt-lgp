package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public abstract class Answer {
    private int id_ = 0;

    public Answer() {
        id_++;
    }

    public int getId() {
        return id_;
    }

    public abstract Object getContent();

    public boolean equals(Answer answer) {
        return (id_ == answer.id_);
    }

    public static Answer valueOf(int type, JSONArray answers_choices) {
        try {
            if (type == 1) {
                // string
                return new TextAnswer();
            } else if (type == 2) {
                // radio
                ArrayList<String> ans = new ArrayList<String>();
                for (int i = 0; i < answers_choices.length(); i++) {
                    ans.add(answers_choices.getString(i));
                }
                return new RadioChoiceAnswer(ans);
            } else if (type == 3) {
                // multi
                ArrayList<String> ans = new ArrayList<String>();
                for (int i = 0; i < answers_choices.length(); i++) {
                    ans.add(answers_choices.getString(i));
                }
                return new MultipleChoiceAnswer(ans);
            } else {
                Log.i("Answer", "Error parsing JSON");
            }
        } catch (JSONException e) {
            Log.i("Answer", "Error parsing JSON");
        }
        return null;
    }
}
