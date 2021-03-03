package priv.sandbox.game;

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
import priv.sandbox.game.entities.ConversationalNPC;
import priv.sandbox.game.entities.FightingNPC;
import priv.sandbox.game.entities.Player;
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

		// this.player.equipWeapon(new Weapon("bow", 2, 270f));

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
		map.addEntity(bowEntity, new Vector2(5, 13));
		SandboxCollisionBox.collisionBoxs.put(TileMap.getEntity("bow").name,
				new SandboxCollisionBox(new Vector2(750.56506f, 781.72107f), new Vector2(887.56506f, 871.72107f)));
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
		text.name = "firstText";
		text.visible = false;
		text.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);

		UIManager.addUI(text);

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

		UIEntity playerView = new UIEntity();
		playerView.setStaticImage(Resources.loadImage(Resources.path + "images/storageBox.png"));
		playerView.name = "playerView";
		playerView.setScale(new Vector2(2f, 2.7f));
		playerView.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		playerView.visible = false;

		UIManager.addUI(playerView);

		// 背包界面的关闭按钮
		UIEntity bagCloseBtn = new UIEntity(this.mouseInputHandler);
		bagCloseBtn.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
		bagCloseBtn.name = "bagCloseBtn";
		bagCloseBtn.setScale(new Vector2(0.5f, 0.5f));
		bagCloseBtn.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		bagCloseBtn.visible = false;

		UIManager.addUI(bagCloseBtn);

		// 储物箱界面，玩家触发储物盒时出现
		UIEntity storageBoxView = new UIEntity();
		storageBoxView.setStaticImage(Resources.loadImage(Resources.path + "images/storageBox.png"));
		storageBoxView.name = "storageBoxView";
		storageBoxView.setScale(new Vector2(2f, 2.7f));
		storageBoxView.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		storageBoxView.visible = false;

		UIManager.addUI(storageBoxView);

		// 储物箱界面的关闭按钮
		UIEntity storageBoxCloseBtn = new UIEntity(this.mouseInputHandler);
		storageBoxCloseBtn.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
		storageBoxCloseBtn.name = "storageBoxCloseBtn";
		storageBoxCloseBtn.setScale(new Vector2(0.5f, 0.5f));
		storageBoxCloseBtn.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);
		storageBoxCloseBtn.visible = false;

		UIManager.addUI(storageBoxCloseBtn);

		// --------------加载数据----------------

		SandboxCollisionBox.notLoadNameSubStrings.add("野鬼");
		SandboxCollisionBox.notLoadNameSubStrings.add("mapBorder");
		SandboxCollisionBox.notLoadNameSubStrings.add("Attack");
		SandboxCollisionBox.notLoadNameSubStrings.add("bow");

		TranslateEntityHelper.notLoadNameSubStrings.add("野鬼");
		TranslateEntityHelper.notLoadNameSubStrings.add("Attack");
		TranslateEntityHelper.notLoadNameSubStrings.add("bow");

		SandboxCollisionBox.load(); // 从数据文件中加载碰撞盒数据
		TranslateEntityHelper.load(); // 从数据文件中加载移动帮助数据

		// ---------------------配置-----------------------
//		SandboxCollisionBox.shouldRender = true; // 渲染碰撞盒
		TranslateEntityHelper.shouldRender = true; // 渲染移动拖拽帮助

	}

	@Override
	public void onUpdate() {
		// 当玩家点击背包图标时，显示背包界面
		if (UIManager.getUIEntity("bagIcon").isPressedMouseButton()) {
			UIManager.getUIEntity("bagView").visible = true;
			UIManager.getUIEntity("playerView").visible = true;
			UIManager.getUIEntity("bagCloseBtn").visible = true;
		}
		// 当玩家按背包键时，奇数次显示背包界面，偶数次关闭背包界面
		if (this.keyInputHandler.getVirtualKey("bag").isPressed()) {
			if (this.keyInputHandler.getVirtualKey("bag").getPressedTimes() % 2 == 1) {
				UIManager.getUIEntity("bagView").visible = true;
				UIManager.getUIEntity("playerView").visible = true;
				UIManager.getUIEntity("bagCloseBtn").visible = true;
			} else {
				if (UIManager.getUIEntity("storageBoxView").visible) {
					UIManager.getUIEntity("bagView").visible = true;
				} else {
					UIManager.getUIEntity("bagView").visible = false;
				}
				UIManager.getUIEntity("playerView").visible = false;
				UIManager.getUIEntity("bagCloseBtn").visible = false;
			}
		}

		if (UIManager.getUIEntity("bagCloseBtn").visible == true) {
			if (UIManager.getUIEntity("bagCloseBtn").isChosenMouseButton()) {
				UIManager.getUIEntity("bagCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtnChoosen.png"));
			} else {
				UIManager.getUIEntity("bagCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
			}
			if (UIManager.getUIEntity("bagCloseBtn").isPressedMouseButton()) {
				UIManager.getUIEntity("bagView").visible = false;
				UIManager.getUIEntity("playerView").visible = false;
				UIManager.getUIEntity("bagCloseBtn").visible = false;
			}
		}

		if (UIManager.getUIEntity("storageBoxCloseBtn").visible == true) {
			if (UIManager.getUIEntity("storageBoxCloseBtn").isChosenMouseButton()) {
				UIManager.getUIEntity("storageBoxCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtnChoosen.png"));
			} else {
				UIManager.getUIEntity("storageBoxCloseBtn")
						.setStaticImage(Resources.loadImage(Resources.path + "images/closeBtn.png"));
			}
			if (UIManager.getUIEntity("storageBoxCloseBtn").isPressedMouseButton()) {
				UIManager.getUIEntity("bagView").visible = false;
				UIManager.getUIEntity("storageBoxView").visible = false;
				UIManager.getUIEntity("storageBoxCloseBtn").visible = false;
			}
		}
	}
}