package priv.dremma.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * 游戏加载类，可生成Applet小程序
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class GameLauncher extends Applet {

	public static Game game;

	public void onStart() {
		game = new Game();
	}
	
	@Override
	public void start() {
		onStart();
		setLayout(new BorderLayout());
		this.add(game, BorderLayout.CENTER);
		this.setSize(Game.DIMENSIONS);
		Game.debug = true;
		game.isApplet = true;
		
		game.start();
		game.setFocusable(true);
	}

	@Override
	public void stop() {
		game.stop();
	}

	public static void lauchToApplication() {
		game.setSize(Game.DIMENSIONS); // 设置游戏窗口大小
		Game.debug = true;
		game.isApplet = false;

		game.window = new JFrame(Game.name); // 新建游戏窗体
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认情况下关闭窗体时游戏退出

		game.window.setLayout(new BorderLayout());

		game.window.add(game, BorderLayout.CENTER);
		game.window.pack();

		game.window.setResizable(false); // 默认窗体不可改变大小
		game.window.setLocationRelativeTo(null); // 使窗体置于屏幕中央
		game.window.setVisible(true); // 设置窗体可见
		game.setFocusable(true); // 默认聚焦游戏

		game.start(); // 开始游戏
	}

	public static void main(String[] args) {
		game = new Game();
		lauchToApplication();
	}
}
