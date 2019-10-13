package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javafx.application.Platform;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Represents the Create tab of the WikiSpeak application
 * 
 *@author Jacinta, Lynette, Tushar
 */
public class Create {
	private Button searchButton;
	private TextField search = new TextField();
	private Label create = new Label();
	private HBox searchBar;
	private VBox contents;
	private Label message = new Label();
	private Tab _tab;
	private String _term;
	private View _view;
	private String _name;
	private Popup _popup;
	private File _file;
	private ImageManager _imMan;
	private TabPane _tabPane;
	private Main _main;
	//private Slider slider = new Slider();
	private ObservableList<String> listLines = FXCollections.observableArrayList();
	private int numberOfAudioFiles = 0;
	private int numberOfPictures;
	private ProgressBar pbCombine = new ProgressBar();
	private ProgressBar pbSave = new ProgressBar();
	private ProgressBar pbSearch = new ProgressBar();
	private String _music;
	private final String EMPTY = "Empty";
	private final String VALID = "Valid";
	private final String DUPLICATE = "Duplicate";

	private final Questions _set;

	private final String FESTIVAL = "Human";
	private final String ESPEAK = "Robot";
	private final String NOMUSIC = "None";
	private final String CLASSICAL = "Classical";
	private final String YELLOW = "Electronic";
	private final String LIGHT = "Light";
	
	{
		pbCombine.setPrefHeight(25);
		pbCombine.setPrefWidth(700);
		pbSave.setPrefHeight(25);
		pbSave.setPrefWidth(700);
		pbSearch.setPrefHeight(25);
		pbSearch.setPrefWidth(200);
	}


	public Create(Tab tab, Popup popup, Questions set) {
		_tab = tab;
		_popup = popup;
		_imMan = new ImageManager();
		_set = set;
	}


	public void setView(View view) {
		_view = view;
	}

	/**
	 * Sets the contents of the Create tab
	 * @param main
	 */
	public void setContents(Main main) {	
		
		if (_main == null) {
			_main = main;
		}
		create.setText("Enter word: ");

		BooleanBinding searchBinding = search.textProperty().isEmpty();

		File file = new File(".resources/search/badWords.txt"); 
		try {
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String st; 
			while ((st = br.readLine()) != null) {
				searchBinding = searchBinding.or(search.textProperty().isEqualToIgnoreCase(st));
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		searchButton = new Button("Search ↳");
		searchButton.disableProperty().bind(searchBinding);

		pbSearch.setVisible(false);
		searchBar = new HBox(create, search, searchButton, pbSearch);
		searchBar.setSpacing(10);

		search.setOnKeyPressed(arg0 -> {if (arg0.getCode().equals(KeyCode.ENTER)) searchButton.fire();});

		searchButton.setOnAction(e -> searchTerm(search.getText()));

		contents = new VBox(searchBar, message);
		contents.setPadding(new Insets(15,30,30,30));
		_tab.setContent(contents);
	}

	/**
	 * Retrieves the wiki search of the supplied term and writes it to a file
	 * @param term	the search term given by user
	 */
	public void searchTerm(String term) {
		pbSearch.setVisible(true);

		// Term searched using wikit, written to a file and reformatted onto separate lines
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {
				_file = new File ("text.txt");
				ProcessBuilder builder = new ProcessBuilder("wikit", term);
				try {
					// Search and write to file
					Process process = builder.start();
					BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

					PrintWriter out = new PrintWriter(new FileWriter(_file));

					int exitStatus = process.waitFor();

					// If search process executes without problems, reformat file contents so that each sentence 
					// is on its own line
					if (exitStatus == 0) {
						String line;
						while ((line = stdout.readLine()) != null) {
							out.println(line);
						}

						out.close();

						String[] cmd = {"sed", "-i", "s/[.] /&\\n/g", _file.toString()};
						ProcessBuilder editFile = new ProcessBuilder(cmd);
						Process edit = editFile.start();

						BufferedReader stdout2 = new BufferedReader(new InputStreamReader(edit.getInputStream()));
						BufferedReader stderr2 = new BufferedReader(new InputStreamReader(edit.getErrorStream()));

						int exitStatus2 = edit.waitFor();

						if (exitStatus2 == 0) {
							String line2;
							while ((line2 = stdout2.readLine()) != null) {
								System.out.println(line2);
							}
						} else {
							String line2;
							while ((line2 = stderr2.readLine()) != null) {
								System.err.println(line2);
							}
						}

					} else {
						String line;
						while ((line = stderr.readLine()) != null) {
							System.err.println(line);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Platform.runLater(new Runnable(){
					// Progress bar is hidden and GUI is updated by reading the text file 
					@Override public void run() {
						try(BufferedReader fileReader = new BufferedReader(new FileReader(_file.toString()))){
							String line = fileReader.readLine();
							// Display contents if there are results, otherwise prompt user to search again
							if(line.contains("not found :^(")) {
								message.setText("Did you misspell? Try again!");
								pbSearch.setVisible(false);
								setContents(_main);
							} else {
								message.setText("");
								_term = term;
								deleteFiles();
								getPics(10, _term);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				return null;
			}
		};
		new Thread(task).start();
	}

	/**
	 * Displays the wiki search results into the TextArea
	 * @param reply	the search term
	 */
	public void displayLines(String reply) {
		pbSearch.setVisible(false);
		ListView<String> list = new ListView<String>(); // List displaying audio files

		list.setItems(listLines);

		HBox views= new HBox();
		TextArea textArea = new TextArea();
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

		Label info = new Label("Reorder audio below!");
		VBox text = new VBox(searchBar, textArea);
		text.setSpacing(10);

		VBox.setVgrow(textArea, Priority.ALWAYS);

		VBox listView = new VBox(info, list);

		listView.setAlignment(Pos.CENTER_LEFT);

		views.getChildren().addAll(text, listView);
		views.setSpacing(10);

		// combo box to select voice
		ObservableList<String> voices = FXCollections.observableArrayList(FESTIVAL, ESPEAK);
		final ComboBox<String> combobox = new ComboBox<String>(voices);
		combobox.setValue(FESTIVAL);


		// buttons
		Label lblVoice = new Label("Voice:  ");
		lblVoice.setFont(new Font("Arial", 20));

		ObservableList<String> music = FXCollections.observableArrayList(NOMUSIC, CLASSICAL, YELLOW, LIGHT);
		final ComboBox<String> musicComb = new ComboBox<String>(music);
		musicComb.setValue(NOMUSIC);

		Label lblMusic = new Label("Music: ");

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

		HBox lineOptions = new HBox(lblVoice, combobox, butPlay, butSave, spacer, butUp, butDown, butDelete);
		lineOptions.setSpacing(15);
		lineOptions.setAlignment(Pos.BOTTOM_CENTER);

		

		final Pane spacer2 = new Pane();
		spacer2.setMinSize(10, 1);

		HBox.setHgrow(text, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		VBox.setVgrow(textArea, Priority.ALWAYS);
    
		pbSave.setVisible(false);
		HBox nameLayout = new HBox(10, lblMusic, musicComb, pbSave, spacer2, butNext);
		nameLayout.setAlignment(Pos.BOTTOM_CENTER);

		VBox layout = new VBox(views, lineOptions, nameLayout);
		layout.setPadding(new Insets(15,30,30,30));
		layout.setSpacing(10);
		
		
		butPlay.setOnAction(e -> {
			String selectedText = textArea.getSelectedText();
			// Display pop-up if number of highlighted words > 30
			if (selectedText.split(" ").length > 30) {
				_popup.tooManyWordsHighlighted();
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
			if (Home.mode.getText().equals(Home.ADVANCED)) {
				selectedText = textArea.getSelectedText();
			} else {
				selectedText = textArea.getText();
			}
			try {
				String fileName = _file.getName();
				FileWriter fw = new FileWriter(fileName, false);
				fw.write("");
				fw.close();
				fw = new FileWriter(fileName, true);
				fw.write(selectedText);
				addCreation(combobox.getSelectionModel().getSelectedItem());
				fw.close();
			} catch (IOException ioex) {
				ioex.getMessage();
			}
		});

		butUp.setOnAction(e -> {
			int i = list.getSelectionModel().getSelectedIndex();
			if (i > 0) {
				String temp = list.getSelectionModel().getSelectedItem();
				list.getItems().remove(i);
				list.getItems().add(i-1, temp);
				list.getSelectionModel().select(i-1);
			}
		});

		butDown.setOnAction(e -> {
			int i = list.getSelectionModel().getSelectedIndex();
			if (i < list.getItems().size()-1) {
				String temp = list.getSelectionModel().getSelectedItem();
				list.getItems().remove(i);
				list.getItems().add(i+1, temp);
				list.getSelectionModel().select(i+1);
			}
		});

		butDelete.setOnAction(e -> {
			if (list.getSelectionModel().getSelectedItem() != null) {
				list.getItems().remove(list.getSelectionModel().getSelectedIndex());
			}
		});

		butNext.setOnAction(e -> {
			if (Home.mode.getText().equals(Home.ADVANCED)) {
				_music = musicComb.getSelectionModel().getSelectedItem();
			} else {
				_music = CLASSICAL;
			}
			pbCombine.setVisible(false);
			displayImages();
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
		
		if (Home.mode.getText().equals(Home.ADVANCED)) {
			butSave.disableProperty().bind(playSaveBinding);
			BooleanBinding combBinding = Bindings.size(listLines).isEqualTo(0);
			butNext.disableProperty().bind(combBinding);
			
			_tab.setContent(layout);
			_tabPane.requestLayout();
			
		} else {
			butSave.fire();
			butNext.fire();
		}

	}

	/**
	 * Displays images for user to choose
	 */
	public void displayImages() {
		ObservableList<File> oToDelete = FXCollections.observableArrayList();
		VBox chooseImages;
		Label prompt = new Label("Choose pictures you like!");
		prompt.setFont(new Font("Arial", 20));
		prompt.setPadding(new Insets(15,10,10,15));
		GridPane imgPane = new GridPane();
		TextField nameField = new TextField();
		Button btnCreate = new Button("Create ↳");
    
    String potentialName = _term;

		int count = 1;
		while (checkName(potentialName).equals(DUPLICATE)) {
			potentialName = _term + "-" + count;
			count++;
		}
		nameField.setText(potentialName);

		// Does not allow characters to be typed into text field
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			String[] badCharacters = {"/", "?", "%", "*", ":", "|", "\"", "<", ">", "\0",
					"\\", "(", ")", "$", "@", "!", "#", "^", "&", "+"};
			for (String s: badCharacters) {
				if (newValue.contains(s)) {
					nameField.setText(oldValue);
				}
			}
		});
		for(int i = 0; i < 10; i++) {
			File file = new File("." + _term + i + ".jpg");
			Image im = new Image(file.toURI().toString());
			oToDelete.add(file);
			ImageView imv = new ImageView(im);
			BorderPane bp = new BorderPane(imv);
			HBox imBox = new HBox(bp);
			imBox.setMinHeight(220);
			imBox.setMinWidth(220);
			imv.setPreserveRatio(true);
			imv.setFitHeight(200);
			imv.setFitWidth(200);
			imv.setOnMouseEntered(arg0 -> {
				imv.setFitHeight(210);
				imv.setFitWidth(210);
			});
			imv.setOnMouseExited(arg0 -> {
				imv.setFitHeight(200);
				imv.setFitWidth(200);
			});
			imv.setOnMouseClicked(arg0 -> {
				if(oToDelete.contains(file)) {
					bp.getStyleClass().add("border");
					oToDelete.remove(file);
				}else {
					bp.getStyleClass().clear();
					oToDelete.add(file);
				}
			});
			imgPane.add(imBox, i%5, i/5);
		}
		imgPane.setPadding(new Insets(10,10,10,10));
		imgPane.setHgap(10);
		imgPane.setVgap(10);
		nameField.setOnKeyPressed(arg0 -> {if (arg0.getCode().equals(KeyCode.ENTER)) btnCreate.fire();});
		BooleanBinding combBinding = Bindings.size(oToDelete).isEqualTo(10).or(nameField.textProperty().isEmpty());
		btnCreate.disableProperty().bind(combBinding);
		btnCreate.setOnAction(arg0 -> {
			numberOfPictures = 10 - oToDelete.size();
			for(File imFile: oToDelete){
				imFile.delete();
			}
			String name = nameField.getText();
			String validity = checkName(name);
			_name = name;
			if (validity.equals(EMPTY)) {
				nameField.setPromptText("Nothing entered.");
				btnCreate.requestFocus();
			} else if (validity.equals(VALID)) {
				nameField.setPromptText("");
				btnCreate.disableProperty().unbind();
				btnCreate.setDisable(true);
				nameField.setDisable(true);
				combineAudioFiles(); // Site of creation creation
			} else if (validity.equals(DUPLICATE)) {
				nameField.clear();
				nameField.setPromptText("");
				btnCreate.requestFocus();
				_popup.showStage(_name, "Name already exists. Overwrite?", "✘", "✔", false);
			}
		});
		HBox nameAndCreate = new HBox(nameField, btnCreate, pbCombine);
		nameAndCreate.setPadding(new Insets(10,10,10,10));
		nameAndCreate.setSpacing(10);
		chooseImages = new VBox(prompt, imgPane, nameAndCreate);
		chooseImages.setPadding(new Insets(15,30,30,30));
		_tab.setContent(chooseImages);
	}

	/**
	 * Checks the validity of the creation name
	 * @param reply	name supplied by user
	 * @return DUPLICATE if the creation already exists
	 * 		   EMPTY if the field is empty
	 * 		   otherwise VALID
	 */
	public String checkName(String reply) {
		File file = new File("./Creations/" + reply + ".mp4");
		if(file.exists()) {
			return DUPLICATE;
		} else if (reply.isEmpty()) {
			return EMPTY;
		} else {
			return VALID;
		}	
	}

	/**
	 * Creates and saves an audiofile into the ListvIew
	 * @param voice	voice selected by user
	 */
	public void addCreation(String voice) {
		if (Home.mode.getText().equals(Home.ADVANCED)) {
			pbSave.setVisible(true);
		}
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {

				String cmd = "mkdir -p AudioFiles";
				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);

				try {
					Process process = builder.start();
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				numberOfAudioFiles++;
				String nameOfFile = "AudioFile" + numberOfAudioFiles;

				if (voice.equals(FESTIVAL)) {
					cmd = "cat " + _file.toString() + " | text2wave -o \"./AudioFiles/" + nameOfFile + ".wav\"";
				} else {
					cmd = "espeak -f " + _file.toString() + " --stdout > \"./AudioFiles/" + nameOfFile + ".wav\"";
				}
				builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process process = builder.start();
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Platform.runLater(new Runnable(){
					@Override public void run() {
						_view.setContents();
						listLines.add(nameOfFile);
						pbSave.setVisible(false);
					}
				});
				return null;
			}
		};
		new Thread(task).start();
	}

	/**
	 * Deletes a specified file
	 * @param name name of the file to be deleted
	 */
	public void removeCreation(String name) {
		File file = new File("./Creations/" + name);
		file.delete();
		File file1 = new File("./Quizzes/" + name);
		file1.delete();
	}

	public void storeTabs(TabPane tabPane) {
		_tabPane = tabPane;
	}

	/**
	 * Creates creation by combining audio files, downloading images, creating video, and merging them all into 
	 * one mp4 video file
	 */
	public void combineAudioFiles() {
		pbCombine.setVisible(true);
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				String cmd;

				// Combine list of audio files into in one if there are multiple, otherwise rename the one audio file
				if (listLines.size() == 1) {
					cmd = "mv ./AudioFiles/AudioFile1.wav ./AudioFiles/audio.wav";
				} else {
					cmd = "ffmpeg";
					for (String s: listLines) {
						cmd += " -i \"./AudioFiles/" + s + ".wav\"";
					}

					cmd += " -filter_complex \"";
					for (int i = 0; i < listLines.size(); i++) {
						cmd += "[" + i + ":0]";
					}
					cmd += "concat=n=" + listLines.size() + ":v=0:a=1[out]\" -map \"[out]\" ./AudioFiles/" + "audio" + ".wav &>/dev/null";
				}

				if(_music == NOMUSIC) {
					cmd += "; mv ./AudioFiles/audio.wav ./AudioFiles/temp.wav";
				}else {
					cmd += "; ffmpeg -i ./AudioFiles/audio.wav -i ./Music/" + _music + ".mp3 -filter_complex amix=inputs=2:duration=shortest ./AudioFiles/temp.wav";
				}

				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process process = builder.start();
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				File file = new File("./AudioFiles/temp.wav");
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
				AudioFormat format = audioInputStream.getFormat();
				long audioFileLength = file.length();
				int frameSize = format.getFrameSize();
				float frameRate = format.getFrameRate();
				float durationInSeconds = (audioFileLength / (frameSize * frameRate));

				cmd = "rm -f out.mp4; mkdir -p Quizzes ; cat \"." + _term + "\"?.jpg | ffmpeg -f image2pipe -framerate $((" + numberOfPictures + "))/"
						+ durationInSeconds + " -i - -c:v libx264 -pix_fmt yuv420p -vf \""
						+ "scale=w=1280:h=720:force_original_aspect_ratio=1,pad=1280:720:(ow-iw)/2:(oh-ih)/2\""
						+ " -r 25 -y \'./Quizzes/" + _term + ".mp4\' ; rm \"" + _term + "\"?.jpg ; ffmpeg -i "
								+ "\'./Quizzes/" + _term + ".mp4\' -vf "
						+ "\"drawtext=fontsize=50:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)"
						+ "/2:borderw=5:text=\'" + _term + "\'\" out.mp4 ; ffmpeg -i out.mp4 -i"
						+ " \'./AudioFiles/" + "temp" + ".wav\' -c:v copy -c:a aac -strict experimental"
						+ " -y \'./Creations/" + _name + ".mp4\' &>/dev/null ; rm out.mp4";
				


				ProcessBuilder builderr = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process vidProcess = builderr.start();
					vidProcess.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				//create question 
				
				Question question = new Question(new File("Quizzes/"+_name+".mp4"), _term);
				_set.addQuestion(question);
				
				
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						_view.setContents();
						_main.refreshGUI(null);
						_popup.showFeedback(_name, false);
						pbCombine.setVisible(false);

					}
				});
				deleteFiles();

				return null;
			}
		};
		new Thread(task).start();
	}

	/**
	 * Deletes supporting files from working directory
	 */
	public void deleteFiles() {
		listLines = FXCollections.observableArrayList();
		numberOfAudioFiles = 0;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				String cmd = "if [ -d AudioFiles ]; then rm -r AudioFiles; fi; rm -f .*.jpg; ";
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
	}

	/**
	 * Downloads a number of images (of the search term) from Flickr
	 * @param input	the number of images
	 * @param reply	the search term
	 */
	public void getPics(int input, String reply) {
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				_imMan.getImages(reply);
				
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						displayLines(_term);
					}
				});
				return null;
			}
		};

		new Thread(task).start();
	}

}


