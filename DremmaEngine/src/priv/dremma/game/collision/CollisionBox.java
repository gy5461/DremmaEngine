package priv.dremma.game.collision;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Vector2;

/**
 * 2D p碰撞盒
 * 
 * @author guoyi
 *
 */
public class CollisionBox {

	public Vector2 leftUpPoint; // 左上点
	public Vector2 rightDownPoint; // 右下点
	static Image collisionBox = Resources.loadImage(Resources.path + "images/collisionBox.png");
	static Image border = Resources.loadImage(Resources.path + "images/border.png");

	public static boolean isRender = true;
	public boolean isChoosenLeftUp;
	public boolean isChoosenRightDown;

	public MouseInputHandler mouseInputHandler;

	public CollisionBox(Vector2 leftUpPoint, Vector2 rightDownPoint, MouseInputHandler mouseInputHandler) {
		this.leftUpPoint = new Vector2(leftUpPoint);
		this.rightDownPoint = new Vector2(rightDownPoint);
		this.isChoosenLeftUp = false;
		this.mouseInputHandler = mouseInputHandler;
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

	public void update() {
		if (!CollisionBox.isRender) {
			return;
		}
		// 调节左上点的位置
		if (this.mouseInputHandler.mouse.isPressed() && this.mouseInputHandler.mouse.getPressedTimes() % 2 == 1
				&& this.isChoosenLeftUp == false && this.mouseInputHandler.mouse.isInRect(
						this.leftUpPoint.sub(Vector2.one().mul(5)), this.leftUpPoint.add(Vector2.one().mul(5)))) {
			this.isChoosenLeftUp = true;
		}

		if (this.mouseInputHandler.mouse.isPressed() && this.mouseInputHandler.mouse.getPressedTimes() % 2 == 0
				&& this.isChoosenLeftUp == true) {
			this.isChoosenLeftUp = false;
			Debug.log(Debug.DebugLevel.INFO, "调整过后，左上点坐标为：" + this.leftUpPoint);
		}

		if (this.isChoosenLeftUp) {
			if (!this.leftUpPoint.isEqual(this.mouseInputHandler.mouse.getCurPos())) {
				this.leftUpPoint = this.mouseInputHandler.mouse.getCurPos();
			}
		}

		// 调节右下点的位置
		if (this.mouseInputHandler.mouse.isPressed() && this.mouseInputHandler.mouse.getPressedTimes() % 2 == 1
				&& this.isChoosenRightDown == false && this.mouseInputHandler.mouse.isInRect(
						this.rightDownPoint.sub(Vector2.one().mul(5)), this.rightDownPoint.add(Vector2.one().mul(5)))) {
			this.isChoosenRightDown = true;
		}

		if (this.mouseInputHandler.mouse.isPressed() && this.mouseInputHandler.mouse.getPressedTimes() % 2 == 0
				&& this.isChoosenRightDown == true) {
			this.isChoosenRightDown = false;
			Debug.log(Debug.DebugLevel.INFO, "调整过后，右下点坐标为：" + this.rightDownPoint);
		}

		if (this.isChoosenRightDown) {
			if (!this.rightDownPoint.isEqual(this.mouseInputHandler.mouse.getCurPos())) {
				this.rightDownPoint = this.mouseInputHandler.mouse.getCurPos();
			}
		}
	}

	public void draw(Graphics2D g) {
		if (!CollisionBox.isRender) {
			return;
		}
		// 渲染半透明部分
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(this.leftUpPoint.x, this.leftUpPoint.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// 渲染边框
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

		// 渲染两角的点
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
