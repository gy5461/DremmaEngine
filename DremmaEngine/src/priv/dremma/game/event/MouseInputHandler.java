package priv.dremma.game.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

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

	//��갴�����������²��ͷţ�ʱ����������ʱ�о���������
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	//��갴������ʱ����
	@Override
	public void mousePressed(MouseEvent e) {
		Debug.log(Debug.DebugLevel.WARNING, "Mouse pressed X=" + e.getX() + ", Y=" + e.getY());
	}

	//����ͷ�ʱ����
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	//������ʱ�����״̬�д���
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	//����Enter״̬���˳�ʱ����
	@Override
	public void mouseExited(MouseEvent e) {

	}

}
