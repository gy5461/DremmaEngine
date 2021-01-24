package priv.dremma.game.util;

import priv.dremma.game.GameCore;

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
	public static final float NSPERFRAME = 1000000000.0f / 60.0f;
	public static float deltaFrame = 0; // �������ı仯����nsPerFrame�����仯��֡��
	public static float deltaTime = 0; // ÿ֡����ʱ����λΪ��
	public static float elapsedTime = 0; // ��ȥ��ʱ��
	public static long curnsTime; // ��¼��ǰ������
	// �����Ҫ���Ի������ޣ����齫shouldRenderʼ������Ϊtrue
	public static boolean shouldRender; // ����Ƿ���Ҫ��Ⱦ����deltaFrame>=1ʱ������Ⱦ��ʱ��

	public static void printFrames() {
		if (System.currentTimeMillis() - Time.lastTime > 1000) {
			// ��Ϸÿ��1�룬��ӡ֡����֡��ָһ������Ϸ��Ⱦ�����������
			Debug.log(Debug.DebugLevel.INFO, "Game Frames :" + GameCore.frames);
			Time.lastTime += 1000;
			GameCore.frames = 0;
		}
	}

	public static void update() {
		Time.curnsTime = System.nanoTime();
		Time.deltaFrame += (Time.curnsTime - Time.lastnsTime) / Time.NSPERFRAME;
		Time.lastnsTime = Time.curnsTime;

		// �仯���������������������޵�ǰ����ֵ����Ⱦһ֡
		if (FloatCompare.isBiggerOrEqual(Time.deltaFrame, 1.0f)) {
			Time.deltaFrame--;
			Time.deltaTime = (Time.curnsTime - Time.lastFramensTime) / 1e9f;
			elapsedTime += Time.deltaTime;
			Time.lastFramensTime = Time.curnsTime;
			Time.shouldRender = true;
		} else {
			Time.shouldRender = false;
		}

		printFrames(); // ��ӡ֡��
	}
}
