package com.ktp.project.util;

import com.ktp.project.constant.RealNameConfig;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ZHSocketSend {
    /**
    *
    * @Description: 组装格式
    * @Author: liaosh
    * @Date: 2019/1/21 0021
    */
    public static byte[] reByte(byte[] content,int command){
        byte[] Header = {1};
        byte[] Length = ByteUtil.convertByteArray(content.length);
        byte[] PartIndex =ByteUtil.convertByteArray(0);
        byte[] PartCount =ByteUtil.convertByteArray(0);
        byte[] Version ={1};
        byte[] Command =ByteUtil.shortToByte((short)command);
        byte[] SessionID =ByteUtil.hexStringToByte(RealNameConfig.ZS_API_KEY);
        byte[] Content =content;
        byte[] Flag ={0};
        byte[] Tail = {1};

        return ByteUtil.byteMergerAll(Header,Length,PartIndex,PartCount,Version,Command,SessionID,Content,Flag,Tail);
    }
    //考勤数据 848
    public static int[] datatobyte(Integer usercode,String kaoqinTime,String pictureUrl){
        byte[] userId = ByteUtil.convertByteArray(usercode);
        byte[] credit_time = BCDUtils.str2Bcd(kaoqinTime);
        //byte[] credit_model = ByteUtil.convertByteArray(6);
        //byte model = 6;
        byte[] credit_model = {6};
        byte[] picture = pictureToByte(pictureUrl);
        byte[] picture_length = ByteUtil.convertByteArray(picture.length);
        byte Xor = getXor(ByteUtil.byteMergerAll(userId,credit_time,credit_model,picture_length,picture));
        byte[] cc = {Xor};
        return javaByteTo(reByte(ByteUtil.byteMergerAll(userId,credit_time,credit_model,picture_length,picture,cc),848));
        //return ByteUtil.byteMergerAll(userId,credit_time,credit_model,picture_length,picture,cc);
    }
    //843数据
    public static byte[] set843(){
        byte[] headingCode = ByteUtil.stringToByte(RealNameConfig.ZHZH_MANUFACTURERS_MUN);
        byte[] logoCode  = ByteUtil.stringToByte(RealNameConfig.ZHZH_EQUIPMENT_MUN);
        byte Xor = getXor(ByteUtil.byteMergerAll(headingCode,logoCode));
        byte[] cc = {Xor};
        return ByteUtil.byteMergerAll(headingCode,logoCode,cc);
    }
    //获取考勤接口数据
   /* public static byte[] getKaoqin(){
        return reByte(datatobyte(),845);
    }*/
    //获取843接口数据
    public static int[] get843(){
        return javaByteTo(reByte(set843(),843));
    }
    /**
    *
    * @Description:  通过图片url转成byte[]
    * @Author: liaosh
    * @Date: 2019/1/21 0021
    */
    public static byte[] pictureToByte(String path){
        try {
        //String path = "https://images.ktpis.com/pic1.png";
        URL url = new URL(path);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();
        InputStream inputStream1 = httpUrl.getInputStream();
        return IOUtils.toByteArray(inputStream1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
    *
    * @Description: Xor校验运算
    * @Author: liaosh
    * @Date: 2019/1/21 0021
    */
    public static byte getXor(byte[] datas){
        byte temp=datas[0];
        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
        }
        return temp;
    }

    /**
    *
    * @Description: java字节数组转为c#字节数组
    * @Author: liaosh
    * @Date: 2019/1/22 0022
    */
    public static int[] javaByteTo(byte[] bytes){
        int data[] = new int[bytes.length];
        for(int i=0;i<bytes.length;i++){
            data[i] = bytes[i] & 0xff;
        }
        return data;
    }
    /** 
    *
    * @Description: 845
    * @Author: liaosh
    * @Date: 2019/1/22 0022 
    */
    public static int[] getUser845(String sfz1){
        byte[] logoCode  = ByteUtil.stringToByte(RealNameConfig.ZHZH_EQUIPMENT_MUN);
        byte[] sfz  = ByteUtil.stringToByte(sfz1);
        byte Xor = getXor(ByteUtil.byteMergerAll(logoCode,sfz));
        byte[] cc = {Xor};
       /* System.out.println("logoCode r长度："+logoCode.length);
        System.out.println("sfz r长度："+sfz.length);
        System.out.println("cc r长度："+cc.length);*/
        return javaByteTo(reByte(ByteUtil.byteMergerAll(logoCode,sfz,cc),845));
    }

    /**
    *
    * @Description: 心跳包
    * @Author: liaosh
    * @Date: 2019/1/22 0022
    */
    public static  int[] heartBeat(){
        byte [] bb = {};
        return javaByteTo(reByte(ByteUtil.byteMergerAll(bb),65535));
        //return reByte(bb);
    }

    public static void main(String[] args) {
        try {
            int[] aa = get843();
            for (int i = 0; i < aa.length; i++) {
                System.out.print(aa[i]+" ");
            }
        }catch (Exception e){

        }

    }
}
