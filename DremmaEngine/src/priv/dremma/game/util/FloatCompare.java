package priv.dremma.game.util;

/**
 * 对于浮点数比较进行的修正
 * 
 * @author guoyi
 *
 */
public class FloatCompare {

	public static final float eps = 1e-8f; // 精度，精确到小数点后6位

	static int sgn(float a) {
		return a < -1.0f * eps ? -1 : a < eps ? 0 : 1;
	}

	public static boolean isEqual(float a, float b) {
		return sgn(a - b) == 0;
	}

	public static boolean isNotEqual(float a, float b) {
		return sgn(a - b) != 0;
	}

	public static boolean isLess(float a, float b) {
		return sgn(a - b) < 0;
	}

	public static boolean isLessOrEqual(float a, float b) {
		return sgn(a - b) <= 0;
	}

	public static boolean isBigger(float a, float b) {
		return sgn(a - b) > 0;
	}

	public static boolean isBiggerOrEqual(float a, float b) {
		return sgn(a - b) >= 0;
	}
}
