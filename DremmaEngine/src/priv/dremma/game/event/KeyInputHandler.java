package priv.dremma.game.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Debug;

/**
 * 键盘输入处理类
 * 
 * @author guoyi
 *
 */
public class KeyInputHandler implements KeyListener {

	// 为game添加键盘输入监听
	public KeyInputHandler(GameCore game) {
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

		private ArrayList<Integer> keyCodes = new ArrayList<Integer>();

		public Key() {

		}

		public Key(ArrayList<Integer> keyCodes) {
			this.keyCodes = keyCodes;
		}

		public ArrayList<Integer> getKeyCodes() {
			return this.keyCodes;
		}

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

	public Key key = new Key();

	// 虚拟键
	private HashMap<String, Key> virtualKeys = new HashMap<String, Key>();

	public void setVirtualKey(String name, ArrayList<Integer> keyCodes) {
		this.virtualKeys.put(name, new Key(keyCodes));
	}

	/**
	 * 通过虚拟键名称获取虚拟键
	 * 
	 * @param name 虚拟键名称
	 * @return
	 */
	public Key getVirtualKey(String name) {
		return this.virtualKeys.get(name);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true); // 按键事件发生时触发键
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false); // 释放键事件发生时将本键是否被按压属性设为false
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void toggleKey(int keyCode, boolean isPressed) {
		boolean flag = false;

		Iterator<Entry<String, Key>> virtualKeysIterator = this.virtualKeys.entrySet().iterator();
		while (virtualKeysIterator.hasNext()) {
			HashMap.Entry<String, Key> entry = (HashMap.Entry<String, Key>) virtualKeysIterator.next();
			for (Integer code : entry.getValue().getKeyCodes()) {
				if (code == keyCode) {
					flag = true;
					entry.getValue().toggle(keyCode, isPressed);
				}
			}
		}
		if (!flag) {
			key.toggle(keyCode, isPressed);	// 触发其他键（未设置键）
		}
	}

}