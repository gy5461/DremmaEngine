package priv.dremma.game.util;

import java.util.HashMap;

import priv.dremma.game.audio.AudioManager;

public class Res {
	
	public static enum ResType {
		Music
	}

	public static HashMap<String, String> res = new HashMap<String, String>();
	
	public static void loadRes(ResType type, String name, String path) {
		res.put(name, path);
		
		switch(type) {
		case Music:
			AudioManager.getInstance().initAudio(name);
			break;
		}
	}
	
}
