package application.main;

import java.io.IOException;

import application.popup.HelpPopup;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * This class facilitates the help function in the app
 * @author Jacinta
 *
 */
public class Help {

	/**
	 * This is the help function for the Create tab. It gives user some more information
	 * on the hard parts of the application, or lets them open the user manual.
	 * @return
	 */

	public ContextMenu getContextMenuCreate() {
		final ContextMenu contextMenu = new ContextMenu(); //Shows multiple options for help
		HelpPopup hp = new HelpPopup();

		MenuItem playSaveText = new MenuItem("To Play/Save Text");
		playSaveText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//Calls help popup class to create helpful popup
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
				//Calls help popup class to create helpful popup
				hp.showCreateHelp("Double click on the AudioFiles\n"
						+ "you have already saved from the list\n"
						+ "on the right.");
			}
		});

		MenuItem userManual = userManualMenu();

		contextMenu.getItems().addAll(playSaveText, playAudio, userManual);
		return contextMenu;
	}

	/**
	 * This is the context menu for the Help button in Home, View, and Learn
	 * @return
	 */
	public ContextMenu getContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		MenuItem userManual = userManualMenu();
		// Opens the user manual 
		
		contextMenu.getItems().addAll(userManual);
		return contextMenu;

	}
	/**
	 * Opens the user manual when this menu item is clicked.
	 * @return
	 */
	public MenuItem userManualMenu() {
		MenuItem userManual = new MenuItem("To User Manual");
		userManual.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Task<Void> task = new Task<Void>() {
					@Override public Void call() {
						String cmd = "xdg-open VARpedia-User-Manual.pdf &> /dev/null";
						ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
						try {
							Process process = builder.start();
							process.waitFor();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						return null;
					}
				};
				new Thread(task).start();
			}
		});
		return userManual;
	}
}
