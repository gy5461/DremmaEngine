package priv.dremma.game.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Rect {

	public Vector2 leftUpPoint;
	public Vector2 rightDownPoint;

	static Image collisionBox = Resources.loadImage(Resources.path + "images/collisionBox.png");
	static Image border = Resources.loadImage(Resources.path + "images/border.png");

	public Rect(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = leftUpPoint;
		this.rightDownPoint = rightDownPoint;
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
		// ‰÷»æ∞ÎÕ∏√˜≤ø∑÷
		Vector2 collisionBoxScreenPos = GUtils.worldPixelToViewPort(this.leftUpPoint);
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(collisionBoxScreenPos.x, collisionBoxScreenPos.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// ‰÷»æ±ﬂøÚ
		int borderWidth = 1;
		Vector2 borderLeftScreenPos = GUtils.worldPixelToViewPort(this.leftUpPoint);
		AffineTransform borderLeftTransform = new AffineTransform();
		borderLeftTransform.translate(borderLeftScreenPos.x, borderLeftScreenPos.y);
		borderLeftTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderLeftTransform, null);

		Vector2 borderRightScreenPos = GUtils
				.worldPixelToViewPort(new Vector2(this.leftUpPoint.x + this.getWidth(), this.leftUpPoint.y));
		AffineTransform borderRightTransform = new AffineTransform();
		borderRightTransform.translate(borderRightScreenPos.x, borderRightScreenPos.y);
		borderRightTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderRightTransform, null);

		Vector2 borderUpScreenPos = GUtils.worldPixelToViewPort(this.leftUpPoint);
		AffineTransform borderUpTransform = new AffineTransform();
		borderUpTransform.translate(borderUpScreenPos.x, borderUpScreenPos.y);
		borderUpTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderUpTransform, null);

		Vector2 borderDownScreenPos = GUtils
				.worldPixelToViewPort(new Vector2(this.leftUpPoint.x, this.leftUpPoint.y + this.getHeight()));
		AffineTransform borderDownTransform = new AffineTransform();
		borderDownTransform.translate(borderDownScreenPos.x, borderDownScreenPos.y);
		borderDownTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderDownTransform, null);

		// ‰÷»æ¡ΩΩ«µƒµ„
//		int pointWidth = 5;
//		Vector2 leftUpPointScreenPos = GUtils
//				.worldPixelToViewPort(new Vector2(this.leftUpPoint.x - pointWidth, this.leftUpPoint.y - pointWidth));
//		AffineTransform leftUpPointTransform = new AffineTransform();
//		leftUpPointTransform.translate(leftUpPointScreenPos.x, leftUpPointScreenPos.y);
//		leftUpPointTransform.scale(pointWidth * 2, pointWidth * 2);
//		g.drawImage(border, leftUpPointTransform, null);
//
//		Vector2 rightDownPointScreenPos = GUtils.worldPixelToViewPort(
//				new Vector2(this.rightDownPoint.x - pointWidth, this.rightDownPoint.y - pointWidth));
//		AffineTransform rightDownPointTransform = new AffineTransform();
//		rightDownPointTransform.translate(rightDownPointScreenPos.x, rightDownPointScreenPos.y);
//		rightDownPointTransform.scale(pointWidth * 2, pointWidth * 2);
//		g.drawImage(border, rightDownPointTransform, null);
	}
}
