package com.ktp.project.component;

import com.google.common.collect.Lists;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.im.MessageDto;
import com.ktp.project.entity.UserSfz;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 用户信息推送
 * Created by LinHon 2018/12/7
 */
@Component
public class UserInfoTask {

    private static final Logger log = LoggerFactory.getLogger(UserInfoTask.class);

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Value("${ktp.team.id}")
    private String KTP_TEAM_ID;

    @Value("${ktp.team.name}")
    private String KTP_TEAM_NAME;

    @Value("${ktp.aide.id}")
    private String KTP_AIDE_ID;

    @Value("${ktp.aide.name}")
    private String KTP_AIDE_NAME;


    /**
     * 生日提醒
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void birthday() {
        try {
            String sql = "select id , u_pic , u_realname , u_sex from user_info where month(u_birthday) = month(now()) and day(u_birthday) = day(now())";
            List<UserInfo> users = queryChannelDao.selectManyAndTransformer(sql, UserInfo.class);
            for (UserInfo item : users) {
                try {
                    MessageDto messageDto = new MessageDto(KTP_TEAM_ID, Lists.newArrayList(item.getId()), "users");
                    messageDto.setTitle("生日提醒");
                    messageDto.putExt("myUserName", KTP_TEAM_NAME);
                    messageDto.putExt("type", "birthday");
                    messageDto.putExt("uPic", item.getU_pic());
                    messageDto.putExt("uRealname", item.getU_realname());
                    messageDto.putExt("uSex", item.getU_sex());
                    messageDto.putExt("uBirthday", DateUtil.format(new Date(), DateUtil.FORMAT_DATE));
                    HuanXinRequestUtils.sendExtendMessage(messageDto);
                } catch (Exception e) {
                    log.error("推送用户生日提醒异常，用户ID:" + item.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("推送用户生日提醒异常", e);
        }
    }


    /**
     * 身份证有效期提醒
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void validityOfIDCard() {
        try {
            String sql = "select id , us_name name , u_id userId , us_expire_time expireTime from user_sfz where datediff(us_expire_time,now()) = ?";
            List<UserSfz> users = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(45), UserSfz.class);
            for (UserSfz item : users) {
                try {
                    String date = new StringBuffer(item.getExpireTime()).insert(4, "-").insert(7, "-").toString();
                    MessageDto messageDto = new MessageDto(KTP_AIDE_ID, Lists.newArrayList(item.getUserId()), "users");
                    messageDto.setTitle("身份证有效期提醒");
                    messageDto.putExt("myUserName", KTP_AIDE_NAME);
                    messageDto.putExt("type", "idCardValidity");
                    messageDto.putExt("realName", item.getName());
                    messageDto.putExt("date", date);
                    HuanXinRequestUtils.sendExtendMessage(messageDto);
                } catch (Exception e) {
                    log.error("推送身份证有效期提醒异常，用户ID:" + item.getUserId(), e);
                }
            }
        } catch (Exception e) {
            log.error("推送身份证将过期提醒异常", e);
        }
    }


}
