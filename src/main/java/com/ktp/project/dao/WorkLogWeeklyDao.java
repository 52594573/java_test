package com.ktp.project.dao;

import com.ktp.project.dto.*;
import com.ktp.project.entity.ChatIgnore;
import com.ktp.project.entity.WlPwInfoBean;
import com.ktp.project.entity.WorkLog;
import com.ktp.project.entity.WorkLogParamer;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.StringUtil;
import net.sf.json.JSONArray;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class WorkLogWeeklyDao {

    private static Logger log = LoggerFactory.getLogger(WorkLogWeeklyDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    //查询质量和安全的统计
    public Long querystat(int pro_id, String wl_type, String startDate, String datetime) {
        String queryString = "select COUNT(*)  from WorkLog " +
                " where pro_id=? and wl_type=? and   in_time>=? and in_time<=?  and wl_state=1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter(0, pro_id);
        query.setParameter(1, wl_type);
        query.setParameter(2, startDate);
        query.setParameter(3, datetime);
        return (Long) query.uniqueResult();
    }

    //查询安全的严重统计
    public Long queryseri(int pro_id, String wl_type, String wl_yzcd, String startDate, String datetime) {
        String queryString = "select COUNT(*)  from WorkLog " +
                " where pro_id=? and wl_type=? and wl_yzcd=? and  in_time>=? and in_time<=? and wl_state=1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter(0, pro_id);
        query.setParameter(1, wl_type);
        query.setParameter(2, wl_yzcd);
        query.setParameter(3, startDate);
        query.setParameter(4, datetime);
        return (Long) query.uniqueResult();
    }

    //查询质量的严重统计
    public Long queryStartseri(int pro_id, String wl_type, String startDate, String datetime) {
        String queryString = "select COUNT(*)  from WorkLog " +
                " where pro_id=? and wl_type=? and (wl_star=1 or wl_star=2) and  in_time>=? and in_time<=? and wl_state=1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter(0, pro_id);
        query.setParameter(1, wl_type);
        query.setParameter(2, startDate);
        query.setParameter(3, datetime);
        return (Long) query.uniqueResult();
    }

    //查询质量和安全班组数
    public Long queryteam(int pro_id) {
        String queryString = "select COUNT(*)  from ProOrgan where pro_id=? and po_state=2";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter(0, pro_id);
        return (Long) query.uniqueResult();
    }

    //查询星级记录条数占比数据
    public WorkLogWeeklyDetailDto querystart(int pro_id, int wl_type, String startDate, String datetime) {
        String queryString = "select  \n" +
                "CONCAT(ROUND(sum(case when wl_star=1 then 1 else 0 end)/count(*)*100,2),'') as onestarp,\n" +
                "CONCAT(ROUND(sum(case when wl_star=2 then 1 else 0 end)/count(*)*100,2),'') as twostarp,\n" +
                "CONCAT(ROUND(sum(case when wl_star=3 then 1 else 0 end)/count(*)*100,2),'') as threestarp,\n" +
                "CONCAT(ROUND(sum(case when wl_star=4 then 1 else 0 end)/count(*)*100,2),'') as fourstarp,\n" +
                "CONCAT(ROUND(sum(case when wl_star=5 then 1 else 0 end)/count(*)*100,2),'') as fivestarp from WorkLog\n" +
                " where pro_id=? and wl_type=? and in_time>=? and in_time<=? and wl_state=1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogWeeklyDetailDto.class));
        query.setParameter(0, pro_id);
        query.setParameter(1, wl_type);
        query.setParameter(2, startDate);
        query.setParameter(3, datetime);

        return (WorkLogWeeklyDetailDto) query.uniqueResult();
    }

    //查询严重程度占比记录条数占比数据
    public WorkLogWeeklySeriousProportionDto querySeriousProportion(int pro_id, int wl_type, String startDate, String datetime) {
        String queryString = "select  \n" +
                "CONCAT(ROUND(sum(case when wl_yzcd=1 then 1 else 0 end)/count(*)*100,2),'') as serious,\n" +
                "CONCAT(ROUND(sum(case when wl_yzcd=2 then 1 else 0 end)/count(*)*100,2),'') as ordinary,\n" +
                "CONCAT(ROUND(sum(case when wl_yzcd=3 then 1 else 0 end)/count(*)*100,2),'') as caution \n" +
                " from WorkLog\n" +
                " where pro_id=? and wl_type=? and in_time>=? and in_time<=?  and wl_state=1";
        Query query = getCurrentSession().createQuery(queryString);
        query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogWeeklySeriousProportionDto.class));
        query.setParameter(0, pro_id);
        query.setParameter(1, wl_type);
        query.setParameter(2, startDate);
        query.setParameter(3, datetime);

        return (WorkLogWeeklySeriousProportionDto) query.uniqueResult();
    }


    //查询班组列表
    public List<WorkLogWeeklyDetailTeamListDto> queryteamList(int pro_id, int wl_type, String startDate, String datetime) {
        try {
            String sql = "select po.id id, po.po_name teamname,sum(case when wl.wl_type=? and in_time>=? and in_time<=? then 1 else 0 end) qualitynum,SUM(case when wl.wl_yzcd=1 and wl.wl_type=? and in_time>=? and in_time<=? then 1 else 0 end) seriounum from pro_organ po \n" +
                    "LEFT JOIN pro_organ_per pop on po.id = pop.po_id \n" +
                    "LEFT JOIN work_log_gr wlg on pop.user_id = wlg.user_id \n" +
                    "LEFT JOIN work_log wl on wl.id = wlg.wl_id \n" +
                    "WHERE \n" +
                    "po.pro_id=? and wlg.id!='' and wl.id!='' \n" +
                    "GROUP BY po.po_name";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogWeeklyDetailTeamListDto.class));
            sqlQuery.setParameter(0, wl_type);
            sqlQuery.setParameter(1, startDate);
            sqlQuery.setParameter(2, datetime);
            sqlQuery.setParameter(3, wl_type);
            sqlQuery.setParameter(4, startDate);
            sqlQuery.setParameter(5, datetime);
            sqlQuery.setParameter(6, pro_id);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    //查询班组列表
    public List<WorkLogWeeklyDetailTeamListDto> queryteamListnew(int pro_id, int wl_type, String startDate, String datetime) {

        //第一步查询全部班组
        try {
            //查询班组列表
            String sqlteamlist = "select po.id id ,po.po_name teamname from pro_organ po\n" +
                    " LEFT JOIN pro_organ_per pop on pop.po_id = po.id\n" +
                    " where po.pro_id = ? and po.po_state =2\n" +
                    " GROUP BY po.po_name";
            Query sqlteamlistQuery = getCurrentSession().createSQLQuery(sqlteamlist);
            sqlteamlistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogWeeklyDetailTeamListDto.class));
            sqlteamlistQuery.setParameter(0, pro_id);
            List<WorkLogWeeklyDetailTeamListDto> teamlist = new ArrayList<>();
            if (sqlteamlistQuery.list() != null && sqlteamlistQuery.list().size() > 0) {
                teamlist.addAll(sqlteamlistQuery.list());
                for (WorkLogWeeklyDetailTeamListDto ww : teamlist) {
                    ww.setQualitynum(new BigDecimal("0"));
                    ww.setSeriounum(new BigDecimal("0"));
                }
            }


            //查询这个一周的全部班组情况
            String sqlworkloglist = "select wl.id id, wl.works_list works_list\n" +
                    " from work_log wl \n" +
                    " where   wl.wl_type = ? \n" +
                    " and wl.pro_id = ? and  wl.in_time>=?\n" +
                    " and wl.in_time<=?\n" +
                    " and wl_state=1  ";
            Query sqlworkloglistQuery = getCurrentSession().createSQLQuery(sqlworkloglist);
            sqlworkloglistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogParamer.class));
            sqlworkloglistQuery.setParameter(0, wl_type);
            sqlworkloglistQuery.setParameter(1, pro_id);
            sqlworkloglistQuery.setParameter(2, startDate);
            sqlworkloglistQuery.setParameter(3, datetime);
            List<WorkLogParamer> wwlist = sqlworkloglistQuery.list();


            //查询这个一周的严重数班组情况
            String serworkloglist = "";

            if (wl_type == 1) {//质量严重数，是星级低于2
                serworkloglist = "select wl.id id, wl.works_list works_list\n" +
                        " from work_log wl \n" +
                        " where   wl.wl_type = ? \n" +
                        " and wl.pro_id = ? and  wl.in_time>=?\n" +
                        " and wl.in_time<=?\n" +
                        " and wl_state=1 and  (wl_star=1 or wl_star=2)";
            } else {
                serworkloglist = "select wl.id id, wl.works_list works_list\n" +
                        " from work_log wl \n" +
                        " where   wl.wl_type = ? \n" +
                        " and wl.pro_id = ? and  wl.in_time>=?\n" +
                        " and wl.in_time<=?\n" +
                        " and wl_state=1 and  wl_yzcd=1";
            }
            Query sersqlworkloglistQuery = getCurrentSession().createSQLQuery(serworkloglist);
            sersqlworkloglistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogParamer.class));
            sersqlworkloglistQuery.setParameter(0, wl_type);
            sersqlworkloglistQuery.setParameter(1, pro_id);
            sersqlworkloglistQuery.setParameter(2, startDate);
            sersqlworkloglistQuery.setParameter(3, datetime);
            List<WorkLogParamer> serlist = sersqlworkloglistQuery.list();


            //全部施工人员班组情况
            List<WorkLogDetail.GrListBean> grlist = new ArrayList<>();
            if (wwlist != null && wwlist.size() > 0) {
                for (WorkLogParamer wlp : wwlist) {
                    List<WorkLogDetail.GrListBean> grListBeanList = GsonUtil.jsonToList(wlp.getWorks_list(), WorkLogDetail.GrListBean.class);
                    grlist.addAll(grListBeanList);
                }
            }


            //总数统计
            for (WorkLogWeeklyDetailTeamListDto ww : teamlist) {
                for (WorkLogDetail.GrListBean wt : grlist) {
                    if (wt != null && !StringUtil.isEmpty(wt.getGr_po_id())) {
                        if (ww.getId() == Long.parseLong(wt.getGr_po_id())) {
                            if (ww.getQualitynum() != null) {
                                ww.setQualitynum(ww.getQualitynum().add(new BigDecimal("1")));
                            } else {
                                ww.setQualitynum(new BigDecimal("1"));
                            }
                        }
                    }
                }
            }

            //严重数统计
            for (WorkLogParamer wlp : serlist) {
                List<WorkLogDetail.GrListBean> grListBeanList = GsonUtil.jsonToList(wlp.getWorks_list(), WorkLogDetail.GrListBean.class);
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    for (WorkLogWeeklyDetailTeamListDto ww : teamlist) {
                        loop:
                        for (WorkLogDetail.GrListBean www : grListBeanList) {
                            if (www != null && !StringUtil.isEmpty(www.getGr_po_id())) {
                                if (Integer.parseInt(www.getGr_po_id().trim()) == ww.getId()) {
                                    ww.setSeriounum(ww.getSeriounum().add(new BigDecimal("1")));
                                    break loop;
                                }
                            }
                        }
                    }
                }
            }


            return teamlist;
        } catch (
                RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }

    }

    //查询班组数
    public Integer queryteamnumber(int pro_id, int wl_type, String startDate, String datetime) {

        try {
            //查询班组列表
            String sqlteamlist = "select po.id id ,po.po_name teamname from pro_organ po\n" +
                    " LEFT JOIN pro_organ_per pop on pop.po_id = po.id\n" +
                    " where po.pro_id = ? and po.po_state =2\n" +
                    " GROUP BY po.po_name";
            Query sqlteamlistQuery = getCurrentSession().createSQLQuery(sqlteamlist);
            sqlteamlistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogWeeklyDetailTeamListDto.class));
            sqlteamlistQuery.setParameter(0, pro_id);
            List<WorkLogWeeklyDetailTeamListDto> teamlist = new ArrayList<>();
            if (sqlteamlistQuery.list() != null && sqlteamlistQuery.list().size() > 0) {
                teamlist.addAll(sqlteamlistQuery.list());
                for (WorkLogWeeklyDetailTeamListDto ww : teamlist) {
                    ww.setQualitynum(new BigDecimal("0"));
                    ww.setSeriounum(new BigDecimal("0"));
                }
            }


            String sqlworkloglist = "select wl.id id, wl.works_list works_list\n" +
                    " from work_log wl \n" +
                    " where   wl.wl_type = ? \n" +
                    " and wl.pro_id = ? and  wl.in_time>=?\n" +
                    " and wl.in_time<=?\n" +
                    " and wl_state=1  ";
            Query sqlworkloglistQuery = getCurrentSession().createSQLQuery(sqlworkloglist);
            sqlworkloglistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogParamer.class));
            sqlworkloglistQuery.setParameter(0, wl_type);
            sqlworkloglistQuery.setParameter(1, pro_id);
            sqlworkloglistQuery.setParameter(2, startDate);
            sqlworkloglistQuery.setParameter(3, datetime);
            List<WorkLogParamer> workloglist = new ArrayList<>();
            List<WorkLogParamer> wwlist = sqlworkloglistQuery.list();

            List<WorkLogDetail.GrListBean> grlist = new ArrayList<>();

            if (wwlist != null && wwlist.size() > 0) {
                for (WorkLogParamer wlp : wwlist) {
                    List<WorkLogDetail.GrListBean> grListBeanList = GsonUtil.jsonToList(wlp.getWorks_list(), WorkLogDetail.GrListBean.class);
                    grlist.addAll(grListBeanList);
                }
            }


            for (WorkLogWeeklyDetailTeamListDto ww : teamlist) {
                for (WorkLogDetail.GrListBean wt : grlist) {
                    if (wt != null && !StringUtil.isEmpty(wt.getGr_po_id())) {
                        if (ww.getId() == Long.parseLong(wt.getGr_po_id())) {
                            if (ww.getQualitynum() != null) {
                                ww.setQualitynum(ww.getQualitynum().add(new BigDecimal("1")));
                            } else {
                                ww.setQualitynum(new BigDecimal("1"));
                            }
                        }
                    }
                }
            }


            if (teamlist != null && teamlist.size() > 0) {
                for (int i = teamlist.size() - 1; i >= 0; i--) {
                    if (teamlist.get(i).getQualitynum().equals(new BigDecimal("0"))) {
                        teamlist.remove(teamlist.get(i));
                    }
                }
            }
            return teamlist.size();
        } catch (
                RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }

    }


    //查询周报班组工作id
    public List<WorkLogWeeklyDto> queryteamList2(int pro_id, int teamid, int wl_type, String startDate, String datetime, int pageNo, int pageSize) {
        try {

            List<WorkLogWeeklyDto> workLogWeeklyDto = new ArrayList<>();

            //查询这个一周的全部班组情况
            String sqlworkloglist = "select wl.id id,wl.pro_id pro_id,wl.send_uid send_uid,wl.wl_type wl_type,wl.wl_content wl_content,wl.wl_star wl_star,wl.wl_yzcd wl_yzcd,wl.in_time in_time, wl.works_list works_list\n" +
                    " from work_log wl \n" +
                    " where   wl.wl_type = ? \n" +
                    " and wl.pro_id = ? and  wl.in_time>=?\n" +
                    " and wl.in_time<=?\n" +
                    " and wl_state=1  ";
            Query sqlworkloglistQuery = getCurrentSession().createSQLQuery(sqlworkloglist);
            sqlworkloglistQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogParamer.class));
            sqlworkloglistQuery.setParameter(0, wl_type);
            sqlworkloglistQuery.setParameter(1, pro_id);
            sqlworkloglistQuery.setParameter(2, startDate);
            sqlworkloglistQuery.setParameter(3, datetime);
            sqlworkloglistQuery.setFirstResult((pageNo - 1) * pageSize);
            sqlworkloglistQuery.setMaxResults(pageSize);
            List<WorkLogParamer> wwlist = sqlworkloglistQuery.list();
            List<WorkLogParamer> teamlist = new ArrayList<>();

            String po_name = "";// 班组名称
            String pro_name = "";// 项目名称


            for (WorkLogParamer wlp : wwlist) {
                List<WorkLogDetail.GrListBean> grListBeanList = GsonUtil.jsonToList(wlp.getWorks_list(), WorkLogDetail.GrListBean.class);
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    loop1:
                    for (WorkLogDetail.GrListBean www : grListBeanList) {
                        if (www != null && !StringUtil.isEmpty(www.getGr_po_id())) {
                            if (Integer.parseInt(www.getGr_po_id()) == teamid) {
                                teamlist.add(wlp);
                                if (!StringUtil.isEmpty(www.getGr_po_name())) {
                                    po_name = www.getGr_po_name();
                                    pro_name = www.getGr_p_name();
                                }
                                break loop1;
                            }
                        }
                    }
                }
            }

            for (WorkLogParamer wp : teamlist) {
                WorkLogWeeklyDto wld = new WorkLogWeeklyDto();
                wld.setId(wp.getId());
                wld.setProId(wp.getPro_id());// int(11) NULL
                wld.setSendUid(wp.getSend_uid());
                wld.setWlType(wp.getWl_type());
                if (!StringUtil.isEmpty(wp.getWl_content())) {
                    wld.setWlContent(wp.getWl_content());
                }
                wld.setWlStar(wp.getWl_star());
                wld.setWlYzcd(wp.getWl_yzcd());
                wld.setInTime(wp.getIn_time());
                wld.setWlState(wp.getWl_state());
                wld.setWlLbsX(wp.getWl_lbs_x());
                wld.setWlLbsY(wp.getWl_lbs_y());
                if (!StringUtil.isEmpty(wp.getWl_lbs_name())) {
                    wld.setWlLbsName(wp.getWl_lbs_name());
                }
                wld.setPwId(wp.getPw_id());
                if (!StringUtil.isEmpty(wp.getPw_content())) {
                    wld.setPwContent(wp.getPw_content());
                }
                wld.setPo_id(teamid);
                if (!StringUtil.isEmpty(po_name)) {
                    wld.setPo_name(po_name);
                }

                if (!StringUtil.isEmpty(wp.getWl_pw_info())) {
                    WlPwInfoBean wlPwInfoBean = GsonUtil.fromJson(wp.getWl_pw_info(), WlPwInfoBean.class);
                    if (wlPwInfoBean != null) {
                        if (!StringUtil.isEmpty(wlPwInfoBean.getPw_name())) {
                            wld.setOneLevelPoint(wlPwInfoBean.getPw_name());
                        }
                        if (!StringUtil.isEmpty(wlPwInfoBean.getP_pw_name())) {
                            wld.setTwoLevelPoint(wlPwInfoBean.getP_pw_name());
                        }
                    }
                }
                wld.setProjectName(pro_name);
                if (!StringUtil.isEmpty(wp.getPic_list())) {
                    List<String> picclis = GsonUtil.jsonToList(wp.getPic_list(), String.class);
                    if (picclis != null && picclis.size() > 0) {
                        wld.setWorkLogPics(picclis);
                    }
                }

                workLogWeeklyDto.add(wld);
            }
            return workLogWeeklyDto;
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


    public List<Integer> validateUserId() {
        String sql = "SELECT DISTINCT  pop.`user_id` FROM `pro_organ_per` pop WHERE pop.`p_type` IN ( 117,118,119,120,121,142,176,180,181 )";
        List<Integer> list = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            list = sqlQuery.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
