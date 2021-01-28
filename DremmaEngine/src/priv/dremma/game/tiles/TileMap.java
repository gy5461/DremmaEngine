package priv.dremma.game.tiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;

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
	private LinkedList<Entity> entities; // ��Ϸ�е�����ʵ��
	private Player player; // ����

	/**
	 * ����ָ�������߶ȵ�TileMap
	 * 
	 * @param width
	 * @param height
	 */
	public TileMap(int width, int height) {
		tiles = new Image[width][height];
		entities = new LinkedList<Entity>();
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
		this.player = player;
	}

	/**
	 * ���ʵ�嵽��ͼ��
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * �ӵ�ͼ��ɾ��ʵ��
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	/**
	 * ȡ�������ͼ������ʵ�壨�����Ƕ����⣩�ĵ�����
	 */
	public Iterator<Entity> getEntities() {
		return entities.iterator();
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
			// ���ϵ��·����ı�
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
	 * ��Ⱦ��ͼ
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		// ���Ƶ�ͼ
		for (int j = 0; j < this.getHeight(); j++) {
			for (int i = 0; i < this.getWidth(); i++) {
				// Debug.log(Debug.DebugLevel.INFO, "width:"+map.getTile(i,
				// j).getWidth(null)+"height:"+map.getTile(i, j).getHeight(null));
				if (j % 2 == 1) {
					g.drawImage(this.getTile(i, j), i * 130 - 130 / 2, j * 88 - 44 - j * 50,
							this.getTile(i, j).getWidth(null), this.getTile(i, j).getHeight(null), null);
				} else {
					g.drawImage(this.getTile(i, j), i * 130, j * 88 - 44 - j * 50, this.getTile(i, j).getWidth(null),
							this.getTile(i, j).getHeight(null), null);
				}
			}
		}
		
		// ����entity
		player.draw(g);
	}
}
