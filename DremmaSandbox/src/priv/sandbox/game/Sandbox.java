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

		// 设置游戏主角
		player = new Player(this.keyInputHandler, 60f);

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);

		player.setScale(new Vector2(2f, 2f));

		// --------------加载音乐----------------
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "runSound", Resources.path + "music/run.wav");
		Resources.load(Resources.ResourceType.Music, "attackSound", Resources.path + "music/attack.wav");
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

		SandboxCollisionBox.collisionBoxs.put(tree1Entity.name, new SandboxCollisionBox(
				tree1Entity.position.sub(Vector2.one().mul(50)), tree1Entity.position.add(Vector2.one().mul(50))));

		tree1Entity.name = "tree1_2";
		tree1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree1Entity, new Vector2(4, 5));

		SandboxCollisionBox.collisionBoxs.put(tree1Entity.name, new SandboxCollisionBox(
				tree1Entity.position.sub(Vector2.one().mul(50)), tree1Entity.position.add(Vector2.one().mul(50))));

		tree1Entity.name = "tree1_3";
		tree1Entity.setScale(new Vector2(1f, 1f));
		map.addEntity(tree1Entity, new Vector2(3, 11));

		SandboxCollisionBox.collisionBoxs.put(tree1Entity.name, new SandboxCollisionBox(
				tree1Entity.position.sub(Vector2.one().mul(50)), tree1Entity.position.add(Vector2.one().mul(50))));

		// tree2
		Entity tree2Entity = new Entity();
		tree2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree2.png"));

		tree2Entity.name = "tree2";
		tree2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree2Entity, new Vector2(4, 8));

		SandboxCollisionBox.collisionBoxs.put(tree2Entity.name, new SandboxCollisionBox(
				tree2Entity.position.sub(Vector2.one().mul(50)), tree2Entity.position.add(Vector2.one().mul(50))));

		// archiving
		Entity archivingEntity = new Entity();
		archivingEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/archiving.png"));

		archivingEntity.name = "archiving";
		archivingEntity.setScale(new Vector2(0.2f, 0.2f));
		map.addEntity(archivingEntity, new Vector2(3, 6));
		SandboxCollisionBox.collisionBoxs.put(archivingEntity.name,
				new SandboxCollisionBox(archivingEntity.position.sub(Vector2.one().mul(50)),
						archivingEntity.position.add(Vector2.one().mul(50))));
		SandboxCollisionBox.collisionBoxs.get("archiving").isTrigger = true; // 触发盒子

		// chair1
		Entity chair1Entity = new Entity();
		chair1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair1.png"));

		chair1Entity.name = "chair1";
		chair1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair1Entity, new Vector2(4, 8));

		SandboxCollisionBox.collisionBoxs.put(chair1Entity.name, new SandboxCollisionBox(
				chair1Entity.position.sub(Vector2.one().mul(50)), chair1Entity.position.add(Vector2.one().mul(50))));

		// chair2
		Entity chair2Entity = new Entity();
		chair2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair2.png"));

		chair2Entity.name = "chair2";
		chair2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair2Entity, new Vector2(4, 5));

		SandboxCollisionBox.collisionBoxs.put(chair2Entity.name, new SandboxCollisionBox(
				chair2Entity.position.sub(Vector2.one().mul(50)), chair2Entity.position.add(Vector2.one().mul(50))));

		// chair3
		Entity chair3Entity = new Entity();
		chair3Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair3.png"));

		chair3Entity.name = "chair3";
		chair3Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair3Entity, new Vector2(4, 11));

		SandboxCollisionBox.collisionBoxs.put(chair3Entity.name, new SandboxCollisionBox(
				chair3Entity.position.sub(Vector2.one().mul(50)), chair3Entity.position.add(Vector2.one().mul(50))));

		// chair4
		Entity chair4Entity = new Entity();
		chair4Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair4.png"));

		chair4Entity.name = "chair4";
		chair4Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair4Entity, new Vector2(5, 5));

		SandboxCollisionBox.collisionBoxs.put(chair4Entity.name, new SandboxCollisionBox(
				chair4Entity.position.sub(Vector2.one().mul(50)), chair4Entity.position.add(Vector2.one().mul(50))));

		// desk
		Entity deskEntity = new Entity();
		deskEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/desk.png"));

		deskEntity.name = "desk";
		deskEntity.setScale(new Vector2(2f, 2f));
		map.addEntity(deskEntity, new Vector2(5, 8));

		SandboxCollisionBox.collisionBoxs.put(deskEntity.name, new SandboxCollisionBox(
				deskEntity.position.sub(Vector2.one().mul(50)), deskEntity.position.add(Vector2.one().mul(50))));

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
		playerHpBarBase.visible = true;
		playerHpBarBase.setScale(new Vector2(300, 20));
		playerHpBarBase.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBarBase);

		// 玩家血条
		UIEntity playerHpBar = new UIEntity();
		playerHpBar.setStaticImage(Resources.loadImage(Resources.path + "images/border.png"));
		playerHpBar.name = "playerHpBar";
		playerHpBar.visible = true;
		playerHpBar.setScale(new Vector2(300, 20));
		playerHpBar.algin = UIEntity.UIAlign.LEFT;
		playerHpBar.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBar);

		// 玩家血条头像
		UIEntity playerHpProfile = new UIEntity();
		playerHpProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/主角头像.png"));
		playerHpProfile.name = "playerHpProfile";
		playerHpProfile.visible = true;
		playerHpProfile.setScale(new Vector2(0.5f, 0.5f));
		playerHpProfile.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpProfile);

		// fightingNPC血条底部
		UIEntity fightingNPCHpBarBase = new UIEntity();
		fightingNPCHpBarBase.mode = UIEntity.UIMode.WORLD;
		fightingNPCHpBarBase.setStaticImage(Resources.loadImage(Resources.path + "images/血条底.png"));
		fightingNPCHpBarBase.name = "fightingNPCHpBarBase";
		fightingNPCHpBarBase.visible = true;
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
		fightingNPCHpBar.visible = true;
		fightingNPCHpBar.setScale(new Vector2(fightingNPC.getWidth() * fightingNPC.getScale().x, 10));
		fightingNPCHpBar.algin = UIEntity.UIAlign.LEFT;
		fightingNPCHpBar.position = fightingNPCHpBarPos;

		UIManager.addUI(fightingNPCHpBar);

		// --------------加载数据----------------

		SandboxCollisionBox.notLoadNameSubStrings.add("野鬼");
		SandboxCollisionBox.notLoadNameSubStrings.add("mapBorder");
		SandboxCollisionBox.notLoadNameSubStrings.add("Attack");

		TranslateEntityHelper.notLoadNameSubStrings.add("野鬼");
		TranslateEntityHelper.notLoadNameSubStrings.add("Attack");

		SandboxCollisionBox.load(); // 从数据文件中加载碰撞盒数据

		TranslateEntityHelper.load(); // 从数据文件中加载移动帮助数据

		// ---------------------配置-----------------------
//		SandboxCollisionBox.shouldRender = true; // 渲染碰撞盒
//		TranslateEntityHelper.shouldRender = true; // 渲染移动拖拽帮助

	}

	@Override
	public void onUpdate() {
	}
}