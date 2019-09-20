package application;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VideoPlayer {
	//Might also want to make video using this...
	
	private Button btnMute = new Button("Mute");
	private Button btnPlayPause = new Button("Pause");
	private Button btnForward = new Button(">>");
	private Button btnBackward = new Button("<<");
	private HBox buttonBox = new HBox(btnBackward, btnPlayPause, btnForward);
	
	public void playVideo(String name) {
		File fileURL = new File(name + ".mp4");
		
		Stage vidStage = new Stage();
		BorderPane vidPane = new BorderPane();
		Scene scene = new Scene(vidPane, 340, 300);
		
		Media video = new Media(fileURL.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);
		
		vidPane.setTop(btnMute);
		btnMute.setPrefWidth(scene.getWidth());
		btnMute.setOnAction(e -> {
			player.setMute(!player.isMute());
			if(player.isMute()) {
				btnMute.setText("Unmute");
			} else {
				btnMute.setText("Mute");
			}
		});
		
		vidStage.setOnCloseRequest(e -> {
			player.pause();
		});
		
		vidPane.setBottom(buttonBox);
		btnPlayPause.setPrefWidth(scene.getWidth()/1.5);
		btnPlayPause.setOnAction(e -> {
			if(player.getStatus() == Status.PLAYING) {
				player.pause();
				btnPlayPause.setText("Play");
			}else {
				player.play();
				btnPlayPause.setText("Pause");
			}
		});
		
		btnForward.setPrefWidth(scene.getWidth()/6);
		btnForward.setOnAction(e -> {
			player.seek(player.getCurrentTime().add(Duration.seconds(2)));
		});
		
		btnBackward.setPrefWidth(scene.getWidth()/6);
		btnBackward.setOnAction(e -> {
			player.seek(player.getCurrentTime().add(Duration.seconds(-2)));
		});
		
		player.setOnEndOfMedia(() -> {
			vidStage.close();
		});
		
		buttonBox.setSpacing(3);
		
		vidPane.setCenter(mediaView);
		vidStage.setScene(scene);
		vidStage.show();
	}
}
