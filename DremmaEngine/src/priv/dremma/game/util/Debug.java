package priv.dremma.game.util;

import priv.dremma.game.Game;
/**
 * 调试类
 * @author guoyi
 *
 */
public class Debug {

	public static void log(DebugLevel level, String msg) {
		if(Game.debug == false) {
			return;
		}
		switch (level) {
		default:
		case INFO:
			// 打印绿色信息
			System.out.println("\u001b[1;32m[" + Game.name + "][INFO]" + msg+"\u001b[0m");
			break;
		case WARNING:
			// 打印黄色警告
			System.out.println("\u001b[1;33m[" + Game.name + "][WARNIGN]" + msg+"\u001b[0m");
			break;
		case SERVERE:
			// 打印红色严重警告
			System.out.println("\u001b[1;31m[" + Game.name + "][SERVERE]" + msg+"\u001b[0m");
			break;
		}
	}

	public static enum DebugLevel {
		INFO, WARNING, SERVERE;
	}
}
