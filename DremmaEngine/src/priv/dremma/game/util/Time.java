package priv.dremma.game.util;

import priv.dremma.game.Game;

/**
 * 时间类
 * 
 * @author guoyi
 *
 */
public class Time {

	public static long lastTime = System.currentTimeMillis(); // 记录上一秒的时间，单位为毫秒
	public static long lastnsTime = System.nanoTime(); // 记录上一次循环纳秒数
	public static long lastFramensTime = System.nanoTime(); // 记录上一帧的纳秒数
	// 每秒1e9纳秒，每秒渲染60帧画面是人眼上限，指在每秒渲染60帧画面的前提下，每帧需要多少纳秒
	public static final double NSPERFRAME = 1000000000.0 / 60.0;
	public static double deltaFrame = 0; // 纳秒数的变化除以nsPerFrame，即变化的帧数
	public static double deltaTime = 0; // 每帧的用时，单位为秒
	public static double elapsedTime = 0; // 过去的时间
	public static long curnsTime; // 记录当前纳秒数
	// 如果需要测试机器极限，建议将shouldRender始终设置为true
	public static boolean shouldRender; // 标记是否需要渲染，当deltaFrame>=1时，是渲染的时机

	public static void printFrames() {
		if (System.currentTimeMillis() - Time.lastTime > 1000) {
			// 游戏每过1秒，打印帧数（帧数指一秒内游戏渲染画面的张数）
			Debug.log(Debug.DebugLevel.INFO, "Game Frames :" + Game.frames);
			Time.lastTime += 1000;
			Game.frames = 0;
		}
	}

	public static void update() {
		Time.curnsTime = System.nanoTime();
		Time.deltaFrame += (Time.curnsTime - Time.lastnsTime) / Time.NSPERFRAME;
		Time.lastnsTime = Time.curnsTime;

		// 变化的纳秒数在满足人眼上限的前提下值得渲染一帧
		if (Time.deltaFrame >= 1.0) {
			Time.deltaFrame--;
			Time.deltaTime = (Time.curnsTime - Time.lastFramensTime) / 1e9;
			elapsedTime += Time.deltaTime;
			Time.lastFramensTime = Time.curnsTime;
			Time.shouldRender = true;
		} else {
			Time.shouldRender = false;
		}

		printFrames(); // 打印帧数
		
		// 线程休息两毫秒
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
