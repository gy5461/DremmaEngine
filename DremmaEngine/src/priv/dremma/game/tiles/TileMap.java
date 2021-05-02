package priv.dremma.game.tiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map.Entry;

import priv.dremma.game.GameCore;
import priv.dremma.game.entities.AttackEntity;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;

/**
 * TileMap 类包含地砖地图的数据、实体
 * 
 * @author guoyi
 *
 */
public class TileMap {
	// 地砖编号与地砖对应的表，加载地图时使用
	public static HashMap<String, Image> tilesTable = new HashMap<String, Image>();

	private Image[][] tiles; // 地砖
	private Vector2 scale;
	public static final Vector2 TILE_SIZE = new Vector2(130, 76);
	public static float modifier = TileMap.TILE_SIZE.y / TileMap.TILE_SIZE.x; // 2.5D视角时，需要进行速度修正才不会走歪
	public static HashMap<String, Entity> entities; // 游戏中的其他实体
	public static Entity player; // 主角
	PriorityQueue<Entity> renderEntities; // 渲染优先队列
	Queue<Entity> updateEntities; // 更新队列

	public Vector2 worldEndTileCenter = Vector2.zero();

	/**
	 * 生成指定宽度与高度的TileMap
	 * 
	 * @param width
	 * @param height
	 */
	public TileMap(int width, int height) {
		tiles = new Image[width][height];
		entities = new HashMap<String, Entity>();
		renderEntities = new PriorityQueue<Entity>(1, new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				// 解决人树问题
				return FloatCompare.isLess(o1.getBottom(), o2.getBottom()) ? -1 : 1;
			}

		});
		this.updateEntities = new LinkedList<Entity>();
		this.scale = Vector2.one();
	}

	public void setScale(Vector2 scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
	}

	public static Vector2 getSize() {
		return TileMap.TILE_SIZE;
	}

	/**
	 * 获取TileMap的宽度，即水平方向的地砖个数
	 */
	public int getWidth() {
		return tiles.length;
	}

	/**
	 * 获取TileMap的高度，即垂直方向的地砖个数
	 */
	public int getHeight() {
		return tiles[0].length;
	}

	/**
	 * 取得指定位置的地砖，越界时返回null
	 * 
	 * @param x 地砖的横坐标
	 * @param y 地砖的纵坐标
	 * @return
	 */
	public Image getTile(int x, int y) {
		if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) {
			return null;
		}
		return tiles[x][y];
	}

	/**
	 * 设置指定位置的地砖
	 * 
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Image tile) {
		tiles[x][y] = tile;
	}

	/**
	 * 获取游戏主角对象
	 * 
	 * @return
	 */
	public Entity getPlayer() {
		return player;
	}

	/**
	 * 设置游戏主角对象
	 * 
	 * @param player
	 */
	public void setPlayer(Entity player) {
		TileMap.player = player;
		player.onStart();
	}

	/**
	 * 添加实体到地图中
	 * 
	 * @param entity
	 */
	public static void addEntity(String name, Entity entity) {
		if (TileMap.entities.containsKey(name)) {
			return;
		}
		entities.put(name, entity);
		entity.onStart();
		if (name.contains("mapBorder")) {
			return;
		}
		if (entity instanceof AttackEntity) {
			return;
		}
		if (TranslateEntityHelper.translateEntities.containsKey(name)) {
			return;
		}
		TranslateEntityHelper translateEntityHelper = new TranslateEntityHelper(entity);
		TranslateEntityHelper.translateEntities.put(name, translateEntityHelper);
	}

	public static Entity getEntity(String name) {
		if (name.equals(TileMap.player.name)) {
			return TileMap.player;
		}
		if (TileMap.entities.containsKey(name)) {
			return TileMap.entities.get(name);
		}
		return null;
	}

	/**
	 * 从地图中删除实体
	 * 
	 * @param entity
	 */
	public void removeEntity(String name) {
		entities.get(name).onDestroy();
		entities.remove(name);
	}

	/**
	 * 取得这个地图中所有实体（除主角对象外）的迭代器
	 */
	public static Iterator<Entry<String, Entity>> getEntitiesIterator() {
		return entities.entrySet().iterator();
	}

	/**
	 * 根据文件加载地图
	 */
	public static TileMap loadTileMap(String path) {
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;
		TileMap resultMap = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while (true) {
				String line;
				try {
					line = reader.readLine();
					if (line == null) {
						reader.close();
						break;
					}
					if (!line.startsWith("#")) {
						lines.add(line);
						width = Math.max(width, GUtils.split(line).length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			height = lines.size();
			resultMap = new TileMap(width, height);
			resultMap.setScale(new Vector2(2, 2));
			// 从上到下分析文本
			for (int y = 0; y < height; y++) {
				String line = (String) lines.get(y);
				String[] words = GUtils.split(line);
				for (int x = 0; x < words.length; x++) {
					resultMap.setTile(x, y, TileMap.tilesTable.get(words[x]));
				}
			}
			// 地图中最后一块砖中心的世界坐标
			resultMap.worldEndTileCenter = GUtils.worldTileCenterToWorldPixel(
					new Vector2(resultMap.getWidth(), resultMap.getHeight()), TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y,
					resultMap.scale);
			resultMap.worldEndTileCenter.x += TileMap.TILE_SIZE.x * resultMap.scale.x;
			resultMap.worldEndTileCenter.y += (TileMap.TILE_SIZE.y / 2 + 10) * resultMap.scale.y;
			if (GameCore.isApplet) {
				resultMap.worldEndTileCenter.y -= 24;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public void addNPC(Entity srcNPC, Vector2 pos) {
		if (srcNPC != null) {
			srcNPC.position = pos;
			TileMap.addEntity(srcNPC.name, srcNPC);
		}
	}

	/**
	 * 向地图中添加实体（如树、NPC等）
	 * 
	 * @param entity
	 * @param tileX
	 * @param tileY
	 */
	public void addEntity(Entity srcEntity, Vector2 worldPos) {
		if (srcEntity != null) {
			// 从主实体中复制实体（深拷贝）
			Entity entity = new Entity(srcEntity);
			entity.position = new Vector2(worldPos);
			TileMap.addEntity(entity.name, entity);
		}
	}

	/**
	 * 更新地图
	 */
	public void update() {
		TileMap.player.update();

		Iterator<Entry<String, Entity>> entitiesIterator = TileMap.getEntitiesIterator();
		while (entitiesIterator.hasNext()) {
			HashMap.Entry<String, Entity> entry = (HashMap.Entry<String, Entity>) entitiesIterator.next();
			if (!entry.getValue().visible) {
				continue;
			}
			this.updateEntities.add(entry.getValue());
		}
		while (!this.updateEntities.isEmpty()) {
			this.updateEntities.peek().update();
			this.updateEntities.poll();
		}
	}

	/**
	 * 渲染地图
	 * 
	 * @param g
	 */
	public synchronized void draw(Graphics2D g) {
		float screenleftUpPointX = player.position.x - GameCore.screen.width / 2.0f;

		screenleftUpPointX = Math.max(screenleftUpPointX, 0);
		screenleftUpPointX = Math.min(screenleftUpPointX, worldEndTileCenter.x - GameCore.screen.width);

		float screenleftUpPointY = player.position.y - GameCore.screen.height / 2.0f;

		screenleftUpPointY = Math.max(screenleftUpPointY, 0);
		screenleftUpPointY = Math.min(screenleftUpPointY, worldEndTileCenter.y - GameCore.screen.height);

		GameCore.screen.setleftUpPoint(new Vector2(screenleftUpPointX, screenleftUpPointY));

		// 绘制地图
		for (int j = 0; j < this.getHeight(); j++) {
			for (int i = 0; i < this.getWidth(); i++) {
				if (j % 2 == 1) {
					Vector2 screenPos = GUtils
							.worldPixelToViewPort(new Vector2(i * TileMap.TILE_SIZE.x - TileMap.TILE_SIZE.x / 2,
									j * TileMap.TILE_SIZE.y / 2 - TileMap.TILE_SIZE.y / 2).mul(this.scale));
					AffineTransform transform = new AffineTransform();

					transform.translate(screenPos.x, screenPos.y);
					transform.scale(this.scale.x, this.scale.y);

					g.drawImage(this.getTile(i, j), transform, null);
				} else {
					Vector2 screenPos = GUtils.worldPixelToViewPort(
							new Vector2(i * TileMap.TILE_SIZE.x, j * TileMap.TILE_SIZE.y / 2 - TileMap.TILE_SIZE.y / 2)
									.mul(this.scale));
					AffineTransform transform = new AffineTransform();

					transform.translate(screenPos.x, screenPos.y);
					transform.scale(this.scale.x, this.scale.y);

					g.drawImage(this.getTile(i, j), transform, null);
				}
			}
		}

		// 向实体渲染队列中添加游戏主角
		renderEntities.add(player);

		// 向实体渲染队列中添加其他实体
		Iterator<Entry<String, Entity>> entitiesIterator = TileMap.getEntitiesIterator();
		while (entitiesIterator.hasNext()) {
			HashMap.Entry<String, Entity> entry = (HashMap.Entry<String, Entity>) entitiesIterator.next();
			if (entry.getValue().visible == false) {
				continue;
			}
			renderEntities.add(entry.getValue());
		}

		// 渲染优先队列
		while (!renderEntities.isEmpty()) {
			renderEntities.peek().draw(g);
			renderEntities.remove(renderEntities.peek());
		}
	}
}
