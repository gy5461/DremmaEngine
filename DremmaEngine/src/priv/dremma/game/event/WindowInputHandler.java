package priv.dremma.game.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import priv.dremma.game.GameCore;
import priv.dremma.game.util.Debug;

/**
 * 窗体事件（如打开关闭等）处理类
 * @author guoyi
 *
 */
public class WindowInputHandler implements WindowListener {

	private final GameCore game;
	
	public WindowInputHandler(GameCore game) {
		this.game = game;
		this.game.window.addWindowListener(this);	//给游戏窗体添加窗体监听器
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