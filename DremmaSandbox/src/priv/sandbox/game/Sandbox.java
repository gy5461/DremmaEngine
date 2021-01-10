package priv.sandbox.game;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

@SuppressWarnings("serial")
public class Sandbox extends Game{
	public void onStart() {
		Game.name = "Sandbox";
	}
	
	public void onUpdate() {
		Debug.log(Debug.DebugLevel.WARNING, "warning, this is sandbox!");
	}

}
