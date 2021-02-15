package priv.dremma.game.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Resources;

/**
 * ��Ƶ���Ź�����
 * 
 * @author guoyi
 *
 */
public class AudioManager {

	private static AudioManager instance;

	public static AudioManager getInstance() {
		if (instance == null) {
			instance = new AudioManager();
		}
		return instance;
	}

	private AudioManager() {
	}

	AudioInputStream audioIn;
	private HashMap<String, Clip> clips = new HashMap<String, Clip>(); // ������
	Thread thread;

	/**
	 * ������Դ��ʼ��
	 * 
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
	 * ����һ��
	 * 
	 * @param name ������
	 */
	public synchronized void playOnce(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).loop(1);
			}
		}, GameCore.name + "_playOnce");
		thread.start();
	}

	/**
	 * ѭ������
	 * 
	 * @param name
	 */
	public synchronized void playLoop(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				clips.get(name).loop(Clip.LOOP_CONTINUOUSLY);
			}
		}, GameCore.name + "_playLoop");
		thread.start();
	}

	/**
	 * ֹͣ����
	 * 
	 * @param name
	 */
	public void stopPlay(String name) {
		thread = new Thread(new Thread() {
			public void run() {
				if (clips.get(name).isRunning()) {
					clips.get(name).stop();
				}
			}
		}, GameCore.name + "_stopPlay");
		thread.start();
	}

	/**
	 * �����ض����Ƶ���������
	 * 
	 * @param name
	 * @param volumn 0��100����ʾ�����ٷ���
	 */
	public void setVolumn(String name, int volumn) {
		thread = new Thread(new Thread() {
			public void run() {
				FloatControl gainControl = (FloatControl) clips.get(name).getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(gainControl.getMinimum()
						+ (gainControl.getMaximum() - gainControl.getMinimum()) * 0.01f * volumn);
			}
		}, GameCore.name + "_setVolumn");
		thread.start();
	}
}
