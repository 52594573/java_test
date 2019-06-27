package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LinHon
 * @date 2018/12/29 09:54
 */
@Repository
public class MessageSwitchDao {

    private static final Logger log = LoggerFactory.getLogger(MessageSwitchDao.class);

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Autowired
    private SessionFactory sessionFactory;


    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 查询忽略提醒的用户ID
     *
     * @param projectId 项目ID
     * @param type      忽略类型 1:考勤预警信息 2:质量预警信息 3:安全文明预警信息 4:管理人员考核预警信息
     * @return
     */
    public List<Integer> selectUserIdsOfIgnore(int projectId, int type) {
        String hql = "select m_user_id from massage_switch where m_pro_id = ? and m_type_id = ? and m_status = ?";
        return queryChannelDao.selectMany(hql, Lists.newArrayList(projectId, type, 0));
    }


    /**
     * 筛选出忽略通知和不不略通知的用户
     *
     * @param projectId
     * @param type
     * @param pushUserIds
     * @return
     */
    public Map<String, List<Integer>> filter(int projectId, int type, List<Integer> pushUserIds) {
        Map result = Maps.newHashMap();
        List<Integer> ignoreUserIds = selectUserIdsOfIgnore(projectId, type);
        result.put("ignore", pushUserIds.stream().filter(userId -> ignoreUserIds.contains(userId)).collect(Collectors.toList()));
        result.put("notice", pushUserIds.stream().filter(userId -> !ignoreUserIds.contains(userId)).collect(Collectors.toList()));
        return result;
    }

}
