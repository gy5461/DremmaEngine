package priv.dremma.game.util;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.tiles.TileMap;

/**
 * 资源加载类
 * 
 * @author guoyi
 *
 */
public class Resources {

	public static String path;

	// 资源类型
	public static enum ResourceType {
		Music, Tile
	}

	public static HashMap<String, String> res = new HashMap<String, String>(); // 资源表（名称-路径）

	public static void load(ResourceType type, String name, String path) {
		res.put(name, path);

		switch (type) {
		case Music:
			AudioManager.getInstance().initAudio(name);
			break;
		case Tile:
			Image tile = Resources.loadImage(path);
			if (!TileMap.tilesTable.contains(tile)) {
				TileMap.tilesTable.add(tile); // 向地砖编号与地砖对应的表中添加地砖
			}
			break;
		}
	}
	
	/**
	 * 加载图片
	 * @param filePath
	 * @return
	 */
	public static Image loadImage(String filePath) {
		return new ImageIcon(filePath).getImage();
	}
}
