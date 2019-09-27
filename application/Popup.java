package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Popup {
	private Create _create;
	private View _view;
	private Stage _popup = new Stage();
	private Stage _confirmPopup = new Stage();
	private Stage _computing = new Stage();
	private Stage _previewStage = new Stage();
	private Stage _textStage = new Stage();
	private Stage _audioSave = new Stage();


	public void setViewCreate(View view, Create create) {
		_create = create;
		_view = view;
	}

	public void showStage(String name, String output, String button1, String button2, boolean isView){
		if(isView) {
			_popup.setTitle("Delete Creation");
		}else {
			_popup.setTitle("Invalid Input");
		}
		BorderPane comp;

		Button cont = new Button(button1);
		cont.setMinWidth(100);
		cont.setOnAction(e -> {
			if(isView) {
				action(name, button2, isView);
			}
			_popup.close();
		});

		Button cancel = new Button(button2);
		cancel.setMinWidth(100);
		cancel.setOnAction(e -> {
			if(!isView) {
				action(name, button2, isView);
			}
			_popup.close();
		});

		HBox buttonBox = new HBox(cont, cancel);
		buttonBox.setPadding(new Insets(5,10,5,10));
		buttonBox.setSpacing(20);
		buttonBox.setAlignment(Pos.BOTTOM_CENTER);

		Label confirmation = new Label(output);
		confirmation.setFont(new Font("Arial", 14));
		confirmation.setTextAlignment(TextAlignment.CENTER);
		confirmation.setLineSpacing(5);
		confirmation.setPrefHeight(100);
		comp = new BorderPane();
		comp.setCenter(confirmation);
		comp.setBottom(buttonBox);
		comp.setPadding(new Insets(10,10,10,10));

		Scene stageScene = new Scene(comp, 400, 100);
		_popup.setScene(stageScene);
		_popup.show();
	}

	public void action(String name, String nxtAction, boolean isView) {
		if(isView) {
			_create.removeCreation(name);
			_view.setContents();
			showFeedback(name, true);
		}else {
			if(nxtAction.equals("Overwrite")) {
				_create.removeCreation(name);
				//_create.addCreation();
				_create.setContents(null);
				showFeedback(name, false);
			}else {
				_create.setContents(null);
			}
		}
	}

	public void showFeedback(String name, boolean isView) {
		Label confirmation = new Label();
		if(isView) {
			_confirmPopup.setTitle("Creation Deleted");
		}else {
			_confirmPopup.setTitle("Creation Created");
		}
		BorderPane comp;

		Button cont = new Button("OK");
		cont.setMinWidth(100);
		cont.setPadding(new Insets(5,10,5,10));

		cont.setOnAction(e -> {
			_confirmPopup.close();
		});

		confirmation.setFont(new Font("Arial", 14));
		confirmation.setTextAlignment(TextAlignment.CENTER);
		confirmation.setLineSpacing(5);
		confirmation.setPrefHeight(100);

		if(isView) {
			confirmation.setText("Creation " + name + " successfully deleted");
		}else {
			confirmation.setText("Creation " + name + " successfully created");
		}

		comp = new BorderPane();
		comp.setCenter(confirmation);
		comp.setBottom(cont);
		BorderPane.setAlignment(cont, Pos.BOTTOM_CENTER);
		comp.setPadding(new Insets(10,10,10,10));

		Scene stageScene = new Scene(comp, 400, 100);
		_confirmPopup.setScene(stageScene);
		_confirmPopup.show();
	}

	public void computeStagePopup() {
		VBox vbox = new VBox(10);
		Label searchText = new Label("Computing... Please wait...");
		ProgressBar pb = new ProgressBar();
		pb.prefWidthProperty().bind(vbox.widthProperty());
		vbox.getChildren().addAll(searchText, pb);
		_computing.setTitle("Computing Task");
		_computing.setScene(new Scene(vbox, 275, 75));
		_computing.show();
	}

	public void closeComputeStagePopup() {
		_computing.close();
	}


//	public void editText() {
//
//		VBox vbox = new VBox(10);
//		vbox.setPadding(new Insets(10,10,0,10));
//		Label label = new Label("Double click on the line in the list you want to edit. "
//				+ "After finishing editing, press enter to confirm edit for that line.\n"
//				+ "Repeat until all desired lines are edited.\n"
//				+ "Finally, press \"Done Edit\" to finalise editing.\n\n"
//				+ "Note: To add a line, simply add the line to the line before where you want it. "
//				+ "If you want to add a line between 4 and 5, simply write the line on line 4 and end with full stop.\n\n"
//				+ "For example:\n\t"
//				+ "4. Fourth Sentence. Added sentence. \n\t5. Fifth sentence.\n"
//				+ "After pressing \"Done Edit\", the above will become \n\t"
//				+ "4. Fourth Setence.\n\t5. Added sentence.\n\t6. Fifth sentence.\n\n"
//				+ "To delete a line, simply remove all text from that line.\n\n"
//				+ "Please do not remove line counts otherwise eg. Keep \"1. \" \"3. \" etc, (with space after dot)\n"
//				+ "Don't worry, they won't show up in the final creation :)\n\n"
//				+ "Have fun editing!");
//		label.setWrapText(true);
//		Button butOK = new Button("I understand. Now let me edit!");
//		butOK.prefWidthProperty().bind(vbox.widthProperty());
//
//		vbox.getChildren().addAll(label, butOK);
//		_textStage.setTitle("Editing text: Some Things You Should Know");
//		_textStage.setScene(new Scene(vbox, 800, 400));
//		_textStage.show();
//
//		butOK.setOnAction(e -> {
//			_textStage.close();
//		});
//
//	}
	public void tooManyWordsHighlighted() {
		_computing.setTitle("Too many words");
		
		Label text = new Label("Preview between 1 and 30 words.");
		Button butOK = new Button("OK");
		butOK.setOnAction(e -> closeComputeStagePopup());
		VBox vbox = new VBox(10, text, butOK);
		vbox.setAlignment(Pos.CENTER);
		_computing.setScene(new Scene(vbox, 275, 75));
		_computing.show();
		
	}

	public void previewText(File file) {
		//		String textString = "";
		//		int count = 1;
		//		for (String s: listLines) {
		//			if (count < 10) {
		//				textString += s.substring(3) + "\n";
		//			} else {
		//				textString += s.substring(5) + "\n";
		//			}
		//			count++;
		//		}

		TextArea textArea = new TextArea();
		textArea.setEditable(false);

		BufferedReader fileContent;
		try {
			fileContent = new BufferedReader(new FileReader(file));
			String line;
			while ((line = fileContent.readLine()) != null) {
				textArea.appendText(line + "\n");
			}
			fileContent.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Button butPreview = new Button("Play");
		Button butDone = new Button("Done Previewing");
		Label label = new Label("Highlight the text you want to preview using speech synthesiser, then click \"Play\"");
		Label lblVoice = new Label("Voice: ");
		label.setWrapText(true);

		ObservableList<String> voices = FXCollections.observableArrayList("Default", "Espeak");
		final ComboBox<String> combobox = new ComboBox<String>(voices);
		combobox.setValue("Default");

		final Pane spacer = new Pane();
		spacer.setMinSize(10, 1);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10,10,10,10));
		textArea.prefHeightProperty().bind(vbox.heightProperty().subtract(20));
		textArea.prefWidthProperty().bind(vbox.widthProperty().subtract(20));

		HBox hbox = new HBox(10);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		hbox.getChildren().addAll(lblVoice, combobox,butPreview,spacer,butDone);
		hbox.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(label, textArea, hbox);
		_previewStage.setTitle("Preview highlighted text");
		_previewStage.setScene(new Scene(vbox, 800, 400));
		_previewStage.show();

		butDone.setOnAction(e -> {
			_previewStage.close();
		});

		butPreview.setOnAction(e -> {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {

					String voice;
					String selection = combobox.getSelectionModel().getSelectedItem();
					if ( selection.equals("Default")) {
						voice = "tts";
					} else {
						voice = "espeak";
					}

					String command = "echo \"" + textArea.getText() + " \" | " + voice ;
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
		});
	}

	public void editText() {

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10,10,0,10));
		Label label = new Label("Double click on the line in the list you want to edit. "
				+ "After finishing editing, press enter to confirm edit for that line.\n"
				+ "Repeat until all desired lines are edited.\n"
				+ "Finally, press \"Done Edit\" to finalise editing.\n\n"
				+ "Note: To add a line, simply add the line to the line before where you want it. "
				+ "If you want to add a line between 4 and 5, simply write the line on line 4 and end with full stop.\n\n"
				+ "For example:\n\t"
				+ "4. Fourth Sentence. Added sentence. \n\t5. Fifth sentence.\n"
				+ "After pressing \"Done Edit\", the above will become \n\t"
				+ "4. Fourth Setence.\n\t5. Added sentence.\n\t6. Fifth sentence.\n\n"
				+ "To delete a line, simply remove all text from that line.\n\n"
				+ "Please do not remove line counts otherwise eg. Keep \"1. \" \"3. \" etc, (with space after dot)\n"
				+ "Don't worry, they won't show up in the final creation :)\n\n"
				+ "Have fun editing!");
		label.setWrapText(true);
		Button butOK = new Button("I understand. Now let me edit!");
		butOK.prefWidthProperty().bind(vbox.widthProperty());

		vbox.getChildren().addAll(label, butOK);
		_textStage.setTitle("Editing text: Some Things You Should Know");
		_textStage.setScene(new Scene(vbox, 1000, 400));
		_textStage.show();

		butOK.setOnAction(e -> {
			_textStage.close();
		});

	}

	public void audioSave () {
		_audioSave.setTitle("Save");
		Label save = new Label("Please enter a name to save the audio as");
		TextField name = new TextField();
		Button butSave = new Button("Save");
		Button butCancel = new Button("Cancel");
		
		butCancel.setOnAction(e -> {
			_audioSave.close();
		});
	}

}
