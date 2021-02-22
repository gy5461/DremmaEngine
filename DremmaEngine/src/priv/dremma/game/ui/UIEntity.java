package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Vector2;

public class UIEntity extends Entity {

	public static enum UIAlign {
		CENTER, LEFT, RIGHT, UP, DOWN;
	}

	public UIAlign algin;
	
	public static enum UIMode {
		WORLD, SCREEN;
	}
	
	public UIMode mode = UIMode.SCREEN;

	public UIEntity() {
		super();
		this.algin = UIAlign.CENTER;
	}

	public UIEntity(Animator animator) {
		super(animator);
	}

	@Override
	public void draw(Graphics2D g) {
		Vector2 screenPos = Vector2.zero();	// 本UI显示在屏幕上的位置
		switch(this.mode) {
		case SCREEN:
			screenPos = this.position;
			break;
		case WORLD:
			screenPos = GUtils.worldPixelToViewPort(this.position);
			break;
		}
		
		AffineTransform transform = new AffineTransform();
		switch (this.algin) {
		case CENTER:
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x / 2f,
					screenPos.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case DOWN:
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x / 2f,
					screenPos.y - this.getHeight() * this.getScale().y);
			break;
		case LEFT:
			transform.translate(screenPos.x, screenPos.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case RIGHT:
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x, screenPos.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case UP:
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x / 2f, screenPos.y);
			break;
		}

		transform.scale(this.getScale().x, this.getScale().y);
		g.drawImage(this.getImage(), transform, null);
	}
}
