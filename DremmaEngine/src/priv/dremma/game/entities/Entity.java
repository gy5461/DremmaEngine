package priv.dremma.game.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import priv.dremma.game.anim.Animator;
import priv.dremma.game.collision.CollisionBox;
import priv.dremma.game.tiles.TileMap;
import priv.dremma.game.util.GUtils;
import priv.dremma.game.util.Time;
import priv.dremma.game.util.Vector2;

public class Entity {

	public Animator animator;

	public Vector2 position; // λ��

	public float speed;
	public Vector2 moveVector; // �ٶ�
	private Vector2 scale; // ����
	public String name;

	public boolean visible = true; // �Ƿ�ɼ�
	public boolean detectCollision = true; // �Ƿ�����ײ

	private float destRestreatDistance = 0f;
	private float curRestreatDistance = 0f;
	public Vector2 retreatVector = Vector2.zero();
	
	private Image staticImage = null;
	public boolean isStatic = false;

	public static enum EntityState {
		STAND, MOVE, ATTACK, DEAD;
	}

	public static enum EntityDirection {
		UP, DOWN, LEFT, RIGHT;
	}

	public EntityState state;
	public EntityDirection direction;

	public Entity() {
		this.position = Vector2.zero();
		this.speed = 0;
		this.moveVector = Vector2.zero();
		this.scale = Vector2.one();

		this.state = Entity.EntityState.STAND;
		this.direction = Entity.EntityDirection.DOWN;
	}

	public Entity(Entity e) {
		this.animator = e.animator;
		this.position = new Vector2(e.position);
		this.speed = e.speed;
		this.moveVector = new Vector2(e.moveVector);
		this.scale = new Vector2(e.scale);
		this.name = new String(e.name);

		this.state = e.state;
		this.direction = e.direction;

		this.visible = e.visible;
		this.detectCollision = e.detectCollision;
		
		this.staticImage = e.staticImage;
		this.isStatic = e.isStatic;
	}

	/**
	 * ����anim����Entity
	 * 
	 * @param anim
	 */
	public Entity(Animator animator) {
		this.animator = animator;
		this.position = Vector2.zero();
		this.speed = 0;
		this.moveVector = Vector2.zero();
		this.scale = Vector2.one();

		this.state = Entity.EntityState.STAND;
		this.direction = Entity.EntityDirection.DOWN;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	public void setScale(Vector2 scale) {
		this.scale.x = scale.x;
		this.scale.y = scale.y;
		this.speed *= scale.x;
	}

	public Vector2 getScale() {
		return this.scale;
	}
	
	/**
	 * ����Ǿ�̬���壬ֱ�ӵ��ñ���������ͼƬ
	 * @param image
	 */
	public void setStaticImage(Image image) {
		this.staticImage = image;
		this.isStatic = true;
	}

	/**
	 * ����Entity
	 */
	public void update() {
		if(this.isStatic) {
			return;
		}
		
		// ���˴���
		if (this.curRestreatDistance < this.destRestreatDistance) {
			this.position = this.position.add(this.retreatVector);
			CollisionBox.collisionBoxs.get(this.name).trans(this.retreatVector);

			this.curRestreatDistance += this.retreatVector.magnitude();
		} else {
			this.curRestreatDistance = 0f;
			this.retreatVector = Vector2.zero();
		}

		if (this.animator == null) {
			return;
		}
		this.animator.update();

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
		if(this.isStatic) {
			return this.staticImage;
		}
		return animator.getAnimation(animator.getState()).getImage();
	}

	/**
	 * ��ȡʵ��ײ�������
	 * 
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
		if (this.visible == false) {
			return;
		}

		Vector2 screenPos = GUtils.worldPixelToViewPort(this.position);

		AffineTransform transform = new AffineTransform();
		transform.translate(screenPos.x - this.getWidth() * this.scale.x / 2f,
				screenPos.y - this.getHeight() * this.scale.y / 2f);
		transform.scale(this.scale.x, this.scale.y);
		g.drawImage(this.getImage(), transform, null);
	}

	/**
	 * Entity��ָ���������ָ������
	 * 
	 * @param direction
	 * @param distance
	 */
	public void retreat(Entity.EntityDirection direction, float distance) {
		Vector2 beatBack = Vector2.zero();
		switch (direction) {
		case DOWN:
			beatBack = new Vector2(distance, distance * TileMap.modifier).mul(new Vector2(1, 1)).mul(Time.deltaTime);
			break;
		case LEFT:
			beatBack = new Vector2(distance, distance * TileMap.modifier).mul(new Vector2(-1, 1)).mul(Time.deltaTime);
			break;
		case RIGHT:
			beatBack = new Vector2(distance, distance * TileMap.modifier).mul(new Vector2(1, -1)).mul(Time.deltaTime);
			break;
		case UP:
			beatBack = new Vector2(distance, distance * TileMap.modifier).mul(new Vector2(-1, -1)).mul(Time.deltaTime);
			break;
		}
		this.destRestreatDistance = distance;
		this.curRestreatDistance = 0f;
		this.retreatVector = beatBack;
	}
}
