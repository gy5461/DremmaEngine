package priv.sandbox.game.entities;

import java.awt.Image;
import java.util.HashMap;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;
import priv.sandbox.game.collision.SandboxCollisionBox;

public class Player extends Entity {
	
	protected KeyInputHandler keyInputHandler;

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

	// 死亡动画：抬棺
	HashMap<Integer, Image> playerDie = new HashMap<Integer, Image>();
	Animation playerDieAnimation = new Animation();

	// 胜利动画：拍手
	HashMap<Integer, Image> playerClapUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerClapDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerClapRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerClapLeft = new HashMap<Integer, Image>();

	Animation playerClapUpAnimation = new Animation();
	Animation playerClapDownAnimation = new Animation();
	Animation playerClapRightAnimation = new Animation();
	Animation playerClapLeftAnimation = new Animation();

	Vector2 lastMoveVector = Vector2.zero(); // 上一个moveVector

	public static final float ATTACK_LERP = 135f; // 攻击动作插值距离
	public static final float ATTACK_OFFSETX = 130f; // 为了得到合适大小的攻击碰撞盒x方向的偏移量
	public static final float ATTACK_OFFSETY = 115f; // 为了得到合适大小的攻击碰撞盒y方向的偏移量

	public int pressAttackTimes = 0; // 攻击次数

	public int hp; // 血条
	public int maxHp; // 满血量
	public int attackHarm; // 攻击造成的伤害

	public Player(KeyInputHandler keyInputHandler, float speed) {
		super();

		this.keyInputHandler = keyInputHandler;
		this.name = "剑侠客";
		this.speed = speed;
		
		this.maxHp = 5;
		this.hp = maxHp;
		this.attackHarm = 1;

		SandboxCollisionBox.collisionBoxs.put(this.name, new SandboxCollisionBox(
				this.position.sub(new Vector2(33, -17)), this.position.add(new Vector2(25, 90))));
		this.loadAnimation();
	}

	public synchronized void update() {
		if (this.state != Entity.EntityState.DEAD && this.state != Entity.EntityState.WIN) {

			switch (GameCore.viewAngle) {
			case ViewAngle2DOT5:
				// 根据当前动画状态初始化角色状态̬
				if (this.animator.getState() != null && (this.animator.getState().contains("playerRun")
						|| (this.animator.getState().contains("playerStand")))) {
					this.state = Entity.EntityState.STAND; // 站立/跑步动画播放中，角色处于站立状态̬
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
						SandboxCollisionBox.collisionBoxs.get("playerAttackUp").trans(transValue);
					}

					if (this.animator.getState().contains("Down")) {
						// 插值，从(0, 0)到(ATTACK_LERP, ATTACK_LERP * TileMap.modifier)
						Vector2 transValue = Vector2.lerp(Vector2.zero(),
								new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier), Time.deltaTime);
						TileMap.entities.get("playerAttackDown").moveVector = transValue;
						SandboxCollisionBox.collisionBoxs.get("playerAttackDown").trans(transValue);
					}

					if (this.animator.getState().contains("Left")) {
						// 插值，从(0, 0)到(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier)
						Vector2 transValue = Vector2.lerp(Vector2.zero(),
								new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier), Time.deltaTime);
						TileMap.entities.get("playerAttackLeft").moveVector = transValue;
						SandboxCollisionBox.collisionBoxs.get("playerAttackLeft").trans(transValue);
					}

					if (this.animator.getState().contains("Right")) {
						// 插值，从(0, 0)到(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier)
						Vector2 transValue = Vector2.lerp(Vector2.zero(),
								new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier), Time.deltaTime);
						TileMap.entities.get("playerAttackRight").moveVector = transValue;
						SandboxCollisionBox.collisionBoxs.get("playerAttackRight").trans(transValue);
					}
				}

				// 处理攻击
				if (this.keyInputHandler.attack.isPressed()
						&& this.pressAttackTimes < this.keyInputHandler.attack.getPressedTimes()) {
					this.pressAttackTimes = this.keyInputHandler.attack.getPressedTimes();

					AudioManager.getInstance().stopPlay("runSound");
					AudioManager.getInstance().playLoop("attackSound");

					switch (this.direction) {
					case UP:
						this.animator.setState("playerAttackUp", true);
						this.animator.setState("playerStandUp", false);

						// 近战碰撞盒 Up
						Animator playerAttackUpAnimator = null;
						AttackEntity playerAttackUpAttackEntity = null;
						if (!TileMap.entities.containsKey("playerAttackUp")) {
							playerAttackUpAnimator = new Animator();
							playerAttackUpAnimator.addAnimation("playerAttackUp", this.playerAttackUpAnimation);
							playerAttackUpAttackEntity = new AttackEntity(playerAttackUpAnimator, this);
							playerAttackUpAttackEntity.animator.setState("playerAttackUp", true);
							playerAttackUpAttackEntity.name = "playerAttackUpAttackEntity";
							playerAttackUpAttackEntity.position = new Vector2(this.position);
							playerAttackUpAttackEntity.setScale(this.getScale());
							playerAttackUpAttackEntity.visible = false;
							playerAttackUpAttackEntity.direction = this.direction;
							TileMap.addEntity("playerAttackUp", playerAttackUpAttackEntity);
						}

						if (playerAttackUpAttackEntity != null
								&& !SandboxCollisionBox.collisionBoxs.containsKey("playerAttackUp")) {
							SandboxCollisionBox.collisionBoxs.put("playerAttackUp", new SandboxCollisionBox(
									playerAttackUpAttackEntity.position.sub(new Vector2(
											playerAttackUpAttackEntity.getWidth()
													* playerAttackUpAttackEntity.getScale().x / 2 - ATTACK_OFFSETX,
											playerAttackUpAttackEntity.getHeight()
													* playerAttackUpAttackEntity.getScale().y / 2 - ATTACK_OFFSETY)),
									playerAttackUpAttackEntity.position));
						}

						if (SandboxCollisionBox.collisionBoxs.get("playerAttackUp").leftUpPoint
								.isLessOrEqual(TileMap.entities.get("playerAttackUp").position
										.sub(new Vector2(
												TileMap.entities.get("playerAttackUp").getWidth()
														* TileMap.entities.get("playerAttackUp").getScale().x / 2
														- ATTACK_OFFSETX,
												TileMap.entities.get("playerAttackUp").getHeight()
														* TileMap.entities.get("playerAttackUp").getScale().y / 2
														- ATTACK_OFFSETY))
										.add(new Vector2(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier)))) {
							SandboxCollisionBox.collisionBoxs.get("playerAttackUp")
									.trans(new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
						}

						break;
					case DOWN:
						this.animator.setState("playerAttackDown", true);
						this.animator.setState("playerStandDown", false);

						// 近战碰撞盒 Down
						Animator playerAttackDownAnimator = null;
						AttackEntity playerAttackDownAttackEntity = null;
						if (!TileMap.entities.containsKey("playerAttackDown")) {
							playerAttackDownAnimator = new Animator();
							playerAttackDownAnimator.addAnimation("playerAttackDown", this.playerAttackDownAnimation);
							playerAttackDownAttackEntity = new AttackEntity(playerAttackDownAnimator, this);
							playerAttackDownAttackEntity.animator.setState("playerAttackDown", true);
							playerAttackDownAttackEntity.name = "playerAttackDownAttackEntity";
							playerAttackDownAttackEntity.position = new Vector2(this.position);
							playerAttackDownAttackEntity.setScale(this.getScale());
							playerAttackDownAttackEntity.visible = false;
							playerAttackDownAttackEntity.direction = this.direction;
							TileMap.addEntity("playerAttackDown", playerAttackDownAttackEntity);
						}

						if (playerAttackDownAttackEntity != null
								&& !SandboxCollisionBox.collisionBoxs.containsKey("playerAttackDown")) {
							SandboxCollisionBox.collisionBoxs.put("playerAttackDown", new SandboxCollisionBox(
									playerAttackDownAttackEntity.position,
									playerAttackDownAttackEntity.position.add(new Vector2(
											playerAttackDownAttackEntity.getWidth()
													* playerAttackDownAttackEntity.getScale().x / 2 - ATTACK_OFFSETX,
											playerAttackDownAttackEntity.getHeight()
													* playerAttackDownAttackEntity.getScale().y / 2
													- ATTACK_OFFSETY))));
						}

						if (SandboxCollisionBox.collisionBoxs.get("playerAttackDown").leftUpPoint
								.isBiggerOrEqual(TileMap.entities.get("playerAttackDown").position
										.add(new Vector2(ATTACK_LERP, ATTACK_LERP * TileMap.modifier)))) {
							SandboxCollisionBox.collisionBoxs.get("playerAttackDown")
									.trans(new Vector2(-ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
						}

						break;
					case LEFT:
						this.animator.setState("playerAttackLeft", true);
						this.animator.setState("playerStandLeft", false);

						// 近战碰撞盒 Left
						Animator playerAttackLeftAnimator = null;
						AttackEntity playerAttackLeftAttackEntity = null;
						if (!TileMap.entities.containsKey("playerAttackLeft")) {
							playerAttackLeftAnimator = new Animator();
							playerAttackLeftAnimator.addAnimation("playerAttackLeft", this.playerAttackLeftAnimation);
							playerAttackLeftAttackEntity = new AttackEntity(playerAttackLeftAnimator, this);
							playerAttackLeftAttackEntity.animator.setState("playerAttackLeft", true);
							playerAttackLeftAttackEntity.name = "playerAttackLeftAttackEntity";
							playerAttackLeftAttackEntity.position = new Vector2(this.position);
							playerAttackLeftAttackEntity.setScale(this.getScale());
							playerAttackLeftAttackEntity.visible = false;
							playerAttackLeftAttackEntity.direction = this.direction;
							TileMap.addEntity("playerAttackLeft", playerAttackLeftAttackEntity);
						}

						if (playerAttackLeftAttackEntity != null
								&& !SandboxCollisionBox.collisionBoxs.containsKey("playerAttackLeft")) {
							SandboxCollisionBox.collisionBoxs.put("playerAttackLeft", new SandboxCollisionBox(
									playerAttackLeftAttackEntity.position.sub(new Vector2(
											playerAttackLeftAttackEntity.getWidth()
													* playerAttackLeftAttackEntity.getScale().x / 2 - ATTACK_OFFSETX,
											0)),
									playerAttackLeftAttackEntity.position.add(new Vector2(0,
											playerAttackLeftAttackEntity.getHeight()
													* playerAttackLeftAttackEntity.getScale().y / 2
													- ATTACK_OFFSETY))));
						}

						Vector2 endPointLeft = TileMap.entities.get("playerAttackLeft").position.sub(new Vector2(
								TileMap.entities.get("playerAttackLeft").getWidth()
										* TileMap.entities.get("playerAttackLeft").getScale().x / 2 - ATTACK_OFFSETX,
								0)).add(new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
						if (FloatCompare.isLessOrEqual(
								SandboxCollisionBox.collisionBoxs.get("playerAttackLeft").leftUpPoint.x, endPointLeft.x)
								&& FloatCompare.isBiggerOrEqual(
										SandboxCollisionBox.collisionBoxs.get("playerAttackLeft").leftUpPoint.y,
										endPointLeft.y)) {
							SandboxCollisionBox.collisionBoxs.get("playerAttackLeft")
									.trans(new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
						}

						break;
					case RIGHT:
						this.animator.setState("playerAttackRight", true);
						this.animator.setState("playerStandRight", false);

						// 近战碰撞盒 Right
						Animator playerAttackRightAnimator = null;
						AttackEntity playerAttackRightAttackEntity = null;
						if (!TileMap.entities.containsKey("playerAttackRight")) {
							playerAttackRightAnimator = new Animator();
							playerAttackRightAnimator.addAnimation("playerAttackRight",
									this.playerAttackRightAnimation);
							playerAttackRightAttackEntity = new AttackEntity(playerAttackRightAnimator, this);
							playerAttackRightAttackEntity.animator.setState("playerAttackRight", true);
							playerAttackRightAttackEntity.name = "playerAttackRightAttackEntity";
							playerAttackRightAttackEntity.position = new Vector2(this.position);
							playerAttackRightAttackEntity.setScale(this.getScale());
							playerAttackRightAttackEntity.visible = false;
							playerAttackRightAttackEntity.direction = this.direction;
							TileMap.addEntity("playerAttackRight", playerAttackRightAttackEntity);
						}

						if (playerAttackRightAttackEntity != null
								&& !SandboxCollisionBox.collisionBoxs.containsKey("playerAttackRight")) {
							SandboxCollisionBox.collisionBoxs.put("playerAttackRight", new SandboxCollisionBox(
									playerAttackRightAttackEntity.position.sub(new Vector2(0,
											playerAttackRightAttackEntity.getHeight()
													* playerAttackRightAttackEntity.getScale().y / 2 - ATTACK_OFFSETY)),
									playerAttackRightAttackEntity.position.add(new Vector2(
											playerAttackRightAttackEntity.getWidth()
													* playerAttackRightAttackEntity.getScale().x / 2 - ATTACK_OFFSETX,
											0))));
						}

						Vector2 endPointRight = TileMap.entities.get("playerAttackRight").position
								.sub(new Vector2(0,
										TileMap.entities.get("playerAttackRight").getHeight()
												* TileMap.entities.get("playerAttackRight").getScale().y / 2
												- ATTACK_OFFSETY))
								.add(new Vector2(ATTACK_LERP, -ATTACK_LERP * TileMap.modifier));
						if (FloatCompare.isBiggerOrEqual(
								SandboxCollisionBox.collisionBoxs.get("playerAttackRight").leftUpPoint.x,
								endPointRight.x)
								&& FloatCompare.isLessOrEqual(
										SandboxCollisionBox.collisionBoxs.get("playerAttackRight").leftUpPoint.y,
										endPointRight.y)) {
							SandboxCollisionBox.collisionBoxs.get("playerAttackRight")
									.trans(new Vector2(-ATTACK_LERP, ATTACK_LERP * TileMap.modifier));
						}

						break;
					}
					this.state = Entity.EntityState.ATTACK;
				}

				// 处理移动
				if (this.state == Entity.EntityState.STAND) {
					// AudioManager.getInstance().stopPlay("ghostWoundedSound");
					if (this.keyInputHandler.up.isPressed()) {
						this.animator.setState("playerRunUp", false);
						this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier))
								.mul(new Vector2(-1, -1)).mul(Time.deltaTime);
						this.direction = Entity.EntityDirection.UP;
						this.state = Entity.EntityState.MOVE;
					} else if (this.keyInputHandler.down.isPressed()) {
						this.animator.setState("playerRunDown", false);
						this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier))
								.mul(new Vector2(1, 1)).mul(Time.deltaTime);
						this.direction = Entity.EntityDirection.DOWN;
						this.state = Entity.EntityState.MOVE;
					} else if (this.keyInputHandler.left.isPressed()) {
						this.animator.setState("playerRunLeft", false);
						this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier))
								.mul(new Vector2(-1, 1)).mul(Time.deltaTime);
						this.direction = Entity.EntityDirection.LEFT;
						this.state = Entity.EntityState.MOVE;
					} else if (this.keyInputHandler.right.isPressed()) {
						this.animator.setState("playerRunRight", false);
						this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier))
								.mul(new Vector2(1, -1)).mul(Time.deltaTime);
						this.direction = Entity.EntityDirection.RIGHT;
						this.state = Entity.EntityState.MOVE;
					}
				}

				// 处理站立
				if (this.state == Entity.EntityState.STAND) {
					SandboxCollisionBox.collisionBoxs.remove("playerAttackUp");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackDown");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackLeft");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackRight");
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
					SandboxCollisionBox.collisionBoxs.remove("playerAttackUp");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackDown");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackLeft");
					SandboxCollisionBox.collisionBoxs.remove("playerAttackRight");
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
		}

		if (this.state == Entity.EntityState.WIN) {
			// 播放拍手动画
			switch (this.direction) {
			case DOWN:
				this.animator.setState("playerClapDown", true);
				break;
			case LEFT:
				this.animator.setState("playerClapLeft", true);
				break;
			case RIGHT:
				this.animator.setState("playerClapRight", true);
				break;
			case UP:
				this.animator.setState("playerClapUp", true);
				break;
			}
		}

		if (this.state == Entity.EntityState.DEAD) {
			// 播放死亡动画
			this.animator.setState("playerDie", true);
			if (!(this.animator.getState() != null && this.animator.getState().contains("playerAttack"))) {
				this.setScale(new Vector2(0.5f, 0.5f));
			}
		}

		super.update();

		// 设置碰撞盒坐标
		SandboxCollisionBox.collisionBoxs.get(this.name).leftUpPoint = this.position.sub(new Vector2(33, -17));
		SandboxCollisionBox.collisionBoxs.get(this.name).rightDownPoint = this.position.add(new Vector2(25, 90));

		// 画血条（黑底绿色）
		UIManager.getUIEntity("playerHpBar").getScale().x = 300 * this.hp * 1.0f / this.maxHp;
	}

	public void loadAnimation() {
		if (this.animator == null) {
			this.animator = new Animator();
		}
		float duration = 1.0f / 8.0f; // 人物动画每组8张，一秒播放8次

		float attackDuration = 1.0f / 10.0f; // 攻击动画每组10张，一秒播放10次

		float dieDuration = 2.0f / 10.0f; // 死亡动画每组10张，二秒播放10次
		float clapDuration = 1.0f / 11.0f; // 拍手动画每组11张，一秒播放11次

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

			// 死亡动画
			for (int i = 1; i <= 10; i++) {
				this.playerDie.put(i,
						Resources.loadImage(Resources.path + "images/animations/player_die/player_die_" + i + ".png"));
				this.playerDieAnimation.addFrame(this.playerDie.get(i), dieDuration);
			}
			this.animator.addAnimation("playerDie", this.playerDieAnimation);

			// 胜利动画
			for (int i = 1; i <= 11; i++) {
				this.playerClapDown.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_clap/player_clap_" + i + ".png"));
				this.playerClapDownAnimation.addFrame(this.playerClapDown.get(i), clapDuration);
			}
			this.animator.addAnimation("playerClapDown", this.playerClapDownAnimation);

			for (int i = 12; i <= 22; i++) {
				this.playerClapLeft.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_clap/player_clap_" + i + ".png"));
				this.playerClapLeftAnimation.addFrame(this.playerClapLeft.get(i), clapDuration);
			}
			this.animator.addAnimation("playerClapLeft", this.playerClapLeftAnimation);

			for (int i = 23; i <= 33; i++) {
				this.playerClapUp.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_clap/player_clap_" + i + ".png"));
				this.playerClapUpAnimation.addFrame(this.playerClapUp.get(i), clapDuration);
			}
			this.animator.addAnimation("playerClapUp", this.playerClapUpAnimation);

			for (int i = 34; i <= 44; i++) {
				this.playerClapRight.put(i, Resources
						.loadImage(Resources.path + "images/animations/player_clap/player_clap_" + i + ".png"));
				this.playerClapRightAnimation.addFrame(this.playerClapRight.get(i), clapDuration);
			}
			this.animator.addAnimation("playerClapRight", this.playerClapRightAnimation);

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
			case WIN:
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

	public synchronized void die() {
		this.detectCollision = false;
		this.state = Entity.EntityState.DEAD;
		AudioManager.getInstance().stopPlay("runSound");
		AudioManager.getInstance().stopPlay("attackSound");
		AudioManager.getInstance().stopPlay("playerWoundedSound");
		// 播放死亡音效
		AudioManager.getInstance().playLoop("playerDieSound");
		// 游戏结束，关闭背景音乐
		AudioManager.getInstance().stopPlay("backgroundSound");
	}

	public synchronized void win() {
		this.detectCollision = false;
		this.state = Entity.EntityState.WIN;
		AudioManager.getInstance().stopPlay("runSound");
		AudioManager.getInstance().stopPlay("attackSound");
		AudioManager.getInstance().stopPlay("playerWoundedSound");
		// 游戏结束，关闭背景音乐
		AudioManager.getInstance().stopPlay("backgroundSound");
		// 播放烟花音效
		AudioManager.getInstance().playLoop("playerWinSound");
		AudioManager.getInstance().playLoop("playerClapSound");
	}
}
