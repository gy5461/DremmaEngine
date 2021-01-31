package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

/**
 * 游戏主角类
 * @author guoyi
 *
 */
public class Player extends Entity {

	public static final int STATE_NORMAL = 0;
	public static final float GRAVITY = 0.002f;

	public boolean isMoved;

	KeyInputHandler keyInputHandler;
	private int state;

	// 站立动画
	HashMap<Integer, Image> playerStandUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandLeft = new HashMap<Integer, Image>();

	Animation playerStandUpAnimation = new Animation();
	Animation playerStandDownAnimation = new Animation();
	Animation playerStandRightAnimation = new Animation();
	Animation playerStandLeftAnimation = new Animation();

	// 跑步动画
	HashMap<Integer, Image> playerRunUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunLeft = new HashMap<Integer, Image>();

	Animation playerRunUpAnimation = new Animation();
	Animation playerRunDownAnimation = new Animation();
	Animation playerRunRightAnimation = new Animation();
	Animation playerRunLeftAnimation = new Animation();

	public Player(KeyInputHandler keyInputHandler, float speed) {
		super();
		state = STATE_NORMAL;
		this.keyInputHandler = keyInputHandler;
		this.isMoved = false;
		this.name = "Player";
		this.speed = speed;
		CollisionBox.collisionBoxs.put(this.name, new CollisionBox(this.position.sub(new Vector2(33,-17)), this.position.add(new Vector2(25,90))));
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}
	
	Vector2 lastMoveVector = Vector2.zero();
	public synchronized void update() {
		if (GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2DOT5) {
			this.moveVector = lastMoveVector;
			float modifier = 76.0f/130.0f;	// 2.5D视角时，需要进行速度修正才不会走歪
			this.isMoved = false;
			if (this.keyInputHandler.up.isPressed()) {
				this.animator.state = "playerRunUp";
				this.moveVector = (new Vector2(this.speed, this.speed*modifier)).mul(new Vector2(-1, -1));
				isMoved = true;
			} else if (this.keyInputHandler.down.isPressed()) {
				this.animator.state = "playerRunDown";
				this.moveVector = (new Vector2(this.speed, this.speed*modifier)).mul(new Vector2(1, 1));
				isMoved = true;
			} else if (this.keyInputHandler.left.isPressed()) {
				this.animator.state = "playerRunLeft";
				this.moveVector = (new Vector2(this.speed, this.speed*modifier)).mul(new Vector2(-1, 1));
				isMoved = true;
			} else if (this.keyInputHandler.right.isPressed()) {
				this.animator.state = "playerRunRight";
				this.moveVector = (new Vector2(this.speed, this.speed*modifier)).mul(new Vector2(1, -1));
				isMoved = true;
			}
			
			if (!isMoved) {
				if (FloatCompare.isBigger(this.moveVector.x , 0) && FloatCompare.isBigger(this.moveVector.y , 0)) {
					this.animator.state = "playerStandDown";
				}
				if (FloatCompare.isBigger(this.moveVector.x , 0) && FloatCompare.isLess(this.moveVector.y , 0)) {
					this.animator.state = "playerStandRight";
				}
				if (FloatCompare.isLess(this.moveVector.x , 0) && FloatCompare.isBigger(this.moveVector.y , 0)) {
					this.animator.state = "playerStandLeft";
				}
				if (FloatCompare.isLess(this.moveVector.x , 0) && FloatCompare.isLess(this.moveVector.y , 0)) {
					this.animator.state = "playerStandUp";
				}
				AudioManager.getInstance().stopPlay("walkSound");
			} else {
				AudioManager.getInstance().playLoop("walkSound");
				this.position = this.position.add(this.moveVector.mul(Time.deltaTime));
				lastMoveVector = this.moveVector;
			}
		} else if(GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2) {
			this.moveVector = lastMoveVector;
			this.isMoved = false;
			if (this.keyInputHandler.up.isPressed()) {
				this.animator.state = "playerRunUp";
				this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(0, -1));
				isMoved = true;
			} else if (this.keyInputHandler.down.isPressed()) {
				this.animator.state = "playerRunDown";
				this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(0, 1));
				isMoved = true;
			} else if (this.keyInputHandler.left.isPressed()) {
				this.animator.state = "playerRunLeft";
				this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(-1, 0));
				isMoved = true;
			} else if (this.keyInputHandler.right.isPressed()) {
				this.animator.state = "playerRunRight";
				this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(1, 0));
				isMoved = true;
			}
			
			if (!isMoved) {
				if (FloatCompare.isEqual(this.moveVector.x, 0f) && FloatCompare.isBigger(this.moveVector.y, 0f)) {
					this.animator.state = "playerStandDown";
				}
				if (FloatCompare.isBigger(this.moveVector.x, 0f) && FloatCompare.isEqual(this.moveVector.y, 0f)) {
					this.animator.state = "playerStandRight";
				}
				if (FloatCompare.isLess(this.moveVector.x, 0f) && FloatCompare.isEqual(this.moveVector.y, 0f)) {
					this.animator.state = "playerStandLeft";
				}
				if (FloatCompare.isEqual(this.moveVector.x, 0f) && FloatCompare.isLess(this.moveVector.y, 0f)) {
					this.animator.state = "playerStandUp";
				}
				AudioManager.getInstance().stopPlay("walkSound");	//停止播放脚步声
			} else {
				AudioManager.getInstance().playLoop("walkSound");	//循环播放脚步声
				this.position = this.position.add(this.moveVector.mul(Time.deltaTime));
				lastMoveVector = this.moveVector;
			}
		}

		super.update();
		
		//设置碰撞盒坐标
		CollisionBox.collisionBoxs.get(this.name).leftUpPoint = this.position.sub(new Vector2(33,-17));
		CollisionBox.collisionBoxs.get(this.name).rightDownPoint = this.position.add(new Vector2(25,90));
		//Debug.log(Debug.DebugLevel.INFO, ""+this.collisionBox.rightDownPoint.sub(this.position));
		//this.collisionBox.setPos(this.position.sub(new Vector2(33,-17)), this.position.add(Vector2.one().mul(50)));
	}


	/**
	 * 加载动画资源
	 */
	public void loadAnimation() {
		if(this.animator == null) {
			this.animator = new Animator();
		}
		float duration = 1.0f / 8.0f; // 人物动画每组8张，一秒播放8次

		if (GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2DOT5) {

			// up
			for (int i = 48; i <= 55; i++) {
				playerStandUp.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandUpAnimation.addFrame(playerStandUp.get(i), duration);

				playerRunUp.put(i, Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunUpAnimation.addFrame(playerRunUp.get(i), duration);
			}
			this.animator.addAnimation("playerStandUp", playerStandUpAnimation);
			this.animator.addAnimation("playerRunUp", playerRunUpAnimation);

			// down
			for (int i = 40; i <= 47; i++) {
				playerStandDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandDownAnimation.addFrame(playerStandDown.get(i), duration);

				playerRunDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunDownAnimation.addFrame(playerRunDown.get(i), duration);
			}
			this.animator.addAnimation("playerStandDown", playerStandDownAnimation);
			this.animator.addAnimation("playerRunDown", playerRunDownAnimation);

			// right
			for (int i = 56; i <= 63; i++) {
				playerStandRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandRightAnimation.addFrame(playerStandRight.get(i), duration);

				playerRunRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunRightAnimation.addFrame(playerRunRight.get(i), duration);
			}
			this.animator.addAnimation("playerStandRight", playerStandRightAnimation);
			this.animator.addAnimation("playerRunRight", playerRunRightAnimation);

			// left
			for (int i = 32; i <= 39; i++) {
				playerStandLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandLeftAnimation.addFrame(playerStandLeft.get(i), duration);

				playerRunLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunLeftAnimation.addFrame(playerRunLeft.get(i), duration);
			}
			this.animator.addAnimation("playerStandLeft", playerStandLeftAnimation);
			this.animator.addAnimation("playerRunLeft", playerRunLeftAnimation);

			this.animator.state = "playerStandDown";
		} else if (GameCore.viewAngle == GameCore.GameViewAngle.ViewAngle2) {
			// up
			for (int i = 24; i <= 31; i++) {
				playerStandUp.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandUpAnimation.addFrame(playerStandUp.get(i), duration);

				playerRunUp.put(i, Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunUpAnimation.addFrame(playerRunUp.get(i), duration);
			}
			this.animator.addAnimation("playerStandUp", playerStandUpAnimation);
			this.animator.addAnimation("playerRunUp", playerRunUpAnimation);

			// down
			for (int i = 0; i <= 7; i++) {
				playerStandDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandDownAnimation.addFrame(playerStandDown.get(i), duration);

				playerRunDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunDownAnimation.addFrame(playerRunDown.get(i), duration);
			}
			this.animator.addAnimation("playerStandDown", playerStandDownAnimation);
			this.animator.addAnimation("playerRunDown", playerRunDownAnimation);

			// right
			for (int i = 16; i <= 23; i++) {
				playerStandRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandRightAnimation.addFrame(playerStandRight.get(i), duration);

				playerRunRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunRightAnimation.addFrame(playerRunRight.get(i), duration);
			}
			this.animator.addAnimation("playerStandRight", playerStandRightAnimation);
			this.animator.addAnimation("playerRunRight", playerRunRightAnimation);

			// left
			for (int i = 8; i <= 15; i++) {
				playerStandLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandLeftAnimation.addFrame(playerStandLeft.get(i), duration);

				playerRunLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunLeftAnimation.addFrame(playerRunLeft.get(i), duration);
			}
			this.animator.addAnimation("playerStandLeft", playerStandLeftAnimation);
			this.animator.addAnimation("playerRunLeft", playerRunLeftAnimation);

			this.animator.state = "playerStandDown";
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}
}
