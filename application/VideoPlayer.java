package application;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
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

/**
 * This class plays specified creations using MediaPlayer.
 */
public class VideoPlayer {

	private Button btnMute = new Button("Mute");
	private Button btnPlayPause = new Button("Pause");
	private Button btnForward = new Button(">>");
	private Button btnBackward = new Button("<<");
	private HBox buttonBox = new HBox(btnBackward, btnPlayPause, btnForward);

	/**
	 * Creates a new Stage to play creation. The video has mute/unmute, play/pause and forward/backward
	 * functionality. The stage closes automatically when the video finishes.
	 * @param name
	 */
	public void playVideo(String name) {
		File fileURL = new File("Creations/" + name + ".mp4");

		Stage vidStage = new Stage();

		vidStage.setTitle(name + ".mp4");
		BorderPane vidPane = new BorderPane();
		Scene scene = new Scene(vidPane, 500, 330);

		Media video = new Media(fileURL.toURI().toString());
		MediaPlayer player = new MediaPlayer(video);
		player.setAutoPlay(true);
		MediaView mediaView = new MediaView(player);;

		vidPane.setTop(btnMute);
		btnMute.prefWidthProperty().bind(scene.widthProperty());
		btnMute.setOnAction(e -> {
			player.setMute(!player.isMute());
			if(player.isMute()) {
				btnMute.setText("Unmute");
			} else {
				btnMute.setText("Mute");
			}
		});

		vidStage.setOnCloseRequest(e -> {
			player.pause();	//Stop sound playing when window has been closed
		});

		vidPane.setBottom(buttonBox);

		btnPlayPause.prefWidthProperty().bind(scene.widthProperty().divide(1.5));
		btnPlayPause.setOnAction(e -> {
			if(player.getStatus() == Status.PLAYING) {
				player.pause();
				btnPlayPause.setText("Play");
			}else {
				player.play();
				btnPlayPause.setText("Pause");
			}
		});

		btnForward.prefWidthProperty().bind(scene.widthProperty().divide(6));
		btnForward.setOnAction(e -> {
			player.seek(player.getCurrentTime().add(Duration.seconds(2)));
		});

		btnBackward.prefWidthProperty().bind(scene.widthProperty().divide(6));
		btnBackward.setOnAction(e -> {
			player.seek(player.getCurrentTime().add(Duration.seconds(-2)));
		});

		player.setOnEndOfMedia(() -> {
			vidStage.close();	//Auto close window when video finishes playing
		});

		buttonBox.setSpacing(3);

		vidPane.setCenter(mediaView);
		DoubleProperty mvw = mediaView.fitWidthProperty();
		DoubleProperty mvh = mediaView.fitHeightProperty();
		mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
		mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height").subtract(50));
		mediaView.setPreserveRatio(true);

		vidStage.setScene(scene);
		vidStage.show();
	}
}
