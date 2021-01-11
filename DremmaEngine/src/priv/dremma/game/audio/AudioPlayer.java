package priv.dremma.game.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	AudioInputStream audioIn;
	private Clip clip;
	Thread thread;

	public AudioPlayer(String path) {
		try {
			File file = new File(path);
			audioIn = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {

			e.printStackTrace();
		}
		try {
			clip.open(audioIn);
		} catch (LineUnavailableException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void playOnce() {
		thread = new Thread(new Thread() {
			public void run() {
				clip.start();
			}
		}, clip.toString());
		thread.start();
	}

	public void playLoop() {
		thread = new Thread(new Thread() {
			public void run() {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}, clip.toString());
		thread.start();
	}
}
