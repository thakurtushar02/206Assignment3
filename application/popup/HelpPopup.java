package application.popup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

/**
 * Special popup for the help function which gives direction to the
 * user when they are stuck
 * @author Jacinta, Lynette, Tushar
 *
 */
public class HelpPopup extends Popup {

	/**
	 * Shows the help message for the Create tab. There are two possible
	 * help messages.
	 * @param msg
	 */
	public void showCreateHelp(String msg) {
		BorderPane comp = new BorderPane();
		Label text = new Label();
		
		Button ok = new Button("OK");
		ok.setMinWidth(100);
		ok.setPadding(new Insets(5,10,5,10));
		ok.setOnAction(e -> {
			_popup.close();
		});
		
		text.setTextAlignment(TextAlignment.CENTER);
		text.setLineSpacing(5);
		text.setPrefHeight(250);

		text.setText(msg);
		
		comp.setCenter(text);
		comp.setBottom(ok);
		BorderPane.setAlignment(ok, Pos.BOTTOM_CENTER);
		comp.setPadding(new Insets(10,10,10,10));

		Scene stageScene = new Scene(comp, 650, 350);
		stageScene.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
		getBorder(comp);
		_popup.setScene(stageScene);
		_popup.show();
	}
}
