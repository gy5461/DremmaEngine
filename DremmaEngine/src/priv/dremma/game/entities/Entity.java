package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class Entity {

	public Animator animator;

	private Vector2 position; // 位置

	private Vector2 speed; // 速度

	public Entity() {
		this.position = new Vector2(0, 0);
		this.speed = new Vector2(0, 0);
	}

	public Entity(Vector2 position, Vector2 speed) {
		this.position = position;
		this.speed = speed;
	}

	/**
	 * 根据anim构造Entity
	 * 
	 * @param anim
	 */
	public Entity(Animator animator) {
		this.animator = animator;
		this.position = new Vector2(0, 0);
		this.speed = new Vector2(0, 0);
	}
	
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	/**
	 * 根据时间更新Entity的动画及位置
	 */
	public void update() {
		this.position.x += this.speed.x * Time.deltaTime;
		this.position.y += this.speed.y * Time.deltaTime;
		this.animator.playLoop(animator.state);
	}

	/**
	 * 获取Entity当前位置
	 * 
	 * @return
	 */
	public Vector2 getPosition() {
		return this.position;
	}

	/**
	 * 获取Entity当前速度
	 * 
	 * @return
	 */
	public Vector2 getSpeed() {
		return this.speed;
	}

	/**
	 * 设置Entity当前位置
	 * 
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * 设置Entity当前速度
	 * 
	 * @param speed
	 */
	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}

	/**
	 * 获取Entity当前图像宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.getImage().getWidth(null);
	}

	/**
	 * 获取Entity当前图像高度
	 * 
	 * @return
	 */
	public int getHeight() {
		return this.getImage().getHeight(null);
	}

	/**
	 * 获取Entity当前图像
	 * 
	 * @return
	 */
	public Image getImage() {
		return animator.getAnimation(animator.state).getImage();
	}

	/**
	 * 绘制entity
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
//		transform.scale(2, 2); // 缩放
//		transform.translate(400, 200); // 平移
//		transform.rotate(Math.toRadians(90)); // 顺时针旋转90度
//		transform.scale(-1, 1); // 镜像翻转
		
		transform.translate(this.position.x, this.position.y);
		g.drawImage(this.getImage(), transform, null);
	}
}
