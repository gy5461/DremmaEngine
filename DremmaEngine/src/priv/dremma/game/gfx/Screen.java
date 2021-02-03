package priv.dremma.game.gfx;

import priv.dremma.game.util.Vector2;

public class Screen {
	
	public int width;
	public int height;
	
	public static Vector2 leftUpPoint;		//ÆÁÄ»×óÉÏµã×ø±ê
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		leftUpPoint = Vector2.zero();
	}
	
	public void setleftUpPoint(Vector2 point) {
		leftUpPoint = point;
	}

}
