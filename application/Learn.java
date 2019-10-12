package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Learn {
	
	private Tab tab;
	private Label learn = new Label();
	private Button start = new Button();
	private BorderPane content = new BorderPane();
	
	public Learn(Tab tab) {
		this.tab = tab;
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
		
//		content.getChildren().addAll(learn, start);
		content.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
		content.setPadding(new Insets(20));
		
		tab.setContent(content);
		
	}
	
	public void quizStart() {
		content.getChildren().removeAll(content.getChildren());
		
		Label quiz = new Label("What is this?");
		quiz.setFont(new Font("Arial", 16));
		
		Button q1 = new Button();
		q1.setText("1");
		q1.setPrefSize(50, 50);
		q1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		q1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Button q2 = new Button();
		q2.setText("2");
		q2.setPrefSize(50, 50);
		q2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		q2.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Button q3 = new Button();
		q3.setText("3");
		q3.setPrefSize(50, 50);
		q3.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		q3.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Button q4 = new Button();
		q4.setText("4");
		q4.setPrefSize(50, 50);
		q4.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		q4.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		Button q5 = new Button();
		q5.setText("5");
		q5.setPrefSize(50, 50);
		q5.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		q5.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		VBox qNumbers = new VBox();
		qNumbers.getChildren().addAll(q1,q2,q3,q4,q5);
		qNumbers.setAlignment(Pos.CENTER_LEFT);
		qNumbers.setSpacing(10);
		
		Region video = new Region();
		video.setPrefSize(200, 300);
		video.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
		
		ToggleButton option1 = new ToggleButton();
		option1.setText("Correct Answer");
		
		ToggleButton option2 = new ToggleButton();
		option2.setText("Wrong Answer");
		
		ToggleButton option3 = new ToggleButton();
		option3.setText("Wrong Answer");

		ToggleButton option4 = new ToggleButton();
		option4.setText("Wrong Answer");
		
		ToggleButton option5 = new ToggleButton();
		option5.setText("Wrong Answer");
		
		Button submit = new Button("Check answer");
		
		submit.setOnAction(e -> {
			// check answer
			// change text to submit
		});
		
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);

		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		HBox options = new HBox();
		options.getChildren().addAll(option1, option2,option3,option4,option5);
		options.setSpacing(10);
		options.setAlignment(Pos.CENTER);
		
		VBox main = new VBox();
		main.getChildren().addAll(video, options);
		main.setSpacing(20);
		
		HBox check = new HBox();
		check.getChildren().addAll(spacer, submit);
		
		content.setRight(qNumbers);
		content.setCenter(main);
		content.setTop(quiz);
		content.setBottom(check);
		
		BorderPane.setMargin(quiz, new Insets(10, 10, 10, 0));
		BorderPane.setMargin(main, new Insets(0, 20, 10, 0));
		BorderPane.setMargin(qNumbers, new Insets(0,10,10,0));
		
	}

}
