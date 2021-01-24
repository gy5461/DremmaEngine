package priv.dremma.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.util.Resources;

/**
 * ��Ϸ�����࣬������AppletС����
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class GameLauncher extends Applet {

	public static GameCore game;

	public void onStart() {
		game = new GameCore();
	}

	@Override
	public void start() {
		onStart();
		setLayout(new BorderLayout());
		this.add(game, BorderLayout.CENTER);
		this.setSize(GameCore.DIMENSIONS);
		GameCore.debug = true;
		game.isApplet = true;
		Resources.path = "../res/";

		game.start();
		game.setFocusable(true);
	}

	@Override
	public void stop() {
		game.stop();
	}

	public static void lauchToApplication() {
		GameCore.debug = true; // ����debugģʽ
		game.isApplet = false;
		Resources.path = "res/";

		game.window = new JFrame(GameCore.name); // �½���Ϸ����
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����Ĭ������¹رմ����˳�����

		game.window.setLayout(new BorderLayout()); // ���ò��ֹ�����

		game.window.add(game, BorderLayout.CENTER); // ���ֵ���Ļ����
		game.window.setSize(GameCore.DIMENSIONS); // ���ô��ڴ�С

		game.window.setResizable(false); // Ĭ�ϴ��岻�ɸı��С
		game.window.setLocationRelativeTo(null); // ʹ��������ڿ����
		game.window.setVisible(true); // ���ô���ɼ�
		game.setFocusable(true); // Ĭ�Ͼ۽���Ϸ
		game.windowInputHandler = new WindowInputHandler(game); // ֻ��Applicationӵ�д����¼�

		game.start(); // ��ʼ��Ϸ
	}

	public static void main(String[] args) {
		game = new GameCore(); // ����main�����Ǿ�̬����������Ҫ��game��Ϊ��̬����
		lauchToApplication();
	}
}
