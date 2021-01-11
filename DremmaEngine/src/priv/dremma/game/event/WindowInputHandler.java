package priv.dremma.game.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

/**
 * �����¼�����򿪹رյȣ�������
 * @author guoyi
 *
 */
public class WindowInputHandler implements WindowListener {

	private final Game game;
	
	public WindowInputHandler(Game game) {
		this.game = game;
		this.game.window.addWindowListener(this);	//����Ϸ������Ӵ��������
	}
	
	@Override
	public void windowOpened(WindowEvent event) {
		Debug.log(Debug.DebugLevel.SERVERE, "Window Opened!");
	}

	@Override
	public void windowClosing(WindowEvent event) {
		Debug.log(Debug.DebugLevel.SERVERE, "Window Closing!");
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