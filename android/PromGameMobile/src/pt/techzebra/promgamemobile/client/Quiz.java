package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Quiz {
    private final String name_;
    private final ArrayList<Question> questions_;

    public Quiz(String name, ArrayList<Question> questions) {
        name_ = name;
        questions_ = questions;
    }

    public Quiz(String name) {
        name_ = name;
        questions_ = new ArrayList<Question>();
    }

    public String getName() {
        return name_;
    }

    public ArrayList<Question> getQuestions() {
        return questions_;
    }

    public boolean addQuestion(Question question) {
        if (questions_.contains(question)) {
            return false;
        } else {
            questions_.add(question);
            return true;
        }
    }

    public void play() {
        /*
         * System.out.println("Bem-vindo"); int numPer = 0; int pontos = 0;
         * while (numPer < questions_.size()) { Question p =
         * questions_.get(numPer); System.out.println(numPer + ":" +
         * (p.getQuestionId()+1)); System.out.println();
         * 
         * Scanner s = new Scanner(System.in); Scanner s1 = new
         * Scanner(System.in);
         * 
         * String opt = "n"; int per = Integer.MAX_VALUE; while
         * (opt.equals("n")) { for (int i = 0; i < p.getAnswers().size(); i++) {
         * Answer r = p.getAnswers().get(i); System.out.println(i + ":" +
         * r.getText()); } System.out.println("Opt:"); per = s.nextInt();
         * 
         * System.out.println("Tem a certeza?(s/n)"); opt = s1.nextLine(); }
         * 
         * if (p.getAnswers().get(per).isCorrect()) { pontos++;
         * System.out.println("Está correta"); } else {
         * System.out.println("Está incorreta"); } numPer++;
         * System.out.println(); } System.out.println("Pontos: " + pontos); if
         * (pontos == questions_.size())
         * System.out.println("Ganhaste a promoção");
         * System.out.println("GoodBye");
         */
    }

    public static Quiz valueOf(JSONObject quiz) {
        try {
            if (quiz.getInt("s") == 0) {
                JSONObject value_quiz = quiz.getJSONObject("r");
                final String name = value_quiz.getString("name");
                final boolean is_quiz = value_quiz.getBoolean("is_quiz");
                if (is_quiz) {
                    Quiz new_quiz = new Quiz(name);
                    final JSONArray question_array = value_quiz
                            .getJSONArray("questions");
                    for (int i = 0; i < question_array.length(); i++) {
                        Question question = Question.valueOf(question_array
                                .getJSONObject(i));
                        new_quiz.addQuestion(question);
                    }

                    return new_quiz;
                } else {
                    return null;
                }
            } else {
                Log.i("Quiz",
                        "Error parsing JSON: message: " + quiz.getString("m"));
            }
        } catch (JSONException e) {
            Log.i("Quiz", "Error parsing JSON user object" + e.toString());
        }

        return null;
    }
}
