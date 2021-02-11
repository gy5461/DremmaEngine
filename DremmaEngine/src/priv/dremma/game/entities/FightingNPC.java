package priv.dremma.game.entities;

import java.awt.Image;
import java.util.HashMap;

import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class FightingNPC extends NPC {

	// FightingNPCµÄ¹¥»÷¶¯»­
	HashMap<Integer, Image> NPCAttack;
	Animation NPCAttackAnimation;

	// FightingNPCµÄ¹¥»÷ÊµÌå£¨Ô¶¹¥£©
	public Entity attackEntity;

	Vector2 attackEndPos = Vector2.zero();

	float attackDistance = 400f;
	float curAttackDistance = 0f;

	public FightingNPC(float speed) {
		super(speed);
		this.moveSound = "ghostFloatSound";
		this.nearDistance = 300f;
	}

	@Override
	public synchronized void update() {
		if (this.nearPlayer()) {	// ÁÙ½üÍæ¼ÒÊ±
			// Debug.log(Debug.DebugLevel.INFO,this.name + " near player!!");
			// ·¢¶¯¹¥»÷£¬·¢Éä¹í»ð
			if (this.attackEntity.visible == false) {
				this.attackEntity.position = this.position.add(this.moveVector.mul(10f));
				this.attackEndPos = this.attackEntity.position.add(this.moveVector.mul(this.attackDistance));
				this.attackEntity.visible = true;
				this.attackEntity.detectCollision = true;
				this.attackEntity.direction = this.direction;
				return;
			}
		}

		if (this.attackEntity.visible == true) {
			// ¹í»ðÔË¶¯Ò»¶Î¾àÀëºóÏûÊ§
			this.curAttackDistance += (Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime).sub(this.attackEntity.position)).magnitude();
			this.attackEntity.moveVector = Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime).sub(this.attackEntity.position);
			this.attackEntity.position = Vector2.lerp(this.attackEntity.position, this.attackEndPos, Time.deltaTime);
			
			CollisionBox.collisionBoxs.get(this.attackEntity.name).setPos(
					new Vector2(this.attackEntity.position.sub(30f)), new Vector2(this.attackEntity.position.add(30f)));
			if(FloatCompare.isBiggerOrEqual(this.curAttackDistance, this.attackDistance)) {
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
		this.attackEntity = new Entity();
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

}
