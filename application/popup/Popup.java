package application.popup;

import application.create.Create;
import application.view.View;
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
import javafx.stage.StageStyle;

/**
 * Represents Pop-ups windows that may be called during the use of the Wiki-Speak application 
 * @author Jacinta, Lynette, Tushar
 */
public class Popup {
	private Create _create;
	private View _view;
	private Stage _popup = new Stage();
	private Stage _confirmPopup = new Stage();
	private Stage _tooManyWords = new Stage();

	{
		_popup.initStyle(StageStyle.TRANSPARENT);
		_confirmPopup.initStyle(StageStyle.TRANSPARENT);
		_tooManyWords.initStyle(StageStyle.TRANSPARENT);
	}


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

		BorderPane comp;
		Button cont;
		Button cancel;

		cont = new Button(button1);
		cancel = new Button(button2);


		cont.setMinWidth(100);
		cont.setOnAction(e -> {
			if(isView) {
				action(name, button2, isView);
			}
			_popup.close();
		});

		cancel.setMinWidth(100);
		cancel.setOnAction(e -> {
			if(!isView) {
				action(name, button2, isView);
			}
			_popup.close();
		});
		HBox buttonBox;

		if (isView) {
			buttonBox = new HBox(cont, cancel); 
		} else {
			buttonBox = new HBox(cancel, cont);
		}
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

		Scene stageScene = new Scene(comp, 600, 200);
		stageScene.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
		comp.getStyleClass().add("blackBorder");
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
		String fullName = name + ".mp4";
		if(isView) {
			//delete creation, refresh tab and display confirmation popup
			_create.removeCreation(fullName);
			_view.setContents();
		}else {
			//delete creation of the same name and create new creation
			_create.removeCreation(fullName);
			_create.combineAudioFiles();

		}
	}

	/**
	 * Creates and displays a confirmation popup
	 * @param name		name of creation
	 * @param isView	boolean of whether popup is being called from View tab
	 */
	public void showFeedback(String name, boolean isView) {
		Label confirmation = new Label();
		_confirmPopup.setTitle("Creation Created");
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

		confirmation.setText(name + " made!");

		comp = new BorderPane();
		comp.setCenter(confirmation);
		comp.setBottom(cont);
		BorderPane.setAlignment(cont, Pos.BOTTOM_CENTER);
		comp.setPadding(new Insets(10,10,10,10));

		Scene stageScene = new Scene(comp, 600, 200);
		stageScene.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
		comp.getStyleClass().add("blackBorder");
		_confirmPopup.setScene(stageScene);
		_confirmPopup.show();
	}

	/**
	 * Creates and displays a warning popup when user highlights too many words
	 */
	public void tooManyWordsHighlighted() {
		_tooManyWords.setTitle("Too many words");

		Label text = new Label("Play only 1-30 words at a time!");
		Button butOK = new Button("OK");
		butOK.setOnAction(e -> _tooManyWords.close());
		VBox vbox = new VBox(10, text, butOK);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox, 600, 200);
		scene.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
		vbox.getStyleClass().add("blackBorder");
		_tooManyWords.setScene(scene);

		_tooManyWords.show();

	}

}
