package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	private Tab _tab;
	private int _titleNumber = 0;
	private final int HEADING_SIZE = 1100;
	private final int TITLE_SIZE = 800;
	
	public Home(Tab tab) {
		_tab = tab;
	}
	
	/**
	 * Initialises the starting Home page for the GUI and instructions on how to use
	 * the application.
	 */
	public void setContents(TabPane tabPane) {
		ImageView titleView = new ImageView();
		titleView.setImage(new Image(".resources/home/title" + _titleNumber + ".png"));
		titleView.setPreserveRatio(true);
		titleView.setFitWidth(TITLE_SIZE);
		
		HBox titleBox = new HBox(titleView);
		titleBox.setOnMouseEntered(arg0 -> {
			titleView.setFitWidth(TITLE_SIZE + 100);
		});
		titleBox.setOnMouseExited(arg0 -> {
			titleView.setFitWidth(TITLE_SIZE);
		});
		titleBox.setOnMouseClicked(arg0 -> {
			_titleNumber = (_titleNumber + 1) % 3;
			titleView.setImage(new Image(".resources/home/title" + _titleNumber + ".png"));
		});
		titleBox.setAlignment(Pos.CENTER);
		
		ImageView infoView = new ImageView();
		infoView.setImage(new Image(".resources/home/heading.png"));
		infoView.setPreserveRatio(true);
		infoView.setFitWidth(HEADING_SIZE);
		
		ImageView createHeading = new ImageView();
		createHeading.setImage(new Image(".resources/home/heading2.png"));
		createHeading.setPreserveRatio(true);
		createHeading.setFitWidth(HEADING_SIZE);
		
		HBox createBox = new HBox(createHeading);
		createBox.setOnMouseEntered(arg0 -> {
			createHeading.setFitWidth(HEADING_SIZE + 50);
		});
		createBox.setOnMouseExited(arg0 -> {
			createHeading.setFitWidth(HEADING_SIZE);
		});
		createBox.setOnMouseClicked(arg0 -> {
			tabPane.getSelectionModel().select(2);
		});
		createBox.setAlignment(Pos.CENTER);
		
		ImageView viewHeading = new ImageView();
		viewHeading.setImage(new Image(".resources/home/heading3.png"));
		viewHeading.setPreserveRatio(true);
		viewHeading.setFitWidth(HEADING_SIZE - 450);
		
		HBox viewBox = new HBox(viewHeading);
		viewBox.setOnMouseEntered(arg0 -> {
			viewHeading.setFitWidth(HEADING_SIZE - 400);
		});
		viewBox.setOnMouseExited(arg0 -> {
			viewHeading.setFitWidth(HEADING_SIZE - 450);
		});
		viewBox.setOnMouseClicked(arg0 -> {
			tabPane.getSelectionModel().select(1);
		});
		viewBox.setAlignment(Pos.CENTER);
	
		VBox contents = new VBox(titleBox, infoView, createBox, viewBox);
		contents.setPadding(new Insets(30,30,30,30));
		contents.setSpacing(30);
		contents.setAlignment(Pos.CENTER);
		_tab.setContent(contents);
	}
}
