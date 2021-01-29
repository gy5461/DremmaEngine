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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import priv.dremma.game.GameCore;
import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;
import priv.dremma.game.util.FloatCompare;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
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
	private LinkedList<Entity> entities; // ��Ϸ�е�����ʵ��
	private Player player; // ����
	PriorityQueue<Entity> renderEntities; // ��Ⱦ���ȶ���

	/**
	 * ����ָ�������߶ȵ�TileMap
	 * 
	 * @param width
	 * @param height
	 */
	public TileMap(int width, int height) {
		tiles = new Image[width][height];
		entities = new LinkedList<Entity>();
		renderEntities = new PriorityQueue<Entity>(1, new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				// �����������
				return FloatCompare.isLess(o1.getBottom(), o2.getBottom()) ? -1 : 1;
			}

		});
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

	static Entity e;

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
		Animator tree = new Animator();
		Animation treeAnim = new Animation();
		treeAnim.addFrame(Resources.loadImage(Resources.path + "images/entities/tree1.png"), 100);
		tree.addAnimation("static", treeAnim);
		tree.state = "static";
		e = new Entity(tree);
		e.setScale(new Vector2(3f, 3f));

		resultMap.addEntity(e, 2, 5);

		e.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(e, 4, 5);
		
		e.setScale(new Vector2(1f, 1f));
		resultMap.addEntity(e, 3, 11);

		return resultMap;
	}

	/**
	 * ���ͼ�����ʵ�壨������NPC�ȣ�
	 * 
	 * @param entity
	 * @param tileX
	 * @param tileY
	 */
	public void addEntity(Entity srcEntity, int tileX, int tileY) {
		if (srcEntity != null) {
			// ����ʵ���и���ʵ�壨�����
			Entity entity = new Entity(srcEntity);
			entity.position = new Vector2(
					GUtils.worldTileCenterToWorldPixel(tileX, tileY, this.getTile(tileX, tileY).getWidth(null),
							this.getTile(tileX, tileY).getHeight(null)).x
							+ GUtils.worldTileCenterToWorldPixel(1, 1, this.getTile(tileX, tileY).getWidth(null),
									this.getTile(tileX, tileY).getHeight(null)).x
							- entity.getWidth() / 2,
					GUtils.worldTileCenterToWorldPixel(tileX, tileY + 1, this.getTile(tileX, tileY).getWidth(null),
							this.getTile(tileX, tileY).getHeight(null)).y - entity.getHeight());

			this.addEntity(entity);
		}
	}

	/**
	 * ��Ⱦ��ͼ
	 * 
	 * @param g
	 */
	public synchronized void draw(Graphics2D g) {
		int offsetX = Math.round(GameCore.screen.width / 2.0f - player.position.x);
//		offsetX = Math.max(offsetX, 0);
//		offsetX = Math.min(offsetX, Math.round(GUtils.worldTileCenterToWorldPixel(this.getWidth(), this.getHeight(), 130, 76).x));

		int offsetY = Math.round(GameCore.screen.height / 2.0f - player.position.y);
//		offsetY = Math.max(offsetY, 0 );
//		offsetY = Math.min(offsetY, Math.round(GUtils.worldTileCenterToWorldPixel(this.getWidth(), this.getHeight(), 130, 76).y));
		GameCore.screen.setOffset(offsetX, offsetY);

		// Debug.log(Debug.DebugLevel.INFO, "offsetX:"+offsetX+", offsetY:"+offsetY);
		// ���Ƶ�ͼ
		for (int j = 0; j < this.getHeight(); j++) {
			for (int i = 0; i < this.getWidth(); i++) {
				if (j % 2 == 1) {
					AffineTransform transform = new AffineTransform();
					transform.scale(2, 2);
					transform.translate(i * 130 - 130 / 2, j * 88 - 44 - j * 50); // scale���translate����Ӱ��

					g.drawImage(this.getTile(i, j), transform, null);
					// Debug.log(Debug.DebugLevel.INFO, "x:"+(i * 130 - 130 / 2)+"y:"+(j * 88 - 44 -
					// j * 50));
				} else {
					AffineTransform transform = new AffineTransform();
					transform.scale(2, 2);
					transform.translate(i * 130, j * 88 - 44 - j * 50);
					g.drawImage(this.getTile(i, j), transform, null);
					// Debug.log(Debug.DebugLevel.INFO, "x:"+(i * 130)+"y:"+(j * 88 - 44 - j * 50));
				}
			}
		}

		// ���ʵ����Ⱦ����
		renderEntities.clear();
		
		// ��ʵ����Ⱦ�����������Ϸ����
		renderEntities.add(player);

		// ��ʵ����Ⱦ�������������ʵ��
		for (Entity e : this.entities) {
			renderEntities.add(e);
		}
		
		// ��Ⱦ���ȶ���
		while(!renderEntities.isEmpty()) {
			renderEntities.peek().draw(g);
			renderEntities.remove(renderEntities.peek());
		}
	}
}
