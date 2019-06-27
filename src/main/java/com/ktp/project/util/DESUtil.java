package com.ktp.project.util;

import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.exception.BusinessException;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;


public class DESUtil {

    public static String DESEncode(String data, Integer type) {
        byte[] encrypt = new byte[0];
        try {
            switch (type){
                case 1:
                    encrypt = encrypt(data, RealNameConfig.JMP174_KEY);
                    break;
                case 2:
                    encrypt = encrypt(data, RealNameConfig.GZ_KEY);break;
                case 3:
                    encrypt = encrypt(data, RealNameConfig.GSX3223_KEY);break;
                default:
                    throw new BusinessException("加密类型不匹配");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("字符串:%s,DES加密失败", data));
        }
        return toHex16Str(encrypt);
    }

    /**
     * des加密
     * @param data 加密参数
     * @param key 加密秘钥
     * @return
     */
    public static String DESEncode(String data, String key) {
        byte[] encrypt = new byte[0];
        try {
            encrypt = encrypt(data, key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("字符串:%s,DES加密失败", data));
        }
        return toHex16Str(encrypt);
    }

    /**
     * des解密
     * @param data
     * @param key
     * @return
     */
    public static String DESDecrypt(String data, String key) {
        String result = null;
        try {
            result = decrypt(data, key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("字符串:%s,解密失败!", data));
        }
        return result;
    }

    /**
     * 加密
     * @param srcStr
     * @param charset
     * @param sKey
     * @return
     */
    public static String encrypt(String srcStr, Charset charset, String sKey) {
        byte[] src = srcStr.getBytes(charset);
        byte[] buf = encrypt(src, sKey);
        return toHex16Str(buf);
    }

    /**
     * 解密
     *
     * @param hexStr
     * @param sKey
     * @return
     * @throws Exception
     */
    private static String decrypt(String hexStr, String sKey) throws Exception {
        byte[] src = parseHexStr2Byte(hexStr);
        byte[] buf = decrypt(src, sKey);
        return new String(buf, Charset.forName("utf-8"));
    }

    /**
     * 加密
     * @param data
     * @param sKey
     * @return
     */
    private static byte[] encrypt(byte[] data, String sKey) {
        try {
            byte[] key = sKey.getBytes("UTF-8");
            // 初始化向量
            IvParameterSpec iv = new IvParameterSpec(key);
            DESKeySpec desKey = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 广州项目DES加密
     * @param data
     * @param sKey
     * @return
     */
    public static byte[] GZDES(byte[] data, String sKey) {
        try {
            byte[] key = sKey.getBytes("UTF-8");
            // 初始化向量
//            IvParameterSpec iv = new IvParameterSpec(key);
            DESKeySpec desKey = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param src
     * @param sKey
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String sKey) throws Exception {
        byte[] key = sKey.getBytes("UTF-8");
        // 初始化向量
        IvParameterSpec iv = new IvParameterSpec(key);
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key);
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    private static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   //这里指定了CBC模式. 如果是Cipher.getInstance("DES")则是EBC模式
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }


    /**
     * 数组转成十六进制字符串
     *
     * @param b
     * @return HexString
     */
    public static String toHex16Str(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString().toUpperCase();
    }

    private static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }



    public static void main(String[] args) throws Exception {
        System.out.println(toHex16Str(GZDES("410422196602017656".getBytes(),"8c2da4c769828fcfa77aedb690999cf9")).toLowerCase());
//80cfe03525bb2b8d43d62ff369e95334cd1facfe4bbb800c

    }
}
