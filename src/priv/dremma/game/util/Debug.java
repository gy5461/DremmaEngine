package priv.dremma.game.util;

import priv.dremma.game.Game;
/**
 * ������
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
			// ��ӡ��ɫ��Ϣ
			System.out.println("\u001b[1;32m[" + Game.NAME + "][INFO]" + msg+"\u001b[0m");
			break;
		case WARNING:
			// ��ӡ��ɫ����
			System.out.println("\u001b[1;33m[" + Game.NAME + "][WARNIGN]" + msg+"\u001b[0m");
			break;
		case SERVERE:
			// ��ӡ��ɫ���ؾ���
			System.out.println("\u001b[1;31m[" + Game.NAME + "][SERVERE]" + msg+"\u001b[0m");
			break;
		}
	}

	public static enum DebugLevel {
		INFO, WARNING, SERVERE;
	}
}
