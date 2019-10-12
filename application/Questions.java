package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Questions {

	private List<Question> questions = new ArrayList<Question>();
	
	public void addQuestion(Question question) {
		questions.add(question);
		System.out.println("question added!");
	}
	
	public Question getQuestion() {
		int index;
		if (questions.size() == 1) {
			index = 0;
		} else {
			Random rand = new Random();
			index = rand.nextInt(questions.size());
		}
		
		return questions.get(index);
	}
}
