package com.ktp.project.constant;

import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumMap {

   public static Map<Integer, ProjectEnum> projectEnumMap = new ConcurrentHashMap<>();

   public static Map<Integer, AuthRealNameApi> subclassMap = new ConcurrentHashMap<>();


   private static final Map<String, Map<Object, Object>> MAP = new HashMap<String, Map<Object, Object>>(){
       {
           /** 南宁项目与开太平项目工种映射 */
           put("GZ_MAP", new HashMap<Object, Object>(){
               {
                   put(19, 4);//4 ⽊⼯
                   put(20, 32);//32 钢筋⼯
                   put(21, 64);//64 混凝⼟⼯
                   put(22, 134);//134 架⼦⼯
                   put(23, 1024);
                   put(24, 1024);//1024 其他
               }
           });

           /** 南宁项目与开太平项目工人角色映射 */
           put("ROLE_MAP", new HashMap<Object, Object>(){
               {
                   put(0, 197);//4 其他管理人员
                   put(4, 1025);//4 建筑工人
                   put(7, 209);//7 总包
                   put(8, 193);//8 班组长
               }
           });

           /** 广州项目与开太平项目工种映射 */
           put("NULL_FIELD", new HashMap<Object, Object>(){
               {
                   put("builderIdcard", RealNameConfig.GZ_SFZ);
                   put("nation", RealNameConfig.GZ_MZ);
                   put("birthday", RealNameConfig.GZ_SFZYXRQ);
                   put("expiryStart", RealNameConfig.GZ_SFZYXRQ);
                   put("headImg", RealNameConfig.GZ_SFZTX);
                   put("phone", RealNameConfig.GZ_SJ);
                   put("cardBank", RealNameConfig.GZ_YHDM);
                   put("workType", RealNameConfig.GZ_GZDM);
                   put("doDate", RealNameConfig.GZ_SFZYXRQ);
                   put("leaderIdcard",RealNameConfig.GZ_SFZ);
                   put("leaderNation",RealNameConfig.GZ_MZ);
                   put("leaderBirthday",RealNameConfig.GZ_SR);
                   put("leaderAddress",RealNameConfig.GZ_SFZZZ);
                   put("leaderSignOrgan",RealNameConfig.GZ_QFJF);
                   put("leaderExpiryStart",RealNameConfig.GZ_SFZYXRQ);
                   put("leaderHeadImg",RealNameConfig.GZ_SFZTX);
                   put("leaderCurrentAddress",RealNameConfig.GZ_JQCZDZ);
                   put("leaderPolitical",RealNameConfig.GZ_ZZMM);
                   put("leaderEducation",RealNameConfig.GZ_WHCD);
                   put("leaderPhone",RealNameConfig.GZ_SJ);
                   put("leaderEmployType",RealNameConfig.GZ_YGXS);
                   put("leaderTechLevel",RealNameConfig.GZ_JNSP);
                   put("leaderSafetyEdu",RealNameConfig.GZ_AQJY);
                   put("leaderDoDate",RealNameConfig.GZ_JCRQ);
                   put("leaderNativePlace",RealNameConfig.GZ_JG);
                   put("leaderMaritalStatus",RealNameConfig.GZ_HYZT);
                   put("leaderCardBank",RealNameConfig.GZ_YHDM);
//                   put("accessNo",RealNameConfig.GZ_ACCESSNO);
                   put("leaderWorkType",RealNameConfig.GZ_BZLX);

               }
           });

           /** 山水项目返回结果映射 */
           put("SS_RES", new HashMap<Object, Object>(){
               {
                   put("0", "请求成功");
                   put("1", "请求接口数据必填字段缺失");
                   put("2", "数据处理时出错");
                   put("3", "服务器应答超时");
                   put("4", "pass授权失败");
                   put("5", "人员已经添加");
                   put("6", "考勤设备不存在");
                   put("7", "人员不存在");
                   put("8", "下发的图片格式或内容不正确");
                   put("9", "企业不存在");
                   put("10", "项目不存在");
                   put("11", "身份证号已存在");
                   put("12", "提交的字段缺失");
                   put("13", "IC卡已被绑定");
               }
           });
       }
   };

   public static Object getValueByKey(String keyOne, Object keyTwo, String defaultValue){
       if (StringUtils.isBlank(keyOne) || keyTwo == null){
           throw new BusinessException("KEY不能为空");
       }
       return MAP.get(keyOne) == null ? defaultValue : ( MAP.get(keyOne).get(keyTwo) == null ? defaultValue : MAP.get(keyOne).get(keyTwo));
   }

   public static Integer getValueByKey(String keyOne, Object keyTwo){
       String value = String.valueOf(getValueByKey(keyOne, keyTwo, "-1"));
       return Integer.parseInt(value);
   }

    public static void main(String[] args) {
        Integer gz_map = Integer.parseInt( String.valueOf(getValueByKey("ROLE_MAP", 0)) );
        System.out.println(gz_map);
        AuthRealNameApi authRealNameApi = subclassMap.get(174);
        System.out.println(authRealNameApi.getpSent());
        Map<Integer, AuthRealNameApi> subclassMap = EnumMap.subclassMap;
        AuthRealNameApi authRealNameApi1 = subclassMap.get(3309);
        System.out.println(authRealNameApi1.getClass());
    }
}
