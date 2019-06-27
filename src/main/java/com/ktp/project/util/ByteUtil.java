package com.ktp.project.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {
    /** 
    *
    * @Description: int 转为byte[] 
    * @Author: liaosh
    * @Date: 2019/1/21 0021 
    */ 
    public static byte[] intToBytes2(int n){
        byte[] b = new byte[4];

        for(int i = 0;i < 4;i++)
        {
            b[i]=(byte)(n>>(24-i*8));

        }
        return b;
    }
    /**
    * 低位在前高位在后
    * @Description:
    * @Author: liaosh
    * @Date: 2019/1/22 0022
    */
    public static byte[] convertByteArray(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;

    }

    
    /** 
    *
    * @Description:  把16进制字符串转换成字节数组
    * @Author: liaosh
    * @Date: 2019/1/21 0021 
    */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
    *
    * @Description: 字符串转byte[]
    * @Author: liaosh
    * @Date: 2019/1/22 0022
    */
    public static byte[] stringToByte(String str){
        return str.getBytes();
    }

    /**
     * 注释：short到字节数组的转换！
     *
     * @param
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();//
                    temp = temp >> 8; // 向右移8位
        }
        return b;
    }

   /** 
   *
   * @Description: 多个byte[]合并
   * @Author: liaosh
   * @Date: 2019/1/21 0021 
   */ 
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    /**
     *
     * @Description: byte[]转为int
     * @Author: liaosh
     * @Date: 2019/1/22 0022
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        try {
            System.arraycopy(src, begin, bs, 0, count);
        } catch (Exception e) {
            e.printStackTrace();
            return bs;
        }
        return bs;
    }

    /**
     *
     * @Description: byte[]转short
     * @Author: liaosh
     * @Date: 2019/1/22 0022
     */
    public static short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }
    /**
     *byte转int
     */
    public static int byte2Int(byte b){
        return ((b & 0xff));
    }
}
