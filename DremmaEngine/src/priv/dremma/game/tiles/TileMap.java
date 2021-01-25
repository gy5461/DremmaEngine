package priv.dremma.game.tiles;

import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;

import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;

/**
 * TileMap 类包含地砖地图的数据、实体
 * @author guoyi
 *
 */
public class TileMap {

	private Image[][] tiles; // 地砖
	private LinkedList<Entity> entities; // 游戏中的其他实体
	private Player player; // 主角
	
	/**
	 * 生成指定宽度与高度的TileMap
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
	 * @param x 地砖的横坐标
	 * @param y 地砖的纵坐标
	 * @return
	 */
	public Image getTile(int x,int y) {
		if(x<0 ||x>=this.getWidth()||
				y<0 || y>=this.getHeight()) {
			return null;
		}
		return tiles[x][y];
	}
	
	/**
	 * 设置指定位置的地砖
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x,int y, Image tile) {
		tiles[x][y] = tile;
	}
	
	/**
	 * 获取游戏主角对象
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * 设置游戏主角对象
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * 添加实体到地图中
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * 从地图中删除实体
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
}
