package application.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import application.home.Home;
import application.learn.Question;
import application.learn.Questions;
import application.main.Main;
import application.popup.Popup;
import application.view.View;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Represents the Create tab of the WikiSpeak application
 * 
 *@author Jacinta, Lynette, Tushar
 */
public class Create {
	private Button searchButton;
	private BooleanBinding searchBinding;
	private Button helpButton;
	private TextField search = new TextField();
	private Label create = new Label();
	private HBox searchBar;
	private VBox contents;
	private Label message = new Label();
	private Main _main;
	private TabPane _tabPane;
	private Tab _tab;
	private View _view;
	private Popup _popup;
	private ImageManager _imMan;
	private HBox _searchBar;
	private VBox _contents;
	private Button _searchButton;
	private TextField _search = new TextField();
	private Label _message = new Label();
	private Label _create = new Label();
	private ProgressBar _pbCombine = new ProgressBar();
	private ProgressBar _pbSave = new ProgressBar();
	private ProgressBar _pbSearch = new ProgressBar();
	private File _file;
	private int _numberOfAudioFiles = 0;
	private int _numberOfPictures;
	private ObservableList<String> _listLines = FXCollections.observableArrayList();
	private String _term;
	private String _name;
	private String _music;

	private final Questions _set;

	private final String EMPTY = "Empty";
	private final String VALID = "Valid";
	private final String DUPLICATE = "Duplicate";
	private final String FESTIVAL = "Human";
	

	{
		pbCombine.setPrefHeight(25);
		pbCombine.setPrefWidth(700);
		pbSave.setPrefHeight(25);
		pbSave.setPrefWidth(700);
		pbSearch.setPrefHeight(25);
		pbSearch.setPrefWidth(125);
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
		_create.setText("Enter word: ");

		searchBinding = search.textProperty().isEmpty();

		File file = new File(".resources/search/badWords.txt"); 
		try {
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String st; 
			while ((st = br.readLine()) != null) {
				searchBinding = searchBinding.or(_search.textProperty().isEqualToIgnoreCase(st));
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		searchButton = new Button("Search ↳");
		searchButton.disableProperty().bind(searchBinding);
		
		helpButton = new Button("?");
		helpButton.setVisible(false);

		pbSearch.setVisible(false);
		searchBar = new HBox(create, search, searchButton, pbSearch, helpButton);
		searchBar.setSpacing(10);

		_search.setOnKeyPressed(arg0 -> {if (arg0.getCode().equals(KeyCode.ENTER)) _searchButton.fire();});

		searchButton.setOnAction(e -> {
			searchButton.disableProperty().unbind();
			searchButton.setDisable(true);
			searchTerm(search.getText());
		});

		_contents = new VBox(_searchBar, _message);
		_contents.setPadding(new Insets(15,30,30,30));
		_tab.setContent(_contents);
	}

	/**
	 * Retrieves the wiki search of the supplied term and writes it to a file
	 * @param term	the search term given by user
	 */
	public void searchTerm(String term) {
		_pbSearch.setVisible(true);

		// Term searched using wikit, written to a file and reformatted onto separate lines
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {
				_file = new File ("text.txt");
				SearchManager searchMan = new SearchManager();
				searchMan.searchTerm(_file, term);

				Platform.runLater(new Runnable(){
					// Progress bar is hidden and GUI is updated by reading the text file 
					@Override public void run() {
						try(BufferedReader fileReader = new BufferedReader(new FileReader(_file.toString()))){
							String line = fileReader.readLine();
							// Display contents if there are results, otherwise prompt user to search again
							if(line.contains("not found :^(")) {
								_message.setText("Did you misspell? Try again!");
								_pbSearch.setVisible(false);
								setContents(_main);
							} else {
								_message.setText("");
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
		_pbSearch.setVisible(false);
		SelectLines selLines = new SelectLines();
		selLines.setScreen(_tab, _tabPane, this, _pbCombine, _pbSave, _listLines, _searchBar);
	}

	/**
	 * Displays images for user to choose
	 */
	public void displayImages() {
		VBox chooseImages;
		String potentialName = _term;
		GridPane imgPane = _imMan.getImagePane(_term);
		
		Label prompt = new Label("Choose pictures you like!");
		prompt.setFont(new Font("Arial", 20));
		prompt.setPadding(new Insets(15,10,10,15));

		TextField nameField = new TextField();
		Button btnCreate = new Button("Create ↳");
		int count = 1;
		while (checkName(potentialName).equals(DUPLICATE)) {
			potentialName = _term + "-" + count;
			count++;
		}
		nameField.setText(potentialName);
		// Does not allow characters to be typed into text field
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			String[] badCharacters = {"/", "?", "%", "*", ":", "|", "\"", "<", ">", "\0",
					"\\", "(", ")", "$", "@", "!", "#", "^", "&", "+", "="};
			for (String s: badCharacters) {
				if (newValue.contains(s)) {
					nameField.setText(oldValue);
				}
			}
		});
		HBox nameAndCreate = new HBox(nameField, btnCreate, _pbCombine);
		nameAndCreate.setPadding(new Insets(10,10,10,10));
		nameAndCreate.setSpacing(10);
		chooseImages = new VBox(prompt, imgPane, nameAndCreate);
		chooseImages.setPadding(new Insets(15,30,30,30));
		
		ObservableList<File> oToDelete = _imMan.getToDelete();
		nameField.setOnKeyPressed(arg0 -> {if (arg0.getCode().equals(KeyCode.ENTER)) btnCreate.fire();});
		BooleanBinding combBinding = Bindings.size(oToDelete).isEqualTo(10).or(nameField.textProperty().isEmpty());
		btnCreate.disableProperty().bind(combBinding);
		btnCreate.setOnAction(arg0 -> {
			_numberOfPictures = 10 - oToDelete.size();
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
		if (Home.MODE.getText().equals(Home.ADVANCED)) {
			_pbSave.setVisible(true);
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
				_numberOfAudioFiles++;
				String nameOfFile = "AudioFile" + _numberOfAudioFiles;

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
						_listLines.add(nameOfFile);
						_pbSave.setVisible(false);
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
	}

	public void storeTabs(TabPane tabPane) {
		_tabPane = tabPane;
	}
	
	public void setMusic(String music) {
		_music = music;
	}

	/**
	 * Creates creation by combining audio files, downloading images, creating video, and merging them all into 
	 * one mp4 video file
	 */
	public void combineAudioFiles() {
		_pbCombine.setVisible(true);
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				AudioManager audMan = new AudioManager();
				audMan.combineAudio(_music, _listLines);
				
				VideoMaker vidMaker = new VideoMaker();
				vidMaker.makeVideo(_term, _name, _numberOfPictures);
				
				Question question = new Question(new File("Quizzes/"+ _term + ".mp4"), _term);
				_set.addQuestion(question);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.setContents();
						_main.refreshGUI(null);
						_popup.showFeedback(_name, false);
						_pbCombine.setVisible(false);
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
		_listLines = FXCollections.observableArrayList();
		_numberOfAudioFiles = 0;
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


