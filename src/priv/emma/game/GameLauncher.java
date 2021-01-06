package priv.emma.game;

import java.applet.Applet;

import javax.swing.JFrame;

/**
 * ��Ϸ�����࣬������AppletС����
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
		game.window = new JFrame(Game.NAME);	//�½���Ϸ����
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//����Ĭ������¹رմ���ʱ��Ϸ�˳�
		game.window.setResizable(false);	//Ĭ�ϴ��岻�ɸı��С
		game.window.setLocationRelativeTo(null);	//ʹ����������Ļ����
		game.window.setVisible(true);	//���ô���ɼ�
		game.setFocusable(true);	//Ĭ�Ͼ۽���Ϸ
		
		game.start();	//��ʼ��Ϸ
	}
	
}
