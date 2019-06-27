package com.ktp.project.util;

import com.ktp.project.pay.weixin.WeixinpayCfg;
import com.ktp.project.pay.weixin.response.WeixinpayRefundAyncNotify;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Map;

/**
 * @author djcken
 * @date 2018/5/22
 */
public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";
    /**
     * 生成key
     */
    private static SecretKeySpec key = new SecretKeySpec(Md5Util.get(WeixinpayCfg.merchant_key, StandardCharsets.UTF_8).toLowerCase().getBytes(), ALGORITHM);

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * AES解密
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data) {

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64Util.decode(base64Data)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        //解密
        String req_info = "6g5PVzhPHV3CeRXXh9hpBmqqxtjSYOiKnzAm23HuxVzarC9Etz83MehWQvpcb5HigVQirkn8XHdDuY+6RQp7rCLbuPQ7S4rUMTsS3pf3IpEizx5lTUZbQIjxtdz8+2//W3E4S0uMbI0iUBh5ViB/Aa7NbTRPqPD16TqFE5GurtTIbNXB/XeklVQlQ+gJnSDRB9mOUuzq4Nt/64UrpYsv7ny8OGcLmJcYPKOj/nQwFWprBrCR34ZhWI9BoPZ62N4A4ZG6J2VtxoH5J/Ev5mjhds2nCq+IN5s5ySz6sTBwi0pxKoPOl47KeYZoznsA9onE4qt8RELiIs97FWkrAIxOMD/AFsllX/PdkGcbfU0eie5XgqdNzLHd3oQqEpPmIONPvZqK151ATKXSqVIylT37UACVmq2EJZXI96tV+OW4CHhWoBiZ94vo2utPmXsxcVOnMiIuXlbe6IjfynLwI/xHEE/esQO8G6+Odvf3CZ0xhqqhkzAlj181bH/LMUlOMi0/RbPh/ysoLqBOJyvCM5N4eJcxIiDMz4cejBt1iptUXdlsQ/e6x3TEY+88CbdKpBnXIdH5oiZRBuMfk09X/DME+cB8rm/toZdUn/m8Lg45VbOQFPF8PlHMMlhZwpe6zTaQetcSOA8hYPhl8xvdfvNyIPOvMNb6tPSKpdfhipCZzZHubdNxAki5mfcv7psXD9KT7wUzoRnBOWQ9effislvVEbQfPZtvoGHFP1bvXUSCI1v6izoAWl48E9q7UXZHmaqTrhV2SXMLya0ceqWAGaOGtyFI98n5rEEAibOl9Lsk1Q7SXbwDEBrGKuZEWvGz31UhiwxxdFy8QC9SNnM9tmMsZ5f6zIjLR0UoE34d1l5uk5YCTmrzurhjEMV7NPl4cA+s9B8s4mtd+o7xIESjLqTtOjEYkWfkKto3VoiqXWB90QnZDmZ6uoAXhN1WId9qHer8XJ3e3/8SbNDIWEN8AOHhc65xiA0UPJCzXsMn/kOr+Hs0KlcxNWq3BtQQvGuvC13bfH33utGAAxHZMl2hCj8TwojDYfL9H7i/1qYhHlxosELplnElrhjmPp9AFl+9WHtS";


        Security.addProvider(new BouncyCastleProvider());
        String B = AESUtil.decryptData(req_info);
        Map<String, Object> response = XmlUtil.xmlStrToMap(B);
        WeixinpayRefundAyncNotify notify = (WeixinpayRefundAyncNotify) XmlUtil.xmlStrToBean(B, WeixinpayRefundAyncNotify.class);
        System.out.println("bbbbbbbbb\n" + B);
        System.out.println("ccccccccc\n" + response);
        System.out.println("ddddddddd\n" + notify);
    }

}
