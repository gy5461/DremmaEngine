package priv.dremma.game.entities;

import java.awt.Image;

import priv.dremma.game.animation.Animation;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class Entity {
	
	private Animation anim;
	
	private Vector2 position;	//λ��
	
	private Vector2 speed;	//�ٶ�
	
	/**
	 * ����anim����Entity
	 * @param anim
	 */
	public Entity(Animation anim) {
		this.anim = anim;
	}
	
	/**
	 * ����ʱ�����Entity�Ķ�����λ��
	 */
	public void update() {
		this.position.x += this.speed.x * Time.deltaTime;
		this.position.y += this.speed.y * Time.deltaTime;
		anim.update();
	}
	
	/**
	 * ��ȡEntity��ǰλ��
	 * @return
	 */
	public Vector2 getPosition() {
		return this.position;
	}
	
	/**
	 * ��ȡEntity��ǰ�ٶ�
	 * @return
	 */
	public Vector2 getSpeed() {
		return this.speed;
	}
	
	/**
	 * ����Entity��ǰλ��
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	/**
	 * ����Entity��ǰ�ٶ�
	 * @param speed
	 */
	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}
	
	/**
	 * ��ȡEntity��ǰͼ����
	 * @return
	 */
	public int getWidth() {
		return anim.getImage().getWidth(null);
	}
	
	/**
	 * ��ȡEntity��ǰͼ��߶�
	 * @return
	 */
	public int getHeight() {
		return anim.getImage().getHeight(null);
	}
	
	/**
	 * ��ȡEntity��ǰͼ��
	 * @return
	 */
	public Image getImage() {
		return anim.getImage();
	}
}
