package priv.dremma.game.util;

import priv.dremma.game.Game;

/**
 * ʱ����
 * 
 * @author guoyi
 *
 */
public class Time {

	public static long lastTime = System.currentTimeMillis(); // ��¼��һ���ʱ�䣬��λΪ����
	public static long lastnsTime = System.nanoTime(); // ��¼��һ��ѭ��������
	public static long lastFramensTime = System.nanoTime(); // ��¼��һ֡��������
	// ÿ��1e9���룬ÿ����Ⱦ60֡�������������ޣ�ָ��ÿ����Ⱦ60֡�����ǰ���£�ÿ֡��Ҫ��������
	public static final double NSPERFRAME = 1000000000.0 / 60.0;
	public static double deltaFrame = 0; // �������ı仯����nsPerFrame�����仯��֡��
	public static double deltaTime = 0; // ÿ֡����ʱ����λΪ��
	public static double elapsedTime = 0; // ��ȥ��ʱ��
	public static long curnsTime; // ��¼��ǰ������
	// �����Ҫ���Ի������ޣ����齫shouldRenderʼ������Ϊtrue
	public static boolean shouldRender; // ����Ƿ���Ҫ��Ⱦ����deltaFrame>=1ʱ������Ⱦ��ʱ��

	public static void printFrames() {
		if (System.currentTimeMillis() - Time.lastTime > 1000) {
			// ��Ϸÿ��1�룬��ӡ֡����֡��ָһ������Ϸ��Ⱦ�����������
			Debug.log(Debug.DebugLevel.INFO, "Game Frames :" + Game.frames);
			Time.lastTime += 1000;
			Game.frames = 0;
		}
	}

	public static void update() {
		Time.curnsTime = System.nanoTime();
		Time.deltaFrame += (Time.curnsTime - Time.lastnsTime) / Time.NSPERFRAME;
		Time.lastnsTime = Time.curnsTime;

		// �仯���������������������޵�ǰ����ֵ����Ⱦһ֡
		if (Time.deltaFrame >= 1.0) {
			Time.deltaFrame--;
			Time.deltaTime = (Time.curnsTime - Time.lastFramensTime) / 1e9;
			elapsedTime += Time.deltaTime;
			Time.lastFramensTime = Time.curnsTime;
			Time.shouldRender = true;
		} else {
			Time.shouldRender = false;
		}

		printFrames(); // ��ӡ֡��
		
		// �߳���Ϣ������
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
