package priv.dremma.game.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Resources;
/**
 * 音频播放管理类
 * @author guoyi
 *
 */
public class AudioManager {
	
	private static AudioManager instance;
	public static AudioManager getInstance() {
		if(instance == null) {
			instance = new AudioManager();
		}
		return instance;
	}
	
	private AudioManager() {}

	AudioInputStream audioIn;
	private HashMap<String, Clip> clips = new HashMap<String, Clip>();	//声音表
	Thread thread;
	
	/**
	 * 音乐资源初始化
	 * @param name
	 */
	public void initAudio(String name) {
		try {
			File file = new File(Resources.res.get(name));
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

	/**
	 * 播放一次
	 * @param name 音乐名
	 */
	public void playOnce(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).start();
			}
		}, GameCore.name + "_playOnce");
		thread.start();
	}

	/**
	 * 循环播放
	 * @param name
	 */
	public void playLoop(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).loop(Clip.LOOP_CONTINUOUSLY);
			}
		}, GameCore.name + "_playLoop");
		thread.start();
	}
	
	/**
	 * 停止播放
	 * @param name
	 */
	public void stopPlay(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				if(clips.get(name).isRunning()) {
					clips.get(name).stop();
				}
			}
		}, GameCore.name + "_stopPlay");
		thread.start();
	}
}
