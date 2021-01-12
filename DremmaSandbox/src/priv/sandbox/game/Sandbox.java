package priv.sandbox.game;

import priv.dremma.game.Game;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.util.Res;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	
	public void onStart() {
		this.setName("SandBox");
		
		Res.loadRes(Res.ResType.Music, "backgroundMusic", "res/vow to virtue.wav");
		Res.loadRes(Res.ResType.Music, "moneySound", "res/money.wav");
	}

	public void onUpdate() {
		AudioManager.getInstance().playOnce("moneySound");
		AudioManager.getInstance().playLoop("backgroundMusic");
	}

}