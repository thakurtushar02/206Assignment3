package gui.learn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Questions {

	private List<Question> questions = new ArrayList<Question>();
	
	public Questions() {
		
		List<String> qVideos = new ArrayList<String>();
		String command = "ls Quizzes/ | grep mp4$";
		
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		try {
			Process question = pb.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(question.getInputStream()));
			
			int exitStatus = question.waitFor();
			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					qVideos.add(line);
				}
			}
			
			if (! qVideos.isEmpty()) {
				for (String q: qVideos) {
					Question term = new Question(new File("Quizzes/"+q), q.substring(0, q.length()-4));
					this.addQuestion(term);
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addQuestion(Question question) {
		questions.add(question);
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
	
	public int numberOfQuestions() {
		return questions.size();
	}
}
