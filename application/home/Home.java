package application.home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	
	private int _titleNumber = (int) (6*Math.random());
	public final static String[] COLOURS = {"#ff4040", "#ff92c9", "#40e0d0", "#ffff66", "#ff7373", "#b967ff"};
	public final static int SMALL_SIZE = 65;
	public final static int BIG_SIZE = 80;
	public final static String BIG = "-fx-font-size:" + BIG_SIZE + "px;";
	public final static String SMALL = "-fx-font-size:" + SMALL_SIZE + "px;";
	public final static String BASIC = "Choose mode: Basic!";
	public final static String ADVANCED = "Choose mode: Advanced";
	
	// GUI fields
	public final static Label MODE = new Label(ADVANCED);
	private Tab _tab;
	private TabPane tabPane;
	private Label titleView = new Label("Welcome to VARpedia!");
	private HBox titleBox = new HBox(titleView);
	private ImageView arrow = new ImageView();
	private ImageView arrow2 = new ImageView();
	private ImageView arrow3 = new ImageView();
	private ImageView image = new ImageView();
	private HBox modeBox = new HBox(MODE);
	private Label createHeading = new Label("New Creation!");
	private HBox createBox = new HBox(20, arrow, createHeading);
	private Label viewHeading = new Label("Past Creations");
	private HBox viewBox = new HBox(20, arrow2, viewHeading);
	private Label playHeading = new Label("Play & Learn");
	private HBox playBox = new HBox(20, arrow3, playHeading);
	private VBox text = new VBox(createBox, viewBox, playBox);
	private HBox headings = new HBox(20, text, image);
	private VBox contents = new VBox(titleBox, modeBox, headings);
	
	/**
	 * GUI related objects. In this initialiser block, these objects relating to the
	 * GUI of the Home tab in our application are set. This section is separated from the actual
	 * logic of the code.
	 */
	{
		titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:130px;"
				+ "-fx-text-fill:" + COLOURS[_titleNumber]);
		
		titleBox.setMinHeight(80);
		titleBox.setAlignment(Pos.CENTER);

		arrow.setImage(new Image(".resources/home/arrow.png"));
		arrow.setPreserveRatio(true);
		arrow.setFitWidth(SMALL_SIZE);

		arrow2.setImage(new Image(".resources/home/arrow.png"));
		arrow2.setPreserveRatio(true);
		arrow2.setFitWidth(SMALL_SIZE);

		arrow3.setImage(new Image(".resources/home/arrow.png"));
		arrow3.setPreserveRatio(true);
		arrow3.setFitWidth(SMALL_SIZE);
		
		image.setImage(new Image(".resources/home/book.png"));
		image.setPreserveRatio(true);
		image.setFitWidth(400);
		
		MODE.setStyle(SMALL);
		modeBox.setAlignment(Pos.CENTER);
		modeBox.setMinHeight(BIG_SIZE + 20);
		
		createHeading.setStyle(SMALL);
		viewHeading.setStyle(SMALL);
		playHeading.setStyle(SMALL);
		
		createBox.setMinHeight(BIG_SIZE + 20);
		viewBox.setMinHeight(BIG_SIZE + 20);
		playBox.setMinHeight(BIG_SIZE + 20);
		text.setMinWidth(viewBox.getWidth() + 650);
		
		contents.setPadding(new Insets(30,30,30,30));
		contents.setSpacing(30);
		contents.setAlignment(Pos.CENTER);
	}

	public Home(Tab tab) {
		_tab = tab;
	}

	/**
	 * Initialises the starting Home page for the GUI and instructions on how to use
	 * the application.
	 */
	public void setContents(TabPane tabPaneToSet) {
		tabPane = tabPaneToSet;
		setTitleBox();
		setModeBox();
		setCreateBox();
		setViewBox();
		setLearnBox();
		_tab.setContent(contents);
	}

	/**
	 * Sets up the HBox to go to the learn tab.
	 */
	private void setLearnBox() {
		playBox.setOnMouseEntered(arg0 -> {
			playHeading.setStyle(BIG);
			arrow3.setFitWidth(BIG_SIZE);
		});
		playBox.setOnMouseExited(arg0 -> {
			playHeading.setStyle(SMALL);
			arrow3.setFitWidth(SMALL_SIZE);
		});
		playBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(3));
	}

	/**
	 * Sets up the HBox to go to the view tab.
	 */
	private void setViewBox() {
		viewBox.setOnMouseEntered(arg0 -> {
			viewHeading.setStyle(BIG);
			arrow2.setFitWidth(BIG_SIZE);
		});
		viewBox.setOnMouseExited(arg0 -> {
			viewHeading.setStyle(SMALL);
			arrow2.setFitWidth(SMALL_SIZE);
		});
		viewBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(1));
	}

	/**
	 * Sets up the HBox to go to the create tab.
	 */
	private void setCreateBox() {
		createBox.setOnMouseEntered(arg0 -> {
			createHeading.setStyle(BIG);
			arrow.setFitWidth(BIG_SIZE);
		});
		createBox.setOnMouseExited(arg0 -> {
			createHeading.setStyle(SMALL);
			arrow.setFitWidth(SMALL_SIZE);
		});
		createBox.setOnMouseClicked(arg0 -> tabPane.getSelectionModel().select(2));
	}

	/**
	 * Sets up the HBox to go change between Basic and Advanced mode
	 */
	private void setModeBox() {
		modeBox.setOnMouseEntered(arg0 -> {
			MODE.setStyle(BIG);
		});
		modeBox.setOnMouseExited(arg0 -> {
			MODE.setStyle(SMALL);
		});
		modeBox.setOnMouseClicked(arg0 -> {
			if (MODE.getText().equals(BASIC)) {
				MODE.setText(ADVANCED);
			} else {
				MODE.setText(BASIC);
			}
		});
	}

	/**
	 * Sets up the HBox to change colour of title
	 */
	private void setTitleBox() {
		titleBox.setOnMouseEntered(arg0 -> titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:140px;"
				+ "-fx-text-fill:" + COLOURS[_titleNumber]));

		titleBox.setOnMouseExited(arg0 -> titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:130px;"
				+ "-fx-text-fill:" + COLOURS[_titleNumber]));

		titleBox.setOnMouseClicked(arg0 -> {
			_titleNumber = (_titleNumber + 1) % 6;
			titleView.setStyle("-fx-font-family:'Grinched'; -fx-font-size:140px;"
					+ "-fx-text-fill:" + COLOURS[_titleNumber]);
		});
	}
	
}
