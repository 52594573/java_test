package com.zm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;


/**
 *  1. 给登录用户更新他的 经纬度信息;
 *  2. 根据用户的经纬度 获取附近的人;
 * */


@Service
@Transactional
public class NearUsersService {
	
	@Autowired
	private UserInfoDAO userInfoDao;
	
	
	 static double DEF_PI = 3.14159265359; // PI
     static double DEF_2PI= 6.28318530712; // 2*PI
     static double DEF_PI180= 0.01745329252; // PI/180.0
     static double DEF_R =6370693.5; // radius of earth
     
     
     public void test(){
    	 
    	 
     }
     
     
     
     /**
      *  1. 更新用户的坐标
      * */
     public String updateUserCoordinate( int userId, double lati, double lon ){
    	 
    	 String resultStr = null;
    	 UserInfo userInfo = userInfoDao.findById(userId);
    	 if( lati < 1.000 && lon<1.00 ){
    		 resultStr = "用户坐标错误";
    		 return resultStr;
    	 }
    		 
		 if( userInfo != null ){
    		 
    		 try {
    			 
    			 userInfo.setU_lbs_x( lati );
    			 userInfo.setU_lbs_y( lon );
    			 userInfoDao.update(userInfo);
    			 
    		 }catch( Exception e ){
    			 
    			 resultStr = "数据库存储失败";
    		 }
    		 
    	 }else {
    		 
    		 resultStr = "用户不存在";
    	 } 
    	 return resultStr;
     }
     
     
     /**
      * 2. 获取附近的人
      * */
     
     
     
     
     
     
     
     
     
     //适用于近距离
     public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
     {
         double ew1, ns1, ew2, ns2;
         double dx, dy, dew;
         double distance;
         // 角度转换为弧度
         ew1 = lon1 * DEF_PI180;
         ns1 = lat1 * DEF_PI180;
         ew2 = lon2 * DEF_PI180;
         ns2 = lat2 * DEF_PI180;
         // 经度差
         dew = ew1 - ew2;
         // 若跨东经和西经180 度，进行调整
         if (dew > DEF_PI)
         dew = DEF_2PI - dew;
         else if (dew < -DEF_PI)
         dew = DEF_2PI + dew;
         dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
         dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
         // 勾股定理求斜边长
         distance = Math.sqrt(dx * dx + dy * dy);
         return distance;
     }
	    
     
     
     /* 计算经纬度点对应正方形4个点的坐标 
     * 
     * @param longitude 
     * @param latitude 
     * @param distance 
     * @return 
     */  
    public static Map<String, double[]> returnLLSquarePoint(double longitude,  
            double latitude, double distance) {  
    	
        Map<String, double[]> squareMap = new HashMap<String, double[]>();  
        // 计算经度弧度,从弧度转换为角度  
        double dLongitude = 2 * (Math.asin(Math.sin(distance  
                / (2 * DEF_R))  
                / Math.cos(Math.toRadians(latitude))));  
        dLongitude = Math.toDegrees(dLongitude);  
        // 计算纬度角度  
        double dLatitude = distance / DEF_R;  
        dLatitude = Math.toDegrees(dLatitude);  
        // 正方形  
        double[] leftTopPoint = { latitude + dLatitude, longitude - dLongitude };  
        double[] rightTopPoint = { latitude + dLatitude, longitude + dLongitude };  
        double[] leftBottomPoint = { latitude - dLatitude,  
                longitude - dLongitude };  
        double[] rightBottomPoint = { latitude - dLatitude,  
                longitude + dLongitude };  
        squareMap.put("leftTopPoint", leftTopPoint);  
        squareMap.put("rightTopPoint", rightTopPoint);  
        squareMap.put("leftBottomPoint", leftBottomPoint);  
        squareMap.put("rightBottomPoint", rightBottomPoint);  
        return squareMap;  
    }  

    
    
    /**
     *  从数据库里面获取附近的人
     * */
     public static List  getNearList(){
    	 
    	 
    	 
    	 
    	 
    	 return null;
     } 
     
     
     
}
