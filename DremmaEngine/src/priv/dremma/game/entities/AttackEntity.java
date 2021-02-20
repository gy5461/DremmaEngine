package priv.dremma.game.entities;

import priv.dremma.game.anim.Animator;

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
	
	public AttackEntity(Animator animator, Entity attacker) {
		super(animator);
		this.willCauseWound = true;
		this.attacker = attacker;
	}
}
