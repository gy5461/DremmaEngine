package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class FightingNPC extends NPC {

	// FightingNPCµÄ¹¥»÷¶¯»­
	HashMap<Integer, Image> NPCAttack;
	Animation NPCAttackAnimation;

	// FightingNPCµÄ¹¥»÷ÊµÌå£¨Ô¶¹¥£©
	public AttackEntity attackEntity;

	Vector2 attackEndPos = Vector2.zero();

	float attackDistance = 400f;
	float curAttackDistance = 0f;

	public int hp; // ÑªÌõ
	public int maxHp; // ÂúÑªÁ¿
	public int attackHarm; // ¹¥»÷Ôì³ÉµÄÉËº¦

	public FightingNPC(float speed) {
		super(speed);
		this.moveSound = "ghostFloatSound";
		this.nearDistance = 300f;

		this.hp = 100;
		this.maxHp = hp;
		this.attackHarm = 10;
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

		if (this.nearPlayer() && this.state != Entity.EntityState.DEAD) { // ÁÙ½üÍæ¼ÒÊ±
			// Debug.log(Debug.DebugLevel.INFO,this.name + " near player!!");
			// ·¢¶¯¹¥»÷£¬·¢Éä¹í»ð
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
			// ¹í»ðÔË¶¯Ò»¶Î¾àÀëºóÏûÊ§
			this.curAttackDistance += (Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime)
					.sub(this.attackEntity.position)).magnitude();
			this.attackEntity.moveVector = Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime)
					.sub(this.attackEntity.position);
			this.attackEntity.position = Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime);

			CollisionBox.collisionBoxs.get(this.attackEntity.name).setPos(
					new Vector2(this.attackEntity.position.sub(30f)), new Vector2(this.attackEntity.position.add(30f)));
			if (FloatCompare.isBiggerOrEqual(this.curAttackDistance, this.attackDistance)) {
				this.curAttackDistance = 0f;
				this.attackEntity.visible = false;
				this.attackEntity.detectCollision = false;
			}
		}

		super.update();
	}

	public void loadAnimation() {
		this.animator = new Animator();

		float duration = 0.5f / 4.0f; // NPC¶¯»­Ã¿×é4ÕÅ£¬0.5Ãë²¥·Å4´Î
		float attackDuration = 1.0f / 10.0f; // NPC¶¯»­Ã¿×é10ÕÅ£¬1Ãë²¥·Å10´Î

		// Down
		for (int i = 1; i <= 4; i++) {
			this.NPCMoveDown.put(i, Resources.loadImage(Resources.path + "images/entities/Ò°¹í/Ò°¹í_" + i + ".png"));
			this.NPCMoveDownAnimation.addFrame(this.NPCMoveDown.get(i), duration);
		}

		this.animator.addAnimation("npcMoveDown", this.NPCMoveDownAnimation);

		// Left
		for (int i = 5; i <= 8; i++) {
			this.NPCMoveLeft.put(i, Resources.loadImage(Resources.path + "images/entities/Ò°¹í/Ò°¹í_" + i + ".png"));
			this.NPCMoveLeftAnimation.addFrame(this.NPCMoveLeft.get(i), duration);
		}

		this.animator.addAnimation("npcMoveLeft", this.NPCMoveLeftAnimation);

		// Right
		for (int i = 9; i <= 12; i++) {
			this.NPCMoveRight.put(i, Resources.loadImage(Resources.path + "images/entities/Ò°¹í/Ò°¹í_" + i + ".png"));
			this.NPCMoveRightAnimation.addFrame(this.NPCMoveRight.get(i), duration);
		}

		this.animator.addAnimation("npcMoveRight", this.NPCMoveRightAnimation);

		// Up
		for (int i = 13; i <= 16; i++) {
			this.NPCMoveUp.put(i, Resources.loadImage(Resources.path + "images/entities/Ò°¹í/Ò°¹í_" + i + ".png"));
			this.NPCMoveUpAnimation.addFrame(this.NPCMoveUp.get(i), duration);
		}

		this.animator.addAnimation("npcMoveUp", this.NPCMoveUpAnimation);

		// Attack
		this.attackEntity = new AttackEntity(this);
		this.NPCAttack = new HashMap<Integer, Image>();
		this.NPCAttackAnimation = new Animation();

		attackEntity.name = "Ò°¹ínpcAttack";
		this.attackEntity.position = new Vector2(-100, -100);
		attackEntity.setScale(new Vector2(3f, 3f));
		this.attackEntity.visible = false;
		this.attackEntity.detectCollision = false;

		TileMap.addEntity(this.attackEntity.name, this.attackEntity);
		CollisionBox.collisionBoxs.put(this.attackEntity.name, new CollisionBox(
				new Vector2(this.attackEntity.position.sub(30f)), new Vector2(this.attackEntity.position.add(30f))));
		CollisionBox.collisionBoxs.get(this.attackEntity.name).name = this.attackEntity.name;

		attackEntity.animator = new Animator();
		for (int i = 1; i <= 10; i++) {
			this.NPCAttack.put(i, Resources.loadImage(Resources.path + "images/entities/Ò°¹í/¹í»ð/¹í»ð_" + i + ".png"));
			this.NPCAttackAnimation.addFrame(this.NPCAttack.get(i), attackDuration);
		}

		attackEntity.animator.addAnimation("npcAttack", this.NPCAttackAnimation);
		attackEntity.animator.setState("npcAttack", false);

		// ³õÊ¼»¯¶¯»­×´Ì¬
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

	public void die() {
		this.visible = false;
		this.detectCollision = false;
		this.state = Entity.EntityState.DEAD;

		AudioManager.getInstance().stopPlay(this.moveSound);
		AudioManager.getInstance().stopPlay("ghostWoundedSound");
		
		AudioManager.getInstance().playOnce("ghostDieSound");
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		Vector2 npcScreenPos = GUtils.worldPixelToViewPort(this.position.sub(
				new Vector2(this.getWidth() * this.getScale().x / 2, this.getHeight() * this.getScale().y / 2 + 20)));
		// »­ÑªÌõ£¨ºÚµ×ºìÉ«£©
		// ÑªÌõµ×
		AffineTransform npcHpBarBase = new AffineTransform();
		npcHpBarBase.translate(npcScreenPos.x, npcScreenPos.y);
		npcHpBarBase.scale(this.getWidth() * this.getScale().x, 10);
		g.drawImage(Resources.loadImage(Resources.path + "images/ÑªÌõµ×.png"), npcHpBarBase, null);

		// ÑªÌõ
		AffineTransform npcHpBar = new AffineTransform();
		npcHpBar.translate(npcScreenPos.x, npcScreenPos.y);
		npcHpBar.scale(this.getWidth() * this.getScale().x * this.hp * 1.0f / this.maxHp, 10);
		g.drawImage(Resources.loadImage(Resources.path + "images/xArrow.png"), npcHpBar, null);
	}

}
