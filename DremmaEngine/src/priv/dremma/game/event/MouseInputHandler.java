package priv.dremma.game.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import priv.dremma.game.Game;
import priv.dremma.game.util.Debug;

/**
 * 鼠标输入处理类
 * 
 * @author guoyi
 *
 */

public class MouseInputHandler implements MouseListener {
	
	public MouseInputHandler(Game game) {
		game.addMouseListener(this); // 给游戏窗体添加鼠标监听器
	}

	//鼠标按键单击（按下并释放）时触发，测试时感觉不够灵敏
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	//鼠标按键按下时触发
	@Override
	public void mousePressed(MouseEvent e) {
		Debug.log(Debug.DebugLevel.WARNING, "Mouse pressed X=" + e.getX() + ", Y=" + e.getY());
	}

	//鼠标释放时触发
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	//鼠标进入时或进入状态中触发
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	//鼠标从Enter状态中退出时触发
	@Override
	public void mouseExited(MouseEvent e) {

	}

}
