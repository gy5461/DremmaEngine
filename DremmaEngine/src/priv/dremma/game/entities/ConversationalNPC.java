package priv.dremma.game.entities;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC{

	public ConversationalNPC(float speed) {
		super(speed);
		this.nearDistance = 80f;
	}
	
	public synchronized void update() {
		super.update();
		if(this.nearPlayer()) {
			//Debug.log(Debug.DebugLevel.INFO,this.name + " near player!!");
		}
	}

	public void loadAnimation() {
		this.animator = new Animator();
		this.NPCStandLeft.put(0, Resources
						.loadImage(Resources.path + "images/entities/ÄÏ¼«ÏÉÎÌ.png"));
		this.NPCStandLeftAnimation.addFrame(this.NPCStandLeft.get(0), 100);
		this.animator.addAnimation("npcStandLeft", this.NPCStandLeftAnimation);
		this.animator.setState("npcStandLeft", false);
	}

}
