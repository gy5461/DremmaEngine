package priv.dremma.game.anim;

import java.util.HashMap;

public class Animator {
	HashMap<String, Animation> animations = new HashMap<String, Animation>();
	public String state;
	
	public void addAnimation(String state, Animation animation) {
		animations.put(state, animation);
	}
	
	public Animation getAnimation(String state) {
		return animations.get(state);
	}
	
//	public void playOnce(String state) {
//		this.state = state;
//		animations.get(state).update();
//	}
	
	public void playLoop(String state) {
		this.state = state;
		animations.get(state).update();
	}

}
