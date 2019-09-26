package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private Home home;
	private View view;
	private Create create;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Wiki Speak");

		BorderPane root = new BorderPane();

		TabPane tabPane = new TabPane();

		Popup popup = new Popup();

		Tab homeTab = new Tab("Home");
		home = new Home(homeTab);
		home.setContents();

		Tab createTab = new Tab("Create Creations");
		create = new Create(createTab, popup);
		create.setContents();

		Tab viewTab = new Tab("View Creations");
		view = new View(viewTab, popup);
		view.setContents();

		create.setView(view);
		popup.setViewCreate(view, create);

		tabPane.getTabs().addAll(homeTab, viewTab, createTab);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		root.setTop(tabPane);

		Scene scene = new Scene(root,800,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
