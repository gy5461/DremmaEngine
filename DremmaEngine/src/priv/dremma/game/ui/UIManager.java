package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

import priv.dremma.game.util.Resources;
import priv.dremma.game.util.TranslateEntityHelper;

public class UIManager {

	public static Queue<UIEntity> uiEntities = new LinkedList<UIEntity>();

	static String path = Resources.path + "data/ui.dat"; // 数据文件目录

	/**
	 * 向游戏中添加UI
	 * 
	 * @param UIEntity
	 */
	public static void addUI(UIEntity UIEntity) {
		UIManager.uiEntities.add(UIEntity);
		if (!TranslateEntityHelper.translateEntities.containsKey(UIEntity.name)) {
			TranslateEntityHelper translateEntityHelper = new TranslateEntityHelper(UIEntity);
			TranslateEntityHelper.translateEntities.put(UIEntity.name, translateEntityHelper);
		}
	}

	/**
	 * 删除游戏中的UI
	 * 
	 * @param UIEntity
	 */
	public static void removeUI(UIEntity UIEntity) {
		if (UIManager.uiEntities.contains(UIEntity)) {
			UIManager.uiEntities.remove(UIEntity);
		} else {
			return;
		}

		if (TranslateEntityHelper.translateEntities.containsKey(UIEntity.name)) {
			TranslateEntityHelper.translateEntities.remove(UIEntity.name);
		}
	}

	public static UIEntity getUIEntity(String name) {
		for (UIEntity ue : UIManager.uiEntities) {
			if (ue.name.equals(name)) {
				return ue;
			}
		}
		return null;
	}

	public static void draw(Graphics2D g) {
		// UI
		for (UIEntity ue : UIManager.uiEntities) {
			if (ue.visible == false) {
				continue;
			}
			ue.draw(g);
		}
	}
}
