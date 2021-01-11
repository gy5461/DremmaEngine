package priv.dremma.game.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Vector2;

/**
 * ������봦����
 * 
 * @author guoyi
 *
 */

public class MouseInputHandler implements MouseListener {

	public MouseInputHandler(Game game) {
		game.addMouseListener(this); // ����Ϸ���������������
	}

	/**
	 * �ڲ��࣬���
	 * 
	 * @author guoyi
	 *
	 */
	public class Mouse {
		private int pressedTimes = 0; // ��갴ѹ����
		private boolean isPressed = false; // ����Ƿ񱻰�
		private Vector2 location; // ��갴ѹλ��

		public int getPressedTimes() {
			return pressedTimes;
		}

		public boolean isPressed() {
			return isPressed;
		}

		public Vector2 getLocation() {
			return location;
		}

		// ������
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

	// ��갴�����������²��ͷţ�ʱ����������ʱ�о���������
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	// ��갴������ʱ����
	@Override
	public void mousePressed(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), true);
		;
	}

	// ����ͷ�ʱ����
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), false);
	}

	// ������ʱ�����״̬�д���
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	// ����Enter״̬���˳�ʱ����
	@Override
	public void mouseExited(MouseEvent e) {

	}

}
