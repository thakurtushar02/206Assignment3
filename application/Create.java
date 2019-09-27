package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
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
	private int lineCount = 0;
	private String _term;
	private View _view;
	private String _name;
	private Popup _popup;
	private File _file;
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

		searchButton = new Button("Search");

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
		_tabPane.getTabs().remove(0);
		_tabPane.getTabs().remove(0);

		Label title = new Label("Results for \"" + reply + "\"");
		title.setFont(new Font("Arial", 16));


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

		Text info = new Text("Move up/down to get desired order.");
		info.setFont(new Font("Arial", 12));

		Text info2 = new Text("The creation will be created with audio");
		info2.setFont(new Font("Arial", 12));

		Text info3 = new Text("files in the order they are below.");
		info3.setFont(new Font("Arial", 12));

		VBox text = new VBox(title, textArea);
		text.setSpacing(10);

		VBox.setVgrow(textArea, Priority.ALWAYS);

		VBox listView = new VBox(lblList, info, info2, info3, list);

		listView.setAlignment(Pos.CENTER_LEFT);
		listView.setSpacing(10);

		views.getChildren().addAll(text, listView);
		views.setSpacing(10);

		ObservableList<String> voices = FXCollections.observableArrayList("Default", "Espeak");
		final ComboBox<String> combobox = new ComboBox<String>(voices);
		combobox.setValue("Default");
		Label lblVoice = new Label("Voice: ");
		Button butPlay = new Button(" Play ");
		Button butSave = new Button(" Save ");
		Button butUp = new Button("  Up  ");
		Button butDown = new Button(" Down ");
		Button butDelete = new Button("Delete");
		Button butCombine = new Button("Combine!");
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
			list.getItems().remove(list.getSelectionModel().getSelectedIndex());
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
				combineAudioFiles();
				//TODO Then make a video file with correct length and number of pictures.
				//TODO Then combine audio file with video file.
				//TODO Then give notification to user.
				_main.refreshGUI(null);
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});


	}
	


	//		butText.setOnAction(e -> {
	//			_popup.editText();
	//			list.setEditable(true);
	//			list.setCellFactory(TextFieldListCell.forListView());
	//			lineOptions.getChildren().removeAll(prompt, numberTextField, butNum, butPreview, butText);
	//			lineOptions.getChildren().add(butDone);
	//		});

	//		butDone.setOnAction(e -> {
	//			list.setEditable(false);
	//			lineOptions.getChildren().remove(butDone);
	//			lineOptions.getChildren().addAll(prompt, numberTextField, butNum, butPreview, butText);
	//				try {
	//					String fileName = _file.getName();
	//					FileWriter fw = new FileWriter(fileName, false);
	//					fw.write("");
	//					fw.close();
	//					fw = new FileWriter(fileName, true);
	//				int count = 1;
	//				for (String s: listLines) {
	//					if (s.length() < 4) {
	//						continue;
	//					}
	//					String newString= "";
	//					if (count < 10) {
	//						String[] sArray = s.substring(3).split("\\. ");
	//						for (String st: sArray) {
	//							if (st.endsWith(".")) {
	//								newString += st + "\n";
	//							} else {
	//								newString += st + ".\n";
	//							}
	//						}
	//					} else {
	//						String[] sArray = s.substring(4).split("\\. ");
	//						for (String st: sArray) {
	//							if (st.endsWith(".")) {
	//								newString += st + "\n";
	//							} else {
	//								newString += st + ".\n";
	//							}
	//						}
	//					}
	//					fw.write(newString);
	//					count++;
	//				}
	//				fw.close();
	//			} catch (IOException ioe){
	//				ioe.getMessage();
	//			}
	//			displayLines(reply);
	//		});
	//
	//		butPreview.setOnAction(e -> {
	//			_popup.previewText(_file);
	//		});
	//	}

	//	public boolean getLines(int input, String reply) {
	//		if(input>=lineCount || input<=0) {
	//			_popup.showStage("", "Please enter a number between 1 and " + (lineCount-1), "OK", "Cancel", false);
	//			return false;
	//		} else {
	//			input++;
	//			if (input < lineCount && input > 1) {
	//				String[] cmd = {"sed", "-i",  input + ","+ lineCount + "d", _file.toString()};
	//				ProcessBuilder builder = new ProcessBuilder(cmd);
	//				try {
	//					Process process = builder.start();
	//					process.waitFor();
	//				} catch (IOException e) {
	//					e.printStackTrace();
	//				} catch (InterruptedException e) {
	//					e.printStackTrace();
	//				}
	//			} else if (input == lineCount) {
	//				String[] cmd= {"sed", "-i", "$d", _file.toString()};
	//				ProcessBuilder builder = new ProcessBuilder(cmd);
	//				try {
	//					Process process = builder.start();
	//					process.waitFor();
	//				} catch (IOException | InterruptedException e) {
	//					e.printStackTrace();
	//				}
	//			}
	//			return true;
	//		}
	//	}

	//	public void getName() {
	//		VBox cont;
	//		Button butNam = new Button("Create");
	//
	//		Label cre = new Label("Enter name for your creation: ");
	//		cre.setFont(new Font("Arial", 16));
	//		TextField wordTextField = new TextField();
	//
	//
	//		HBox nameBar = new HBox(cre, wordTextField, butNam);
	//		nameBar.setSpacing(15);
	//
	//		Label mes = new Label();
	//		mes.setFont(new Font("Arial", 14));
	//
	//		butNam.setOnAction(new EventHandler<ActionEvent>() {
	//			@Override
	//			public void handle(ActionEvent e) {
	//				String reply = wordTextField.getText();
	//				String validity = checkName(reply);
	//				_name = reply;
	//				if (validity.equals(EMPTY)) {
	//					mes.setText("You haven't entered a creation name! Please try again.");
	//				} else if (validity.equals(VALID)) {
	//					mes.setText("");
	//					_name = reply;
	//					//addCreation();
	//				} else if (validity.equals(DUPLICATE)) {
	//					_popup.showStage(_name, "Creation name already exists.\nWould you like to rename or overwrite?", "Rename", "Overwrite", false);
	//				}
	//				else if (validity.equals(INVALID)){
	//					mes.setText("Creation name contains invalid characters, please try again.");
	//				}
	//			}
	//		});
	//
	//		cont = new VBox(nameBar, mes);
	//		cont.setPadding(new Insets(15,10,10,15));
	//		_tab.setContent(cont);
	//	}

	public String checkName(String reply) {
		File file = new File(reply + ".mp4");
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




				//				String cMD = "";

				//				cMD = "ffmpeg -f lavfi -i color=c=blue:s=320x240:d=$(soxi -D temp.wav) -vf \"drawtext=fontsize=30:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\'" + _term + "\'\" visual.mp4 &>/dev/null ; ffmpeg -i visual.mp4 -i temp.wav -c:v copy -c:a aac -strict experimental -y \"./AudioFiles/" + nameOfFile + ".mp4\" &>/dev/null ; rm visual.mp4;";
				//				ProcessBuilder builderr = new ProcessBuilder("/bin/bash", "-c", cMD);
				//				try {
				//					Process vidProcess = builderr.start();
				//					vidProcess.waitFor();
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				} catch (InterruptedException e) {
				//					e.printStackTrace();
				//				}
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

				String cmd = "ffmpeg";
				for (String s: listLines) {
					cmd += " -i \"./AudioFiles/" + s + ".wav\"";
				}

				cmd += " -filter_complex \"";
				for (int i = 0; i < listLines.size(); i++) {
					cmd += "[" + i + ":0]";
				}
				cmd += "concat=n=" + listLines.size() + ":v=0:a=1[out]\" -map \"[out]\" " + _name + ".wav";
				System.out.println(cmd);
				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process process = builder.start();
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				cmd = "rm -r AudioFiles";
				builder = new ProcessBuilder("/bin/bash", "-c", cmd);
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
}
