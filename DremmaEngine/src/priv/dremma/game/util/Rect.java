package priv.dremma.game.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Rect {

	public Vector2 leftUpPoint;
	public Vector2 rightDownPoint;

	public boolean shouldTransScreenPos;

	static Image collisionBox = Resources.loadImage(Resources.path + "images/collisionBox.png");
	static Image border = Resources.loadImage(Resources.path + "images/border.png");

	public Rect(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = leftUpPoint;
		this.rightDownPoint = rightDownPoint;
		this.shouldTransScreenPos = true;
	}

	public float getWidth() {
		return (this.rightDownPoint.sub(this.leftUpPoint)).x;
	}

	public float getHeight() {
		return (this.rightDownPoint.sub(this.leftUpPoint)).y;
	}

	public Rect getScreenRect() {
		return new Rect(GUtils.worldPixelToViewPort(this.leftUpPoint),
				GUtils.worldPixelToViewPort(this.rightDownPoint));
	}

	public void draw(Graphics2D g) {
		// 渲染半透明部分
		Vector2 collisionBoxScreenPos = this.leftUpPoint;
		if (this.shouldTransScreenPos) {
			collisionBoxScreenPos = GUtils.worldPixelToViewPort(collisionBoxScreenPos);
		}
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(collisionBoxScreenPos.x, collisionBoxScreenPos.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// 渲染边框
		int borderWidth = 1;
		Vector2 borderLeftScreenPos = this.leftUpPoint;
		if (this.shouldTransScreenPos) {
			borderLeftScreenPos = GUtils.worldPixelToViewPort(borderLeftScreenPos);
		}
		AffineTransform borderLeftTransform = new AffineTransform();
		borderLeftTransform.translate(borderLeftScreenPos.x, borderLeftScreenPos.y);
		borderLeftTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderLeftTransform, null);

		Vector2 borderRightScreenPos = new Vector2(this.leftUpPoint.x + this.getWidth(), this.leftUpPoint.y);
		if (this.shouldTransScreenPos) {
			borderRightScreenPos = GUtils.worldPixelToViewPort(borderRightScreenPos);
		}
		AffineTransform borderRightTransform = new AffineTransform();
		borderRightTransform.translate(borderRightScreenPos.x, borderRightScreenPos.y);
		borderRightTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderRightTransform, null);

		Vector2 borderUpScreenPos = this.leftUpPoint;
		if(this.shouldTransScreenPos) {
			borderUpScreenPos=GUtils.worldPixelToViewPort(borderUpScreenPos);
		}
		AffineTransform borderUpTransform = new AffineTransform();
		borderUpTransform.translate(borderUpScreenPos.x, borderUpScreenPos.y);
		borderUpTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderUpTransform, null);

		Vector2 borderDownScreenPos = new Vector2(this.leftUpPoint.x, this.leftUpPoint.y + this.getHeight());
		if(this.shouldTransScreenPos) {
			borderDownScreenPos = GUtils.worldPixelToViewPort(borderDownScreenPos);
		}
		AffineTransform borderDownTransform = new AffineTransform();
		borderDownTransform.translate(borderDownScreenPos.x, borderDownScreenPos.y);
		borderDownTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderDownTransform, null);
	}
}
