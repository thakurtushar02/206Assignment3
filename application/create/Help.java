package application.create;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class Help {

	public ContextMenu getContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("To Play/Save Text");
		item1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("To Play/Save Text");
			}
		});
		MenuItem item2 = new MenuItem("To Play AudioFiles");
		item2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        System.out.println("To Play AudioFiles");
		    }
		});
		contextMenu.getItems().addAll(item1, item2);
		return contextMenu;
	}
}
