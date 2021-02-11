package priv.dremma.game.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import priv.dremma.game.entities.Entity;

public class TranslateEntityHelper {
	public static HashMap<String, TranslateEntityHelper> translateEntities = new HashMap<String, TranslateEntityHelper>();

	public Entity entity;

	public static boolean shouldRender = true;

	// x轴
	public Rect xAxis;
	public boolean choosenX;

	// y轴
	public Rect yAxis;
	public boolean choosenY;

	// 双轴
	public Rect xyAxis;
	public boolean choosenXY;

	static String path = Resources.path + "data/translateEntities.dat"; // 数据文件目录

	public TranslateEntityHelper(Entity entity) {
		this.entity = entity;
		this.choosenX = false;
		this.choosenY = false;
		this.choosenXY = false;
	}

	public static Iterator<Entry<String, TranslateEntityHelper>> getTranslateEntitiesHelperIterator() {
		return TranslateEntityHelper.translateEntities.entrySet().iterator();
	}

	public void draw(Graphics2D g) {
		if (this.entity.visible == false) {
			return;
		}
		if (!TranslateEntityHelper.shouldRender) {
			return;
		}
		// 绘制箭头
		float length = 50f;
		float xyScale = 15f; // xy的方块大小

		// x
		if (this.choosenX == false) {
			Vector2 xArrowScreenPos = GUtils.worldPixelToViewPort(this.entity.position);
			AffineTransform xArrowTransform = new AffineTransform();
			xArrowTransform.translate(xArrowScreenPos.x, xArrowScreenPos.y);
			xArrowTransform.scale(length, 1);
			g.drawImage(Resources.loadImage(Resources.path + "images/xArrow.png"), xArrowTransform, null);

			Image xArrowHead = Resources.loadImage(Resources.path + "images/xArrowHead.png");
			Vector2 xArrowHeadScreenPos = GUtils.worldPixelToViewPort(
					new Vector2(this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
							this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2 + 1));
			AffineTransform xArrowHeadTransform = new AffineTransform();
			xArrowHeadTransform.translate(xArrowHeadScreenPos.x, xArrowHeadScreenPos.y);
			xArrowHeadTransform.rotate(Math.toRadians(90));
			xArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(xArrowHead, xArrowHeadTransform, null);

			this.xAxis = new Rect(
					new Vector2(this.entity.position.x + 1 + xyScale,
							this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2),
					new Vector2(this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
							this.entity.position.y + xArrowHead.getWidth(null) * 0.25f / 2 + 1f));
		} else {
			Vector2 xArrowScreenPos = GUtils.worldPixelToViewPort(this.entity.position);
			AffineTransform xArrowTransform = new AffineTransform();
			xArrowTransform.translate(xArrowScreenPos.x, xArrowScreenPos.y);
			xArrowTransform.scale(length, 1);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), xArrowTransform, null);

			Image xArrowHead = Resources.loadImage(Resources.path + "images/choosenArrowHead.png");
			Vector2 xArrowHeadScreenPos = GUtils.worldPixelToViewPort(
					new Vector2(this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
							this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2 + 1));
			AffineTransform xArrowHeadTransform = new AffineTransform();
			xArrowHeadTransform.translate(xArrowHeadScreenPos.x, xArrowHeadScreenPos.y);
			xArrowHeadTransform.rotate(Math.toRadians(90));
			xArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(xArrowHead, xArrowHeadTransform, null);

			this.xAxis = new Rect(
					new Vector2(this.entity.position.x + 1 + xyScale,
							this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2),
					new Vector2(this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
							this.entity.position.y + xArrowHead.getWidth(null) * 0.25f / 2 + 1f));
		}

		// y
		if (this.choosenY == false) {
			Vector2 yArrowScreenPos = GUtils.worldPixelToViewPort(this.entity.position);
			AffineTransform yArrowTransform = new AffineTransform();
			yArrowTransform.translate(yArrowScreenPos.x, yArrowScreenPos.y);
			yArrowTransform.scale(1, length);
			g.drawImage(Resources.loadImage(Resources.path + "images/yArrow.png"), yArrowTransform, null);

			Image yArrowHead = Resources.loadImage(Resources.path + "images/yArrowHead.png");
			Vector2 yArrowHeadScreenPos = GUtils.worldPixelToViewPort(
					new Vector2(this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f));
			AffineTransform yArrowHeadTransform = new AffineTransform();
			yArrowHeadTransform.translate(yArrowHeadScreenPos.x, yArrowHeadScreenPos.y);
			yArrowHeadTransform.rotate(Math.toRadians(180));
			yArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(yArrowHead, yArrowHeadTransform, null);

			this.yAxis = new Rect(
					new Vector2(this.entity.position.x - yArrowHead.getWidth(null) * 0.25f / 2,
							this.entity.position.y + 1 + xyScale),
					new Vector2(this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f));
		} else {
			Vector2 yArrowScreenPos = GUtils.worldPixelToViewPort(this.entity.position);
			AffineTransform yArrowTransform = new AffineTransform();
			yArrowTransform.translate(yArrowScreenPos.x, yArrowScreenPos.y);
			yArrowTransform.scale(1, length);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), yArrowTransform, null);

			Image yArrowHead = Resources.loadImage(Resources.path + "images/choosenArrowHead.png");
			Vector2 yArrowHeadScreenPos = GUtils.worldPixelToViewPort(
					new Vector2(this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f));
			AffineTransform yArrowHeadTransform = new AffineTransform();
			yArrowHeadTransform.translate(yArrowHeadScreenPos.x, yArrowHeadScreenPos.y);
			yArrowHeadTransform.rotate(Math.toRadians(180));
			yArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(yArrowHead, yArrowHeadTransform, null);

			this.yAxis = new Rect(
					new Vector2(this.entity.position.x - yArrowHead.getWidth(null) * 0.25f / 2,
							this.entity.position.y + 1 + xyScale),
					new Vector2(this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f));
		}

		// xy
		if (this.choosenXY == false) {
			Vector2 xyScreenPos = GUtils
					.worldPixelToViewPort(new Vector2(this.entity.position.x + 1, this.entity.position.y + 1));
			AffineTransform choosen = new AffineTransform();
			choosen.translate(xyScreenPos.x, xyScreenPos.y);
			choosen.scale(xyScale, xyScale);
			g.drawImage(Resources.loadImage(Resources.path + "images/collisionBox.png"), choosen, null);

			this.xyAxis = new Rect(new Vector2(this.entity.position.x + 1, this.entity.position.y + 1),
					new Vector2(this.entity.position.x + 1 + xyScale, this.entity.position.y + 1 + xyScale));
		} else {
			Vector2 xyScreenPos = GUtils
					.worldPixelToViewPort(new Vector2(this.entity.position.x + 1, this.entity.position.y + 1));
			AffineTransform choosen = new AffineTransform();
			choosen.translate(xyScreenPos.x, xyScreenPos.y);
			choosen.scale(xyScale, xyScale);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), choosen, null);

			this.xyAxis = new Rect(new Vector2(this.entity.position.x + 1, this.entity.position.y + 1),
					new Vector2(this.entity.position.x + 1 + xyScale, this.entity.position.y + 1 + xyScale));
		}
	}

	/**
	 * 从文件中加载碰撞盒数据
	 */
	@SuppressWarnings("unchecked")
	public static void load() {
		String name;
		Vector2 position;
		Queue<Object> objs = (Queue<Object>) IOHelper.readObject(path);
		while (!objs.isEmpty()) {
			name = (String) objs.peek();
			objs.remove(name);
			position = (Vector2) objs.peek();
			objs.remove(position);

			if (name.contains("野鬼")) {
				continue;
			}
			TranslateEntityHelper.translateEntities.get(name).entity.position = position;
		}
	}

	/**
	 * 将碰撞盒数据存进文件
	 */
	public static void save() {
		Queue<Object> objs = new LinkedList<Object>();
		Iterator<Entry<String, TranslateEntityHelper>> translateEntitiesIterator = TranslateEntityHelper
				.getTranslateEntitiesHelperIterator();
		while (translateEntitiesIterator.hasNext()) {
			HashMap.Entry<String, TranslateEntityHelper> entry = (HashMap.Entry<String, TranslateEntityHelper>) translateEntitiesIterator
					.next();
			String name = entry.getKey();
			TranslateEntityHelper translateEntity = entry.getValue();

			objs.add(name);
			objs.add(translateEntity.entity.position);
		}
		IOHelper.writeObject(path, objs);
	}
}
