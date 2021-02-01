package priv.dremma.game.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Debug;

/**
 * �����¼�����򿪹رյȣ�������
 * @author guoyi
 *
 */
public class WindowInputHandler implements WindowListener {

	private final GameCore game;
	
	public WindowInputHandler(GameCore game) {
		this.game = game;
		this.game.window.addWindowListener(this);	//����Ϸ������Ӵ��������
	}
	
	@Override
	public void windowOpened(WindowEvent event) {
		Debug.log(Debug.DebugLevel.WARNING, "Window Opened!");
	}

	@Override
	public void windowClosing(WindowEvent event) {
		Debug.log(Debug.DebugLevel.WARNING, "Window Closing!");
	}

	@Override
	public void windowClosed(WindowEvent event) {
	}

	@Override
	public void windowIconified(WindowEvent event) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent event) {
		
	}

	@Override
	public void windowActivated(WindowEvent event) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent event) {
		
	}

}