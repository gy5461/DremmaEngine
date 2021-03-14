package priv.sandbox.game.collision;

import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;
import priv.sandbox.game.entities.FightingNPC;
import priv.sandbox.game.entities.NPC;
import priv.sandbox.game.entities.Player;

public class SandboxCollisionBox extends CollisionBox {

	public SandboxCollisionBox(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		super(leftUpPoint, rightDownPoint);
	}

	/**
	 * 碰撞盒撞到别的碰撞盒时调用
	 * 
	 * @param entity      本实体
	 * @param otherEntity 被撞到的实体
	 */
	public void onCollision(Entity entity, Entity otherEntity) {

		// Debug.log(Debug.DebugLevel.INFO, entity.name + " 撞到了:" + otherEntity.name);
		// ------------------攻击处理--------------------
		if (entity instanceof AttackEntity && ((AttackEntity) entity).willCauseWound) {
			if (!((otherEntity instanceof Player) || (otherEntity instanceof FightingNPC))) {
				// 如果不是打中玩家或者战斗型NPC
				return;
			}
			if (((AttackEntity) entity).attacker instanceof Player) {
				if (otherEntity instanceof FightingNPC) {
					// 被打的实体是战斗型NPC，则该NPC受伤
					// 击退
					otherEntity.retreat(((AttackEntity) entity).attacker.direction, 100f);

					// 播放NPC受伤音效
					AudioManager.getInstance().playOnce("ghostWoundedSound");
					((AttackEntity) entity).willCauseWound = false;
					Debug.log(Debug.DebugLevel.INFO,
							((AttackEntity) entity).attacker.name + " 伤害了:" + otherEntity.name);

					// 减血
					if (((FightingNPC) otherEntity).hp > ((Player) ((AttackEntity) entity).attacker).attackHarm) {
						((FightingNPC) otherEntity).hp -= ((Player) ((AttackEntity) entity).attacker).attackHarm;
					} else {
						// NPC otherEntity死亡
						((FightingNPC) otherEntity).hp = 0;
						((FightingNPC) otherEntity).die();
						if (((Player) TileMap.player).state != Entity.EntityState.DEAD) {
							((Player) TileMap.player).win();
						}
					}
				}
			} else if (((AttackEntity) entity).attacker instanceof FightingNPC) {
				if (otherEntity instanceof Player) {
					// npc打中了玩家
					// 击退
					otherEntity.retreat(((AttackEntity) entity).attacker.direction, 50f);

					// 播放玩家受伤音效
					AudioManager.getInstance().playOnce("playerWoundedSound");
					((AttackEntity) entity).willCauseWound = false;
					Debug.log(Debug.DebugLevel.INFO,
							((AttackEntity) entity).attacker.name + " 伤害了:" + otherEntity.name);

					// 减血
					if (((Player) otherEntity).hp > ((FightingNPC) ((AttackEntity) entity).attacker).attackHarm) {
						((Player) otherEntity).hp -= ((FightingNPC) ((AttackEntity) entity).attacker).attackHarm;
					} else {
						// NPC otherEntity死亡
						((Player) otherEntity).hp = 0;
						((Player) otherEntity).die();
					}
				}
			}

		}

		// ------------------NPC碰到障碍物处理--------------------
		if (entity instanceof NPC) {
			// 碰撞后逆时针转向
			switch (entity.direction) {
			case UP:
				entity.direction = Entity.EntityDirection.LEFT;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(-1, 1))
						.mul(Time.deltaTime);
				break;
			case DOWN:
				entity.direction = Entity.EntityDirection.RIGHT;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(1, -1))
						.mul(Time.deltaTime);
				break;
			case LEFT:
				entity.direction = Entity.EntityDirection.DOWN;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(1, 1))
						.mul(Time.deltaTime);
				break;
			case RIGHT:
				entity.direction = Entity.EntityDirection.UP;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier))
						.mul(new Vector2(-1, -1)).mul(Time.deltaTime);
				break;
			}

			((NPC) entity).startPos = entity.position;
			((NPC) entity).endPos = ((NPC) entity).startPos.add(entity.moveVector.mul(((NPC) entity).totalDistance));
			entity.retreatVector = Vector2.zero();
		}

	}

	/**
	 * 碰撞盒触发别的触发器碰撞盒时调用
	 * 
	 * @param entity      本实体
	 * @param otherEntity 被撞到的实体
	 */
	public void onTriggerEnter(Entity entity, Entity otherEntity) {
		// Debug.log(Debug.DebugLevel.INFO, entity.name + " 触发了:" + otherEntity.name);
		// ------------------玩家触发地上的弓时--------------------
		if (entity.name.equals(TileMap.player.name) && otherEntity.name.equals("bow")) {
			// 弓从场景中消失
			TileMap.entities.remove("bow");
			SandboxCollisionBox.collisionBoxs.remove("bow");
			TranslateEntityHelper.translateEntities.remove("bow");
			// 弓进入背包
			((Player) TileMap.player).bag.addPropItemToBag("bowPropItem");
		}

		// ------------------玩家触发储物箱时--------------------
		if (UIManager.getUIEntity("playerView").visible == false && entity.name.equals(TileMap.player.name)
				&& otherEntity.name.equals("storageBox")) {
			UIManager.setUIVisibility("bagView", true);
			UIManager.setUIVisibility("storageBoxView", true);
			if (((Player) TileMap.player).bag.getSize() > 0) {
				// 默认选项文字为 放入
				UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
				UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt3"));
			} else if(((Player) TileMap.player).storageBox.getSize() > 0){
				// 默认选项文字为 取出
				UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
				UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt4"));
			}
		}
	}
}
