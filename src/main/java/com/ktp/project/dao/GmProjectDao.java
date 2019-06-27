package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.ktp.project.dto.AuthRealName.GmProjectInfo;
import com.ktp.project.dto.AuthRealName.PoInfo;
import com.ktp.project.dto.AuthRealName.TeamAndUserInfo;
import com.ktp.project.dto.AuthRealName.ZhzhCaiJiDto;
import com.ktp.project.dto.LaoXiangDto.LaoXiangDto;
import com.ktp.project.entity.ChatFriend;
import com.ktp.project.entity.GmProjectInfoEntity;
import com.ktp.project.entity.KtpCompanyInfoEntity;
import com.ktp.project.entity.KtpProjectInfoEntity;
import com.zm.entity.ProOrgan;
import com.zm.friendCircle.entity.ZmNetworkArticle;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 网络内容表
 *
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 15:30
 */
@Repository("GmProjectDao")
public class GmProjectDao {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private QueryChannelDao queryChannelDao;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void saveOrUpdateGmProject(GmProjectInfoEntity gmProjectInfoEntity) {
        getCurrentSession().saveOrUpdate(gmProjectInfoEntity);
    }

    public void updateGmProject(GmProjectInfoEntity gmProjectInfoEntity) {
        String sql = "update  ktp_project_out set team_code='" + gmProjectInfoEntity.getTeam_code() + "'  where  id=" + gmProjectInfoEntity.getId();
        getCurrentSession().createSQLQuery(sql).executeUpdate();
    }

    /**
     * 根据项目id查询对应关系表
     *
     * @param project_id 项目id
     * @param type       所属地区
     * @return
     */
    public GmProjectInfo getGmProjectInfo(int project_id, String type) {
        try {
            String sql = " select id, corp_code  corpCode ,corp_name  corpName  ,project_code projectCode,project_id  projectId,team_code teamCode ,region_code  regionCode " +
                    " ,date_format(corp_intime,'%Y-%m-%d %H:%i:%S')  corpIntime , date_format(corp_intime,'%Y-%m-%d')  corpIntimeDay ,  corp_intime  corp_intime_date from  ktp_project_out " +
                    " where   project_id = ?  and  region_code=?   ";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(GmProjectInfo.class));
            sqlQuery.setParameter(0, project_id);
            sqlQuery.setParameter(1, type);
            return (GmProjectInfo) sqlQuery.uniqueResult();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }


    /** 
    *
    * @Description: 获取采集人员信息
    * @Author: liaosh
    * @Date: 2019/1/18 0018 
    */ 
    public ZhzhCaiJiDto getZhCJ(int userId) {
        try {
            String sql = "select ui.u_realname Name, " +
                    "ui.u_sfz CerfNum, " +
                    "kc.key_name Nation, " +
                    "uw.w_native Native, " +
                    "CASE ui.u_sex " +
                    "WHEN 2 THEN 1 " +
                    "WHEN 1 THEN 0 " +
                    "ELSE 0  " +
                    "END AS Sex, " +
                    "usfz.us_address IdCardAddress, " +
                    "usfz.us_birth_year byear, " +
                    "usfz.us_birth_month bmonth , " +
                    "usfz.us_birth_day bday, " +
                    "ui.u_cert_pic CollectPhoto, " +
                    "ui.u_pic IdCardPhoto, " +
                    "usfz.us_org IssuingAuthority, " +
                    "DATE_FORMAT(usfz.us_start_time,'%Y.%c.%d') stime, " +
                    "DATE_FORMAT(usfz.us_expire_time,'%Y.%c.%d') etime " +
                    " from user_info ui  " +
                    "left join user_sfz usfz on usfz.u_id = ui.id  " +
                    "left join user_work uw on ui.id = uw.id " +
                    "left join key_content kc on kc.id = uw.w_nation " +
                    "where ui.id = ?";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ZhzhCaiJiDto.class));
            sqlQuery.setParameter(0, userId);
            return (ZhzhCaiJiDto) sqlQuery.uniqueResult();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }

   /* public List<Integer> queryIdByPro(Integer projectId){
        String sql = "select pop.user_id from project pro left join pro_organ po on pro.id = po.pro_id left join pro_organ_per pop on po.id = pop.po_id where pro.id = "+projectId+" and pop.pop_state = 0";
        return queryChannelDao.selectMany(sql);
        //return queryChannelDao.selectMany("select id from kaoqin" + projectId + " where user_id != 0 order by id desc limit 5000");
    }*/

    /**
     *
     * 项目人员
     * @param projectId
     * @param
     * @return
     */
    public List<Integer> queryIdByPro(int projectId) {
        String hql = "select pop.user_id from project pro left join pro_organ po on pro.id = po.pro_id left join pro_organ_per pop on po.id = pop.po_id where pro.id = ? and pop.pop_state = 0";
        return queryChannelDao.selectMany(hql, Lists.newArrayList(projectId));
    }

    /**
     * 根据项目用户查询班组id
     * @param poId
     * @return
     */
    public String findteamSysNo(int poId) {
        String sql = "select team_sysNo  from  ktp_po_out where po_id=" +poId;
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        return (String) sqlQuery.uniqueResult();
    }


    /** 
    *
    * @Description: 保存中山对接班组code  
    * @Author: liaosh
    * @Date: 2019/1/15 0015 
    */ 
    
    /**
     * 根据班组id查询对应关系表
     *
     * @param poid 项目id
     * @param type       所属地区
     * @return
     */
    public PoInfo getTeamSysNo(int poid, String type) {
        try {
            String sql = " select id,team_sysNo teamSysNo , po_id poId ,region_code  regionCode  from  ktp_po_out " +
                    " where   po_id = ?  and  region_code=?   ";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(PoInfo.class));
            sqlQuery.setParameter(0, poid);
            sqlQuery.setParameter(1, type);
            return (PoInfo) sqlQuery.uniqueResult();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }

    /**
     * 获取承建信息
     *
     * @param project_id
     * @return
     */
    public List<KtpProjectInfoEntity> getProjectInfo(int project_id) {

        try {
            String sql = " select id, contractor_corp_code  ,contractor_corp_name   ,project_id  ,build_corp_code ,build_corp_name ,builderLicenses ,area_code  from  ktp_project_info " +
                    " where  project_id = ?    ";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(KtpProjectInfoEntity.class));
            sqlQuery.setParameter(0, project_id);
            return (List<KtpProjectInfoEntity>) sqlQuery.list();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }

    /**
     * 获取企业信息列表
     *
     * @return
     */
    public List<KtpCompanyInfoEntity> getKtpCompanyInfo() {
        try {
            String sql = " select id, corp_code  ,corp_name ,area_code  ,register_date ,product_no ,compamy_type  from  ktp_company_Info ";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(KtpCompanyInfoEntity.class));
            return (List<KtpCompanyInfoEntity>) sqlQuery.list();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }

    /**
     * 根据项目id查询所有的班组id与用户id
     * --排除掉 班组存在的用户，用户表里不存在的用
     *
     * @return
     */
    public List<TeamAndUserInfo> getTeamAndUserInfo(Integer project_id) {
        try {
            String sql = "select  pro.id as teamId ,per.user_id  as userId ,pro.po_name as poName   from  project  pr LEFT JOIN  pro_organ pro on pr.id=pro.pro_id LEFT JOIN  pro_organ_per per on pro.id=per.po_id  " +
                    "LEFT JOIN  user_info u on per.user_id=u.id    " +
                    "where pr.id=" + project_id + " and pro.po_state=2  and per.pop_state=0  and per.user_id is not null  and u.id is not null  " +
                    " ORDER BY pro.id   ";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TeamAndUserInfo.class));
            return (List<TeamAndUserInfo>) sqlQuery.list();
        } catch (RuntimeException re) {
            //log.error("查询失败", re);
            throw re;
        }
    }


    //查询 班组名字    根据班组id
    public ProOrgan getTeamname(int id) {
        try {
            String queryString = "select po.id id,po.po_state poState, po.po_gzid poGzid, po.po_name poName FROM pro_organ po  " +
                    " WHERE  po.id = ?";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(ProOrgan.class));
            query.setParameter(0, id);
            return (ProOrgan) query.uniqueResult();
        } catch (Exception e) {
            return null;
        }

    }


}
