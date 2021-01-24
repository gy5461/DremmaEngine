package priv.dremma.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import priv.dremma.game.anim.Animation;
import priv.dremma.game.anim.Animator;
import priv.dremma.game.audio.AudioManager;
import priv.dremma.game.entities.Player;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.util.Debug;
import priv.dremma.game.util.Resources;
import priv.dremma.game.util.Time;
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

	public void onStart() {
		loadImages();
	}

	private Player entity;

	// 站立动画
	HashMap<Integer, Image> playerStandUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerStandLeft = new HashMap<Integer, Image>();

	Animation playerStandUpAnimation = new Animation();
	Animation playerStandDownAnimation = new Animation();
	Animation playerStandRightAnimation = new Animation();
	Animation playerStandLeftAnimation = new Animation();

	// 跑步动画
	HashMap<Integer, Image> playerRunUp = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunDown = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunRight = new HashMap<Integer, Image>();
	HashMap<Integer, Image> playerRunLeft = new HashMap<Integer, Image>();

	Animation playerRunUpAnimation = new Animation();
	Animation playerRunDownAnimation = new Animation();
	Animation playerRunRightAnimation = new Animation();
	Animation playerRunLeftAnimation = new Animation();

	Animator playerAnimator = new Animator();

	public void loadImages() {

		// up
		for (int i = 48; i <= 55; i++) {
			playerStandUp.put(i, loadImage(Resources.path + "images/player_stand/player_stand_" + i + ".png"));
			playerStandUpAnimation.addFrame(playerStandUp.get(i), 0.25f);

			playerRunUp.put(i, loadImage(Resources.path + "images/player_run/player_run_" + i + ".png"));
			playerRunUpAnimation.addFrame(playerRunUp.get(i), 0.25f);
		}
		playerAnimator.addAnimation("playerStandUp", playerStandUpAnimation);
		playerAnimator.addAnimation("playerRunUp", playerRunUpAnimation);

		// down
		for (int i = 40; i <= 47; i++) {
			playerStandDown.put(i, loadImage(Resources.path + "images/player_stand/player_stand_" + i + ".png"));
			playerStandDownAnimation.addFrame(playerStandDown.get(i), 0.25f);

			playerRunDown.put(i, loadImage(Resources.path + "images/player_run/player_run_" + i + ".png"));
			playerRunDownAnimation.addFrame(playerRunDown.get(i), 0.25f);
		}
		playerAnimator.addAnimation("playerStandDown", playerStandDownAnimation);
		playerAnimator.addAnimation("playerRunDown", playerRunDownAnimation);

		// right
		for (int i = 56; i <= 63; i++) {
			playerStandRight.put(i, loadImage(Resources.path + "images/player_stand/player_stand_" + i + ".png"));
			playerStandRightAnimation.addFrame(playerStandRight.get(i), 0.25f);

			playerRunRight.put(i, loadImage(Resources.path + "images/player_run/player_run_" + i + ".png"));
			playerRunRightAnimation.addFrame(playerRunRight.get(i), 0.25f);
		}
		playerAnimator.addAnimation("playerStandRight", playerStandRightAnimation);
		playerAnimator.addAnimation("playerRunRight", playerRunRightAnimation);

		// left
		for (int i = 32; i <= 39; i++) {
			playerStandLeft.put(i, loadImage(Resources.path + "images/player_stand/player_stand_" + i + ".png"));
			playerStandLeftAnimation.addFrame(playerStandLeft.get(i), 0.25f);

			playerRunLeft.put(i, loadImage(Resources.path + "images/player_run/player_run_" + i + ".png"));
			playerRunLeftAnimation.addFrame(playerRunLeft.get(i), 0.25f);
		}
		playerAnimator.addAnimation("playerStandLeft", playerStandLeftAnimation);
		playerAnimator.addAnimation("playerRunLeft", playerRunLeftAnimation);

		playerAnimator.state = "playerStandDown";

		entity = new Player(playerAnimator);

		entity.setPosition(new Vector2(400f, 100f));

		entity.setSpeed(new Vector2(0f, 0f));
		
		Resources.load(Resources.ResourceType.Music, "backgroundSound", Resources.path+"music/background.wav");
		AudioManager.getInstance().playLoop("backgroundSound");

	}

	public Image loadImage(String fileName) {
		return new ImageIcon(fileName).getImage();
	}

	public void onUpdate() {
		
	}

	public void onDestroy() {
	}

	/**
	 * 在游戏循环开始前进行初始化工作
	 */
	public void init() {
		keyInputHandler = new KeyInputHandler(this);
		mouseInputHandler = new MouseInputHandler(this);
	}

	public synchronized void start() {
		// 加载游戏资源
		viewAngle = GameCore.GameViewAngle.ViewAngle2DOT5;
		isRunning = true;
		thread = new Thread(this, name + "_main");
		thread.start();
		onStart();
	}

	public synchronized void stop() {
		onDestroy();
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
				frames++;
				render();

				animationLoop();

			}
		}
	}
	
	int deltaX = 0, deltaY = 0;

	public void animationLoop() {
		int speed = 80;
		boolean isMoved = false;
		
		if (this.keyInputHandler.up.isPressed()) {
			entity.animator.state = "playerRunUp";
			entity.getPosition().x += -1 * speed * Time.deltaTime;
			entity.getPosition().y += -1 * speed * Time.deltaTime;
			
			deltaX = -1;
			deltaY = -1;
			isMoved = true;
		} else if (this.keyInputHandler.down.isPressed()) {
			entity.animator.state = "playerRunDown";
			entity.getPosition().x += 1 * speed * Time.deltaTime;
			entity.getPosition().y += 1 * speed * Time.deltaTime;

			deltaX = 1;
			deltaY = 1;
			isMoved = true;
		} else if (this.keyInputHandler.left.isPressed()) {
			entity.animator.state = "playerRunLeft";
			entity.getPosition().x += -1 * speed * Time.deltaTime;
			entity.getPosition().y += 1 * speed * Time.deltaTime;

			deltaX = -1;
			deltaY = 1;
			isMoved = true;
		} else if (this.keyInputHandler.right.isPressed()) {
			entity.animator.state = "playerRunRight";
			entity.getPosition().x += 1 * speed * Time.deltaTime;
			entity.getPosition().y += -1 * speed * Time.deltaTime;

			deltaX = 1;
			deltaY = -1;
			isMoved = true;
		}

		if (!isMoved) {
			Debug.log(Debug.DebugLevel.INFO, "deltaX:"+deltaX+"deltaY:"+deltaY);
			if (deltaX > 0 && deltaY > 0) {
				entity.animator.state = "playerStandDown";
			}
			if (deltaX > 0 && deltaY < 0) {
				entity.animator.state = "playerStandRight";
			}
			if (deltaX < 0 && deltaY > 0) {
				entity.animator.state = "playerStandLeft";
			}
			if (deltaX < 0 && deltaY < 0) {
				entity.animator.state = "playerStandUp";
			}
		}

		entity.update();

		Graphics2D g = this.getGraphics2D();
		draw(g);
		g.dispose();
	}

	public void draw(Graphics2D g) {
		// 渲染bufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);

		// 绘制entity
		entity.draw(g);
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
		if (isApplet) {
			return;
		}
		GameCore.name = name;
		this.window.setTitle(name);
	}
}
