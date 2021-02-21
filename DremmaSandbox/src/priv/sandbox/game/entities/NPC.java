package priv.sandbox.game.entities;

import java.awt.Image;
import java.util.HashMap;
import java.util.Random;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;
import priv.sandbox.game.collision.SandboxCollisionBox;

public class NPC extends Entity {
	// NPC的站立动画
	HashMap<Integer, Image> NPCStandUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandLeft = new HashMap<Integer, Image>();

	Animation NPCStandUpAnimation = new Animation();
	Animation NPCStandDownAnimation = new Animation();
	Animation NPCStandRightAnimation = new Animation();
	Animation NPCStandLeftAnimation = new Animation();

	// NPC的移动动画
	HashMap<Integer, Image> NPCMoveUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveLeft = new HashMap<Integer, Image>();

	Animation NPCMoveUpAnimation = new Animation();
	Animation NPCMoveDownAnimation = new Animation();
	Animation NPCMoveRightAnimation = new Animation();
	Animation NPCMoveLeftAnimation = new Animation();

	// NPC死亡动画
	HashMap<Integer, Image> NPCDie = new HashMap<Integer, Image>();
	Animation NPCDieAnimation = new Animation();

	public Vector2 startPos = Vector2.zero();
	public Vector2 endPos = Vector2.zero();
	public float totalDistance = 300f;
	public float curDistance = 0f;

	protected float nearDistance = 100f;

	public float standTimer = 0.0f; // 站立计时
	float standTime = 1.0f; // 站立时间

	protected String moveSound;

	public NPC(float speed) {
		super();
		this.name = "NPC";
		this.speed = speed;
		this.loadAnimation();
	}

	/**
	 * NPC更新：处理NPC的随机移动
	 */
	@Override
	public synchronized void update() {
		switch (GameCore.viewAngle) {
		case ViewAngle2DOT5:
			// 若NPC速度为0，不需要动
			if (FloatCompare.isEqual(this.speed, 0f)) {
				return;
			}

			if (this.state == Entity.EntityState.STAND) {
				// 站立一会儿
				if (this.standTimer < this.standTime) {
					this.standTimer += Time.deltaTime;
					return;
				} else {
					this.standTimer = 0.0f;
				}
				// 随机一个方向开始走
				Random random = new Random();
				switch (Entity.EntityDirection.values()[random.nextInt(4)]) {
				case UP:
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(-1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.UP;
					this.state = Entity.EntityState.MOVE;
					this.animator.setState("npcMoveUp", false);
					break;
				case DOWN:
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.DOWN;
					this.state = Entity.EntityState.MOVE;
					this.animator.setState("npcMoveDown", false);
					break;
				case LEFT:
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(-1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.LEFT;
					this.state = Entity.EntityState.MOVE;
					this.animator.setState("npcMoveLeft", false);
					break;
				case RIGHT:
					this.moveVector = (new Vector2(this.speed, this.speed * TileMap.modifier)).mul(new Vector2(1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.RIGHT;
					this.state = Entity.EntityState.MOVE;
					this.animator.setState("npcMoveRight", false);
					break;
				}

				this.startPos = this.position;
				this.endPos = this.startPos.add(this.moveVector.mul(totalDistance));
			}

			// 移动时，移动一段距离后站立
			if (this.state == Entity.EntityState.MOVE) {
				AudioManager.getInstance().playLoop(moveSound);

				switch (this.direction) {
				case DOWN:
					this.animator.setState("npcMoveDown", false);
					break;
				case LEFT:
					this.animator.setState("npcMoveLeft", false);
					break;
				case RIGHT:
					this.animator.setState("npcMoveRight", false);
					break;
				case UP:
					this.animator.setState("npcMoveUp", false);
					break;
				}

				this.position = this.position.add(this.moveVector);
				this.curDistance += this.moveVector.magnitude();

				if (FloatCompare.isBiggerOrEqual(this.curDistance, this.totalDistance)) {
					this.state = Entity.EntityState.STAND;
					AudioManager.getInstance().stopPlay(moveSound);
					this.curDistance = 0.0f;
				}
			}
		case ViewAngle2:
			break;
		}

		super.update();

		// 设置碰撞盒坐标
		SandboxCollisionBox.collisionBoxs.get(this.name).leftUpPoint = this.position.sub(new Vector2(33, -17));
		SandboxCollisionBox.collisionBoxs.get(this.name).rightDownPoint = this.position.add(new Vector2(25, 90));
	}

	/**
	 * 加载动画资源
	 */
	public void loadAnimation() {

	}

	/**
	 * 获取当前NPC是否距离玩家近
	 * 
	 * @return
	 */
	protected boolean nearPlayer() {
		if (FloatCompare.isLessOrEqual(this.position.sub(TileMap.player.position).sqrMagnitude(),
				this.nearDistance * this.nearDistance)) {
			return true;
		}
		return false;
	}

	protected float getDistance2Player() {
		return this.position.sub(TileMap.player.position).magnitude();
	}

}
