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
import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
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
	public static HashMap<String, Entity> entities; // ��Ϸ�е�����ʵ��
	public static Player player; // ����
	PriorityQueue<Entity> renderEntities; // ��Ⱦ���ȶ���

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
		entities.put(name, entity);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// tree1
		Animator tree1Animator = new Animator();
		Animation tree1Animation = new Animation();
		tree1Animation.addFrame(Resources.loadImage(Resources.path + "images/entities/tree1.png"), 100);
		tree1Animator.addAnimation("static", tree1Animation);
		tree1Animator.state = "static";
		Entity tree1Entity = new Entity(tree1Animator);

		tree1Entity.setScale(new Vector2(3f, 3f));
		tree1Entity.name = "tree1_1";
		resultMap.addEntity(tree1Entity, new Vector2(2, 5));

		tree1Entity.name = "tree1_2";
		tree1Entity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(tree1Entity, new Vector2(4, 5));

		tree1Entity.name = "tree1_3";
		tree1Entity.setScale(new Vector2(1f, 1f));
		resultMap.addEntity(tree1Entity, new Vector2(3, 11));

		// archiving
		Animator archivingAnimator = new Animator();
		Animation archivingAnimation = new Animation();
		archivingAnimation.addFrame(Resources.loadImage(Resources.path + "images/entities/archiving.png"), 100);
		archivingAnimator.addAnimation("static", archivingAnimation);
		archivingAnimator.state = "static";
		Entity archivingEntity = new Entity(archivingAnimator);

		archivingEntity.name = "archiving";
		archivingEntity.setScale(new Vector2(0.2f, 0.2f));
		resultMap.addEntity(archivingEntity, new Vector2(3, 6));
		CollisionBox.collisionBoxs.get("archiving").isTrigger = true; // ��������

		// chair1
		Animator chair1Animator = new Animator();
		Animation chair1Animation = new Animation();
		chair1Animation.addFrame(Resources.loadImage(Resources.path + "images/entities/chair1.png"), 100);
		chair1Animator.addAnimation("static", chair1Animation);
		chair1Animator.state = "static";
		Entity chair1Entity = new Entity(chair1Animator);

		chair1Entity.name = "chair1";
		chair1Entity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(chair1Entity, new Vector2(4, 8));

		// chair2
		Animator chair2Animator = new Animator();
		Animation chair2Animation = new Animation();
		chair2Animation.addFrame(Resources.loadImage(Resources.path + "images/entities/chair2.png"), 100);
		chair2Animator.addAnimation("static", chair2Animation);
		chair2Animator.state = "static";
		Entity chair2Entity = new Entity(chair2Animator);

		chair2Entity.name = "chair2";
		chair2Entity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(chair2Entity, new Vector2(4, 5));

		// chair3
		Animator chair3Animator = new Animator();
		Animation chair3Animation = new Animation();
		chair3Animation.addFrame(Resources.loadImage(Resources.path + "images/entities/chair3.png"), 100);
		chair3Animator.addAnimation("static", chair3Animation);
		chair3Animator.state = "static";
		Entity chair3Entity = new Entity(chair3Animator);

		chair3Entity.name = "chair3";
		chair3Entity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(chair3Entity, new Vector2(4, 11));

		// chair4
		Animator chair4Animator = new Animator();
		Animation chair4Animation = new Animation();
		chair4Animation.addFrame(Resources.loadImage(Resources.path + "images/entities/chair4.png"), 100);
		chair4Animator.addAnimation("static", chair4Animation);
		chair4Animator.state = "static";
		Entity chair4Entity = new Entity(chair4Animator);

		chair4Entity.name = "chair4";
		chair4Entity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(chair4Entity, new Vector2(5, 5));
		
		// chair4
		Animator deskAnimator = new Animator();
		Animation deskAnimation = new Animation();
		deskAnimation.addFrame(Resources.loadImage(Resources.path + "images/entities/desk.png"), 100);
		deskAnimator.addAnimation("static", deskAnimation);
		deskAnimator.state = "static";
		Entity deskEntity = new Entity(deskAnimator);

		deskEntity.name = "desk";
		deskEntity.setScale(new Vector2(2f, 2f));
		resultMap.addEntity(deskEntity, new Vector2(5, 8));

		CollisionBox.load(); // �������ļ��м�����ײ������

		// �����е�entity����ƶ�����
		Iterator<Entry<String, Entity>> entitiesIterator = TileMap.getEntitiesIterator();
		while (entitiesIterator.hasNext()) {
			HashMap.Entry<String, Entity> entry = (HashMap.Entry<String, Entity>) entitiesIterator.next();
			TranslateEntityHelper translateEntityHelper = new TranslateEntityHelper(entry.getValue());
			TranslateEntityHelper.translateEntities.put(entry.getKey(), translateEntityHelper);
		}

		TranslateEntityHelper.load(); // �������ļ��м����ƶ���������
		return resultMap;
	}

	/**
	 * ���ͼ�����ʵ�壨������NPC�ȣ�
	 * 
	 * @param entity
	 * @param tileX
	 * @param tileY
	 */
	public void addEntity(Entity srcEntity, Vector2 tile) {
		if (srcEntity != null) {
			// ����ʵ���и���ʵ�壨�����
			Entity entity = new Entity(srcEntity);
			entity.position = new Vector2(
					GUtils.worldTileCenterToWorldPixel(tile, TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y, this.scale).x,
					GUtils.worldTileCenterToWorldPixel(tile, TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y, this.scale).y);

			TileMap.addEntity(entity.name, entity);
			CollisionBox.collisionBoxs.put(entity.name,
					new CollisionBox(entity.position.sub(Vector2.one().mul(50)), entity.position.add(Vector2.one().mul(50))));
		}
	}

	/**
	 * ��Ⱦ��ͼ
	 * 
	 * @param g
	 */
	public synchronized void draw(Graphics2D g) {
		float screenleftUpPointX = player.position.x - GameCore.screen.width / 2.0f;

		Vector2 worldEndTileCenter = GUtils.worldTileCenterToWorldPixel(new Vector2(this.getWidth(), this.getHeight()),
				TileMap.TILE_SIZE.x, TileMap.TILE_SIZE.y, this.scale);

		screenleftUpPointX = Math.max(screenleftUpPointX, 0);
		screenleftUpPointX = Math.min(screenleftUpPointX,
				worldEndTileCenter.x + TileMap.TILE_SIZE.x * this.scale.x - GameCore.screen.width);

		float screenleftUpPointY = player.position.y - GameCore.screen.height / 2.0f;

		screenleftUpPointY = Math.max(screenleftUpPointY, 0);
		screenleftUpPointY = Math.min(screenleftUpPointY,
				worldEndTileCenter.y + (TileMap.TILE_SIZE.y / 2 + 10) * this.scale.y - GameCore.screen.height);

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
			renderEntities.add(entry.getValue());
		}

		// ��Ⱦ���ȶ���
		while (!renderEntities.isEmpty()) {
			renderEntities.peek().draw(g);
			renderEntities.remove(renderEntities.peek());
		}
	}
}
