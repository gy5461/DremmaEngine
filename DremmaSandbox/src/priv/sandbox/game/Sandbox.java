package priv.sandbox.game;


import java.util.Timer;
import java.util.TimerTask;

import priv.dremma.game.Game;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.util.Resources;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	
	int state = 0;
	
	public void onStart() {
		this.setName("SandBox");
		
		Resources.load(Resources.ResourceType.Music, "backgroundMusic", "res/music/background.wav");
		Resources.load(Resources.ResourceType.Music, "moneySound", "res/music/money.wav");
		Resources.load(Resources.ResourceType.Music, "electricCurrentSound", "res/music/electricCurrent.wav");
		Resources.load(Resources.ResourceType.Music, "walkSound", "res/music/walk.wav");
	}

	public void onUpdate() {
		
		AudioManager.getInstance().playOnce("moneySound");
		AudioManager.getInstance().playOnce("backgroundMusic");
		AudioManager.getInstance().playOnce("electricCurrentSound");
		if(state==0)
			AudioManager.getInstance().playLoop("walkSound");
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				state = 1;
				AudioManager.getInstance().stopPlay("walkSound");
			}
		}, 2000);
		
	}

}