package priv.sandbox.game;

import priv.dremma.game.GameLauncher;

@SuppressWarnings("serial")
public class SandboxLauncher extends GameLauncher {
	public void onStart() {
		GameLauncher.game = new Sandbox();
	}
	
	public static void main(String[] args) {
		GameLauncher.game = new Sandbox();
		lauchToApplication();
	}
}
