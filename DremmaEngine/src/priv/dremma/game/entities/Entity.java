package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Resources;
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
		AffineTransform transform = new AffineTransform();
		transform.translate(this.position.x - this.getWidth() * this.scale.x / 2f, this.position.y - this.getHeight() * this.scale.y / 2f);
		transform.scale(this.scale.x, this.scale.y);
		g.drawImage(this.getImage(), transform, null);
		
		// 绘制箭头
		float length = 50f;
		
		AffineTransform xArrowTransform = new AffineTransform();
		xArrowTransform.translate(this.position.x, this.position.y);
		xArrowTransform.scale(length, 1);
		g.drawImage(Resources.loadImage(Resources.path+"images/xArrow.png"), xArrowTransform, null);
		
		Image xArrowHead = Resources.loadImage(Resources.path+"images/xArrowHead.png");
		AffineTransform xArrowHeadTransform = new AffineTransform();
		xArrowHeadTransform.translate(this.position.x+length+xArrowHead.getHeight(null)*0.25, this.position.y-xArrowHead.getWidth(null)*0.25/2+1);
		xArrowHeadTransform.rotate(Math.toRadians(90));
		xArrowHeadTransform.scale(0.25, 0.25);
		g.drawImage(xArrowHead, xArrowHeadTransform, null);
		
		AffineTransform yArrowTransform = new AffineTransform();
		yArrowTransform.translate(this.position.x, this.position.y);
		yArrowTransform.scale(1, length);
		g.drawImage(Resources.loadImage(Resources.path+"images/yArrow.png"), yArrowTransform, null);
		
		Image yArrowHead = Resources.loadImage(Resources.path+"images/yArrowHead.png");
		AffineTransform yArrowHeadTransform = new AffineTransform();
		yArrowHeadTransform.translate(this.position.x+yArrowHead.getWidth(null)*0.25/2+0.5, this.position.y+length+yArrowHead.getHeight(null)*0.25);
		yArrowHeadTransform.rotate(Math.toRadians(180));
		yArrowHeadTransform.scale(0.25, 0.25);
		g.drawImage(yArrowHead, yArrowHeadTransform, null);
		
		AffineTransform choosen = new AffineTransform();
		choosen.translate(this.position.x+1, this.position.y+1);
		choosen.scale(15, 15);
		g.drawImage(Resources.loadImage(Resources.path+"images/choosen.png"), choosen, null);
	}
}
