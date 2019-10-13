package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Question {
	private File video;
	private List<String> answers = new ArrayList<String>();
	private String correct;
	private String[] dummy = {"tree", "dog", "potato", "grass", "snail", "cat", "sky", "ocean"};
	
	public Question(File video, String term) {
		this.video = video;
		correct = term;
		answers.add(correct);
		
		while (answers.size() < 4) {
			Random rand = new Random();
			int index = rand.nextInt(dummy.length);
			if (! dummy[index].equals(correct)) {
				answers.add(dummy[index]);
			}
		}
	}
	
	public String getCorrectAnswer() {
		return answers.get(0);
	}
	
	public List<String> getAnswers() {
		return answers;
	}

	public String getVideo() {
		return video.toURI().toString();
	}
}
