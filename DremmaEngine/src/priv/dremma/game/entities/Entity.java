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

	public Vector2 position; // 位置
	public float rotation = 0; // 顺时针旋转角度

	public float speed;
	public Vector2 moveVector; // 速度
	private Vector2 scale; // 缩放
	public String name;

	public boolean visible = true; // 是否可见
	public boolean detectCollision = true; // 是否检测碰撞

	private float destRestreatDistance = 0f;
	private float curRestreatDistance = 0f;
	public Vector2 retreatVector = Vector2.zero();

	private Image staticImage = null;
	public boolean isStatic = false;

	public static enum EntityState {
		STAND, MOVE, ATTACK, DEAD, WIN;
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
		this.rotation = e.rotation;
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

		this.state = Entity.EntityState.STAND;
		this.direction = Entity.EntityDirection.DOWN;
	}
	
	// ------------------钩子函数----------------------
	public void onStart() {
	}
	
	public void onUpdate() {
	}
	
	public void onDestroy() {
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
	 * 如果是静态物体，直接调用本方法设置图片
	 * 
	 * @param image
	 */
	public void setStaticImage(Image image) {
		this.staticImage = image;
		this.isStatic = true;
	}

	/**
	 * 更新Entity
	 */
	public void update() {
		this.onUpdate();
		if (this.isStatic) {
			return;
		}

		// 后退处理
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
	 * 获取Entity当前图像宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		if (this.getImage() == null) {
			return 0;
		}
		return this.getImage().getWidth(null);
	}

	/**
	 * 获取Entity当前图像高度
	 * 
	 * @return
	 */
	public int getHeight() {
		if (this.getImage() == null) {
			return 0;
		}
		return this.getImage().getHeight(null);
	}

	/**
	 * 获取Entity当前图像
	 * 
	 * @return
	 */
	public Image getImage() {
		if (this.isStatic) {
			return this.staticImage;
		}
		if (animator == null) {
			return null;
		}
		if (animator.getState() == null) {
			return null;
		}
		if (animator.getAnimation(animator.getState()) == null) {
			return null;
		}
		return animator.getAnimation(animator.getState()).getImage();
	}

	/**
	 * 获取实体底部纵坐标
	 * 
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
		if (this.visible == false) {
			return;
		}

		Vector2 screenPos = GUtils.worldPixelToViewPort(this.position);

		AffineTransform transform = new AffineTransform();
		transform.translate(screenPos.x - this.getWidth() * this.scale.x / 2f,
				screenPos.y - this.getHeight() * this.scale.y / 2f);
		transform.rotate(Math.toRadians(this.rotation));
		transform.scale(this.scale.x, this.scale.y);

		g.drawImage(this.getImage(), transform, null);
	}

	/**
	 * Entity向指定方向后退指定距离
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
