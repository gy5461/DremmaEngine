package priv.dremma.game.entities;

import priv.dremma.game.anim.Animator;

public class AttackEntity extends Entity {

	public boolean willCauseWound;
	
	public AttackEntity() {
		super();
		this.willCauseWound = true;
	}
	
	public AttackEntity(Animator animator) {
		super(animator);
		this.willCauseWound = true;
	}
}
