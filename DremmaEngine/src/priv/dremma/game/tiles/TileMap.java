package priv.dremma.game.tiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import priv.dremma.game.GameCore;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Vector2;

/**
 * TileMap 类包含地砖地图的数据、实体
 * 
 * @author guoyi
 *
 */
public class TileMap {
	// 地砖编号与地砖对应的表，加载地图时使用
	public static ArrayList<Image> tilesTable = new ArrayList<Image>();

	private Image[][] tiles; // 地砖
	private LinkedList<Entity> entities; // 游戏中的其他实体
	private Player player; // 主角

	/**
	 * 生成指定宽度与高度的TileMap
	 * 
	 * @param width
	 * @param height
	 */
	public TileMap(int width, int height) {
		tiles = new Image[width][height];
		entities = new LinkedList<Entity>();
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
	public Player getPlayer() {
		return player;
	}

	/**
	 * 设置游戏主角对象
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * 添加实体到地图中
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * 从地图中删除实体
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	/**
	 * 取得这个地图中所有实体（除主角对象外）的迭代器
	 */
	public Iterator<Entity> getEntities() {
		return entities.iterator();
	}

	/**
	 * 加载地砖资源
	 */
	public static void loadTiles() {
		// 加载地砖
		Resources.load(Resources.ResourceType.Tile, "floor0", Resources.path + "images/tiles/floor_0.png");
		Resources.load(Resources.ResourceType.Tile, "floor1", Resources.path + "images/tiles/floor_1.png");
		Resources.load(Resources.ResourceType.Tile, "floor2", Resources.path + "images/tiles/floor_2.png");
		Resources.load(Resources.ResourceType.Tile, "floor3", Resources.path + "images/tiles/floor_3.png");
		Resources.load(Resources.ResourceType.Tile, "floor4", Resources.path + "images/tiles/floor_4.png");
	}

	/**
	 * 根据文件加载地图
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
			// 从上到下分析文本
			for (int y = 0; y < height; y++) {
				String line = (String) lines.get(y);
				String[] words = GUtils.split(line);
				for (int x = 0; x < words.length; x++) {
					resultMap.setTile(x, y, TileMap.tilesTable.get(Integer.valueOf(words[x])));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 向地图中添加实体（如树、NPC等）
	 * 
	 * @param entity
	 * @param tileX
	 * @param tileY
	 */
	public void addEntity(Entity srcEntity, int tileX, int tileY) {
		if (srcEntity != null) {
			try {
				// 从主实体中复制实体
				Entity entity = (Entity) srcEntity.clone();
				entity.position = new Vector2(
						GUtils.worldTileCenterToWorldPixel(tileX, tileY, this.getTile(tileX, tileY).getWidth(null),
								this.getTile(tileX, tileY).getHeight(null)).x
								+ GUtils.worldTileCenterToWorldPixel(1, 1, this.getTile(tileX, tileY).getWidth(null),
										this.getTile(tileX, tileY).getHeight(null)).x
								- entity.getWidth() / 2,
						GUtils.worldTileCenterToWorldPixel(tileX, tileY + 1, this.getTile(tileX, tileY).getWidth(null),
								this.getTile(tileX, tileY).getHeight(null)).y - entity.getHeight());

				this.addEntity(entity);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 渲染地图
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		int offsetX = Math.round(GameCore.screen.width / 2.0f - player.position.x);
//		offsetX = Math.max(offsetX, 0);
//		offsetX = Math.min(offsetX, Math.round(GUtils.worldTileCenterToWorldPixel(this.getWidth(), this.getHeight(), 130, 76).x));
		
		int offsetY = Math.round(GameCore.screen.height / 2.0f - player.position.y);
//		offsetY = Math.max(offsetY, 0 );
//		offsetY = Math.min(offsetY, Math.round(GUtils.worldTileCenterToWorldPixel(this.getWidth(), this.getHeight(), 130, 76).y));
		GameCore.screen.setOffset(offsetX, offsetY);
		
		//Debug.log(Debug.DebugLevel.INFO, "offsetX:"+offsetX+", offsetY:"+offsetY);
		// 绘制地图
		for (int j = 0; j < this.getHeight(); j++) {
			for (int i = 0; i < this.getWidth(); i++) {
				if (j % 2 == 1) {
					AffineTransform transform = new AffineTransform();
					transform.scale(2, 2);
					transform.translate(i * 130 - 130 / 2 + offsetX, j * 88 - 44 - j * 50 + offsetY);	// scale会对translate产生影响
					
					g.drawImage(this.getTile(i, j), transform, null);
					// Debug.log(Debug.DebugLevel.INFO, "x:"+(i * 130 - 130 / 2)+"y:"+(j * 88 - 44 -
					// j * 50));
				} else {
					AffineTransform transform = new AffineTransform();
					transform.scale(2, 2);
					transform.translate(i * 130 + offsetX, j * 88 - 44 - j * 50 + offsetY);
					//transform.scale(2, 2);
					g.drawImage(this.getTile(i, j), transform, null);
					// Debug.log(Debug.DebugLevel.INFO, "x:"+(i * 130)+"y:"+(j * 88 - 44 - j * 50));
				}
			}
		}

		// 绘制entity
		player.draw(g);
	}
}
