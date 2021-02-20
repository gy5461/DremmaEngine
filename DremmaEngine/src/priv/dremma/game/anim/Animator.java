package priv.dremma.game.anim;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import priv.dremma.game.util.Debug;

public class Animator {
	public HashMap<String, Animation> animations = new HashMap<String, Animation>();
	private String state;

	public Animator() {

	}

	public void addAnimation(String state, Animation animation) {
		animations.put(state, animation);
	}

	public Animation getAnimation(String state) {
		return animations.get(state);
	}

	/**
	 * 设置动画状态
	 * 
	 * @param state       转到的目的动画状态
	 * @param hasExitTime 目的动画状态有无退出时间
	 */
	public synchronized void setState(String destState, boolean hasExitTime) {
		if (this.animations.get(destState) == null) {
			Debug.log(Debug.DebugLevel.SERVERE, "目的动画状态" + destState + "不存在");
			return;
		}
		if (this.state == null) {
			this.state = destState;
			this.animations.get(state).hasExitTime = hasExitTime;
			return;
		}
		if (this.state.equals(destState)) {			
			return;
		}

		if (this.animations.get(this.state).hasExitTime) {
			// 先播放完再转换
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					state = destState;
					animations.get(destState).hasExitTime = hasExitTime;
				}
			}, Math.round((this.animations.get(this.state).totalDuration-this.animations.get(this.state).curAnimTime) * 1000));
			return;
		}
		this.state = destState;
		this.animations.get(state).hasExitTime = hasExitTime;
	}

	public String getState() {
		return this.state;
	}

	public void update() {
		if (this.animations.get(this.state) != null) {
			animations.get(state).update();
		}
	}

}