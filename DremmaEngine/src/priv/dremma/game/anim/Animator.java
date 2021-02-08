package priv.dremma.game.anim;

import java.util.HashMap;
import java.util.Map.Entry;

public class Animator {
	HashMap<String, Animation> animations = new HashMap<String, Animation>();
	public String state;

	public Animator() {

	}

	/**
	 * ¿½±´¹¹Ôìº¯Êý
	 * 
	 * @param anim
	 */
	public Animator(Animator anim) {
		this.animations = new HashMap<String, Animation>();
		for (Entry<String, Animation> entry : anim.animations.entrySet()) {
			this.animations.put(entry.getKey(), new Animation(entry.getValue()));
		}
		this.state = new String(anim.state);
	}

	public void addAnimation(String state, Animation animation) {
		animations.put(state, animation);
	}

	public Animation getAnimation(String state) {
		return animations.get(state);
	}

	public void update() {
		if (this.animations.get(this.state) != null) {
			animations.get(state).update();
		}
	}

}
