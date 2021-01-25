package priv.dremma.game.entities;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class Player extends Entity {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_JUMPING = 1;
	
	public static float SPEED = 60f;
	public static final float GRAVITY = 0.002f;

	public boolean isMoved;

	KeyInputHandler keyInputHandler;

	private int floorY;
	private int state;

	public Player(Animator animator, KeyInputHandler keyInputHandler) {
		super(animator);
		state = STATE_NORMAL;
		this.keyInputHandler = keyInputHandler;
		this.isMoved = false;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setFloorY(int floorY) {
		this.floorY = floorY;
		this.position.y = floorY;
	}

	public void jump() {
		this.speed.y = -1;
		state = STATE_JUMPING;
	}

	public synchronized void update() {
		
		if (GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2DOT5) {
			this.isMoved = false;
			if (this.keyInputHandler.up.isPressed()) {
				this.animator.state = "playerRunUp";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(-1, -1));
				isMoved = true;
			} else if (this.keyInputHandler.down.isPressed()) {
				this.animator.state = "playerRunDown";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(1, 1));
				isMoved = true;
			} else if (this.keyInputHandler.left.isPressed()) {
				this.animator.state = "playerRunLeft";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(-1, 1));
				isMoved = true;
			} else if (this.keyInputHandler.right.isPressed()) {
				this.animator.state = "playerRunRight";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(1, -1));
				isMoved = true;
			}
			
			if (!isMoved) {
				if (FloatCompare.isBigger(this.speed.x , 0) && FloatCompare.isBigger(this.speed.y , 0)) {
					this.animator.state = "playerStandDown";
				}
				if (FloatCompare.isBigger(this.speed.x , 0) && FloatCompare.isLess(this.speed.y , 0)) {
					this.animator.state = "playerStandRight";
				}
				if (FloatCompare.isLess(this.speed.x , 0) && FloatCompare.isBigger(this.speed.y , 0)) {
					this.animator.state = "playerStandLeft";
				}
				if (FloatCompare.isLess(this.speed.x , 0) && FloatCompare.isLess(this.speed.y , 0)) {
					this.animator.state = "playerStandUp";
				}
				AudioManager.getInstance().stopPlay("walkSound");
			} else {
				AudioManager.getInstance().playLoop("walkSound");
				this.position = this.position.add(this.speed.mul(Time.deltaTime));
			}
		} else if(GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2) {
			this.isMoved = false;
			if (this.keyInputHandler.up.isPressed()) {
				this.animator.state = "playerRunUp";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(0, -1));
				isMoved = true;
			} else if (this.keyInputHandler.down.isPressed()) {
				this.animator.state = "playerRunDown";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(0, 1));
				isMoved = true;
			} else if (this.keyInputHandler.left.isPressed()) {
				this.animator.state = "playerRunLeft";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(-1, 0));
				isMoved = true;
			} else if (this.keyInputHandler.right.isPressed()) {
				this.animator.state = "playerRunRight";
				this.speed = (new Vector2(SPEED, SPEED)).mul(new Vector2(1, 0));
				isMoved = true;
			}
			
			if (!isMoved) {
				if (FloatCompare.isEqual(this.speed.x, 0f) && FloatCompare.isBigger(this.speed.y, 0f)) {
					this.animator.state = "playerStandDown";
				}
				if (FloatCompare.isBigger(this.speed.x, 0f) && FloatCompare.isEqual(this.speed.y, 0f)) {
					this.animator.state = "playerStandRight";
				}
				if (FloatCompare.isLess(this.speed.x, 0f) && FloatCompare.isEqual(this.speed.y, 0f)) {
					this.animator.state = "playerStandLeft";
				}
				if (FloatCompare.isEqual(this.speed.x, 0f) && FloatCompare.isLess(this.speed.y, 0f)) {
					this.animator.state = "playerStandUp";
				}
				AudioManager.getInstance().stopPlay("walkSound");	//Í£Ö¹²¥·Å½Å²½Éù
			} else {
				AudioManager.getInstance().playLoop("walkSound");	//Ñ­»·²¥·Å½Å²½Éù
				this.position = this.position.add(this.speed.mul(Time.deltaTime));
			}
		}
		
		if (this.getState() == STATE_JUMPING) {
			this.speed.y += GRAVITY * Time.deltaTime;
		}

		super.update();

		if (this.getState() == STATE_JUMPING && this.position.y >= floorY) {
			this.speed.y = 0;
			this.position.y = floorY;
			this.setState(STATE_NORMAL);
		}
	}

}
