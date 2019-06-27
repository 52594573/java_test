package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.ktp.project.dto.WorkLogDetail;
import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.ProOrgan;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class WorkLogDao {

    private static Logger log = LoggerFactory.getLogger(WorkLogDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private WorkLogPushDao workLogPushDao;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Page<WorkLogGatherDto> listWorkLogByMore(Page<WorkLogGatherDto> page) {
        String sql = "SELECT %s " +
                "FROM ( " +
                "SELECT wl.id, wl.pro_id AS proId, wl.send_uid AS sendUid, wl.wl_type AS wlType, wl.wl_content AS wlContent " +
                ", wl.wl_star AS wlStar, wl.wl_yzcd AS wlYzcd, wl.in_time AS inTime, wl.wl_state AS wlState, wl.wl_lbs_x AS wlLbsX " +
                ", wl.wl_lbs_y AS wlLbsY, wl.wl_lbs_name AS wlLbsName, wl.pw_id AS pwId, wl.pw_content AS pwContent, p.`p_name` AS projectName " +
                ", GROUP_CONCAT(wlp.`wl_pic`, '') AS workLogPicUrl " +
                "FROM work_log wl " +
                "LEFT JOIN `WorkLogPic` wlp ON wl.`id` = wlp.`wl_id` " +
                "LEFT JOIN project p ON wl.`pro_id` = p.`id` " +
                " %s " +
                "GROUP BY wl.`pro_id`, wl.`id` " +
                ") temp ";
        ArrayList<Object> params = Lists.newArrayList();
        String querySql = this.createQueryHql(page.getT(), params);
        String resultSql = String.format(sql, "*", querySql);
        String totalSql = String.format(sql, "count(1)", querySql);
        try {
            SQLQuery query = getCurrentSession().createSQLQuery(resultSql);
            SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
            query.setResultTransformer(new AliasToBeanResultTransformer(WorkLog.class));
            query.setFirstResult((page.getPageNo() - 1) * page.getPageSize());
            query.setMaxResults(page.getPageSize());
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i, params.get(i));
                totalQuery.setParameter(i, params.get(i));
            }
            page.setResult(query.list());
            BigInteger totalCount = (BigInteger) totalQuery.uniqueResult();
            page.setTotalCount(totalCount.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("listWorkLogByMore WorkLog failed" + e);
            throw new RuntimeException(e.getMessage());
        }

        return page.builderPage();
    }

    private String createQueryHql(WorkLogGatherDto vo, ArrayList<Object> params) {
        StringBuilder builder = new StringBuilder("where 1= 1 ");
        if (vo != null) {
            if (vo.getMonth() != null && vo.getMonth() > 0 && vo.getYear() != null && vo.getYear() > 0) {
                builder.append("and wl.in_time BETWEEN ? and ? ");
                params.add(NumberUtil.getFirstDayOfMonthByYearAndMonth(vo.getYear(), vo.getMonth()));
                params.add(NumberUtil.getFirstDayOfMonthByYearAndMonth(vo.getYear(), vo.getMonth() + 1));
            }
            if (vo.getProjectId() != null && vo.getProjectId() > 0) {
                builder.append("and wl.pro_id = ? ");
                params.add(vo.getProjectId());
            }
            if (vo.getUserId() != null && vo.getUserId() > 0) {
                builder.append("and wl.send_uid = ? ");
                params.add(vo.getUserId());
            }
            if (vo.getRecordType() != null && vo.getRecordType() > 0) {
                builder.append("and wl.wl_type = ? ");
                params.add(vo.getRecordType());
            }
        }
        return builder.toString();
    }

    public List<ProWork> listProWorkByProjectId(Integer projectId) {
        String hql = "from ProWork where proId = ?";
        List<ProWork> proWorks = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, projectId);
            proWorks = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proWorks;
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


    public int saveworkloggr(String wl_id, String user_id) {
        try {
            String sql = "insert into work_log_gr " +
                    "(wl_id,user_id) " +
                    "values " +
                    "(?,?)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, wl_id);
            query.setParameter(1, user_id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("工人信息录入失败", re);
            return 0;
        }
    }

    public int saveworklogpic(String saveworklogid, String wl_pic) {
        try {
            String sql = "insert into work_log_pic " +
                    "(wl_id,wl_pic) " +
                    "values " +
                    "(?,?)";
            SQLQuery query = getCurrentSession().createSQLQuery(sql);
            query.setParameter(0, saveworklogid);
            query.setParameter(1, wl_pic);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("工人图片录入失败", re);
            return 0;
        }
    }


    //查询前端工作详情
    public WorkLogDetail worklogdetail(String worklogid) {
        try {
            String queryString = "select  " +
                    " wl.id  id ,\n" +
                    " wl.pro_id  pro_id,\n" +
                    " wl.send_uid  send_uid,\n" +
                    " pj.p_name  pro_name,\n" +
                    " wl.wl_type  wl_type,\n" +
                    " wl.wl_content  wl_content,\n" +
                    " wl.wl_star  wl_star,\n" +
                    " wl.wl_state  wl_state,\n" +
                    " wl.wl_yzcd  wl_yzcd,\n" +
                    " wl.wl_lbs_x  wl_lbs_x,\n" +
                    " wl.wl_lbs_y  wl_lbs_y,\n" +
                    " wl.wl_lbs_name wl_lbs_name,\n" +
                    " wl.wl_safe_type wl_safe_type,\n" +
                    " wl.in_time  in_time,\n" +
                    " wl.works_list  works_list,\n" +
                    " wl.send_info  send_info,\n" +
                    " wl.wl_pw_info  wl_pw_info,\n" +
                    " wl.pic_list  pic_list\n" +
                    " FROM\n" +
                    " work_log wl\n" +
                    " LEFT JOIN project pj on pj.id = wl.pro_id\n" +
                    " where  \n" +
                    "   wl.id = ? ";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogParamer.class));
            query.setParameter(0, worklogid);
            WorkLogParamer workLogParamer = (WorkLogParamer) query.uniqueResult();

            WorkLogDetail workLogDetail = new WorkLogDetail();

            if (workLogParamer != null) {
                workLogDetail.setId(workLogParamer.getId());
                workLogDetail.setPro_id(workLogParamer.getPro_id());
                workLogDetail.setSend_uid(workLogParamer.getSend_uid());
                workLogDetail.setWl_type(workLogParamer.getWl_type());
                workLogDetail.setWl_content(workLogParamer.getWl_content() + "");
                workLogDetail.setWl_star(workLogParamer.getWl_star());
                workLogDetail.setWl_state(workLogParamer.getWl_state());
                workLogDetail.setWl_yzcd(workLogParamer.getWl_yzcd());
                workLogDetail.setWl_lbs_x(workLogParamer.getWl_lbs_x());
                workLogDetail.setWl_lbs_y(workLogParamer.getWl_lbs_y());
                if (!StringUtil.isEmpty(workLogParamer.getWl_lbs_name())) {
                    workLogDetail.setWl_lbs_name(workLogParamer.getWl_lbs_name() + "");
                }
                workLogDetail.setWl_safe_type(workLogParamer.getWl_safe_type());
                workLogDetail.setIn_time(workLogParamer.getIn_time());

                List<WorkLogDetail.GrListBean> grListBeanList = GsonUtil.jsonToList(workLogParamer.getWorks_list(), WorkLogDetail.GrListBean.class);

                if (grListBeanList != null && grListBeanList.size() > 0) {
                    workLogDetail.setGr_list(grListBeanList);
                }

                WorkLogSendInfo workLogSendInfo = GsonUtil.fromJson(workLogParamer.getSend_info(), WorkLogSendInfo.class);
                if (workLogSendInfo != null) {
                    workLogDetail.setP_type(workLogSendInfo.getP_type());
                    if (!StringUtil.isEmpty(workLogSendInfo.getU_name())) {
                        workLogDetail.setU_name(workLogSendInfo.getU_name() + "");
                    }
                    workLogDetail.setU_realname(workLogSendInfo.getU_realname() + "");
                    workLogDetail.setU_pic(workLogSendInfo.getU_pic() + "");
                    workLogDetail.setU_cert(workLogSendInfo.getU_cert());
                    workLogDetail.setU_cert_type(workLogSendInfo.getU_cert_type());
                    workLogDetail.setZhiwu(workLogSendInfo.getZhiwu() + "");
                    workLogDetail.setPo_id(workLogSendInfo.getPo_id());
                    workLogDetail.setPo_name(workLogSendInfo.getPo_name() + "");
                }

                List<String> picll = new ArrayList<>();

                if (!StringUtil.isEmpty(workLogParamer.getPic_list())) {
                    List<String> pici = GsonUtil.jsonToList(workLogParamer.getPic_list(), String.class);
                    if (pici != null && pici.size() > 0) {
                        picll.addAll(pici);
                    }
                }

                workLogDetail.setPic_list(picll);

                WlPwInfoBean wlPwInfoBean = GsonUtil.fromJson(workLogParamer.getWl_pw_info(), WlPwInfoBean.class);
                if (wlPwInfoBean != null) {
                    workLogDetail.setPw_id(wlPwInfoBean.getPw_id());// : 1012         //节点
                    workLogDetail.setPw_content(wlPwInfoBean.getPw_content() + "");// :        //节点描述
                    workLogDetail.setPw_name(wlPwInfoBean.getPw_name() + "");// : 地下车库   //节点
                    workLogDetail.setPw_pid(wlPwInfoBean.getPw_pid());// : 1007        //节点
                    workLogDetail.setP_pw_name(wlPwInfoBean.getP_pw_name() + "");// : 2#楼      //节点

                }
            }


            if (workLogParamer != null) {
                //适配久的asp接口
                if (StringUtil.isEmpty(workLogParamer.getWl_pw_info())
                        && StringUtil.isEmpty(workLogParamer.getSend_info())
                        && StringUtil.isEmpty(workLogParamer.getWorks_list())
                        && StringUtil.isEmpty(workLogParamer.getPic_list())
                ) {
                    //发送人的信息
                    UserInfo senduserinfo = workLogPushDao.queryUseInfoByID(workLogParamer.getSend_uid() + "");

                    if (senduserinfo != null) {
                        workLogDetail.setU_realname(senduserinfo.getU_realname());
                        workLogDetail.setU_pic(senduserinfo.getU_pic());
                        workLogDetail.setU_cert(senduserinfo.getU_cert());
                        workLogDetail.setU_cert_type(senduserinfo.getU_cert_type());
                    }
                    //查询节点
                    ProWork querynode = querynode(workLogDetail.getPw_id() + "");
                    if (querynode != null) {
                        workLogDetail.setPw_id(querynode.getId());
                        workLogDetail.setPw_name(querynode.getPwName() + "");
                        workLogDetail.setPw_pid(querynode.getPwPid());

                        if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                            //查询节点
                            ProWork pwpidnode = querynode(querynode.getPwPid() + "");
                            if (pwpidnode != null) {
                                workLogDetail.setP_pw_name(querynode.getPwName() + "");
                            }
                        } else {
                            workLogDetail.setP_pw_name("");
                        }
                    }

                    //查询部门
                    List<ProOrgan> proOrganList = workLogPushDao.queryProOrgan(workLogParamer.getSend_uid());
                    if (proOrganList != null && proOrganList.size() > 0) {
                        workLogDetail.setPo_id(proOrganList.get(0).getId());
                        workLogDetail.setPo_name(proOrganList.get(0).getPoName());
                    }

                    //查询施工人列表详情
                    List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBean(worklogid, workLogDetail.getPro_id() + "", workLogDetail.getPro_name());

                    workLogDetail.setGr_list(grListBeanList);

                    //获取工作日志图片
                    List<WorkLogPic> querypic = querypic(worklogid);
                    List<String> piclist = new ArrayList<>();
                    if (querypic != null && querypic.size() > 0) {
                        for (WorkLogPic wp : querypic) {
                            if (!StringUtil.isEmpty(wp.getWl_pic())) {
                                String wl_pic = wp.getWl_pic();
                                if (wl_pic.contains("\\|")) {
                                    String[] split = wl_pic.split("\\|");
                                    for (String ss : split) {
                                        piclist.add(ss);
                                    }
                                } else {
                                    piclist.add(wl_pic);
                                }
                            }
                        }
                    }

                    workLogDetail.setPic_list(piclist);

                }
            }
            return workLogDetail;
        } catch (Exception re) {
            re.printStackTrace();
            throw new BusinessException(String.format("通过ID:%s,查询详情失败", worklogid));
        }
    }


    //查询节点
    public ProWork querynode(String id) {
        String hql = "from ProWork where id = ?";
        ProWork proWork = null;
        try {
            if (!StringUtil.isEmpty(id) && !id.equals("null") && !id.equals("")) {
                Query query = getCurrentSession().createQuery(hql);
                query.setParameter(0, Integer.parseInt(id));
                proWork = (ProWork) query.uniqueResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过ID:%s,查询节点失败", id));
        }
        return proWork;
    }

    //查询全部工作日志
    public ArrayList<WorkLog> queryAllworkLog() {
        String hql = "from WorkLog order by  id  desc";
        ArrayList<WorkLog> workLog = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            workLog = (ArrayList<WorkLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;
    }

    //查询时间段工作日志
    public ArrayList<WorkLog> queryAllworkLogbytime(String startDate, String datetime) {
//        String hql = "from WorkLog  where  in_time>='" + startDate + "' and in_time<='" + datetime + "'";
        String hql = "from WorkLog  where  in_time>='" + startDate + "' and in_time<='" + datetime + "' and wl_pw_info ='' and send_info ='' and works_list ='' and pic_list ='' ";
        ArrayList<WorkLog> workLog = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            workLog = (ArrayList<WorkLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;
    }


    //查询时间段工作日志
    public ArrayList<WorkLog> updateAllworkLogbytime(String startDate, String datetime) {
//        String hql = "from WorkLog  where  in_time>='" + startDate + "' and in_time<='" + datetime + "'";
        String hql = "from WorkLog  where  in_time>='" + startDate + "' and in_time<='" + datetime + "'";
        ArrayList<WorkLog> workLog = new ArrayList<>();
        try {
            Query query = getCurrentSession().createQuery(hql);

            ArrayList<WorkLog> list = (ArrayList<WorkLog>) query.list();
            if (list != null && list.size() > 0) {
                workLog.addAll(list);
                for (int i = workLog.size() - 1; i > 0; i--) {
                    //任何一个有值
                    if (!StringUtil.isEmpty(workLog.get(i).getSend_info())) {
                        workLog.remove(i);//就不用遍历
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;
    }


    //查询时间段工作日志
    public ArrayList<WorkLog> mustupdateAllworkLogbytime(String startDate, String datetime) {
        String hql = "from WorkLog  where  in_time>='" + startDate + "' and in_time<='" + datetime + "'";
        ArrayList<WorkLog> workLog = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            workLog = (ArrayList<WorkLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;
    }

    //查询时间段工作日志
    public ArrayList<WorkLog> mustupdateAllworkLogbyProject(Integer proid) {
        String hql = "from WorkLog  where  pro_id=" + proid;
        ArrayList<WorkLog> workLog = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            workLog = (ArrayList<WorkLog>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;
    }


    //查询全部工作工人
    public ArrayList<WorkLogGr> queryAllWorkLogGr(int wl_id) {
        String hql = "select id ,wl_id,user_id from work_log_gr where wl_id=?";
        ArrayList<WorkLogGr> workLog = null;
        try {
            SQLQuery query = getCurrentSession().createSQLQuery(hql);
            query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogGr.class));
            query.setParameter(0, wl_id);
            workLog = (ArrayList<WorkLogGr>) query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("查询全部工作日志"));
        }
        return workLog;

    }

    public void updateWorklog(Integer wl_id, String piclist, String wrlList, String senderinfo, String jiedianinfo) {
        String sql = "UPDATE work_log SET pic_list = '" + piclist + "' ," +
                "works_list  = '" + wrlList + "' ," +
                " send_info  = '" + senderinfo + "' ," +
                " wl_pw_info  = '" + jiedianinfo + "' " +
                "where id = " + wl_id;
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    //查询图片根据工作日志id
    public List<WorkLogPic> querypic(String id) {
        String hql = "select id,wl_id,wl_pic from work_log_pic where wl_id = ?";
        List<WorkLogPic> workLogPic = null;
        try {
            SQLQuery query = getCurrentSession().createSQLQuery(hql);
            query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogPic.class));
            query.setParameter(0, id);
            workLogPic = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过工作日志ID:%s,查询工作日志图片失败", id));
        }
        return workLogPic;
    }

    //根据wordlogid 查询

    public WorkLogParamer saveworklog(WorkLogParamer wl) {
        try {
            System.out.println("====" + wl.toString());
            getCurrentSession().save(wl);
            return wl;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
