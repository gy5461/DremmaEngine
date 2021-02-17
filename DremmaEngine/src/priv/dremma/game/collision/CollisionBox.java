package priv.dremma.game.collision;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.FightingNPC;
import priv.dremma.game.entities.NPC;
import priv.dremma.game.entities.Player;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
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

	public String name = null; // ��ײ������

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

	/**
	 * ��Ⱦ��ײ��
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if (this.name != null && TileMap.getEntity(this.name).detectCollision == false) {
			return;
		}

		if (!CollisionBox.shouldRender) {
			return;
		}
		// ��Ⱦ��͸������
		Vector2 collisionBoxScreenPos = GUtils.worldPixelToViewPort(this.leftUpPoint);
		AffineTransform collisionBoxTransform = new AffineTransform();
		collisionBoxTransform.translate(collisionBoxScreenPos.x, collisionBoxScreenPos.y);
		collisionBoxTransform.scale(this.getWidth(), this.getHeight());
		g.drawImage(collisionBox, collisionBoxTransform, null);

		// ��Ⱦ�߿�
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

		// ��Ⱦ���ǵĵ�
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
	 * ��ײ��⣺���ƶ������������ײ���
	 */
	public static synchronized void collisionDetection() {
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			String name = entry.getKey();
			CollisionBox collisionBox = entry.getValue();
			Entity entity = TileMap.getEntity(name);
			if (entity.detectCollision == false) {
				continue;
			}
			if (!entity.moveVector.add(entity.retreatVector).isEqual(Vector2.zero())) {
				Iterator<Entry<String, CollisionBox>> otherIterator = CollisionBox.getCollisionBoxsIterator();
				while (otherIterator.hasNext()) {
					HashMap.Entry<String, CollisionBox> otherEntry = (HashMap.Entry<String, CollisionBox>) otherIterator
							.next();
					String otherName = otherEntry.getKey();
					if (TileMap.getEntity(otherName).detectCollision == false) {
						continue;
					}
					if (!otherName.equals(name)) {
						CollisionBox otherCollisionBox = otherEntry.getValue();
						if (otherCollisionBox.isTrigger) {
							// ���д������
							if (collisionBox.isIntersected(otherCollisionBox) == true) {
								// �����˴���
								collisionBox.onTriggerEnter(name, otherName);
							}
						} else {
							// ������ײ���
							Entity otherEntity = TileMap.getEntity(otherName);
							// ���otherEntity����ײ���ǹ�����ײ��
							if (otherName.contains("Attack")) {
								// ʲô������
							}
							// ���entity����ײ���ǹ�����ײ��
							else if (name.contains("Attack")) {
								if (!((otherEntity instanceof Player) || (otherEntity instanceof FightingNPC))) {
									continue;
								}
								CollisionBox nextCollisionBox = collisionBox.translate(entity.moveVector.add(entity.retreatVector));
								// ����ʱ��Ӧ�����һentity��ײ���Ƿ���otherEntity����һͼƬ�ཻ
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
									collisionBox.onCollision(name, otherName);
								}
							} else if (otherEntity.moveVector.isEqual(Vector2.zero())) {
								// �����һ��ʵ�徲ֹ���ұ���ײ������һ��ײ�ж����ǹ�����ײ��
								if (otherName.equals("Player") || otherName.contains("NPC")) {
									continue;
								}
								CollisionBox nextCollisionBox = collisionBox.translate(entity.moveVector.add(entity.retreatVector));

								if (collisionBox.isIntersected(otherCollisionBox) == false
										&& nextCollisionBox.isIntersected(otherCollisionBox) == true) {
									Vector2 offset = nextCollisionBox.leftUpPoint.sub(collisionBox.leftUpPoint);
									// ��������ײ
									entity.position = entity.position.sub(offset);
									collisionBox.trans(offset.mul(-1));

									collisionBox.onCollision(name, otherName);
								} else if (collisionBox.isIntersected(otherCollisionBox) == true
										&& nextCollisionBox.isIntersected(otherCollisionBox) == true) {
									// �޸���ģ
									entity.position = entity.position.sub(entity.moveVector.add(entity.retreatVector).mul(2));
									collisionBox.trans(entity.moveVector.add(entity.retreatVector).mul(-2f));
								}
							}

						}
					}
				}
			}
		}
	}

	/**
	 * ��ײ��ײ�������ײ��ʱ����
	 * 
	 * @param name      ����ײ�е�����
	 * @param otherName ��ײ������ײ�е�����
	 */
	public void onCollision(String name, String otherName) {
		// Debug.log(Debug.DebugLevel.INFO, name + " ײ����:" + otherName);

		Entity entity = TileMap.getEntity(name);
		Entity otherEntity = TileMap.getEntity(otherName);

		// ------------------��������--------------------
		if (name.contains("Attack") && ((AttackEntity) entity).willCauseWound) {
			if (name.contains("player")) {
				if (otherEntity instanceof FightingNPC) {
					// �����ʵ����ս����NPC�����NPC����
					// ����
					otherEntity.retreat(entity.direction, 100f);
					
					AudioManager.getInstance().playOnce("ghostWoundedSound");
					((AttackEntity) entity).willCauseWound = false;
					Debug.log(Debug.DebugLevel.INFO, name + " �˺���:" + otherName);
				}
			} else if (name.contains("npc")) {
				if (otherEntity instanceof Player) {
					// npc���������
					// ����
					otherEntity.retreat(entity.direction, 50f);
					
					AudioManager.getInstance().playOnce("playerWoundedSound");
					((AttackEntity) entity).willCauseWound = false;
					Debug.log(Debug.DebugLevel.INFO, name + " �˺���:" + otherName);
				}
			}

		}

		// ------------------NPC�����ϰ��ﴦ��--------------------
		if (entity instanceof NPC) {
			// ��ײ����ʱ��ת��
			switch (entity.direction) {
			case UP:
				entity.direction = Entity.EntityDirection.LEFT;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(-1, 1))
						.mul(Time.deltaTime);
				break;
			case DOWN:
				entity.direction = Entity.EntityDirection.RIGHT;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(1, -1))
						.mul(Time.deltaTime);
				break;
			case LEFT:
				entity.direction = Entity.EntityDirection.DOWN;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier)).mul(new Vector2(1, 1))
						.mul(Time.deltaTime);
				break;
			case RIGHT:
				entity.direction = Entity.EntityDirection.UP;
				entity.moveVector = (new Vector2(entity.speed, entity.speed * TileMap.modifier))
						.mul(new Vector2(-1, -1)).mul(Time.deltaTime);
				break;
			}

			((NPC) entity).startPos = entity.position;
			((NPC) entity).endPos = ((NPC) entity).startPos.add(entity.moveVector.mul(((NPC) entity).totalDistance));
			entity.retreatVector = Vector2.zero();
		}
	}

	/**
	 * ��ײ�д�����Ĵ�������ײ��ʱ����
	 * 
	 * @param name      ����ײ�е�����
	 * @param otherName ��ײ������ײ�е�����
	 */
	public void onTriggerEnter(String name, String otherName) {
		// Debug.log(Debug.DebugLevel.INFO, name + " ������:" + otherName);
	}

	/**
	 * �ж�������ײ���Ƿ��ཻ
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
		Queue<Object> objs = (Queue<Object>) IOHelper.readObject(path);
		while (!objs.isEmpty()) {
			name = (String) objs.peek();
			objs.remove(name);
			leftUpPoint = (Vector2) objs.peek();
			objs.remove(leftUpPoint);
			rightDownPoint = (Vector2) objs.peek();
			objs.remove(rightDownPoint);
			if (name.contains("Ұ��")) {
				continue;
			}
			if (name.contains("mapBorder")) {
				continue;
			}
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
