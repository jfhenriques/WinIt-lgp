package pt.techzebra.promgamemobile.client;

import java.util.ArrayList;

import org.json.JSONObject;


public class Quiz {
	private final int id_;
	private final String name_;
	private final ArrayList<Question> questions_;

	public Quiz(int id, String name, ArrayList<Question> questions) {
		id_ = id;
		name_ = name;
		questions_ = questions;
	}

	public Quiz(int id, String name) {
		id_ = id;
		name_ = name;
		questions_ = new ArrayList<Question>();
	}

	public int getId() {
		return id_;
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

	
//	public void play() {
//		System.out.println("Bem-vindo");
//		int numPer = 0;
//		int pontos = 0;
//		while (numPer < questions_.size()) {
//			Question p = questions_.get(numPer);
//			System.out.println(numPer + ":" + (p.getQuestionId()+1));
//			System.out.println();
//
//			Scanner s = new Scanner(System.in);
//			Scanner s1 = new Scanner(System.in);
//
//			String opt = "n";
//			int per = Integer.MAX_VALUE;
//			while (opt.equals("n")) {
//				for (int i = 0; i < p.getAnswers().size(); i++) {
//					Answer r = p.getAnswers().get(i);
//					System.out.println(i + ":" + r.getText());
//				}
//				System.out.println("Opt:");
//				per = s.nextInt();
//
//				System.out.println("Tem a certeza?(s/n)");
//				opt = s1.nextLine();
//			}
//
//			if (p.getAnswers().get(per).isCorrect()) {
//				pontos++;
//				System.out.println("Está correta");
//			} else {
//				System.out.println("Está incorreta");
//			}
//			numPer++;
//			System.out.println();
//		}
//		System.out.println("Pontos: " + pontos);
//		if (pontos == questions_.size())
//			System.out.println("Ganhaste a promoção");
//		System.out.println("GoodBye");
//	}
	
	
	public static Quiz valueOf(JSONObject quiz) {
	    // TODO: parse json
		return null;
	}
	
	
}
