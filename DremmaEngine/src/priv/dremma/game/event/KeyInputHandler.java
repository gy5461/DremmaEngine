package priv.dremma.game.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

/**
 * �������봦����
 * 
 * @author guoyi
 *
 */
public class KeyInputHandler implements KeyListener {

	// Ϊgame��Ӽ����������
	public KeyInputHandler(Game game) {
		game.addKeyListener(this);
	}

	/**
	 * �ڲ��࣬��
	 * 
	 * @author guoyi
	 *
	 */
	public class Key {
		private int pressedTimes = 0; // ��������
		private boolean isPressed = false; // �Ƿ񱻰�
		public int keyCode;

		public int getPressedTimes() {
			return pressedTimes;
		}

		public boolean isPressed() {
			return isPressed;
		}

		// ������
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

	// �����������ⷽ���
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key key = new Key();

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true); // �����¼�����ʱ������
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false); // �ͷż��¼�����ʱ�������Ƿ񱻰�ѹ������Ϊfalse
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