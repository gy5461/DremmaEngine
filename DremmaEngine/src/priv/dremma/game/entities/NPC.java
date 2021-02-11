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
	// NPC��վ������
	HashMap<Integer, Image> NPCStandUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCStandLeft = new HashMap<Integer, Image>();

	Animation NPCStandUpAnimation = new Animation();
	Animation NPCStandDownAnimation = new Animation();
	Animation NPCStandRightAnimation = new Animation();
	Animation NPCStandLeftAnimation = new Animation();

	// NPC���ƶ�����
	HashMap<Integer, Image> NPCMoveUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> NPCMoveLeft = new HashMap<Integer, Image>();

	Animation NPCMoveUpAnimation = new Animation();
	Animation NPCMoveDownAnimation = new Animation();
	Animation NPCMoveRightAnimation = new Animation();
	Animation NPCMoveLeftAnimation = new Animation();

	public Vector2 startPos = Vector2.zero();
	public Vector2 endPos = Vector2.zero();
	public float totalDistance = 300f;
	public float curDistance = 0f;
	
	protected float nearDistance = 100f;

	public float standTimer = 0.0f; // վ����ʱ
	float standTime = 1.0f; // վ��ʱ��

	protected String moveSound;

	public NPC(float speed) {
		super();
		this.name = "NPC";
		this.speed = speed;
		this.loadAnimation();
	}

	/**
	 * NPC���£�����NPC������ƶ�
	 */
	@Override
	public synchronized void update() {
		switch (GameCore.viewAngle) {
		case ViewAngle2DOT5:
			// ��NPC�ٶ�Ϊ0������Ҫ��
			if (FloatCompare.isEqual(this.speed, 0f)) {
				return;
			}

			if (this.state == Entity.EntityState.STAND) {
				// վ��һ���
				if (this.standTimer < this.standTime) {
					this.standTimer += Time.deltaTime;
					return;
				} else {
					this.standTimer = 0.0f;
				}
				// ���һ������ʼ��
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

			// �ƶ�ʱ���ƶ�һ�ξ����վ��
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

				CollisionBox.collisionBoxs.get(this.name).trans(this.moveVector);
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

		// ������ײ������
		CollisionBox.collisionBoxs.get(this.name).leftUpPoint = this.position.sub(new Vector2(33, -17));
		CollisionBox.collisionBoxs.get(this.name).rightDownPoint = this.position.add(new Vector2(25, 90));
	}

	/**
	 * ���ض�����Դ
	 */
	public void loadAnimation() {

	}

	/**
	 * ��ȡ��ǰNPC�Ƿ������ҽ�
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

}
