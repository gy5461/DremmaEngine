package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.entities.Entity;

public class UIEntity extends Entity {

	public static enum UIAlign {
		CENTER, LEFT, RIGHT, UP, DOWN;
	}

	public UIAlign algin;

	public UIEntity() {
		super();
		this.algin = UIAlign.CENTER;
	}

	public UIEntity(Animator animator) {
		super(animator);
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
		switch (this.algin) {
		case CENTER:
			transform.translate(this.position.x - this.getWidth() * this.getScale().x / 2f,
					this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case DOWN:
			transform.translate(this.position.x - this.getWidth() * this.getScale().x / 2f,
					this.position.y - this.getHeight() * this.getScale().y);
			break;
		case LEFT:
			transform.translate(this.position.x, this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case RIGHT:
			transform.translate(this.position.x - this.getWidth() * this.getScale().x, this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case UP:
			transform.translate(this.position.x - this.getWidth() * this.getScale().x / 2f, this.position.y);
			break;
		}

		transform.scale(this.getScale().x, this.getScale().y);
		g.drawImage(this.getImage(), transform, null);
	}
}
