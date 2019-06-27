package com.ktp.project.util;
/** 
*
* @Description: 很多图片路径要添加不同前缀  
* @Author: liaosh
* @Date: 2018/12/19 0019 
*/ 

public class KTPImgUtil {
    private static final String HEAD  = "https://t.ktpis.com";

    private static final String NO_HEAD = "https://images.ktpis.com/images/pic/20181122154936224290102181.png";

    public static String getHead(String head,Integer type){
        if(head!=null && !"".equals(head)){
            if(type==1){
                 head = KTPImgUtil.HEAD + head;
            }
        }else{
            return KTPImgUtil.NO_HEAD;
        }
        return head;
    }

}
