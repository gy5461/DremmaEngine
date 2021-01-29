package priv.dremma.game.anim;

import java.awt.Image;
import java.util.ArrayList;

import priv.dremma.game.util.Time;

/**
 * Animation����һϵ��ͼ��֡����ÿ����ʾ����ʱ����
 * 
 * @author guoyi
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Animation {

	private ArrayList<AnimFrame> frames; // �����ؼ�֡����
	private int curFrameIndex; // ��ǰ֡�ڹؼ�֡�����е��±�
	private float curAnimTime; // ��ǰ��������ʱ��

	private float totalDuration; // ������ʱ��

	/**
	 * �����µ�Animation
	 */
	public Animation() {
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0f;
		start();
	}
	
	/**
	 * ���������µ�Animation
	 */
	public Animation(Animation animation) {
		frames = new ArrayList();
		for(AnimFrame a : animation.frames) {
			this.addFrame(a.image, a.endTime);
		}
		this.curFrameIndex = animation.curFrameIndex;
		this.curAnimTime = animation.curAnimTime;
		this.totalDuration = animation.totalDuration;
		start();
	}

	/**
	 * ��ָ����ʾʱ���ͼ��ӽ�������
	 * 
	 * @param image    ͼ��
	 * @param duration ָ����ʾʱ��
	 */
	public synchronized void addFrame(Image image, float duration) {
		totalDuration += duration;
		frames.add(new AnimFrame(image, totalDuration));
	}

	/**
	 * ��ͷ��ʼ������
	 */
	public synchronized void start() {
		curAnimTime = 0;
		curFrameIndex = 0;
	}

	/**
	 * ���ݹ�ȥ��ʱ�䳤�ȸ��¶���
	 * 
	 * @param elapsedTime
	 */
	public synchronized void update() {
		if (frames.size() > 1) {
			curAnimTime += Time.deltaTime;

			if (curAnimTime >= totalDuration) {
				// ��������β����ͷ��ʼ
				curAnimTime = curAnimTime % totalDuration;
				curFrameIndex = 0;
			}

			//Debug.log(Debug.DebugLevel.SERVERE, "curFrameIndex:"+curFrameIndex+ "    EndTime:"+getFrame(curFrameIndex).endTime);
			while (curAnimTime > getFrame(curFrameIndex).endTime) {
				curFrameIndex++;
			}
		}
	}

	/**
	 * ȡ�����Animation�ĵ�ǰͼ�����û��ͼ���򷵻�null
	 * 
	 * @return
	 */
	public synchronized Image getImage() {
		if (frames.size() == 0) {
			return null;
		} else {
			return getFrame(curFrameIndex).image;
		}
	}

	private AnimFrame getFrame(int i) {
		return (AnimFrame) frames.get(i);
	}

	private class AnimFrame {
		Image image;
		float endTime;

		public AnimFrame(Image image, float endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}

}
