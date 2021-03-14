package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;

public class UIEntity extends Entity {
	
	UIEntity parent = null;	// 双亲UI

	public static enum UIAlign {
		CENTER, LEFT, RIGHT, UP, DOWN;
	}

	public UIAlign algin = UIAlign.CENTER;;

	public static enum UIMode {
		WORLD, SCREEN;
	}

	public UIMode mode = UIMode.SCREEN;

	MouseInputHandler mouseInputHandler = null;

	public UIEntity() {
		super();
	}

	public UIEntity(MouseInputHandler mouseInputHandler) {
		super();
		this.mouseInputHandler = mouseInputHandler;
	}

	public UIEntity(Animator animator) {
		super(animator);
	}

	/**
	 * UIEntity是否被鼠标点击
	 * 
	 * @return
	 */
	public boolean isPressedMouseButton() {
		if (this.mouseInputHandler == null) {
			return false;
		}
		if (this != UIManager.lockUIEntiy) {
			return false;
		}
		if (TranslateEntityHelper.hasLock) {
			return false;
		}
		return (this.mouseInputHandler.mouse.isPressed()
				&& this.mouseInputHandler.getCurPos().isInRect(this.getLeftUpPoint(), this.getRightDownPoint()));
	}

	/**
	 * UIEntity是否被鼠标选中
	 * 
	 * @return
	 */
	public boolean isChosenMouseButton() {
		if (this.mouseInputHandler == null) {
			return false;
		}
		return this.mouseInputHandler.getCurPos().isInRect(this.getLeftUpPoint(), this.getRightDownPoint());
	}
	
	/**
	 * 获取UI双亲
	 * @param name
	 * @return
	 */
	public UIEntity getParent() {
		return this.parent;
	}

	/**
	 * 获取UIEntity左上角点
	 * 
	 * @return
	 */
	public Vector2 getLeftUpPoint() {
		Vector2 result = Vector2.zero();
		switch (this.algin) {
		case CENTER:
			result = new Vector2(this.position.x - this.getWidth() * this.getScale().x / 2f,
					this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case DOWN:
			result = new Vector2(this.position.x - this.getWidth() * this.getScale().x / 2,
					this.position.y - this.getHeight() * this.getScale().y);
			break;
		case LEFT:
			result = new Vector2(this.position.x, this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case RIGHT:
			result = new Vector2(this.position.x - this.getWidth() * this.getScale().x,
					this.position.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case UP:
			result = new Vector2(this.position.x - this.getWidth() * this.getScale().x / 2f, this.position.y);
			break;
		}
		return result;
	}

	/**
	 * 获取UIEntity右下角点
	 * 
	 * @return
	 */
	public Vector2 getRightDownPoint() {
		Vector2 result = Vector2.zero();
		switch (this.algin) {
		case CENTER:
			result = new Vector2(this.position.x + this.getWidth() * this.getScale().x / 2f,
					this.position.y + this.getHeight() * this.getScale().y / 2f);
			break;
		case DOWN:
			result = new Vector2(this.position.x + this.getWidth() * this.getScale().x / 2f, this.position.y);
			break;
		case LEFT:
			result = new Vector2(this.position.x + this.getWidth() * this.getScale().x,
					this.position.y + this.getHeight() * this.getScale().y / 2f);
			break;
		case RIGHT:
			result = new Vector2(this.position.x, this.position.y + this.getHeight() * this.getScale().y / 2f);
			break;
		case UP:
			result = new Vector2(this.position.x + this.getWidth() * this.getScale().x / 2f,
					this.position.y + this.getHeight() * this.getScale().y);
			break;
		}
		return result;
	}

	@Override
	public void draw(Graphics2D g) {
		Vector2 screenPos = Vector2.zero(); // 本UI显示在屏幕上的位置
		switch (this.mode) {
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
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x,
					screenPos.y - this.getHeight() * this.getScale().y / 2f);
			break;
		case UP:
			transform.translate(screenPos.x - this.getWidth() * this.getScale().x / 2f, screenPos.y);
			break;
		}

		transform.scale(this.getScale().x, this.getScale().y);
		g.drawImage(this.getImage(), transform, null);
	}
}
