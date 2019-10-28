package application.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import application.popup.Popup;
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

/**
 * This class is related to the View Creations tab and provides viewing,
 * playing and deleting creations functionalities for the app.
 * @author Jacinta, Lynette, Tushar
 */
public class View {
	private ListView<String> creations = new ListView<>();
	private ObservableList<String> list = FXCollections.observableArrayList();
	
	private Label title = new Label();
	private Button delete = new Button("Delete ✘");
	private Button play = new Button(" Play ►  ");
	
	private VBox sideOptions;
	private HBox optionBox;
	private VBox contents;
	
	private Tab _tab;
	private Popup _popup;
	
	public final static int ROW_HEIGHT = 50;
	public final static int SPACING = 20;

	public View(Tab tab, Popup popup) {
		_tab = tab;
		_popup = popup;
	}

	/**
	 * Sets the contents of the View Creations tab, with a list of creations and has play and
	 * delete buttons and functionalities for when a creation is selected.
	 */
	public void setContents() {
		delete.setMinWidth(100);
		delete.disableProperty().bind(creations.getSelectionModel().selectedItemProperty().isNull());
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String item = creations.getSelectionModel().getSelectedItem();
				_popup.showStage(item, "Delete " + item + "?", "✔", "✘", true);
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

		try {
			findCreations();
		} catch (IOException e) {
			System.out.println("There was an IOException thrown :(.");
			e.printStackTrace();
		}
		
		creations.setItems(list);
		creations.setPrefSize(1000, list.size() * ROW_HEIGHT + SPACING);
		creations.setMaxHeight(500);

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
		contents.setPadding(new Insets(10,30,30,30));
		contents.setSpacing(10);
		_tab.setContent(contents);
		
	}

	/**
	 * Lists all creations into the ListView.
	 * @throws IOException
	 */
	public void findCreations() throws IOException {
		list.clear();
		String cmd = "mkdir -p Creations; cd ./Creations; ls *.mp4; cd ../";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		InputStream stdout = process.getInputStream();

		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		while((line = stdoutBuffered.readLine()) != null) {
			list.add(line.substring(0, line.length()-4));
		}
	}

	/**
	 * Creates a new VideoPlayer and calls playVideo of the creation specified.
	 * @param name
	 */
	public void playCreation(String name) {
		VideoPlayer vid = new VideoPlayer();
		vid.playVideo(name);
	}
}
