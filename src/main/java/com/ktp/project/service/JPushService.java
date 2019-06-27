package com.ktp.project.service;

import com.ktp.project.dao.BenefitDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.util.JPushClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送服务
 *
 * @author djcken
 * @date 2018/9/13
 */
@Service
public class JPushService {

    private static final boolean ONLINE = false;//是否线上环境
    @Autowired
    private BenefitDao benefitDao;
    @Autowired
    private UserInfoDao userInfoDao;

    public void pushMessage() {
        List<String> userIdList = new ArrayList<>();
        userIdList.add("44865");
        List<String> aliasList = buildAliasList(userIdList);
        JPushClientUtil.getInstance().sendToAliasPassThrough(aliasList, "benefitNew", "{dddddddd}");
    }


    /**
     * 传入用户id返回别名列表-两个端
     *
     * @param userIdList 用户id列表
     * @return
     */
    private List<String> buildAliasList(List<String> userIdList) {
        List<String> aliasList = new ArrayList<>();
        if (userIdList != null && !userIdList.isEmpty()) {
            for (String userId : userIdList) {
                String android = "KTP_" + (ONLINE ? "1" : "2") + "_A_" + userId;
                aliasList.add(android);
                String ios = "KTP_" + (ONLINE ? "1" : "2") + "_I_" + userId;
                aliasList.add(ios);
            }
        }
        return aliasList;
    }

}
