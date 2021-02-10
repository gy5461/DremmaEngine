package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

/**
 * 游戏主角类
 * 
 * @author guoyi
 *
 */
public class Player extends Entity {

	KeyInputHandler keyInputHandler;

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

	// 攻击动画（近战）
	HashMap<Integer, Image> playerAttackUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerAttackDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerAttackRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerAttackLeft = new HashMap<Integer, Image>();

	Animation playerAttackUpAnimation = new Animation();
	Animation playerAttackDownAnimation = new Animation();
	Animation playerAttackRightAnimation = new Animation();
	Animation playerAttackLeftAnimation = new Animation();
	
	Vector2 lastMoveVector = Vector2.zero();	// 上一个moveVector
	
	public static final float ATTACK_LERP = 135f;	// 攻击动作插值距离
	public static final float ATTACK_OFFSETX = 130f;	// 为了得到合适大小的攻击碰撞盒x方向的偏移量
	public static final float ATTACK_OFFSETY = 115f;	// 为了得到合适大小的攻击碰撞盒y方向的偏移量

	public Player(KeyInputHandler keyInputHandler, float speed) {
		super();
		this.keyInputHandler = keyInputHandler;
		this.name = "Player";
		this.speed = speed;
		CollisionBox.collisionBoxs.put(this.name,
				new CollisionBox(this.position.sub(new Vector2(33, -17)), this.position.add(new Vector2(25, 90))));
		this.loadAnimation();
	}

	public synchronized void update() {
		switch (GameCore.viewAngle) {
		case ViewAngle2DOT5:
			// 根据当前动画状态初始化角色状态
			if (this.animator.getState() != null && (this.animator.getState().contains("playerRun")
					|| (this.animator.getState().contains("playerStand")))) {
				this.state = Entity.EntityState.STAND; // 站立/跑步动画播放中，角色处于站立状态
			}

			if (this.animator.getState() != null && this.animator.getState().contains("playerAttack")) {
				this.state = Entity.EntityState.ATTACK; // 攻击动画播放中，角色处于攻击状态
			}

			if (this.state == Entity.EntityState.ATTACK) {
				if (this.animator.getState().contains("Up")) {
					// 插值，从(0, 0)到(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier)
					Vector2 transValue = Vector2.lerp(Vector2.zero(),
							new Vector2(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier), Time.deltaTime);
					TileMap.entities.get("playerAttackUp").moveVector = transValue;
					CollisionBox.collisionBoxs.get("playerAttackUp").trans(transValue);
				}

				if (this.animator.getState().contains("Down")) {
					// 插值，从(0, 0)到(ATTACK_LERP, ATTACK_LERP * TileMap.modifier)
					Vector2 transValue = Vector2.lerp(Vector2.zero(), new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier),
							Time.deltaTime);
					TileMap.entities.get("playerAttackDown").moveVector = transValue;
					CollisionBox.collisionBoxs.get("playerAttackDown").trans(transValue);
				}

				if (this.animator.getState().contains("Left")) {
					// 插值，从(0, 0)到(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier)
					Vector2 transValue = Vector2.lerp(Vector2.zero(), new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier),
							Time.deltaTime);
					TileMap.entities.get("playerAttackLeft").moveVector = transValue;
					CollisionBox.collisionBoxs.get("playerAttackLeft").trans(transValue);
				}

				if (this.animator.getState().contains("Right")) {
					// 插值，从(0, 0)到(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier)
					Vector2 transValue = Vector2.lerp(Vector2.zero(), new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier),
							Time.deltaTime);
					TileMap.entities.get("playerAttackRight").moveVector = transValue;
					CollisionBox.collisionBoxs.get("playerAttackRight").trans(transValue);
				}
			}

			// 处理攻击
			if (this.keyInputHandler.key.keyCode == KeyEvent.VK_J && this.keyInputHandler.key.isPressed()) {
				AudioManager.getInstance().stopPlay("runSound");
				AudioManager.getInstance().playLoop("attackSound");

				switch (this.direction) {
				case UP:
					this.animator.setState("playerAttackUp", true);
					this.animator.setState("playerStandUp", false);

					// 近战碰撞盒 Up
					Animator playerAttackUpAnimator = null;
					Entity playerAttackUpEntity = null;
					if (!TileMap.entities.containsKey("playerAttackUp")) {
						playerAttackUpAnimator = new Animator();
						playerAttackUpAnimator.addAnimation("playerAttackUp", this.playerAttackUpAnimation);
						playerAttackUpEntity = new Entity(playerAttackUpAnimator);
						playerAttackUpEntity.animator.setState("playerAttackUp", true);
						playerAttackUpEntity.name = "playerAttackUpEntity";
						playerAttackUpEntity.position = new Vector2(this.position);
						playerAttackUpEntity.setScale(this.getScale());
						playerAttackUpEntity.visible = false;
						TileMap.addEntity("playerAttackUp", playerAttackUpEntity);
					}

					if (playerAttackUpEntity != null && !CollisionBox.collisionBoxs.containsKey("playerAttackUp")) {
						CollisionBox.collisionBoxs.put("playerAttackUp",
								new CollisionBox(playerAttackUpEntity.position.sub(new Vector2(
										playerAttackUpEntity.getWidth() * playerAttackUpEntity.getScale().x / 2
												- ATTACK_OFFSETX,
										playerAttackUpEntity.getHeight() * playerAttackUpEntity.getScale().y / 2
												- ATTACK_OFFSETY)),
										playerAttackUpEntity.position));
					}

					if (CollisionBox.collisionBoxs.get("playerAttackUp").leftUpPoint
							.isLessOrEqual(TileMap.entities.get("playerAttackUp").position
									.sub(new Vector2(
											TileMap.entities.get("playerAttackUp").getWidth()
													* TileMap.entities.get("playerAttackUp").getScale().x / 2
													- ATTACK_OFFSETX,
											TileMap.entities.get("playerAttackUp").getHeight()
													* TileMap.entities.get("playerAttackUp").getScale().y / 2
													- ATTACK_OFFSETY))
									.add(new Vector2(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier)))) {
						CollisionBox.collisionBoxs.get("playerAttackUp")
								.trans(new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
					}

					break;
				case DOWN:
					this.animator.setState("playerAttackDown", true);
					this.animator.setState("playerStandDown", false);

					// 近战碰撞盒 Down
					Animator playerAttackDownAnimator = null;
					Entity playerAttackDownEntity = null;
					if (!TileMap.entities.containsKey("playerAttackDown")) {
						playerAttackDownAnimator = new Animator();
						playerAttackDownAnimator.addAnimation("playerAttackDown", this.playerAttackDownAnimation);
						playerAttackDownEntity = new Entity(playerAttackDownAnimator);
						playerAttackDownEntity.animator.setState("playerAttackDown", true);
						playerAttackDownEntity.name = "playerAttackDownEntity";
						playerAttackDownEntity.position = new Vector2(this.position);
						playerAttackDownEntity.setScale(this.getScale());
						playerAttackDownEntity.visible = false;
						TileMap.addEntity("playerAttackDown", playerAttackDownEntity);
					}

					if (playerAttackDownEntity != null && !CollisionBox.collisionBoxs.containsKey("playerAttackDown")) {
						CollisionBox.collisionBoxs.put("playerAttackDown", new CollisionBox(
								playerAttackDownEntity.position,
								playerAttackDownEntity.position.add(new Vector2(
										playerAttackDownEntity.getWidth() * playerAttackDownEntity.getScale().x / 2
												- ATTACK_OFFSETX,
										playerAttackDownEntity.getHeight() * playerAttackDownEntity.getScale().y / 2
												- ATTACK_OFFSETY))));
					}

					if (CollisionBox.collisionBoxs.get("playerAttackDown").leftUpPoint
							.isBiggerOrEqual(TileMap.entities.get("playerAttackDown").position
									.add(new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier)))) {
						CollisionBox.collisionBoxs.get("playerAttackDown")
								.trans(new Vector2(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
					}

					break;
				case LEFT:
					this.animator.setState("playerAttackLeft", true);
					this.animator.setState("playerStandLeft", false);

					// 近战碰撞盒 Left
					Animator playerAttackLeftAnimator = null;
					Entity playerAttackLeftEntity = null;
					if (!TileMap.entities.containsKey("playerAttackLeft")) {
						playerAttackLeftAnimator = new Animator();
						playerAttackLeftAnimator.addAnimation("playerAttackLeft", this.playerAttackLeftAnimation);
						playerAttackLeftEntity = new Entity(playerAttackLeftAnimator);
						playerAttackLeftEntity.animator.setState("playerAttackLeft", true);
						playerAttackLeftEntity.name = "playerAttackLeftEntity";
						playerAttackLeftEntity.position = new Vector2(this.position);
						playerAttackLeftEntity.setScale(this.getScale());
						playerAttackLeftEntity.visible = false;
						TileMap.addEntity("playerAttackLeft", playerAttackLeftEntity);
					}

					if (playerAttackLeftEntity != null && !CollisionBox.collisionBoxs.containsKey("playerAttackLeft")) {
						CollisionBox.collisionBoxs.put("playerAttackLeft", new CollisionBox(
								playerAttackLeftEntity.position.sub(new Vector2(
										playerAttackLeftEntity.getWidth() * playerAttackLeftEntity.getScale().x / 2
												- Player.ATTACK_OFFSETX,
										0)),
								playerAttackLeftEntity.position.add(new Vector2(0,
										playerAttackLeftEntity.getHeight() * playerAttackLeftEntity.getScale().y / 2
												- Player.ATTACK_OFFSETY))));
					}

					Vector2 endPointLeft = TileMap.entities.get("playerAttackLeft").position.sub(new Vector2(
							TileMap.entities.get("playerAttackLeft").getWidth()
									* TileMap.entities.get("playerAttackLeft").getScale().x / 2 - Player.ATTACK_OFFSETX,
							0)).add(new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
					if (FloatCompare.isLessOrEqual(CollisionBox.collisionBoxs.get("playerAttackLeft").leftUpPoint.x,
							endPointLeft.x)
							&& FloatCompare.isBiggerOrEqual(
									CollisionBox.collisionBoxs.get("playerAttackLeft").leftUpPoint.y, endPointLeft.y)) {
						CollisionBox.collisionBoxs.get("playerAttackLeft")
								.trans(new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
					}

					break;
				case RIGHT:
					this.animator.setState("playerAttackRight", true);
					this.animator.setState("playerStandRight", false);

					// 近战碰撞盒 Right
					Animator playerAttackRightAnimator = null;
					Entity playerAttackRightEntity = null;
					if (!TileMap.entities.containsKey("playerAttackRight")) {
						playerAttackRightAnimator = new Animator();
						playerAttackRightAnimator.addAnimation("playerAttackRight", this.playerAttackRightAnimation);
						playerAttackRightEntity = new Entity(playerAttackRightAnimator);
						playerAttackRightEntity.animator.setState("playerAttackRight", true);
						playerAttackRightEntity.name = "playerAttackRightEntity";
						playerAttackRightEntity.position = new Vector2(this.position);
						playerAttackRightEntity.setScale(this.getScale());
						playerAttackRightEntity.visible = false;
						TileMap.addEntity("playerAttackRight", playerAttackRightEntity);
					}

					if (playerAttackRightEntity != null
							&& !CollisionBox.collisionBoxs.containsKey("playerAttackRight")) {
						CollisionBox.collisionBoxs.put("playerAttackRight",
								new CollisionBox(
										playerAttackRightEntity.position.sub(new Vector2(0,
												playerAttackRightEntity.getHeight()
														* playerAttackRightEntity.getScale().y / 2
														- Player.ATTACK_OFFSETY)),
										playerAttackRightEntity.position
												.add(new Vector2(playerAttackRightEntity.getWidth()
														* playerAttackRightEntity.getScale().x / 2
														- Player.ATTACK_OFFSETX, 0))));
					}

					Vector2 endPointRight = TileMap.entities.get("playerAttackRight").position
							.sub(new Vector2(0,
									TileMap.entities.get("playerAttackRight").getHeight()
											* TileMap.entities.get("playerAttackRight").getScale().y / 2
											- Player.ATTACK_OFFSETY))
							.add(new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
					if (FloatCompare.isBiggerOrEqual(CollisionBox.collisionBoxs.get("playerAttackRight").leftUpPoint.x,
							endPointRight.x)
							&& FloatCompare.isLessOrEqual(
									CollisionBox.collisionBoxs.get("playerAttackRight").leftUpPoint.y,
									endPointRight.y)) {
						CollisionBox.collisionBoxs.get("playerAttackRight")
								.trans(new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
					}

					break;
				}
				this.state = Entity.EntityState.ATTACK;
			}

			// 处理移动
			if (this.state == Entity.EntityState.STAND) {
				if (this.keyInputHandler.up.isPressed()) {
					this.animator.setState("playerRunUp", false);
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(-1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.UP;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.down.isPressed()) {
					this.animator.setState("playerRunDown", false);
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.DOWN;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.left.isPressed()) {
					this.animator.setState("playerRunLeft", false);
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(-1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.LEFT;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.right.isPressed()) {
					this.animator.setState("playerRunRight", false);
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.RIGHT;
					this.state = Entity.EntityState.MOVE;
				}
			}

			// 处理站立
			if (this.state == Entity.EntityState.STAND) {
				CollisionBox.collisionBoxs.remove("playerAttackUp");
				CollisionBox.collisionBoxs.remove("playerAttackDown");
				CollisionBox.collisionBoxs.remove("playerAttackLeft");
				CollisionBox.collisionBoxs.remove("playerAttackRight");
				TileMap.entities.remove("playerAttackUp");
				TileMap.entities.remove("playerAttackDown");
				TileMap.entities.remove("playerAttackLeft");
				TileMap.entities.remove("playerAttackRight");

				switch (this.direction) {
				case UP:
					this.animator.setState("playerStandUp", false);
					break;
				case DOWN:
					this.animator.setState("playerStandDown", false);
					break;
				case LEFT:
					this.animator.setState("playerStandLeft", false);
					break;
				case RIGHT:
					this.animator.setState("playerStandRight", false);
					break;
				}
				AudioManager.getInstance().stopPlay("runSound");
				AudioManager.getInstance().stopPlay("attackSound");
				this.moveVector = Vector2.zero();

			} else if (this.state == Entity.EntityState.MOVE) {
				CollisionBox.collisionBoxs.remove("playerAttackUp");
				CollisionBox.collisionBoxs.remove("playerAttackDown");
				CollisionBox.collisionBoxs.remove("playerAttackLeft");
				CollisionBox.collisionBoxs.remove("playerAttackRight");
				TileMap.entities.remove("playerAttackUp");
				TileMap.entities.remove("playerAttackDown");
				TileMap.entities.remove("playerAttackLeft");
				TileMap.entities.remove("playerAttackRight");

				AudioManager.getInstance().stopPlay("attackSound");
				AudioManager.getInstance().playLoop("runSound");

				this.position = this.position.add(this.moveVector);
				lastMoveVector = this.moveVector;
			}

			break;
		case ViewAngle2:
			this.state = Entity.EntityState.STAND;

			// 处理移动
			if (this.state == Entity.EntityState.STAND) {
				if (this.keyInputHandler.up.isPressed()) {
					this.animator.setState("playerRunUp", false);
					this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(0, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.UP;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.down.isPressed()) {
					this.animator.setState("playerRunDown", false);
					this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(0, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.DOWN;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.left.isPressed()) {
					this.animator.setState("playerRunLeft", false);
					this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(-1, 0))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.LEFT;
					this.state = Entity.EntityState.MOVE;
				} else if (this.keyInputHandler.right.isPressed()) {
					this.animator.setState("playerRunRight", false);
					this.moveVector = (new Vector2(this.speed, this.speed)).mul(new Vector2(1, 0))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.RIGHT;
					this.state = Entity.EntityState.MOVE;
				}
			}

			// 处理站立
			if (this.state == Entity.EntityState.STAND) {

				switch (this.direction) {
				case UP:
					this.animator.setState("playerStandUp", false);
					break;
				case DOWN:
					this.animator.setState("playerStandDown", false);
					break;
				case LEFT:
					this.animator.setState("playerStandLeft", false);
					break;
				case RIGHT:
					this.animator.setState("playerStandRight", false);
					break;
				}
				AudioManager.getInstance().stopPlay("runSound");
				this.moveVector = Vector2.zero();

			} else if (this.state == Entity.EntityState.MOVE) {
				AudioManager.getInstance().playLoop("runSound");
				this.position = this.position.add(this.moveVector);
				lastMoveVector = this.moveVector;
			}

			break;
		}

		super.update();

		// 设置碰撞盒坐标
		CollisionBox.collisionBoxs.get(this.name).leftUpPoint = this.position.sub(new Vector2(33, -17));
		CollisionBox.collisionBoxs.get(this.name).rightDownPoint = this.position.add(new Vector2(25, 90));
	}

	/**
	 * 加载动画资源
	 */
	public void loadAnimation() {
		if (this.animator == null) {
			this.animator = new Animator();
		}
		float duration = 1.0f / 8.0f; // 人物动画每组8张，一秒播放8次

		float attackDuration = 1.0f / 10.0f; // 人物动画每组10张，一秒播放10次

		switch (GameCore.viewAngle) {
		case ViewAngle2DOT5:
			// up
			for (int i = 48; i <= 55; i++) {
				playerStandUp.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandUpAnimation.addFrame(playerStandUp.get(i), duration);

				playerRunUp.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunUpAnimation.addFrame(playerRunUp.get(i), duration);
			}
			this.animator.addAnimation("playerStandUp", playerStandUpAnimation);
			this.animator.addAnimation("playerRunUp", playerRunUpAnimation);

			for (int i = 21; i <= 30; i++) {
				this.playerAttackUp.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_attack/player_attack_" + i + ".png"));
				this.playerAttackUpAnimation.addFrame(this.playerAttackUp.get(i), attackDuration);
			}
			this.animator.addAnimation("playerAttackUp", this.playerAttackUpAnimation);

			// down
			for (int i = 40; i <= 47; i++) {
				playerStandDown.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandDownAnimation.addFrame(playerStandDown.get(i), duration);

				playerRunDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunDownAnimation.addFrame(playerRunDown.get(i), duration);
			}
			this.animator.addAnimation("playerStandDown", playerStandDownAnimation);
			this.animator.addAnimation("playerRunDown", playerRunDownAnimation);

			for (int i = 1; i <= 10; i++) {
				this.playerAttackDown.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_attack/player_attack_" + i + ".png"));
				this.playerAttackDownAnimation.addFrame(this.playerAttackDown.get(i), attackDuration);
			}
			this.animator.addAnimation("playerAttackDown", this.playerAttackDownAnimation);

			// right
			for (int i = 56; i <= 63; i++) {
				playerStandRight.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandRightAnimation.addFrame(playerStandRight.get(i), duration);

				playerRunRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunRightAnimation.addFrame(playerRunRight.get(i), duration);
			}
			this.animator.addAnimation("playerStandRight", playerStandRightAnimation);
			this.animator.addAnimation("playerRunRight", playerRunRightAnimation);

			for (int i = 31; i <= 40; i++) {
				this.playerAttackRight.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_attack/player_attack_" + i + ".png"));
				this.playerAttackRightAnimation.addFrame(this.playerAttackRight.get(i), attackDuration);
			}
			this.animator.addAnimation("playerAttackRight", this.playerAttackRightAnimation);

			// left
			for (int i = 32; i <= 39; i++) {
				playerStandLeft.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandLeftAnimation.addFrame(playerStandLeft.get(i), duration);

				playerRunLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunLeftAnimation.addFrame(playerRunLeft.get(i), duration);
			}
			this.animator.addAnimation("playerStandLeft", playerStandLeftAnimation);
			this.animator.addAnimation("playerRunLeft", playerRunLeftAnimation);

			for (int i = 11; i <= 20; i++) {
				this.playerAttackLeft.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_attack/player_attack_" + i + ".png"));
				this.playerAttackLeftAnimation.addFrame(this.playerAttackLeft.get(i), attackDuration);
			}
			this.animator.addAnimation("playerAttackLeft", this.playerAttackLeftAnimation);

			// 初始化动画状态
			switch (this.state) {
			case STAND:
				switch (this.direction) {
				case UP:
					this.animator.setState("playerStandUp", false);
					break;
				case DOWN:
					this.animator.setState("playerStandDown", false);
					break;
				case LEFT:
					this.animator.setState("playerStandLeft", false);
					break;
				case RIGHT:
					this.animator.setState("playerStandRight", false);
					break;
				}
				break;
			case MOVE:
				switch (this.direction) {
				case UP:
					this.animator.setState("playerRUNUp", false);
					break;
				case DOWN:
					this.animator.setState("playerRUNDown", false);
					break;
				case LEFT:
					this.animator.setState("playerRUNLeft", false);
					break;
				case RIGHT:
					this.animator.setState("playerRUNRight", false);
					break;
				}
				break;
			case ATTACK:
				switch (this.direction) {
				case UP:
					this.animator.setState("playerAttackUp", false);
					break;
				case DOWN:
					this.animator.setState("playerAttackDown", false);
					break;
				case LEFT:
					this.animator.setState("playerAttackLeft", false);
					break;
				case RIGHT:
					this.animator.setState("playerAttackRight", false);
					break;
				}
				break;
			case DEAD:
				break;
			}
			break;
		case ViewAngle2:
			// up
			for (int i = 24; i <= 31; i++) {
				playerStandUp.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandUpAnimation.addFrame(playerStandUp.get(i), duration);

				playerRunUp.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunUpAnimation.addFrame(playerRunUp.get(i), duration);
			}
			this.animator.addAnimation("playerStandUp", playerStandUpAnimation);
			this.animator.addAnimation("playerRunUp", playerRunUpAnimation);

			// down
			for (int i = 0; i <= 7; i++) {
				playerStandDown.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandDownAnimation.addFrame(playerStandDown.get(i), duration);

				playerRunDown.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunDownAnimation.addFrame(playerRunDown.get(i), duration);
			}
			this.animator.addAnimation("playerStandDown", playerStandDownAnimation);
			this.animator.addAnimation("playerRunDown", playerRunDownAnimation);

			// right
			for (int i = 16; i <= 23; i++) {
				playerStandRight.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandRightAnimation.addFrame(playerStandRight.get(i), duration);

				playerRunRight.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunRightAnimation.addFrame(playerRunRight.get(i), duration);
			}
			this.animator.addAnimation("playerStandRight", playerStandRightAnimation);
			this.animator.addAnimation("playerRunRight", playerRunRightAnimation);

			// left
			for (int i = 8; i <= 15; i++) {
				playerStandLeft.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_stand/player_stand_" + i + ".png"));
				playerStandLeftAnimation.addFrame(playerStandLeft.get(i), duration);

				playerRunLeft.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_run/player_run_" + i + ".png"));
				playerRunLeftAnimation.addFrame(playerRunLeft.get(i), duration);
			}
			this.animator.addAnimation("playerStandLeft", playerStandLeftAnimation);
			this.animator.addAnimation("playerRunLeft", playerRunLeftAnimation);

			this.animator.setState("playerStandDown", false);
			break;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}
}
