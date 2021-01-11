package priv.dremma.game.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

/**
 * 键盘输入处理类
 * 
 * @author guoyi
 *
 */
public class KeyInputHandler implements KeyListener {

	// 为game添加键盘输入监听
	public KeyInputHandler(Game game) {
		game.addKeyListener(this);
	}

	/**
	 * 内部类，键
	 * 
	 * @author guoyi
	 *
	 */
	public class Key {
		private int pressedTimes = 0; // 按键次数
		private boolean isPressed = false; // 是否被按
		public int keyCode;

		public int getPressedTimes() {
			return pressedTimes;
		}

		public boolean isPressed() {
			return isPressed;
		}

		// 触发键
		public void toggle(int keyCode, boolean isPressed) {
			this.keyCode = keyCode;
			this.isPressed = isPressed;
			if (isPressed) {
				Debug.log(Debug.DebugLevel.WARNING, "Key Pressed:" + keyCode);
			} else {
				Debug.log(Debug.DebugLevel.WARNING, "Key Released:" + keyCode);
			}

			if (isPressed) {
				pressedTimes++;
			}
		}
	}

	// 上下左右虚拟方向键
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key key = new Key();

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true); // 按键事件发生时触发键
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false); // 释放键事件发生时将本键是否被按压属性设为false
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void toggleKey(int keyCode, boolean isPressed) {
		boolean flag = false;
		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			flag = true;
			up.toggle(keyCode, isPressed);
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			flag = true;
			down.toggle(keyCode, isPressed);
		}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			flag = true;
			left.toggle(keyCode, isPressed);
		}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			flag = true;
			right.toggle(keyCode, isPressed);
		}
		if (!flag) {
			key.toggle(keyCode, isPressed);
		}
	}

}