package priv.sandbox.game;

import priv.dremma.game.GameLauncher;

@SuppressWarnings("serial")
public class SandboxLaucher extends GameLauncher {
	public void onStart() {
		GameLauncher.game = new Sandbox();
	}
	
	public static void main(String[] args) {
		GameLauncher.game = new Sandbox();
		lauchToApplication();
	}
}
