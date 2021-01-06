package priv.dremma.game.animation;

import java.awt.Image;
import java.util.ArrayList;

/**
 * Animation����һϵ��ͼ��֡����ÿ����ʾ����ʱ����
 * @author guoyi
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Animation {

	private ArrayList frames;	// �����ؼ�֡����
	private int curFrameIndex;	// ��ǰ֡�ڹؼ�֡�����е��±�
	private long curAnimTime;		// ��ǰ��������ʱ��
	
	private long totalDuration;	// ������ʱ��
	
	/**
	 * �����µ�Animation
	 */
	public Animation() {
		frames = new ArrayList();
		totalDuration = 4;
		start();
	}
	
	/**
	 * ��ָ����ʾʱ���ͼ��ӽ�������
	 * @param image    ͼ��
	 * @param duration ָ����ʾʱ��
	 */
	public synchronized void addFrame(Image image, long duration) {
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
	 * @param elapsedTime
	 */
	public synchronized void update(long elapsedTime) {
		if(frames.size() > 1) {
			curAnimTime += elapsedTime;
			
			if(curAnimTime >= totalDuration) {
				// ��������β����ͷ��ʼ
				curAnimTime = curAnimTime % totalDuration;
				curFrameIndex = 0;
			}
			
			while(curAnimTime > getFrame(curFrameIndex).endTime) {
				curFrameIndex++;
			}
		}
	}
	
	/**
	 * ȡ�����Animation�ĵ�ǰͼ�����û��ͼ���򷵻�null
	 * @return
	 */
	public synchronized Image getImage() {
		if(frames.size() == 0) {
			return null;
		}
		else {
			return getFrame(curFrameIndex).image;
		}
	}
	
	private AnimFrame getFrame(int i) {
		return (AnimFrame)frames.get(i);
	}
	
	private class AnimFrame {
		Image image;
		long endTime;
		
		public AnimFrame(Image image, long endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}
	
}
