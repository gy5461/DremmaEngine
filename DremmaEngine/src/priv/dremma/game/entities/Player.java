package priv.dremma.game.entities;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Time;

public class Player extends Entity {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_JUMPING = 1;

	public static final float SPEED = 3f;
	public static final float GRAVITY = 0.002f;

	private int floorY;
	private int state;

	public Player(Animator animator) {
		super(animator);
		state = STATE_NORMAL;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setFloorY(int floorY) {
		this.floorY = floorY;
		this.getPosition().y = floorY;
	}

	public void jump() {
		this.getSpeed().y = -1;
		state = STATE_JUMPING;
	}

	public void update() {
		if (this.getState() == STATE_JUMPING) {
			this.getSpeed().y += GRAVITY * Time.deltaTime;
		}

		super.update();

		if (this.getState() == STATE_JUMPING && this.getPosition().y >= floorY) {
			this.getSpeed().y = 0;
			this.getPosition().y = floorY;
			this.setState(STATE_NORMAL);
		}
	}

}
