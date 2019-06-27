package com.ktp.project.constant;

import com.ktp.project.util.PropertiesUtil;

import java.util.Properties;

public class RealNameConfig {

    private static Properties prop = PropertiesUtil.readConfig("/properties/realName.properties");

    public static final String KTP_CLOUD_REQ_IP = String.valueOf(prop.get("ktp_cloud_req_ip"));
    public static final String OPENAPI_SECRET = String.valueOf(prop.get("ktp_cloud_openapi_secret"));
    public static final String BUSINESS_SECRET = String.valueOf(prop.get("ktp_cloud_business_secret"));
    public static final String KTP_NORMAL_TOPIC = String.valueOf(prop.get("mqconfig.topic.normal"));
    public static final String TAG_PRO_ORG_USER_ADD = String.valueOf(prop.get("mqconfig.tag.pro.org.user.add"));
    //remark
    public static final String CLOUD_TOKEN_KEY = "ktp_cloud_redis_token_key";
    public static final String SYN_POP = "/openapi/authRealName/sendMq";
    public static final String JOIN = "同步进场员工信息";
    public static final String OUT = "同步离场员工信息";
    public static final String KAOQIN = "同步考勤员工信息";
    public static final String JIANLIBANZU = "同步考勤员工信息";
    public static final String API_PACKAGE = "com.ktp.project.service.realName";
    public static final String SAVEPO = "同步添加班组";
    public static final String UPDATEPO = "同步更新班组";
    public static final String SUPO = "同步添加班组工人";
    public static final String UUPO = "同步更新班组工人";
    public static final String EPO = "同步班组工人进退场";

    /** 南宁项目对接参数 */
    public static final String IMG_BASE_PATH = String.valueOf(prop.get("img.base.path"));
    public static final String LOGIN_USER_NAME = String.valueOf(prop.get("login.username"));
    public static final String LOGIN_PASSWORD = String.valueOf(prop.get("login.password"));
    public static final String PROJECT_CODE = String.valueOf(prop.get("project.code"));
    public static final String SYN_LOGIN_URL = String.valueOf(prop.get("base.request")) + String.valueOf(prop.get("syn.login.url"));
    public static final String SYN_JOIN_WORKER_INFO_URL = String.valueOf(prop.get("base.request")) + String.valueOf(prop.get("syn.joinworkerinfo.url"));
    public static final String SYN_JOIN_WORKER_ATTENDANCE_URL = String.valueOf(prop.get("base.request")) + String.valueOf(prop.get("syn.joinworkerattendance.url"));
    public static final String SYN_LEAVE_PROJECT_WORKER_URL = String.valueOf(prop.get("base.request")) + String.valueOf(prop.get("syn.leaveprojectworker.url"));
    /** 泉州项目对接参数*/
    public static final String QZP_BASE_PATH = String.valueOf(prop.get("qzp.base.request"));
    public static final String QZP_KAOQIN_URL = String.valueOf(prop.get("qzp.kaoqin.url"));
    public static final String QZP3874_TOKEN = String.valueOf(prop.get("qzp3874.token"));
    public static final String QZP3874_KEY = String.valueOf(prop.get("qzp3874.key"));
    public static final String QZP4049_TOKEN = String.valueOf(prop.get("qzp4049.token"));
    public static final String QZP4049_KEY = String.valueOf(prop.get("qzp4049.key"));

    public static final String QZP4103_TOKEN = String.valueOf(prop.get("qzp4103.token"));
    public static final String QZP4103_KEY = String.valueOf(prop.get("qzp4103.key"));

    public static final String QZP4057_TOKEN = String.valueOf(prop.get("qzp4057.token"));
    public static final String QZP4057_KEY = String.valueOf(prop.get("qzp4057.key"));

    public static final String QZP4179_TOKEN = String.valueOf(prop.get("qzp4179.token"));
    public static final String QZP4179_KEY = String.valueOf(prop.get("qzp4179.key"));

    public static final String QZP4090_TOKEN = String.valueOf(prop.get("qzp4090.token"));
    public static final String QZP4090_KEY = String.valueOf(prop.get("qzp4090.key"));

    public static final String QZP4271_TOKEN = String.valueOf(prop.get("qzp4271.token"));
    public static final String QZP4271_KEY = String.valueOf(prop.get("qzp4271.key"));

    public static final String QZP4303_TOKEN = String.valueOf(prop.get("qzp4303.token"));
    public static final String QZP4303_KEY = String.valueOf(prop.get("qzp4303.key"));
    /** 江门项目对接参数 */
    public static final String JMP_BASE_PATH = String.valueOf(prop.get("jmp.base.request"));
    public static final String JMP_AND_GSX_KAOQIN_URL = String.valueOf(prop.get("jmp.kaoqin.url"));
    public static final String JMP_AND_GSX_BANKCARD_URL = String.valueOf(prop.get("jmp.bankCard.url"));
    public static final String JMP_AND_GSX_SALARYRECORD_URL = String.valueOf(prop.get("jmp.salaryRecord.url"));

    public static final String JMP174_TOKEN = String.valueOf(prop.get("jmp174.token"));
    public static final String JMP174_KEY = String.valueOf(prop.get("jmp174.key"));

    public static final String JMP2989_TOKEN = String.valueOf(prop.get("jmp2989.token"));
    public static final String JMP2989_KEY = String.valueOf(prop.get("jmp2989.key"));

    public static final String JMP3144_TOKEN = String.valueOf(prop.get("jmp3144.token"));
    public static final String JMP3144_KEY = String.valueOf(prop.get("jmp3144.key"));

    public static final String JMP3150_TOKEN = String.valueOf(prop.get("jmp3150.token"));
    public static final String JMP3150_KEY = String.valueOf(prop.get("jmp3150.key"));

    public static final String JMP3251_TOKEN = String.valueOf(prop.get("jmp3251.token"));
    public static final String JMP3251_KEY = String.valueOf(prop.get("jmp3251.key"));

    public static final String JMP3249_TOKEN = String.valueOf(prop.get("jmp3249.token"));
    public static final String JMP3249_KEY = String.valueOf(prop.get("jmp3249.key"));

    public static final String JMP3059_TOKEN = String.valueOf(prop.get("jmp3059.token"));
    public static final String JMP3059_KEY = String.valueOf(prop.get("jmp3059.key"));

    public static final String JMP3209_TOKEN = String.valueOf(prop.get("jmp3209.token"));
    public static final String JMP3209_KEY = String.valueOf(prop.get("jmp3209.key"));

    public static final String JMP3250_TOKEN = String.valueOf(prop.get("jmp3250.token"));
    public static final String JMP3250_KEY = String.valueOf(prop.get("jmp3250.key"));

    public static final String JMP3252_TOKEN = String.valueOf(prop.get("jmp3252.token"));
    public static final String JMP3252_KEY = String.valueOf(prop.get("jmp3252.key"));

    public static final String JMP3253_TOKEN = String.valueOf(prop.get("jmp3253.token"));
    public static final String JMP3253_KEY = String.valueOf(prop.get("jmp3253.key"));

    public static final String JMP3254_TOKEN = String.valueOf(prop.get("jmp3254.token"));
    public static final String JMP3254_KEY = String.valueOf(prop.get("jmp3254.key"));

    public static final String JMP3231_TOKEN = String.valueOf(prop.get("jmp3231.token"));
    public static final String JMP3231_KEY = String.valueOf(prop.get("jmp3231.key"));

    public static final String JMP3232_TOKEN = String.valueOf(prop.get("jmp3232.token"));
    public static final String JMP3232_KEY = String.valueOf(prop.get("jmp3232.key"));

    public static final String JMP2624_TOKEN = String.valueOf(prop.get("jmp2624.token"));
    public static final String JMP2624_KEY = String.valueOf(prop.get("jmp2624.key"));

    public static final String JMP2625_TOKEN = String.valueOf(prop.get("jmp2625.token"));
    public static final String JMP2625_KEY = String.valueOf(prop.get("jmp2625.key"));

    public static final String JMP3246_TOKEN = String.valueOf(prop.get("jmp3246.token"));
    public static final String JMP3246_KEY = String.valueOf(prop.get("jmp3246.key"));

    public static final String JMP3234_TOKEN = String.valueOf(prop.get("jmp3234.token"));
    public static final String JMP3234_KEY = String.valueOf(prop.get("jmp3234.key"));

    public static final String JMP2526_TOKEN = String.valueOf(prop.get("jmp2526.token"));
    public static final String JMP2526_KEY = String.valueOf(prop.get("jmp2526.key"));

    public static final String JMP3236_TOKEN = String.valueOf(prop.get("jmp3236.token"));
    public static final String JMP3236_KEY = String.valueOf(prop.get("jmp3236.key"));

    public static final String JMP2466_TOKEN = String.valueOf(prop.get("jmp2466.token"));
    public static final String JMP2466_KEY = String.valueOf(prop.get("jmp2466.key"));

    public static final String JMP2629_TOKEN = String.valueOf(prop.get("jmp2629.token"));
    public static final String JMP2629_KEY = String.valueOf(prop.get("jmp2629.key"));

    public static final String JMP2359_TOKEN = String.valueOf(prop.get("jmp2359.token"));
    public static final String JMP2359_KEY = String.valueOf(prop.get("jmp2359.key"));

    public static final String JMP3235_TOKEN = String.valueOf(prop.get("jmp3235.token"));
    public static final String JMP3235_KEY = String.valueOf(prop.get("jmp3235.key"));

    public static final String JMP2375_TOKEN = String.valueOf(prop.get("jmp2375.token"));
    public static final String JMP2375_KEY = String.valueOf(prop.get("jmp2375.key"));

    public static final String JMP2086_TOKEN = String.valueOf(prop.get("jmp2086.token"));
    public static final String JMP2086_KEY = String.valueOf(prop.get("jmp2086.key"));

    public static final String JMP2245_TOKEN = String.valueOf(prop.get("jmp2245.token"));
    public static final String JMP2245_KEY = String.valueOf(prop.get("jmp2245.key"));

    public static final String JMP2237_TOKEN = String.valueOf(prop.get("jmp2237.token"));
    public static final String JMP2237_KEY = String.valueOf(prop.get("jmp2237.key"));

    public static final String JMP2156_TOKEN = String.valueOf(prop.get("jmp2156.token"));
    public static final String JMP2156_KEY = String.valueOf(prop.get("jmp2156.key"));

    public static final String JMP2776_TOKEN = String.valueOf(prop.get("jmp2776.token"));
    public static final String JMP2776_KEY = String.valueOf(prop.get("jmp2776.key"));

    public static final String JMP3469_TOKEN = String.valueOf(prop.get("jmp3469.token"));
    public static final String JMP3469_KEY = String.valueOf(prop.get("jmp3469.key"));

    public static final String JMP2822_TOKEN = String.valueOf(prop.get("jmp2822.token"));
    public static final String JMP2822_KEY = String.valueOf(prop.get("jmp2822.key"));

    public static final String JMP2202_TOKEN = String.valueOf(prop.get("jmp2202.token"));
    public static final String JMP2202_KEY = String.valueOf(prop.get("jmp2202.key"));

    public static final String JMP2155_TOKEN = String.valueOf(prop.get("jmp2155.token"));
    public static final String JMP2155_KEY = String.valueOf(prop.get("jmp2155.key"));

    public static final String JMP2221_TOKEN = String.valueOf(prop.get("jmp2221.token"));
    public static final String JMP2221_KEY = String.valueOf(prop.get("jmp2221.key"));

    public static final String JMP2199_TOKEN = String.valueOf(prop.get("jmp2199.token"));
    public static final String JMP2199_KEY = String.valueOf(prop.get("jmp2199.key"));

    public static final String JMP2224_TOKEN = String.valueOf(prop.get("jmp2224.token"));
    public static final String JMP2224_KEY = String.valueOf(prop.get("jmp2224.key"));

    public static final String JMP3470_TOKEN = String.valueOf(prop.get("jmp3470.token"));
    public static final String JMP3470_KEY = String.valueOf(prop.get("jmp3470.key"));

    public static final String JMP2425_TOKEN = String.valueOf(prop.get("jmp2425.token"));
    public static final String JMP2425_KEY = String.valueOf(prop.get("jmp2425.key"));

    public static final String JMP2140_TOKEN = String.valueOf(prop.get("jmp2140.token"));
    public static final String JMP2140_KEY = String.valueOf(prop.get("jmp2140.key"));

    public static final String QZP3903_TOKEN = String.valueOf(prop.get("qzp3903.token"));
    public static final String QZP3903_KEY = String.valueOf(prop.get("qzp3903.key"));

    public static final String JMP2374_TOKEN = String.valueOf(prop.get("jmp2374.token"));
    public static final String JMP2374_KEY = String.valueOf(prop.get("jmp2374.key"));

    public static final String JMP3171_TOKEN = String.valueOf(prop.get("jmp3171.token"));
    public static final String JMP3171_KEY = String.valueOf(prop.get("jmp3171.key"));

    public static final String JMP4192_TOKEN = String.valueOf(prop.get("jmp4192.token"));
    public static final String JMP4192_KEY = String.valueOf(prop.get("jmp4192.key"));
    /** 广州项目对接参数*******************begin**************************/
    public static final String GZ_BASE_REQUEST = String.valueOf(prop.get("gz.base.request"));
    public static final String GZ_KAOQIN_URL = String.valueOf(prop.get("gz.kaoqin.url"));
    public static final String GZ_KAOQIN_OLD_URL = String.valueOf(prop.get("gz.kaoqin.old.url"));
    public static final String GZ_BASE_POSAVE = String.valueOf(prop.get("gz.base.poSave"));
    public static final String GZ_BASE_POUPDATE = String.valueOf(prop.get("gz.base.poUpdate"));
    public static final String GZ_BASE_POUSERSAVE = String.valueOf(prop.get("gz.base.poUserSave"));
    public static final String GZ_BASE_POUSERUPDATE = String.valueOf(prop.get("gz.base.poUserUpdate"));
    public static final String GZ_BASE_POUSEREES = String.valueOf(prop.get("gz.base.poUserEnterExitSave"));
    public static final String GZ60_ACCESSNO = String.valueOf(prop.get("gz60.accessNo"));
    public static final String GZ60_SECRETKEY = String.valueOf(prop.get("gz60.key"));
    public static final String GZ182_ACCESSNO = String.valueOf(prop.get("gz182.accessNo"));
    public static final String GZ182_SECRETKEY = String.valueOf(prop.get("gz182.key"));
    public static final String GZ_KEY = String.valueOf(prop.get("gz.key"));


    public static final String GZ_FACTORY_NUM = String.valueOf(prop.get("gz.factory.num"));
    //广州空值初始化
    //身份证
    public static final String GZ_SFZ = "430522197912116397";
    //班组类型代码
    public static final String GZ_BZLX = "Z20";
    public static final String GZ_MZ = "汉族";
    //生日
    public static final String GZ_SR = "20180801";
    //身份证住址
    public static final String GZ_SFZZZ = "未知地址";
    //签发机关
    public static final String GZ_QFJF = "无签发机关";
    //身份证开始有效日期
    public static final String GZ_SFZYXRQ = "20180101";
    //身份证头像
    public static final String GZ_SFZTX = "https://images.ktpis.com/images/pic/20181122154936224290102181.png";
    //近期常住地址
    public static final String GZ_JQCZDZ = "未知地址";
    //政治面貌
    public static final String GZ_ZZMM = "2";
    //文化程度
    public static final String GZ_WHCD = "初中及以下";
    //手机号
    public static final String GZ_SJ = "13300000000";
    //用工形式
    public static final String GZ_YGXS = "0";
    //技能水平
    public static final String GZ_JNSP = "5";
    //工种代码
    public static final String GZ_GZDM = "Z20";
    //是否参加安全教育、
    public static final String GZ_AQJY = "false";
    //进场日期
    public static final String GZ_JCRQ = "20180101";
    //籍贯
    public static final String GZ_JG = "未知";
    //婚姻状态
    public static final String GZ_HYZT = "0";
    //银行代码
    public static final String GZ_YHDM = "1402";
    /** 广州项目对接参数********************end*************************/


    /** 固始县项目对接参数 */
    public static final String GSX_BASE_PATH = String.valueOf(prop.get("gsx.base.request"));
    public static final String GSX_KAOQIN_URL = String.valueOf(prop.get("gsx.kaoqin.url"));

    public static final String GSX3309_TOKEN = String.valueOf(prop.get("gsx3309.token"));
    public static final String GSX3309_KEY = String.valueOf(prop.get("gsx3309.key"));

    public static final String GSX3223_TOKEN = String.valueOf(prop.get("gsx3223.token"));
    public static final String GSX3223_KEY = String.valueOf(prop.get("gsx3223.key"));

    public static final String GSX3242_TOKEN = String.valueOf(prop.get("gsx3242.token"));
    public static final String GSX3242_KEY = String.valueOf(prop.get("gsx3242.key"));

    public static final String GSX3247_TOKEN = String.valueOf(prop.get("gsx3247.token"));
    public static final String GSX3247_KEY = String.valueOf(prop.get("gsx3247.key"));

    public static final String GSX3265_TOKEN = String.valueOf(prop.get("gsx3265.token"));
    public static final String GSX3265_KEY = String.valueOf(prop.get("gsx3265.key"));

    public static final String GSX3271_TOKEN = String.valueOf(prop.get("gsx3271.token"));
    public static final String GSX3271_KEY = String.valueOf(prop.get("gsx3271.key"));

    public static final String GSX3447_TOKEN = String.valueOf(prop.get("gsx3447.token"));
    public static final String GSX3447_KEY = String.valueOf(prop.get("gsx3447.key"));

    public static final String GSX3448_TOKEN = String.valueOf(prop.get("gsx3448.token"));
    public static final String GSX3448_KEY = String.valueOf(prop.get("gsx3448.key"));

    public static final String GSX3273_TOKEN = String.valueOf(prop.get("gsx3273.token"));
    public static final String GSX3273_KEY = String.valueOf(prop.get("gsx3273.key"));

    public static final String GSX3272_TOKEN = String.valueOf(prop.get("gsx3272.token"));
    public static final String GSX3272_KEY = String.valueOf(prop.get("gsx3272.key"));

    public static final String GSX3418_TOKEN = String.valueOf(prop.get("gsx3418.token"));
    public static final String GSX3418_KEY = String.valueOf(prop.get("gsx3418.key"));

    /** 高明项目对接参数 */
    //基础路径
    public static final String GM_BASE_PATH = String.valueOf(prop.get("gm.base.request"));
    //api_key
    public static final String GM_API_KEY = String.valueOf(prop.get("gm.api.key"));
    //api_version
    public static final String GM_API_VERSION = String.valueOf(prop.get("gm.api.version"));

    //获取基础数据类型数据字典
    public static final String GM_DICTIONARY_URL= String.valueOf(prop.get("gm.dictionary.url"));
    //上传采集人员特征信息
    public static final String GM_WORKERFECE_URL= String.valueOf(prop.get("gm.workerFece.url"));
    //上传企业信息
    public static final String GM_COMPANYINFO_URL= String.valueOf(prop.get("gm.companyInfo.url"));
    //上传项目信息
    public static final String GM_PROJECTINFO_URL= String.valueOf(prop.get("gm.projectInfo.url"));
    //上传参建单位信息
    public static final String GM_PARTICIPATEINFO_URL= String.valueOf(prop.get("gm.participateInfo.url"));
    //修改参建单位信息
    public static final String GM_UPDATE_PARTICIPATEINFO_URL= String.valueOf(prop.get("gm.update.participateInfo.url"));
    //上传班组信息
    public static final String GM_TEAMINFO_URL= String.valueOf(prop.get("gm.teamInfo.url"));
    //修改班组信息
    public static final String GM_UPDATE_TEAMINFO_URL= String.valueOf(prop.get("gm.update.teamInfo.url"));
    //上传项目工人信息
    public static final String GM_WORKINFO_URL= String.valueOf(prop.get("gm.workInfo.url"));
    //修改项目工人信息
    public static final String GM_UPDATE_WORKINFO_URL= String.valueOf(prop.get("gm.update.workInfo.url"));
    //上传人员进退场
    public static final String GM_JOINOROUT_URL= String.valueOf(prop.get("gm.joinOrOut.url"));
    //上传劳动合同
    public static final String GM_CONTRACT_URL= String.valueOf(prop.get("gm.contract.url"));
    //上传考勤信息
    public static final String GM_ATTENDANCE_URL= String.valueOf(prop.get("gm.attendance.url"));
    //上传项目工资单信息
    public static final String GM_PAYROLL_URL= String.valueOf(prop.get("gm.payroll.url"));
    //上传项目工资单信息
    public static final String GM_PAYROLLDETAIL_URL= String.valueOf(prop.get("gm.payrollDetail.url"));
    //上传项目培训课程信息
    public static final String GM_TRAININFO_URL= String.valueOf(prop.get("gm.trainInfo.url"));
    //上传项目培训课程人员信息
    public static final String GM_TRAINWORKERINFO_URL= String.valueOf(prop.get("gm.trainWorkerInfo.url"));
    //上传人员资质信息
    public static final String GM_WORKERCREDENTIAL_URL= String.valueOf(prop.get("gm.workerCredential.url"));
    //上传人员注册信息
    public static final String GM_WORKERREGISTER_URL= String.valueOf(prop.get("gm.workerRegister.url"));
    //上传设备绑定信息
    public static final String GM_DEVICEINFO_URL= String.valueOf(prop.get("gm.deviceInfo.url"));

    /**中山项目对接参数-begin*/
    //基础路径
    //appid
    public static final String ZS_APPID = String.valueOf(prop.get("zs.appid"));
    //接口地址
    public static final String ZS_BASE_PATH = String.valueOf(prop.get("zs.base.request"));
    //api_key
    public static final String ZS_API_KEY = String.valueOf(prop.get("zs.api.key"));
    //api_version
    public static final String ZS_API_VERSION = String.valueOf(prop.get("zs.api.version"));

    //上传企业信息
    public static final String ZS_COMPANYINFO_METHOD= String.valueOf(prop.get("zs.companyInfo.method"));
    //上传项目信息
    public static final String ZS_PROJECTINFO_METHOD= String.valueOf(prop.get("zs.projectInfo.method"));
    //查询上传项目信息
    public static final String ZS_PROJECTUPDATE_METHOD= String.valueOf(prop.get("zs.projectupdate.method"));
    //查询上传项目信息
    public static final String ZS_PROJECTQUERY_METHOD= String.valueOf(prop.get("zs.projectquery.method"));
    //上传项目人员信息
    public static final String ZS_PROJECTWORKERADD_METHOD= String.valueOf(prop.get("zs.projectWorkeradd.method"));
    //查询上传项目人员信息
    public static final String ZS_PROJECTWORKERQUERY_METHOD= String.valueOf(prop.get("zs.projectWorkerquery.method"));
    //查询上传参建信息
    public static final String ZS_CONTRACTOR_METHOD= String.valueOf(prop.get("zs.contractor.method"));
    //查询上传班组信息
    public static final String ZS_TEAMQUERY_METHOD= String.valueOf(prop.get("zs.teamquery.method"));
    //查询上传项目信息
    public static final String ZS_CORP_METHOD= String.valueOf(prop.get("zs.corp.method"));
    //上传项目信息
    public static final String ZS_PROJECTSUBCONTRACTOR_METHOD= String.valueOf(prop.get("zs.projectSubContractor.method"));
    //上传班组
    public static final String ZS_TEAMINFO_METHOD= String.valueOf(prop.get("zs.teamInfo.method"));
    //上传项目工人进/退场信息
    public static final String ZS_JOINOROUT_METHOD=String.valueOf(prop.get("zs.joinOrOut.method"));
    //上传工人考勤
    public static final String ZS_ATTENDANCE_URL=String.valueOf(prop.get("zs.workerattendance.method"));
    //查询工人考勤
    public static final String ZS_CONTRACT_URL=String.valueOf(prop.get("zs.WorkerAttendance.method"));
    //上传工人合同
    public static final String ZS_WORKERCONTRACT_URL=String.valueOf(prop.get("zs.workercontract.method"));
    //上传工人工资
    public static final String ZS_WORKERWAGE_URL=String.valueOf(prop.get("zs.workerwage.method"));
    //上传项目培训
    public static final String ZS_PROJECTCONTRINNNG_URL=String.valueOf(prop.get("zs.projecttrainning.method"));
    //上传项目培训
    public static final String ZS_RESULT_URL=String.valueOf(prop.get("zs.result.method"));
    /**中山项目对接参数-end*/

    /**珠海整合国际begin*/
    //api_key
    public static final String ZHZH_APPID = String.valueOf(prop.get("zhzh.api.key"));
    //接口地址
    public static final String ZHZH_BASE_PATH203 = String.valueOf(prop.get("zhzh.base.request203"));
    public static final String ZHZH_BASE_PATH204 = String.valueOf(prop.get("zhzh.base.request204"));
    //端口9204
    public static final String ZHZH_PORT_PATH = String.valueOf(prop.get("zhzh.base.port"));
    //设备编号
    public static final String ZHZH_EQUIPMENT_MUN = String.valueOf(prop.get("zhzh.equipment.mun"));
    //厂家机构编码
    public static final String ZHZH_MANUFACTURERS_MUN = String.valueOf(prop.get("zhzh.manufacturers.mun"));
    //版本
    public static final String ZHZH_API_VERSION = String.valueOf(prop.get("zhzh.api.version"));
    //上传企业信息
    public static final String ZHZH_PROJECTINFO_URL = String.valueOf(prop.get("zhzh.companyInfo.url"));
    //上传企业信息
    public static final String ZHZH_UPLOADWORKERFEATURE_URL = String.valueOf(prop.get("zhzh.uploadWorkerFeature.url"));
    /**珠海整合国际end*/



    /** 顺德项目对接参数 */
    //基础路径
    public static final String SD_BASE_PATH = String.valueOf(prop.get("sd.base.request"));
    //token
    public static final String SD_API_TOKEN = String.valueOf(prop.get("sd.api.token"));
    //上传外来人员登记记录
    public static final String SD_PEOPLE_UPLOADREGISTRATIONRECORD = String.valueOf(prop.get("sd.people.uploadregistrationrecord"));
    //添加人员信息
    public static final String SD_PEOPLE_ADDATTENDANCEPEOPLE = String.valueOf(prop.get("sd.people.addAttendancePeople"));
    //项目进度获取
    public static final String SD_PEOPLE_GETPROJECTSTEP = String.valueOf(prop.get("sd.people.getProjectStep"));
    //更新项目进度
    public static final String SD_PEOPLE_UPDATEPROJECTSTEP = String.valueOf(prop.get("sd.people.updateProjectStep"));
    //人员违规记录查询
    public static final String SD_PEOPLE_GETILLEGALLIST = String.valueOf(prop.get("sd.people.getIllegalList"));
    //获取施工单位的班组列表
    public static final String SD_PEOPLE_GETTEAMLIST = String.valueOf(prop.get("sd.people.getTeamList"));
    //添加班组
    public static final String SD_PEOPLE_ADDPROJECTTEAM = String.valueOf(prop.get("sd.people.addProjectTeam"));
    //人员进/退场
    public static final String SD_PEOPLE_ADDPROJECTPEOPLE= String.valueOf(prop.get("sd.people.addProjectPeople"));
    //上传文件
    public static final String SD_REST_DOCUMENT_UPLOAD= String.valueOf(prop.get("sd.rest.document.upload"));
    //下载文件
    public static final String SD_REST_DOCUMENT_GETCONTENT= String.valueOf(prop.get("sd.rest.document.getContent"));
    //创建文件系统的文件夹
    public static final String SD_REST_FOLDER_CREATEPATH= String.valueOf(prop.get("sd.rest.folder.createPath"));
    //获取考勤机列表
    public static final String SD_ATTENDANCE_ATTENDANCEMACHINELIST= String.valueOf(prop.get("sd.attendance.attendanceMachineList"));
    //添加考勤机
    public static final String SD_ATTENDANCEMACHINE_SAVEORUPDATE= String.valueOf(prop.get("sd.attendanceMachine.saveOrUpdate"));
    //批量上传历史考勤记录
    public static final String SD_ATTENDANCE_UPLOADATTENDANCEITEM= String.valueOf(prop.get("sd.attendance.uploadAttendanceItem"));
    //更新人员图片ID
    public static final String SD_PEOPLE_UPDATEEMPLOYEEPHOTOID= String.valueOf(prop.get("sd.people.updateEmployeePhotoID"));
    //实时上传考勤记录
    public static final String SD_ATTENDANCE_DEVICEUPLOADATTENDANCEITEM= String.valueOf(prop.get("sd.attendance.deviceUploadAttendanceItem"));
    //批量上传设备缓存考勤记录
    public static final String SD_ATTENDANCE_DEVICEUPLOADATTENDANCEITEMMULTIPLE= String.valueOf(prop.get("sd.attendance.deviceUploadAttendanceItemMultiple"));
    //添加/更新采集人脸图片ID
    public static final String SD_PEOPLE_UPLOADCOLLECTEDPHOTO= String.valueOf(prop.get("sd.people.uploadCollectedPhoto"));
    //设备心跳接口
    public static final String SD_ATTENDANCEMACHINE_DEVICEHEARDBEAD= String.valueOf(prop.get("sd.attendanceMachine.deviceHeardBead"));
    //合同类容
    public static final String SD_PROJECTPART = String.valueOf(prop.get("sd.ProjectPart"));
    //合同金额
    public static final Integer SD_CONTRACTSUM = Integer.parseInt(String.valueOf(prop.get("sd.ContractSum")));
    //紧急联系人
    public static final String SD_EMERPEOPLE =  String.valueOf(prop.get("sd.EmerPeople"));
    //紧急联系人电话
    public static final String SD_EMERTEL =  String.valueOf(prop.get("sd.EmerTel"));
    //考勤机编号
    public static final String SD_DEVICEKEY =  String.valueOf(prop.get("sd.devicekey"));
    public static final String SD_BUSINESS_LICENCE =  String.valueOf(prop.get("sd.business.licence"));

    /** 山水项目 */
    public static final String SS_BASE_REQUEST = String.valueOf(prop.get("ss.base.request"));
    public static final String SS_TOKEN = String.valueOf(prop.get("ss.token"));
    public static final String SS_PROORGAN_SAVE_URL = String.valueOf(prop.get("ss.proorgan.save.url"));
    public static final String SS_KAOQIN_URL = String.valueOf(prop.get("ss.kaoqin.url"));
    public static final String SS_DEVICEKEY = String.valueOf(prop.get("ss.deviceKey"));
}
