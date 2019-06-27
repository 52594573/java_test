package com.ktp.project.service.realName;

public interface ZhzhAuthRealNameApi {

    public void CaijiAll(Integer projectId);

    public void uploadWorkerFeature(Integer userId,String tp);
    /**
     * 上传企业信息
     * @param corpCode 企业编号
     * @param corpName 企业名称
     * @param corpName 企业注册地区编码
     * @param registerDate 注册日期
     */
    public void synCompanyInfo(String corpCode, String corpName, String areaCode, String registerDate);

    /**
     * 上传参建单位
     * @param projectId 项目id
     */
    public void uploadParticipateInfo(Integer projectId);



    /**
     * 上传项目信息
     */
    public void synWorkinfo(Integer projectId);

    public void sgsynWorkinfo(Integer projectId);

/**
 * 查询项目信息
 */
    public void projectquery(String contractorCorpCode);

    /**
     * 查询班组信息
     */
    public void poquery(String projectCode, Integer pageIndex, Integer pageSize);


    public void queryuser(String projectCode);

    public void workercontract(String projectCode, Integer pageIndex, Integer pageSize, String teamSysNo, String date);

    public void kaoqingAll(Integer projectId);

    /**
     * 查询项目信息
     */
    public void querycanjian(String projectCode);

    /**
     *查询上传项目信息
     */
    public void corpQuery(String corpCode);

    /**\
     * 上传班组信息
     * @param projectId 项目id
     * @param teamId 班组id
     * @param type  类型
     * @throws Exception
     */
    public void uploadTeamInfo(Integer projectId, Integer teamId, String teamName, String type);

    public void uploadTeamAll(Integer projectId);

    public void uploadProjectUserAll(Integer projectId);

    /**\
     * 上传项目人员信息
     * @param projectId 项目id
     * @param teamId  班组id
     *
     */
    public void uploadRosterInfo(Integer projectId, Integer teamId, Integer uerId);

    /**
     * 上传进退场信息
     * @param projectId
     * @param userId
     * @param type
     * @param teamId
     */
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId);


    /**
     * 上传所有企业信息
     */
    public void synAllCompanyInfo();

    /**
     * 根据项目id上传所有的项目相关的信息
     */
    public void synAllInfoByProjectId(Integer projectId);
/*
    *//**
     * 获取数据字典
     *
     * @param type -数据类型
     * @return list  符合条件的list
     *//*
    public  List getBaseDataDictionary(int type);*/
}
