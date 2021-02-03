package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Vector2;

public class Entity {

	public Animator animator;

	public Vector2 position; // 位置

	public float speed;
	public Vector2 moveVector; // 速度
	private Vector2 scale; // 缩放
	public String name;

	public Entity() {
		this.position = Vector2.zero();
		this.speed = 0;
		this.moveVector = Vector2.zero();
		this.scale = Vector2.one();
	}
	
	public Entity(Entity e) {
		this.animator = new Animator(e.animator);
		this.position = new Vector2(e.position);
		this.speed = e.speed;
		this.moveVector = new Vector2(e.moveVector);
		this.scale = new Vector2(e.scale);
		this.name = new String(e.name);
	}

	/**
	 * 根据anim构造Entity
	 * 
	 * @param anim
	 */
	public Entity(Animator animator) {
		this.animator = animator;
		this.position = Vector2.zero();
		this.speed = 0;
		this.moveVector = Vector2.zero();
		this.scale = Vector2.one();
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}
	
	public void setScale(Vector2 scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
		this.speed *= scale.x;
	}

	/**
	 * 根据时间更新Entity的动画
	 */
	public void update() {
		this.animator.playLoop(animator.state);
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
	 * 获取实体底部纵坐标
	 * @return
	 */
	public float getBottom() {
		return this.position.y + this.getHeight() * this.scale.y / 2f;
	}

	/**
	 * 绘制entity
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		Vector2 screenPos = GUtils.worldPixelToViewPort(this.position);
		
		AffineTransform transform = new AffineTransform();
		transform.translate(screenPos.x - this.getWidth() * this.scale.x / 2f, screenPos.y - this.getHeight() * this.scale.y / 2f);
		transform.scale(this.scale.x, this.scale.y);
		g.drawImage(this.getImage(), transform, null);
	}
}
