package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
	private int _numPics;
	private TabPane _tabPane;
	private Main _main;
	ObservableList<String> listLines = FXCollections.observableArrayList();
	private int numberOfAudioFiles = 0;
	private final String EMPTY = "Empty";
	private final String VALID = "Valid";
	private final String DUPLICATE = "Duplicate";
	private final String INVALID = "Invalid";

	public Create(Tab tab, Popup popup) {
		_tab = tab;
		_popup = popup;
		_imMan = new ImageManager();
	}

	public void setView(View view) {
		_view = view;
	}

	public void setContents(Main main) {
		if (_main == null) {
			_main = main;
		}
		create.setText("Enter term to search for: ");
		create.setFont(new Font("Arial", 16));

		searchButton = new Button("Search ↳");

		searchBar = new HBox(create, search, searchButton);
		searchBar.setSpacing(15);

		message.setFont(new Font("Arial", 14));
		search.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode().equals(KeyCode.ENTER)) {
					searchButton.fire();
				}
			}
		});

		searchButton.setOnAction(e -> searchTerm(search.getText()));

		contents = new VBox(searchBar, message);
		contents.setPadding(new Insets(15,10,10,15));
		_tab.setContent(contents);
	}

	public void searchTerm(String term) {
		_popup.computeStagePopup();
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {
				_file = new File ("text.txt");
				ProcessBuilder builder = new ProcessBuilder("wikit", term);
				try {
					Process process = builder.start();
					BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

					PrintWriter out = new PrintWriter(new FileWriter(_file));

					int exitStatus = process.waitFor();

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
					@Override public void run() {
						_popup.closeComputeStagePopup();
						try(BufferedReader fileReader = new BufferedReader(new FileReader(_file.toString()))){
							String line = fileReader.readLine();
							if(line.contains("not found :^(")) {
								message.setText("Search term is invalid, please try again with another search term.");
								setContents(_main);
							} else {
								message.setText("");
								_term = term;
								deleteFiles();
								displayLines(term);
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

	public void displayLines(String reply) {
		ListView<String> list = new ListView<String>();

		list.setItems(listLines);

		HBox views= new HBox();
		TextArea textArea = new TextArea();
		textArea.setEditable(true);
		textArea.setWrapText(true);

		BufferedReader fileContent;
		try {
			fileContent = new BufferedReader(new FileReader(_file));
			String line;
			while ((line = fileContent.readLine()) != null) {
				textArea.appendText(line + "\n");
			}
			fileContent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		textArea.setText(textArea.getText().substring(2));

		Label lblList = new Label("Saved audio");
		lblList.setFont(new Font("Arial", 16));

		Text info = new Text("Move audio files ↑ or ↓ to get desired order.\n\n"
				+ "The creation will be created with audio\nfiles in the order "
				+ "they are below.\n\nDouble click to play audio file.");
		info.setFont(new Font("Arial", 12));
		VBox text = new VBox(searchBar, textArea);
		text.setSpacing(10);

		VBox.setVgrow(textArea, Priority.ALWAYS);

		VBox listView = new VBox(lblList, info, list);

		listView.setAlignment(Pos.CENTER_LEFT);
		listView.setSpacing(10);

		views.getChildren().addAll(text, listView);
		views.setSpacing(10);

		ObservableList<String> voices = FXCollections.observableArrayList("Default", "Espeak");
		final ComboBox<String> combobox = new ComboBox<String>(voices);
		combobox.setValue("Default");
		Label lblVoice = new Label("Voice: ");
		Button butPlay = new Button(" Play ►");
		Button butSave = new Button(" Save ✔");
		Button butUp = new Button("Move ↑");
		Button butDown = new Button("Move ↓");
		Button butDelete = new Button("Delete ✘");
		Button butCombine = new Button("Combine ↳");
		butCombine.disableProperty().bind(Bindings.size(listLines).isEqualTo(0));
		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);

		Slider slider = new Slider();
		slider.setMin(1);
		slider.setMax(10);
		slider.setValue(1);
		slider.setMajorTickUnit(1f);
		slider.isSnapToTicks();
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);

		Label photos = new Label("Choose Number of Pictures");
		photos.setFont(new Font("Arial", 16));

		slider.valueProperty().addListener((obs, oldval, newVal) -> slider.setValue(newVal.intValue()));


		HBox lineOptions = new HBox(lblVoice, combobox, butPlay, butSave, spacer, butUp, butDown, butDelete);
		lineOptions.setSpacing(15);
		lineOptions.setAlignment(Pos.BOTTOM_CENTER);

		TextField nameField = new TextField();
		nameField.setPromptText("Enter name of creation");
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if ((newValue.contains("/")) || (newValue.contains("\0"))) {
				nameField.setText(oldValue);
			}
		});

		final Pane spacer2 = new Pane();
		spacer2.setMinSize(10, 1);

		HBox.setHgrow(text, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		VBox.setVgrow(textArea, Priority.ALWAYS);

		HBox nameLayout = new HBox(10, photos, spacer2, nameField, butCombine);
		nameLayout.setAlignment(Pos.BOTTOM_CENTER);

		VBox layout = new VBox(views, lineOptions, nameLayout, slider);
		layout.setPadding(new Insets(10));
		layout.setSpacing(10);

		_tab.setContent(layout);
		_tabPane.requestLayout();

		butPlay.setOnAction(e -> {
			String selectedText = textArea.getSelectedText();
			if (selectedText.split(" ").length > 30) {
				_popup.tooManyWordsHighlighted();
			} else {
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {				
						String voice;
						String selection = combobox.getSelectionModel().getSelectedItem();
						if ( selection.equals("Default")) {
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
		butSave.setOnAction(e -> {
			String selectedText = textArea.getSelectedText();
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
			//TODO Delete mp4 file
		});

		nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode().equals(KeyCode.ENTER)) {
					butCombine.fire();
				}
			}
		});

		butCombine.setOnAction(e -> {
			String name = nameField.getText();
			String validity = checkName(name);
			_name = name;
			if (validity.equals(EMPTY)) {
				nameField.setPromptText("Nothing entered.");
				butCombine.requestFocus();
			} else if (validity.equals(VALID)) {
				nameField.setPromptText("");
				_popup.computeStagePopup();
				combineAudioFiles();
			} else if (validity.equals(DUPLICATE)) {
				nameField.clear();
				nameField.setPromptText("");
				butCombine.requestFocus();
				_popup.showStage(_name, "Creation name already exists.\nWould you like to rename or overwrite?", "Rename", "Overwrite", false);
			}
			else if (validity.equals(INVALID)){
				nameField.clear();
				nameField.setPromptText("Invalid Characters");
				butCombine.requestFocus();
			}
		});
		
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

	public String checkName(String reply) {
		File file = new File("./Creations/" + reply + ".mp4");
		if(file.exists()) {
			return DUPLICATE;
		} else {
			String newName = reply.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
			if(newName == reply) {
				if (reply.isEmpty() == false) {
					return VALID;
				} else {
					return EMPTY;
				}	
			}else {
				return INVALID;
			}
		}
	}

	public void addCreation(String voice) {
		_popup.computeStagePopup();
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

				if (voice.equals("Default")) {
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
						_popup.showFeedback(nameOfFile, false);
						listLines.add(nameOfFile);
						_popup.closeComputeStagePopup();
					}
				});
				return null;
			}
		};
		new Thread(task).start();
	}

	public void removeCreation(String name) {
		File file = new File(name + ".mp4");
		file.delete();
	}

	public void storeTabs(TabPane tabPane) {
		_tabPane = tabPane;
	}

	public void combineAudioFiles() {
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				String cmd;
				if (listLines.size() == 1) {
					cmd = "mv ./AudioFiles/AudioFile1.wav ./AudioFiles/"+ _name + ".wav";
				} else {
					cmd = "ffmpeg";
					for (String s: listLines) {
						cmd += " -i \"./AudioFiles/" + s + ".wav\"";
					}

					cmd += " -filter_complex \"";
					for (int i = 0; i < listLines.size(); i++) {
						cmd += "[" + i + ":0]";
					}
					cmd += "concat=n=" + listLines.size() + ":v=0:a=1[out]\" -map \"[out]\" ./AudioFiles/" + _name + ".wav &>/dev/null";
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
        //cmd = "ffmpeg -f lavfi -i color=c=blue:s=320x240:d=$(soxi -D ./AudioFiles/"+ _name +".wav) "
				//		+ "-vf \"drawtext=fontsize=30:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\'" 
				//		+ _term + "\'\" visual.mp4 &>/dev/null ; "; 
				//combining
        
        cmd = "ffmpeg -framerate $((" + _numPics + "))/$(soxi -D ./AudioFiles/"+ _name +".wav) -i " + _term + "%02d.jpg -vf \"scale=w=1280:h=720:force_original_aspect_ratio=1,pad=1280:720:(ow-iw)/2:(oh-ih)/2\" -r 25 visual.mp4 ; rm " + _term + "??.jpg ; ffmpeg -i visual.mp4 -vf \"drawtext=fontsize=50:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:borderw=5:text=\'" + _term + "\'\" out.mp4 ; ffmpeg -i out.mp4 -i ./AudioFiles/"+ _name +".wav -c:v copy -c:a aac -strict experimental -y ./Creations/" + _name + ".mp4 &>/dev/null ; rm visual.mp4 ; rm out.mp4";
				ProcessBuilder builderr = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process vidProcess = builderr.start();
					vidProcess.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			//	cmd = "mkdir -p Creations; ffmpeg -i visual.mp4 -i ./AudioFiles/"+_name+".wav -c:v copy -c:a "
			//	+ "aac -strict experimental -y \"Creations/" + _name + ".mp4\" &>/dev/null ; "
			//			+ "rm visual.mp4;";
				
			//	builderr = new ProcessBuilder("bash", "-c", cmd);
			//	builderr.start().waitFor();
				
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						_view.setContents();
						_main.refreshGUI(null);
						_popup.showFeedback(_name, false);
						_popup.closeComputeStagePopup();

					}
				});
				deleteFiles();
        
				return null;
			}
		};
		new Thread(task).start();
	}
	
	public void deleteFiles() {
		listLines = FXCollections.observableArrayList();
		numberOfAudioFiles = 0;
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				String cmd = "if [ -d AudioFiles ]; then rm -r AudioFiles; fi; if [ -e text.txt ]; then rm -f text.txt; fi";
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
  
  	public boolean getPics(int input, String reply) {
		if(input>10 || input<=0) {
			_popup.showStage("", "For amount of images, please enter a number between 1 and 10", "OK", "Cancel", false);
			return false;
		} else {
			_numPics = input;
			_imMan.getImages(input, reply);
			return true;
		}
	}
	
//	public void makeVideo() {
//		Task<Void> task = new Task<Void>() {
//
//			@Override
//			protected Void call() throws Exception {
//				
//				
//					}
//					
//				});
//				return null;
//			}
//			
//			
//			//TODO Then make a video file with correct length and number of pictures.
//			//TODO Then combine audio file with video file.
//			//TODO Then give notification to user.
//		};
//		new Thread(task).start();
//	}
}


