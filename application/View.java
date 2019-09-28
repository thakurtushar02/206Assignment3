package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class View {
	private ListView<String> creations = new ListView<>();
	private ObservableList<String> list = FXCollections.observableArrayList();
	private Label title = new Label();
	private Button delete = new Button("Delete ✘");
	private Button play = new Button("Play ►");
	private VBox sideOptions;
	private HBox optionBox;
	private VBox contents;
	private Tab _tab;
	private Popup _popup;
	public final static int ROW_HEIGHT = 24;
	public final static int SPACING = 2;

	public View(Tab tab, Popup popup) {
		_tab = tab;
		_popup = popup;
		
	}

	public void setContents() {
		delete.setMinWidth(100);
		delete.disableProperty().bind(creations.getSelectionModel().selectedItemProperty().isNull());
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String item = creations.getSelectionModel().getSelectedItem();
				_popup.showStage(item, "Are you sure you want to delete " + item + "?", "Yes", "No", true);
			}
		});

		play.setMinWidth(100);
		play.disableProperty().bind(creations.getSelectionModel().selectedItemProperty().isNull());
		play.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String item = creations.getSelectionModel().getSelectedItem();
				playCreation(item);
			}
		});

		title.setText("Creations:");
		title.setFont(new Font("Arial", 16));

		try {
			findCreations();
		} catch (IOException e) {
			System.out.println("There was an IOException thrown :(.");
			e.printStackTrace();
		}
		creations.setItems(list);
		
		creations.setPrefSize(1000, list.size()* ROW_HEIGHT + SPACING);
		creations.setMaxHeight(550);

		sideOptions = new VBox(delete, play);
		sideOptions.setPadding(new Insets(10,10,10,10));
		sideOptions.setSpacing(15);

		optionBox = new HBox(creations, sideOptions);
		HBox.setHgrow(creations, Priority.ALWAYS);
		HBox.setHgrow(sideOptions, Priority.ALWAYS);
		
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);
		VBox.setVgrow(spacer, Priority.ALWAYS);
		contents = new VBox(title, optionBox, spacer);
		contents.setPadding(new Insets(10,10,10,10));
		contents.setSpacing(10);
		_tab.setContent(contents);
		
	}

	public void findCreations() throws IOException {
		list.clear();
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ls *.mp4");
		Process process = builder.start();
		InputStream stdout = process.getInputStream();

		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		while((line = stdoutBuffered.readLine()) != null) {
			list.add(line.substring(0, line.length()-4));
		}
	}

	public void playCreation(String name) {
		VideoPlayer vid = new VideoPlayer();
		vid.playVideo(name);
	}
}
