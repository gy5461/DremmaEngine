package priv.dremma.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import javax.swing.JFrame;

import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.entities.ConversationalNPC;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.FightingNPC;
import priv.dremma.game.entities.Player;
import priv.dremma.game.entities.UIEntity;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.gfx.Screen;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;

/**
 * 游戏主体类，包含游戏窗体、渲染等
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class GameCore extends Canvas implements Runnable {

	public JFrame window; // 游戏窗体

	public Thread thread; // 游戏线程
	public boolean isRunning; // 游戏是否正在运行

	public static enum GameViewAngle {
		ViewAngle2DOT5, ViewAngle2; // 游戏视角，2.5D or 2D
	}

	public static String name = "DremmaEngine"; // 名称
	public static int width = 160; // 窗体宽度
	public static int height = width / 12 * 9; // 窗体高度
	public static int scale = 6; // 窗体放大倍数
	public static final Dimension DIMENSIONS = new Dimension(width * scale, height * scale);

	public static boolean debug = true; // 游戏引擎默认为Debug模式
	public boolean isApplet = false;
	public static GameViewAngle viewAngle; // 游戏视角

	public static int frames = 0; // 游戏帧数

	private BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	public KeyInputHandler keyInputHandler;
	public MouseInputHandler mouseInputHandler;
	public WindowInputHandler windowInputHandler;

	private Player player;
	private TileMap map;
	public static Screen screen;

	public static boolean willSave = true;

	public static HashMap<String, UIEntity> uiEntities;
	private Queue<UIEntity> renderUIEntities;

	public void onStart() {
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5; // 设置2D游戏视角
		player = new Player(this.keyInputHandler, 60f);

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);

		player.setScale(new Vector2(2f, 2f));

		// 加载音乐
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "runSound", Resources.path + "music/run.wav");
		Resources.load(Resources.ResourceType.Music, "attackSound", Resources.path + "music/attack.wav");
		Resources.load(Resources.ResourceType.Music, "ghostFloatSound", Resources.path + "music/鬼漂浮声.wav");

		Resources.load(Resources.ResourceType.Music, "ghostWoundedSound", Resources.path + "music/鬼受伤.wav");
		AudioManager.getInstance().setVolumn("ghostWoundedSound", 100);
		Resources.load(Resources.ResourceType.Music, "playerWoundedSound", Resources.path + "music/主角受伤.wav");
		AudioManager.getInstance().setVolumn("playerWoundedSound", 100);

		// 从文件中加载地图
		map = TileMap.loadTileMap(Resources.path + "maps/map1.txt");
		map.setPlayer(player);

		// --------------场景中的物体----------------

		// tree1
		Animator tree1Animator = new Animator();
		Animation tree1Animation = new Animation();
		tree1Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree1.png"));
		tree1Animator.addAnimation("static", tree1Animation);
		tree1Animator.setState("static", false);
		Entity tree1Entity = new Entity(tree1Animator);

		tree1Entity.setScale(new Vector2(3f, 3f));
		tree1Entity.name = "tree1_1";
		map.addEntity(tree1Entity, new Vector2(2, 5));

		tree1Entity.name = "tree1_2";
		tree1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree1Entity, new Vector2(4, 5));

		tree1Entity.name = "tree1_3";
		tree1Entity.setScale(new Vector2(1f, 1f));
		map.addEntity(tree1Entity, new Vector2(3, 11));

		// tree2
		Animator tree2Animator = new Animator();
		Animation tree2Animation = new Animation();
		tree2Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree2.png"));
		tree2Animator.addAnimation("static", tree2Animation);
		tree2Animator.setState("static", false);
		Entity tree2Entity = new Entity(tree2Animator);

		tree2Entity.name = "tree2";
		tree2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree2Entity, new Vector2(4, 8));
		Debug.log(Debug.DebugLevel.INFO, "" + tree2Entity.getBottom());

		// archiving
		Animator archivingAnimator = new Animator();
		Animation archivingAnimation = new Animation();
		archivingAnimation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/archiving.png"));
		archivingAnimator.addAnimation("static", archivingAnimation);
		archivingAnimator.setState("static", false);
		Entity archivingEntity = new Entity(archivingAnimator);

		archivingEntity.name = "archiving";
		archivingEntity.setScale(new Vector2(0.2f, 0.2f));
		map.addEntity(archivingEntity, new Vector2(3, 6));
		CollisionBox.collisionBoxs.get("archiving").isTrigger = true; // 触发盒子

		// chair1
		Animator chair1Animator = new Animator();
		Animation chair1Animation = new Animation();
		chair1Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair1.png"));
		chair1Animator.addAnimation("static", chair1Animation);
		chair1Animator.setState("static", false);
		Entity chair1Entity = new Entity(chair1Animator);

		chair1Entity.name = "chair1";
		chair1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair1Entity, new Vector2(4, 8));

		// chair2
		Animator chair2Animator = new Animator();
		Animation chair2Animation = new Animation();
		chair2Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair2.png"));
		chair2Animator.addAnimation("static", chair2Animation);
		chair2Animator.setState("static", false);
		Entity chair2Entity = new Entity(chair2Animator);

		chair2Entity.name = "chair2";
		chair2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair2Entity, new Vector2(4, 5));

		// chair3
		Animator chair3Animator = new Animator();
		Animation chair3Animation = new Animation();
		chair3Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair3.png"));
		chair3Animator.addAnimation("static", chair3Animation);
		chair3Animator.setState("static", false);
		Entity chair3Entity = new Entity(chair3Animator);

		chair3Entity.name = "chair3";
		chair3Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair3Entity, new Vector2(4, 11));

		// chair4
		Animator chair4Animator = new Animator();
		Animation chair4Animation = new Animation();
		chair4Animation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair4.png"));
		chair4Animator.addAnimation("static", chair4Animation);
		chair4Animator.setState("static", false);
		Entity chair4Entity = new Entity(chair4Animator);

		chair4Entity.name = "chair4";
		chair4Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair4Entity, new Vector2(5, 5));

		// chair4
		Animator deskAnimator = new Animator();
		Animation deskAnimation = new Animation();
		deskAnimation.setStaticImage(Resources.loadImage(Resources.path + "images/entities/desk.png"));
		deskAnimator.addAnimation("static", deskAnimation);
		deskAnimator.setState("static", false);
		Entity deskEntity = new Entity(deskAnimator);

		deskEntity.name = "desk";
		deskEntity.setScale(new Vector2(2f, 2f));
		map.addEntity(deskEntity, new Vector2(5, 8));

		// --------------场景中的NPC----------------

		// 南极仙翁（对话NPC）
		ConversationalNPC talkNPC = new ConversationalNPC(this.keyInputHandler, 0);
		talkNPC.name = "南极仙翁";
		talkNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(talkNPC, new Vector2(1043, 275));

		// 野鬼（打斗NPC）
		FightingNPC fightingNPC = new FightingNPC(30);
		fightingNPC.name = "野鬼";
		fightingNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(fightingNPC, new Vector2(800, 600));

		// --------------场景中的边界----------------

		float borderThickness = 100f;
		// 为地图边界添加碰撞盒 Up
		Entity mapBorderUp = new Entity();
		mapBorderUp.name = "mapBorderUp";
		mapBorderUp.visible = false;
		TileMap.addEntity("mapBorderUp", mapBorderUp);
		CollisionBox.collisionBoxs.put("mapBorderUp",
				new CollisionBox(new Vector2(0, -2 - borderThickness), new Vector2(map.worldEndTileCenter.x, -1)));

		// 为地图边界添加碰撞盒 Down
		Entity mapBorderDown = new Entity();
		mapBorderDown.name = "mapBorderDown";
		mapBorderDown.visible = false;
		TileMap.addEntity("mapBorderDown", mapBorderDown);
		CollisionBox.collisionBoxs.put("mapBorderDown",
				new CollisionBox(new Vector2(0, map.worldEndTileCenter.y + 1 - 23),
						new Vector2(map.worldEndTileCenter.x, map.worldEndTileCenter.y + 2 - 23 + borderThickness)));

		// 为地图边界添加碰撞盒 Left
		Entity mapBorderLeft = new Entity();
		mapBorderLeft.name = "mapBorderLeft";
		mapBorderLeft.visible = false;
		TileMap.addEntity("mapBorderLeft", mapBorderLeft);
		CollisionBox.collisionBoxs.put("mapBorderLeft",
				new CollisionBox(new Vector2(-2 - borderThickness, 0), new Vector2(-1, map.worldEndTileCenter.y)));

		// 为地图边界添加碰撞盒 Right
		Entity mapBorderRight = new Entity();
		mapBorderRight.name = "mapBorderRight";
		mapBorderRight.visible = false;
		TileMap.addEntity("mapBorderRight", mapBorderRight);
		CollisionBox.collisionBoxs.put("mapBorderRight", new CollisionBox(new Vector2(map.worldEndTileCenter.x + 1, 0),
				new Vector2(map.worldEndTileCenter.x + 2 + borderThickness, map.worldEndTileCenter.y)));

		// --------------UI----------------
		Animator talkBoxAnimator = new Animator();
		Animation talkBoxAnimation = new Animation();
		talkBoxAnimation.setStaticImage(Resources.loadImage(Resources.path + "images/对话框.png"));
		talkBoxAnimator.addAnimation("static", talkBoxAnimation);
		talkBoxAnimator.setState("static", false);
		UIEntity talkBox = new UIEntity(talkBoxAnimator);
		talkBox.name = "talkBox";
		talkBox.visible = false;
		talkBox.setScale(new Vector2(2f, 1.5f));
		talkBox.position = new Vector2(GameCore.screen.width / 2f,
				GameCore.screen.height - talkBox.getHeight() * talkBox.getScale().y / 2 - 25);

		GameCore.addUI(talkBox);

		// --------------加载数据----------------

		CollisionBox.load(); // 从数据文件中加载碰撞盒数据

		TranslateEntityHelper.load(); // 从数据文件中加载移动帮助数据

		// CollisionBox.shouldRender = false; // 不渲染碰撞盒
		// TranslateEntityHelper.shouldRender = false; // 不渲染移动拖拽帮助
	}

	public void onUpdate() {
		// Debug.log(Debug.DebugLevel.INFO, ""+TileMap.player.position);
	}

	public void onDestroy() {
		if (GameCore.willSave) {
			CollisionBox.save();
			TranslateEntityHelper.save();
		}
	}

	/**
	 * 在游戏循环开始前进行初始化工作
	 */
	public void init() {
		keyInputHandler = new KeyInputHandler(this);
		mouseInputHandler = new MouseInputHandler(this);

		screen = new Screen(GameCore.width * GameCore.scale, GameCore.height * GameCore.scale);
		this.renderUIEntities = new LinkedList<UIEntity>();
		GameCore.uiEntities = new HashMap<String, UIEntity>();

		onStart();
	}

	public synchronized void start() {
		// 加载游戏资源
		isRunning = true;
		thread = new Thread(this, name + "_main");
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;

		try {
			thread.join(); // 等待线程thread运行结束
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			init();

			while (isRunning) { // 游戏循环

				Time.update();

				// Time.shouldRender = true; //解除shouldRender对于60帧左右的限制
				if (Time.shouldRender) {
					// 游戏开发者更新
					onUpdate();

					// 碰撞
					collisionBoxAjustUpdate();
					CollisionBox.collisionDetection();

					// 移动帮助
					translateEntityAjustUpdate();
					this.mouseInputHandler.update();

					frames++;
					render();

					map.update();
					animationLoop();

				}
			}
		} catch (Exception e) {
			Debug.log(Debug.DebugLevel.SERVERE, "出错啦!!!");
			e.printStackTrace();
			willSave = false;
		}
	}

	public void animationLoop() {
		Graphics2D g = this.getGraphics2D();
		draw(g);
		g.dispose();
	}

	public void draw(Graphics2D g) {
		// 渲染bufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

		// 绘制地图
		map.draw(g);

		// 绘制UI
		Iterator<Entry<String, UIEntity>> uiEntitiesIterator = GameCore.getUIEntitiesIterator();
		while (uiEntitiesIterator.hasNext()) {
			HashMap.Entry<String, UIEntity> entry = (HashMap.Entry<String, UIEntity>) uiEntitiesIterator.next();
			if (entry.getValue().visible == false) {
				continue;
			}
			renderUIEntities.add(entry.getValue());
		}

		// 渲染UI队列
		while (!renderUIEntities.isEmpty()) {
			renderUIEntities.peek().draw(g);
			renderUIEntities.poll();
		}

		// 绘制碰撞盒
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			CollisionBox collisionBox = entry.getValue();
			collisionBox.draw(g);
		}

		// 绘制移动帮助
		Iterator<Entry<String, TranslateEntityHelper>> translateEntitiesIterator = TranslateEntityHelper
				.getTranslateEntitiesHelperIterator();
		while (translateEntitiesIterator.hasNext()) {
			HashMap.Entry<String, TranslateEntityHelper> entry = (HashMap.Entry<String, TranslateEntityHelper>) translateEntitiesIterator
					.next();
			TranslateEntityHelper translateEntity = entry.getValue();
			translateEntity.draw(g);
//			if (translateEntity.entity.visible) {
//				translateEntity.xAxis.draw(g);
//				translateEntity.yAxis.draw(g);
//			}
		}
	}

	/**
	 * 渲染游戏
	 */
	public void render() {
		if (!this.getBS().contentsLost()) {
			this.getBS().show();
		}
	}

	/**
	 * 获取2D画笔
	 * 
	 * @return
	 */
	public Graphics2D getGraphics2D() {
		return (Graphics2D) this.getBS().getDrawGraphics();
	}

	/**
	 * 获取Buffer Strategy
	 * 
	 * @return
	 */
	private BufferStrategy getBS() {
		BufferStrategy bufferStrategy = this.getBufferStrategy(); // 取得本Canvas的buffer strategy
		if (bufferStrategy == null) {
			this.createBufferStrategy(2); // 通过双缓存、翻页技术，解决白屏闪烁、裂开等问题
		}
		return this.getBufferStrategy();
	}

	/**
	 * 设置游戏名称
	 */
	public void setName(String name) {
		if (isApplet) { // applet小程序不需要设置名称
			return;
		}
		GameCore.name = name;
		this.window.setTitle(name);
	}

	/**
	 * 用于调整其他实体的位置
	 */
	public synchronized void translateEntityAjustUpdate() {
		if (!TranslateEntityHelper.shouldRender) {
			return;
		}

		Iterator<Entry<String, TranslateEntityHelper>> translateEntitiesIterator = TranslateEntityHelper
				.getTranslateEntitiesHelperIterator();
		while (translateEntitiesIterator.hasNext()) {
			HashMap.Entry<String, TranslateEntityHelper> entry = (HashMap.Entry<String, TranslateEntityHelper>) translateEntitiesIterator
					.next();
			String name = entry.getKey();
			TranslateEntityHelper translateEntity = entry.getValue();

			if (translateEntity.entity.visible == false) {
				continue;
			}

			if (translateEntity.shouldTransScreenPos) {

				// 当拖拽x轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaX = curPos.x - lastPos.x;

					TileMap.entities.get(name).position.x += deltaX;
					CollisionBox.collisionBoxs.get(name).trans(new Vector2(deltaX, 0));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xAxis)) {
					translateEntity.choosenX = true;
				} else {
					translateEntity.choosenX = false;
				}

				// 当拖拽y轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(translateEntity.yAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaY = curPos.y - lastPos.y;

					TileMap.entities.get(name).position.y += deltaY;
					CollisionBox.collisionBoxs.get(name).trans(new Vector2(0, deltaY));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.yAxis)) {
					translateEntity.choosenY = true;
				} else {
					translateEntity.choosenY = false;
				}

				// 当拖拽黄色部分时，在两个方向上移动
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					TileMap.entities.get(name).position = TileMap.entities.get(name).position.add(curPos.sub(lastPos));
					CollisionBox.collisionBoxs.get(name).trans(curPos.sub(lastPos));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xyAxis)) {
					translateEntity.choosenXY = true;
				} else {
					translateEntity.choosenXY = false;
				}
			} else {
				// 当拖拽x轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaX = curPos.x - lastPos.x;

					GameCore.getUIEntity(name).position.x += deltaX;
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xAxis)) {
					translateEntity.choosenX = true;
				} else {
					translateEntity.choosenX = false;
				}

				// 当拖拽y轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.screenCurPosIsInRect(translateEntity.yAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaY = curPos.y - lastPos.y;

					GameCore.getUIEntity(name).position.y += deltaY;

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.yAxis)) {
					translateEntity.choosenY = true;
				} else {
					translateEntity.choosenY = false;
				}

				// 当拖拽黄色部分时，在两个方向上移动
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					GameCore.getUIEntity(name).position = GameCore.getUIEntity(name).position.add(curPos.sub(lastPos));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xyAxis)) {
					translateEntity.choosenXY = true;
				} else {
					translateEntity.choosenXY = false;
				}
			}

		}
	}

	/**
	 * 用于调整碰撞盒
	 */
	public synchronized void collisionBoxAjustUpdate() {

		if (!CollisionBox.shouldRender) {
			return;
		}

		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			String name = entry.getKey();
			CollisionBox collisionBox = entry.getValue();

			if (TileMap.entities.containsKey(name) && TileMap.getEntity(name).detectCollision == false) {
				continue;
			}

			// 调节左上点的位置
			if (this.mouseInputHandler.mouse.isPressed()
					&& this.mouseInputHandler.mouse.getPressedTimes() > CollisionBox.pressedTimes
					&& collisionBox.isChoosenLeftUp == false
					&& this.mouseInputHandler.mouse.isInRect(collisionBox.leftUpPoint.sub(Vector2.one().mul(5)),
							collisionBox.leftUpPoint.add(Vector2.one().mul(5)))) {
				collisionBox.isChoosenLeftUp = true;
				CollisionBox.pressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
			}

			if (this.mouseInputHandler.mouse.isPressed()
					&& this.mouseInputHandler.mouse.getPressedTimes() > CollisionBox.pressedTimes
					&& collisionBox.isChoosenLeftUp == true) {
				collisionBox.isChoosenLeftUp = false;
				Debug.log(Debug.DebugLevel.INFO, name + ":调整过后，左上点坐标为：" + collisionBox.leftUpPoint);
				CollisionBox.pressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
			}

			if (collisionBox.isChoosenLeftUp) {
				if (!collisionBox.leftUpPoint
						.isEqual(GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos()))) {
					collisionBox.leftUpPoint = GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos());
				}
			}

			// 调节右下点的位置
			if (this.mouseInputHandler.mouse.isPressed()
					&& this.mouseInputHandler.mouse.getPressedTimes() > CollisionBox.pressedTimes
					&& collisionBox.isChoosenRightDown == false
					&& this.mouseInputHandler.mouse.isInRect(collisionBox.rightDownPoint.sub(Vector2.one().mul(5)),
							collisionBox.rightDownPoint.add(Vector2.one().mul(5)))) {
				collisionBox.isChoosenRightDown = true;
				CollisionBox.pressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
			}

			if (this.mouseInputHandler.mouse.isPressed()
					&& this.mouseInputHandler.mouse.getPressedTimes() > CollisionBox.pressedTimes
					&& collisionBox.isChoosenRightDown == true) {
				collisionBox.isChoosenRightDown = false;
				Debug.log(Debug.DebugLevel.INFO, name + ":调整过后，右下点坐标为：" + collisionBox.rightDownPoint);
				CollisionBox.pressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
			}

			if (collisionBox.isChoosenRightDown) {
				if (!collisionBox.rightDownPoint
						.isEqual(GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos()))) {
					collisionBox.rightDownPoint = GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos());
				}
			}
		}
	}

	/**
	 * 向游戏中添加UI
	 * 
	 * @param UIEntity
	 */
	public static void addUI(UIEntity UIEntity) {
		if (!GameCore.uiEntities.containsKey(UIEntity.name)) {
			GameCore.uiEntities.put(UIEntity.name, UIEntity);
			if (!TranslateEntityHelper.translateEntities.containsKey(UIEntity.name)) {
				TranslateEntityHelper translateEntityHelper = new TranslateEntityHelper(UIEntity);
				TranslateEntityHelper.translateEntities.put(UIEntity.name, translateEntityHelper);
			}
		}
	}

	public static UIEntity getUIEntity(String name) {
		if (GameCore.uiEntities.containsKey(name)) {
			return GameCore.uiEntities.get(name);
		}
		return null;
	}

	/**
	 * 获取游戏UI迭代器
	 * 
	 * @return
	 */
	public static Iterator<Entry<String, UIEntity>> getUIEntitiesIterator() {
		return GameCore.uiEntities.entrySet().iterator();
	}
}
