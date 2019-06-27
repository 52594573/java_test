package com.ktp.project.service.realName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ktp.project.constant.FsBaseInfo;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.dto.AuthRealName.SdProjectInfoDto;
import com.ktp.project.dto.AuthRealName.SdWorkerAttendanceDto;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.realName.impl.RealNameService;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.RealNameUtil;
import com.zm.entity.ProOrganDAO;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FoShanAuthRealNameAbstractService implements AuthRealNameApi{

    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected static ObjectMapper jsckSon = new ObjectMapper();
    protected static final String CONTENT_TYPE_TEXT = "text/html;charset=UTF-8";
    protected static final String CONTENT_TYPE_JSON = "application/json";

    @Autowired
    protected QueryChannelDao queryChannelDao;
    @Autowired
    protected AuthRealNameLogService logService;
    @Autowired
    protected UserInfoDao userInfoDao;
    @Autowired
    protected ProOrganDAO proOrganDAO;
    @Autowired
    protected RealNameService realNameService;


    /**
     * 获取拼接地址，并调用
     * @param url     -访问接口地址
     * @return
     */
    protected static String getUrlString(String url, Map<String, Object> paranms) {
        StringBuffer buffer = new StringBuffer(url);
        try {
            int index = 0;
            for (String key : paranms.keySet()) {
                String value = String.valueOf(paranms.get(key));
                if (value != null) {
                    if (index == 0) {
                        buffer.append("?");
                    } else {
                        buffer.append("&");
                    }
                    String encodeValue = URLEncoder.encode(value, "UTF-8");
                    buffer.append(key).append("=").append(encodeValue);
                    index++;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.valueOf(buffer);
    }


    /**
     * 实时同步工人考勤
     */
    @Override
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        AuthRealNameLog logBean = null;
        String reqBody = null;
        try {
            SdProjectInfoDto projectInfoDto = realNameService.querySdProjectInfoByProjectId(projectId);
            String pSent = projectInfoDto.getProjectArea();
            FsBaseInfo baseInfo = FsBaseInfo.caseEnumByType(pSent);
            String reqUrl =  baseInfo.getReqIp() + RealNameConfig.SD_ATTENDANCE_DEVICEUPLOADATTENDANCEITEM;
            logBean = logService.createBaseBean(projectId, kaoQinId, reqUrl, RealNameConfig.KAOQIN);
            List<SdWorkerAttendanceDto.KaoQinData> data = queryKaoQinInfo(projectId, kaoQinId);

            SdWorkerAttendanceDto params = new SdWorkerAttendanceDto();
            params.setData(data);
            params.setDeviceKey(baseInfo.getDeviceKey());
            params.setProjectNum(projectInfoDto.getProjectCode());
            params.setToken(baseInfo.getToken());

            reqBody = jsckSon.writeValueAsString(params);
            String url= getUrlString(reqUrl, JSONObject.fromObject(reqBody));
            String result = HttpClientUtils.post(url, reqBody, CONTENT_TYPE_TEXT, new HashMap<>());
            RealNameUtil.SDproResIfSussess(result);
        } catch (Exception e) {
            e.printStackTrace();
            assert logBean != null;
            logBean.setIsSuccess(0);
            logBean.setResMsg(e.getMessage());
        }finally {
            assert logBean != null;
            logBean.setReqBody(reqBody);
            logService.saveOrUpdate(logBean);
            log.info("远程调用顺德实名系统: URL: {},请求参数 {},返回结果 {},结束时间:{}", logBean.getReqUrl(), logBean.getReqBody(), logBean.getResMsg(), NumberUtil.formatDateTime(new Date()));
        }
    }

    private List<SdWorkerAttendanceDto.KaoQinData> queryKaoQinInfo(Integer projectId, Integer kaoQinId){//, 4 AS AttendanceType
        String sql = "SELECT ui.u_sfz AS IDcardNum, DATE_FORMAT(kq.k_time, '%Y-%m-%d %H:%i:%s') AS AttendanceTime, kq.k_pic AS imgBase64 " +
                " , CASE k_state " +
                "  WHEN 1 THEN 0 " +
                "  WHEN 2 THEN 1 " +
                "  WHEN 3 THEN 0 " +
                "  WHEN 4 THEN 1 " +
                "  WHEN 5 THEN 0 " +
                "  WHEN 6 THEN 1 " +
                "  ELSE 0 " +
                " END AS IsInAttendance " +
                "FROM kaoqin" + projectId + " kq " +
                " LEFT JOIN user_info ui ON kq.user_id = ui.id " +
                "WHERE kq.pro_id = " + projectId +
                " AND kq.id = " + kaoQinId;
        List<SdWorkerAttendanceDto.KaoQinData> dtos = null;
        try {
            dtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), SdWorkerAttendanceDto.KaoQinData.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s,考勤ID:%s.查询顺德考勤信息失败", projectId, kaoQinId));
        }
        //设置考勤图片base64
        for (SdWorkerAttendanceDto.KaoQinData dto : dtos) {
            dto.setAttendanceType(4);//dto.setInAttendance((byte) 1);
//            dto.setImgBase64(Base64Util.compressBase64(dto.getImgBase64(), ImgRatioEnum.SD_BASE64_PHOTO));
        }
        return dtos;
    }

}
