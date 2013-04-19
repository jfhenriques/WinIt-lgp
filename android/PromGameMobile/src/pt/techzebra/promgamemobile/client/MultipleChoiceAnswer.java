package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;

public class MultipleChoiceAnswer extends Answer {
    private ArrayList<String> answers_;
    
    public MultipleChoiceAnswer(int id) {
        super(id);
        answers_ = new ArrayList<String>();
    }

    @Override
    public Object getContent() {
        return answers_;
    }

    public void addAnswer(String answer) {
        if(!answers_.contains(answer)){
            answers_.add(answer);
        }
    }
    
}
