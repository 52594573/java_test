package com.ktp.project.web;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * User: dpj
 * Date: 13-10-17
 * Time: 下午3:19
 */
public class AesUntil  {
    public final static String UTF_8="UTF-8";
    /**
     * AES 加密
     *
     * @param sSrc  加密内容 必填 ( 必须为UTF_8)
     * @param sKey 密钥 必填
     * @return 成功或失败或异常信息
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            return null;
        }
        byte[] seed = sKey.getBytes("UTF8");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        // 设置一个种子,一般是用户设定的密码
        sr.setSeed(seed);
        // 获得一个key生成器（AES加密模式）
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        // 设置密匙长度128位
        keyGen.init(128, sr);
        // 获得密匙
        SecretKey key = keyGen.generateKey();
        // 返回密匙的byte数组供加解密使用
        byte[] raw = key.getEncoded();

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("dccf770eec9e4fe6".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(UTF_8));
        return Base64.encodeBase64String(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。

    }
    public static void main(String[] args) throws Exception {
        String str = encrypt("130434198508160074","dccf770eec9e4fe6b4949def388dc68c");
        System.out.println("str:"+str);
    }
}