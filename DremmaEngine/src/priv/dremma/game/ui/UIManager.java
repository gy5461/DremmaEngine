package priv.dremma.game.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import priv.dremma.game.util.TranslateEntityHelper;

public class UIManager {

	public static Queue<UIEntity> uiEntities = new LinkedList<UIEntity>();
	public static HashMap<UIEntity, ArrayList<UIEntity>> parentAndChirld = new HashMap<UIEntity, ArrayList<UIEntity>>();

	public static UIEntity lockUIEntiy = null;

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
	 * 向双亲UI中添加子UI
	 * 
	 * @param UIEntity
	 */
	public static void attachSubUI(UIEntity parentUIEntity, UIEntity childUIEntity) {
		if (!UIManager.uiEntities.contains(childUIEntity)) {
			UIManager.addUI(childUIEntity);
		}
		
		if(childUIEntity.parent != null) {
			UIManager.detachSubUI(childUIEntity.parent, childUIEntity);
		}
		childUIEntity.parent = parentUIEntity;

		ArrayList<UIEntity> childs;
		if (UIManager.parentAndChirld.containsKey(parentUIEntity)) {
			childs = UIManager.parentAndChirld.get(parentUIEntity);
			childs.add(childUIEntity);
		} else {
			childs = new ArrayList<UIEntity>();
			childs.add(childUIEntity);
		}
		UIManager.parentAndChirld.put(parentUIEntity, childs);
	}

	/**
	 * 删除游戏中的UI
	 * 
	 * @param UIEntity
	 */
	public static void removeUI(UIEntity UIEntity) {
		// 删除其子UI
		if (UIManager.parentAndChirld.containsKey(UIEntity)) {
			for (UIEntity child : UIManager.parentAndChirld.get(UIEntity)) {
				if (UIManager.uiEntities.contains(child)) {
					UIManager.removeUI(child);
				} else {
					continue;
				}

				if (TranslateEntityHelper.translateEntities.containsKey(child.name)) {
					TranslateEntityHelper.translateEntities.remove(child.name);
				}
			}
		}

		// 删除本UI
		if (UIManager.uiEntities.contains(UIEntity)) {
			UIManager.uiEntities.remove(UIEntity);
		} else {
			return;
		}

		if (TranslateEntityHelper.translateEntities.containsKey(UIEntity.name)) {
			TranslateEntityHelper.translateEntities.remove(UIEntity.name);
		}
	}

	/**
	 * 解除子UI与双亲UI的关系
	 * 
	 * @param parentUIEntity
	 * @param childUIEntity
	 */
	public static void detachSubUI(UIEntity parentUIEntity, UIEntity childUIEntity) {
		if (UIManager.parentAndChirld.containsKey(parentUIEntity)
				&& UIManager.parentAndChirld.get(parentUIEntity).contains(childUIEntity)
				&& childUIEntity.parent == parentUIEntity) {
			UIManager.parentAndChirld.get(parentUIEntity).remove(childUIEntity);
			childUIEntity.parent = null;
		}
	}

	/**
	 * 去除所有子UI
	 * 
	 * @param parentUIEntity
	 */
	public static void detachAllChildUI(UIEntity UIEntity) {
		if (UIManager.parentAndChirld.containsKey(UIEntity)) {
			for (UIEntity ue : UIManager.parentAndChirld.get(UIEntity)) {
				if (ue.parent != UIEntity) {
					continue;
				}
				ue.parent = null;
			}
			UIManager.parentAndChirld.remove(UIEntity);
		}
	}

	/**
	 * 设置UI可视性，将影响其子UI
	 * 
	 * @param visible
	 */
	public static void setUIVisibility(String name, boolean visible) {
		if (UIManager.parentAndChirld.containsKey(UIManager.getUIEntity(name))) {
			for (UIEntity child : UIManager.parentAndChirld.get(UIManager.getUIEntity(name))) {
				UIManager.setUIVisibility(child.name, visible);
			}
		}
		if (UIManager.getUIEntity(name) != null) {
			UIManager.getUIEntity(name).visible = visible;
		}
	}

	/**
	 * 获取UI管理器中的UIEntity
	 * 
	 * @param name
	 * @return
	 */
	public static UIEntity getUIEntity(String name) {
		for (UIEntity ue : UIManager.uiEntities) {
			if (ue.name.equals(name)) {
				return ue;
			}
		}
		return null;
	}

	/**
	 * 根据UIEntity名字返回其子UI
	 * 
	 * @param g
	 */
	public static ArrayList<UIEntity> getChilds(String name) {
		if (UIManager.parentAndChirld.containsKey(UIManager.getUIEntity(name))) {
			return UIManager.parentAndChirld.get(UIManager.getUIEntity(name));
		}
		return null;
	}

	public static void draw(Graphics2D g) {
		// UI
		boolean hasLocked = false;
		for (UIEntity ue : UIManager.uiEntities) {
			if (ue.visible == false) {
				continue;
			}

			if (ue.isChosenMouseButton()) {
				UIManager.lockUIEntiy = ue;
				hasLocked = true;
			}
			ue.draw(g);
		}
		if (!hasLocked) {
			UIManager.lockUIEntiy = null;
		}
	}
}
