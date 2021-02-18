package priv.dremma.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import priv.dremma.game.GameCore;

public class Text extends UIEntity {

	public String content = null;
	public Font font = null;
	public int fontSize = 30;
	public Color color = null;

	public final int TXTWIDTH;
	public final int xBorderToScreen = 55;

	public Text(String content) {
		super();
		this.content = content;
		this.font = new Font("TimesRoman", Font.BOLD, fontSize);
		this.color = Color.black;
		this.TXTWIDTH = (GameCore.screen.width - xBorderToScreen * 2) / fontSize;
	}

	// 重写draw方法
	@Override
	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(color);
		// 将文字渲染到屏幕上
		int contentLen = this.content.length();
		int xOffset = 0;
		int yOffset = 0;
		while (contentLen > this.TXTWIDTH) {
			String tmp = this.content.substring(xOffset, this.TXTWIDTH + xOffset);
			g.drawString(tmp, this.position.x, this.position.y + yOffset);
			contentLen -= this.TXTWIDTH;
			xOffset += this.TXTWIDTH;
			yOffset += this.fontSize;
		}
		String tmp = this.content.substring(xOffset, this.content.length());
		g.drawString(tmp, this.position.x, this.position.y + yOffset);
	}
}
