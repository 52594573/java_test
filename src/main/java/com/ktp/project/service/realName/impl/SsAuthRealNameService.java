package com.ktp.project.service.realName.impl;

import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dto.AuthRealName.SsProOrganPerDto;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.FoShanAuthRealNameAbstractService;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.RealNameUtil;
import com.zm.entity.UserInfo;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service("ssAuthRealNameService")
@Transactional
public class SsAuthRealNameService extends FoShanAuthRealNameAbstractService {

    @Override
    public void synBuildPo(Integer projectId, Integer userId, String type) {

        String url = null;
        AuthRealNameLog logBean = null;
        String reqBody = null;
        try {
            switch (type) {
                case "POSAVE":
                    //新增班组

                    break;
                case "POUPDATE":
                    //修改班组
                    break;
                case "POUSERSAV":
                    //增加班组工人
                    url = RealNameConfig.SS_BASE_REQUEST + RealNameConfig.SS_PROORGAN_SAVE_URL;
                    SsProOrganPerDto dto = queryWorkerInfo(userId);
                    String urlString = getUrlString(url, JSONObject.fromObject(dto));
                    String result = HttpClientUtils.post(urlString, GsonUtil.toJson(dto), CONTENT_TYPE_TEXT, new HashMap<>());
                    RealNameUtil.SSproResIfSussess(result);

                    break;
                case "POUSERUPDATE":
                    //修改班组工人
                    break;
                default:
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private SsProOrganPerDto queryWorkerInfo(Integer userId){
        UserInfo user = userInfoDao.getUserInfoById(userId);
        if (user == null){
            throw new BusinessException(String.format("通过用户ID:%s查询不到结果", userId));
        }
        return SsProOrganPerDto.newInstance(user.getU_sfz(), user.getU_realname());
    }


    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId) {

    }

    @Override
    public String getpSent() {
        return "SS";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }
}
