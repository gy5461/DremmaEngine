package priv.dremma.game.util;

import java.util.HashMap;

import priv.dremma.game.audio.AudioManager;

/**
 * 资源加载类
 * @author guoyi
 *
 */
public class Resources {
	
	public static String path;
	
	// 资源类型
	public static enum ResourceType {
		Music
	}

	public static HashMap<String, String> res = new HashMap<String, String>();	//资源表（名称-路径）
	
	public static void load(ResourceType type, String name, String path) {
		res.put(name, path);
		
		switch(type) {
		case Music:
			AudioManager.getInstance().initAudio(name);
			break;
		}
	}
	
}
