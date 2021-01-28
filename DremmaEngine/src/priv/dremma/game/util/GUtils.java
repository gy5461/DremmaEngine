package priv.dremma.game.util;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * ��̬������
 *
 */
public final class GUtils {
    public static final int INT_ERROR = -2147483648;
    /**
     * ��һ��������������ָ���Ĵ�С��
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
     * @param vx: �ӿ�����x��
     * @param vy: �ӿ�����y��
     * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
     * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ
     * return ������������.
     **/
    public static int[] viewPortToWorldPixel(int vx, int vy, int basePointX,
                                             int basePointY) {
        int[] piexl = new int[2];
        piexl[0] = vx + basePointX;
        piexl[1] = vy + basePointY;
        return piexl;        
        
    }

    /**
     ** ������������ת�����ӿ�����.
     * @param x: ������������x��
     * @param y: ������������y��
     * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
     * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ
     * return �ӿ�����.
     *
     **/
    public static int[] worldPixelToViewPort(int tx, int ty, int basePointX,
                                             int basePointY) {
        int[] view = new int[2];
        view[0] = tx - basePointX;
        view[1] = ty - basePointY;
        return view;
    }

    /**
     * ������������ת��������tile����.
     * @ param x: ��������x���ꣻ
     * @ param y: ��������y���ꣻ
     * @ renturn ����tile���ꡣ
     **/
    public static int[] worldPixelToWorldTile(int x, int y, int tileWidth, int tileHeight ) {
        int Atx, Aty, centerx, centery;
        if (x >= 0) {
            Atx = x / tileWidth;
        }
        else {
            Atx = x / tileWidth - 1;
        }
        if (y >= 0) {
            Aty = ( (y / tileHeight) << 1) + 1;
        }
        else {
            Aty = ( (y / tileHeight) << 1) - 1;
        }

        centerx = Atx * tileWidth + (tileWidth >> 1);

        centery = (Aty >> 1) * tileHeight + (tileHeight >> 1);
        centery = centery << 1;
        y = y << 1;

        centerx = x - centerx;
        centery = y - centery;

        if (centerx >= 0) {
            if (centery >= 0) {
                return (centerx + centery) > tileHeight ? new int[] {
                    Atx + 1, Aty + 1}
                    : new int[] {
                    Atx, Aty};
            }
            else {
                return (centerx - centery) > tileHeight ? new int[] {
                    Atx + 1, Aty - 1}
                     : new int[] {
                    Atx, Aty};
            }
        }
        else {
            if (centery >= 0) {
                return (centery - centerx) > tileHeight ? new int[] {
                    Atx, Aty + 1}
                     : new int[] {
                    Atx, Aty};
            }
            else {
                return ( -centerx - centery) > tileHeight ? new int[] {
                    Atx, Aty - 1}
                     : new int[] {
                    Atx, Aty};
            }
        }
    }

    /**
     * �ӿ�����ת��������tile����.
     * @param vx: �ӿ�����x��
     * @param vy: �ӿ�����y��
     * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
     * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ
     * return ����tile����.
     **/
    public static int[] viewPortToWorldTile(int vx, int vy,
                                            int basePointX, int basePointY,
                                            int tileWidth, int tileHeight) {
        int[] worldPiexl = new int[2];
        worldPiexl = viewPortToWorldPixel(vx, vy, basePointX, basePointY);
        return worldPixelToWorldTile(worldPiexl[0], worldPiexl[1], tileWidth, tileHeight );
    }

    /**
     * ����ĳ��tile�����ĵ�ת����������������.
     * @param x: ����tile����x��
     * @param y: ����tile����y��
     * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
     * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ
     * return �ӿ�����.
     **/
    public static Vector2 worldTileCenterToWorldPixel( int tx, int ty,
                                                     int tileWidth, int tileHeight) {
    	Vector2 piexl = Vector2.zero;
        if (ty % 2 == 0) {
            piexl.x = tx * tileWidth;
            piexl.y = ty * (tileHeight/2);
            return piexl;
        }
        else {
            piexl.x = tx * tileWidth + tileWidth/2;
            piexl.y = ty * (tileHeight/2);
            return piexl;
        }
    }

    /**
     * ����ĳ��tile�����ĵ�ת�����ӿ�����.
     * @param x: ����tile����x��
     * @param y: ����tile����y��
     * @param basePointX: �ӿ����Ͻǵ�����x�������ꣻ
     * @param basePointY: �ӿ����Ͻǵ�����y�������ꣻ
     * return �ӿ�����.
     **/
    public static int[] worldTileCenterToViewPort(int tx, int ty,
                                                  int basePointX, int basePointY,
                                                  int tileWidth, int tileHeight ) {
        int[] piexl = new int[2];
        if (ty % 2 == 0) {
            piexl[0] = tx * tileWidth - basePointX;
            piexl[1] = ty * (tileHeight/2) - basePointY;
            return piexl;
        }
        else {
            piexl[0] = tx * tileWidth + (tileWidth/2) - basePointX;
            piexl[1] = ty * (tileHeight/2) - basePointY;
            return piexl;
        }
    }

    //б����ת������Ϸ�����tile��ֱ����
    //��б����ϵ�У���ԭ����x�����ߵ��м�㣬����x������Ŀ����x������ͬ��
    //����м�㣬��ֱ����ϵ�������ԭ�������Ϊmx��my��Ȼ����м���������б����ϵ��y����Ŀ���ǰ����
    //�ڴ���;��˳�򾭹��ĵ��ֱ������ֳ���ǿ�Ĺ����ԣ��ݴ����ǵõ�Ŀ��������ԭ���ֱ���ꡣ
    public static int[] slantTileToWorldTile(int originX, int originY, int tx, int ty) {
        int[] tileXY = new int[2];
        int mx = originX + (originY % 2 + tx) / 2;
        int my = originY + tx;
        tileXY[0] = mx - ( (my + 1) % 2 + ty) / 2;
        tileXY[1] = my + ty;
        return tileXY;
    }

    //��һ������tile��ֱ���꣨destX,destY��ת��������һ��tile���꣨oringX,oringY��Ϊԭ�������ϵ
    public static int[] worldTileToSlantTile(int oringX, int oringY, int destX, int destY) {
        int[] destination = new int[2];
        int tempX, tempY ;
        int distantY = destY - oringY;
        //��ֱ����ϵ�У���ԭ����x�������ߵ��м�㣬����x������Ŀ����x������ͬ��
        //����м�㣬��б����ϵ�е������ԭ�������Ϊ�����tempX��tempY��
        tempX = destX - oringX;
        tempY = -tempX;
        //���м�����������ֱ������ϵ��y�ᣬ��Ŀ���ǰ����
        //�������;��˳�򾭹��ĵ��б������ֳ���ǿ�Ĺ�����:�����������
        //�����ߣ����ȵ�һ��ʱx�����1���ڶ���y�����1����˽��棬ֱ��x������y����֮��ΪdistantY��������Ŀ��㡣
        //������Ŀ����б������Ȼ�͵õ��ˡ�
        //�����ߣ����ȵ�һ��ʱy�����1���ڶ���x�����1����˽��棬ֱ��x������y����֮��ΪdistantY��������Ŀ��㡣
        if (distantY > 0) {
            tempX += distantY >> 1;
            tempY += distantY >> 1;
        } else {
            tempX += (distantY + 1) >> 1;
            tempY += (distantY + 1) >> 1;
        }
        if ( ( (destY - oringY) & 1) != 0) {
            if (destY > oringY) {
                tempX += 1 - (oringY & 1);
                tempY += oringY & 1;
            } else {
                tempX -= oringY & 1;
                tempY -= 1 - (oringY & 1);
            }
        }
        destination[0] = tempX;
        destination[1] = tempY;

        return destination;
    }

    /* ******************************************************************************
     ********************��д�ı��Ĺ���************************************************
     ******************************************************************************/

    /**
     * read a line from the "reader", and the next char to be read
     * after the call of this method will start from the front of a new line.
     * @param s String
     * @param writer Writer
     * @throws IOException
     */
    @SuppressWarnings("unused")
	public static String readLine(Reader reader) {
        int c;
        StringBuffer s = new StringBuffer();
        try {
            while ( ( (c = reader.read()) != -1)) {
                if (c != 13) {
                    s.append( (char) c);
                }
                else {
                    reader.read();
                    break;
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("���ı�ʱ�����쳣������");
        }
        if (s != null) {
            return s.toString();
        }
        else {
            return "endOfFile";
        }
    }

    //��һ��Sting���зָ�ָ�Ľ���Ϊһ�������ո���' '��
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
    //��һ���ַ����ж���ָ����������һ���ַ���������ֵ
    public static int getIntProperty(String sourceStr, String propertyName) {
	sourceStr = sourceStr.trim();
	if (!sourceStr.startsWith("#")) {
	    System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ���һ���ַ����� # ��");
	}
	String result = sourceStr.substring(1);
	result = result.trim();
	if (!result.startsWith(propertyName)) {
	    System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ�ʵ���������ǣ�"
			       + result + "���������������:" + propertyName + " ��");
	}
	result = result.substring(propertyName.length());
	result = result.trim();
	return Integer.parseInt(result);
    }

    //��һ���ַ����ж���ָ����������������������ֵ
    public static int[] getIntProperties(String sourceStr, String propertyName,
                                      int num) {
        String[] splitted = split(sourceStr);
        if (splitted.length != num + 2) {
            System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName +
                               "����Ԫ�صĸ������ԣ�");
            return null;
        }
        int[] result = new int[num];
        if (!splitted[0].equalsIgnoreCase("#")) {
            System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName +
                               "������һ���ַ����� # ����ʽ����");
            return null;
        }
        if (!splitted[1].equalsIgnoreCase(propertyName)) {
            System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName +
                               "����������������������������ޣ�");
            return null;
        }
        for (int i = 0; i < num; i++) {
            try {
                result[i] = Integer.parseInt(splitted[i + 2]);
            }
            catch (NumberFormatException e) {
                System.out.println("��һ���ַ����ж�ȡ���ԣ�" + propertyName +
                                   "�����ַ�������ת��Ϊint:" + splitted[i + 1]);
                return null;
            }
        }
        return result;

    }

    //��һ���ַ����ж���ָ����������һ���ַ���������ֵ
    public static String getStringProperty(String sourceStr, String propertyName) {
        sourceStr = sourceStr.trim();
        if (!sourceStr.startsWith("#")) {
            System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ���һ���ַ����� # ��");
            return null;
        }
        String result = sourceStr.substring(1);
        result = result.trim();
        if (!result.startsWith(propertyName)) {
            System.out.println("��һ���ַ����ж�ȡ�ַ����͵����ԣ�ʵ���������ǣ�"
                               + result + "���������������:" + propertyName + " ��");
            return null;
        }
        result = result.substring(propertyName.length());
        return result.trim();
    }
    //��һ���ַ����ж���ָ���������������ַ����͵�����
    public static String[] getStringProperties( String sourceStr, String propertyName ){
	String[] splitted = split(sourceStr);
	String[] result = new String[splitted.length-2];
	if (!splitted[0].equalsIgnoreCase("#")) {
	    System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName +
			       "������һ���ַ����� # ����ʽ����");
	    return null;
	}
	if (!splitted[1].equalsIgnoreCase(propertyName)) {
	    System.out.println("��һ���ַ����ж�ȡ�������ԣ�" + propertyName +
			       "����������������������������ޣ�");
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
        }
        else if (trueOrFalse.equalsIgnoreCase("false")) {
            return false;
        }
        else {
            System.out.println("��һ���ַ����ж�ȡ���������ԣ�" + propertyName +
                               "��û�ж��� true ������ false ������");
            return false;
        }
    }

}
