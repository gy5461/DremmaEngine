package priv.dremma.game.util;

import java.io.Serializable;

/**
 * 对二维向量的常用方法的封装
 * 
 * @author guoyi
 *
 */
public class Vector2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6749397976961813275L;
	public float x, y; // 向量(x,y)在x轴与y轴上的分量

	public static Vector2 zero() {
		return new Vector2();
	}

	public static Vector2 one() {
		return new Vector2(1, 1);
	}

	// 默认构造函数
	public Vector2() {
		this.x = 0;
		this.y = 0;
	}

	// 带参构造函数
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 vec2) {
		this.x = vec2.x;
		this.y = vec2.y;
	}

	// 返回向量模长
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	// 返回向量模长的平方，比magnitude快
	public float sqrMagnitude() {
		return x * x + y * y;
	}

	// 返回本向量点乘另一个向量的结果：a·b = |a||b|cosθ
	public float dot(Vector2 another) {
		return x * another.x + y * another.y;
	}

	// 将向量归一化
	public Vector2 normalized() {
		float magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}

	// 旋转向量
	public Vector2 rotate(float angle) {
		double radians = Math.toRadians(angle); // 将角度转换为弧度
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		return new Vector2((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	// 向量相加
	public Vector2 add(Vector2 another) {
		return new Vector2(x + another.x, y + another.y);
	}

	// 向量加数字
	public Vector2 add(float another) {
		return new Vector2(x + another, y + another);
	}

	// 向量相减
	public Vector2 sub(Vector2 another) {
		return new Vector2(x - another.x, y - another.y);
	}

	// 向量减数字
	public Vector2 sub(float another) {
		return new Vector2(x - another, y - another);
	}

	// 向量相乘
	public Vector2 mul(Vector2 another) {
		return new Vector2(x * another.x, y * another.y);
	}

	// 向量乘数字
	public Vector2 mul(float another) {
		return new Vector2(x * another, y * another);
	}

	// 向量相除
	public Vector2 div(Vector2 another) {
		return new Vector2(x / another.x, y / another.y);
	}

	// 向量除数字
	public Vector2 div(float another) {
		return new Vector2(x / another, y / another);
	}

	// 向量绝对值
	public Vector2 abs() {
		return new Vector2(Math.abs(x), Math.abs(y));
	}

	// 向量打印格式
	@Override
	public String toString() {
		return "Vector2 [x=" + x + ", y=" + y + "]";
	}
	
	/**
	 * 向量是否相等
	 * @param another
	 * @return
	 */
	public boolean isEqual(Vector2 another) {
		return FloatCompare.isEqual(this.x, another.x) && FloatCompare.isEqual(this.y, another.y);
	}

	/**
	 * 向量是否大于another
	 * @param another
	 * @return
	 */
	public boolean isBigger(Vector2 another) {
		return FloatCompare.isBigger(this.x, another.x) && FloatCompare.isBigger(this.y, another.y);
	}
	
	/**
	 * 向量是否大于等于another
	 * @param another
	 * @return
	 */
	public boolean isBiggerOrEqual(Vector2 another) {
		return this.isBigger(another) || this.isEqual(another);
	}
	

	/**
	 * 向量是否小于another
	 * @param another
	 * @return
	 */
	public boolean isLess(Vector2 another) {
		return FloatCompare.isLess(this.x, another.x) && FloatCompare.isLess(this.y, another.y);
	}
	
	/**
	 * 向量是否小于等于another
	 * @param another
	 * @return
	 */
	public boolean isLessOrEqual(Vector2 another) {
		return this.isLess(another) || this.isEqual(another);
	}
	
	/**
	 * 当前坐标是否在一个矩形内
	 * @param leftUp
	 * @param rightDown
	 * @return
	 */
	public boolean isInRect(Vector2 leftUp, Vector2 rightDown) {
		return this.isLessOrEqual(rightDown) && this.isBiggerOrEqual(leftUp);
	}
	
	/**
	 * 二维向量的插值函数
	 * @param startPos 开始位置
	 * @param endPos 结束位置
	 * @param time 插值时间（0.0f～1.0f）
	 * @return
	 */
	public static Vector2 lerp(Vector2 startPos, Vector2 endPos, float time) {
		if(FloatCompare.isBigger(time, 1.0f)) {
			time = 1.0f;
		}
		return startPos.add(endPos.sub(startPos).mul(time));
	}
}
