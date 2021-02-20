package priv.dremma.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;

import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.entities.ConversationalNPC;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.entities.FightingNPC;
import priv.dremma.game.entities.Player;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.gfx.Screen;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.Text;
import priv.dremma.game.ui.UIEntity;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.TranslateEntityHelper;
import priv.dremma.game.util.Vector2;

/**
 * ��Ϸ�����࣬������Ϸ���塢��Ⱦ��
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class GameCore extends Canvas implements Runnable {

	public JFrame window; // ��Ϸ����

	public Thread thread; // ��Ϸ�߳�
	public boolean isRunning; // ��Ϸ�Ƿ���������

	public static enum GameViewAngle {
		ViewAngle2DOT5, ViewAngle2; // ��Ϸ�ӽǣ�2.5D or 2D
	}

	public static String name = "DremmaEngine"; // ����
	public static int width = 160; // ������
	public static int height = width / 12 * 9; // ����߶�
	public static int scale = 6; // ����Ŵ���
	public static final Dimension DIMENSIONS = new Dimension(width * scale, height * scale);

	public static boolean debug = true; // ��Ϸ����Ĭ��ΪDebugģʽ
	public boolean isApplet = false;
	public static GameViewAngle viewAngle; // ��Ϸ�ӽ�

	public static int frames = 0; // ��Ϸ֡��

	private BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	public KeyInputHandler keyInputHandler;
	public MouseInputHandler mouseInputHandler;
	public WindowInputHandler windowInputHandler;

	private Player player;
	private TileMap map;
	public static Screen screen;

	public static boolean willSave = true;

	public void onStart() {
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5; // ����2D��Ϸ�ӽ�
		player = new Player(this.keyInputHandler, 60f);

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);

		player.setScale(new Vector2(2f, 2f));

		// ��������
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "runSound", Resources.path + "music/run.wav");
		Resources.load(Resources.ResourceType.Music, "attackSound", Resources.path + "music/attack.wav");
		Resources.load(Resources.ResourceType.Music, "ghostFloatSound", Resources.path + "music/��Ư����.wav");
		
		Resources.load(Resources.ResourceType.Music, "ghostDieSound", Resources.path + "music/������.wav");
		Resources.load(Resources.ResourceType.Music, "playerDieSound", Resources.path + "music/��������.wav");
		Resources.load(Resources.ResourceType.Music, "playerWinSound", Resources.path + "music/����ʤ��.wav");
		Resources.load(Resources.ResourceType.Music, "playerClapSound", Resources.path + "music/clap.wav");

		Resources.load(Resources.ResourceType.Music, "ghostWoundedSound", Resources.path + "music/������.wav");
		AudioManager.getInstance().setVolumn("ghostWoundedSound", 100);
		Resources.load(Resources.ResourceType.Music, "playerWoundedSound", Resources.path + "music/��������.wav");
		AudioManager.getInstance().setVolumn("playerWoundedSound", 100);

		// ���ļ��м��ص�ͼ
		map = TileMap.loadTileMap(Resources.path + "maps/map2.txt");
		map.setPlayer(player);

		// --------------�����е�����----------------

		// tree1
		Entity tree1Entity = new Entity();
		tree1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree1.png"));

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
		Entity tree2Entity = new Entity();
		tree2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/tree2.png"));

		tree2Entity.name = "tree2";
		tree2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(tree2Entity, new Vector2(4, 8));

		// archiving
		Entity archivingEntity = new Entity();
		archivingEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/archiving.png"));

		archivingEntity.name = "archiving";
		archivingEntity.setScale(new Vector2(0.2f, 0.2f));
		map.addEntity(archivingEntity, new Vector2(3, 6));
		CollisionBox.collisionBoxs.get("archiving").isTrigger = true; // ��������

		// chair1
		Entity chair1Entity = new Entity();
		chair1Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair1.png"));

		chair1Entity.name = "chair1";
		chair1Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair1Entity, new Vector2(4, 8));

		// chair2
		Entity chair2Entity = new Entity();
		chair2Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair2.png"));

		chair2Entity.name = "chair2";
		chair2Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair2Entity, new Vector2(4, 5));

		// chair3
		Entity chair3Entity = new Entity();
		chair3Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair3.png"));

		chair3Entity.name = "chair3";
		chair3Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair3Entity, new Vector2(4, 11));

		// chair4
		Entity chair4Entity = new Entity();
		chair4Entity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/chair4.png"));

		chair4Entity.name = "chair4";
		chair4Entity.setScale(new Vector2(2f, 2f));
		map.addEntity(chair4Entity, new Vector2(5, 5));

		// desk
		Entity deskEntity = new Entity();
		deskEntity.setStaticImage(Resources.loadImage(Resources.path + "images/entities/desk.png"));

		deskEntity.name = "desk";
		deskEntity.setScale(new Vector2(2f, 2f));
		map.addEntity(deskEntity, new Vector2(5, 8));

		// --------------�����е�NPC----------------

		// �ϼ����̣��Ի�NPC��
		ConversationalNPC talkNPC = new ConversationalNPC(this.keyInputHandler, 0, "�ϼ�����");
		talkNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(talkNPC, new Vector2(1043, 275));

		// Ұ����NPC��
		FightingNPC fightingNPC = new FightingNPC(30);
		fightingNPC.name = "Ұ��";
		fightingNPC.setScale(new Vector2(2f, 2f));
		map.addNPC(fightingNPC, new Vector2(800, 600));

		// --------------�����еı߽�----------------

		float borderThickness = 100f;
		// Ϊ��ͼ�߽������ײ�� Up
		Entity mapBorderUp = new Entity();
		mapBorderUp.name = "mapBorderUp";
		mapBorderUp.visible = false;
		TileMap.addEntity("mapBorderUp", mapBorderUp);
		CollisionBox.collisionBoxs.put("mapBorderUp",
				new CollisionBox(new Vector2(0, -2 - borderThickness), new Vector2(map.worldEndTileCenter.x, -1)));

		// Ϊ��ͼ�߽������ײ�� Down
		Entity mapBorderDown = new Entity();
		mapBorderDown.name = "mapBorderDown";
		mapBorderDown.visible = false;
		TileMap.addEntity("mapBorderDown", mapBorderDown);
		CollisionBox.collisionBoxs.put("mapBorderDown",
				new CollisionBox(new Vector2(0, map.worldEndTileCenter.y + 1 - 23),
						new Vector2(map.worldEndTileCenter.x, map.worldEndTileCenter.y + 2 - 23 + borderThickness)));

		// Ϊ��ͼ�߽������ײ�� Left
		Entity mapBorderLeft = new Entity();
		mapBorderLeft.name = "mapBorderLeft";
		mapBorderLeft.visible = false;
		TileMap.addEntity("mapBorderLeft", mapBorderLeft);
		CollisionBox.collisionBoxs.put("mapBorderLeft",
				new CollisionBox(new Vector2(-2 - borderThickness, 0), new Vector2(-1, map.worldEndTileCenter.y)));

		// Ϊ��ͼ�߽������ײ�� Right
		Entity mapBorderRight = new Entity();
		mapBorderRight.name = "mapBorderRight";
		mapBorderRight.visible = false;
		TileMap.addEntity("mapBorderRight", mapBorderRight);
		CollisionBox.collisionBoxs.put("mapBorderRight", new CollisionBox(new Vector2(map.worldEndTileCenter.x + 1, 0),
				new Vector2(map.worldEndTileCenter.x + 2 + borderThickness, map.worldEndTileCenter.y)));

		// --------------UI----------------
		// �Ի���
		UIEntity talkBox = new UIEntity();
		talkBox.setStaticImage(Resources.loadImage(Resources.path + "images/�Ի���.png"));
		talkBox.name = "talkBox";
		talkBox.visible = false;
		talkBox.setScale(new Vector2(2f, 1.5f));
		talkBox.position = new Vector2(GameCore.screen.width / 2f,
				GameCore.screen.height - talkBox.getHeight() * talkBox.getScale().y / 2 - 25);

		UIManager.addUI(talkBox);

		// �ϼ�����ͷ��
		UIEntity talkNPCProfile = new UIEntity();
		talkNPCProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/�ϼ�����ͷ��.png"));
		talkNPCProfile.name = "talkNPCProfile";
		talkNPCProfile.visible = false;
		talkNPCProfile.position = new Vector2(107, 325);

		UIManager.addUI(talkNPCProfile);

		// ����ͷ��
		UIEntity playerProfile = new UIEntity();
		playerProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/����ͷ��.png"));
		playerProfile.name = "playerProfile";
		playerProfile.visible = false;
		playerProfile.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerProfile);

		// ����
		Text text = new Text("��ӭ��������Ϸ���磡��");
		text.name = "firstText";
		text.visible = false;
		text.position = new Vector2(GameCore.screen.width / 2, GameCore.screen.height / 2);

		UIManager.addUI(text);

		// ���Ѫ���ײ�
		UIEntity playerHpBarBase = new UIEntity();
		playerHpBarBase.setStaticImage(Resources.loadImage(Resources.path + "images/Ѫ����.png"));
		playerHpBarBase.name = "playerHpBarBase";
		playerHpBarBase.visible = true;
		playerHpBarBase.setScale(new Vector2(300, 20));
		playerHpBarBase.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBarBase);

		// ���Ѫ��
		UIEntity playerHpBar = new UIEntity();
		playerHpBar.setStaticImage(Resources.loadImage(Resources.path + "images/border.png"));
		playerHpBar.name = "playerHpBar";
		playerHpBar.visible = true;
		playerHpBar.setScale(new Vector2(300, 20));
		playerHpBar.algin = UIEntity.UIAlign.LEFT;
		playerHpBar.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpBar);

		// ���Ѫ��ͷ��
		UIEntity playerHpProfile = new UIEntity();
		playerHpProfile.setStaticImage(Resources.loadImage(Resources.path + "images/entities/����ͷ��.png"));
		playerHpProfile.name = "playerHpProfile";
		playerHpProfile.visible = true;
		playerHpProfile.setScale(new Vector2(0.5f, 0.5f));
		playerHpProfile.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.width / 2f);

		UIManager.addUI(playerHpProfile);
		// --------------��������----------------

		CollisionBox.load(); // �������ļ��м�����ײ������

		TranslateEntityHelper.load(); // �������ļ��м����ƶ���������

		// ---------------------����-----------------------
		CollisionBox.shouldRender = false; // ����Ⱦ��ײ��
		TranslateEntityHelper.shouldRender = false; // ����Ⱦ�ƶ���ק����
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
	 * ����Ϸѭ����ʼǰ���г�ʼ������
	 */
	public void init() {
		keyInputHandler = new KeyInputHandler(this);
		mouseInputHandler = new MouseInputHandler(this);

		screen = new Screen(GameCore.width * GameCore.scale, GameCore.height * GameCore.scale);

		onStart();
	}

	public synchronized void start() {
		// ������Ϸ��Դ
		isRunning = true;
		thread = new Thread(this, name + "_main");
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;

		try {
			thread.join(); // �ȴ��߳�thread���н���
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			init();

			while (isRunning) { // ��Ϸѭ��

				Time.update();

				// Time.shouldRender = true; //���shouldRender����60֡���ҵ�����
				if (Time.shouldRender) {
					// ��Ϸ�����߸���
					onUpdate();

					// ��ײ
					collisionBoxAjustUpdate();
					CollisionBox.collisionDetection();

					// �ƶ�����
					translateEntityAjustUpdate();
					this.mouseInputHandler.update();

					frames++;
					render();

					map.update();
					animationLoop();

				}
			}
		} catch (Exception e) {
			Debug.log(Debug.DebugLevel.SERVERE, "������!!!");
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
		// ��ȾbufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

		// ���Ƶ�ͼ
		map.draw(g);

		// UI
		UIManager.draw(g);

		// ������ײ��
		Iterator<Entry<String, CollisionBox>> collisionBoxsIterator = CollisionBox.getCollisionBoxsIterator();
		while (collisionBoxsIterator.hasNext()) {
			HashMap.Entry<String, CollisionBox> entry = (HashMap.Entry<String, CollisionBox>) collisionBoxsIterator
					.next();
			CollisionBox collisionBox = entry.getValue();
			collisionBox.draw(g);
		}

		// �����ƶ�����
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
	 * ��Ⱦ��Ϸ
	 */
	public void render() {
		if (!this.getBS().contentsLost()) {
			this.getBS().show();
		}
	}

	/**
	 * ��ȡ2D����
	 * 
	 * @return
	 */
	public Graphics2D getGraphics2D() {
		return (Graphics2D) this.getBS().getDrawGraphics();
	}

	/**
	 * ��ȡBuffer Strategy
	 * 
	 * @return
	 */
	private BufferStrategy getBS() {
		BufferStrategy bufferStrategy = this.getBufferStrategy(); // ȡ�ñ�Canvas��buffer strategy
		if (bufferStrategy == null) {
			this.createBufferStrategy(2); // ͨ��˫���桢��ҳ���������������˸���ѿ�������
		}
		return this.getBufferStrategy();
	}

	/**
	 * ������Ϸ����
	 */
	public void setName(String name) {
		if (isApplet) { // appletС������Ҫ��������
			return;
		}
		GameCore.name = name;
		this.window.setTitle(name);
	}

	/**
	 * ���ڵ�������ʵ���λ��
	 */
	public synchronized void translateEntityAjustUpdate() {
		if (!TranslateEntityHelper.shouldRender) {
			return;
		}

		if (TranslateEntityHelper.hasLock) {
			if (TranslateEntityHelper.lockedEntity.entity.visible == true &&
					TranslateEntityHelper.lockedEntity.shouldTransScreenPos) {

				// ����קx��ʱ
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaX = curPos.x - lastPos.x;

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position.x += deltaX;
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name).trans(new Vector2(deltaX, 0));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis)) {
					TranslateEntityHelper.lockedEntity.choosenX = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenX = false;
				}

				// ����קy��ʱ
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaY = curPos.y - lastPos.y;

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position.y += deltaY;
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name).trans(new Vector2(0, deltaY));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis)) {
					TranslateEntityHelper.lockedEntity.choosenY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenY = false;
				}

				// ����ק��ɫ����ʱ���������������ƶ�
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position = TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position
							.add(curPos.sub(lastPos));
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name).trans(curPos.sub(lastPos));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					TranslateEntityHelper.lockedEntity.choosenXY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenXY = false;
				}
			} else {
				// ����קx��ʱ
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaX = curPos.x - lastPos.x;

					UIManager.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position.x += deltaX;
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis)) {
					TranslateEntityHelper.lockedEntity.choosenX = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenX = false;
				}

				// ����קy��ʱ
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaY = curPos.y - lastPos.y;

					UIManager.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position.y += deltaY;

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis)) {
					TranslateEntityHelper.lockedEntity.choosenY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenY = false;
				}

				// ����ק��ɫ����ʱ���������������ƶ�
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					UIManager.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position = UIManager.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position
							.add(curPos.sub(lastPos));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					TranslateEntityHelper.lockedEntity.choosenXY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenXY = false;
				}
			}

			if (TranslateEntityHelper.lockedEntity.choosenX || TranslateEntityHelper.lockedEntity.choosenY || TranslateEntityHelper.lockedEntity.choosenXY) {
				// �����ƶ�������ѡ��ʱ��Ӧ������ֻ�ܽ�����ƶ����������¼�����
				TranslateEntityHelper.hasLock = true;
			} else {
				TranslateEntityHelper.hasLock = false;
			}

		} else {
			// ���������
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

					// ����קx��ʱ
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

					// ����קy��ʱ
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

					// ����ק��ɫ����ʱ���������������ƶ�
					if (this.mouseInputHandler.mouse.isPressed()
							&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xyAxis)) {
						Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
						Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

						TileMap.entities.get(name).position = TileMap.entities.get(name).position
								.add(curPos.sub(lastPos));
						CollisionBox.collisionBoxs.get(name).trans(curPos.sub(lastPos));

					}

					if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
							&& this.mouseInputHandler.worldCurPosIsInRect(translateEntity.xyAxis)) {
						translateEntity.choosenXY = true;
					} else {
						translateEntity.choosenXY = false;
					}
				} else {
					// ����קx��ʱ
					if (this.mouseInputHandler.mouse.isPressed()
							&& (this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xAxis))) {
						Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
						Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
						float deltaX = curPos.x - lastPos.x;

						UIManager.getUIEntity(name).position.x += deltaX;
					}

					if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
							&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xAxis)) {
						translateEntity.choosenX = true;
					} else {
						translateEntity.choosenX = false;
					}

					// ����קy��ʱ
					if (this.mouseInputHandler.mouse.isPressed()
							&& (this.mouseInputHandler.screenCurPosIsInRect(translateEntity.yAxis))) {
						Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
						Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
						float deltaY = curPos.y - lastPos.y;

						UIManager.getUIEntity(name).position.y += deltaY;

					}

					if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
							&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.yAxis)) {
						translateEntity.choosenY = true;
					} else {
						translateEntity.choosenY = false;
					}

					// ����ק��ɫ����ʱ���������������ƶ�
					if (this.mouseInputHandler.mouse.isPressed()
							&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xyAxis)) {
						Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
						Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

						UIManager.getUIEntity(name).position = UIManager.getUIEntity(name).position
								.add(curPos.sub(lastPos));
					}

					if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
							&& this.mouseInputHandler.screenCurPosIsInRect(translateEntity.xyAxis)) {
						translateEntity.choosenXY = true;
					} else {
						translateEntity.choosenXY = false;
					}
				}

				if (translateEntity.choosenX || translateEntity.choosenY || translateEntity.choosenXY) {
					// �����ƶ�������ѡ��ʱ��Ӧ������ֻ�ܽ�����ƶ����������¼�����
					TranslateEntityHelper.hasLock = true;
					TranslateEntityHelper.lockedEntity = translateEntity;
				}

			}
		}
	}

	/**
	 * ���ڵ�����ײ��
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

			// �������ϵ��λ��
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
				Debug.log(Debug.DebugLevel.INFO, name + ":�����������ϵ�����Ϊ��" + collisionBox.leftUpPoint);
				CollisionBox.pressedTimes = this.mouseInputHandler.mouse.getPressedTimes();
			}

			if (collisionBox.isChoosenLeftUp) {
				if (!collisionBox.leftUpPoint
						.isEqual(GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos()))) {
					collisionBox.leftUpPoint = GUtils.viewPortToWorldPixel(this.mouseInputHandler.getCurPos());
				}
			}

			// �������µ��λ��
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
				Debug.log(Debug.DebugLevel.INFO, name + ":�����������µ�����Ϊ��" + collisionBox.rightDownPoint);
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

}
