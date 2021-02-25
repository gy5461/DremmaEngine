package priv.dremma.game.entities;

/**
 * 攻击碰撞盒
 * @author guoyi
 *
 */
public class AttackEntity extends Entity {

	public boolean willCauseWound;
	public Entity attacker;
	
	public AttackEntity(Entity attacker) {
		super();
		this.willCauseWound = true;
		this.attacker = attacker;
	}
}
