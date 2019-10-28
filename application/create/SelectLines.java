package application.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import application.home.Home;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import application.popup.Popup;
/**
 * This class sets the contents of the Create tab to continue after searching a term
 * @author Jacinta, Lynette, Tushar
 *
 */
public class SelectLines {
	private final String FESTIVAL = "Human";
	private final String ESPEAK = "Robot";
	private final String NOMUSIC = "None";
	private final String CLASSICAL = "Classical";
	private final String YELLOW = "Electronic";
	private final String LIGHT = "Light";
	private File _file;
	/**
	 * User selected background music
	 */
	private String _music;
	/**
	 * List view to display audio files
	 */
	private ListView<String> list = new ListView<String>();
	private TextArea textArea;
	private Create _create;

	/**
	 * Sets GUI components (TextArea, ListView of audio files, controls) to Create tab
	 * @param tab
	 * @param tabPane
	 * @param create
	 * @param pbCombine
	 * @param pbSave
	 * @param listLines
	 * @param searchBar
	 */
	public void setScreen(Tab tab, TabPane tabPane, Create create, ProgressIndicator pbCombine, ProgressIndicator pbSave, ObservableList<String> listLines, HBox searchBar) {
		_create = create;
		_file = new File ("text.txt");
		list.setItems(listLines);
		textArea = getText();
		
		VBox text = new VBox(searchBar, textArea);
		text.setSpacing(10);
		VBox.setVgrow(textArea, Priority.ALWAYS);

		Label info = new Label("Reorder audio below!");
		VBox listView = new VBox(info, list);
		list.setPrefHeight(490);
		listView.setAlignment(Pos.CENTER_LEFT);

		HBox views= new HBox();
		views.getChildren().addAll(text, listView);
		views.setPrefHeight(550);
		views.setSpacing(10);

		// combo box to select music
		ObservableList<String> music = FXCollections.observableArrayList(NOMUSIC, CLASSICAL, YELLOW, LIGHT);
		final ComboBox<String> musicComb = new ComboBox<String>(music);
		musicComb.setValue(NOMUSIC);

		Label lblMusic = new Label("Music: ");

		// combo box to select voice
		ObservableList<String> voices = FXCollections.observableArrayList(FESTIVAL, ESPEAK);
		final ComboBox<String> combobox = new ComboBox<String>(voices);
		combobox.setValue(FESTIVAL);

		// buttons
		Label lblVoice = new Label("Voice:  ");
		lblVoice.setFont(new Font("Arial", 20));

		Button butPlay = new Button(" Play ►");
		BooleanBinding playSaveBinding = textArea.selectedTextProperty().isEmpty();
		butPlay.disableProperty().bind(playSaveBinding);
		combobox.prefHeightProperty().bind(butPlay.prefHeightProperty());

		Button butSave = new Button(" Save ✔");

		Button butUp = new Button("Move ↑");
		BooleanBinding upDownBinding = Bindings.size(listLines).lessThan(2).or(list.getSelectionModel().selectedItemProperty().isNull());
		butUp.disableProperty().bind(upDownBinding);
		Button butDown = new Button("Move ↓");
		butDown.disableProperty().bind(upDownBinding);

		Button butDelete = new Button("Delete ✘");
		butDelete.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

		Button butNext = new Button("Next ↳");
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);

		HBox lineOptions = new HBox(lblVoice, combobox, butPlay, butSave, pbSave, spacer, butUp, butDown, butDelete);
		lineOptions.setSpacing(15);
		lineOptions.setAlignment(Pos.BOTTOM_CENTER);

		final Pane spacer2 = new Pane();
		spacer2.setMinSize(1, 1);

		HBox.setHgrow(text, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		VBox.setVgrow(textArea, Priority.ALWAYS);

		pbSave.setVisible(false);
		HBox nameLayout = new HBox(10, lblMusic, musicComb, spacer2, butNext);
		nameLayout.setAlignment(Pos.BOTTOM_CENTER);

		VBox layout = new VBox(views, lineOptions, nameLayout);
		layout.setPadding(new Insets(15,30,30,30));
		layout.setSpacing(10);

		playSave(butPlay, butSave, combobox);

		customiseAudio(butUp, butDown, butDelete);

		//Basic mode has music set classic 
		butNext.setOnAction(e -> {
			if (Home.MODE.getText().equals(Home.ADVANCED)) {
				_music = musicComb.getSelectionModel().getSelectedItem();
				create.setMusic(_music);
			} else {
				_music = CLASSICAL;
				create.setMusic(_music);
			}
			pbCombine.setVisible(false);
			create.displayImages();
		});

		// Basic mode automatically creates and audio file and continues to the next step
		if (Home.MODE.getText().equals(Home.ADVANCED)) {
			butSave.disableProperty().bind(playSaveBinding);
			BooleanBinding combBinding = Bindings.size(listLines).isEqualTo(0);
			butNext.disableProperty().bind(combBinding);

			tab.setContent(layout);
			tabPane.requestLayout();

		} else {
			butSave.fire();
			butNext.fire();
		}
	}

	/**
	 * Create text area where the lines from the search are displayed
	 * @return textArea - where the lines are displayed
	 */
	public TextArea getText() {
		TextArea textArea= new TextArea();
		textArea.setEditable(true);
		textArea.setWrapText(true);

		// Populate TextArea with text file contents
		BufferedReader fileContent;
		try {
			fileContent = new BufferedReader(new FileReader(_file));
			String line;
			int numberOfLines = 0;
			while (((line = fileContent.readLine()) != null) && (numberOfLines < 5)) {
				textArea.appendText(line + "\n\n");
				numberOfLines++;
			}
			fileContent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		textArea.setText(textArea.getText().substring(2,textArea.getText().length()-3));
		textArea.setStyle("-fx-font-size:30");
		return textArea;
	}

	/**
	 * Provides functionality to customise audio files
	 * @param butUp
	 * @param butDown
	 * @param butDelete
	 */
	public void customiseAudio(Button butUp, Button butDown, Button butDelete) {
		//Move audio files up listview
		butUp.setOnAction(e -> {
			int i = list.getSelectionModel().getSelectedIndex();
			if (i > 0) {
				String temp = list.getSelectionModel().getSelectedItem();
				list.getItems().remove(i);
				list.getItems().add(i-1, temp);
				list.getSelectionModel().select(i-1);
			}
		});

		//Move audio files down listview
		butDown.setOnAction(e -> {
			int i = list.getSelectionModel().getSelectedIndex();
			if (i < list.getItems().size()-1) {
				String temp = list.getSelectionModel().getSelectedItem();
				list.getItems().remove(i);
				list.getItems().add(i+1, temp);
				list.getSelectionModel().select(i+1);
			}
		});

		//Delete created audio file
		butDelete.setOnAction(e -> {
			if (list.getSelectionModel().getSelectedItem() != null) {
				list.getItems().remove(list.getSelectionModel().getSelectedIndex());
			}
		});

		// Plays the selected audio file on double-click
		list.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					String audio = list.getSelectionModel().getSelectedItem();
					String cmd = "aplay AudioFiles/" + audio +".wav";
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
					try {
						pb.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Provides functionality to play and save chunks of text in different voices
	 * @param butPlay
	 * @param butSave
	 * @param combobox
	 */
	public void playSave(Button butPlay, Button butSave, ComboBox<String> combobox) {
		butPlay.setOnAction(e -> {
			String selectedText = textArea.getSelectedText();
			// Display pop-up if number of highlighted words > 30
			if (selectedText.split(" ").length > 30) {
				Popup popup = new Popup();
				popup.tooManyWordsHighlighted();
			} else {
				Task<Void> task = new Task<Void>() {
					// Preview text according to user's selected voice
					@Override
					protected Void call() throws Exception {				
						String voice;
						String selection = combobox.getSelectionModel().getSelectedItem();
						if ( selection.equals(FESTIVAL)) {
							voice = "festival --tts";
						} else {
							voice = "espeak";
						}
						String command = "echo \"" + textArea.getSelectedText() + " \" | " + voice ;
						ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
						try {
							Process p = pb.start();
							BufferedReader stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
							int exitStatus = p.waitFor();
							if (exitStatus != 0) {
								String line2;
								while ((line2 = stderr.readLine()) != null) {
									System.err.println(line2);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
				};
				new Thread(task).start();
			}
		});

		// Save selected text as an audio file  
		butSave.setOnAction(e -> {
			String selectedText;
			if (Home.MODE.getText().equals(Home.ADVANCED)) {
				selectedText = textArea.getSelectedText();
			} else {
				selectedText = textArea.getText();
			}
			try {
				String fileName = _file.getName();
				FileWriter fw = new FileWriter(fileName, false);
				fw.write(""); //Overwrite the contents of the file
				fw.close();
				fw = new FileWriter(fileName, true);
				fw.write(selectedText); //Write the selected text into the file
				_create.addCreation(combobox.getSelectionModel().getSelectedItem());
				fw.close();
			} catch (IOException ioex) {
				ioex.getMessage();
			}
		});
	}


}
