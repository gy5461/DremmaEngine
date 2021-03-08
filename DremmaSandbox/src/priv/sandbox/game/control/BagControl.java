package priv.sandbox.game.control;

import java.util.ArrayList;
import java.util.HashMap;

import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Debug;
import priv.sandbox.game.entities.PropCellView;

/**
 * 背包控制层
 * @author guoyi
 *
 */
public class BagControl {

	ArrayList<PropCellView> bagView;
	HashMap<String, Integer> propItems;
	
	private int size;

	public BagControl(ArrayList<PropCellView> bagView) {
		this.bagView = bagView;
		this.size = 0;
		this.propItems = new HashMap<String, Integer>();
	}
	
	public int getSize() {
		return this.size;
	}

	/**
	 * 向背包中添加道具
	 * @param propName
	 * @param propItem
	 */
	public void addPropItemToBag(String propName) {
		if (this.size < this.bagView.size()) {
			UIManager.addUI(this.bagView.get(this.size), UIManager.getUIEntity(propName));
			this.bagView.get(this.size).setPropItem(UIManager.getUIEntity(propName));
			this.propItems.put(propName, this.size);
			this.size++;
		} else {
			Debug.log(Debug.DebugLevel.WARNING, "背包已满，无法再装入道具");
		}
	}
	
	/**
	 * 从背包中移除指定道具
	 * @param propName
	 */
	public void removePropItemFromBag(String propName) {
		if(this.propItems.containsKey(propName)) {
			this.bagView.get(this.propItems.get(propName)).removePropItem();
		}
	}
}
