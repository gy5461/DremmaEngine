package priv.sandbox.game;

import priv.dremma.game.Game;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.util.Resources;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	
	public void onStart() {
		this.setName("SandBox");
		
		Resources.load(Resources.ResourceType.Music, "backgroundMusic", "res/music/background.wav");
		Resources.load(Resources.ResourceType.Music, "moneySound", "res/music/money.wav");
	}

	public void onUpdate() {
		AudioManager.getInstance().playOnce("moneySound");
		AudioManager.getInstance().playLoop("backgroundMusic");
	}

}