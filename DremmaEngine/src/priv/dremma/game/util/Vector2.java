package priv.dremma.game.util;

import java.io.Serializable;

/**
 * �Զ�ά�����ĳ��÷����ķ�װ
 * 
 * @author guoyi
 *
 */
public class Vector2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6749397976961813275L;
	public float x, y; // ����(x,y)��x����y���ϵķ���

	public static Vector2 zero() {
		return new Vector2();
	}

	public static Vector2 one() {
		return new Vector2(1, 1);
	}

	// Ĭ�Ϲ��캯��
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}

	// ���ι��캯��
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 vec2) {
		this.x = vec2.x;
		this.y = vec2.y;
	}

	// ��������ģ��
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	// ��������ģ����ƽ������magnitude��
	public float sqrMagnitude() {
		return x * x + y * y;
	}

	// ���ر����������һ�������Ľ����a��b = |a||b|cos��
	public float dot(Vector2 another) {
		return x * another.x + y * another.y;
	}

	// ��������һ��
	public Vector2 normalized() {
		float magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}

	// ��ת����
	public Vector2 rotate(float angle) {
		double radians = Math.toRadians(angle); // ���Ƕ�ת��Ϊ����
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	// �������
	public Vector2 add(Vector2 another) {
		return new Vector2(x + another.x, y + another.y);
	}

	// ����������
	public Vector2 add(float another) {
		return new Vector2(x + another, y + another);
	}

	// �������
	public Vector2 sub(Vector2 another) {
		return new Vector2(x - another.x, y - another.y);
	}

	// ����������
	public Vector2 sub(float another) {
		return new Vector2(x - another, y - another);
	}

	// �������
	public Vector2 mul(Vector2 another) {
		return new Vector2(x * another.x, y * another.y);
	}

	// ����������
	public Vector2 mul(float another) {
		return new Vector2(x * another, y * another);
	}

	// �������
	public Vector2 div(Vector2 another) {
		return new Vector2(x / another.x, y / another.y);
	}

	// ����������
	public Vector2 div(float another) {
		return new Vector2(x / another, y / another);
	}

	// ��������ֵ
	public Vector2 abs() {
		return new Vector2(Math.abs(x), Math.abs(y));
	}

	// ������ӡ��ʽ
	@Override
	public String toString() {
		return "Vector2 [x=" + x + ", y=" + y + "]";
	}
	
	/**
	 * �����Ƿ����
	 * @param another
	 * @return
	 */
	public boolean isEqual(Vector2 another) {
		return FloatCompare.isEqual(this.x, another.x) && FloatCompare.isEqual(this.y, another.y);
	}

	/**
	 * �����Ƿ����another
	 * @param another
	 * @return
	 */
	public boolean isBigger(Vector2 another) {
		return FloatCompare.isBigger(this.x, another.x) && FloatCompare.isBigger(this.y, another.y);
	}
	
	/**
	 * �����Ƿ���ڵ���another
	 * @param another
	 * @return
	 */
	public boolean isBiggerOrEqual(Vector2 another) {
		return this.isBigger(another) || this.isEqual(another);
	}
	

	/**
	 * �����Ƿ�С��another
	 * @param another
	 * @return
	 */
	public boolean isLess(Vector2 another) {
		return FloatCompare.isLess(this.x, another.x) && FloatCompare.isLess(this.y, another.y);
	}
	
	/**
	 * �����Ƿ�С�ڵ���another
	 * @param another
	 * @return
	 */
	public boolean isLessOrEqual(Vector2 another) {
		return this.isLess(another) || this.isEqual(another);
	}
	
	/**
	 * ��ǰ�����Ƿ���һ��������
	 * @param leftUp
	 * @param rightDown
	 * @return
	 */
	public boolean isInRect(Vector2 leftUp, Vector2 rightDown) {
		return this.isLessOrEqual(rightDown) && this.isBiggerOrEqual(leftUp);
	}
	
	/**
	 * ��ά�����Ĳ�ֵ����
	 * @param startPos ��ʼλ��
	 * @param endPos ����λ��
	 * @param time ��ֵʱ�䣨0.0f��1.0f��
	 * @return
	 */
	public static Vector2 lerp(Vector2 startPos, Vector2 endPos, float time) {
		if(FloatCompare.isBigger(time, 1.0f)) {
			time = 1.0f;
		}
		return startPos.add(endPos.sub(startPos).mul(time));
	}
}
