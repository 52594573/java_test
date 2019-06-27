package com.ktp.project.dao;


import com.ktp.project.dto.education.*;
import com.ktp.project.entity.*;
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
public class EducationDao {
    private static final Logger log = LoggerFactory.getLogger("DataBaseDao");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public List<EducationListDto> getEducationList(int page, int pageSize, int userId,int typeId,String searchKey) {
        boolean keyStatus = false;
        boolean typeStatus = typeId != 0 ;
        boolean loginStatus = true;
        if(userId == 0){
            loginStatus = false;
        }
        if(!searchKey.isEmpty()){
            keyStatus = true;
        }

        try {
            String sql =
            "select "+
            "ev.id id,"+
            "etype.name vTypeName,"+
            "ev.v_name vName,"+
            "ev.v_long vLong,"+
            "(select count(1) from edu_like elike where elike.v_id = ev.id and elike.l_status = 1) vLikeNum,"+
            "(select count(1) from edu_look elook where elook.v_id = ev.id ) vLearnNum,"+
            "(select count(1) from edu_comment ec where ec.v_id = ev.id ) vCommentNum,"+
            " ev.v_picture_url vPictureUrl,"+
            (loginStatus ? " el.l_status+0 vLike" : "0 vLike")+
            /*" el.l_status vLike"+*/
            " from edu_video ev"+
            " left join edu_type etype on etype.id = ev.v_type_id"+
            " left join edu_like el on (el.v_id = ev.id and el.user_id = :userIdName)"+
            " where 1=1"+
            (typeStatus ? " and etype.id = :typeIdName" : "")+
            (keyStatus ? " and (ev.v_name like :searchKey or ev.v_supply like :searchKey or ev.v_content like :searchKey)" : "")+
            " order by ev.v_build_time desc";

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(EducationListDto.class));
            if (typeStatus) {
                sqlQuery.setParameter("typeIdName", typeId);
            }
            sqlQuery.setParameter("userIdName", userId);
            if (keyStatus) {
                sqlQuery.setParameter("searchKey", "%" + searchKey + "%");
            }
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);

            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


    public EducationDetailDto getEducationList(int id,int userId) {
        //是否登陆，没登陆就是0
        boolean loginStatus = true;
        if(userId==0) {
            loginStatus = false;
        }
        try {
            String sql = "select ev.id,"+
            "etype.name vTypeName,"+
            "ev.v_name vName,"+
            "(select count(1) from edu_like elike where elike.v_id = ev.id and elike.l_status = 1) vLikeNum,"+
            "(select count(1) from edu_look elook where elook.v_id = ev.id ) vLearnNum,"+
            "ev.v_supply vSupply,"+
            "ev.v_picture_url vPictureUrl,"+
            "ev.v_url vUrl,"+
            (loginStatus ? " edl.l_status+0 vLike," : "0 vLike,")+
            /*"IFNULL(sum(edl.l_status),0) vLike,"+*/
            "ev.v_content vContent,"+
            "ev.v_long vLong"+
            " from edu_video ev"+
            " left join edu_type etype on etype.id = ev.v_type_id"+
            " left join edu_like edl on (ev.id = edl.v_id and edl.user_id = :userId)"+
            " where ev.id = :Id";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EducationDetailDto.class));
            queryObject.setParameter("userId", userId);
            queryObject.setParameter("Id", id);
            //Object o =  queryObject.uniqueResult();
            return (EducationDetailDto) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("教育视频详情", re);
            throw re;
        }
    }
    /**
     *标签list
     */
    public List<EducationLabelEntity> getEducationLabelListById(int id) {

        try {
            String sql =
                    "select "+
                            "el.name name"+
                            " from edu_label el"+
                            " left join edu_video_label evl on el.id = evl.l_id"+
                            " where evl.v_id = ? and el.status = 1";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(EducationLabelEntity.class));
            sqlQuery.setParameter(0, id);

            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public  List<EducationKeyContentDto> getvideoType() {
        try {
            String sql = "select id,"+
                    "name tName"+
                    " from edu_type" +
                    " where status = 1  ";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EducationKeyContentDto.class));
            return queryObject.list();

        } catch (RuntimeException re) {
            log.error("find all key_contend failed", re);
            throw re;
        }
    }

    /**
     *  获取视频类型
     */
    public EductionLikeEntity getEductionLike(int id,int userId) {
        try {
            String sql = "select el.id,"+
                    "el.user_id userId,"+
                    "el.v_id vId,"+
                    "el.l_status lStatus"+
                    " from edu_like el"+
                    " where el.v_id = ? and el.user_id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EductionLikeEntity.class));
            queryObject.setParameter(0, id);
            queryObject.setParameter(1, userId);
            //Object o =  queryObject.uniqueResult();
            return (EductionLikeEntity) queryObject.uniqueResult();

        } catch (RuntimeException re) {
            log.error("查找EductionLikeEntity异常", re);
            throw re;
        }
    }

    /**
     * 更新点赞信息
     *
     * @param:
     * @return:
     */
    public int updateEductionLike(int id, int user_id,int status) {
        try {
            String sql = "update edu_like el set el.l_status = ? where el.v_id = ? and el.user_id = ?";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setInteger(0, status);
            query.setParameter(1, id);
            query.setParameter(2, user_id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("更新点赞信息", re);
            return 0;
        }
    }

    /**
     * 插入点赞信息
     *
     * @param:
     * @return:
     */
    public int insertEductionLike(int id, int user_id) {
        try {
            String sql = "insert into edu_like (v_id,user_id) values (?,?)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, id);
            query.setParameter(1, user_id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("更新点赞信息", re);
            return 0;
        }
    }

    /**
     * 插入评论信息
     *
     * @param:
     * @return:
     */
    public int saveVideoComment(int id, int user_id, String cContent, Date date) {
        try {
            String sql = "insert into edu_comment (v_id,user_id,c_content,c_time) values (?,?,?,?)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, id);
            query.setParameter(1, user_id);
            query.setParameter(2, cContent);
            query.setParameter(3, date);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("插入评论信息", re);
            return 0;
        }
    }

    /**
     *  获取观看视频
     */
    public EductionLookEntity getEductionLook(int id,int userId) {
        try {
            String sql = "select el.id,"+
                    "el.user_id userId,"+
                    "el.v_id vId,"+
                    "el.l_num lNum,"+
                    "el.l_long lLong,"+
                    "el.l_time lTime"+
                    " from edu_look el"+
                    " where el.v_id = ? and el.user_id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EductionLookEntity.class));
            queryObject.setParameter(0, id);
            queryObject.setParameter(1, userId);
            //Object o =  queryObject.uniqueResult();
            return (EductionLookEntity) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查找EductionLookEntity异常", re);
            throw re;
        }
    }

    /**
     * 插入观看视频信息
     *
     * @param:
     * @return:
     */
    public int insertEductionLook(int id, int user_id,int tLong) {
        try {
            String sql = "insert into edu_look (user_id,v_id,l_num,l_long,l_time) values (?,?,?,?,?)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, user_id);
            query.setParameter(1, id);
            query.setParameter(2, 1);
            query.setParameter(3, tLong);
            query.setParameter(4, new Date());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("插入观看视频", re);
            return 0;
        }
    }

    /**
     * 更新观看视频
     *
     * @param:
     * @return:
     */
    public int updateEductionLook(int id, int user_id,int num,int tLong) {
        try {
            String sql = "update edu_look el set el.l_num = ?,el.l_long = ?,el.l_time = ? " +
                    " where el.v_id = ? and el.user_id = ?";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setInteger(0, num);
            query.setParameter(1, tLong);
            query.setParameter(2, new Date());
            query.setParameter(3, id);
            query.setParameter(4, user_id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("更新观看视频", re);
            return 0;
        }
    }
    /**
     *获取评论列表
     */
    public  List<EducationCommentDto> getVideoComment(int id,int page,int pageSize) {
        try {
            String sql = "select (select count(1) from edu_comment ecomment where ecomment.v_id = ?) cNum,"+
                    "ui.id userId,"+
                    "ui.u_realname cName,"+
                    "ui.u_pic cUrl,"+
                    "ui.u_sex cSex,"+
                    "ec.c_content cContent,"+
                    "ec.c_time cTime "+
                    " from edu_comment ec" +
                    " left join user_info ui on ec.user_id = ui.id "+
                    " where ec.v_id = ? " +
                    " order by c_time desc";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EducationCommentDto.class));
            queryObject.setParameter(0, id);
            queryObject.setParameter(1, id);
            queryObject.setFirstResult(page);
            queryObject.setMaxResults(pageSize);
            return queryObject.list();

        } catch (RuntimeException re) {
            log.error("find all key_contend failed", re);
            throw re;
        }
    }

    /**
     * 点赞数量
     */
    public BigInteger queryEducationLikeNumById(int id) {
        try {
            String queryString = "select count(*) from edu_like where v_id = ? and l_status = 1";
            //SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).setParameter(0, id);
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setInteger(0, id);
            return (BigInteger) query.uniqueResult();
        } catch (RuntimeException re) {
            log.error("点赞数量", re);
            throw re;
        }
    }

    /**
     *  分享视频
     */
    public EducationH5Dto getEducationShareH5ById(int id) {
        try {
            String sql =  "select ev.v_name vName," +
                    "ev.v_picture_url pUrl," +
                    "ev.v_url vUrl," +
                    " ev.v_supply vSupplyName," +
                    " ev.v_content vContent," +
                    "(select count(1) from edu_look elook where elook.v_id = ev.id ) vLearnNum," +
                    "(select count(1) from edu_like elike where elike.v_id = ev.id and l_status = 1) vLikeNum," +
                    "(select count(1) from edu_comment ecomment where ecomment.v_id = ev.id and c_status = 1) vCommentNum" +
                    " from edu_video ev where ev.id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EducationH5Dto.class));
            queryObject.setParameter(0, id);
            return (EducationH5Dto) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查找EducationH5Dto异常", re);
            throw re;
        }
    }

    /**
     *  视频评论列表
     */
    public  List<EducationH5CommentDto> getEducationCommentListById(int id) {
        try {
            String sql = "select ui.u_pic uUrl," +
                    "ui.u_nicheng uName," +
                    "ui.u_sex uSex," +
                    "ec.c_content uComment," +
                    "DATE_FORMAT(ec.c_time,'%Y-%c-%d %H:%i:%s') uTime  " +
                    " from edu_comment ec" +
                    " left join user_info ui on ec.user_id = ui.id" +
                    " where ec.v_id = ? and ec.c_status = 1 " +
                    " order by ec.c_time desc ";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(EducationH5CommentDto.class));
            queryObject.setParameter(0, id);
            return queryObject.list();

        } catch (RuntimeException re) {
            log.error("find all key_contend failed", re);
            throw re;
        }
    }

}
