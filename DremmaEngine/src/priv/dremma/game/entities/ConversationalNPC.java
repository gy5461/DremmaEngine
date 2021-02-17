package priv.dremma.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.ui.Text;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC {

	KeyInputHandler keyInputHandler;
	int pressTalkTime = 0;
	int pressEnterTime = 0;
	public boolean isTalking;

	public ConversationalNPC(KeyInputHandler keyInputHandler, float speed) {
		super(speed);
		this.nearDistance = 80f;
		this.keyInputHandler = keyInputHandler;
		this.isTalking = false;

		this.setStaticImage(Resources.loadImage(Resources.path + "images/entities/�ϼ�����.png"));
	}

	public synchronized void update() {
		super.update();
		if (this.nearPlayer()) {
			if (!this.isTalking && this.keyInputHandler.talk.isPressed()
					&& this.pressTalkTime < this.keyInputHandler.talk.getPressedTimes()) {
				this.pressTalkTime = this.keyInputHandler.talk.getPressedTimes();
				this.isTalking = true;
			}
		}

		if (this.isTalking && this.keyInputHandler.talk.isPressed()
				&& this.pressTalkTime < this.keyInputHandler.talk.getPressedTimes()) {
			// �رնԻ�
			UIManager.getUIEntity("talkBox").visible = false;
			UIManager.getUIEntity("talkNPCProfile").visible = false;
			UIManager.getUIEntity("playerProfile").visible = false;
			UIManager.getUIEntity("firstText").visible = false;
			this.isTalking = false;
			this.pressTalkTime = this.keyInputHandler.talk.getPressedTimes();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if (this.isTalking) {
			UIManager.getUIEntity("talkBox").visible = true;
			UIManager.getUIEntity("talkNPCProfile").visible = true;
			UIManager.getUIEntity("playerProfile").visible = true;

			UIManager.getUIEntity("firstText").visible = true;
			((Text)UIManager.getUIEntity("firstText")).color = Color.white;
			
			// ��̸��ʱ����Ұ�Enter���������ֳ��ּ���
			if (this.keyInputHandler.enter.isPressed()
					&& this.pressEnterTime < this.keyInputHandler.enter.getPressedTimes()) {
				Debug.log(Debug.DebugLevel.INFO, "���٣�" + this.pressEnterTime);
				// ����Ļ�ϻ�������
				this.pressEnterTime = this.keyInputHandler.enter.getPressedTimes();
			}

		}
	}

}
