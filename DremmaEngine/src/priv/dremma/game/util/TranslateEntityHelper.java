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
import priv.dremma.game.ui.UIEntity;

public class TranslateEntityHelper {
	public static HashMap<String, TranslateEntityHelper> translateEntities = new HashMap<String, TranslateEntityHelper>();

	public Entity entity;

	public static boolean shouldRender = true;
	public boolean shouldTransScreenPos;

	// x轴
	public Rect xAxis;
	public boolean choosenX;

	// y轴
	public Rect yAxis;
	public boolean choosenY;

	// 双轴
	public Rect xyAxis;
	public boolean choosenXY;
	
	public static boolean hasLock = false;
	public static TranslateEntityHelper lockedEntity = null;
	

	static String path = Resources.path + "data/translateEntities.dat"; // 数据文件目录

	public TranslateEntityHelper(Entity entity) {
		this.entity = entity;
		this.choosenX = false;
		this.choosenY = false;
		this.choosenXY = false;
		this.shouldTransScreenPos = entity instanceof UIEntity ? false : true;
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

		Vector2 worldPos = this.entity.position;
		Vector2 screenPos = worldPos;
		if (this.shouldTransScreenPos) {
			screenPos = GUtils.worldPixelToViewPort(worldPos);
		}

		// x
		if (this.choosenX == false) {
			AffineTransform xArrowTransform = new AffineTransform();
			xArrowTransform.translate(screenPos.x, screenPos.y);
			xArrowTransform.scale(length, 1);
			g.drawImage(Resources.loadImage(Resources.path + "images/xArrow.png"), xArrowTransform, null);

			Image xArrowHead = Resources.loadImage(Resources.path + "images/xArrowHead.png");
			Vector2 xArrowHeadScreenPos = new Vector2(
					this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
					this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2 + 1);
			if (this.shouldTransScreenPos) {
				xArrowHeadScreenPos = GUtils.worldPixelToViewPort(xArrowHeadScreenPos);
			}
			AffineTransform xArrowHeadTransform = new AffineTransform();
			xArrowHeadTransform.translate(xArrowHeadScreenPos.x, xArrowHeadScreenPos.y);
			xArrowHeadTransform.rotate(Math.toRadians(90));
			xArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(xArrowHead, xArrowHeadTransform, null);

			this.xAxis = new Rect(
					new Vector2(worldPos.x + 1 + xyScale, worldPos.y - xArrowHead.getWidth(null) * 0.25f / 2),
					new Vector2(worldPos.x + length + xArrowHead.getHeight(null) * 0.25f,
							worldPos.y + xArrowHead.getWidth(null) * 0.25f / 2 + 1f));
			this.xAxis.shouldTransScreenPos = this.shouldTransScreenPos;

		} else {
			AffineTransform xArrowTransform = new AffineTransform();
			xArrowTransform.translate(screenPos.x, screenPos.y);
			xArrowTransform.scale(length, 1);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), xArrowTransform, null);

			Image xArrowHead = Resources.loadImage(Resources.path + "images/choosenArrowHead.png");
			Vector2 xArrowHeadScreenPos = new Vector2(
					this.entity.position.x + length + xArrowHead.getHeight(null) * 0.25f,
					this.entity.position.y - xArrowHead.getWidth(null) * 0.25f / 2 + 1);
			if (this.shouldTransScreenPos) {
				xArrowHeadScreenPos = GUtils.worldPixelToViewPort(xArrowHeadScreenPos);
			}

			AffineTransform xArrowHeadTransform = new AffineTransform();
			xArrowHeadTransform.translate(xArrowHeadScreenPos.x, xArrowHeadScreenPos.y);
			xArrowHeadTransform.rotate(Math.toRadians(90));
			xArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(xArrowHead, xArrowHeadTransform, null);

			this.xAxis = new Rect(
					new Vector2(worldPos.x + 1 + xyScale, worldPos.y - xArrowHead.getWidth(null) * 0.25f / 2),
					new Vector2(worldPos.x + length + xArrowHead.getHeight(null) * 0.25f,
							worldPos.y + xArrowHead.getWidth(null) * 0.25f / 2 + 1f));
			this.xAxis.shouldTransScreenPos = this.shouldTransScreenPos;
		}

		// y
		if (this.choosenY == false) {
			AffineTransform yArrowTransform = new AffineTransform();
			yArrowTransform.translate(screenPos.x, screenPos.y);
			yArrowTransform.scale(1, length);
			g.drawImage(Resources.loadImage(Resources.path + "images/yArrow.png"), yArrowTransform, null);

			Image yArrowHead = Resources.loadImage(Resources.path + "images/yArrowHead.png");
			Vector2 yArrowHeadScreenPos = new Vector2(
					this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
					this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f);
			if (this.shouldTransScreenPos) {
				yArrowHeadScreenPos = GUtils.worldPixelToViewPort(yArrowHeadScreenPos);
			}
			AffineTransform yArrowHeadTransform = new AffineTransform();
			yArrowHeadTransform.translate(yArrowHeadScreenPos.x, yArrowHeadScreenPos.y);
			yArrowHeadTransform.rotate(Math.toRadians(180));
			yArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(yArrowHead, yArrowHeadTransform, null);

			this.yAxis = new Rect(
					new Vector2(worldPos.x - yArrowHead.getWidth(null) * 0.25f / 2, worldPos.y + 1 + xyScale),
					new Vector2(worldPos.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							worldPos.y + length + yArrowHead.getHeight(null) * 0.25f));
			this.yAxis.shouldTransScreenPos = this.shouldTransScreenPos;
		} else {
			AffineTransform yArrowTransform = new AffineTransform();
			yArrowTransform.translate(screenPos.x, screenPos.y);
			yArrowTransform.scale(1, length);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), yArrowTransform, null);

			Image yArrowHead = Resources.loadImage(Resources.path + "images/choosenArrowHead.png");
			Vector2 yArrowHeadScreenPos = new Vector2(
					this.entity.position.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
					this.entity.position.y + length + yArrowHead.getHeight(null) * 0.25f);
			if (this.shouldTransScreenPos) {
				yArrowHeadScreenPos = GUtils.worldPixelToViewPort(yArrowHeadScreenPos);
			}
			AffineTransform yArrowHeadTransform = new AffineTransform();
			yArrowHeadTransform.translate(yArrowHeadScreenPos.x, yArrowHeadScreenPos.y);
			yArrowHeadTransform.rotate(Math.toRadians(180));
			yArrowHeadTransform.scale(0.25, 0.25);
			g.drawImage(yArrowHead, yArrowHeadTransform, null);

			this.yAxis = new Rect(
					new Vector2(worldPos.x - yArrowHead.getWidth(null) * 0.25f / 2, worldPos.y + 1 + xyScale),
					new Vector2(worldPos.x + yArrowHead.getWidth(null) * 0.25f / 2 + 0.5f,
							worldPos.y + length + yArrowHead.getHeight(null) * 0.25f));
			this.yAxis.shouldTransScreenPos = this.shouldTransScreenPos;
		}

		// xy
		if (this.choosenXY == false) {
			AffineTransform choosen = new AffineTransform();
			choosen.translate(screenPos.x + 1, screenPos.y + 1);
			choosen.scale(xyScale, xyScale);
			g.drawImage(Resources.loadImage(Resources.path + "images/collisionBox.png"), choosen, null);

			this.xyAxis = new Rect(new Vector2(worldPos.x + 1, worldPos.y + 1),
					new Vector2(worldPos.x + 1 + xyScale, worldPos.y + 1 + xyScale));
			this.xyAxis.shouldTransScreenPos = this.shouldTransScreenPos;
		} else {
			AffineTransform choosen = new AffineTransform();
			choosen.translate(screenPos.x + 1, screenPos.y + 1);
			choosen.scale(xyScale, xyScale);
			g.drawImage(Resources.loadImage(Resources.path + "images/choosen.png"), choosen, null);

			this.xyAxis = new Rect(new Vector2(worldPos.x + 1, worldPos.y + 1),
					new Vector2(worldPos.x + 1 + xyScale, worldPos.y + 1 + xyScale));
			this.xyAxis.shouldTransScreenPos = this.shouldTransScreenPos;
		}
	}

	/**
	 * 从文件中加载移动帮助数据
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

			if (name.contains("Attack")) {
				continue;
			}

			TranslateEntityHelper.translateEntities.get(name).entity.position = position;
		}
	}

	/**
	 * 将移动帮助数据存进文件
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
