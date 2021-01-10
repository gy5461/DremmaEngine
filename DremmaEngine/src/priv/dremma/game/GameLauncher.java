package priv.dremma.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * ��Ϸ�����࣬������AppletС����
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
		game.setSize(Game.DIMENSIONS); // ������Ϸ���ڴ�С
		Game.debug = true;
		game.isApplet = false;

		game.window = new JFrame(Game.name); // �½���Ϸ����
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����Ĭ������¹رմ���ʱ��Ϸ�˳�

		game.window.setLayout(new BorderLayout());

		game.window.add(game, BorderLayout.CENTER);
		game.window.pack();

		game.window.setResizable(false); // Ĭ�ϴ��岻�ɸı��С
		game.window.setLocationRelativeTo(null); // ʹ����������Ļ����
		game.window.setVisible(true); // ���ô���ɼ�
		game.setFocusable(true); // Ĭ�Ͼ۽���Ϸ

		game.start(); // ��ʼ��Ϸ
	}

	public static void main(String[] args) {
		game = new Game();
		lauchToApplication();
	}
}
