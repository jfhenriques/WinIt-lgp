package pt.techzebra.promgamemobile.client;

public class TextAnswer extends Answer {
    private String answer_;
    
    public TextAnswer(int id, boolean correct) {
        super(id, correct);
    }

    @Override
    public Object getContent() {
        return answer_;
    }
}
