package priv.dremma.game.collision;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.IOHelper;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Vector2;

/**
 * 2D 碰撞盒
 * 
 * @author guoyi
 *
 */
public class CollisionBox {

	// 场景中所有的碰撞盒
	public static HashMap<String, CollisionBox> collisionBoxs = new HashMap<String, CollisionBox>();
	// 不进行数据加载的碰撞盒名称子串队列
	public static Queue<String> notLoadNameSubStrings = new LinkedList<String>();

	public Vector2 leftUpPoint; // 左上点
	public Vector2 rightDownPoint; // 右下点

	static Image collisionBox = Resources.loadImage(Resources.path + "images/collisionBox.png");
	static Image border = Resources.loadImage(Resources.path + "images/border.png");

	public static boolean shouldRender = false;
	public static int pressedTimes = 0;
	public boolean isTrigger;
	public boolean isChoosenLeftUp;
	public boolean isChoosenRightDown;

	static String path = Resources.path + "data/collisionBox.dat"; // 数据文件目录

	public CollisionBox(Vector2 leftUpPoint, Vector2 rightDownPoint) {
		this.leftUpPoint = new Vector2(leftUpPoint);
		this.rightDownPoint = new Vector2(rightDownPoint);
		this.isChoosenLeftUp = false;
		this.isTrigger = false; // 默认不为触发器
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

	/**
	 * 渲染碰撞盒
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if (!CollisionBox.shouldRender) {
			return;
		}
		// 渲染半透明部分
		Vector2 collisionBoxScreenPos = GUtils.worldPixelToViewPort(this.leftUpPoint);
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(collisionBoxScreenPos.x, collisionBoxScreenPos.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// 渲染边框
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

		// 渲染两角的点
		int pointWidth = 5;
		Vector2 leftUpPointScreenPos = GUtils
				.worldPixelToViewPort(new Vector2(this.leftUpPoint.x - pointWidth, this.leftUpPoint.y - pointWidth));
		AffineTransform leftUpPointTransform = new AffineTransform();
		leftUpPointTransform.translate(leftUpPointScreenPos.x, leftUpPointScreenPos.y);
		leftUpPointTransform.scale(pointWidth * 2, pointWidth * 2);
		g.drawImage(border, leftUpPointTransform, null);

		Vector2 rightDownPointScreenPos = GUtils.worldPixelToViewPort(
				new Vector2(this.rightDownPoint.x - pointWidth, this.rightDownPoint.y - pointWidth));
		AffineTransform rightDownPointTransform = new AffineTransform();
		rightDownPointTransform.translate(rightDownPointScreenPos.x, rightDownPointScreenPos.y);
		rightDownPointTransform.scale(pointWidth * 2, pointWidth * 2);
		g.drawImage(border, rightDownPointTransform, null);
	}

	/**
	 * 碰撞检测：对移动的物体进行碰撞检测
	 */
	public static synchronized void collisionDetection() {
		HashMap<String, CollisionBox> tmpCollisionBoxs = new HashMap<String, CollisionBox>();
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			tmpCollisionBoxs.put(entry.getKey(), entry.getValue());
		}

		Iterator<Entry<String, CollisionBox>> tmpCollisionBoxsIterator = tmpCollisionBoxs.entrySet().iterator();
		while (tmpCollisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) tmpCollisionBoxsIterator
					.next();
			String name = entry.getKey();
			CollisionBox collisionBox = entry.getValue();
			Entity entity = TileMap.getEntity(name);
			if (entity == null) {
				continue;
			}
			if (entity.detectCollision == false) {
				continue;
			}
			if (!entity.moveVector.add(entity.retreatVector).isEqual(Vector2.zero())) {
				Iterator<Entry<String, CollisionBox>> otherIterator = tmpCollisionBoxs.entrySet().iterator();
				while (otherIterator.hasNext()) {
					HashMap.Entry<String, CollisionBox> otherEntry = (HashMap.Entry<String, CollisionBox>) otherIterator
							.next();
					String otherName = otherEntry.getKey();
					if (TileMap.getEntity(otherName) == null) {
						continue;
					}
					if (TileMap.getEntity(otherName).detectCollision == false) {
						continue;
					}
					if (!otherName.equals(name)) {
						Entity otherEntity = TileMap.getEntity(otherName);
						CollisionBox nextCollisionBox = collisionBox
								.translate(entity.moveVector.add(entity.retreatVector));
						CollisionBox otherCollisionBox = otherEntry.getValue();
						if (otherCollisionBox.isTrigger) {
							// 进行触发检测
							if (collisionBox.isIntersected(otherCollisionBox) == false
									&& nextCollisionBox.isIntersected(otherCollisionBox) == true) {
								// 进入触发
								collisionBox.onTriggerEnter(entity, otherEntity);
							}
							
							if (collisionBox.isIntersected(otherCollisionBox) == true
									&& nextCollisionBox.isIntersected(otherCollisionBox) == false) {
								// 退出触发
								collisionBox.onTriggerExit(entity, otherEntity);
							}
						} else {
							// 进行碰撞检测
							// 如果otherEntity的碰撞盒是攻击碰撞盒
							if (otherEntity instanceof AttackEntity) {
								// 什么都不做
								continue;
							}
							// 如果entity的碰撞盒是攻击碰撞盒
							if (entity instanceof AttackEntity) {
								if (otherEntity.getImage() == null) { // 确保对有图片的实体进行碰撞检测，攻击型实体的碰撞检测是与图片进行的，不是碰撞盒
									continue;
								}
								// 攻击时，应检测下一entity碰撞盒是否与otherEntity的下一图片相交
								CollisionBox otherImageCollisionBox = new CollisionBox(
										new Vector2(
												otherEntity.position
														.sub(new Vector2(
																otherEntity.getWidth() * otherEntity.getScale().x / 2f,
																otherEntity.getHeight() * otherEntity.getScale().y
																		/ 2f))),
										new Vector2(otherEntity.position
												.add(new Vector2(otherEntity.getWidth() * otherEntity.getScale().x / 2f,
														otherEntity.getHeight() * otherEntity.getScale().y / 2f))));

								CollisionBox nextOtherImageCollisionBox = otherImageCollisionBox
										.translate(otherEntity.moveVector.add(otherEntity.retreatVector));

								if (nextCollisionBox.isIntersected(nextOtherImageCollisionBox) == true) {
									collisionBox.onCollision(entity, otherEntity);
								}
							}
							// 如果另一个实体静止
							else if (otherEntity.moveVector.isEqual(Vector2.zero())) {
								if (collisionBox.isIntersected(otherCollisionBox) == false
										&& nextCollisionBox.isIntersected(otherCollisionBox) == true) {
									Vector2 offset = nextCollisionBox.leftUpPoint.sub(collisionBox.leftUpPoint);
									// 发生了碰撞
									entity.position = entity.position.sub(offset);
									collisionBox.trans(offset.mul(-1));

									collisionBox.onCollision(entity, otherEntity);
								} else if (collisionBox.isIntersected(otherCollisionBox) == true
										&& nextCollisionBox.isIntersected(otherCollisionBox) == true) {
									// 修复穿模
									entity.position = entity.position
											.sub(entity.moveVector.add(entity.retreatVector).mul(2));
									collisionBox.trans(entity.moveVector.add(entity.retreatVector).mul(-2f));
								}
							} else if (!otherEntity.moveVector.isEqual(Vector2.zero())) {
								// 如果另一个实体也运动
								CollisionBox nextOtherCollisionBox = otherCollisionBox
										.translate(otherEntity.moveVector.add(otherEntity.retreatVector));

								if (nextCollisionBox.isIntersected(nextOtherCollisionBox) == true) {
									Vector2 offset = collisionBox.leftUpPoint.sub(otherCollisionBox.leftUpPoint)
											.normalized();
									// 发生了碰撞
									entity.position = entity.position.add(offset.mul(2));
									collisionBox.trans(offset.mul(2));

									collisionBox.onCollision(entity, otherEntity);
								}
							}

						}
					}
				}
			}
		}
	}

	/**
	 * 碰撞盒撞到别的碰撞盒时调用
	 * 
	 * @param entity      本实体
	 * @param otherEntity 被撞到的实体
	 */
	public void onCollision(Entity entity, Entity otherEntity) {
	}

	/**
	 * 碰撞盒触发进入触发器碰撞盒时调用
	 * 
	 * @param entity      本实体
	 * @param otherEntity 被撞到的实体
	 */
	public void onTriggerEnter(Entity entity, Entity otherEntity) {
	}
	
	/**
	 * 碰撞盒触发退出触发器碰撞盒时调用
	 * @param entity
	 * @param otherEntity
	 */
	private void onTriggerExit(Entity entity, Entity otherEntity) {
	}

	/**
	 * 判断两个碰撞盒是否相交
	 * 
	 * @param otherCollisionBox
	 * @return
	 */
	private boolean isIntersected(CollisionBox otherCollisionBox) {
		float thisXMin = this.leftUpPoint.x;
		float thisXMax = this.rightDownPoint.x;
		float thisYMin = this.leftUpPoint.y;
		float thisYMax = this.rightDownPoint.y;

		float otherXMin = otherCollisionBox.leftUpPoint.x;
		float otherXMax = otherCollisionBox.rightDownPoint.x;
		float otherYMin = otherCollisionBox.leftUpPoint.y;
		float otherYMax = otherCollisionBox.rightDownPoint.y;

		float xMin = Math.min(thisXMin, otherXMin);
		float xMax = Math.max(thisXMax, otherXMax);
		float yMin = Math.min(thisYMin, otherYMin);
		float yMax = Math.max(thisYMax, otherYMax);

		if (FloatCompare.isLessOrEqual(xMax - xMin, thisXMax - thisXMin + otherXMax - otherXMin)
				&& FloatCompare.isLessOrEqual(yMax - yMin, thisYMax - thisYMin + otherYMax - otherYMin)) {
			return true;
		}
		return false;
	}

	/**
	 * 对碰撞盒进行平移
	 * 
	 * @param moveVector
	 * @return
	 */
	private CollisionBox translate(Vector2 moveVector) {
		return new CollisionBox(new Vector2(this.leftUpPoint.add(moveVector)),
				new Vector2(this.rightDownPoint.add(moveVector)));
	}

	/**
	 * 对碰撞盒进行平移
	 * 
	 * @param moveVector
	 * @return
	 */
	public void trans(Vector2 moveVector) {
		this.leftUpPoint = this.leftUpPoint.add(moveVector);
		this.rightDownPoint = this.rightDownPoint.add(moveVector);
	}

	/**
	 * 从文件中加载碰撞盒数据
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		String name;
		Vector2 leftUpPoint, rightDownPoint;
		Queue<Object> objs = (Queue<Object>) IOHelper.readObject(path);
		while (!objs.isEmpty()) {
			name = (String) objs.peek();
			objs.remove(name);
			leftUpPoint = (Vector2) objs.peek();
			objs.remove(leftUpPoint);
			rightDownPoint = (Vector2) objs.peek();
			objs.remove(rightDownPoint);

			boolean willLoad = true; // 是否加载到游戏
			for (String s : CollisionBox.notLoadNameSubStrings) {
				if (name.contains(s)) {
					willLoad = false;
				}
			}
			if (!willLoad) {
				continue; // 加载下一个
			}

			CollisionBox.collisionBoxs.get(name).setPos(leftUpPoint, rightDownPoint);
		}
	}

	/**
	 * 将碰撞盒数据存进文件
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