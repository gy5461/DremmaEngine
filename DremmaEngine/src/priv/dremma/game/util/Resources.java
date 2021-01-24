package priv.dremma.game.util;

import java.util.HashMap;

import priv.dremma.game.audio.AudioManager;

/**
 * ��Դ������
 * @author guoyi
 *
 */
public class Resources {
	
	public static String path;
	
	// ��Դ����
	public static enum ResourceType {
		Music
	}

	public static HashMap<String, String> res = new HashMap<String, String>();	//��Դ������-·����
	
	public static void load(ResourceType type, String name, String path) {
		res.put(name, path);
		
		switch(type) {
		case Music:
			AudioManager.getInstance().initAudio(name);
			break;
		}
	}
	
}
