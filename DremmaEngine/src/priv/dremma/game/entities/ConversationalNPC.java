package priv.dremma.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Queue;

import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.Text;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC {

	KeyInputHandler keyInputHandler;
	int pressTalkTime = 0;
	int pressEnterTime = 0;
	public boolean isTalking;

	public Queue<String> conversation = new LinkedList<String>();

	public ConversationalNPC(KeyInputHandler keyInputHandler, float speed, String name) {
		super(speed);
		this.nearDistance = 80f;
		this.keyInputHandler = keyInputHandler;
		this.isTalking = false;
		this.name = name;

		this.setStaticImage(Resources.loadImage(Resources.path + "images/entities/南极仙翁.png"));

		this.conversation.add(this.name + "：欢迎来到梦玛世界，我是南极仙翁，你好！");
		this.conversation.add(TileMap.player.name + "：" + this.name + "，你好！我是剑侠客，请问我下一步需要做什么呢？");
		this.conversation.add(this.name + "：传说这里有一只游荡的野鬼，它身上围绕着鬼火，神出鬼没、杀人无数。");
		this.conversation.add(this.name + "：少侠，我希望你可以为民除害，拯救苍生！！");
		this.conversation.add(TileMap.player.name + "：" + "原来如此，多谢老者。侠之大者、为国为民，我定不辱使命！");
		this.conversation.add(this.name + "：果真后生可谓，我这里有一本武林秘籍《九阳神功》，希望可以助你一臂之力！！");
		this.conversation.add(TileMap.player.name + "：多谢老者。");

		// 最后一句
		this.conversation.add(this.name + "：浮生偷得半日闲。(๑‾ ꇴ ‾๑)");
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
			// 关闭对话
			this.closeConversation();
			this.pressTalkTime = this.keyInputHandler.talk.getPressedTimes();
		}
	}

	private void closeConversation() {
		UIManager.getUIEntity("talkBox").visible = false;
		UIManager.getUIEntity("talkNPCProfile").visible = false;
		UIManager.getUIEntity("playerProfile").visible = false;
		UIManager.getUIEntity("firstText").visible = false;
		this.isTalking = false;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if (this.isTalking) {
			UIManager.getUIEntity("talkBox").visible = true;
			UIManager.getUIEntity("talkNPCProfile").visible = true;
			UIManager.getUIEntity("playerProfile").visible = true;

			UIManager.getUIEntity("firstText").visible = true;
			((Text) UIManager.getUIEntity("firstText")).color = Color.white;
			this.setTalkContent();

			// 在谈话时，玩家按Enter键进行翻页
			if (this.keyInputHandler.enter.isPressed()
					&& this.pressEnterTime < this.keyInputHandler.enter.getPressedTimes()) {
				// 处理交流队列，进行翻页
				if (this.conversation.size() > 1) {
					this.conversation.poll();
					if (this.conversation.size() == 1) {
						this.closeConversation();
						TileMap.player.attackHarm += 5;
					}
				} else {
					this.closeConversation();
				}

				this.pressEnterTime = this.keyInputHandler.enter.getPressedTimes();
			}

		}
	}

	private void setTalkContent() {
		((Text) UIManager.getUIEntity("firstText")).content = this.conversation.peek();
		if (this.conversation.peek().startsWith(this.name)) {
			UIManager.getUIEntity("talkNPCProfile").visible = true;
			UIManager.getUIEntity("playerProfile").visible = false;
		}
		if (this.conversation.peek().startsWith(TileMap.player.name)) {
			UIManager.getUIEntity("talkNPCProfile").visible = false;
			UIManager.getUIEntity("playerProfile").visible = true;
		}
	}

}
