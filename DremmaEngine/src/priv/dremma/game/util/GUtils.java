package priv.dremma.game.util;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * 静态工具类
 *
 */
public final class GUtils {
    public static final int INT_ERROR = -2147483648;
    /**
     * 把一个整型数组扩充指定的大小。
     * @参数 oldArray 被扩充的数组
     * @参数 expand 扩充的大小
     * @返回 一个新数组，旧数组被拷贝在其中开始部分，还剩下一些空间用于容纳更多元素，
     */
    public final static int[] expandArray(int[] oldArray, int expand) {
        int[] newArray = new int[oldArray.length + expand];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }

    /**
     * 把一个二维图像数组的行数扩大指定大小。
     * @参数 oldArray 要扩大的数组；
     * @参数 expand 扩大的数目；
     * @返回 一个新数组。
     */
    public final static Image[][] expandArray(Image[][] oldArray, int expand) {
        Image[][] newArray = new Image[oldArray.length + expand][];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }

    /********************************************************************************************
     ****** ****************视口坐标、世界像素坐标和世界tile坐标之间的转换方法**********************
     *******************************************************************************************/
    /**
     * 视口坐标转换成世界像素坐标.
     * @param vx: 视口坐标x；
     * @param vy: 视口坐标y；
     * @param basePointX: 视口左上角的世界x像素坐标；
     * @param basePointY: 视口左上角的世界y像素坐标；
     * return 世界像素坐标.
     **/
    public static int[] viewPortToWorldPixel(int vx, int vy, int basePointX,
                                             int basePointY) {
        int[] piexl = new int[2];
        piexl[0] = vx + basePointX;
        piexl[1] = vy + basePointY;
        return piexl;        
        
    }

    /**
     ** 世界像素坐标转换成视口坐标.
     * @param x: 世界像素坐标x；
     * @param y: 世界像素坐标y；
     * @param basePointX: 视口左上角的世界x像素坐标；
     * @param basePointY: 视口左上角的世界y像素坐标；
     * return 视口坐标.
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
     * 世界像素坐标转换成世界tile坐标.
     * @ param x: 世界像素x坐标；
     * @ param y: 世界像素y坐标；
     * @ renturn 世界tile坐标。
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
     * 视口坐标转换成世界tile坐标.
     * @param vx: 视口坐标x；
     * @param vy: 视口坐标y；
     * @param basePointX: 视口左上角的世界x像素坐标；
     * @param basePointY: 视口左上角的世界y像素坐标；
     * return 世界tile坐标.
     **/
    public static int[] viewPortToWorldTile(int vx, int vy,
                                            int basePointX, int basePointY,
                                            int tileWidth, int tileHeight) {
        int[] worldPiexl = new int[2];
        worldPiexl = viewPortToWorldPixel(vx, vy, basePointX, basePointY);
        return worldPixelToWorldTile(worldPiexl[0], worldPiexl[1], tileWidth, tileHeight );
    }

    /**
     * 世界某个tile的中心点转换成世界像素坐标.
     * @param x: 世界tile坐标x；
     * @param y: 世界tile坐标y；
     * @param basePointX: 视口左上角的世界x像素坐标；
     * @param basePointY: 视口左上角的世界y像素坐标；
     * return 视口坐标.
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
     * 世界某个tile的中心点转换成视口坐标.
     * @param x: 世界tile坐标x；
     * @param y: 世界tile坐标y；
     * @param basePointX: 视口左上角的世界x像素坐标；
     * @param basePointY: 视口左上角的世界y像素坐标；
     * return 视口坐标.
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

    //斜坐标转换成游戏世界的tile的直坐标
    //在斜坐标系中，从原点沿x方向走到中间点，它的x坐标与目标点的x坐标相同，
    //这个中间点，在直坐标系中相对与原点的坐标为mx和my；然后从中间点出发沿着斜坐标系的y轴向目标点前进。
    //在此旅途中顺序经过的点的直坐标表现出很强的规律性，据此我们得到目标点相对与原点的直坐标。
    public static int[] slantTileToWorldTile(int originX, int originY, int tx, int ty) {
        int[] tileXY = new int[2];
        int mx = originX + (originY % 2 + tx) / 2;
        int my = originY + tx;
        tileXY[0] = mx - ( (my + 1) % 2 + ty) / 2;
        tileXY[1] = my + ty;
        return tileXY;
    }

    //把一个世界tile的直坐标（destX,destY）转换成以另一个tile坐标（oringX,oringY）为原点的坐标系
    public static int[] worldTileToSlantTile(int oringX, int oringY, int destX, int destY) {
        int[] destination = new int[2];
        int tempX, tempY ;
        int distantY = destY - oringY;
        //在直坐标系中，从原点在x方向上走到中间点，它的x坐标与目标点的x坐标相同；
        //这个中间点，在斜坐标系中的相对与原点的坐标为下面的tempX和tempY。
        tempX = destX - oringX;
        tempY = -tempX;
        //从中间点出发，沿着直角坐标系的y轴，向目标点前进；
        //在这个旅途中顺序经过的点的斜坐标表现出很强的规律行:分两种情况：
        //向下走：首先第一步时x坐标加1，第二步y坐标加1，如此交替，直到x坐标与y坐标之和为distantY，即到达目标点。
        //这样，目标点的斜坐标自然就得到了。
        //向上走：首先第一步时y坐标减1，第二步x坐标减1，如此交替，直到x坐标与y坐标之和为distantY，即到达目标点。
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
     ********************读写文本的工具************************************************
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
            System.out.println("读文本时发生异常！！！");
        }
        if (s != null) {
            return s.toString();
        }
        else {
            return "endOfFile";
        }
    }

    //对一个Sting进行分割，分割的界线为一个或多个空格苻' '。
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
    //在一行字符串中读出指定属性名的一个字符串型属性值
    public static int getIntProperty(String sourceStr, String propertyName) {
	sourceStr = sourceStr.trim();
	if (!sourceStr.startsWith("#")) {
	    System.out.println("在一行字符串中读取字符串型的属性：第一个字符不是 # ！");
	}
	String result = sourceStr.substring(1);
	result = result.trim();
	if (!result.startsWith(propertyName)) {
	    System.out.println("在一行字符串中读取字符串型的属性：实际属性名是："
			       + result + "！与输入参数不符:" + propertyName + " ！");
	}
	result = result.substring(propertyName.length());
	result = result.trim();
	return Integer.parseInt(result);
    }

    //在一行字符串中读出指定属性名的若干整型属性值
    public static int[] getIntProperties(String sourceStr, String propertyName,
                                      int num) {
        String[] splitted = split(sourceStr);
        if (splitted.length != num + 2) {
            System.out.println("在一行字符串中读取整型属性（" + propertyName +
                               "）：元素的个数不对！");
            return null;
        }
        int[] result = new int[num];
        if (!splitted[0].equalsIgnoreCase("#")) {
            System.out.println("在一行字符串中读取整型属性（" + propertyName +
                               "）：第一个字符不是 # ！格式出错");
            return null;
        }
        if (!splitted[1].equalsIgnoreCase(propertyName)) {
            System.out.println("在一行字符串中读取整型属性（" + propertyName +
                               "）：读出的属性名与输入参数不苻！");
            return null;
        }
        for (int i = 0; i < num; i++) {
            try {
                result[i] = Integer.parseInt(splitted[i + 2]);
            }
            catch (NumberFormatException e) {
                System.out.println("在一行字符串中读取属性（" + propertyName +
                                   "）：字符串不能转换为int:" + splitted[i + 1]);
                return null;
            }
        }
        return result;

    }

    //在一行字符串中读出指定属性名的一个字符串型属性值
    public static String getStringProperty(String sourceStr, String propertyName) {
        sourceStr = sourceStr.trim();
        if (!sourceStr.startsWith("#")) {
            System.out.println("在一行字符串中读取字符串型的属性：第一个字符不是 # ！");
            return null;
        }
        String result = sourceStr.substring(1);
        result = result.trim();
        if (!result.startsWith(propertyName)) {
            System.out.println("在一行字符串中读取字符串型的属性：实际属性名是："
                               + result + "！与输入参数不符:" + propertyName + " ！");
            return null;
        }
        result = result.substring(propertyName.length());
        return result.trim();
    }
    //在一行字符串中读出指定属性名的若干字符串型的属性
    public static String[] getStringProperties( String sourceStr, String propertyName ){
	String[] splitted = split(sourceStr);
	String[] result = new String[splitted.length-2];
	if (!splitted[0].equalsIgnoreCase("#")) {
	    System.out.println("在一行字符串中读取整型属性（" + propertyName +
			       "）：第一个字符不是 # ！格式出错");
	    return null;
	}
	if (!splitted[1].equalsIgnoreCase(propertyName)) {
	    System.out.println("在一行字符串中读取整型属性（" + propertyName +
			       "）：读出的属性名与输入参数不苻！");
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
            System.out.println("在一行字符串中读取布尔型属性（" + propertyName +
                               "）没有读到 true 或者是 false ！！！");
            return false;
        }
    }

}
