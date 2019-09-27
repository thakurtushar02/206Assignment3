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
	private Stage currentStage;
	private final String HOME_STYLE = "-fx-background-color: #f2eace";
	private final String VIEW_STYLE = "-fx-background-color: #f2cef2";
	private final String CREATE_STYLE = "-fx-background-color: #cef2f1";
	
	@Override
	public void start(Stage primaryStage) {
		currentStage = primaryStage;
		primaryStage.setTitle("Wiki Speak Authoring Tool");
		
		BorderPane root = new BorderPane();
		
		TabPane tabPane = new TabPane();
		
		Popup popup = new Popup();
		
		Tab homeTab = new Tab("Home");
		homeTab.setStyle(HOME_STYLE);
		home = new Home(homeTab);
		home.setContents();
		
		Tab createTab = new Tab("Create Creations");
		createTab.setStyle(CREATE_STYLE);
		create = new Create(createTab, popup);
		create.setContents(this);
		
		Tab viewTab = new Tab("View Creations");
		viewTab.setStyle(VIEW_STYLE);
		view = new View(viewTab, popup);
		view.setContents();
		
		create.setView(view);
		popup.setViewCreate(view, create);
		
		tabPane.getTabs().addAll(homeTab, viewTab, createTab);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		create.storeTabs(tabPane);
		
		root.setTop(tabPane);
		primaryStage.setMinHeight(725);
		primaryStage.setMaxHeight(725);
		
		Scene scene = new Scene(root,1200, 725);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void refreshGUI(String[] args) {
		start(currentStage);
	}
}
