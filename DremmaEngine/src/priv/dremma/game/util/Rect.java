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

	public void draw(Graphics2D g) {
		// ‰÷»æ∞ÎÕ∏√˜≤ø∑÷
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// ‰÷»æ±ﬂøÚ
		int borderWidth = 1;

		AffineTransform borderLeftTransform = new AffineTransform();
		borderLeftTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y);
		borderLeftTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderLeftTransform, null);

		AffineTransform borderRightTransform = new AffineTransform();
		borderRightTransform.translate(this.leftUpPoint.x + this.getWidth(), this.leftUpPoint.y);
		borderRightTransform.scale(borderWidth, this.getHeight());
		g.drawImage(border, borderRightTransform, null);

		AffineTransform borderUpTransform = new AffineTransform();
		borderUpTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y);
		borderUpTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderUpTransform, null);

		AffineTransform borderDownTransform = new AffineTransform();
		borderDownTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y + this.getHeight());
		borderDownTransform.scale(this.getWidth(), borderWidth);
		g.drawImage(border, borderDownTransform, null);

		// ‰÷»æ¡ΩΩ«µƒµ„
//		int pointWidth = 5;
//		AffineTransform leftUpPointTransform = new AffineTransform();
//		leftUpPointTransform.translate(this.leftUpPoint.x - pointWidth, this.leftUpPoint.y - pointWidth);
//		leftUpPointTransform.scale(pointWidth * 2, pointWidth * 2);
//		g.drawImage(border, leftUpPointTransform, null);
//
//		AffineTransform rightDownPointTransform = new AffineTransform();
//		rightDownPointTransform.translate(this.rightDownPoint.x - pointWidth, this.rightDownPoint.y - pointWidth);
//		rightDownPointTransform.scale(pointWidth * 2, pointWidth * 2);
//		g.drawImage(border, rightDownPointTransform, null);
	}
}
