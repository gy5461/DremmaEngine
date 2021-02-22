package priv.sandbox.game.entities;

import java.awt.Image;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;
import priv.sandbox.game.collision.SandboxCollisionBox;

public class FightingNPC extends NPC {

	// FightingNPC的攻击动画
	HashMap<Integer, Image> NPCAttack;
	Animation NPCAttackAnimation;

	// FightingNPC的攻击实体（远攻）
	public AttackEntity attackEntity;

	Vector2 attackEndPos = Vector2.zero();

	float attackDistance = 400f;
	float curAttackDistance = 0f;

	public int hp; // 血条
	public int maxHp; // 满血量
	public int attackHarm; // 攻击造成的伤害

	public FightingNPC(float speed) {
		super(speed);
		this.moveSound = "ghostFloatSound";
		this.nearDistance = 300f;

		this.maxHp = 20;
		this.hp = maxHp;

		this.attackHarm = 1;
	}

	@Override
	public synchronized void update() {
		
		// Debug.log(Debug.DebugLevel.INFO,
		// ""+AudioManager.getInstance().getVolumn("ghostFloatSound"));
		if (this.state == Entity.EntityState.MOVE) {
			if (this.nearPlayer()) {
				int volumnPercent = (int) (((this.nearDistance - this.getDistance2Player()) / this.nearDistance) * 300);
				volumnPercent = Math.min(93, volumnPercent);
				AudioManager.getInstance().setVolumn("ghostFloatSound", volumnPercent);
			} else {
				AudioManager.getInstance().setVolumn("ghostFloatSound", 0);
			}
		}

		if (this.nearPlayer() && this.state != Entity.EntityState.DEAD) { // 临近玩家时
			// Debug.log(Debug.DebugLevel.INFO,this.name + " near player!!");
			// 发动攻击，发射鬼火
			if (this.attackEntity.visible == false) {
				this.attackEntity.position = this.position.add(this.moveVector.mul(10f));
				this.attackEndPos = this.attackEntity.position.add(this.moveVector.mul(this.attackDistance));
				this.attackEntity.visible = true;
				this.attackEntity.detectCollision = true;
				this.attackEntity.direction = this.direction;

				this.attackEntity.willCauseWound = true;
				return;
			}
		}

		if (this.attackEntity.visible == true) {
			Vector2 attackNewPos = Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime);
			// 鬼火运动一段距离后消失
			this.curAttackDistance += (attackNewPos.sub(this.attackEntity.position)).magnitude();
			this.attackEntity.moveVector = attackNewPos.sub(this.attackEntity.position);
			this.attackEntity.position = attackNewPos;

			SandboxCollisionBox.collisionBoxs.get(this.attackEntity.name).setPos(
					new Vector2(this.attackEntity.position.sub(30f)), new Vector2(this.attackEntity.position.add(30f)));
			if (FloatCompare.isBiggerOrEqual(this.curAttackDistance, this.attackDistance)) {
				this.curAttackDistance = 0f;
				this.attackEntity.visible = false;
				this.attackEntity.detectCollision = false;
			}
		}

		super.update();
		
		if (this.state == Entity.EntityState.DEAD) {
			UIManager.removeUI(UIManager.getUIEntity("fightingNPCHpBarBase"));
			UIManager.removeUI(UIManager.getUIEntity("fightingNPCHpBar"));
			return;
		}

		UIManager.getUIEntity("fightingNPCHpBar").position = this.position.sub(
				new Vector2(this.getWidth() * this.getScale().x / 2, this.getHeight() * this.getScale().y / 2 + 20));
		
		UIManager.getUIEntity("fightingNPCHpBar")
				.setScale(new Vector2(this.getWidth() * this.getScale().x * this.hp * 1.0f / this.maxHp, 10));

		UIManager.getUIEntity("fightingNPCHpBarBase").position = new Vector2(
				UIManager.getUIEntity("fightingNPCHpBar").position.x + this.getWidth() * this.getScale().x / 2,
				UIManager.getUIEntity("fightingNPCHpBar").position.y);
		UIManager.getUIEntity("fightingNPCHpBarBase").setScale(new Vector2(this.getWidth() * this.getScale().x, 10));
	}

	public void loadAnimation() {
		this.animator = new Animator();

		float duration = 0.5f / 4.0f; // NPC移动动画每组4张，0.5秒播放4次
		float attackDuration = 1.0f / 10.0f; // NPC攻击动画每组10张，1秒播放10次
		float dieDuration = 8.0f / 64.0f; // NPC动画每组64张，8秒播放64次

		// Down
		for (int i = 1; i <= 4; i++) {
			this.NPCMoveDown.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/野鬼_" + i + ".png"));
			this.NPCMoveDownAnimation.addFrame(this.NPCMoveDown.get(i), duration);
		}

		this.animator.addAnimation("npcMoveDown", this.NPCMoveDownAnimation);

		// Left
		for (int i = 5; i <= 8; i++) {
			this.NPCMoveLeft.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/野鬼_" + i + ".png"));
			this.NPCMoveLeftAnimation.addFrame(this.NPCMoveLeft.get(i), duration);
		}

		this.animator.addAnimation("npcMoveLeft", this.NPCMoveLeftAnimation);

		// Right
		for (int i = 9; i <= 12; i++) {
			this.NPCMoveRight.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/野鬼_" + i + ".png"));
			this.NPCMoveRightAnimation.addFrame(this.NPCMoveRight.get(i), duration);
		}

		this.animator.addAnimation("npcMoveRight", this.NPCMoveRightAnimation);

		// Up
		for (int i = 13; i <= 16; i++) {
			this.NPCMoveUp.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/野鬼_" + i + ".png"));
			this.NPCMoveUpAnimation.addFrame(this.NPCMoveUp.get(i), duration);
		}

		this.animator.addAnimation("npcMoveUp", this.NPCMoveUpAnimation);

		// Attack
		this.attackEntity = new AttackEntity(this);
		this.NPCAttack = new HashMap<Integer, Image>();
		this.NPCAttackAnimation = new Animation();

		this.attackEntity.name = "野鬼npcAttack";
		this.attackEntity.position = new Vector2(500, 400);
		this.attackEntity.setScale(new Vector2(3f, 3f));
		this.attackEntity.visible = false;
		this.attackEntity.detectCollision = false;

		attackEntity.animator = new Animator();
		for (int i = 1; i <= 10; i++) {
			this.NPCAttack.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/鬼火/鬼火_" + i + ".png"));
			this.NPCAttackAnimation.addFrame(this.NPCAttack.get(i), attackDuration);
		}
		attackEntity.animator.addAnimation("npcAttack", this.NPCAttackAnimation);
		attackEntity.animator.setState("npcAttack", false);

		TileMap.addEntity(this.attackEntity.name, this.attackEntity);
		SandboxCollisionBox.collisionBoxs.put(this.attackEntity.name, new SandboxCollisionBox(
				new Vector2(this.attackEntity.position.sub(30f)), new Vector2(this.attackEntity.position.add(30f))));
		SandboxCollisionBox.collisionBoxs.get(this.attackEntity.name).name = this.attackEntity.name;

		// 死亡
		for (int i = 0; i <= 63; i++) {
			this.NPCDie.put(i, Resources.loadImage(Resources.path + "images/entities/野鬼/死亡/死亡_" + i + ".png"));
			this.NPCDieAnimation.addFrame(this.NPCDie.get(i), dieDuration);
		}
		this.animator.addAnimation("npcDie", this.NPCDieAnimation);

		// 初始化动画状态̬
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
	}

	public synchronized void die() {
		this.detectCollision = false;
		this.state = Entity.EntityState.DEAD;

		AudioManager.getInstance().stopPlay(this.moveSound);
		AudioManager.getInstance().stopPlay("ghostWoundedSound");

		AudioManager.getInstance().playOnce("ghostDieSound");
		this.animator.setState("npcDie", false);

		// 8秒后NPC消失
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				visible = false;
			}
		}, 8000);
	}

}
