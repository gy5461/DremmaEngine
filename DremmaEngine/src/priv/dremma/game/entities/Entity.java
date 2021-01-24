package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.Game;
import priv.dremma.game.animation.Animation;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class Entity {

	private Animation anim;

	private Vector2 position; // λ��

	private Vector2 speed; // �ٶ�

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
	public Entity(Animation anim) {
		this.anim = anim;
		this.position = new Vector2(0, 0);
		this.speed = new Vector2(0, 0);
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
	 * 
	 * @return
	 */
	public Vector2 getPosition() {
		return this.position;
	}

	/**
	 * ��ȡEntity��ǰ�ٶ�
	 * 
	 * @return
	 */
	public Vector2 getSpeed() {
		return this.speed;
	}

	/**
	 * ����Entity��ǰλ��
	 * 
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * ����Entity��ǰ�ٶ�
	 * 
	 * @param speed
	 */
	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}

	/**
	 * ��ȡEntity��ǰͼ����
	 * 
	 * @return
	 */
	public int getWidth() {
		return anim.getImage().getWidth(null);
	}

	/**
	 * ��ȡEntity��ǰͼ��߶�
	 * 
	 * @return
	 */
	public int getHeight() {
		return anim.getImage().getHeight(null);
	}

	/**
	 * ��ȡEntity��ǰͼ��
	 * 
	 * @return
	 */
	public Image getImage() {
		return anim.getImage();
	}

	/**
	 * ����entity
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		AffineTransform transform = new AffineTransform();
		transform.scale(2, 2); // ����
		transform.translate(400, 200); // ƽ��
		transform.rotate(Math.toRadians(90)); // ˳ʱ����ת90��
		transform.scale(-1, 1); // ����ת
		g.drawImage(this.getImage(), transform, null);
	}
}
