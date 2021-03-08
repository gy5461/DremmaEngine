package priv.sandbox.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;

import priv.dremma.game.ui.UIEntity;

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

	public void setPropItem(UIEntity propItem) {
		this.propItem = propItem;
		this.propItem.position = this.position;
	}

	public void removePropItem() {
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

		this.propItem.draw(g);

		// 渲染道具框
		super.draw(g);
	}

}
