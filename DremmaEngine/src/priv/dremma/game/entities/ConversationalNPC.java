package priv.dremma.game.entities;

import java.awt.Graphics2D;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC {

	KeyInputHandler keyInputHandler;
	int pressedTime = 0;
	public boolean isTalking;

	public ConversationalNPC(KeyInputHandler keyInputHandler, float speed) {
		super(speed);
		this.nearDistance = 80f;
		this.keyInputHandler = keyInputHandler;
		this.isTalking = false;
	}

	public synchronized void update() {
		super.update();
		if (this.nearPlayer()) {
			if (!this.isTalking && this.keyInputHandler.talk.isPressed()
					&& this.pressedTime < this.keyInputHandler.talk.getPressedTimes()) {
				this.pressedTime = this.keyInputHandler.talk.getPressedTimes();
				this.isTalking = true;
			}
		}
		
		if (this.isTalking && this.keyInputHandler.talk.isPressed()
				&& this.pressedTime < this.keyInputHandler.talk.getPressedTimes()) {
			// 关闭对话
			GameCore.getUIEntity("talkBox").visible = false;
			this.isTalking = false;
			this.pressedTime = this.keyInputHandler.talk.getPressedTimes();
		}
	}

	public void loadAnimation() {
		this.animator = new Animator();
		this.NPCStandLeft.put(0, Resources.loadImage(Resources.path + "images/entities/南极仙翁.png"));
		this.NPCStandLeftAnimation.setStaticImage(this.NPCStandLeft.get(0));
		this.animator.addAnimation("npcStandLeft", this.NPCStandLeftAnimation);
		this.animator.setState("npcStandLeft", false);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if (this.isTalking) {
			GameCore.getUIEntity("talkBox").visible = true;
		}
	}

}
