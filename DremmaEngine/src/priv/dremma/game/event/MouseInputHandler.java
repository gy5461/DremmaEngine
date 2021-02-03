package priv.dremma.game.event;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Rect;
import priv.dremma.game.util.Vector2;

/**
 * 鼠标输入处理类
 * 
 * @author guoyi
 *
 */

public class MouseInputHandler implements MouseListener {
	GameCore game;
	private Vector2 curPos = Vector2.zero(); // 鼠标当前位置
	public Vector2 transCurPos = Vector2.zero();
	private Vector2 lastTransPos = Vector2.zero(); // 鼠标上一位置

	public MouseInputHandler(GameCore game) {
		game.addMouseListener(this); // 给游戏窗体添加鼠标监听器
		this.game = game;
	}

	public void update() {
		transCurPos = this.getTransCurPos();

		lastTransPos = transCurPos;
	}

	public Vector2 getLastPos() {
		return this.lastTransPos;
	}

	public Vector2 getCurPos() {
		PointerInfo pinfo = MouseInfo.getPointerInfo();
		curPos = new Vector2(pinfo.getLocation().x, pinfo.getLocation().y);
		curPos = curPos.sub(new Vector2(game.window.getLocation().x, game.window.getLocation().y + 24));
		return curPos;
	}

	public Vector2 getTransCurPos() {
		PointerInfo pinfo = MouseInfo.getPointerInfo();
		transCurPos = new Vector2(pinfo.getLocation().x, pinfo.getLocation().y);
		transCurPos = transCurPos.sub(new Vector2(game.window.getLocation().x, game.window.getLocation().y + 27));
		return transCurPos;
	}

	public boolean transCurPosIsInRect(Rect rect) {
		return GUtils.viewPortToWorldPixel(transCurPos).isInRect(rect.leftUpPoint, rect.rightDownPoint);
	}

	/**
	 * 内部类，鼠标
	 * 
	 * @author guoyi
	 *
	 */
	public class Mouse {
		private int pressedTimes = 0; // 鼠标按压次数
		private boolean isPressed = false; // 鼠标是否被按
		private Vector2 location; // 鼠标按压位置

		public int getPressedTimes() {
			return pressedTimes;
		}

		public void setPressedTimes(int pressedTimes) {
			this.pressedTimes = pressedTimes;
		}

		public boolean isPressed() {
			return this.isPressed;
		}

		public Vector2 getLocation() {
			return location;
		}

		/**
		 * 判断鼠标坐标是否在矩形内 矩形：
		 * 
		 * @param leftUp    左上点坐标
		 * @param rightDown 右下点坐标
		 * @return
		 */
		public boolean isInRect(Vector2 leftUp, Vector2 rightDown) {
			return GUtils.viewPortToWorldPixel(this.location).isInRect(leftUp, rightDown);
		}

		/**
		 * 判断鼠标坐标是否在矩形内 矩形：
		 * 
		 * @param leftUp    左上点坐标
		 * @param rightDown 右下点坐标
		 * @return
		 */
		public boolean isInRect(Rect rect) {
			return GUtils.viewPortToWorldPixel(this.location).isInRect(rect.leftUpPoint, rect.rightDownPoint);
		}

		// 触发键
		public void toggle(Vector2 location, boolean isPressed) {
			this.location = location;
			this.isPressed = isPressed;
			if (isPressed) {
				Debug.log(Debug.DebugLevel.WARNING, "Mouse pressed " + location);
			} else {
				Debug.log(Debug.DebugLevel.WARNING, "Mouse released " + location);
			}
			if (isPressed) {
				pressedTimes++;
			}
		}
	}

	public Mouse mouse = new Mouse();

	// 鼠标按键单击（按下并释放）时触发，测试时感觉不够灵敏
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	// 鼠标按键按下时触发
	@Override
	public void mousePressed(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), true);
	}

	// 鼠标释放时触发
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), false);
	}

	// 鼠标进入时触发
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	// 鼠标从Enter状态中退出时触发
	@Override
	public void mouseExited(MouseEvent e) {
	}

}
