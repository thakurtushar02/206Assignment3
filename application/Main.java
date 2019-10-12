package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class containing the main method used to run the entire application.
 * @author Jacinta, Lynette, Tushar
 *
 */
public class Main extends Application {
	private Home home;
	private View view;
	private Create create;
	private Learn learn;
	private Stage currentStage;
	
	/**
	 * Creates the three tabs, Home, View, and Create Creation.
	 */
	@Override
	public void start(Stage primaryStage) {
		currentStage = primaryStage;
		primaryStage.setTitle("VARpedia");
		
		BorderPane root = new BorderPane();
		
		TabPane tabPane = new TabPane();
		
		Popup popup = new Popup();
		
		Tab homeTab = new Tab("HOME");
		homeTab.getStyleClass().add("home_style");
		home = new Home(homeTab);
		home.setContents(tabPane);
		
		Tab createTab = new Tab("CREATE");
		createTab.getStyleClass().add("create_style");
		create = new Create(createTab, popup);
		create.setContents(this);
		
		Tab viewTab = new Tab("VIEW");
		viewTab.getStyleClass().add("view_style");
		view = new View(viewTab, popup);
		view.setContents();
		
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				view.setContents();
			}
		}); 
		
		create.setView(view);
		popup.setViewCreate(view, create);
		
		Tab learnTab = new Tab("REVIEW");
		learn = new Learn(learnTab);
		learn.setContents();
		
		tabPane.getTabs().addAll(homeTab, viewTab, createTab, learnTab);
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		create.storeTabs(tabPane);
		
		root.setTop(tabPane);
		primaryStage.setResizable(false);
		
		Scene scene = new Scene(root, 1200, 900);

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(arg0 -> {
			create.deleteFiles();
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					
					String cmd = "rm -f text.txt";
					ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
					try {
						Process process = builder.start();
						process.waitFor();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}

			};
			new Thread(task).start();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void refreshGUI(String[] args) {
		start(currentStage);
	}
}
