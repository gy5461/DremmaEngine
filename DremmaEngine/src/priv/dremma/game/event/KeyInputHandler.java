package priv.dremma.game.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

/**
 * �������봦����
 * @author guoyi
 *
 */
public class KeyInputHandler implements KeyListener {

	//Ϊgame��Ӽ����������
	public KeyInputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	/**
	 * �ڲ��࣬��
	 * @author guoyi
	 *
	 */
	public class Key {
		private int pressedTimes = 0;	//��������
		private boolean isPressed = false;	//�Ƿ񱻰�
		
		public int getPressedTimes() {
			return pressedTimes;
		}
		
		public boolean isPressed() {
			return isPressed;
		}

		//������
		public void toggle(boolean isPressed) {
			this.isPressed = isPressed;
			if(isPressed) {
				pressedTimes++;
			}
		}
	}
	
	//�����������ⷽ���
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);	//�����¼�����ʱ������
		Debug.log(Debug.DebugLevel.INFO, "Key Pressed:" + e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);	//�ͷż��¼�����ʱ�������Ƿ񱻰�ѹ������Ϊfalse
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			up.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			down.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			left.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			right.toggle(isPressed);
		}
	}

}