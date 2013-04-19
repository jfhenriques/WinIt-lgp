package pt.techzebra.promgamemobile.client;

public class TextAnswer extends Answer {
    private String answer_;
    
    public TextAnswer(int id) {
        super(id);
    }

    @Override
    public Object getContent() {
        return answer_;
    }
}
