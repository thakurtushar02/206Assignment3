package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class is related to the Home tab. It is where the user starts once
 * the application begins running, and after a creation is created.
 * @author Jacinta, Lynette, Tushar
 *
 */
public class Home {
	private Tab _tab;
	private int _titleNumber = (int) (6*Math.random());
	private String[] colour = {"#ff4040", "#ff92c9", "#40e0d0", "#ffff66", "#ff7373", "#b967ff"};
	private final static int SMALL_SIZE = 65;
	private final static int BIG_SIZE = 80;
	private final static String BIG = "-fx-font-size:" + BIG_SIZE + "px;";
	private final static String SMALL = "-fx-font-size:" + SMALL_SIZE + "px;";
	protected final static String BASIC = "Choose mode: Basic!";
	protected final static String ADVANCED = "Choose mode: Advanced";
	protected final static Label mode = new Label(BASIC);

	public Home(Tab tab) {
		_tab = tab;
	}

	/**
	 * Initialises the starting Home page for the GUI and instructions on how to use
	 * the application.
	 */
	public void setContents(TabPane tabPane) {

		Label titleView = new Label("Welcome to VARpedia!");
		titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:130px;"
				+ "-fx-text-fill:" + colour[_titleNumber]);

		HBox titleBox = new HBox(titleView);
		titleBox.setMinHeight(80);
		titleBox.setOnMouseEntered(arg0 -> titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:140px;"
				+ "-fx-text-fill:" + colour[_titleNumber]));

		titleBox.setOnMouseExited(arg0 -> titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:130px;"
				+ "-fx-text-fill:" + colour[_titleNumber]));

		titleBox.setOnMouseClicked(arg0 -> {
			_titleNumber = (_titleNumber + 1) % 6;
			titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:140px;"
					+ "-fx-text-fill:" + colour[_titleNumber]);
		});
		titleBox.setAlignment(Pos.CENTER);

		ImageView arrow = new ImageView();
		arrow.setImage(new Image(".resources/home/arrow.png"));
		arrow.setPreserveRatio(true);
		arrow.setFitWidth(SMALL_SIZE);

		ImageView arrow2 = new ImageView();
		arrow2.setImage(new Image(".resources/home/arrow.png"));
		arrow2.setPreserveRatio(true);
		arrow2.setFitWidth(SMALL_SIZE);

		ImageView arrow3 = new ImageView();
		arrow3.setImage(new Image(".resources/home/arrow.png"));
		arrow3.setPreserveRatio(true);
		arrow3.setFitWidth(SMALL_SIZE);

		ImageView image = new ImageView();
		image.setImage(new Image(".resources/home/book.png"));
		image.setPreserveRatio(true);
		image.setFitWidth(500);

		mode.setStyle(SMALL);

		HBox modeBox = new HBox(mode);
		modeBox.setAlignment(Pos.CENTER);
		modeBox.setMinHeight(BIG_SIZE + 20);
		modeBox.setOnMouseEntered(arg0 -> {
			mode.setStyle(BIG);
		});
		modeBox.setOnMouseExited(arg0 -> {
			mode.setStyle(SMALL);
		});
		modeBox.setOnMouseClicked(arg0 -> {
			if (mode.getText().equals(BASIC)) {
				mode.setText(ADVANCED);
			} else {
				mode.setText(BASIC);
			}
		});

		Label createHeading = new Label("Create new Creation!");
		createHeading.setStyle(SMALL);
		HBox createBox = new HBox(20, arrow, createHeading);
		createBox.setMinHeight(BIG_SIZE + 20);
		createBox.setOnMouseEntered(arg0 -> {
			createHeading.setStyle(BIG);
			arrow.setFitWidth(BIG_SIZE);
		});
		createBox.setOnMouseExited(arg0 -> {
			createHeading.setStyle(SMALL);
			arrow.setFitWidth(SMALL_SIZE);
		});
		createBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(2));


		Label viewHeading = new Label("See Past Creations");
		viewHeading.setStyle(SMALL);
		HBox viewBox = new HBox(20, arrow2, viewHeading);
		viewBox.setMinHeight(BIG_SIZE + 20);
		viewBox.setOnMouseEntered(arg0 -> {
			viewHeading.setStyle(BIG);
			arrow2.setFitWidth(BIG_SIZE);
		});
		viewBox.setOnMouseExited(arg0 -> {
			viewHeading.setStyle(SMALL);
			arrow2.setFitWidth(SMALL_SIZE);
		});
		viewBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(1));


		Label playHeading = new Label("Play & Learn");
		playHeading.setStyle(SMALL);
		HBox playBox = new HBox(20, arrow3, playHeading);
		playBox.setMinHeight(BIG_SIZE + 20);
		playBox.setOnMouseEntered(arg0 -> {
			playHeading.setStyle(BIG);
			arrow3.setFitWidth(BIG_SIZE);
		});
		playBox.setOnMouseExited(arg0 -> {
			playHeading.setStyle(SMALL);
			arrow3.setFitWidth(SMALL_SIZE);
		});
		playBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(3));


		VBox text = new VBox(createBox, viewBox, playBox);
		text.setMinWidth(viewBox.getWidth() + 650);
		HBox headings = new HBox(20, text, image);
		VBox contents = new VBox(titleBox, modeBox, headings);
		contents.setPadding(new Insets(30,30,30,30));
		contents.setSpacing(30);
		contents.setAlignment(Pos.CENTER);
		_tab.setContent(contents);
	}
}
