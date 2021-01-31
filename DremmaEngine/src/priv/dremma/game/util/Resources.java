package priv.dremma.game.util;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.tiles.TileMap;

/**
 * ��Դ������
 * 
 * @author guoyi
 *
 */
public class Resources {

	public static String path;

	// ��Դ����
	public static enum ResourceType {
		Music, Tile
	}

	public static HashMap<String, String> res = new HashMap<String, String>(); // ��Դ������-·����

	public static void load(ResourceType type, String name, String path) {
		res.put(name, path);

		switch (type) {
		case Music:
			AudioManager.getInstance().initAudio(name);
			break;
		case Tile:
			Image tile = Resources.loadImage(path);
			if (!TileMap.tilesTable.contains(tile)) {
				TileMap.tilesTable.add(tile); // ���ש������ש��Ӧ�ı�����ӵ�ש
			}
			break;
		}
	}
	
	/**
	 * ����ͼƬ
	 * @param filePath
	 * @return
	 */
	public static Image loadImage(String filePath) {
		return new ImageIcon(filePath).getImage();
	}
}
