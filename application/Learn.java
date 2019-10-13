package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Learn {
	
	private Tab tab;
	private Label learn = new Label();
	private Button start = new Button();
	private BorderPane content = new BorderPane();
	private final double BUTTON_WIDTH = 75;
	private final double BUTTON_HEIGHT = 75;
	private List<ToggleButton> answer = new ArrayList<ToggleButton>();
	private List<Button> qNums = new ArrayList<Button>();
	private ToggleGroup ops = new ToggleGroup();
	private int current;
	private Question question;
	private Questions qSet = new Questions();
	private Label end = new Label();
	private int correct;
	private Button playAgain = new Button();
	private MediaView mView;
	private Media media;
	private MediaPlayer player;
	
	public Learn(Tab tab, Questions set) {
		this.tab = tab;
		qSet = set;
	}
	
	public void setContents() {
		learn.setText("Time to review what you have learned!");
		learn.setFont(new Font("Arial", 16));
		learn.setPadding(new Insets(20));
		
		start.setText("Start Quiz");
		start.setLayoutX(100);
		start.setLayoutY(100);
		
		start.setOnAction(e -> quizStart());
		
		content.setTop(learn);
		learn.setAlignment(Pos.CENTER);
		content.setCenter(start);
		content.setPadding(new Insets(20));
		
		tab.setContent(content);
		
	}
	
	public void quizStart() {
		qNums.clear();
		answer.clear();
		
		content.getChildren().removeAll(content.getChildren());
		question = qSet.getQuestion();
		
		HBox options = new HBox();
		VBox qNumbers = new VBox();
		
		correct = 0;
		current = 1;
		
		Label quiz = new Label("What is this?");
		quiz.setFont(new Font("Arial", 16));
		
		for (int i = 1; i < 6; i ++) {
			Button btn = new Button();
			btn.setText(""+i);
			btn.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
			btn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
			btn.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
			qNums.add(btn);
			qNumbers.getChildren().add(btn);
		}
		
		qNumbers.setAlignment(Pos.CENTER_LEFT);
		qNumbers.setSpacing(10);
		
		
		for (int i = 0; i < 4; i++) {
			ToggleButton btn = new ToggleButton();
			btn.setToggleGroup(ops);
			answer.add(btn);
			options.getChildren().add(btn);
		}
		
		setQuestion(question);
		
		mView.setFitHeight(400);
		mView.setPreserveRatio(true);
		
		Button submit = new Button("Check Answer");
		submit.setVisible(false);
		
		// Button only appears if an answer is selected
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
				if (isCorrect(question)) {
					btn.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
					btn.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
					correct++;
				} else {
					btn.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
					btn.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
				}
				
				if (current < 5) {
					submit.setText("Next");
				} else if (current == 5) {
					submit.setText("Finish");
				}
			} else if (submit.getText() == "Next") {
				nextQuestion();
				current++;
				submit.setText("Check Answer");
			} else if (submit.getText() == "Finish") {
				endScreen();
			}
			
		});
		
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);

		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		options.setSpacing(10);
		options.setAlignment(Pos.CENTER);
		
		VBox main = new VBox();
		main.getChildren().addAll(mView, options);
		main.setSpacing(20);
		
		HBox check = new HBox();
		check.getChildren().addAll(spacer, submit);
		
		content.setTop(quiz);
		content.setRight(qNumbers);
		content.setCenter(main);
		content.setBottom(check);
		
		BorderPane.setMargin(quiz, new Insets(10, 10, 10, 0));
		BorderPane.setMargin(main, new Insets(0, 20, 10, 0));
		BorderPane.setMargin(qNumbers, new Insets(0,10,10,0));
		
	}
	
	public void setQuestion(Question question) {
		
		Collections.shuffle(answer);
		for (int i = 0; i < 4; i++) {
			answer.get(i).setText(question.getAnswers().get(i));
			answer.get(i).setSelected(false);
		}
		
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
	
	public boolean isCorrect(Question question) {
		ToggleButton selected = (ToggleButton) ops.getSelectedToggle();
		if (selected.getText() == question.getCorrectAnswer()) {
			return true;
		}
		return false;
	}
	
	public void nextQuestion() {
		question = qSet.getQuestion();
		setQuestion(question);
	}
	
	public void endScreen() {
		content.getChildren().removeAll(content.getChildren());
		end.setText("You got "+correct+"/5!");
		
		playAgain.setText("Play Again!");
		playAgain.setOnAction(e -> quizStart());
		
		content.setTop(end);
		content.setCenter(playAgain);
		
	}
	
}
