package com.ktp.project.dao;

import com.ktp.project.dto.WorkLogDetail;
import com.ktp.project.dto.WorkLogQualityPushDto;
import com.ktp.project.entity.KeyContent;
import com.ktp.project.entity.MassageSwitch;
import com.ktp.project.entity.WorkLog;
import com.ktp.project.entity.WorkLogSendInfo;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * autor:hjl
 * create date:2018-12-12 9:15
 */
@Repository
@Transactional
public class WorkLogPushDao {


    private static Logger log = LoggerFactory.getLogger(WorkLogPushDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    //查询消息免打扰
    public MassageSwitch querymassageSwitch(int proid, String m_type_id, String user_id) {
        try {
            String queryString = "SELECT ms.id id,ms.m_config_id mConfigId, ms.m_user_id mUserId,\n" +
                    "                     ms.m_role_id mRoleId,ms.m_app_id mAppId,ms.m_status mStatus,ms.m_type_id mTypeId, \n" +
                    "                     ms.m_name mName,ms.m_pro_id mProId FROM massage_switch ms where ms.m_pro_id =? and ms.m_user_id = ? and ms.m_type_id =?";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(MassageSwitch.class));
            query.setParameter(0, proid);
            query.setParameter(1, Integer.parseInt(user_id));
            query.setParameter(2, Integer.parseInt(m_type_id));
            return (MassageSwitch) query.uniqueResult();
        } catch (Exception re) {
            re.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s,查询失败", proid));
        }
    }

    /**
     * 根据项目id查询项目经理 和生成经理
     */
    public List<Object> getUserList(Integer projectId) {
        try {
            String queryString = "SELECT po.user_id from  pro_organ pro INNER JOIN\n" +
                    "pro_organ_per po on pro.id = po.po_id \n" +
                    "where (po.p_type=120 OR po.p_type=118)  AND pro.pro_id=" + projectId;
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("select count failed", re);
            throw re;
        }
    }


    //根据项目id查询项生成经理
    public List<Object> getUserListOrddo(Integer projectId) {
        try {
            String queryString = "SELECT po.user_id from  pro_organ pro INNER JOIN\n" +
                    "pro_organ_per po on pro.id = po.po_id \n" +
                    "where  po.p_type=118  AND pro.pro_id=" + projectId;
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("select count failed", re);
            throw re;
        }
    }


    //查询真实姓名
    public String queryRealname(int id) {
        String queryString = "select u_realname FROM  user_info   WHERE  id = ?";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class));
        query.setParameter(0, id);
        UserInfo userInfo = (UserInfo) query.uniqueResult();
        if (userInfo != null) {
            return userInfo.getU_realname();
        } else {
            return "";
        }
    }


    //查询施工人全部信息   根据多个工人id
    public List<WorkLogDetail.GrListBean> queryGrListBeanbyworks(String ids, String proid) {

        try {

            Project project = queryProject(proid);
            String proName = "";
            if (project != null) {
                proName = project.getPName();
            }

            String queryString = "select ui.id,\n" +
                    "ui.id as user_id,\n" +
                    "ui.u_name,\n" +
                    "ui.u_pic,\n" +
                    "ui.u_sfz,\n" +
                    "ui.u_realname,\n" +
                    "ui.u_introduce,\n" +
                    "ui.u_type,\n" +
                    "ui.u_state,\n" +
                    "ui.u_birthday,\n" +
                    "ui.u_cert,\n" +
                    "ui.u_cert_type,\n" +
                    "ui.u_cert_pic,\n" +
                    "ui.u_sex,\n" +
                    "ui.u_proid,\n" +
                    "ui.u_up_proid,\n" +
                    "ui.u_sfzpic,\n" +
                    "ui.u_yhkpic,\n" +
                    "ui.u_lbs,\n" +
                    "ui.u_star,\n" +
                    "ui.u_isfujin,\n" +
                    "ui.u_nicheng,\n" +
                    "ui.last_device,\n" +
                    "ui.u_intime,\n" +
                    "ui.u_lastime,\n" +
                    "ui.logout_time FROM user_info ui \n" +
                    "WHERE ui.id in(" + ids + ") ";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class));
//            query.setParameter(0, ids);
            List<UserInfo> userInfos = query.list();
            List<WorkLogDetail.GrListBean> grListBeanList = new ArrayList<>();

            if (userInfos != null && userInfos.size() > 0) {
                for (UserInfo us : userInfos) {
                    ProOrgan proOrgan = queryProOrganTeambyidandproid(us.getId(), Integer.parseInt(proid));
                    WorkLogDetail.GrListBean grListBean = new WorkLogDetail.GrListBean();
                    grListBean.setGr_user_id(us.getId() + "");
                    grListBean.setGr_u_name(us.getU_name() + "");
                    grListBean.setGr_u_pic(us.getU_pic() + "");
                    grListBean.setGr_u_realname(us.getU_realname() + "");
                    grListBean.setGr_pro_id(proid);
                    if (!StringUtil.isEmpty(proName)) {
                        grListBean.setGr_p_name(proName);
                    }
                    if (proOrgan != null) {
                        grListBean.setGr_po_id(proOrgan.getId() + "");
                        grListBean.setGr_po_name(proOrgan.getPoName() + "");
                        //查询班组长，工种
                        Integer zzid = queryWorkOrdID(proOrgan.getId());
                        if (zzid != null && zzid > 0) {
                            String bzrealname = queryRealname(zzid);
                            if (!StringUtil.isEmpty(bzrealname)) {
                                grListBean.setGr_banzuzhang(bzrealname);
                            }
                            //工种
                            String agz = queryWorkType(proOrgan.getId());
                            if (!StringUtil.isEmpty(agz)) {
                                grListBean.setGr_gongzhong(agz);
                            }
                        }
                    }
                    grListBeanList.add(grListBean);
                }

            }
            return grListBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询施工人全部信息错误"));
        }
    }

    //查询施工人全部信息   根据工作id
    public List<WorkLogDetail.GrListBean> queryGrListBean(String id, String proid, String proName) {

        try {
            String queryString = "select ui.id,\n" +
                    "ui.id as user_id,\n" +
                    "ui.u_name,\n" +
                    "ui.u_pic,\n" +
                    "ui.u_sfz,\n" +
                    "ui.u_realname,\n" +
                    "ui.u_introduce,\n" +
                    "ui.u_type,\n" +
                    "ui.u_state,\n" +
                    "ui.u_birthday,\n" +
                    "ui.u_cert,\n" +
                    "ui.u_cert_type,\n" +
                    "ui.u_cert_pic,\n" +
                    "ui.u_sex,\n" +
                    "ui.u_proid,\n" +
                    "ui.u_up_proid,\n" +
                    "ui.u_sfzpic,\n" +
                    "ui.u_yhkpic,\n" +
                    "ui.u_lbs,\n" +
                    "ui.u_star,\n" +
                    "ui.u_isfujin,\n" +
                    "ui.u_nicheng,\n" +
                    "ui.last_device,\n" +
                    "ui.u_intime,\n" +
                    "ui.u_lastime,\n" +
                    "ui.logout_time FROM user_info ui \n" +
                    "WHERE ui.id in(select  user_id from work_log_gr where wl_id = ?) ";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class));
            query.setParameter(0, id);


            List<UserInfo> userInfos = query.list();

            List<WorkLogDetail.GrListBean> grListBeanList = new ArrayList<>();

            if (userInfos != null && userInfos.size() > 0) {
                for (UserInfo us : userInfos) {
                    ProOrgan proOrgan = queryProOrganTeambyidandproid(us.getId(), Integer.parseInt(proid));
                    WorkLogDetail.GrListBean grListBean = new WorkLogDetail.GrListBean();
                    grListBean.setGr_user_id(us.getId() + "");
                    grListBean.setGr_u_name(us.getU_name() + "");
                    grListBean.setGr_u_pic(us.getU_pic() + "");
                    grListBean.setGr_u_realname(us.getU_realname() + "");
                    grListBean.setGr_pro_id(proid);
                    grListBean.setGr_p_name(proName);
                    if (proOrgan != null) {
                        grListBean.setGr_po_id(proOrgan.getId() + "");
                        grListBean.setGr_po_name(proOrgan.getPoName() + "");
                    }
                    grListBeanList.add(grListBean);
                }

            }
            return grListBeanList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询施工人全部信息错误"));
        }
    }

    //查询用户信息     根据用户id
    public UserInfo queryUseInfoByID(String userid) {

        String hql = "from UserInfo where id = ?";
        UserInfo userInfo = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, Integer.parseInt(userid));
            userInfo = (UserInfo) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过ID:%s,查询用户失败", userid));
        }
        return userInfo;
    }

    //查询项目信息     根据项目id
    public Project queryProject(String id) {

        String hql = "from Project where id = ?";
        Project project = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, Integer.parseInt(id));
            project = (Project) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过ID:%s,查询项目失败", id));
        }
        return project;

    }

    //  查询 工种名    根据  班组id 一个班组对应一个工种
    public List<ProOrgan> queryProOrgan(int po_id) {
        try {
            String queryString = "select po.id id, po.po_gzid poGzid, po.po_name poName FROM pro_organ po \n" +
                    " WHERE  po.id in(select po_id FROM pro_organ_per WHERE user_id  = ?) ";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(ProOrgan.class));
            query.setParameter(0, po_id);
            return query.list();
        } catch (Exception e) {
            return null;
        }

    }


    //查询  部门 名字
    public ProOrgan querybumen(int id, int proid) {
        try {
            String queryString = "select po.id id, po.po_gzid poGzid, po.po_name poName FROM pro_organ po \n" +
                    " WHERE  po.id in(select po_id FROM pro_organ_per WHERE user_id  = ? and pop_state !=4) and po.pro_id = ? and po.po_state=1";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(ProOrgan.class));
            query.setParameter(0, id);
            query.setParameter(1, proid);
            return (ProOrgan) query.uniqueResult();
        } catch (Exception e) {
            return null;
        }

    }


    //查询  工种ID  和 ( 班组)名字
    public ProOrgan queryProOrganTeambyidandproid(int id, int proid) {
        try {
            String queryString = "select po.id id, po.po_gzid poGzid, po.po_name poName FROM pro_organ po \n" +
                    " WHERE  po.id in(select po_id FROM pro_organ_per WHERE user_id  = ? and pop_state!=4) and po.pro_id = ? and po.po_state = 2";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(ProOrgan.class));
            query.setParameter(0, id);
            query.setParameter(1, proid);
            return (ProOrgan) query.uniqueResult();
        } catch (Exception e) {
            return null;
        }

    }


    //查询  工种 根据班组 id
    public String queryWorkType(int id) {
        String queryString = "select key_name keyName FROM key_content WHERE id = (select po_gzid from  pro_organ WHERE id= ?)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setResultTransformer(new AliasToBeanResultTransformer(KeyContent.class));
        query.setParameter(0, id);
        KeyContent keyContent = (KeyContent) query.uniqueResult();

        if (keyContent != null) {
            return keyContent.getKeyName();
        } else {
            return "";
        }
    }

    //查询  职务
    public String queryZhiwu(int pro_id, int user_id) {
        String KeyName = "";
        try {
            String queryString = "select id,key_id as keyId,key_name as keyName,key_value as keyValue   from key_content where  id in(\n" +
                    " SELECT p_type from  pro_organ_per WHERE po_id in(\n" +
                    " select id from  pro_organ WHERE pro_id = ? ) and user_id = ?)";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(KeyContent.class));
            query.setParameter(0, pro_id);
            query.setParameter(1, user_id);
            List<KeyContent> list = query.list();

            if (list != null && list.size() > 0) {
                for (KeyContent kk : list) {
                    KeyName = KeyName + kk.getKeyName() + ",";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return KeyName;
    }


    //查询班组长的id
    public int queryWorkOrdID(int po_id) {
        String queryString = "select user_id as userId  from pro_organ_per  WHERE po_id = ? and pop_type =8";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setResultTransformer(new AliasToBeanResultTransformer(ProOrganPer.class));
        query.setParameter(0, po_id);
        List<ProOrganPer> proOrganPer = (List<ProOrganPer>) query.list();
        if (proOrganPer != null && proOrganPer.size() > 0) {
            return proOrganPer.get(0).getUserId();
        } else {
            return 0;
        }

    }


}
