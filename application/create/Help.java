package application.create;

import application.popup.HelpPopup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * This class facilitates the help function in the app
 * @author Jacinta, Lynette, Tushar
 *
 */
public class Help {

	/**
	 * This is the help function for the Create tab
	 * @return
	 */
	public ContextMenu getContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		HelpPopup hp = new HelpPopup();
		
		MenuItem playSaveText = new MenuItem("To Play/Save Text");
		playSaveText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				hp.showCreateHelp("Highlight the piece of text you\n"
						+ "want to Play or Save and select the\n"
						+ "voice you want, then press the\n"
						+ "Play/Save button.");
			}
		});
		
		MenuItem playAudio = new MenuItem("To Play AudioFiles");
		playAudio.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				hp.showCreateHelp("Double click on the AudioFiles\n"
						+ "you have already saved from the list\n"
						+ "on the right.");
		    }
		});
		
		contextMenu.getItems().addAll(playSaveText, playAudio);
		return contextMenu;
	}
}
