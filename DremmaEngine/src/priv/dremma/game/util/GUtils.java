package priv.dremma.game.util;

import java.awt.*;
import java.io.*;
import java.util.*;

import priv.dremma.game.gfx.Screen;

/**
 * ��̬������
 *
 */
public final class GUtils {
	public static final int INT_ERROR = -2147483648;

	/**
	 * ��һ��������������ָ���Ĵ�С��
	 * 
	 * @���� oldArray �����������
	 * @���� expand ����Ĵ�С
	 * @���� һ�������飬�����鱻���������п�ʼ���֣���ʣ��һЩ�ռ��������ɸ���Ԫ�أ�
	 */
	public final static int[] expandArray(int[] oldArray, int expand) {
		int[] newArray = new int[oldArray.length + expand];
		System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
		return newArray;
	}

	/**
	 * ��һ����άͼ���������������ָ����С��
	 * 
	 * @���� oldArray Ҫ��������飻
	 * @���� expand �������Ŀ��
	 * @���� һ�������顣
	 */
	public final static Image[][] expandArray(Image[][] oldArray, int expand) {
		Image[][] newArray = new Image[oldArray.length + expand][];
		System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
		return newArray;
	}

	/********************************************************************************************
	 ****** ****************�ӿ����ꡢ�����������������tile����֮���ת������**********************
	 *******************************************************************************************/
	/**
	 * �ӿ�����ת����������������.
	 * 
	 * @param vx:         �ӿ�����x��
	 * @param vy:         �ӿ�����y��
	 * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
	 * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ return ������������.
	 **/
	public static Vector2 viewPortToWorldPixel(Vector2 viewport) {
		Vector2 worldPixel = Vector2.zero();
		worldPixel.x = viewport.x + Screen.leftUpPoint.x;
		worldPixel.y = viewport.y + Screen.leftUpPoint.y;
		return worldPixel;

	}

	/**
	 ** ������������ת�����ӿ�����.
	 * 
	 * @param x:          ������������x��
	 * @param y:          ������������y��
	 * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
	 * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ return �ӿ�����.
	 *
	 **/
	public static Vector2 worldPixelToViewPort(Vector2 worldPixel) {
		Vector2 viewport = Vector2.zero();
		viewport.x = worldPixel.x - Screen.leftUpPoint.x;
		viewport.y = worldPixel.y - Screen.leftUpPoint.y;
		return viewport;
	}

	/**
	 * ������������ת��������tile����. @ param x: ��������x���ꣻ @ param y: ��������y���ꣻ @ renturn
	 * ����tile���ꡣ
	 **/
	public static Vector2 worldPixelToWorldTile(Vector2 world, float tileWidth, float tileHeight) {
		float Atx, Aty, centerx, centery;
		if (world.x >= 0) {
			Atx = world.x / tileWidth;
		} else {
			Atx = world.x / tileWidth - 1;
		}
		if (world.y >= 0) {
			Aty = ((world.y / tileHeight) * 2) + 1;
		} else {
			Aty = ((world.y / tileHeight) * 2) - 1;
		}

		centerx = Atx * tileWidth + (tileWidth / 2);

		centery = (Aty * 2) * tileHeight + (tileHeight / 2);
		centery = centery / 2;
		world.y = world.y * 2;

		centerx = world.x - centerx;
		centery = world.y - centery;

		if (centerx >= 0) {
			if (centery >= 0) {
				return (centerx + centery) > tileHeight ? new Vector2(Atx + 1, Aty + 1) : new Vector2(Atx, Aty);
			} else {
				return (centerx - centery) > tileHeight ? new Vector2(Atx + 1, Aty - 1) : new Vector2(Atx, Aty);
			}
		} else {
			if (centery >= 0) {
				return (centery - centerx) > tileHeight ? new Vector2(Atx, Aty + 1) : new Vector2(Atx, Aty);
			} else {
				return (-centerx - centery) > tileHeight ? new Vector2(Atx, Aty - 1) : new Vector2(Atx, Aty);
			}
		}
	}

	/**
	 * �ӿ�����ת��������tile����.
	 * 
	 * @param vx:         �ӿ�����x��
	 * @param vy:         �ӿ�����y��
	 * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
	 * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ return ����tile����.
	 **/
	public static Vector2 viewPortToWorldTile(Vector2 viewport, Vector2 basePoint, float tileWidth, float tileHeight) {
		Vector2 worldPiexl = viewPortToWorldPixel(viewport);
		return worldPixelToWorldTile(worldPiexl, tileWidth, tileHeight);
	}

	/**
	 * ����ĳ��tile�����ĵ�ת����������������.
	 * 
	 * @param x:          ����tile����x��
	 * @param y:          ����tile����y��
	 * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
	 * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ return �ӿ�����.
	 **/
	public static Vector2 worldTileCenterToWorldPixel(Vector2 tile, float tileWidth, float tileHeight, Vector2 scale) {
		tile = tile.sub(Vector2.one());
		Vector2 pixel = Vector2.zero();

		if (tile.y % 2 == 1) {
			pixel = new Vector2(tile.x * tileWidth - tileWidth / 2, tile.y * tileHeight/2 - tileHeight/2).mul(scale);
			//Debug.log(Debug.DebugLevel.INFO, "pixel:" + pixel);
		} else {
			pixel = new Vector2(tile.x * tileWidth,           tile.y * tileHeight/2 - tileHeight/2).mul(scale);
		}
		return pixel;
	}

	/**
	 * ����ĳ��tile�����ĵ�ת�����ӿ�����.
	 * 
	 * @param x:          ����tile����x��
	 * @param y:          ����tile����y��
	 * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
	 * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ return �ӿ�����.
	 **/
	public static Vector2 worldTileCenterToViewPort(Vector2 tile, Vector2 basePoint, float tileWidth,
			float tileHeight) {
		Vector2 piexl = Vector2.zero();
		if (tile.y % 2 == 0) {
			piexl.x = tile.x * tileWidth - basePoint.x;
			piexl.y = tile.y * (tileHeight / 2) - basePoint.y;
			return piexl;
		} else {
			piexl.x = tile.x * tileWidth + (tileWidth / 2) - basePoint.x;
			piexl.y = tile.y * (tileHeight / 2) - basePoint.y;
			return piexl;
		}
	}

	// б����ת������Ϸ�����tile��ֱ����
	// ��б����ϵ�У���ԭ����x�����ߵ��м�㣬����x������Ŀ����x������ͬ��
	// ����м�㣬��ֱ����ϵ�������ԭ�������Ϊmx��my��Ȼ����м���������б����ϵ��y����Ŀ���ǰ����
	// �ڴ���;��˳�򾭹��ĵ��ֱ������ֳ���ǿ�Ĺ����ԣ��ݴ����ǵõ�Ŀ��������ԭ���ֱ���ꡣ
	public static Vector2 slantTileToWorldTile(Vector2 origin, Vector2 tile) {
		Vector2 tileXY = Vector2.zero();
		float mx = origin.x + (origin.y % 2 + tile.x) / 2;
		float my = origin.y + tile.x;
		tileXY.x = mx - ((my + 1) % 2 + tile.y) / 2;
		tileXY.y = my + tile.y;
		return tileXY;
	}

	// ��һ������tile��ֱ���꣨destX,destY��ת��������һ��tile���꣨oringX,oringY��Ϊԭ�������ϵ
	public static Vector2 worldTileToSlantTile(Vector2 origin, Vector2 dest) {
		Vector2 destination = Vector2.zero();
		float tempX, tempY;
		float distantY = dest.y - origin.y;
		// ��ֱ����ϵ�У���ԭ����x�������ߵ��м�㣬����x������Ŀ����x������ͬ��
		// ����м�㣬��б����ϵ�е������ԭ�������Ϊ�����tempX��tempY��
		tempX = dest.x - origin.x;
		tempY = -tempX;
		// ���м�����������ֱ������ϵ��y�ᣬ��Ŀ���ǰ����
		// �������;��˳�򾭹��ĵ��б������ֳ���ǿ�Ĺ�����:�����������
		// �����ߣ����ȵ�һ��ʱx�����1���ڶ���y�����1����˽��棬ֱ��x������y����֮��ΪdistantY��������Ŀ��㡣
		// ������Ŀ����б������Ȼ�͵õ��ˡ�
		// �����ߣ����ȵ�һ��ʱy�����1���ڶ���x�����1����˽��棬ֱ��x������y����֮��ΪdistantY��������Ŀ��㡣
		if (distantY > 0) {
			tempX += distantY / 2;
			tempY += distantY / 2;
		} else {
			tempX += (distantY + 1) / 2;
			tempY += (distantY + 1) / 2;
		}
		if (((int) (dest.y - origin.y) & 1) != 0) {
			if (dest.y > origin.y) {
				tempX += 1 - ((int) origin.y & 1);
				tempY += (int) origin.y & 1;
			} else {
				tempX -= (int) origin.y & 1;
				tempY -= 1 - ((int) origin.y & 1);
			}
		}
		destination.x = tempX;
		destination.y = tempY;

		return destination;
	}

	/*
	 * *****************************************************************************
	 * * ��д�ı��Ĺ���************************************************
	 ******************************************************************************/

	/**
	 * read a line from the "reader", and the next char to be read after the call of
	 * this method will start from the front of a new line.
	 * 
	 * @param s      String
	 * @param writer Writer
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String readLine(Reader reader) {
		int c;
		StringBuffer s = new StringBuffer();
		try {
			while (((c = reader.read()) != -1)) {
				if (c != 13) {
					s.append((char) c);
				} else {
					reader.read();
					break;
				}
			}
		} catch (IOException ioe) {
			System.out.println("���ı�ʱ�����쳣������");
		}
		if (s != null) {
			return s.toString();
		} else {
			return "endOfFile";
		}
	}

	// ��һ��Sting���зָ�ָ�Ľ���Ϊһ�������ո���' '��
	public static String[] split(String s) {
		Vector<String> resultBuffer = new Vector<String>();
		String trimed = s.trim();

		int i = trimed.indexOf(' ');
		while (i != -1) {
			resultBuffer.addElement(trimed.substring(0, i));
			trimed = trimed.substring(i + 1);
			trimed = trimed.trim();
			i = trimed.indexOf(' ');
		}
		resultBuffer.addElement(trimed);

		String[] result = new String[resultBuffer.size()];
		resultBuffer.copyInto(result);
		return result;
	}

	// ��һ���ַ����ж���ָ����������һ���ַ���������ֵ
	public static int getIntProperty(String sourceStr, String propertyName) {
		sourceStr = sourceStr.trim();
		if (!sourceStr.startsWith("#")) {
			System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ���һ���ַ����� # ��");
		}
		String result = sourceStr.substring(1);
		result = result.trim();
		if (!result.startsWith(propertyName)) {
			System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ�ʵ���������ǣ�" + result + "���������������:" + propertyName + " ��");
		}
		result = result.substring(propertyName.length());
		result = result.trim();
		return Integer.parseInt(result);
	}

	// ��һ���ַ����ж���ָ����������������������ֵ
	public static int[] getIntProperties(String sourceStr, String propertyName, int num) {
		String[] splitted = split(sourceStr);
		if (splitted.length != num + 2) {
			System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName + "����Ԫ�صĸ������ԣ�");
			return null;
		}
		int[] result = new int[num];
		if (!splitted[0].equalsIgnoreCase("#")) {
			System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName + "������һ���ַ����� # ����ʽ����");
			return null;
		}
		if (!splitted[1].equalsIgnoreCase(propertyName)) {
			System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName + "����������������������������ޣ�");
			return null;
		}
		for (int i = 0; i < num; i++) {
			try {
				result[i] = Integer.parseInt(splitted[i + 2]);
			} catch (NumberFormatException e) {
				System.out.println("��һ���ַ����ж�ȡ���ԣ�" + propertyName + "�����ַ�������ת��Ϊint:" + splitted[i + 1]);
				return null;
			}
		}
		return result;

	}

	// ��һ���ַ����ж���ָ����������һ���ַ���������ֵ
	public static String getStringProperty(String sourceStr, String propertyName) {
		sourceStr = sourceStr.trim();
		if (!sourceStr.startsWith("#")) {
			System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ���һ���ַ����� # ��");
			return null;
		}
		String result = sourceStr.substring(1);
		result = result.trim();
		if (!result.startsWith(propertyName)) {
			System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ�ʵ���������ǣ�" + result + "���������������:" + propertyName + " ��");
			return null;
		}
		result = result.substring(propertyName.length());
		return result.trim();
	}

	// ��һ���ַ����ж���ָ���������������ַ����͵�����
	public static String[] getStringProperties(String sourceStr, String propertyName) {
		String[] splitted = split(sourceStr);
		String[] result = new String[splitted.length - 2];
		if (!splitted[0].equalsIgnoreCase("#")) {
			System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName + "������һ���ַ����� # ����ʽ����");
			return null;
		}
		if (!splitted[1].equalsIgnoreCase(propertyName)) {
			System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName + "����������������������������ޣ�");
			return null;
		}
		for (int i = 0; i < splitted.length - 2; i++) {
			result[i] = splitted[i + 2];
		}
		return result;
	}

	public static boolean getBooleanProperty(String sourceStr, String propertyName) {
		String trueOrFalse = getStringProperty(sourceStr, propertyName);
		if (trueOrFalse.equalsIgnoreCase("true")) {
			return true;
		} else if (trueOrFalse.equalsIgnoreCase("false")) {
			return false;
		} else {
			System.out.println("��һ���ַ����ж�ȡ���������ԣ�" + propertyName + "��û�ж��� true ������ false ������");
			return false;
		}
	}

}
