package priv.sandbox.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;

import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.ui.UIEntity;
import priv.dremma.game.ui.UIManager;

/**
 * 道具格子
 * 
 * @author guoyi
 *
 */
public class PropCellView extends UIEntity {

	private UIEntity propItem = null;

	public PropCellView(Image cell) {
		super();
		this.setStaticImage(cell);
	}
	
	public PropCellView(Image cell, MouseInputHandler mouseInputHandler) {
		super(mouseInputHandler);
		this.setStaticImage(cell);
	}

	public void setPropItem(UIEntity propItem) {
		UIManager.attachSubUI(this, propItem);
		this.propItem = propItem;
		this.propItem.position = this.position;
	}

	public void removePropItem() {
		UIManager.detachSubUI(this, propItem);
		this.propItem = null;
	}

	public UIEntity getPropItem() {
		return this.propItem;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.propItem == null) {
			super.draw(g);
			return;
		}

		// 渲染道具
		this.propItem.draw(g);

		// 渲染道具框
		super.draw(g);
	}

}
