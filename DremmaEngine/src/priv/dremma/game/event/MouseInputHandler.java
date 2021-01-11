package priv.dremma.game.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Vector2;

/**
 * 鼠标输入处理类
 * 
 * @author guoyi
 *
 */

public class MouseInputHandler implements MouseListener {

	public MouseInputHandler(Game game) {
		game.addMouseListener(this); // 给游戏窗体添加鼠标监听器
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

		public boolean isPressed() {
			return isPressed;
		}

		public Vector2 getLocation() {
			return location;
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
		;
	}

	// 鼠标释放时触发
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), false);
	}

	// 鼠标进入时或进入状态中触发
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	// 鼠标从Enter状态中退出时触发
	@Override
	public void mouseExited(MouseEvent e) {

	}

}
