package application.learn;

import java.io.File;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.create.Help;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
/**
 * This class represents the Learn tab in the VARpedia application. Here the user completes a quiz based
 * on the creations they have made using the app. 
 * @author Lynette
 *
 */
public class Learn {

	private Tab tab;
	private BorderPane content = new BorderPane();
	
	private Label learn = new Label();
	private Label end = new Label();
	
	private Button start = new Button();
	private Button playAgain = new Button();
	private Button submit = new Button();
	private Button back = new Button();
	private Button _helpButton = new Button("?");
	
	private int current;
	private int correct;
	private Question question;
	private QuestionSet qSet = new QuestionSet();
	
	private ToggleGroup ops = new ToggleGroup();
	private List<ToggleButton> answer = new ArrayList<ToggleButton>();
	private List<Button> qNums = new ArrayList<Button>();
	
	private MediaView mView;
	private Media media;
	private MediaPlayer player;
	private ImageView image = new ImageView();
	
	private final double BUTTON_WIDTH = 75;
	private final double BUTTON_HEIGHT = 75;
	private final String CURRENT_STYLE = "-fx-background-color: #6F93FF";

	public Learn(Tab tab, QuestionSet set) {
		this.tab = tab;
		qSet = set;
	}

	/**
	 * Sets the contents of the tab when it is first selected (i.e. the quiz start screen)
	 */
	public void setContents() {
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		Help helpContents = new Help();
		ContextMenu cm = helpContents.getContextMenu();
		_helpButton.setContextMenu(cm);
		_helpButton.setOnAction(e -> {
			cm.show(_helpButton, Side.BOTTOM, 0, 0);
		});
		
		HBox learnPlusHelp = new HBox(learn, spacer, _helpButton);
		
		content.getChildren().removeAll(content.getChildren()); // remove any nodes that may have been set previously
		
		// Quiz unable to start if there are no questions to display
		if(qSet.numberOfQuestions() == 0) {
			learn.setText("You don't have any quizzes!\n\nHead over to the Create tab "
					+ "and make some creations first");
			content.setTop(learn);
		}else {
			learn.setText("Time to review what you have learned!");
			
			Image i = null;
			try {
				i = new Image(new File(".resources/learn/man.png").toURI().toURL().toString());
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			image.setImage(i);
			image.setPreserveRatio(true);
			image.setFitWidth(400);
			
			start.setText("Start Quiz");
			start.setOnAction(e -> quizStart());
			
			content.setTop(learnPlusHelp);
			learn.setAlignment(Pos.CENTER);
			
			content.setCenter(image);	
			content.setBottom(start);
			BorderPane.setAlignment(start, Pos.CENTER);
		}

		learn.setFont(new Font("Arial", 16));
		learn.setPadding(new Insets(20));
		
		content.setPadding(new Insets(20));
		
		
		
		BorderPane.setAlignment(learn, Pos.CENTER);
		tab.setContent(content);
	}

	/**
	 * Starts the quiz. This method gets all the questions (the logic) and sets up what buttons do
	 * when they are pressed.
	 */
	public void quizStart() {
		correct = 0;
		current = 1;
		quizSetContent();
		
		// Check button only appears if an answer is selected
		ops.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				if (ops.getSelectedToggle() != null) {
					submit.setVisible(true);
				} else {
					submit.setVisible(false);
				}
			}
		});

		submit.setOnAction(e -> {
			if (submit.getText() == "Check Answer") {
				
				Button btn = qNums.get(current - 1);
				ToggleButton selection = (ToggleButton) ops.getSelectedToggle();
				ToggleButton correctAnswer = null;
				
				for (ToggleButton t : answer) {
					if (t.getText().equals(question.getCorrectAnswer())) {
						correctAnswer = t;
						break;
					}
				}
				// Set the colour of the question number and answer buttons whether it is correct or not
				if (isCorrect(question)) {
					btn.setStyle("-fx-background-color:green");
					selection.setStyle("-fx-border-color:green; ");
					correct++;
				} else {
					btn.setStyle("-fx-background-color:red");
					selection.setStyle("-fx-border-color:red");
					correctAnswer.setStyle("-fx-border-color:green");
				}
				// Set button text to finish on the last question
				if (current < 5) {
					submit.setText("Next");
				} else if (current == 5) {
					submit.setText("Finish");
				}
			} else if (submit.getText() == "Next") {
				current++;
				nextQuestion();
				
				// Reset the styling of the answer buttons after it has been checked
				for (ToggleButton toggle : answer) {
					toggle.setStyle(null);
				}
				submit.setText("Check Answer");
			} else if (submit.getText() == "Finish") {
				endScreen();
			}

		});
	}
 
	/**
	 * Retrieves and sets the video and text for answer buttons for each question
	 * @param question Question to be asked
	 */
	public void setQuestion(Question question) {
		
		qNums.get(current-1).setStyle(CURRENT_STYLE);

		// Shuffles and sets the text for the answer buttons
		Collections.shuffle(answer);
		for (int i = 0; i < 4; i++) {
			answer.get(i).setText(question.getAnswers().get(i));
			answer.get(i).setSelected(false);
		}

		// Retrieve and set the quiz video in the mediaplayer
		media = new Media(question.getVideo());
		player = new MediaPlayer(media);
		player.setAutoPlay(true);
		player.setCycleCount(MediaPlayer.INDEFINITE);

		if (mView == null) {
			mView = new MediaView(player);
		} else {
			mView.setMediaPlayer(player);
		}
	}

	/**
	 * Checks if the selected answer is correct
	 * @param	question Question that is being asked
	 * @return	true if correct, otherwise false
	 * 		
	 */
	public boolean isCorrect(Question question) {
		ToggleButton selected = (ToggleButton) ops.getSelectedToggle();
		if (selected.getText() == question.getCorrectAnswer()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the next question and sets it in the GUI
	 */
	public void nextQuestion() {
		question = qSet.getQuestion();
		setQuestion(question);
	}

	/**
	 * Sets the end screen once the user has completed all the questions 
	 */
	public void endScreen() {
		content.getChildren().removeAll(content.getChildren());
		end.setText("You got "+correct+"/5!");
		
		HBox buttons = new HBox();

		Image i = null;
		try {
			i = new Image(new File(".resources/learn/thumb.png").toURI().toURL().toString());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		image.setImage(i);
		image.setPreserveRatio(true);
		image.setFitWidth(400);

		playAgain.setText("Play Again");
		playAgain.setOnAction(e -> quizStart());
		back.setText("Back to Start");
		back.setOnAction(e -> setContents());
		back.setVisible(true);
		
		buttons.getChildren().addAll(playAgain, back);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);

		content.setTop(end);
		content.setCenter(image);
		content.setBottom(buttons);
		BorderPane.setAlignment(end, Pos.CENTER);
	}
	
	/**
	 * Sets the GUI components of the quiz after the user decides to start the quiz. It places all
	 * the necessary buttons required for the quiz tab after clicking "Start quiz".
	 */
	public void quizSetContent() {
		// clear collections every time quiz starts (in case of replays)
		qNums.clear();
		answer.clear();

		content.getChildren().removeAll(content.getChildren());

		HBox options = new HBox();
		HBox check = new HBox();
		VBox qNumbers = new VBox();
		VBox main = new VBox();

		Label quiz = new Label("What is being shown in the video? Choose the right answer!");
		quiz.setFont(new Font("Arial", 16));
		
		final Pane spacer = new Pane();
		final Pane spacer2 = new Pane();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		HBox quizPlusHelp = new HBox(quiz, spacer2, _helpButton);
		
		submit.setText("Check Answer");
		submit.setVisible(false);
		
		back.setText("Finish Quiz");
		back.setVisible(true);
		back.setOnAction(e -> endScreen());
		
		// Generate question numbers to display
		for (int i = 1; i < 6; i ++) {
			Button btn = new Button();
			btn.setText(""+i);
			btn.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
			qNums.add(btn);
			qNumbers.getChildren().add(btn);
		}
		
		// Generate answer toggle buttons
		for (int i = 0; i < 4; i++) {
			ToggleButton btn = new ToggleButton();
			btn.setToggleGroup(ops);
			answer.add(btn);
			options.getChildren().add(btn);
		}
		
		// Add components to the view
		content.setTop(quizPlusHelp);
		content.setRight(qNumbers);
		content.setCenter(main);
		content.setBottom(check);
		
		nextQuestion();

		// GUI component configurations
		mView.setFitHeight(400);
		mView.setPreserveRatio(true);

		spacer.setMinSize(10, 1);
		HBox.setHgrow(spacer, Priority.ALWAYS);

		options.setSpacing(10);
		options.setAlignment(Pos.CENTER);

		main.getChildren().addAll(mView, options);
		main.setAlignment(Pos.CENTER);
		main.setSpacing(20);

		check.getChildren().addAll(back, spacer, submit);
		
		qNumbers.setAlignment(Pos.CENTER_LEFT);
		qNumbers.setSpacing(10);

		BorderPane.setAlignment(quiz, Pos.CENTER);
		BorderPane.setMargin(quiz, new Insets(10, 10, 10, 0));
		BorderPane.setMargin(main, new Insets(0, 20, 10, 0));
		BorderPane.setMargin(qNumbers, new Insets(0,10,10,0));
	}

}
