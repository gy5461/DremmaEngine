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

	public void onStart() {
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5; // 设置2D游戏视角
		player = new Player(this.keyInputHandler, 60f);
		player.loadAnimation();

		player.position = new Vector2(GameCore.screen.width / 2f, GameCore.screen.height / 2f);

		player.setScale(new Vector2(2f, 2f));

		// 加载音乐
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path + "music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

		Resources.load(Resources.ResourceType.Music, "walkSound", Resources.path + "music/walk.wav");

		// 从文件中加载地图
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
	 * 在游戏循环开始前进行初始化工作
	 */
	public void init() {
		keyInputHandler = new KeyInputHandler(this);
		mouseInputHandler = new MouseInputHandler(this);
		
		screen = new Screen(GameCore.width * GameCore.scale, GameCore.height * GameCore.scale);

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
		// 渲染bufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

		// 绘制地图
		map.draw(g);

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

//			translateEntity.xAxis.draw(g);
//			
//			translateEntity.yAxis.draw(g);
//			
//			translateEntity.xyAxis.draw(g);
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

			// 当拖拽x轴时
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
			
			// 当拖拽y轴时
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

			// 当拖拽黄色部分时，在两个方向上移动
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
				if (!collisionBox.leftUpPoint.isEqual(this.mouseInputHandler.getCurPos())) {
					collisionBox.leftUpPoint = this.mouseInputHandler.getCurPos();
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
				if (!collisionBox.rightDownPoint.isEqual(this.mouseInputHandler.getCurPos())) {
					collisionBox.rightDownPoint = this.mouseInputHandler.getCurPos();
				}
			}
		}
	}
}
