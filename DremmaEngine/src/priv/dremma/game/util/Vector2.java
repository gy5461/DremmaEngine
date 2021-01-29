package priv.dremma.game.util;

/**
 * 对二维向量的常用方法的封装
 * 
 * @author guoyi
 *
 */
public class Vector2 {

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

}
