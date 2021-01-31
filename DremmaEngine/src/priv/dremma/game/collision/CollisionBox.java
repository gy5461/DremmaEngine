package priv.dremma.game.collision;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Vector2;

/**
 * 2D p��ײ��
 * 
 * @author guoyi
 *
 */
public class CollisionBox {
	// ���������е���ײ��
	public static HashMap<String, CollisionBox> collisionBoxs = new HashMap<String, CollisionBox>();

	public Vector2 leftUpPoint; // ���ϵ�
	public Vector2 rightDownPoint; // ���µ�
	static Image collisionBox = Resources.loadImage(Resources.path + "images/collisionBox.png");
	static Image border = Resources.loadImage(Resources.path + "images/border.png");

	public static boolean isRender = true;
	public boolean isChoosenLeftUp;
	public boolean isChoosenRightDown;

	public CollisionBox(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = new Vector2(leftUpPoint);
		this.rightDownPoint = new Vector2(rightDownPoint);
		this.isChoosenLeftUp = false;
	}
	
	public static Iterator<Entry<String, CollisionBox>> getCollisionBoxsIterator() {
		return CollisionBox.collisionBoxs.entrySet().iterator();
	}

	public void setPos(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = new Vector2(leftUpPoint);
		this.rightDownPoint = new Vector2(rightDownPoint);
	}

	public float getWidth() {
		return (this.rightDownPoint.sub(this.leftUpPoint)).x;
	}

	public float getHeight() {
		return (this.rightDownPoint.sub(this.leftUpPoint)).y;
	}

	public void draw(Graphics2D g) {
		if (!CollisionBox.isRender) {
			return;
		}
		// ��Ⱦ��͸������
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// ��Ⱦ�߿�
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

		// ��Ⱦ���ǵĵ�
		int pointWidth = 5;
		AffineTransform leftUpPointTransform = new AffineTransform();
		leftUpPointTransform.translate(this.leftUpPoint.x - pointWidth, this.leftUpPoint.y - pointWidth);
		leftUpPointTransform.scale(pointWidth * 2, pointWidth * 2);
		g.drawImage(border, leftUpPointTransform, null);

		AffineTransform rightDownPointTransform = new AffineTransform();
		rightDownPointTransform.translate(this.rightDownPoint.x - pointWidth, this.rightDownPoint.y - pointWidth);
		rightDownPointTransform.scale(pointWidth * 2, pointWidth * 2);
		g.drawImage(border, rightDownPointTransform, null);
	}
}
