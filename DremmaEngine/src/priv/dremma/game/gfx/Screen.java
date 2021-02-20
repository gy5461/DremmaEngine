package priv.dremma.game.gfx;

import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

/**
 * 屏幕类
 * @author guoyi
 *
 */
public class Screen {
	
	public int width;
	public int height;
	
	public static Vector2 leftUpPoint;		//屏幕左上点坐标
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		leftUpPoint = Vector2.zero();
	}
	
	public void setleftUpPoint(Vector2 point) {
		// 使摄像头平滑地跟随
		leftUpPoint = Vector2.lerp(leftUpPoint, point, 2.5f*Time.deltaTime);
	}

}
