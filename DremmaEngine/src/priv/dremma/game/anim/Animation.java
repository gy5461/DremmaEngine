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
public class Animation {

	private ArrayList<AnimFrame> frames; // �����ؼ�֡����
	private int curFrameIndex; // ��ǰ֡�ڹؼ�֡�����е��±�
	public float curAnimTime; // ��ǰ��������ʱ��

	public float totalDuration; // ������ʱ��
	
	public boolean hasExitTime;
	public boolean isStatic;
	private Image staticImage;

	/**
	 * �����µ�Animation
	 */
	public Animation() {
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0f;
		this.hasExitTime = false;
		this.isStatic = false;
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
	
	public void setStaticImage(Image image) {
		this.staticImage = image;
		this.isStatic = true;
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
		if(this.isStatic) {
			return;
		}
		
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
		if(this.isStatic) {
			return staticImage;
		}
		
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
