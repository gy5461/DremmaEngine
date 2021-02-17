package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.entities.Entity;

public class UIEntity extends Entity {
	
	public UIEntity() {
		super();
	}
	
	public UIEntity(Animator animator) {
		super(animator);
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
		transform.translate(this.position.x - this.getWidth() * this.getScale().x / 2f, this.position.y - this.getHeight() * this.getScale().y / 2f);
		transform.scale(this.getScale().x, this.getScale().y);
		
		g.drawImage(this.getImage(), transform, null);
	}
}
