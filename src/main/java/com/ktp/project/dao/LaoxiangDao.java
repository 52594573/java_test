package com.ktp.project.dao;


import com.ktp.project.dto.LaoXiangDto.LaoXiangDto;
import com.ktp.project.dto.education.EducationCommentDto;
import com.ktp.project.dto.education.EducationDetailDto;
import com.ktp.project.dto.education.EducationKeyContentDto;
import com.ktp.project.dto.education.EducationListDto;
import com.ktp.project.entity.EducationLabelEntity;
import com.ktp.project.entity.EductionLikeEntity;
import com.ktp.project.entity.EductionLookEntity;
import com.zm.entity.UserInfo;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public class LaoxiangDao {
    private static final Logger log = LoggerFactory.getLogger("DataBaseDao");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 用户身份证
     */
    public UserInfo getIDCardById(int userId) {
        try {
            String queryString = "select id,u_sfz from user_info where id = ?";
            Query queryObject = getCurrentSession().createSQLQuery(queryString).setParameter(0, userId);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class));
            return (UserInfo) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("身份证获取错误", re);
            throw re;
        }
    }

    public List<LaoXiangDto> getLaoXiangChatFriendList(int page, int pageSize, int userId,String laoxiangStr,Date newBeginTime, Date newEndTime) {

        try {
            String sql =
                    "select ui.id userId," +
                            "u_nicheng uName," +
                            "u_sex uSex," +
                            "u_pic uPicture," +
                            "city uAddress"+
                            " from user_info ui " +
                            " left join sys_citys on sys_citys.id = left(u_sfz,6) "+
                            " where" +
                            " u_sfz like ?"+
                            " and ui.id not in (select left_uid from chat_friend where right_uid = ? and relationType = 3) "+
                            " and ui.id not in (select right_uid from chat_friend where left_uid = ? and relationType = 3)" +
                            " and ui.u_intime between ? and ?"
                    ;

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(LaoXiangDto.class));
            sqlQuery.setParameter(0, laoxiangStr+"%");
            sqlQuery.setParameter(1, userId);
            sqlQuery.setParameter(2, userId);
            sqlQuery.setParameter(3, newBeginTime);
            sqlQuery.setParameter(4, newEndTime);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);

            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


}
