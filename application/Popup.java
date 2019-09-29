package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Represents Pop-ups windows that may be called during the use of the Wiki-Speak application 
 *
 */
public class Popup {
	private Create _create;
	private View _view;
	private Stage _popup = new Stage();
	private Stage _confirmPopup = new Stage();
	private Stage _tooManyWords = new Stage();


	public void setViewCreate(View view, Create create) {
		_create = create;
		_view = view;
	}

	/**
	 * Creates a popup with two buttons
	 * @param name		name of the file the popup will take action on 
	 * @param output	text that the pop up will contain
	 * @param button1	text to be written on button
	 * @param button2	text to be written on button
	 * @param isView	boolean of whether the popup is being called from the View tab
	 */
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

	/**
	 * Defines the action that is to be taken depending on whether it is being called 
	 * from the View tab
	 * @param name		name of file
	 * @param nxtAction	text of the button that was pushed
	 * @param isView	boolean of whether popup is being called from View tab
	 */
	public void action(String name, String nxtAction, boolean isView) {
		String fullName = "Creations/" + name + ".mp4";
		if(isView) {
			//delete creation, refresh tab and display confirmation popup
			_create.removeCreation(fullName);
			_view.setContents();
			showFeedback(name, true);
		}else {
			if(nxtAction.equals("Overwrite")) {
				//delete creation of the same name and create new creation
				_create.removeCreation(fullName);
				_create.combineAudioFiles();
			}else {
				_create.setContents(null);
			}
		}
	}

	/**
	 * Creates and displays a confirmation popup
	 * @param name		name of creation
	 * @param isView	boolean of whether popup is being called from View tab
	 */
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

	/**
	 * Creates and displays a warning popup when user highlights too many words
	 */
	public void tooManyWordsHighlighted() {
		_tooManyWords.setTitle("Too many words");
		
		Label text = new Label("Preview between 1 and 30 words.");
		Button butOK = new Button("OK");
		butOK.setOnAction(e -> _tooManyWords.close());
		VBox vbox = new VBox(10, text, butOK);
		vbox.setAlignment(Pos.CENTER);
		_tooManyWords.setScene(new Scene(vbox, 275, 75));
		_tooManyWords.show();
		
	}

}
