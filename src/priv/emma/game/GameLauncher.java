package priv.emma.game;

import java.applet.Applet;

import javax.swing.JFrame;

/**
 * 游戏加载类，可生成Applet小程序
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class GameLauncher extends Applet{
	
	private static Game game = new Game();
	
	public void start() {
		game.start();
		game.setFocusable(true);
	}
	
	public void stop() {
		game.stop();
	}
	
	public static void main(String[] args) {
		game.window = new JFrame(Game.NAME);	//新建游戏窗体
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//设置默认情况下关闭窗体时游戏退出
		game.window.setResizable(false);	//默认窗体不可改变大小
		game.window.setLocationRelativeTo(null);	//使窗体置于屏幕中央
		game.window.setVisible(true);	//设置窗体可见
		game.setFocusable(true);	//默认聚焦游戏
		
		game.start();	//开始游戏
	}
	
}
