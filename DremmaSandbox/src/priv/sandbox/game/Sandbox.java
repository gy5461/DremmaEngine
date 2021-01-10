package priv.sandbox.game;

import java.awt.Color;

import priv.dremma.game.Game;

@SuppressWarnings("serial")
public class Sandbox extends Game {
	public void onStart() {
		this.setName("SandBox");
	}

	public void onUpdate() {
		if (Game.g != null) {
			// 渲染一个蓝色的长方形
			g.setColor(Color.blue);
			g.fillRect(200, 100, 500, 200);
			
			// 渲染一个绿色的长方形
			g.setColor(Color.green);
			g.fillRect(300, 250, 100, 100);
		}
	}

}