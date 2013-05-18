package pt.techzebra.winit.client;

import java.util.ArrayList;

public class MultipleChoiceAnswer extends Answer {
    private ArrayList<String> answers_;

    public MultipleChoiceAnswer(ArrayList<String> answer) {
        super();
        answers_ = answer;
    }

    @Override
    public Object getContent() {
        return answers_;
    }
}
