package application.create;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * This class handles the visual aspect of the creation process and makes the creation
 * @author Jacinta
 *
 */
public class VideoMaker {

	/**
	 * Creates a video consisting of pictures and the term as text and combines audio to make creation
	 * @param term	term that was searched for
	 * @param name	name for the video to be saved as
	 * @param numberOfPictures number of images to be included
	 */
	public void makeVideo(String term, String name, int numberOfPictures) {
		
		String cmd;
		File file = new File("./AudioFiles/temp.wav");
		AudioInputStream audioInputStream = null;
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		AudioFormat format = audioInputStream.getFormat();
		
		//Calculate the duration the video needs to be based on audio length
		long audioFileLength = file.length();
		int frameSize = format.getFrameSize();
		float frameRate = format.getFrameRate();
		float durationInSeconds = (audioFileLength / (frameSize * frameRate));

		//Create video with images and text, combine with audio, delete supporting files
		cmd = "rm -f out.mp4; mkdir -p Quizzes ; cat \"." + term + "\"?.jpg | ffmpeg -f image2pipe -framerate $((" + numberOfPictures + "))/"
				+ durationInSeconds + " -i - -c:v libx264 -pix_fmt yuv420p -vf \""
				+ "scale=w=1280:h=720:force_original_aspect_ratio=1,pad=1280:720:(ow-iw)/2:(oh-ih)/2\""
				+ " -r 25 -y \'./Quizzes/" + term + ".mp4\' ; rm \"" + term + "\"?.jpg ; ffmpeg -i "
				+ "\'./Quizzes/" + term + ".mp4\' -vf "
				+ "\"drawtext=fontsize=50:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)"
				+ "/2:borderw=5:text=\'" + term + "\'\" out.mp4 ; ffmpeg -i out.mp4 -i"
				+ " \'./AudioFiles/" + "temp" + ".wav\' -c:v copy -c:a aac -strict experimental"
				+ " -y \'./Creations/" + name + ".mp4\' &>/dev/null ; rm out.mp4";

		ProcessBuilder builderr = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process vidProcess = builderr.start();
			vidProcess.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
