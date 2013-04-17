package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;

public class MultipleChoiceAnswer extends Answer {
    private ArrayList<String> answers_;
    
    public MultipleChoiceAnswer(int id, boolean correct) {
        super(id, correct);
    }

    @Override
    public Object getContent() {
        return answers_;
    }

//    public void addAnswer(String answer) {
//        
//    }
    
}
