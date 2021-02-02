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
import priv.dremma.game.entities.Player;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.gfx.Screen;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.Debug;
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

	public void onStart() {
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5; // ����2D��Ϸ�ӽ�
		player = new Player(this.keyInputHandler, 60f);
		player.loadAnimation();

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);

		player.setScale(new Vector2(2f, 2f));

		// ��������
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "walkSound", Resources.path + "music/walk.wav");

		// ���ļ��м��ص�ͼ
		map = TileMap.loadTileMap(Resources.path + "maps/map1.txt");
		map.setPlayer(player);

		// CollisionBox.shouldRender = false;
		// TranslateEntityHelper.shouldRender = false;
	}

	public void onUpdate() {
	}

	public void onDestroy() {
		CollisionBox.save();
		TranslateEntityHelper.save();
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

				animationLoop();
				

			}
		}
	}

	public void animationLoop() {
		player.update();

		Graphics2D g = this.getGraphics2D();
		draw(g);
		g.dispose();
	}

	public void draw(Graphics2D g) {
		// ��ȾbufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

		// ���Ƶ�ͼ
		map.draw(g);

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

//			translateEntity.xAxis.draw(g);
//			
//			translateEntity.yAxis.draw(g);
//			
//			translateEntity.xyAxis.draw(g);
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
		
		Iterator<Entry<String, TranslateEntityHelper>> translateEntitiesIterator = TranslateEntityHelper
				.getTranslateEntitiesHelperIterator();
		while (translateEntitiesIterator.hasNext()) {
			HashMap.Entry<String, TranslateEntityHelper> entry = (HashMap.Entry<String, TranslateEntityHelper>) translateEntitiesIterator
					.next();
			String name = entry.getKey();
			TranslateEntityHelper translateEntity = entry.getValue();

			// ����קx��ʱ
			if (this.mouseInputHandler.mouse.isPressed()
					&& (this.mouseInputHandler.transCurPosIsInRect(translateEntity.xAxis))) {
				Vector2 curPos = new Vector2(this.mouseInputHandler.getTransCurPos());
				Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
				float deltaX = curPos.x - lastPos.x;
				TileMap.entities.get(name).position.x += deltaX;
				CollisionBox.collisionBoxs.get(name).trans(new Vector2(deltaX, 0));
			}

			if(!this.mouseInputHandler.transCurPos.isEqual(Vector2.zero()) && this.mouseInputHandler.transCurPosIsInRect(translateEntity.xAxis)) {
				translateEntity.choosenX = true;
			} else {
				translateEntity.choosenX = false;
			}
			
			// ����קy��ʱ
			if (this.mouseInputHandler.mouse.isPressed()
					&& (this.mouseInputHandler.transCurPosIsInRect(translateEntity.yAxis))) {
				Vector2 curPos = new Vector2(this.mouseInputHandler.getTransCurPos());
				Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
				float deltaY = curPos.y - lastPos.y;
				TileMap.entities.get(name).position.y += deltaY;
				CollisionBox.collisionBoxs.get(name).trans(new Vector2(0, deltaY));
			}

			if(!this.mouseInputHandler.transCurPos.isEqual(Vector2.zero()) && this.mouseInputHandler.transCurPosIsInRect(translateEntity.yAxis)) {
				translateEntity.choosenY = true;
			} else {
				translateEntity.choosenY = false;
			}

			// ����ק��ɫ����ʱ���������������ƶ�
			if (this.mouseInputHandler.mouse.isPressed()
					&& this.mouseInputHandler.transCurPosIsInRect(translateEntity.xyAxis)) {
				Vector2 curPos = new Vector2(this.mouseInputHandler.getTransCurPos());
				Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
				TileMap.entities.get(name).position = TileMap.entities.get(name).position.add(curPos.sub(lastPos));
				CollisionBox.collisionBoxs.get(name).trans(curPos.sub(lastPos));
			}
			
			if(!this.mouseInputHandler.transCurPos.isEqual(Vector2.zero()) && this.mouseInputHandler.transCurPosIsInRect(translateEntity.xyAxis)) {
				translateEntity.choosenXY = true;
			} else {
				translateEntity.choosenXY = false;
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
				if (!collisionBox.leftUpPoint.isEqual(this.mouseInputHandler.getCurPos())) {
					collisionBox.leftUpPoint = this.mouseInputHandler.getCurPos();
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
				if (!collisionBox.rightDownPoint.isEqual(this.mouseInputHandler.getCurPos())) {
					collisionBox.rightDownPoint = this.mouseInputHandler.getCurPos();
				}
			}
		}
	}
}
