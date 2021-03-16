package priv.sandbox.game;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import priv.dremma.game.GameCore;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.Text;
import priv.dremma.game.ui.UIEntity;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;
import priv.sandbox.game.collision.SandboxCollisionBox;
import priv.sandbox.game.control.BagControl;
import priv.sandbox.game.entities.ConversationalNPC;
import priv.sandbox.game.entities.FightingNPC;
import priv.sandbox.game.entities.Player;
import priv.sandbox.game.entities.PropCellView;
import priv.sandbox.game.entities.Weapon;

@SuppressWarnings("serial")
public class Sandbox extends GameCore {
	private Player player;

	@Override
	public void onStart() {
		this.setName("SandBox");

		Sandbox.scale = 3;
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5; // 设置2D游戏视角

		// --------------设置游戏按键----------------
		ArrayList<Integer> upKeyCodes = new ArrayList<Integer>();
		upKeyCodes.add(KeyEvent.VK_W);
		upKeyCodes.add(KeyEvent.VK_UP);
		this.keyInputHandler.setVirtualKey("up", upKeyCodes);

		ArrayList<Integer> downKeyCodes = new ArrayList<Integer>();
		downKeyCodes.add(KeyEvent.VK_S);
		downKeyCodes.add(KeyEvent.VK_DOWN);
		this.keyInputHandler.setVirtualKey("down", downKeyCodes);

		ArrayList<Integer> leftKeyCodes = new ArrayList<Integer>();
		leftKeyCodes.add(KeyEvent.VK_A);
		leftKeyCodes.add(KeyEvent.VK_LEFT);
		this.keyInputHandler.setVirtualKey("left", leftKeyCodes);

		ArrayList<Integer> rightKeyCodes = new ArrayList<Integer>();
		rightKeyCodes.add(KeyEvent.VK_D);
		rightKeyCodes.add(KeyEvent.VK_RIGHT);
		this.keyInputHandler.setVirtualKey("right", rightKeyCodes);

		ArrayList<Integer> attackKeyCodes = new ArrayList<Integer>();
		attackKeyCodes.add(KeyEvent.VK_J);
		this.keyInputHandler.setVirtualKey("attack", attackKeyCodes);

		ArrayList<Integer> talkKeyCodes = new ArrayList<Integer>();
		talkKeyCodes.add(KeyEvent.VK_T);
		this.keyInputHandler.setVirtualKey("talk", talkKeyCodes);

		ArrayList<Integer> enterKeyCodes = new ArrayList<Integer>();
		enterKeyCodes.add(KeyEvent.VK_ENTER);
		this.keyInputHandler.setVirtualKey("enter", enterKeyCodes);

		ArrayList<Integer> bagKeyCodes = new ArrayList<Integer>();
		bagKeyCodes.add(KeyEvent.VK_B);
		this.keyInputHandler.setVirtualKey("bag", bagKeyCodes);

		// 设置游戏主角
		player = new Player(this.keyInputHandler, 60f);

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);
		player.setScale(new Vector2(2f, 2f));

		// --------------加载音乐----------------
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "runSound", Resources.path + "music/run.wav");
		Resources.load(Resources.ResourceType.Music, "attackSound", Resources.path + "music/attack.wav");
		Resources.load(Resources.ResourceType.Music, "attackWithBowSound", Resources.path + "music/射箭声.wav");
		Resources.load(Resources.ResourceType.Music, "ghostFloatSound", Resources.path + "music/鬼漂浮声.wav");

		Resources.load(Resources.ResourceType.Music, "ghostDieSound", Resources.path + "music/鬼死亡.wav");
		Resources.load(Resources.ResourceType.Music, "playerDieSound", Resources.path + "music/主角死亡.wav");
		Resources.load(Resources.ResourceType.Music, "playerWinSound", Resources.path + "music/主角胜利.wav");
		Resources.load(Resources.ResourceType.Music, "playerClapSound", Resources.path + "music/clap.wav");

		Resources.load(Resources.ResourceType.Music, "ghostWoundedSound", Resources.path + "music/鬼受伤.wav");
		AudioManager.getInstance().setVolumn("ghostWoundedSound", 100);
		Resources.load(Resources.ResourceType.Music, "playerWoundedSound", Resources.path + "music/主角受伤.wav");
		AudioManager.getInstance().setVolumn("playerWoundedSound", 100);

		// --------------加载地图------------------
		// 加载基础地砖
		Resources.load(Resources.ResourceType.Tile, "0", Resources.path + "images/tiles/floor_0.png");
		Resources.load(Resources.ResourceType.Tile, "1", Resources.path + "images/tiles/floor_1.png");
		Resources.load(Resources.ResourceType.Tile, "2", Resources.path + "images/tiles/floor_2.png");
		Resources.load(Resources.ResourceType.Tile, "3", Resources.path + "images/tiles/floor_3.png");
		Resources.load(Resources.ResourceType.Tile, "4", Resources.path + "images/tiles/floor_4.png");

		// 根据基础地砖从文件中加载地图
		map = TileMap.loadTileMap(Resources.path + "maps/map1.txt");
		map.setPlayer(player);

		// --------------场景中的物体----------------

		// tree1
		Entity tree1Entity = new Entity();
		tree1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree1.png"));

		tree1Entity.setScale(new Vector2(3f, 3f));
		tree1Entity.name = "tree1_1";
		map.addEntity(tree1Entity, new Vector2(2, 5));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("tree1_1").name,
				new SandboxCollisionBox(TileMap.getEntity("tree1_1").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("tree1_1").position.add(Vector2.one().mul(50))));

		tree1Entity.name = "tree1_2";
		tree1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree1Entity, new Vector2(4, 5));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("tree1_2").name,
				new SandboxCollisionBox(TileMap.getEntity("tree1_2").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("tree1_2").position.add(Vector2.one().mul(50))));

		tree1Entity.name = "tree1_3";
		tree1Entity.setScale(new Vector2(1f, 1f));
		map.addEntity(tree1Entity, new Vector2(3, 11));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("tree1_3").name,
				new SandboxCollisionBox(TileMap.getEntity("tree1_3").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("tree1_3").position.add(Vector2.one().mul(50))));

		// tree2
		Entity tree2Entity = new Entity();
		tree2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree2.png"));

		tree2Entity.name = "tree2";
		tree2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree2Entity, new Vector2(4, 8));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("tree2").name,
				new SandboxCollisionBox(TileMap.getEntity("tree2").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("tree2").position.add(Vector2.one().mul(50))));

		// storageBox
		Entity storageBoxEntity = new Entity();
		storageBoxEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/storageBox.png"));

		storageBoxEntity.name = "storageBox";
		storageBoxEntity.setScale(new Vector2(0.2f, 0.2f));
		map.addEntity(storageBoxEntity, new Vector2(3, 6));
		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("storageBox").name,
				new SandboxCollisionBox(TileMap.getEntity("storageBox").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("storageBox").position.add(Vector2.one().mul(50))));
		SandboxCollisionBox.collisionBoxs.get("storageBox").isTrigger = true; // 触发盒子

		// bow
		Entity bowEntity = new Entity();
		bowEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/bow.png"));
		bowEntity.rotation = 60;
		bowEntity.name = "bow";
		bowEntity.setScale(new Vector2(0.125f, 0.125f));
		map.addEntity(bowEntity, new Vector2(692, 562));
		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("bow").name,
				new SandboxCollisionBox(new Vector2(533, 508), new Vector2(664, 601)));
		SandboxCollisionBox.collisionBoxs.get("bow").isTrigger = true; // 触发盒子

		// chair1
		Entity chair1Entity = new Entity();
		chair1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair1.png"));

		chair1Entity.name = "chair1";
		chair1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair1Entity, new Vector2(4, 8));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("chair1").name,
				new SandboxCollisionBox(TileMap.getEntity("chair1").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("chair1").position.add(Vector2.one().mul(50))));

		// chair2
		Entity chair2Entity = new Entity();
		chair2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair2.png"));

		chair2Entity.name = "chair2";
		chair2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair2Entity, new Vector2(4, 5));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("chair2").name,
				new SandboxCollisionBox(TileMap.getEntity("chair2").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("chair2").position.add(Vector2.one().mul(50))));

		// chair3
		Entity chair3Entity = new Entity();
		chair3Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair3.png"));

		chair3Entity.name = "chair3";
		chair3Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair3Entity, new Vector2(4, 11));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("chair3").name,
				new SandboxCollisionBox(TileMap.getEntity("chair3").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("chair3").position.add(Vector2.one().mul(50))));

		// chair4
		Entity chair4Entity = new Entity();
		chair4Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair4.png"));

		chair4Entity.name = "chair4";
		chair4Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair4Entity, new Vector2(5, 5));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("chair4").name,
				new SandboxCollisionBox(TileMap.getEntity("chair4").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("chair4").position.add(Vector2.one().mul(50))));

		// desk
		Entity deskEntity = new Entity();
		deskEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/desk.png"));

		deskEntity.name = "desk";
		deskEntity.setScale(new Vector2(2f, 2f));
		map.addEntity(deskEntity, new Vector2(5, 8));

		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("desk").name,
				new SandboxCollisionBox(TileMap.getEntity("desk").position.sub(Vector2.one().mul(50)),
						TileMap.getEntity("desk").position.add(Vector2.one().mul(50))));

		// --------------场景中的NPC----------------

		// 南极仙翁（对话NPC）
		ConversationalNPC talkNPC = new ConversationalNPC(this.keyInputHandler, 0, "南极仙翁");
		talkNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(talkNPC, new Vector2(1043, 275));

		SandboxCollisionBox.collisionBoxs.put(talkNPC.name, new SandboxCollisionBox(
				talkNPC.position.sub(Vector2.one().mul(50)), talkNPC.position.add(Vector2.one().mul(50))));

		// 野鬼（打斗NPC）
		FightingNPC fightingNPC = new FightingNPC(30f);
		fightingNPC.name = "野鬼";
		fightingNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(fightingNPC, new Vector2(800, 600));

		SandboxCollisionBox.collisionBoxs.put(fightingNPC.name, new SandboxCollisionBox(
				fightingNPC.position.sub(Vector2.one().mul(50)), fightingNPC.position.add(Vector2.one().mul(50))));

		// --------------场景中的边界----------------

		float borderThickness = 100f;
		// 为地图边界添加碰撞盒 Up
		Entity mapBorderUp = new Entity();
		mapBorderUp.name = "mapBorderUp";
		mapBorderUp.visible = false;
		TileMap.addEntity("mapBorderUp", mapBorderUp);
		SandboxCollisionBox.collisionBoxs.put("mapBorderUp", new SandboxCollisionBox(
				new Vector2(0, -2 - borderThickness), new Vector2(map.worldEndTileCenter.x, -1)));

		// 为地图边界添加碰撞盒 Down
		Entity mapBorderDown = new Entity();
		mapBorderDown.name = "mapBorderDown";
		mapBorderDown.visible = false;
		TileMap.addEntity("mapBorderDown", mapBorderDown);
		SandboxCollisionBox.collisionBoxs.put("mapBorderDown",
				new SandboxCollisionBox(new Vector2(0, map.worldEndTileCenter.y + 1 - 23),
						new Vector2(map.worldEndTileCenter.x, map.worldEndTileCenter.y + 2 - 23 + borderThickness)));
		if (GameCore.isApplet) {
			// 如果是applet端，地图下边界需要上移24（24是applet和非applet端y轴的差）
			SandboxCollisionBox.collisionBoxs.get("mapBorderDown").trans(new Vector2(0, 24));
		}

		// 为地图边界添加碰撞盒 Left
		Entity mapBorderLeft = new Entity();
		mapBorderLeft.name = "mapBorderLeft";
		mapBorderLeft.visible = false;
		TileMap.addEntity("mapBorderLeft", mapBorderLeft);
		SandboxCollisionBox.collisionBoxs.put("mapBorderLeft", new SandboxCollisionBox(
				new Vector2(-2 - borderThickness, 0), new Vector2(-1, map.worldEndTileCenter.y)));

		// 为地图边界添加碰撞盒 Right
		Entity mapBorderRight = new Entity();
		mapBorderRight.name = "mapBorderRight";
		mapBorderRight.visible = false;
		TileMap.addEntity("mapBorderRight", mapBorderRight);
		SandboxCollisionBox.collisionBoxs.put("mapBorderRight",
				new SandboxCollisionBox(new Vector2(map.worldEndTileCenter.x + 1, 0),
						new Vector2(map.worldEndTileCenter.x + 2 + borderThickness, map.worldEndTileCenter.y)));

		// --------------UI----------------

		// 玩家血条底部
		UIEntity playerHpBarBase = new UIEntity();
		playerHpBarBase.setStaticImage(Resources.loadImage(Resources.path + "images/血条底.png"));
		playerHpBarBase.name = "playerHpBarBase";
		playerHpBarBase.setScale(new Vector2(300, 20));
		playerHpBarBase.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBarBase);

		// 玩家血条
		UIEntity playerHpBar = new UIEntity();
		playerHpBar.setStaticImage(Resources.loadImage(Resources.path + "images/border.png"));
		playerHpBar.name = "playerHpBar";
		playerHpBar.setScale(new Vector2(300, 20));
		playerHpBar.algin = UIEntity.UIAlign.LEFT;
		playerHpBar.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBar);

		// 玩家血条头像
		UIEntity playerHpProfile = new UIEntity();
		playerHpProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/主角头像.png"));
		playerHpProfile.name = "playerHpProfile";
		playerHpProfile.setScale(new Vector2(0.5f, 0.5f));
		playerHpProfile.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpProfile);

		// fightingNPC血条底部
		UIEntity fightingNPCHpBarBase = new UIEntity();
		fightingNPCHpBarBase.mode = UIEntity.UIMode.WORLD;
		fightingNPCHpBarBase.setStaticImage(Resources.loadImage(Resources.path + "images/血条底.png"));
		fightingNPCHpBarBase.name = "fightingNPCHpBarBase";
		fightingNPCHpBarBase.setScale(new Vector2(fightingNPC.getWidth() * fightingNPC.getScale().x, 10));
		Vector2 fightingNPCHpBarPos = new Vector2(
				fightingNPC.position.sub(new Vector2(fightingNPC.getWidth() * fightingNPC.getScale().x / 2,
						fightingNPC.getHeight() * fightingNPC.getScale().y / 2 + 20)));
		fightingNPCHpBarBase.position = new Vector2(
				fightingNPCHpBarPos.x - fightingNPC.getWidth() * fightingNPC.getScale().x / 2, fightingNPCHpBarPos.y);

		UIManager.addUI(fightingNPCHpBarBase);

		// fightingNPC血条
		UIEntity fightingNPCHpBar = new UIEntity();
		fightingNPCHpBar.mode = UIEntity.UIMode.WORLD;
		fightingNPCHpBar.setStaticImage(Resources.loadImage(Resources.path + "images/xArrow.png"));
		fightingNPCHpBar.name = "fightingNPCHpBar";
		fightingNPCHpBar.setScale(new Vector2(fightingNPC.getWidth() * fightingNPC.getScale().x, 10));
		fightingNPCHpBar.algin = UIEntity.UIAlign.LEFT;
		fightingNPCHpBar.position = fightingNPCHpBarPos;

		UIManager.addUI(fightingNPCHpBar);

		// 背包icon，点击背包icon进入背包界面
		UIEntity bagIcon = new UIEntity(this.mouseInputHandler);
		bagIcon.setStaticImage(Resources.loadImage(Resources.path + "images/bagIcon.png"));
		bagIcon.name = "bagIcon";
		bagIcon.setScale(new Vector2(0.5f, 0.5f));
		bagIcon.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);

		UIManager.addUI(bagIcon);

		// 背包界面
		UIEntity bagView = new UIEntity();
		bagView.setStaticImage(Resources.loadImage(Resources.path + "images/bag.png"));
		bagView.name = "bagView";
		bagView.setScale(new Vector2(0.5f, 0.5f));
		bagView.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		bagView.visible = false;

		UIManager.addUI(bagView);

		// 为bagView加上道具栏
		ArrayList<PropCellView> bagPropItemViews = new ArrayList<PropCellView>();
		Image bagPropFrame = Resources.loadImage(Resources.path + "images/bagCell.png");

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 5; j++) {
				PropCellView propCell = new PropCellView(bagPropFrame, this.mouseInputHandler);
				propCell.name = "bagPropCell" + (i * 5 + j);
				propCell.setScale(new Vector2(0.5f, 0.5f));
				propCell.position = new Vector2(548 + j * 60, 152 + i * 60);
				propCell.visible = false;
				bagPropItemViews.add(propCell);
				UIManager.attachUI(bagView, propCell);
			}
		}
		((Player) TileMap.player).bag = new BagControl(bagPropItemViews);

		UIEntity playerView = new UIEntity();
		playerView.setStaticImage(Resources.loadImage(Resources.path + "images/storageBox.png"));
		playerView.name = "playerView";
		playerView.setScale(new Vector2(2f, 2.7f));
		playerView.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		playerView.visible = false;

		UIManager.addUI(playerView);

		// 角色展示图
		UIEntity playerShowView = new UIEntity();
		playerShowView.animator = TileMap.player.animator;
		playerShowView.name = "playerShowView";
		playerShowView.setScale(new Vector2(5f, 5f));
		playerShowView.position = new Vector2(GameCore.screen.width / 2 - 150, GameCore.screen.height / 2);
		playerShowView.visible = false;

		UIManager.attachUI(playerView, playerShowView);

		// 为playerView加上道具栏，方便卸下装备
		PropCellView playerViewPropCell = new PropCellView(
				Resources.loadImage(Resources.path + "images/storageBoxCell.png"), this.mouseInputHandler);
		playerViewPropCell.name = "playerViewPropCell";
		playerViewPropCell.setScale(new Vector2(0.5f, 0.5f));
		playerViewPropCell.position = new Vector2(GameCore.screen.width / 2 - 150 + 120,
				GameCore.screen.height / 2 - 196);
		playerViewPropCell.visible = false;
		bagPropItemViews.add(playerViewPropCell);
		UIManager.attachUI(playerView, playerViewPropCell);

		// 背包界面的关闭按钮
		UIEntity playerViewCloseBtn = new UIEntity(this.mouseInputHandler);
		playerViewCloseBtn.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
		playerViewCloseBtn.name = "playerViewCloseBtn";
		playerViewCloseBtn.setScale(new Vector2(0.5f, 0.5f));
		playerViewCloseBtn.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		playerViewCloseBtn.visible = false;

		UIManager.attachUI(playerView, playerViewCloseBtn);

		// 储物箱界面，玩家触发储物盒时出现
		UIEntity storageBoxView = new UIEntity();
		storageBoxView.setStaticImage(Resources.loadImage(Resources.path + "images/storageBox.png"));
		storageBoxView.name = "storageBoxView";
		storageBoxView.setScale(new Vector2(2f, 2.7f));
		storageBoxView.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		storageBoxView.visible = false;

		UIManager.addUI(storageBoxView);

		// 为storageBoxView加上道具栏
		ArrayList<PropCellView> storageBoxPropItemViews = new ArrayList<PropCellView>();
		Image storageBoxPropFrame = Resources.loadImage(Resources.path + "images/storageBoxCell.png");

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 5; j++) {
				PropCellView propCell = new PropCellView(storageBoxPropFrame, this.mouseInputHandler);
				propCell.name = "storageBoxPropCell" + (i * 5 + j);
				propCell.setScale(new Vector2(0.5f, 0.5f));
				propCell.position = new Vector2(202 + j * 60, 152 + i * 60);
				propCell.visible = false;
				storageBoxPropItemViews.add(propCell);
				UIManager.attachUI(storageBoxView, propCell);
			}
		}
		((Player) TileMap.player).storageBox = new BagControl(storageBoxPropItemViews);

		// 储物箱界面的关闭按钮
		UIEntity storageBoxCloseBtn = new UIEntity(this.mouseInputHandler);
		storageBoxCloseBtn.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
		storageBoxCloseBtn.name = "storageBoxCloseBtn";
		storageBoxCloseBtn.setScale(new Vector2(0.5f, 0.5f));
		storageBoxCloseBtn.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		storageBoxCloseBtn.visible = false;

		UIManager.attachUI(storageBoxView, storageBoxCloseBtn);

		// 道具：弓
		UIEntity bowPropItem = new UIEntity(this.mouseInputHandler);
		bowPropItem.setStaticImage(Resources.loadImage(Resources.path + "images/entities/bowPropItem.png"));
		bowPropItem.name = "bowPropItem";
		bowPropItem.setScale(new Vector2(0.6f, 0.6f));
		bowPropItem.visible = false;

		UIManager.addUI(bowPropItem);

		// 选项界面
		UIEntity optionView = new UIEntity();
		optionView.setStaticImage(Resources.loadImage(Resources.path + "images/optionFrame.png"));
		optionView.name = "optionView";
		optionView.setScale(new Vector2(0.13f, 0.07f));
		optionView.visible = false;

		UIManager.attachUI(bowPropItem, optionView);

		// 选项界面中的选项
		UIEntity option = new UIEntity(this.mouseInputHandler);
		option.setStaticImage(Resources.loadImage(Resources.path + "images/option.png"));
		option.name = "option";
		option.setScale(new Vector2(0.5f, 1f));
		option.visible = false;

		UIManager.attachUI(optionView, option);

		// 选项表面的字：装备
		Text optionTxt = new Text("装备");
		optionTxt.name = "optionTxt";
		optionTxt.setFontSize(20);
		optionTxt.color = Color.lightGray;
		optionTxt.visible = false;

		UIManager.attachUI(option, optionTxt);

		// 选项表面的字：卸下
		Text optionTxt2 = new Text("卸下");
		optionTxt2.name = "optionTxt2";
		optionTxt2.setFontSize(20);
		optionTxt2.color = Color.lightGray;
		optionTxt2.visible = false;

		UIManager.addUI(optionTxt2);

		// 选项表面的字：放入
		Text optionTxt3 = new Text("放入");
		optionTxt3.name = "optionTxt3";
		optionTxt3.setFontSize(20);
		optionTxt3.color = Color.lightGray;
		optionTxt3.visible = false;

		UIManager.addUI(optionTxt3);

		// 选项表面的字：取出
		Text optionTxt4 = new Text("取出");
		optionTxt4.name = "optionTxt4";
		optionTxt4.setFontSize(20);
		optionTxt4.color = Color.lightGray;
		optionTxt4.visible = false;

		UIManager.addUI(optionTxt4);

		// 对话框
		UIEntity talkBox = new UIEntity();
		talkBox.setStaticImage(Resources.loadImage(Resources.path + "images/对话框.png"));
		talkBox.name = "talkBox";
		talkBox.visible = false;
		talkBox.setScale(new Vector2(2f, 1.5f));
		talkBox.position = new Vector2(GameCore.screen.width / 2f,
				GameCore.screen.height - talkBox.getHeight() * talkBox.getScale().y / 2 - 25);

		UIManager.addUI(talkBox);

		// 南极仙翁头像
		UIEntity talkNPCProfile = new UIEntity();
		talkNPCProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/南极仙翁头像.png"));
		talkNPCProfile.name = "talkNPCProfile";
		talkNPCProfile.visible = false;
		talkNPCProfile.position = new Vector2(107, 325);

		UIManager.addUI(talkNPCProfile);

		// 主角头像
		UIEntity playerProfile = new UIEntity();
		playerProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/主角头像.png"));
		playerProfile.name = "playerProfile";
		playerProfile.visible = false;
		playerProfile.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerProfile);

		// 文字
		Text text = new Text("欢迎你来到游戏世界！！");
		text.needAlignScreen = true;
		text.name = "firstText";
		text.visible = false;
		text.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);

		UIManager.addUI(text);

		// --------------加载数据----------------

		SandboxCollisionBox.notLoadNameSubStrings.add("野鬼");
		SandboxCollisionBox.notLoadNameSubStrings.add("mapBorder");
		SandboxCollisionBox.notLoadNameSubStrings.add("Attack");
		SandboxCollisionBox.notLoadNameSubStrings.add("bow");

		TranslateEntityHelper.notLoadNameSubStrings.add("野鬼");
		TranslateEntityHelper.notLoadNameSubStrings.add("Attack");
		TranslateEntityHelper.notLoadNameSubStrings.add("bow");
		TranslateEntityHelper.notLoadNameSubStrings.add("Prop");
		TranslateEntityHelper.notLoadNameSubStrings.add("playerShowView");
		TranslateEntityHelper.notLoadNameSubStrings.add("option");

		SandboxCollisionBox.load(); // 从数据文件中加载碰撞盒数据
		TranslateEntityHelper.load(); // 从数据文件中加载移动帮助数据

		// ---------------------配置-----------------------
		SandboxCollisionBox.shouldRender = true; // 渲染碰撞盒
		TranslateEntityHelper.shouldRender = true; // 渲染移动拖拽帮助

	}

	static int pressBagButtonTimes = 0;

	@Override
	public void onUpdate() {
		// 当玩家点击背包图标时，显示背包界面
		if (UIManager.getUIEntity("storageBoxView").visible == false
				&& UIManager.getUIEntity("bagIcon").isPressedMouseButton()) {
			UIManager.setUIVisibility("bagView", true);
			UIManager.setUIVisibility("playerView", true);
			UIManager.setUIVisibility("optionView", false);
		}

		// 当玩家按背包键时，背包关闭则开启，反之则关闭
		if (this.keyInputHandler.getVirtualKey("bag").isPressed()
				&& pressBagButtonTimes < this.keyInputHandler.getVirtualKey("bag").getPressedTimes()) {
			if (UIManager.getUIEntity("storageBoxView").visible == false) {
				if (UIManager.getUIEntity("bagView").visible == false) {
					// 显示
					UIManager.setUIVisibility("bagView", true);
					UIManager.setUIVisibility("playerView", true);
					UIManager.setUIVisibility("optionView", false);
				} else {
					// 关闭
					if (UIManager.getUIEntity("storageBoxView").visible) {
						UIManager.setUIVisibility("bagView", true);
					} else {
						UIManager.setUIVisibility("bagView", false);
					}
					UIManager.setUIVisibility("playerView", false);
				}
				pressBagButtonTimes = this.keyInputHandler.getVirtualKey("bag").getPressedTimes();
			}
		}

		// 关闭角色展示界面
		if (UIManager.getUIEntity("playerViewCloseBtn").visible == true) {
			// 鼠标移到角色展示界面关闭按钮时按钮高亮
			if (UIManager.getUIEntity("playerViewCloseBtn").isChosenMouseButton()) {
				UIManager.getUIEntity("playerViewCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtnChoosen.png"));
			} else {
				UIManager.getUIEntity("playerViewCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
			}
			if (UIManager.getUIEntity("playerViewCloseBtn").isPressedMouseButton()) {
				UIManager.setUIVisibility("bagView", false);
				UIManager.setUIVisibility("playerView", false);
			}
		}

		// 关闭储物箱界面
		if (UIManager.getUIEntity("storageBoxCloseBtn").visible == true) {
			if (UIManager.getUIEntity("storageBoxCloseBtn").isChosenMouseButton()) {
				UIManager.getUIEntity("storageBoxCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtnChoosen.png"));
			} else {
				UIManager.getUIEntity("storageBoxCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
			}
			if (UIManager.getUIEntity("storageBoxCloseBtn").isPressedMouseButton()) {
				UIManager.setUIVisibility("bagView", false);
				UIManager.setUIVisibility("storageBoxView", false);
			}
		}

		if (UIManager.getUIEntity("playerView").visible) {
			// 在打开角色展示界面时
			
			// 更新选项文字
			if (UIManager.getUIEntity("bowPropItem").getParent() != null) {
				if (UIManager.getUIEntity("bowPropItem").getParent().name.startsWith("playerViewPropCell")
						&& UIManager.getUIEntity("optionTxt2").getParent() != UIManager.getUIEntity("option")) {
					UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
					UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt2"));
				}
				if (UIManager.getUIEntity("bowPropItem").getParent().name.startsWith("bagPropCell")
						&& UIManager.getUIEntity("optionTxt").getParent() != UIManager.getUIEntity("option")) {
					UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
					UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt"));
				}
			}
			
			if (UIManager.getUIEntity("option").isPressedMouseButton() && UIManager.getUIEntity("optionTxt2").visible) {
				// 当玩家点击卸下选项时，选项界面消失，角色展示界面中的装备消失，该装备装入背包
				UIManager.setUIVisibility("optionView", false);
				// 从玩家的装备格子中消失
				((PropCellView) UIManager.getUIEntity("playerViewPropCell")).removePropItem();
				this.player.unequipWeapon();

				((Player) TileMap.player).bag.addPropItemToBag("bowPropItem");
			}

			if (UIManager.getUIEntity("option").isPressedMouseButton() && UIManager.getUIEntity("optionTxt").visible) {
				// 当玩家点击装备选项时，选项界面消失，背包中的装备消失，玩家装备上该装备
				UIManager.setUIVisibility("optionView", false);
				((Player) TileMap.player).bag.removePropItemFromBag("bowPropItem");

				this.player.equipWeapon(new Weapon("bow", 2, 270f));
				// 放入玩家的装备格子
				((PropCellView) UIManager.getUIEntity("playerViewPropCell"))
						.setPropItem(UIManager.getUIEntity("bowPropItem"));
			}
		}

		if (UIManager.getUIEntity("storageBoxView").visible) {
			// 当储物箱界面打开时
			
			// 更新选项文字
			if (UIManager.getUIEntity("bowPropItem").getParent() != null) {
				if (UIManager.getUIEntity("bowPropItem").getParent().name.startsWith("bagPropCell")
						&& UIManager.getUIEntity("optionTxt3").getParent() != UIManager.getUIEntity("option")) {
					UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
					UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt3"));
				}
				if (UIManager.getUIEntity("bowPropItem").getParent().name.startsWith("storageBoxPropCell")
						&& UIManager.getUIEntity("optionTxt4").getParent() != UIManager.getUIEntity("option")) {
					UIManager.detachAllChildUI(UIManager.getUIEntity("option"));
					UIManager.attachUI(UIManager.getUIEntity("option"), UIManager.getUIEntity("optionTxt4"));
				}
			}

			if (UIManager.getUIEntity("option").isPressedMouseButton() && UIManager.getUIEntity("optionTxt3").visible) {
				// 当玩家点击放入选项时，选项界面消失，背包中的装备消失，装备被添加到储物箱中
				UIManager.setUIVisibility("optionView", false);
				((Player) TileMap.player).bag.removePropItemFromBag("bowPropItem");
				((Player) TileMap.player).storageBox.addPropItemToBag("bowPropItem");
			}

			if (UIManager.getUIEntity("option").isPressedMouseButton() && UIManager.getUIEntity("optionTxt4").visible) {
				// 当玩家点击取出选项时，选项界面消失，储物箱中的装备消失，装备被添加到背包中
				UIManager.setUIVisibility("optionView", false);
				// 此处应先删后加，否则bowPropItem的双亲将变成null
				((Player) TileMap.player).storageBox.removePropItemFromBag("bowPropItem");
				((Player) TileMap.player).bag.addPropItemToBag("bowPropItem");
			}
		}

		// 当玩家点击道具时，弹出道具的选项界面
		if (UIManager.getUIEntity("bowPropItem") != null
				&& UIManager.getUIEntity("bowPropItem").isPressedMouseButton()) {
			UIManager.getUIEntity("optionView").position = UIManager.getUIEntity("bowPropItem").position
					.add(new Vector2(
							UIManager.getUIEntity("optionView").getWidth()
									* UIManager.getUIEntity("optionView").getScale().x / 2,
							UIManager.getUIEntity("optionView").getHeight()
									* UIManager.getUIEntity("optionView").getScale().y / 2));
			UIManager.getUIEntity("option").position = UIManager.getUIEntity("optionView").position;
			for (UIEntity ue : UIManager.getChilds("option")) {
				ue.position = UIManager.getUIEntity("option").position
						.sub(new Vector2(((Text) ue).getWidth() / 2, ((Text) ue).getHeight() / 2 - 3));
			}
			UIManager.setUIVisibility("optionView", true);
		} else if (this.mouseInputHandler.mouse.isPressed() && UIManager.getUIEntity("optionView").visible) {
			UIManager.setUIVisibility("optionView", false);
		}

	}
}