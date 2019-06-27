package com.ktp.project.dao;

import com.ktp.project.dto.ProjectDeptDto;
import com.ktp.project.dto.ProjectDetailDto;
import com.ktp.project.dto.ProjectDto;
import com.ktp.project.dto.project.*;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectCommonDao {
    private static final Logger log = LoggerFactory.getLogger(ProjectCommonDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 获取项目具体基本信息
     */
    public ProMessage getProjectMessage(Integer proId) {
        try {
            String sql = "select " +
                    "pro.p_name proName," +
                    "pro.p_pkCorpCore proPkCorp," +
                    "pro.p_corpNameCore proCorpName," +
                    "pro.p_corpIdCodeCore proCorpId," +
                    "pro.p_lbs_y proLongitude," +
                    "pro.p_lbs_x proLatitude," +
                    "tcity.cityname proCityName," +
                    "uinfo.u_name proPrincipalPhone," +
                    "uinfo.u_realname proPrincipal," +
                    "pro.p_content proContent," +
                    "pro.p_begintime proBeginTime," +
                    "pro.p_endtime proEndTime," +
                    "pro.p_logo proLogo," +
                    "pro.p_state proStatus," +
                    /*"pro. proDevelopers," +*/
                    "pro.p_lbs_fw proRange," +
                    "pro.p_address proAddress," +
                    "pro.p_renshu proPeopleNum," +
                    "pro.p_banzushu proGroupNum," +
                    "pro.p_kaoqinlv proAttendanceRate" +
                    " from project pro " +
                    " left join tianqi_city tcity on pro.p_city = tcity.cityid" +
                    " left join user_info uinfo on pro.p_principal = uinfo.id" +
                    " where pro.id = 22 and pro.is_del = 0";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(ProMessage.class));
            queryObject.setParameter(0, proId);
            return (ProMessage) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取项目具体基本信息sql异常", re);
            throw re;
        }

    }

    /**
     * 获取班组基本信息
     */
    public PoMessage getPojectMessage(Integer proId) {
        try {
            String sql = "select" +
                    " porgan.id poId," +
                    "porgan.po_name poName," +
                    "porgan.po_fzr poMonitorId," +
                    "uinfo.u_realname poMonitorName," +
                    "porgan.pro_id poProId," +
                    "project.p_name poProName," +
                    "porgan.po_pkCorpFinance poPkCorpFinance," +
                    "porgan.po_corpNameFinance poCorpNameFinance," +
                    "porgan.po_corpIdCodeFinance poCorpIdCodeFinance" +
                    " from pro_organ porgan " +
                    " left join user_info uinfo on uinfo.id = porgan.po_fzr" +
                    " left join project on project.id = porgan.pro_id " +
                    " where porgan.id = 175 and porgan.po_state = 2";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(PoMessage.class));
            queryObject.setParameter(0, proId);
            return (PoMessage) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取班组具体基本信息sql异常", re);
            throw re;
        }

    }

    /**
     * 获取班组人员基本信息
     */
    public PoUser getPoUser(Integer proId,Integer poId) {
        try {
            String sql = "select pop.po_id poId," +
                    "pop.user_id uId," +
                    "pop.pop_state uStatus," +
                    "kcontent.key_name uTypeName," +
                    "uinfo.u_jnf uSkillScore," +
                    "uinfo.u_xyf  uCreditScore," +
                    "pop.pop_intime uIntime," +
                    "pop.pop_endtime uEndtime," +
                    "pop.pop_card uCard," +
                    "pop.pop_pic1 uFacePicture," +
                    "pop.pop_pic2 uIDCardFront," +
                    "pop.pop_pic3 uIDCardReverse," +
                    "pop.pop_pic4 uBustShot," +
                    "pop.pop_sgzt uSgzt," +
                    "pop.popBankid uBankId," +
                    "porgan.po_name uPoName," +
                    "uinfo.u_sfz uIDCard," +
                    "uinfo.u_realname uName," +
                    "uinfo.u_sex uSex," +
                    "kcontent1.key_name," +
                    "uinfo.u_birthday uBirthday," +
                    "uinfo.u_name uPhone," +
                    "uwork.w_resi uAddress," +
                    "kcontent2.key_name" +
                    " from pro_organ_per pop" +
                    " left join user_info uinfo on pop.user_id = uinfo.id" +
                    " left join key_content kcontent on pop.p_type = kcontent.id" +
                    " left join pro_organ porgan on pop.po_id = porgan.id " +
                    " left join user_work uwork on pop.user_id = uwork.u_id" +
                    " left join key_content kcontent1 on uwork.w_nation = kcontent1.id" +
                    " left join key_content kcontent2 on uwork.w_edu = kcontent2.id" +
                    " where pop.po_id = ?";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(PoUser.class));
            queryObject.setParameter(0, poId);
            return (PoUser) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取班组具体基本信息sql异常", re);
            throw re;
        }

    }

    /**
     *人员考勤
     */
    public KaoqinUser getClocking(String biaoming ,Integer proId, Integer uId) {
        try {
            String sql = "select kq.k_lbsy kLongitude," +
                    "kq.k_lbsx kLatitude," +
                    "kq.k_time kTime," +
                    "kq.k_card kCard," +
                    "kq.k_jihao kJihao," +
                    "kq.k_xsd kXsd," +
                    "kq.k_pic kPic" +
                    " from ? kq" +
                    " where kq.pro_id = ? and kq.user_id = ?";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(KaoqinUser.class));
            queryObject.setParameter(0, biaoming);
            queryObject.setParameter(1, proId);
            queryObject.setParameter(2, uId);
            return (KaoqinUser) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取班组具体基本信息sql异常", re);
            throw re;
        }

    }

    /**
     *人员工资
     */
    public WageUser getWageUser(Integer proId,Integer poId, Integer uId) {
        try {
            String sql = "";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(WageUser.class));
            queryObject.setParameter(0, proId);
            queryObject.setParameter(1, poId);
            queryObject.setParameter(2, uId);
            return (WageUser) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取班组具体基本信息sql异常", re);
            throw re;
        }

    }
}
