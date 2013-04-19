package pt.techzebra.promgamemobile.client;

import org.json.JSONObject;

public abstract class Answer {
	private final int id_;
	
	
	public Answer(int id) {
		id_ = id;
	}
	
	public int getId() {
		return id_;
	}

	public abstract Object getContent();

	
	public boolean equals(Answer answer) {
		return (id_ == answer.id_);
	}

	//public static Answer valueOf(JSONObject answer);
}
