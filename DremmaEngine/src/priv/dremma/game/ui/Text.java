package priv.dremma.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text extends UIEntity {

	public String content = null;
	public Font font = null;
	public Color color = null;

	public Text(String content) {
		super();
		this.content = content;
		this.font = new Font("TimesRoman", Font.BOLD, 20);
		this.color = Color.black;
	}

	// ��дdraw����
	@Override
	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(color);
		// ��������Ⱦ����Ļ��
		g.drawString(this.content, this.position.x, this.position.y);
	}
}
