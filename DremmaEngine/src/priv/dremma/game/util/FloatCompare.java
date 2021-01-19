package priv.dremma.game.util;

/**
 * ���ڸ������ȽϽ��е�����
 * @author guoyi
 *
 */
public class FloatCompare {

	public static final float eps = 1e-8f;	//���ȣ���ȷ��С�����6λ

	static int sgn(float a) {
		return a < -1.0f * eps ? -1 : a < eps ? 0 : 1;
	}

	public static boolean isEqual(float a, float b) {
		return  sgn(a-b)==0;
	}
	
	public static boolean isNotEqual(float a, float b) {
		return  sgn(a-b)!=0;
	}
	
	public static boolean isLess(float a, float b) {
		return  sgn(a-b)<0;
	}
	
	public static boolean isLessOrEqual(float a, float b) {
		return  sgn(a-b)<=0;
	}
	
	public static boolean isBigger(float a, float b) {
		return  sgn(a-b)>0;
	}
	
	public static boolean isBiggerOrEqual(float a, float b) {
		return  sgn(a-b)>=0;
	}
}
