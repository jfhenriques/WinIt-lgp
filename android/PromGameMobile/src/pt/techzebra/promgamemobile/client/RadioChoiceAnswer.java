package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;

public class RadioChoiceAnswer extends Answer {
    private ArrayList<String> answers_;

    public RadioChoiceAnswer(ArrayList<String> answers) {
        super();
        answers_ = answers;

    }

    @Override
    public Object getContent() {
        return answers_;
    }

}
