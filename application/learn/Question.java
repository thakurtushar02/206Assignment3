package application.learn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a question. A question contains a video, a correct answer and three other answer
 * choices to be displayed in the quiz portion of the VARpedia app.
 * @author Lynette
 *
 */
public class Question {
	private File video;
	private List<String> answers = new ArrayList<String>();
	private String correct;
	private String[] dummy = {"tree", "dog", "potato", "grass", "snail", "cat", 
			"sky", "ocean", "chess", "sea", "pie", "cloud"};
	
	/**
	 * Creates a Question object
	 * @param video quiz video
	 * @param term the term the quiz video is about
	 */
	public Question(File video, String term) {
		this.video = video;
		correct = term.toLowerCase();
		answers.add(correct);
		
		// Assign three distinct dummy answers randomly
		while (answers.size() < 4) {
			Random rand = new Random();
			int index = rand.nextInt(dummy.length);
			if (! dummy[index].equals(correct) && ! answers.contains(dummy[index])) {
				answers.add(dummy[index]);
			}
		}
	}
	
	/**
	 * Returns the correct answer
	 * @return correct answer as a string
	 */
	public String getCorrectAnswer() {
		return answers.get(0);
	}
	
	/**
	 * Returns the list of answer options
	 * @return answers
	 */
	public List<String> getAnswers() {
		return answers;
	}

	/**
	 * Returns the file path of the video
	 * @return video file path as a string
	 */
	public String getVideo() {
		return video.toURI().toString();
	}
}
