package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.ktp.project.dao.ModifyChannelDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AuthRealNameLogService {

    @Autowired
    private ModifyChannelDao modifyChannelDao;
    @Autowired
    private QueryChannelDao queryChannelDao;

    public void saveOrUpdate(AuthRealNameLog entiry){
        modifyChannelDao.saveOrUpdate(entiry);
    }

    public AuthRealNameLog createBaseBean(Integer projectId, Integer userId, String url, String remark){
        AuthRealNameLog authRealNameLog = new AuthRealNameLog();
        authRealNameLog.setProjectId(projectId);
        authRealNameLog.setUserId(userId);
        authRealNameLog.setResMsg("成功");
        authRealNameLog.setReqUrl(url);
        authRealNameLog.setIsSuccess(1);
        authRealNameLog.setId(null);
        authRealNameLog.setCreateTime(new Date());
        authRealNameLog.setRemark(remark);
        return authRealNameLog;
    }

    public AuthRealNameLog createBaseBean(Integer projectId, Integer userId, String remark){
        return createBaseBean(projectId, userId, null, remark);
    }
    public AuthRealNameLog createBaseBean(Integer projectId, Integer userId){
        return createBaseBean(projectId, userId, null, null);
    }

    public AuthRealNameLog queryOneByUserIdAndUrl(Integer userId, String url) {
        String sql = "SELECT id, project_id AS projectId, user_id AS userId, req_url AS reqUrl, req_body AS reqBody " +
                ", is_success AS isSuccess, res_msg AS resMsg, create_time AS createTime, remark " +
                "FROM auth_real_name_log " +
                "WHERE user_id = ? " +
                "AND req_url like ? " +
                "ORDER BY id DESC " +
                "LIMIT 1 ";
        AuthRealNameLog one = null;
        try {
            one = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(userId, "%" + url + "%"), AuthRealNameLog.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过用户ID:%S, 请求路径:%s,查询AuthRealNameLog失败", userId, url));
        }
        return one;
    }

    public AuthRealNameLog findOne(Integer id){
        return queryChannelDao.queryOne("from AuthRealNameLog where id = ?", Lists.newArrayList(id), AuthRealNameLog.class);
    }
}
