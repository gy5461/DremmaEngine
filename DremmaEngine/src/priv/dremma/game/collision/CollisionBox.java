package priv.dremma.game.collision;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.IOHelper;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

/**
 * 2D ��ײ��
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

	public static boolean shouldRender = true;
	public static int pressedTimes = 0;
	public boolean isTrigger;
	public boolean isChoosenLeftUp;
	public boolean isChoosenRightDown;

	static String path = Resources.path + "data/collisionBox.dat"; // �����ļ�Ŀ¼

	public CollisionBox(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = new Vector2(leftUpPoint);
		this.rightDownPoint = new Vector2(rightDownPoint);
		this.isChoosenLeftUp = false;
		this.isTrigger = false; // Ĭ�ϲ�Ϊ������
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
		if (!CollisionBox.shouldRender) {
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

	/**
	 * ��ײ��⣺���ƶ������������ײ���
	 */
	public static void collisionDetection() {
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			String name = entry.getKey();
			CollisionBox collisionBox = entry.getValue();
			Entity entity;
			if (name == "Player") {
				entity = TileMap.player;
			} else {
				entity = TileMap.entities.get(name);
			}

			if (!entity.moveVector.isEqual(Vector2.zero())) {
				Iterator<Entry<String, CollisionBox>> anotherIterator = CollisionBox.getCollisionBoxsIterator();
				while (anotherIterator.hasNext()) {
					HashMap.Entry<String, CollisionBox> anotherEntry = (HashMap.Entry<String, CollisionBox>) anotherIterator
							.next();
					String anotherName = anotherEntry.getKey();
					if (!anotherName.equals(name)) {
						CollisionBox anotherCollisionBox = anotherEntry.getValue();
						if (anotherCollisionBox.isTrigger) {
							// ���д������
							if (collisionBox.isIntersected(anotherCollisionBox) == true) {
								// ��������ײ
								Debug.log(Debug.DebugLevel.INFO, name + " ������:" + anotherName);
							}
						} else {
							// ������ײ���
							CollisionBox nextCollisionBox = collisionBox
									.translate(entity.moveVector.mul(Time.deltaTime));
							if (collisionBox.isIntersected(anotherCollisionBox) == false
									&& nextCollisionBox.isIntersected(anotherCollisionBox) == true) {
								// ��������ײ
								Debug.log(Debug.DebugLevel.INFO, name + " ײ����:" + anotherName);
								entity.position = entity.position
										.sub(nextCollisionBox.leftUpPoint.sub(collisionBox.leftUpPoint));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * �ж�������ײ���Ƿ��ཻ
	 * 
	 * @param anotherCollisionBox
	 * @return
	 */
	private boolean isIntersected(CollisionBox anotherCollisionBox) {
		float thisXMin = this.leftUpPoint.x;
		float thisXMax = this.rightDownPoint.x;
		float thisYMin = this.leftUpPoint.y;
		float thisYMax = this.rightDownPoint.y;

		float anotherXMin = anotherCollisionBox.leftUpPoint.x;
		float anotherXMax = anotherCollisionBox.rightDownPoint.x;
		float anotherYMin = anotherCollisionBox.leftUpPoint.y;
		float anotherYMax = anotherCollisionBox.rightDownPoint.y;

		float xMin = Math.min(thisXMin, anotherXMin);
		float xMax = Math.max(thisXMax, anotherXMax);
		float yMin = Math.min(thisYMin, anotherYMin);
		float yMax = Math.max(thisYMax, anotherYMax);

		if (FloatCompare.isLessOrEqual(xMax - xMin, thisXMax - thisXMin + anotherXMax - anotherXMin)
				&& FloatCompare.isLessOrEqual(yMax - yMin, thisYMax - thisYMin + anotherYMax - anotherYMin)) {
			return true;
		}
		return false;
	}

	/**
	 * ����ײ�н���ƽ��
	 * 
	 * @param moveVector
	 * @return
	 */
	private CollisionBox translate(Vector2 moveVector) {
		return new CollisionBox(new Vector2(this.leftUpPoint.add(moveVector)),
				new Vector2(this.rightDownPoint.add(moveVector)));
	}
	
	/**
	 * ����ײ�н���ƽ��
	 * 
	 * @param moveVector
	 * @return
	 */
	public void trans(Vector2 moveVector) {
		this.leftUpPoint = this.leftUpPoint.add(moveVector);
		this.rightDownPoint = this.rightDownPoint.add(moveVector);
	}

	/**
	 * ���ļ��м�����ײ������
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		String name;
		Vector2 leftUpPoint, rightDownPoint;
		Queue<Object> objs = (Queue<Object>)IOHelper.readObject(path);
		while(!objs.isEmpty()) {
			name = (String)objs.peek();
			objs.remove(name);
			leftUpPoint = (Vector2)objs.peek();
			objs.remove(leftUpPoint);
			rightDownPoint = (Vector2)objs.peek();
			objs.remove(rightDownPoint);
			
			CollisionBox.collisionBoxs.get(name).setPos(leftUpPoint, rightDownPoint);
		}
	}

	/**
	 * ����ײ�����ݴ���ļ�
	 */
	public static void save() {
		Queue<Object> objs = new LinkedList<Object>();
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			String name = entry.getKey();
			CollisionBox collisionBox = entry.getValue();
			
			objs.add(name);
			objs.add(collisionBox.leftUpPoint);
			objs.add(collisionBox.rightDownPoint);
		}
		IOHelper.writeObject(path, objs);
	}
}
