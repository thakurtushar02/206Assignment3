package application.create;

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
	 * This is the help function for the Create tab
	 * @return
	 */
	@SuppressWarnings("all")
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

		// Opens the user manual 
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

		contextMenu.getItems().addAll(playSaveText, playAudio, userManual);
		return contextMenu;
	}
}
