package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * This class is related to the Home tab. It is where the user starts once
 * the application begins running, and after a creation is created.
 * @author Jacinta, Lynette, Tushar
 *
 */
public class Home {
	private VBox contents;
	private Label heading;
	private Label heading1;
	private Label heading2;
	private Tab _tab;
	
	public Home(Tab tab) {
		_tab = tab;
	}
	
	/**
	 * Initialises the starting Home page for the GUI and instructions on how to use
	 * the application.
	 */
	public void setContents() {
		heading = new Label();
		heading.setText("Welcome to the Wiki-Speak Authoring Tool!\t(◕ヮ◕)");
		heading.setFont(new Font("Arial", 40));
		
		
		heading1 = new Label();
		heading1.setText("\nPlease choose your options from the tabs above.\n"
				+ "If you want to DELETE or PLAY your creations please select View Creations.\n\n\n");
		heading1.setFont(new Font("Arial", 25));
		
		
		heading2 = new Label();
		heading2.setText("\nHave fun!\n\n"
				+ "\t\t\t\t\t\t(╯°□°）╯︵ ┻━┻\n\n"
				+ "...But not too much fun.\n\n"
				+ "\t\t\t\t\t\t┬──┬ ノ( °-°ノ)");
		heading2.setFont(new Font("Arial", 30));
		
		
		contents = new VBox(heading, heading1, heading2);
		contents.setPadding(new Insets(15,10,10,15));
		contents.setSpacing(15);
		_tab.setContent(contents);
	}
}
