package pt.techzebra.promgamemobile.client;

public class TextAnswer extends Answer {
    private String answer_;
    
    public TextAnswer() {
        super();
    }

    @Override
    public Object getContent() {
        return answer_;
    }
}
