package priv.dremma.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import priv.dremma.game.GameCore;

public class Text extends UIEntity {

	public String content = null;
	public Font font = null;
	private int fontSize = 30;
	public Color color = null;

	public final int TXTWIDTH;
	public final int xBorderToScreen = 55;

	public boolean needAlignScreen = false;

	public Text(String content) {
		super();
		this.content = content;
		this.font = new Font("TimesRoman", Font.BOLD, fontSize);
		this.color = Color.black;
		this.TXTWIDTH = (GameCore.screen.width - xBorderToScreen * 2) / fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		this.font = new Font("TimesRoman", Font.BOLD, fontSize);
	}

	public int getWidth() {
		return this.content.length() * this.fontSize;
	}

	public int getHeihgt() {
		if (this.needAlignScreen) {
			return (int) Math.ceil(this.content.length() * 1.0f / this.TXTWIDTH);
		} else {
			return this.fontSize;
		}
	}

	// 重写draw方法
	@Override
	public synchronized void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(color);

		if (needAlignScreen) {
			// 将文字渲染到屏幕上
			int contentLen = content.length();
			int xOffset = 0;
			int yOffset = 0;
			while (contentLen > TXTWIDTH) {
				String tmp = content.substring(xOffset, TXTWIDTH + xOffset);
				g.drawString(tmp, position.x, position.y + yOffset);
				contentLen -= TXTWIDTH;
				xOffset += TXTWIDTH;
				yOffset += fontSize;
			}
			String tmp = content.substring(xOffset, content.length());
			g.drawString(tmp, position.x, position.y + yOffset);
		} else {
			g.drawString(content, position.x, position.y);
		}
	}
}
