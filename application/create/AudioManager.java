package application.create;

import java.io.IOException;

import javafx.collections.ObservableList;

public class AudioManager {
	
	private final String NOMUSIC = "None";
	public void combineAudio(String music, ObservableList<String> listLines){
		String cmd;
		// Combine list of audio files into in one if there are multiple, otherwise rename the one audio file
		if (listLines.size() == 1) {
			cmd = "mv ./AudioFiles/AudioFile1.wav ./AudioFiles/audio.wav";
		} else {
			cmd = "ffmpeg";
			for (String s: listLines) {
				cmd += " -i \"./AudioFiles/" + s + ".wav\"";
			}

			cmd += " -filter_complex \"";
			for (int i = 0; i < listLines.size(); i++) {
				cmd += "[" + i + ":0]";
			}
			cmd += "concat=n=" + listLines.size() + ":v=0:a=1[out]\" -map \"[out]\" ./AudioFiles/" + "audio" + ".wav &>/dev/null";
		}

		if(music == NOMUSIC) {
			cmd += "; mv ./AudioFiles/audio.wav ./AudioFiles/temp.wav";
		}else {
			cmd += "; ffmpeg -i ./AudioFiles/audio.wav -i ./.resources/Music/" + music + ".mp3 -filter_complex amix=inputs=2:duration=shortest ./AudioFiles/temp.wav";
		}

		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
