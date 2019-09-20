package application;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Video {
	//Might also want to make video using this...
	
	public void playVideo(String name) {
		File fileURL = new File(name + ".mp4");
		
		Stage vidStage = new Stage();
		BorderPane vidPane = new BorderPane();
		Scene scene = new Scene(vidPane, 400, 400);
		
		Media video = new Media(fileURL.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);
		
		vidPane.setCenter(mediaView);
		vidStage.setScene(scene);
		vidStage.show();
		
//		String cmd = "ffplay -autoexit -i " + name + ".mp4 &>/dev/null";
//		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
//		try {
//			builder.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
	}
}
