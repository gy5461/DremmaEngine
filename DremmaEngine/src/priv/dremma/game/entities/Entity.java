package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Vector2;

public class Entity implements Cloneable {

	public Animator animator;

	public Vector2 position; // λ��

	public Vector2 speed; // �ٶ�
	private Vector2 scale; // ����

	public Entity() {
		this.position = Vector2.zero();
		this.speed = Vector2.zero();
		this.scale = Vector2.one();
	}

	public Entity(Vector2 position, Vector2 speed) {
		this.position = position;
		this.speed = speed;
		this.scale = Vector2.one();
	}
	
	public Entity(Entity e) {
		this.animator = new Animator(e.animator);
		this.position = new Vector2(e.position);
		this.speed = new Vector2(e.speed);
		this.scale = new Vector2(e.scale);
	}

	/**
	 * ����anim����Entity
	 * 
	 * @param anim
	 */
	public Entity(Animator animator) {
		this.animator = animator;
		this.position = Vector2.zero();
		this.speed = Vector2.zero();
		this.scale = Vector2.one();
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}
	
	public void setScale(Vector2 scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
		this.speed = this.speed.mul(scale);
	}

	/**
	 * ����ʱ�����Entity�Ķ���
	 */
	public void update() {
		this.animator.playLoop(animator.state);
	}

	/**
	 * ��ȡEntity��ǰͼ����
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.getImage().getWidth(null);
	}

	/**
	 * ��ȡEntity��ǰͼ��߶�
	 * 
	 * @return
	 */
	public int getHeight() {
		return this.getImage().getHeight(null);
	}

	/**
	 * ��ȡEntity��ǰͼ��
	 * 
	 * @return
	 */
	public Image getImage() {
		return animator.getAnimation(animator.state).getImage();
	}

	/**
	 * ��ȡʵ��ײ�������
	 * @return
	 */
	public float getBottom() {
		return this.position.y + this.getHeight() * this.scale.y / 2f;
	}

	/**
	 * ����entity
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
		transform.translate(this.position.x - this.getWidth() * this.scale.x / 2f, this.position.y - this.getHeight() * this.scale.y / 2f);
		transform.scale(this.scale.x, this.scale.y);
		g.drawImage(this.getImage(), transform, null);
	}
}
