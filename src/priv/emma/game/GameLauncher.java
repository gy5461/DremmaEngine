package priv.emma.game;

import java.applet.Applet;

import javax.swing.JFrame;


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
		game.frame = new JFrame(Game.NAME);	//�½���Ϸ����
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//����Ĭ������¹رմ���ʱ��Ϸ�˳�
		game.frame.setResizable(false);	//Ĭ�ϴ��岻�ɸı��С
		game.frame.setLocationRelativeTo(null);	//ʹ����������Ļ����
		game.frame.setVisible(true);	//���ô���ɼ�
		game.setFocusable(true);	//Ĭ�Ͼ۽���Ϸ
		
		game.start();	//��ʼ��Ϸ
	}
	
}
