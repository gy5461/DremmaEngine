package priv.dremma.game;

import java.awt.Canvas;

import javax.swing.JFrame;

import priv.dremma.game.util.Debug;

/**
 * ��Ϸ�����࣬������Ϸ���塢��Ⱦ��
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	public JFrame window; // ��Ϸ����

	public Thread thread; // ��Ϸ�߳�
	public boolean isRunning; // ��Ϸ�Ƿ���������

	public static String name = "DremmaEngine"; // ����
	public static int width = 160; // ������
	public static int height = width / 12 * 9; // ����߶�
	
	public static boolean debug = true;	// ��Ϸ����Ĭ��ΪDebugģʽ
	
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
			thread.join(); // �ȴ��߳�thread���н���
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int frames = 0; // ��Ϸ֡��
		long lastTimer = System.currentTimeMillis(); // ��¼��һ֡��ʱ�䣬��λΪ����

		long lastnsTime = System.nanoTime(); // ��¼��һ֡������
		// ÿ��1e9���룬ÿ����Ⱦ60֡�������������ޣ�ָ��ÿ����Ⱦ60֡�����ǰ���£�ÿ֡��Ҫ��������
		final double NSPERFRAME = 1000000000.0 / 60.0;
		double deltaFrame = 0; // �������ı仯����nsPerFrame�����仯��֡��
		long curnsTime; // ��¼��ǰ������
		// �����Ҫ���Ի������ޣ����齫shouldRenderʼ������Ϊtrue
		boolean shouldRender; // ����Ƿ���Ҫ��Ⱦ����deltaFrame>=1ʱ������Ⱦ��ʱ��

		while (isRunning) {

			curnsTime = System.nanoTime();
			deltaFrame += (curnsTime - lastnsTime) / NSPERFRAME;
			lastnsTime = curnsTime;

			// �仯���������������������޵�ǰ����ֵ����Ⱦһ֡
			if (deltaFrame >= 1.0) {
				deltaFrame--;
				
				shouldRender = true;
			} else {
				shouldRender = false;
			}
			
			// �߳���Ϣ������
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
				// ��Ϸÿ��1�룬��ӡ֡����֡��ָһ������Ϸ��Ⱦ�����������
				Debug.log(Debug.DebugLevel.INFO, "Game Frames :" + frames);
				lastTimer += 1000;
				frames = 0;
			}

		}
	}
}
