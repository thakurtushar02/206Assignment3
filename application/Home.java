package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
	private Label heading1;
	private Label heading2;
	private Tab _tab;
	private int _titleNumber = 0;
	
	public Home(Tab tab) {
		_tab = tab;
	}
	
	/**
	 * Initialises the starting Home page for the GUI and instructions on how to use
	 * the application.
	 */
	public void setContents() {
		ImageView iv = new ImageView();
		iv.setImage(new Image(".resources/titles/title" + _titleNumber + ".png"));
		iv.setPreserveRatio(true);
		iv.setFitWidth(800);
		
		HBox imageBox = new HBox(iv);
		
		imageBox.setOnMouseEntered(arg0 -> {
			iv.setFitWidth(900);
		});
		imageBox.setOnMouseExited(arg0 -> {
			iv.setFitWidth(800);
		});
		imageBox.setOnMouseClicked(arg0 -> {
			_titleNumber = (_titleNumber + 1) % 3;
			iv.setImage(new Image(".resources/titles/title" + _titleNumber + ".png"));
		});
		
		imageBox.setAlignment(Pos.CENTER);
		
		
		heading1 = new Label();
		heading1.setText("\nPlease choose your options from the tabs above.\n"
				+ "If you want to DELETE or PLAY your creations please select View Creations.\n\n\n");
		heading1.setFont(new Font("Arial", 20));
		
		
		heading2 = new Label();
		heading2.setText("\nHave fun!\n\n"
				+ "\t\t\t\t\t\t(╯°□°）╯︵ ┻━┻\n\n"
				+ "...But not too much fun.\n\n"
				+ "\t\t\t\t\t\t┬──┬ ノ( °-°ノ)");
		heading2.setFont(new Font("Arial", 20));
		
		
		contents = new VBox(imageBox, heading1, heading2);
		contents.setPadding(new Insets(30,10,10,15));
		contents.setSpacing(15);
		_tab.setContent(contents);
	}
}
