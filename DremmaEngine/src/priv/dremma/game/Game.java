package priv.dremma.game;

import java.awt.Canvas;

import javax.swing.JFrame;

import priv.dremma.game.util.Debug;

/**
 * 游戏主体类，包含游戏窗体、渲染等
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	public JFrame window; // 游戏窗体

	public Thread thread; // 游戏线程
	public boolean isRunning; // 游戏是否正在运行

	public static String name = "DremmaEngine"; // 名称
	public static int width = 160; // 窗体宽度
	public static int height = width / 12 * 9; // 窗体高度
	
	public static boolean debug = true;	// 游戏引擎默认为Debug模式
	
	public void onStart() {
	}
	
	public void onUpdate() {
	}
	
	public void onDestroy() {
	}

	public synchronized void start() {
		onStart();
		isRunning = true;
		thread = new Thread(this, name + "_main");
		thread.start();
	}

	public synchronized void stop() {
		onDestroy();
		isRunning = false;

		try {
			thread.join(); // 等待线程thread运行结束
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int frames = 0; // 游戏帧数
		long lastTimer = System.currentTimeMillis(); // 记录上一帧的时间，单位为毫秒

		long lastnsTime = System.nanoTime(); // 记录上一帧纳秒数
		// 每秒1e9纳秒，每秒渲染60帧画面是人眼上限，指在每秒渲染60帧画面的前提下，每帧需要多少纳秒
		final double NSPERFRAME = 1000000000.0 / 60.0;
		double deltaFrame = 0; // 纳秒数的变化除以nsPerFrame，即变化的帧数
		long curnsTime; // 记录当前纳秒数
		// 如果需要测试机器极限，建议将shouldRender始终设置为true
		boolean shouldRender; // 标记是否需要渲染，当deltaFrame>=1时，是渲染的时机

		while (isRunning) {

			curnsTime = System.nanoTime();
			deltaFrame += (curnsTime - lastnsTime) / NSPERFRAME;
			lastnsTime = curnsTime;

			// 变化的纳秒数在满足人眼上限的前提下值得渲染一帧
			if (deltaFrame >= 1.0) {
				deltaFrame--;
				
				shouldRender = true;
			} else {
				shouldRender = false;
			}
			
			// 线程休息两毫秒
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				onUpdate();
			}

			if (System.currentTimeMillis() - lastTimer > 1000) {
				// 游戏每过1秒，打印帧数（帧数指一秒内游戏渲染画面的张数）
				Debug.log(Debug.DebugLevel.INFO, "Game Frames :" + frames);
				lastTimer += 1000;
				frames = 0;
			}

		}
	}
}
