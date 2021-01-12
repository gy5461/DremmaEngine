package priv.dremma.game.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import priv.dremma.game.Game;
import priv.dremma.game.util.Res;

public class AudioManager {
	
	private static AudioManager instance;
	public static AudioManager getInstance() {
		if(instance == null) {
			instance = new AudioManager();
		}
		return instance;
	}

	AudioInputStream audioIn;
	private HashMap<String, Clip> clips = new HashMap<String, Clip>();
	Thread thread;
	
	public void initAudio(String name) {
		try {
			File file = new File(Res.res.get(name));
			audioIn = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			clips.put(name, AudioSystem.getClip());
		} catch (LineUnavailableException e) {

			e.printStackTrace();
		}
		try {
			clips.get(name).open(audioIn);
		} catch (LineUnavailableException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playOnce(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).start();
			}
		}, Game.name + "_playOnce");
		thread.start();
	}

	public void playLoop(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).loop(Clip.LOOP_CONTINUOUSLY);
			}
		}, Game.name + "_playLoop");
		thread.start();
	}
}
