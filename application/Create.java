package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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

	public Create(Tab tab, Popup popup) {
		_tab = tab;
		_popup = popup;
	}

	public void setView(View view) {
		_view = view;
	}

	public void setContents() {
		create.setText("Enter term to search for: ");
		create.setFont(new Font("Arial", 16));

		searchButton = new Button("Search");

		searchBar = new HBox(create, search, searchButton);
		searchBar.setSpacing(15);

		message.setFont(new Font("Arial", 14));

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String reply = search.getText();
				searchTerm(reply);
			}
		});

		contents = new VBox(searchBar, message);
		contents.setPadding(new Insets(15,10,10,15));
		_tab.setContent(contents);
	}

	public void searchTerm(String term) {
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {
				String cmd = "$(wikit " + term + " > temp.txt) sed 's/\\./\\.\\n/g' temp.txt > temp2.txt";
				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
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
						try(BufferedReader fileReader = new BufferedReader(new FileReader("temp2.txt"))){
							String line = fileReader.readLine();
							if(line.contains("not found :^(")) {
								message.setText("Search term is invalid, please try again with another search term.");
								setContents();
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
		BorderPane lineContents = new BorderPane();
		lineContents.setPadding(new Insets(15,10,10,15));
		lineContents.setMaxHeight(360);

		Label title = new Label(reply);
		title.setFont(new Font("Arial", 16));
		lineContents.setTop(title);

		Label prompt = new Label("How many lines do you want in your creation:");
		prompt.setFont(new Font("Arial", 14));

		TextField ans = new TextField();
		ans.setMaxWidth(100);

		ListView<String> list = new ListView<String>();
		ObservableList<String> listLines = FXCollections.observableArrayList();
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("temp2.txt"));
			String line = null;
			int i = 1;
			while((line = reader.readLine()) != null) {
				listLines.add(i + ". " + line);
				i++;
			}
			listLines.remove(i-2);
			lineCount = i-1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		list.setItems(listLines);
		lineContents.setCenter(list);
		BorderPane.setMargin(list, new Insets(10,0,10,0));

		Button butNum = new Button("Submit");
		butNum.setOnAction(e -> {
			String inNum = ans.getText();
			try {
				int num = Integer.parseInt(inNum);
				if(getLines(num, reply)) {
					getName();
				}
			}catch(NumberFormatException | NullPointerException nfe) {
				_popup.showStage("", "Please enter an integer number. Would you like to continue?", "Yes", "No", false);
			}
		});
		HBox lineOptions = new HBox(prompt, ans, butNum);
		lineOptions.setSpacing(15);
		lineContents.setBottom(lineOptions);

		_tab.setContent(lineContents);
	}

	public boolean getLines(int input, String reply) {
		if(input>=lineCount || input<=0) {
			_popup.showStage("", "Please enter a number between 1 and " + (lineCount-1), "OK", "Cancel", false);
			return false;
		} else {
			String cmd = "head -n " + input + " temp2.txt > text.txt";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = builder.start();
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public void getName() {
		VBox cont;
		Button butNam = new Button("Create");

		Label cre = new Label("Enter name for your creation: ");
		cre.setFont(new Font("Arial", 16));
		TextField ans = new TextField();

		HBox nameBar = new HBox(cre, ans, butNam);
		nameBar.setSpacing(15);

		Label mes = new Label();
		mes.setFont(new Font("Arial", 14));

		butNam.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String reply = ans.getText();
				String validity = checkName(reply);
				_name = reply;
				if(validity=="Valid") {
					mes.setText("");
					_name = reply;
					addCreation();
				} else if(validity=="Duplicate"){
					_popup.showStage(_name, "Creation name already exists.\nWould you like to rename or overwrite?", "Rename", "Overwrite", false);
				}
				else {
					mes.setText("Creation name contains invalid characters, please try again.");
				}
			}
		});

		cont = new VBox(nameBar, mes);
		cont.setPadding(new Insets(15,10,10,15));
		_tab.setContent(cont);
	}

	public String checkName(String reply) {
		File file = new File(reply + ".mp4");
		if(file.exists()) {
			return "Duplicate";
		} else {
			String newName = reply.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
			if(newName == reply) {
				return "Valid";
			}else {
				return "Invalid";
			}
		}
	}

	public void addCreation() {
		Task<Void> task = new Task<Void>() {
			@Override public Void call() {
				String cmd = "cat text.txt | text2wave -o temp.wav";
				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				try {
					Process process = builder.start();
					process.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String cMD = "ffmpeg -f lavfi -i color=c=blue:s=320x240:d=$(soxi -D temp.wav) -vf \"drawtext=fontsize=30:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=\'" + _term + "\'\" visual.mp4 &>/dev/null ; ffmpeg -i visual.mp4 -i temp.wav -c:v copy -c:a aac -strict experimental -y " + _name + ".mp4 &>/dev/null ; rm visual.mp4";
				ProcessBuilder builderr = new ProcessBuilder("/bin/bash", "-c", cMD);
				try {
					Process vidProcess = builderr.start();
					vidProcess.waitFor();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(new Runnable(){
					@Override public void run() {
						_view.setContents();
						_popup.showFeedback(_name, false);
						setContents();
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
}
