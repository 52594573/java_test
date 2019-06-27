package com.ktp.project.service.realName;

import com.ktp.project.constant.RealNameEnum;

public interface AuthRealNameApi {

    //同步工人考勤信息
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId) throws Exception;

    /**\
     * 同步班组信息
     * @param projectId 项目id
     * @param userId 用户id
     * @param type  类型
     * @throws Exception
     */
    //同步班组信息
    public void synBuildPo(Integer projectId, Integer userId,String type);


    //同步班组工人进退信息
    public void synBuildPoUserJT(Integer projectId, Integer userId,String type,Integer teamId);

    //获取pSent值
    public String getpSent();

    //根据类型同步信息,用于不常用的API同步
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum);


}
