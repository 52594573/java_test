package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.SynKaoQinIfFalseDto;
import com.ktp.project.dto.GZProject.GZProjectBuildDto;
import com.ktp.project.dto.GZProject.GzAddOrUpdateWorkerDto;
import com.ktp.project.dto.GZProject.GzKaoQinDto;
import com.ktp.project.dto.GZProject.GzWorkJoinOut;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.*;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganDAO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

import static com.alibaba.druid.util.Utils.md5;


@Service("gzAuthRealNameService")
@Transactional
public class GZProjectService implements AuthRealNameApi {
    private static final Logger log = LoggerFactory.getLogger(GZProjectService.class);
    @Autowired
    private AuthRealNameLogService logService;

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Autowired
    private ProOrganDAO proOrganDAO;


    @Override
    public void synWorkerAttendance(Integer pId, Integer kaoQinId) {

        log.info("广州项目同步考勤信息考勤ID:{},同步开始时间:{}", kaoQinId, NumberUtil.formatDateTime(new Date()));
        String data = "";
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(pId);
        String url = projectEnum.getBaseRequest() + RealNameConfig.GZ_KAOQIN_URL;
        AuthRealNameLog realNameLog = logService.createBaseBean(pId, kaoQinId, url, RealNameConfig.KAOQIN);
        GzKaoQinDto gzKaoQinDto = null;
        try {
            gzKaoQinDto = queryKaoQinInfo(pId, kaoQinId);
            RealNameUtil.setDefaultValueCheckNull(gzKaoQinDto);
            gzKaoQinDto.setFactoryNum(RealNameConfig.GZ_FACTORY_NUM);
            gzKaoQinDto.setAccessNo(projectEnum.getToken());
            gzKaoQinDto.setTimestamp(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL));
            String idCard = "des:" + DESUtil.toHex16Str(DESUtil.GZDES(gzKaoQinDto.getBuilderIdcard().getBytes(), projectEnum.getKey())).toLowerCase();
            gzKaoQinDto.setBuilderIdcard(idCard);
            data = RealNameUtil.GZencodeRequestBody(gzKaoQinDto, projectEnum.getKey());
            realNameLog.setReqBody(GsonUtil.toJson(gzKaoQinDto));
            sendDate(url, data);
        } catch (Exception e) {
            e.printStackTrace();
            realNameLog.setIsSuccess(0);
            realNameLog.setResMsg(e.getMessage());
            log.error(e.getMessage());
        }finally {
            logService.saveOrUpdate(realNameLog);
        }
        log.info("广州项目同步考勤信息考勤ID:{},同步结束时间:{}", kaoQinId, NumberUtil.formatDateTime(new Date()));

    }

    /**
     * 广州项目班组
     */
    @Override
    public void synBuildPo(Integer poId, Integer userId,String type){
        ProOrgan proOrgan = proOrganDAO.findById(poId);
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(proOrgan.getProId());
        GZProjectBuildDto dto = new GZProjectBuildDto();
        String url = "";
        String annotation = "";
        String datastr = "";
        AuthRealNameLog realNameLog = logService.createBaseBean(poId, userId);
        try {
            switch (type) {
                case "POSAVE":
                    //新增班组
                    dto = queryGZProjectBuild(Lists.newArrayList(userId, poId));
                    url = RealNameConfig.GZ_BASE_POSAVE;
                    annotation = RealNameConfig.SAVEPO;
                    dto.setTimestamp(DateUtil.format(new Date(),DateUtil.FORMAT_DATE_TIME_NORMAL));
                    dto.setLeaderWorkType(queryGZGZ(dto.getLeaderWorkType(),"GZ"));
                    dto.setWorkType(queryGZGZ(dto.getWorkType(),"GZ"));
                    dto.setLeaderCardBank(queryGZGZ(dto.getLeaderCardBank(),"GZ"));
                    dto.setAccessNo(projectEnum.getToken());
                    dto.setName(dto.getLeaderName()+dto.getWorkType()+dto.getName());
                    dto.setLeaderHeadImg(KTPImgUtil.getHead(dto.getLeaderHeadImg(),1));
                    //默认值设置
                    RealNameUtil.setDefaultValueCheckNull(dto);
                    dto.setTeamCode(dto.getLeaderIdcard()+dto.getWorkType());

                    try {
                        //设置空值为默认值
                        datastr = encryptionPo(dto,Lists.newArrayList("teamCode","leaderIdcard","leaderCardNum"),projectEnum.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "POUPDATE":
                    //修改班组
                    dto = queryGZProjectBuild(Lists.newArrayList( userId,poId));
                    dto.setTimestamp(DateUtil.format(new Date(),DateUtil.FORMAT_DATE_TIME_NORMAL));
                    dto.setLeaderWorkType(queryGZGZ(dto.getLeaderWorkType(),"GZ"));
                    dto.setWorkType(queryGZGZ(dto.getWorkType(),"GZ"));
                    dto.setLeaderCardBank(queryGZGZ(dto.getLeaderCardBank(),"GZ"));
                    dto.setAccessNo(projectEnum.getToken());
                    dto.setName(dto.getLeaderName()+dto.getWorkType()+dto.getName());
                    dto.setLeaderHeadImg(KTPImgUtil.getHead(dto.getLeaderHeadImg(),1));
                    //默认值设置
                    RealNameUtil.setDefaultValueCheckNull(dto);
                    dto.setTeamCode(dto.getLeaderIdcard()+dto.getWorkType());

                    url = RealNameConfig.GZ_BASE_POUPDATE;
                    annotation = RealNameConfig.UPDATEPO;
                    try {
                        //生成加密串
                        datastr = encryptionPo(dto,Lists.newArrayList("teamCode","leaderIdcard","leaderCardNum"),projectEnum.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "POUSERSAV":
                    //增加班组工人
                    annotation = RealNameConfig.SUPO;
                    url = RealNameConfig.GZ_BASE_POUSERSAVE;
                    datastr = createSynWorkerInfoJson(poId, userId);
                    break;
                case "POUSERUPDATE":
                    //修改班组工人
                    annotation = RealNameConfig.UUPO;
                    url = RealNameConfig.GZ_BASE_POUSERUPDATE;
                    datastr = createSynWorkerInfoJson(poId, userId);
                    break;
                default:
                    return;
            }
            sendDate( url, datastr);
        } catch (Exception e) {
            e.printStackTrace();
            realNameLog.setIsSuccess(0);
            realNameLog.setResMsg(e.getMessage());
            log.error(e.getMessage());
        }finally {
            realNameLog.setReqBody(datastr);
            realNameLog.setReqUrl(url);
            realNameLog.setRemark(annotation);
            logService.saveOrUpdate(realNameLog);
        }
    }

    /**
     *班组工人进退场信息
     */
    @Override
    public void synBuildPoUserJT(Integer poId, Integer userId, String type,Integer teamId){
        //获取信息
        GZProjectBuildDto dto = queryGZProjectBuild(Lists.newArrayList( 2465,182));

        dto.setWorkType(queryGZGZ(dto.getLeaderWorkType(),"GZ"));

        String url = RealNameConfig.GZ_BASE_POSAVE;
        String annotation = RealNameConfig.EPO;
        Integer flag = 4;
        if (type.equals("JOIN")){
            flag = 0;
        }
        String sql = "SELECT ui.`u_sfz` AS idcard " +
                " , CASE pop.`pop_state` " +
                "  WHEN 0 THEN '0' " +
                "  WHEN 4 THEN '1' " +
                " END AS TYPE " +
                " , CASE pop.`pop_state` " +
                "  WHEN 0 THEN pop.pop_intime " +
                "  WHEN 4 THEN pop.pop_endtime " +
                " END AS doDate " +
                "FROM `pro_organ_per` pop " +
                " LEFT JOIN user_info ui ON pop.`user_id` = ui.`id` " +
                "WHERE (ui.`id` = ? " +
                " AND pop.po_id = ? " +
                " AND pop.`pop_state` = ? ) ";
        GzWorkJoinOut gzWorkJoinOut = null;
        try {
            gzWorkJoinOut = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(poId, userId, flag), GzWorkJoinOut.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询进退场信息失败");
        }
        //todo
//        String idCard = "des:" + DESUtil.toHex16Str(DESUtil.GZDES(gzWorkJoinOut.getIdcard().getBytes(), RealNameConfig.GZ_SECRETKEY)).toLowerCase();
//        gzWorkJoinOut.setIdcard(idCard);dto.setAccessNo(RealNameConfig.GZ_ACCESSNO);
        gzWorkJoinOut.setTimestamp(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL));
        String data = RealNameUtil.GZencodeRequestBody(dto, "");
        //获取信息
        AuthRealNameLog realNameLog = logService.createBaseBean(poId, userId, url,annotation);
        realNameLog.setReqBody(data);
        sendDate( url, data);
    }


    @Override
    public String getpSent() {
        return "GZ";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }


    private String createSynWorkerInfoJson(Integer poId, Integer userId){
        ProOrgan proOrgan = proOrganDAO.findById(poId);
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(proOrgan.getProId());
        String teamCode = getTeamCode(poId);
        teamCode = teamCode == null ? "430522197912116397,{\"GZ\": \"Z20\"}" : teamCode;
        String[] split = teamCode.split(",");
        String gz = queryGZGZ(split[1], "GZ");
        teamCode = ( split[0] + gz );
        if (poId.equals(715)){
            teamCode = "432930197108040719Z20";
        }
        GzAddOrUpdateWorkerDto workerDto = addOrUpdateWorker(poId, userId);
        if (workerDto == null){
            throw new BusinessException(String.format("通过班组ID:%s,用户ID:%s,查询GzAddOrUpdateWorkerDto为空", poId, userId));
        }
        if (StringUtils.isNotBlank(workerDto.getWorkType())){
            workerDto.setWorkType(queryGZGZ(workerDto.getWorkType(), "GZ"));
        }
        RealNameUtil.setDefaultValueCheckNull(workerDto);
        workerDto.setAccessNo(projectEnum.getToken());
        workerDto.setTimestamp(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL));
        workerDto.setTeamCode(teamCode);workerDto.setWorkType(gz);
        String idCard = "des:" + DESUtil.toHex16Str(DESUtil.GZDES(workerDto.getIdcard().getBytes(), projectEnum.getKey())).toLowerCase();
        String cardNum = "des:" + DESUtil.toHex16Str(DESUtil.GZDES(workerDto.getCardNum().getBytes(), projectEnum.getKey())).toLowerCase();
        String aaa = "des:" +DESUtil.toHex16Str(DESUtil.GZDES(teamCode.getBytes(),projectEnum.getKey())).toLowerCase();
        workerDto.setTeamCode(aaa);workerDto.setIdcard(idCard);workerDto.setCardNum(cardNum);
        return  RealNameUtil.GZencodeRequestBody(workerDto, projectEnum.getKey());
    }

    private String getTeamCode(Integer poId){
        String sql ="SELECT CONCAT( IFNULL( ui.`u_sfz`,'430522197912116397') , ',', IFNULL( CAST(wt.w_code AS CHAR), '{\"GZ\": \"Z20\"}') ) teamCode FROM `pro_organ_per` pop  " +
                "LEFT JOIN `pro_organ` po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN  user_info ui ON pop.`user_id` = ui.`id` " +
                "LEFT JOIN work_type wt ON wt.id = po.`po_gzid` " +
                "WHERE po.`id` = ? AND pop.`pop_type` = 8 group by teamCode limit 1  ";
        String teamCode = null;
        try {
            teamCode = queryChannelDao.selectOne(sql, Lists.newArrayList(poId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,查询teamCode失败", poId));
        }
        return teamCode;
    }

    private GzAddOrUpdateWorkerDto addOrUpdateWorker(Integer poId, Integer userId){

        String sql = "SELECT uinfo.u_sfz AS idcard, uinfo.u_realname AS name " +
                " , CASE uinfo.u_sex " +
                "  WHEN 1 THEN 'M' " +
                "  WHEN 2 THEN 'F' " +
                "  ELSE 'M' " +
                " END AS sex, IFNULL(knation.key_name, '未知') AS nation " +
                " , DATE_FORMAT(uinfo.u_birthday, '%Y%c%d') AS birthday " +
                " , IFNULL(uwork.w_resi, '未知地址') AS address " +
                " , IFNULL(usfz.us_org, '没有签发机关') AS signOrgan, '20180101' AS expiryStart " +
                " , IFNULL(pop.pop_pic1, 'https://images.ktpis.com') AS headImg, '未知地址' AS currentAddress " +
                " , '2' AS political " +
                " , CASE uwork.w_edu " +
                "  WHEN (89 " +
                "  OR 90 " +
                "  OR 97) THEN '初中及以下' " +
                "  WHEN 91 THEN '高中' " +
                "  WHEN 92 THEN '中专' " +
                "  WHEN 93 THEN '大专' " +
                "  WHEN 94 THEN '本科' " +
                "  WHEN 95 " +
                "  OR 96 THEN '本科以上' " +
                "  ELSE '初中及以下' " +
                " END AS education, IFNULL(uinfo.u_name, '没有号码') AS phone, '0' AS employType " +
                " , IFNULL(pop.popBankid, '0000000000000000000') AS cardNum " +
                " , IFNULL(sbank.code, '') AS cardBank, wtype.w_code AS workType " +
                " , '0' AS techLevel " +
                " , CASE uwork.w_px " +
                "  WHEN NULL THEN 'false' " +
                "  ELSE 'true' " +
                " END AS safetyEdu " +
                " , CASE uwork.w_gzid " +
                "  WHEN 4 THEN '0' " +
                "  WHEN 20 " +
                "  OR 21 THEN '1' " +
                "  ELSE '2' " +
                " END AS builderType, DATE_FORMAT(pop.pop_intime, '%Y%m%d') AS doDate " +
                " , IFNULL(uwork.w_native, '未知籍贯') AS nativePlace " +
                " , CASE kmar.key_name " +
                "  WHEN '未婚' THEN '0' " +
                "  WHEN '已婚' THEN '1' " +
                "  WHEN '离异' THEN '2' " +
                "  WHEN '丧偶' THEN '3' " +
                "  ELSE '0' " +
                " END AS maritalStatus " +
                "FROM pro_organ_per pop " +
                " LEFT JOIN user_info uinfo ON pop.user_id = uinfo.id " +
                " LEFT JOIN user_work uwork ON uinfo.id = uwork.u_id " +
                " LEFT JOIN key_content knation ON uwork.w_nation = knation.id " +
                " LEFT JOIN key_content kmar ON uwork.w_mar = kmar.id " +
                " LEFT JOIN pro_organ porgan ON pop.po_id = porgan.id " +
                " LEFT JOIN work_type wtype ON porgan.po_gzid = wtype.id " +
                " LEFT JOIN user_sfz usfz ON pop.user_id = usfz.u_id " +
                " LEFT JOIN user_bank ubank ON pop.popBankid = ubank.u_bankid " +
                " LEFT JOIN sys_bank sbank ON ubank.id = ubank.sb_id " +
                "WHERE pop.po_id = ? " +
                " AND pop.user_id = ? " +
                "limit 1  ";
        GzAddOrUpdateWorkerDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(poId, userId), GzAddOrUpdateWorkerDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,用户ID:%s,查询GzAddOrUpdateWorkerDto失败", poId, userId));
        }
        return dto;
    }

    //发送处理数据
    public void sendDate(String url ,String datastr){
        try {
            if(url == null || "".equals(url)){
                throw new Exception("发送类型异常");
            }
           String ss = url +"?"+datastr;
           String result = HttpClientUtils.get(ss, new HashMap<>());
            RealNameUtil.GZproResIfSussess(result);
            log.info("同步到广州实名系统成功! url: {}, 参数: {}, 返回结果: {}", url, datastr, result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     *查询sql
     */
    public GZProjectBuildDto queryGZProjectBuild(List list){
        GZProjectBuildDto dto = new GZProjectBuildDto();
        String sql = "select " +
                "porgan.po_name name, " +
                "wtype.w_code workType, " +
                "uinfo.u_sfz leaderIdcard, " +
                "uinfo.u_realname leaderName, " +
                "case uinfo.u_sex   " +
                "when 1 then 'M'  " +
                "when 2 then 'F' " +
                "else 'M' " +
                "end as leaderSex, " +
                "knation.key_name leaderNation, " +
                "DATE_FORMAT(uinfo.u_birthday,'%Y%c%d') leaderBirthday, " +
                "uwork.w_resi leaderAddress, " +
                "usfz.us_org leaderSignOrgan," +
                "usfz.us_start_time leaderExpiryStart, " +
                "pop.pop_pic1 leaderHeadImg, " +
                "usfz.us_current_address leaderCurrentAddress, " +
                "IFNULL(usfz.us_political,'2') leaderPolitical, " +
                "case uwork.w_edu  " +
                "when 89 or 90 or 97 then '初中及以下'   " +
                "when 91 then '高中' " +
                "when 92 then '中专'  " +
                "when 93 then '大专'  " +
                "when 94 then '本科' " +
                "when 95 or 96 then '本科以上'  " +
                "else '初中及以下' " +
                "end as leaderEducation, " +
                "uinfo.u_name leaderPhone, " +
                " CASE pop.pop_employType " +
                "  WHEN 0 THEN '0' " +
                "  WHEN 1 THEN '1' " +
                "  ELSE '0' " +
                " END AS leaderEmployType, "+
                " CASE pop.pop_techLevel " +
                "  WHEN 0 THEN '0' " +
                "  WHEN 1 THEN '1' " +
                "  WHEN 2 THEN '2' " +
                "  WHEN 3 THEN '3' " +
                "  WHEN 4 THEN '4' " +
                "  WHEN 5 THEN '5' " +
                "  ELSE '0' " +
                " END AS leaderTechLevel, "+
                "IFNULL(pop.popBankid,'0000000000000000') leaderCardNum, " +
                "IFNULL(sbank.code, '') leaderCardBank, " +
                "wtype.w_code leaderWorkType, " +
                "case uwork.w_px   " +
                "when NULL then 'false'  " +
                "else 'true' " +
                "end as leaderSafetyEdu, " +
                "case uwork.w_gzid  " +
                "when 4 then '0'  " +
                "when 20 or 21 then '1' " +
                "else '2' " +
                "end as leaderBuilderType, " +
                "DATE_FORMAT(pop.pop_intime,'%Y%c%d') leaderDoDate,  " +
                "uwork.w_native leaderNativePlace, " +
                " CASE kmar.key_name " +
                "  WHEN '未婚' THEN '0' " +
                "  WHEN '已婚' THEN '1' " +
                "  WHEN '离异' THEN '2' " +
                "  WHEN '丧偶' THEN '3' " +
                "  ELSE '0' " +
                " END AS leaderMaritalStatus "+
                /*"kmar.key_name leaderMaritalStatus " +*/
                "from pro_organ_per pop " +
                "left join user_info uinfo on pop.user_id = uinfo.id " +
                "left join user_work uwork on uinfo.id = uwork.u_id " +
                "left join key_content knation on uwork.w_nation = knation.id " +
                "left join key_content kmar on uwork.w_mar = kmar.id " +
                "left join key_content ktype on pop.p_type = ktype.id "+
                "left join pro_organ porgan on pop.po_id = porgan.id " +
                "left join work_type wtype on porgan.po_gzid = wtype.id "+
                "left join user_sfz usfz on pop.user_id = usfz.u_id  " +
                "left join user_bank ubank on pop.popBankid = ubank.u_bankid " +
                "left join sys_bank sbank on sbank.id = ubank.sb_id "+
                "where pop.user_id = ? and pop.po_id = ? and porgan.po_state = 2 "+
                "GROUP BY porgan.id";
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, list, GZProjectBuildDto.class);
            if(dto != null){
                return dto;
            }else{
                throw new BusinessException("查询项目和用户信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(String.format("查询项目和用户信息为空，项目ID:"+list.get(0)+",班组长ID:"+list.get(1)));
        }

    }

    //string格式转化为map
    public String queryGZGZ(String code,String type){
        if(StringUtil.isEmpty(code)){
            return "";
        }
		Gson gson = new Gson();
		Map map = Maps.newHashMap();
		map = gson.fromJson(code,Map.class);
		return (String) map.get(type);
    }

    private GzKaoQinDto queryKaoQinInfo(Integer projectId, Integer kaoqinId) {
        String sql = "SELECT ui.u_sfz AS builderIdcard, DATE_FORMAT(kq.`k_time`, '%Y%m%d%H%i%s') AS atteTime, '2' AS atteType " +
                " , 'ADT-YZ1' AS checkChannel " +
                " , CASE kq.`k_state` " +
                "  WHEN 1 THEN '1' " +
                "  WHEN 2 THEN '2' " +
                "  WHEN 3 THEN '1' " +
                "  WHEN 4 THEN '2' " +
                " END AS checkType " +
                " , CASE pop.p_type " +
                "  WHEN 117 THEN '1' " +
                "  WHEN 118 THEN '1' " +
                "  WHEN 119 THEN '1' " +
                "  WHEN 120 THEN '1' " +
                "  WHEN 121 THEN '1' " +
                "  WHEN 142 THEN '1' " +
                "  WHEN 179 THEN '1' " +
                "  WHEN 180 THEN '1' " +
                "  WHEN 0 THEN '1' " +
                "  WHEN 181 THEN '1' " +
                "  ELSE '0' " +
                " END AS builderType " +
                "FROM kaoqin" + projectId +" kq " +
                " LEFT JOIN user_info ui ON kq.`user_id` = ui.`id` " +
                " LEFT JOIN `pro_organ_per` pop ON kq.user_id = pop.user_id " +
                " LEFT JOIN pro_organ po ON po.id = pop.po_id " +
                " LEFT JOIN key_content kc ON pop.pop_type = kc.id " +
                "WHERE kq.id =" + kaoqinId +
                " AND po.pro_id = " + projectId +
                " GROUP BY kq.user_id ";//kq.k_state IN (3, 4) AND
        GzKaoQinDto gzKaoQinDto = null;
        try {
            gzKaoQinDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(), GzKaoQinDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s, 考勤ID:%s查询结果失败", projectId, kaoqinId));
        }
        return gzKaoQinDto;
    }

	//根据对象组装键值对并根据list中的值加密
	public String encryptionPo(Object obj,List<String> list,String key) throws Exception{
		TreeMap<String,Object> map = MapUtil.convertBeanToTreeMap(obj);

		Set<Map.Entry<String, Object>> entrySet = map.entrySet();

		StringBuilder strb = new StringBuilder();

		for (Iterator<Map.Entry<String, Object>> iter = entrySet.iterator(); iter.hasNext(); ){
			Map.Entry<String, Object> entry = iter.next();
			String fieldName = entry.getKey();
			Object o = entry.getValue();


//			按照广州项目要求数据格式,是否需要加密
			if (o == null) {
				strb.append("&"+fieldName+"="+ URLEncoder.encode("", "UTF-8"));
			}
			if (list.contains(fieldName)){
				strb.append("&"+fieldName+"="+ URLEncoder.encode("des:" +
						DESUtil.toHex16Str(DESUtil.GZDES(o.toString().getBytes(), key)).toLowerCase(),"UTF-8"));
			}else {
				strb.append("&"+fieldName+"="+ URLEncoder.encode(o.toString(), "UTF-8"));
			}
		}

		strb = strb.delete(0,1);
		strb.append("&sign=" +md5(strb + key));
        log.info(strb.toString());
		return strb.toString();
	}

	public List<Integer> listUserIdsByGz(){
        String sql = "SELECT DISTINCT pop.`user_id`   " +
                "FROM `pro_organ_per` pop    " +
                "LEFT JOIN `pro_organ` po ON po.`id` = pop.`po_id`   " +
                "LEFT JOIN project p ON p.id = po.pro_id " +
                "WHERE p.id = 60 ";
        List<Integer> list = null;
        try {
            list = queryChannelDao.selectMany(sql, Lists.newArrayList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询广州项目下所有员工ID失败");
        }
        return list;
    }

    /**
     * 查询当天同步到实名系统的员工信息
     */
    public List<SynKaoQinIfFalseDto> synKaoQinIfFalse(Integer projectId) {
        String sql = "SELECT " +
                "  nl.project_id AS projectId, " +
                "  kq.`user_id` userId, ui.u_realname name , ui.u_name mobile,ui.u_sfz IdCard , kq.id kaoQinId, " +
                " po.id poId, po.po_name poName " +
                "FROM " +
                "  `ktp_test`.`auth_real_name_log` nl " +
                "  LEFT JOIN `ktp_test`.`kaoqin60` kq " +
                "    ON nl.`user_id` = kq.`id` " +
                "  LEFT join " +
                " user_info ui on ui.id = kq.user_id " +
                " LEFT JOIN pro_organ_per pop on pop.user_id = kq.user_id " +
                "  LEFT JOIN pro_organ po ON pop.po_id = po.id " +
                "WHERE nl.`project_id` = ? AND po.pro_id = ? " +
                "  AND nl.`remark` LIKE '%同步考勤员工信息%' " +
                "  AND nl.`is_success` = '0' " +
                " AND nl.create_time > ? " +
                "GROUP BY userId";
        List<SynKaoQinIfFalseDto> synKaoQinIfFalseDtos = null;
        try {
            String format = NumberUtil.dateFormat.format(new Date());
            Date today = NumberUtil.FORMAT_DATE_TIME.parse(format + " 00:00:00");
            synKaoQinIfFalseDtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(projectId,projectId, today), SynKaoQinIfFalseDto.class);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("时间格式化失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s 查询当天同步考勤员工信息失败", projectId));
        }

        return synKaoQinIfFalseDtos;
    }

    public static void main(String[] args) {
        String teamCode = "432930197108040719240";
        String key = "f97e371b0e59e20781e5294f0ce09585";
        String s = "des:" + DESUtil.toHex16Str(DESUtil.GZDES(teamCode.getBytes(), key)).toLowerCase();
        System.out.println(s);
    }
}
