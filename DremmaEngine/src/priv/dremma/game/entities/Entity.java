package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.util.Vector2;

public class Entity implements Cloneable{

	public Animator animator;

	public Vector2 position; // λ��

	public Vector2 speed; // �ٶ�

	public Entity() {
		this.position = new Vector2(0, 0);
		this.speed = new Vector2(0, 0);
	}

	public Entity(Vector2 position, Vector2 speed) {
		this.position = position;
		this.speed = speed;
	}

	/**
	 * ����anim����Entity
	 * 
	 * @param anim
	 */
	public Entity(Animator animator) {
		this.animator = animator;
		this.position = Vector2.zero;
		this.speed = Vector2.zero;
	}
	
	public void setAnimator(Animator animator) {
		this.animator = animator;
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
	 * �ı�clone�����Ŀɼ���
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}

	/**
	 * ����entity
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
		transform.translate(this.position.x, this.position.y);
		g.drawImage(this.getImage(), transform, null);
	}
}
