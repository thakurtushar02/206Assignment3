package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Popup {
	private Create _create;
	private View _view;
	private Stage _popup;
	private Stage _popup2;
	private Stage _computing = new Stage();
	
	public void setViewCreate(View view, Create create) {
		_create = create;
		_view = view;
	}
	
	public void showStage(String name, String output, String button1, String button2, boolean isView){
		_popup = new Stage();
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

		Scene stageScene = new Scene(comp, 500, 100);
		_popup.setScene(stageScene);
		_popup.show();
	}
	
	public void action(String name, String nxtAction, boolean isView) {
		if(isView) {
			_create.removeCreation(name);
			_view.setContents();
			showFeedback(name, true);
		}else {
			if(nxtAction == "Overwrite") {
				_create.removeCreation(name);
				_create.addCreation();
				_create.setContents();
				showFeedback(name, false);
			}else {
				_create.setContents();
			}
		}
	}
	
	public void showFeedback(String name, boolean isView) {
		_popup2 = new Stage();
		Label confirmation = new Label();
		if(isView) {
			_popup2.setTitle("Creation Deleted");
		}else {
			_popup2.setTitle("Creation Created");
		}
		BorderPane comp;

		Button cont = new Button("OK");
		cont.setMinWidth(100);
		cont.setPadding(new Insets(5,10,5,10));
		
		cont.setOnAction(e -> {
			_popup2.close();
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

		Scene stageScene = new Scene(comp, 500, 100);
		_popup2.setScene(stageScene);
		_popup2.show();
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
}
