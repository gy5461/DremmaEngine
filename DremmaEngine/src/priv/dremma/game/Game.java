package priv.dremma.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import priv.dremma.game.animation.Animation;
import priv.dremma.game.entities.Entity;
import priv.dremma.game.event.KeyInputHandler;
import priv.dremma.game.event.MouseInputHandler;
import priv.dremma.game.event.WindowInputHandler;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

/**
 * 游戏主体类，包含游戏窗体、渲染等
 * 
 * @author guoyi
 *
 */
@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

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

	private Entity entity;

	Vector<Image> playerRun = new Vector<Image>();
	Vector<Image> playerStand = new Vector<Image>();

	public void loadImages() {
		Animation anim = new Animation();
		for (int i = 0; i <= 63; i++) {
			playerRun.add(loadImage("res/images/player_run/player_run_" + i + ".png"));
			playerStand.add(loadImage("res/images/player_stand/player_stand_" + i + ".png"));
			anim.addFrame(playerRun.get(i), 0.25f);
		}

		entity = new Entity(anim);

		entity.setPosition(new Vector2(400f, 100f));

		entity.setSpeed(new Vector2(0f, 0f));

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
		viewAngle = Game.GameViewAngle.ViewAngle2DOT5;
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

			//Time.shouldRender = true; //解除shouldRender对于60帧左右的限制
			if (Time.shouldRender) {
				// 游戏开发者更新
				onUpdate();
				frames++;
				render();

				animationLoop();

			}
		}
	}

	public void animationLoop() {

		entity.update();

		Graphics2D g = this.getGraphics2D();
		draw(g);
		g.dispose();
	}

	public void draw(Graphics2D g) {
		// 渲染bufferedImage
		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
		
		//绘制entity
		g.drawImage(entity.getImage(), Math.round(entity.getPosition().x), Math.round(entity.getPosition().y), null);
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
		Game.name = name;
		this.window.setTitle(name);
	}
}
