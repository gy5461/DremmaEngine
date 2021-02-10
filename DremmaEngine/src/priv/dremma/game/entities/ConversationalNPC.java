package priv.dremma.game.entities;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC{

	public ConversationalNPC(float speed) {
		super(speed);
		this.loadAnimation();
	}

	public void loadAnimation() {
		this.animator = new Animator();
		this.NPCStandLeft.put(0, Resources
						.loadImage(Resources.path + "images/entities/ÄÏ¼«ÏÉÎÌ.png"));
		this.NPCStandLeftAnimation.addFrame(this.NPCStandLeft.get(0), 100);
		this.animator.addAnimation("stand", this.NPCStandLeftAnimation);
		this.animator.setState("stand", false);
	}

}
