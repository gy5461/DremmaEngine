package priv.emma.game.animation;

import java.awt.Image;
import java.util.ArrayList;

/**
 * Animation管理一系列图像（帧）和每个显示器的时间量
 * @author guoyi
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Animation {

	private ArrayList frames;	// 动画关键帧数组
	private int curFrameIndex;	// 当前帧在关键帧数组中的下标
	private long curAnimTime;		// 当前动画播放时长
	
	private long totalDuration;	// 动画总时长
	
	/**
	 * 构造新的Animation
	 */
	public Animation() {
		frames = new ArrayList();
		totalDuration = 4;
		start();
	}
	
	/**
	 * 将指定显示时间的图像加进动画中
	 * @param image    图像
	 * @param duration 指定显示时间
	 */
	public synchronized void addFrame(Image image, long duration) {
		totalDuration += duration;
		frames.add(new AnimFrame(image, totalDuration));
	}
	
	/**
	 * 从头开始本动画
	 */
	public synchronized void start() {
		curAnimTime = 0;
		curFrameIndex = 0;
	}
	
	/**
	 * 根据过去的时间长度更新动画
	 * @param elapsedTime
	 */
	public synchronized void update(long elapsedTime) {
		if(frames.size() > 1) {
			curAnimTime += elapsedTime;
			
			if(curAnimTime >= totalDuration) {
				// 动画到结尾，从头开始
				curAnimTime = curAnimTime % totalDuration;
				curFrameIndex = 0;
			}
			
			while(curAnimTime > getFrame(curFrameIndex).endTime) {
				curFrameIndex++;
			}
		}
	}
	
	/**
	 * 取得这个Animation的当前图像，如果没有图像，则返回null
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
