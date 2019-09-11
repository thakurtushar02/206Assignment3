package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Home {
	private VBox contents;
	private Label heading;
	private Label heading1;
	private Tab _tab;
	
	public Home(Tab tab) {
		_tab = tab;
	}
	
	public void setContents() {
		heading = new Label();
		heading.setText("Welcome to the Wiki-Speak Authoring Tool.");
		heading.setFont(new Font("Arial", 20));
		
		heading1 = new Label();
		heading1.setText("Please choose your options in the tabs above.\n\nIf you want to DELETE or PLAY your creations please select View Creations.");
		heading1.setFont(new Font("Arial", 14));
		
		contents = new VBox(heading, heading1);
		contents.setPadding(new Insets(15,10,10,15));
		contents.setSpacing(15);
		_tab.setContent(contents);
	}
}
