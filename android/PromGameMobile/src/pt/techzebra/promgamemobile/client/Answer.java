package pt.techzebra.promgamemobile.client;

import org.json.JSONObject;

public abstract class Answer {
	private final int id_;
	
	// TODO: just for test, delete at last implementation
	private final boolean correct_;
	
	public Answer(int id, boolean correct) {
		id_ = id;
		correct_ = correct;
	}
	
	public int getId() {
		return id_;
	}

	public abstract Object getContent();

	// TODO: remove after use
	public boolean isCorrect() {
		return correct_;
	}
	
	public boolean equals(Answer answer) {
		return (id_ == answer.id_);
	}

	//public static Answer valueOf(JSONObject answer);
}
