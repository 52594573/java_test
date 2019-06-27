package com.ktp.project.dao;

import com.ktp.project.dto.BenefitDto.CommentAndApply;
import com.ktp.project.dto.*;
import com.ktp.project.entity.*;
import com.ktp.project.po.DonateActSearchPojo;
import com.ktp.project.util.Page;
import com.ktp.project.util.PageUtil;
import com.zm.entity.UserInfo;
import org.apache.http.util.TextUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 公益
 *
 * @Author: liaosh
 * @Date: 2018/8/22 0022
 */
@Repository
public class AnnouncementListDao {
    private static final Logger log = LoggerFactory.getLogger("AnnouncementListDao");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<WordListDto> getAnnouncementList(int page, int pageSize, int proId,int userId) {
        try {
            String sql = "select " +
                    " wl.id," +
                    " wl.wl_title wlTitle," +
                    " wl.wl_content wlContent," +
                    " wl.wl_intime wlIntime," +
                    " wl.wl_author wlAuthor," +
                    " ui.u_realname wlUserName"+
                    " from word_list wl " +
                    " left join user_info ui on wl.wl_sysuid = ui.id" +
                    "  where wl.is_del = 0 and " +
                    " (wl.wl_status = 1 or wl.wl_sysuid = ?) and" +
                    " (wl.wl_proid = ? or wl.wl_proid = 0) " +
                    " and wl.wl_sort = 28 and is_home=2 order by wl.wl_intime desc";

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WordListDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setParameter(1, proId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("find all WordList failed", re);
            throw re;
        }
    }

    /**
     * 插入公告
     *
     * @param:
     * @return:
     */
    public int insertWordList(WordList wordList) {
        try {
            String sql = "insert into word_list (wl_content,wl_intime,wl_proid,wl_sort,wl_title,wl_sysuid,wl_author,wl_status,is_home) values (?,?,?,?,?,?,?,?,2)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, wordList.getWlContent());
            query.setParameter(1, wordList.getWlIntime());
            query.setParameter(2, wordList.getWlProid());
            query.setParameter(3, wordList.getWlSort());
            query.setParameter(4, wordList.getWlTitle());
            query.setParameter(5, wordList.getWlUserid());
            query.setParameter(6, wordList.getWlAuthor());
            query.setParameter(7, 0);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("插入公告信息", re);
            return 0;
        }
    }

    /**
     * 获取公告详情
     */
    public WordListDto getAnnouncementDetails(int id) {
        try {
            String sql = "select " +
                    " wl.id," +
                    " wl.wl_title wlTitle," +
                    " wl.wl_title2 wlTitle2," +
                    " wl.wl_sort wlSort," +
                    " wl.wl_content wlContent," +
                    " wl.wl_pic wlPic," +
                    " wl.wl_source wlSource," +
                    " wl.wl_author wlAuthor," +
                    " wl.wl_sysuid wlSysuid," +
                    " wl.wl_intime wlIntime," +
                    " wl.wl_lasttime wlLasttime," +
                    " wl.wl_proid wlProid," +
                    " wl.wl_userid wlUserid," +
                    " wl.wl_userintime wlUserintime," +
                    " wl.wl_key wlKey," +
                    " ui.u_nicheng uNiCheng," +
                    " ui.u_name uPhone," +
                    " ui.u_realname uRealname,"+
                    " ui1.u_nicheng userNiCheng," +
                    " ui1.u_name userPhone," +
                    " ui1.u_realname userRealname"+
                    " from word_list wl " +
                    " left join user_info ui on wl.wl_sysuid = ui.id" +
                    " left join user_info ui1 on wl.wl_userid = ui1.id" +
                    "  where wl.is_del = 0 and wl.id = ?";


            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(WordListDto.class));
            queryObject.setParameter(0, id);
            return (WordListDto) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取公告详情异常", re);
            throw re;
        }
    }


    public List<WordListDto> getAnnouncementListS(int proId) {
        try{
            String sql = "SELECT\n" +
                    "\twl.id,\n" +
                    "\twl.wl_title wlTitle,\n" +
                    "\twl.wl_content wlContent,\n" +
                    "\twl.wl_intime wlIntime,\n" +
                    "\twl.wl_author wlAuthor,\n" +
                    "\tui.u_realname wlUserName,\n" +
                    "\tws.ws_name wsName," +
                    "wl.wl_sort sort \n" +
                    "FROM\n" +
                    "\tword_list wl\n" +
                    "LEFT JOIN user_info ui ON wl.wl_sysuid = ui.id\n" +
                    "LEFT JOIN word_sort ws ON wl.wl_sort = ws.id\n" +
                    "WHERE\n" +
                    "\twl.is_del = 0\n" +
                    "AND wl.wl_status = 1\n" +
                    "AND (\n" +
                    "\twl.wl_proid = 0\n" +
                    "\tOR wl.wl_proid = ?\n" +
                    ")\n" +
                    "AND wl.wl_sort = 28\n" +
                    "ORDER BY\n" +
                    "\twl.wl_sort DESC";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WordListDto.class));
            sqlQuery.setParameter(0, proId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
        log.error("find all WordList failed", re);
        throw re;
    }
    }

    public List<Map<String,Object>> getWordSort() {
        String sql = "SELECT id,ws_name FROM word_sort WHERE sort!=0 ORDER BY sort LIMIT 0,3";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    public List<Map<String, Object>> getAnnouncement(String stringBuffer) {
        String sql = "SELECT wl_title title,wl_title2 title2,wl_content content,wl_pic pic,url,fb_intime fbIntime  FROM word_list WHERE  is_home=0 and wl_sort in("+stringBuffer+") ORDER BY fb_intime";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    public List<Map<String, Object>> getAnnouncementListShou(Page page) {
        long current = page.getPageNo();
        long size = page.getPageSize();
        current=(current-1)*size;
        String sql = "SELECT wl_title title,wl_title2 title2,wl_content content,wl_pic pic,url,fb_intime fbIntime  FROM word_list WHERE is_home=0 ORDER BY fb_intime desc  limit "+current+","+size;
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    public Map<String, Object> getResult(Integer id) {
        String sql = "SELECT wl_title title,wl_title2 title2,wl_content content,wl_pic pic,url,fb_intime fbIntime  FROM word_list WHERE  is_home=0 and wl_sort ="+id+" ORDER BY fb_intime desc limit 0,1";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map<String, Object>) sqlQuery.uniqueResult();
    }
}
