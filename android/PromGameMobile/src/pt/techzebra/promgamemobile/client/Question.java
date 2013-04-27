package pt.techzebra.promgamemobile.client;

import org.json.JSONException;
import org.json.JSONObject;

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
            //final int choices = question.getString("answer_choices");
            //Answer answer = Answer.valueOf(question);
            
            return new Question(question_id, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	    
		return null;
	}
}
