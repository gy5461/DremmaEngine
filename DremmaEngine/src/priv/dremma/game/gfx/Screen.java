package priv.dremma.game.gfx;

import priv.dremma.game.util.Vector2;

/**
 * ��Ļ��
 * @author guoyi
 *
 */
public class Screen {
	
	public int width;
	public int height;
	
	public static Vector2 leftUpPoint;		//��Ļ���ϵ�����
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		leftUpPoint = Vector2.zero();
	}
	
	public void setleftUpPoint(Vector2 point) {
		leftUpPoint = point;
	}

}
