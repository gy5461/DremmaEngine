package priv.sandbox.game.entities;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.Text;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;

public class ConversationalNPC extends NPC {
	
	protected KeyInputHandler keyInputHandler;
	protected int pressTalkTime = 0;
	protected int pressEnterTime = 0;
	public boolean isTalking;

	public Queue<String> conversation = new LinkedList<String>();

	public String idleChat = "";

	public ConversationalNPC(KeyInputHandler keyInputHandler, float speed, String name) {
		super(speed);
		
		this.keyInputHandler = keyInputHandler;
		this.isTalking = false;
		this.name = name;

		this.nearDistance = 80f;
		
		this.setStaticImage(Resources.loadImage(Resources.path + "images/entities/南极仙翁.png"));

		this.readConversation(Resources.path + "conversation/conversation.txt");
	}

	protected void readConversation(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while (true) {
				String line;
				try {
					line = reader.readLine();
					if (line == null) {
						reader.close();
						break;
					}
					if (line.startsWith("#")) {
						continue;	// 注释部分跳过
					}
					if (line.startsWith("(Idle)")) {
						String[] idle = GUtils.split(line);
						for (int i = 1; i < idle.length; i++) {
							if (i == idle.length - 1) {
								this.idleChat += idle[i];
							} else {
								this.idleChat += idle[i] + " ";
							}
						}
					} else {
						this.conversation.add(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized void update() {
		super.update();
		if (this.nearPlayer()) {
			if (!this.isTalking && this.keyInputHandler.getVirtualKey("talk").isPressed()
					&& this.pressTalkTime < this.keyInputHandler.getVirtualKey("talk").getPressedTimes()) {
				this.pressTalkTime = this.keyInputHandler.getVirtualKey("talk").getPressedTimes();
				this.isTalking = true;
			}
		}

		if (this.isTalking && this.keyInputHandler.getVirtualKey("talk").isPressed()
				&& this.pressTalkTime < this.keyInputHandler.getVirtualKey("talk").getPressedTimes()) {
			// 关闭对话
			this.closeConversation();
			this.pressTalkTime = this.keyInputHandler.getVirtualKey("talk").getPressedTimes();
		}
		
		if (this.isTalking) {
			UIManager.getUIEntity("talkBox").visible = true;
			UIManager.getUIEntity("talkNPCProfile").visible = true;
			UIManager.getUIEntity("playerProfile").visible = true;

			UIManager.getUIEntity("firstText").visible = true;
			((Text) UIManager.getUIEntity("firstText")).color = Color.white;
			if (!this.conversation.isEmpty()) {
				this.setTalkContent(this.conversation.peek());
			} else {
				this.setTalkContent(this.idleChat);
			}

			// 在谈话时，玩家按Enter键进行翻页
			if (this.keyInputHandler.getVirtualKey("enter").isPressed()
					&& this.pressEnterTime < this.keyInputHandler.getVirtualKey("enter").getPressedTimes()) {
				// 处理交流队列，进行翻页
				if (this.conversation.size() > 0) {
					this.conversation.poll();
					if (this.conversation.size() == 0) {
						this.closeConversation();
						((Player)TileMap.player).attackHarm += 5; // 给游戏主角神功
					}
				} else {
					this.closeConversation();
				}

				this.pressEnterTime = this.keyInputHandler.getVirtualKey("enter").getPressedTimes();
			}

		}
	}

	private void closeConversation() {
		UIManager.getUIEntity("talkBox").visible = false;
		UIManager.getUIEntity("talkNPCProfile").visible = false;
		UIManager.getUIEntity("playerProfile").visible = false;
		UIManager.getUIEntity("firstText").visible = false;
		this.isTalking = false;
	}

	private void setTalkContent(String content) {
		((Text) UIManager.getUIEntity("firstText")).content = content;
		if (content.startsWith(this.name)) {
			UIManager.getUIEntity("talkNPCProfile").visible = true;
			UIManager.getUIEntity("playerProfile").visible = false;
		}
		if (content.startsWith(TileMap.player.name)) {
			UIManager.getUIEntity("talkNPCProfile").visible = false;
			UIManager.getUIEntity("playerProfile").visible = true;
		}
	}

}
