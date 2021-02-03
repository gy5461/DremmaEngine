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
 * ������봦����
 * 
 * @author guoyi
 *
 */

public class MouseInputHandler implements MouseListener {
	GameCore game;
	private Vector2 curPos = Vector2.zero(); // ��굱ǰλ��
	public Vector2 transCurPos = Vector2.zero();
	private Vector2 lastTransPos = Vector2.zero(); // �����һλ��

	public MouseInputHandler(GameCore game) {
		game.addMouseListener(this); // ����Ϸ���������������
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
		 * �ж���������Ƿ��ھ����� ���Σ�
		 * 
		 * @param leftUp    ���ϵ�����
		 * @param rightDown ���µ�����
		 * @return
		 */
		public boolean isInRect(Vector2 leftUp, Vector2 rightDown) {
			return GUtils.viewPortToWorldPixel(this.location).isInRect(leftUp, rightDown);
		}

		/**
		 * �ж���������Ƿ��ھ����� ���Σ�
		 * 
		 * @param leftUp    ���ϵ�����
		 * @param rightDown ���µ�����
		 * @return
		 */
		public boolean isInRect(Rect rect) {
			return GUtils.viewPortToWorldPixel(this.location).isInRect(rect.leftUpPoint, rect.rightDownPoint);
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
	}

	// ����ͷ�ʱ����
	@Override
	public void mouseReleased(MouseEvent e) {
		mouse.toggle(new Vector2(e.getX(), e.getY()), false);
	}

	// ������ʱ����
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	// ����Enter״̬���˳�ʱ����
	@Override
	public void mouseExited(MouseEvent e) {
	}

}
