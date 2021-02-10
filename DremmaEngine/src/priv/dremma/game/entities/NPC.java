package priv.dremma.game.entities;

import java.awt.Image;
import java.util.HashMap;
import java.util.Random;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class NPC extends Entity {

	Vector2 lastMoveVector = Vector2.zero(); // 上一个moveVector

	// NPC的站立动画
	HashMap<Integer, Image> NPCStandUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandLeft = new HashMap<Integer, Image>();

	Animation NPCStandUpAnimation = new Animation();
	Animation NPCStandDownAnimation = new Animation();
	Animation NPCStandRightAnimation = new Animation();
	Animation NPCStandLeftAnimation = new Animation();

	// NPC的跑步动画
	HashMap<Integer, Image> NPCRunUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCRunDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCRunRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCRunLeft = new HashMap<Integer, Image>();

	Animation NPCRunUpAnimation = new Animation();
	Animation NPCRunDownAnimation = new Animation();
	Animation NPCRunRightAnimation = new Animation();
	Animation NPCRunLeftAnimation = new Animation();

	Vector2 startPos = Vector2.zero();
	float standTimer = 0.0f; // 站立计时
	float standTime = 1.0f; // 站立时间

	public NPC(float speed) {
		super();
		this.name = "NPC";
		this.speed = speed;
	}

	/**
	 * NPC更新：处理NPC的随机移动
	 */
	@Override
	public synchronized void update() {
		switch (GameCore.viewAngle) {
		case ViewAngle2DOT5:
			float modifier = TileMap.TILE_SIZE.y / TileMap.TILE_SIZE.x; // 2.5D视角时，需要进行速度修正才不会走歪

			// 站立时，开始移动
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
					this.moveVector = (new Vector2(this.speed, this.speed * modifier)).mul(new Vector2(-1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.UP;
					this.state = Entity.EntityState.MOVE;
					break;
				case DOWN:
					this.moveVector = (new Vector2(this.speed, this.speed * modifier)).mul(new Vector2(1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.DOWN;
					this.state = Entity.EntityState.MOVE;
					break;
				case LEFT:
					this.moveVector = (new Vector2(this.speed, this.speed * modifier)).mul(new Vector2(-1, 1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.LEFT;
					this.state = Entity.EntityState.MOVE;
					break;
				case RIGHT:
					this.moveVector = (new Vector2(this.speed, this.speed * modifier)).mul(new Vector2(1, -1))
							.mul(Time.deltaTime);
					this.direction = Entity.EntityDirection.RIGHT;
					this.state = Entity.EntityState.MOVE;
					break;
				}

				this.startPos = this.position;
			}

			float distance = 300f;

			// 移动时，移动一段距离后站立
			if (this.state == Entity.EntityState.MOVE) {
				AudioManager.getInstance().playLoop("ghostFloatSound");

				this.position = this.position.add(this.moveVector);
				CollisionBox.collisionBoxs.get(this.name).trans(this.moveVector);
				switch (this.direction) {
				case UP:
					if (this.position.isLessOrEqual(this.startPos.add(this.moveVector.mul(distance)))) {
						this.state = Entity.EntityState.STAND;
						AudioManager.getInstance().stopPlay("ghostFloatSound");
					}
					break;
				case DOWN:
					if (this.position.isBiggerOrEqual(this.startPos.add(this.moveVector.mul(distance)))) {
						this.state = Entity.EntityState.STAND;
						AudioManager.getInstance().stopPlay("ghostFloatSound");
					}
					break;
				case LEFT:
					if (FloatCompare.isLessOrEqual(this.position.x,
							(this.startPos.add(this.moveVector.mul(distance))).x)
							&& FloatCompare.isBiggerOrEqual(this.position.y,
									(this.startPos.add(this.moveVector.mul(distance))).y)) {
						this.state = Entity.EntityState.STAND;
						AudioManager.getInstance().stopPlay("ghostFloatSound");
					}
					break;
				case RIGHT:
					if (FloatCompare.isBiggerOrEqual(this.position.x,
							(this.startPos.add(this.moveVector.mul(distance))).x)
							&& FloatCompare.isLessOrEqual(this.position.y,
									(this.startPos.add(this.moveVector.mul(distance))).y)) {
						this.state = Entity.EntityState.STAND;
						AudioManager.getInstance().stopPlay("ghostFloatSound");
					}
					break;
				}

				lastMoveVector = this.moveVector;
			}
		case ViewAngle2:
			break;
		}

		super.update();
	}

	/**
	 * 加载动画资源
	 */
	public void loadAnimation() {

	}

}
