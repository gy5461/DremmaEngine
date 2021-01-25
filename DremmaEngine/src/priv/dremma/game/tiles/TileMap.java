package priv.dremma.game.tiles;

import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;

import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.Player;

/**
 * TileMap �������ש��ͼ�����ݡ�ʵ��
 * @author guoyi
 *
 */
public class TileMap {

	private Image[][] tiles; // ��ש
	private LinkedList<Entity> entities; // ��Ϸ�е�����ʵ��
	private Player player; // ����
	
	/**
	 * ����ָ�������߶ȵ�TileMap
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
	 * @param x ��ש�ĺ�����
	 * @param y ��ש��������
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
	 * ����ָ��λ�õĵ�ש
	 * @param x
	 * @param y
	 * @param tile
	 */
	public void setTile(int x,int y, Image tile) {
		tiles[x][y] = tile;
	}
	
	/**
	 * ��ȡ��Ϸ���Ƕ���
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * ������Ϸ���Ƕ���
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * ���ʵ�嵽��ͼ��
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * �ӵ�ͼ��ɾ��ʵ��
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
}
