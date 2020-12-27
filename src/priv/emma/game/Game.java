package priv.emma.game;

import java.awt.Canvas;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable{
	
	public JFrame frame;	//��Ϸ����
	
	public Thread thread;	//��Ϸ�߳�
	public boolean isRunning;	//��Ϸ�Ƿ���������
	
	
	public static final String NAME = "DremmaEngine";	//����
	public static final int WIDTH = 160;	//������
	public static final int HEIGHT = WIDTH / 12 * 9;	//����߶�

	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this, NAME + "_main");
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		
		try {
			thread.join();	//�ȴ��߳�thread���н���
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(isRunning) {
			System.out.println("Game is running...");
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
