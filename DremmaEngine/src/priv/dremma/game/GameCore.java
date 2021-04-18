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

import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.gfx.Screen;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.ui.UIManager;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.GUtils;
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
	public static boolean isApplet = false;
	public static GameViewAngle viewAngle; // 游戏视角

	public static int frames = 0; // 游戏帧数

	private BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	public KeyInputHandler keyInputHandler;
	public MouseInputHandler mouseInputHandler;
	public WindowInputHandler windowInputHandler;

	protected TileMap map;
	public static Screen screen;

	public static boolean willSave = true;

	public void onStart() {
	}

	public void onUpdate() {
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
		this.onDestroy();
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

					frames++;
					
					// 碰撞
					collisionBoxAjustUpdate();
					CollisionBox.collisionDetection();

					if (map != null) {
						map.update();
					}
					animationLoop();
					
					// 移动帮助
					translateEntityAjustUpdate();
					this.mouseInputHandler.update();
					render();
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
		if (map != null) {
			map.draw(g);
		}

		// UI
		UIManager.draw(g);

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
			this.createBufferStrategy(3); // 通过三重缓存、翻页技术，解决白屏闪烁、裂开等问题，减少掉帧
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

		if (TranslateEntityHelper.hasLock) {
			if(TranslateEntityHelper.lockedEntity.entity.visible == false) {
				return;
			}
			if (TranslateEntityHelper.lockedEntity.shouldTransScreenPos) {

				// 当拖拽x轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaX = curPos.x - lastPos.x;

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position.x += deltaX;
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name)
							.trans(new Vector2(deltaX, 0));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xAxis)) {
					TranslateEntityHelper.lockedEntity.choosenX = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenX = false;
				}

				// 当拖拽y轴时
				if (this.mouseInputHandler.mouse.isPressed()
						&& (this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis))) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());
					float deltaY = curPos.y - lastPos.y;

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position.y += deltaY;
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name)
							.trans(new Vector2(0, deltaY));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.yAxis)) {
					TranslateEntityHelper.lockedEntity.choosenY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenY = false;
				}

				// 当拖拽黄色部分时，在两个方向上移动
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					TileMap.entities.get(TranslateEntityHelper.lockedEntity.entity.name).position = TileMap.entities
							.get(TranslateEntityHelper.lockedEntity.entity.name).position.add(curPos.sub(lastPos));
					CollisionBox.collisionBoxs.get(TranslateEntityHelper.lockedEntity.entity.name)
							.trans(curPos.sub(lastPos));

				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.worldCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					TranslateEntityHelper.lockedEntity.choosenXY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenXY = false;
				}
			} else {
				// 当拖拽x轴时
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

				// 当拖拽y轴时
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

				// 当拖拽黄色部分时，在两个方向上移动
				if (this.mouseInputHandler.mouse.isPressed()
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					Vector2 curPos = new Vector2(this.mouseInputHandler.getCurPos());
					Vector2 lastPos = new Vector2(this.mouseInputHandler.getLastPos());

					UIManager.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position = UIManager
							.getUIEntity(TranslateEntityHelper.lockedEntity.entity.name).position
									.add(curPos.sub(lastPos));
				}

				if (!this.mouseInputHandler.curPos.isEqual(Vector2.zero())
						&& this.mouseInputHandler.screenCurPosIsInRect(TranslateEntityHelper.lockedEntity.xyAxis)) {
					TranslateEntityHelper.lockedEntity.choosenXY = true;
				} else {
					TranslateEntityHelper.lockedEntity.choosenXY = false;
				}
			}

			if (TranslateEntityHelper.lockedEntity.choosenX || TranslateEntityHelper.lockedEntity.choosenY
					|| TranslateEntityHelper.lockedEntity.choosenXY) {
				// 当有移动帮助被选中时，应锁定，只能进入该移动帮助进行事件监听
				TranslateEntityHelper.hasLock = true;
			} else {
				TranslateEntityHelper.hasLock = false;
			}

		} else {
			// 无锁则遍历
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
					// 当拖拽x轴时
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

					// 当拖拽y轴时
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

					// 当拖拽黄色部分时，在两个方向上移动
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
					// 当有移动帮助被选中时，应锁定，只能进入该移动帮助进行事件监听
					TranslateEntityHelper.hasLock = true;
					TranslateEntityHelper.lockedEntity = translateEntity;
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

	public void onWindowOpened() {
	}

	public void onWindowClose() {
	}

}