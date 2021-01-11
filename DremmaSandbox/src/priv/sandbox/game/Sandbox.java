package priv.sandbox.game;

import priv.dremma.game.Game;
import priv.dremma.game.audio.AudioPlayer;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	
	AudioPlayer backgroundMusic;
	AudioPlayer moneySound;
	public void onStart() {
		this.setName("SandBox");
		
		backgroundMusic = new  AudioPlayer("res/vow to virtue.wav");
		moneySound = new AudioPlayer("res/money.wav");
	}

	public void onUpdate() {
		this.backgroundMusic.playLoop();
		this.moneySound.playOnce();
	}

}