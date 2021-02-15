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
import java.util.PriorityQueue;
import java.util.Map.Entry;

import priv.dremma.game.GameCore;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;

/**
 * TileMap �������ש��ͼ�����ݡ�ʵ��
 * 
 * @author guoyi
 *
 */
public class TileMap {
	// ��ש������ש��Ӧ�ı����ص�ͼʱʹ��
	public static ArrayList<Image> tilesTable = new ArrayList<Image>();

	private Image[][] tiles; // ��ש
	private Vector2 scale;
	public static final Vector2 TILE_SIZE = new Vector2(130, 76);
	public static float modifier = TileMap.TILE_SIZE.y / TileMap.TILE_SIZE.x; // 2.5D�ӽ�ʱ����Ҫ�����ٶ������Ų�������
	public static HashMap<String, Entity> entities; // ��Ϸ�е�����ʵ��
	public static Player player; // ����
	PriorityQueue<Entity> renderEntities; // ��Ⱦ���ȶ���

	public Vector2 worldEndTileCenter = Vector2.zero();

	/**
	 * ����ָ�������߶ȵ�TileMap
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
				// �����������
				return FloatCompare.isLess(o1.getBottom(), o2.getBottom()) ? -1 : 1;
			}

		});
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
	 * ��ȡTileMap�Ŀ�ȣ���ˮƽ����ĵ�ש����
	 */
	public int getWidth() {
		return tiles.length;
	}

	/**
	 * ��ȡTileMap�ĸ߶ȣ�����ֱ����ĵ�ש����
	 */
	public int getHeight() {
		return tiles[0].length;
	}

	/**
	 * ȡ��ָ��λ�õĵ�ש��Խ��ʱ����null
	 * 
	 * @param x ��ש�ĺ�����
	 * @param y ��ש��������
	 * @return
	 */
	public Image getTile(int x, int y) {
		if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) {
			return null;
		}
		return tiles[x][y];
	}

	/**
	 * ����ָ��λ�õĵ�ש
	 * 
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x, int y, Image tile) {
		tiles[x][y] = tile;
	}

	/**
	 * ��ȡ��Ϸ���Ƕ���
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * ������Ϸ���Ƕ���
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		TileMap.player = player;
	}

	/**
	 * ���ʵ�嵽��ͼ��
	 * 
	 * @param entity
	 */
	public static void addEntity(String name, Entity entity) {
		if (TileMap.entities.containsKey(name)) {
			return;
		}
		entities.put(name, entity);
		if (name.contains("mapBorder")) {
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
	 * �ӵ�ͼ��ɾ��ʵ��
	 * 
	 * @param entity
	 */
	public void removeEntity(String name) {
		entities.remove(name);
	}

	/**
	 * ȡ�������ͼ������ʵ�壨�����Ƕ����⣩�ĵ�����
	 */
	public static Iterator<Entry<String, Entity>> getEntitiesIterator() {
		return entities.entrySet().iterator();
	}

	/**
	 * ���ص�ש��Դ
	 */
	public static void loadTiles() {
		// ���ص�ש
		Resources.load(Resources.ResourceType.Tile, "floor0", Resources.path + "images/tiles/floor_0.png");
		Resources.load(Resources.ResourceType.Tile, "floor1", Resources.path + "images/tiles/floor_1.png");
		Resources.load(Resources.ResourceType.Tile, "floor2", Resources.path + "images/tiles/floor_2.png");
		Resources.load(Resources.ResourceType.Tile, "floor3", Resources.path + "images/tiles/floor_3.png");
		Resources.load(Resources.ResourceType.Tile, "floor4", Resources.path + "images/tiles/floor_4.png");
	}

	/**
	 * �����ļ����ص�ͼ
	 */
	public static TileMap loadTileMap(String path) {
		TileMap.loadTiles();

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
			// ���ϵ��·����ı�
			for (int y = 0; y < height; y++) {
				String line = (String) lines.get(y);
				String[] words = GUtils.split(line);
				for (int x = 0; x < words.length; x++) {
					resultMap.setTile(x, y, TileMap.tilesTable.get(Integer.valueOf(words[x])));
				}
			}
			resultMap.worldEndTileCenter = GUtils.worldTileCenterToWorldPixel(
					new Vector2(resultMap.getWidth(), resultMap.getHeight()), TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y,
					resultMap.scale);
			resultMap.worldEndTileCenter.x += TileMap.TILE_SIZE.x * resultMap.scale.x;
			resultMap.worldEndTileCenter.y += (TileMap.TILE_SIZE.y / 2 + 10) * resultMap.scale.y;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public void addNPC(Entity srcNPC, Vector2 pos) {
		if (srcNPC != null) {
			srcNPC.position = pos;
			TileMap.addEntity(srcNPC.name, srcNPC);

			CollisionBox.collisionBoxs.put(srcNPC.name, new CollisionBox(srcNPC.position.sub(Vector2.one().mul(50)),
					srcNPC.position.add(Vector2.one().mul(50))));
		}
	}

	/**
	 * ���ͼ�����ʵ�壨������NPC�ȣ�
	 * 
	 * @param entity
	 * @param tileX
	 * @param tileY
	 */
	public void addEntity(Entity srcEntity, Vector2 tilePos) {
		if (srcEntity != null) {
			// ����ʵ���и���ʵ�壨�����
			Entity entity = new Entity(srcEntity);
			entity.position = new Vector2(
					GUtils.worldTileCenterToWorldPixel(tilePos, TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y, this.scale).x,
					GUtils.worldTileCenterToWorldPixel(tilePos, TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y,
							this.scale).y);

			TileMap.addEntity(entity.name, entity);
			CollisionBox.collisionBoxs.put(entity.name, new CollisionBox(entity.position.sub(Vector2.one().mul(50)),
					entity.position.add(Vector2.one().mul(50))));
		}
	}

	/**
	 * ���µ�ͼ
	 */
	public void update() {
		TileMap.player.update();

		Iterator<Entry<String, Entity>> entitiesIterator = TileMap.getEntitiesIterator();
		while (entitiesIterator.hasNext()) {
			HashMap.Entry<String, Entity> entry = (HashMap.Entry<String, Entity>) entitiesIterator.next();
			if (entry.getKey().contains("player")) {
				continue;
			}
			entry.getValue().update();
		}
	}

	/**
	 * ��Ⱦ��ͼ
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

		// ���Ƶ�ͼ
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

		// ��ʵ����Ⱦ�����������Ϸ����
		renderEntities.add(player);

		// ��ʵ����Ⱦ�������������ʵ��
		Iterator<Entry<String, Entity>> entitiesIterator = TileMap.getEntitiesIterator();
		while (entitiesIterator.hasNext()) {
			HashMap.Entry<String, Entity> entry = (HashMap.Entry<String, Entity>) entitiesIterator.next();
			if (entry.getValue().visible == false) {
				continue;
			}
			renderEntities.add(entry.getValue());
		}

		// ��Ⱦ���ȶ���
		while (!renderEntities.isEmpty()) {
			renderEntities.peek().draw(g);
			renderEntities.remove(renderEntities.peek());
		}
	}
}
