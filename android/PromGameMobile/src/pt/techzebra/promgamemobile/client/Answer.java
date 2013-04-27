package pt.techzebra.promgamemobile.client;

import org.json.JSONException;
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

	public static Answer valueOf(JSONObject answer){
	   
        try {
            final int answer_id = answer.getInt("id");
           
    	    final String answer_type;
    	    if(!(answer_type = answer.getString("multi")).equals(null)){
    	        MultipleChoiceAnswer multiple_answer = new MultipleChoiceAnswer(answer_id);
    	        int size_array = answer.getInt("adsa");
    	        for(int i = 0; i<size_array; i++){
    	            
    	        }
    	        return new MultipleChoiceAnswer(answer_id);
    	    }else{
    	        return new TextAnswer(answer_id);
    	    }
	    
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
	}
}
