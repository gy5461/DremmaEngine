package priv.emma.game;

import java.awt.Canvas;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable{
	
	public JFrame frame;	//游戏窗体
	
	public Thread thread;	//游戏线程
	public boolean isRunning;	//游戏是否正在运行
	
	
	public static final String NAME = "DremmaEngine";	//名称
	public static final int WIDTH = 160;	//窗体宽度
	public static final int HEIGHT = WIDTH / 12 * 9;	//窗体高度

	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this, NAME + "_main");
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		
		try {
			thread.join();	//等待线程thread运行结束
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
