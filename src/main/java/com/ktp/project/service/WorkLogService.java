package com.ktp.project.service;

import com.ktp.project.dao.WorkLogDao;
import com.ktp.project.dao.WorkLogPushDao;
import com.ktp.project.dto.WorkLogDetail;
import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.dto.WorkLogQualityPushDto;
import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.StringUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import com.zm.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkLogService {

    private static Logger log = LoggerFactory.getLogger(WorkLogService.class);

    @Autowired
    private WorkLogDao workLogDao;


    @Autowired
    private WorkLogPushDao workLogPushDao;

    public Page<WorkLogGatherDto> listWorkLogByMore(Page page) {
        Page bo = workLogDao.listWorkLogByMore(page);
        WorkLogGatherDto t = (WorkLogGatherDto) page.getT();
        List<ProWork> proWorks = workLogDao.listProWorkByProjectId(t.getProjectId());
        Map<Integer, Integer> ID_M_PID = new HashMap<>();
        Map<Integer, String> ID_M_NAME = new HashMap<>();
        for (ProWork proWork : proWorks) {
            ID_M_PID.put(proWork.getId(), proWork.getPwPid());
            ID_M_NAME.put(proWork.getId(), proWork.getPwName());
        }
        List<WorkLog> result = (List<WorkLog>) bo.getResult();
        for (WorkLog workLog : result) {
            if (workLog.getPwId() != null && workLog.getPwId() > 0) {
                workLog.setTwoLevelPoint(ID_M_NAME.get(workLog.getPwId()));
                if (ID_M_PID.get(workLog.getPwId()) != null) {
                    workLog.setOneLevelPoint(ID_M_NAME.get(ID_M_PID.get(workLog.getPwId())));
                }
            }
            if (StringUtils.isNotBlank(workLog.getWorkLogPicUrl())) {
                String[] split = workLog.getWorkLogPicUrl().split(",");
                workLog.setWorkLogPics(Arrays.asList(split));
            }
        }
        return bo;
    }

    public void validateUserId(Integer userId) {
        List<Integer> userIds = workLogDao.validateUserId();
        if (!userIds.contains(userId)) {
            throw new BusinessException("亲爱的用户,您没有该功能的查看权限!");
        }
    }

    @Async
    public void updateAllworkLog() {
        //查询全部工作日志，然后遍历更新，完事
        //查询全部工作记录
        ArrayList<WorkLog> workLoglist = workLogDao.queryAllworkLog();
        String picListinfo = "";
        String workerListinfo = "";
        String senderinfo = "";
        String jiedianinfo = "";

        for (WorkLog wl : workLoglist) {
            //图片json
            List<WorkLogPic> querypic = workLogDao.querypic(String.valueOf(wl.getId()));
            if (querypic != null && querypic.size() > 0) {
                List<String> picll = new ArrayList<>();
                for (WorkLogPic wlp : querypic) {
                    picll.add(wlp.getWl_pic());
                }
                picListinfo = GsonUtil.toJson(picll);
            }

            //发送人json
            WorkLogSendInfo workLogSendInfo = new WorkLogSendInfo();
            UserInfo userInfo = workLogPushDao.queryUseInfoByID(wl.getSendUid() + "");
            //查询职务
            String aazhiwu = workLogPushDao.queryZhiwu(wl.getProId(), wl.getSendUid());
            workLogSendInfo.setZhiwu("");
            workLogSendInfo.setPo_id(0);
            workLogSendInfo.setPo_name("");
            workLogSendInfo.setU_name(userInfo.getU_name() + "");
            //TODO
            ProOrgan querybumen = workLogPushDao.querybumen(wl.getSendUid(), wl.getProId());
            if (userInfo != null) {
                workLogSendInfo.setU_realname(userInfo.getU_realname());
                workLogSendInfo.setU_pic(userInfo.getU_pic());
                workLogSendInfo.setU_cert(userInfo.getU_cert());
                if (userInfo.getU_cert_type() != null) {
                    workLogSendInfo.setU_cert_type(userInfo.getU_cert_type());
                }
                if (!StringUtil.isEmpty(aazhiwu)) {
                    workLogSendInfo.setZhiwu(aazhiwu);
                }
            }
            if (querybumen != null) {
                workLogSendInfo.setPo_id(querybumen.getId());
                workLogSendInfo.setPo_name(querybumen.getPoName());
            }
            senderinfo = GsonUtil.toJson(workLogSendInfo);

            //工人json
            ArrayList<WorkLogGr> workLogGrs = workLogDao.queryAllWorkLogGr(wl.getId());
            if (workLogGrs != null && workLogGrs.size() > 0) {
                String wlgids = "";
                for (WorkLogGr wlg : workLogGrs) {
                    wlgids = wlgids + wlg.getUser_id() + ",";
                }

                if (wlgids.length() > 0) {
                    wlgids = wlgids.substring(0, wlgids.length() - 1);
                }
                List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBeanbyworks(wlgids, wl.getProId() + "");
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    workerListinfo = GsonUtil.toJson(grListBeanList);
                }
            }

            //节点json
            WlPwInfoBean wlPwInfoBean = new WlPwInfoBean();
            wlPwInfoBean.setPw_id(0);
            wlPwInfoBean.setPw_name("");
            wlPwInfoBean.setPw_pid(0);
            wlPwInfoBean.setP_pw_name("");
            wlPwInfoBean.setPw_content("");
            if (!StringUtil.isEmpty(wl.getPwContent())) {
                wlPwInfoBean.setPw_content(wl.getPwContent());
            }

            //查询节点
            ProWork querynode = workLogDao.querynode(wl.getPwId() + "");
            if (querynode != null) {
                wlPwInfoBean.setPw_id(querynode.getId());
                wlPwInfoBean.setPw_name(querynode.getPwName() + "");
                wlPwInfoBean.setPw_pid(querynode.getPwPid());

                if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                    //查询节点
                    ProWork pwpidnode = workLogDao.querynode(querynode.getPwPid() + "");
                    if (pwpidnode != null) {
                        wlPwInfoBean.setP_pw_name(querynode.getPwName() + "");
                    }
                } else {
                    wlPwInfoBean.setP_pw_name("");
                }
            }

            if (wlPwInfoBean != null) {
                jiedianinfo = GsonUtil.toJson(wlPwInfoBean);
            }
            workLogDao.updateWorklog(wl.getId(), picListinfo, workerListinfo, senderinfo, jiedianinfo);
        }


    }

    public void updateYesterdayAllworkLog() {
//查询全部工作记录
        String enddate = DateUtil.format(new Date(), DateUtil.FORMAT_DATE);
        enddate = enddate + " 00:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        String startdate = DateUtil.format(date, DateUtil.FORMAT_DATE);
        startdate = startdate + " 00:00:00";
        updateAllworkLogBytime(startdate, enddate);
    }


    @Async
    public void updateAllworkLogBytime(String startdate, String enddate) {
        ArrayList<WorkLog> workLoglist = workLogDao.updateAllworkLogbytime(startdate, enddate);

        String picListinfo = "";
        String workerListinfo = "";
        String senderinfo = "";
        String jiedianinfo = "";

        for (WorkLog wl : workLoglist) {
            //图片json
            List<WorkLogPic> querypic = workLogDao.querypic(String.valueOf(wl.getId()));
            if (querypic != null && querypic.size() > 0) {
                List<String> picll = new ArrayList<>();
                for (WorkLogPic wlp : querypic) {
                    picll.add(wlp.getWl_pic());
                }
                picListinfo = GsonUtil.toJson(picll);
            }

            //发送人json
            WorkLogSendInfo workLogSendInfo = new WorkLogSendInfo();
            UserInfo userInfo = workLogPushDao.queryUseInfoByID(wl.getSendUid() + "");
            //查询职务
            String aazhiwu = workLogPushDao.queryZhiwu(wl.getProId(), wl.getSendUid());
            workLogSendInfo.setZhiwu("");
            workLogSendInfo.setPo_id(0);
            workLogSendInfo.setPo_name("");
            workLogSendInfo.setU_name(userInfo.getU_name() + "");

            if (userInfo != null) {
                workLogSendInfo.setU_realname(userInfo.getU_realname());
                workLogSendInfo.setU_pic(userInfo.getU_pic());
                workLogSendInfo.setU_cert(userInfo.getU_cert());
                if (userInfo.getU_cert_type() != null) {
                    workLogSendInfo.setU_cert_type(userInfo.getU_cert_type());
                }
                if (!StringUtil.isEmpty(aazhiwu)) {
                    workLogSendInfo.setZhiwu(aazhiwu);
                }
            }


            //查询部门
            ProOrgan querybumen = workLogPushDao.querybumen(wl.getSendUid(), wl.getProId());
            if (querybumen != null) {
                workLogSendInfo.setPo_id(querybumen.getId());
                workLogSendInfo.setPo_name(querybumen.getPoName());
            }
            senderinfo = GsonUtil.toJson(workLogSendInfo);


            //工人json
            ArrayList<WorkLogGr> workLogGrs = workLogDao.queryAllWorkLogGr(wl.getId());
            if (workLogGrs != null && workLogGrs.size() > 0) {
                String wlgids = "";
                for (WorkLogGr wlg : workLogGrs) {
                    wlgids = wlgids + wlg.getUser_id() + ",";
                }

                if (wlgids.length() > 0) {
                    wlgids = wlgids.substring(0, wlgids.length() - 1);
                }
                List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBeanbyworks(wlgids, wl.getProId() + "");
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    workerListinfo = GsonUtil.toJson(grListBeanList);
                }
            }

            //TODO
            //节点json
            WlPwInfoBean wlPwInfoBean = new WlPwInfoBean();
            wlPwInfoBean.setPw_id(0);
            wlPwInfoBean.setPw_name("");
            wlPwInfoBean.setPw_pid(0);
            wlPwInfoBean.setP_pw_name("");
            wlPwInfoBean.setPw_content("");
            if (!StringUtil.isEmpty(wl.getPwContent())) {
                wlPwInfoBean.setPw_content(wl.getPwContent());
            }

            //查询节点
            ProWork querynode = workLogDao.querynode(wl.getPwId() + "");
            if (querynode != null) {
                wlPwInfoBean.setPw_id(querynode.getId());
                wlPwInfoBean.setPw_name(querynode.getPwName() + "");
                wlPwInfoBean.setPw_pid(querynode.getPwPid());

                if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                    //查询节点
                    ProWork pwpidnode = workLogDao.querynode(querynode.getPwPid() + "");
                    if (pwpidnode != null) {
                        wlPwInfoBean.setP_pw_name(querynode.getPwName() + "");
                    }
                } else {
                    wlPwInfoBean.setP_pw_name("");
                }
            }

            if (wlPwInfoBean != null) {
                jiedianinfo = GsonUtil.toJson(wlPwInfoBean);
            }
            workLogDao.updateWorklog(wl.getId(), picListinfo, workerListinfo, senderinfo, jiedianinfo);
        }
    }


    @Async
    public void mustupdateAllworkLogbytime(String startdate, String enddate) {
        ArrayList<WorkLog> workLoglist = workLogDao.mustupdateAllworkLogbytime(startdate, enddate);

        String picListinfo = "";
        String workerListinfo = "";
        String senderinfo = "";
        String jiedianinfo = "";

        for (WorkLog wl : workLoglist) {
            //图片json
            List<WorkLogPic> querypic = workLogDao.querypic(String.valueOf(wl.getId()));
            if (querypic != null && querypic.size() > 0) {
                List<String> picll = new ArrayList<>();
                for (WorkLogPic wlp : querypic) {
                    picll.add(wlp.getWl_pic());
                }
                picListinfo = GsonUtil.toJson(picll);
            }

            //发送人json
            WorkLogSendInfo workLogSendInfo = new WorkLogSendInfo();
            UserInfo userInfo = workLogPushDao.queryUseInfoByID(wl.getSendUid() + "");
            //查询职务
            String aazhiwu = workLogPushDao.queryZhiwu(wl.getProId(), wl.getSendUid());
            workLogSendInfo.setZhiwu("");
            workLogSendInfo.setPo_id(0);
            workLogSendInfo.setPo_name("");
            workLogSendInfo.setU_name(userInfo.getU_name() + "");

            if (userInfo != null) {
                workLogSendInfo.setU_realname(userInfo.getU_realname());
                workLogSendInfo.setU_pic(userInfo.getU_pic());
                workLogSendInfo.setU_cert(userInfo.getU_cert());
                if (userInfo.getU_cert_type() != null) {
                    workLogSendInfo.setU_cert_type(userInfo.getU_cert_type());
                }
                if (!StringUtil.isEmpty(aazhiwu)) {
                    workLogSendInfo.setZhiwu(aazhiwu);
                }
            }


            //查询部门
            ProOrgan querybumen = workLogPushDao.querybumen(wl.getSendUid(), wl.getProId());
            if (querybumen != null) {
                workLogSendInfo.setPo_id(querybumen.getId());
                workLogSendInfo.setPo_name(querybumen.getPoName());
            }
            senderinfo = GsonUtil.toJson(workLogSendInfo);


            //工人json
            ArrayList<WorkLogGr> workLogGrs = workLogDao.queryAllWorkLogGr(wl.getId());
            if (workLogGrs != null && workLogGrs.size() > 0) {
                String wlgids = "";
                for (WorkLogGr wlg : workLogGrs) {
                    wlgids = wlgids + wlg.getUser_id() + ",";
                }

                if (wlgids.length() > 0) {
                    wlgids = wlgids.substring(0, wlgids.length() - 1);
                }
                List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBeanbyworks(wlgids, wl.getProId() + "");
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    workerListinfo = GsonUtil.toJson(grListBeanList);
                }
            }

            //TODO
            //节点json
            WlPwInfoBean wlPwInfoBean = new WlPwInfoBean();
            wlPwInfoBean.setPw_id(0);
            wlPwInfoBean.setPw_name("");
            wlPwInfoBean.setPw_pid(0);
            wlPwInfoBean.setP_pw_name("");
            wlPwInfoBean.setPw_content("");
            if (!StringUtil.isEmpty(wl.getPwContent())) {
                wlPwInfoBean.setPw_content(wl.getPwContent());
            }

            //查询节点
            ProWork querynode = workLogDao.querynode(wl.getPwId() + "");
            if (querynode != null) {
                wlPwInfoBean.setPw_id(querynode.getId());
                wlPwInfoBean.setPw_name(querynode.getPwName() + "");
                wlPwInfoBean.setPw_pid(querynode.getPwPid());

                if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                    //查询节点
                    ProWork pwpidnode = workLogDao.querynode(querynode.getPwPid() + "");
                    if (pwpidnode != null) {
                        wlPwInfoBean.setP_pw_name(querynode.getPwName() + "");
                    }
                } else {
                    wlPwInfoBean.setP_pw_name("");
                }
            }

            if (wlPwInfoBean != null) {
                jiedianinfo = GsonUtil.toJson(wlPwInfoBean);
            }
            workLogDao.updateWorklog(wl.getId(), picListinfo, workerListinfo, senderinfo, jiedianinfo);
        }
    }


    @Async
    public void mustupdateAllworkLogbyProject(Integer proid) {
        ArrayList<WorkLog> workLoglist = workLogDao.mustupdateAllworkLogbyProject(proid);
        String picListinfo = "";
        String workerListinfo = "";
        String senderinfo = "";
        String jiedianinfo = "";

        for (WorkLog wl : workLoglist) {
            //图片json
            List<WorkLogPic> querypic = workLogDao.querypic(String.valueOf(wl.getId()));
            if (querypic != null && querypic.size() > 0) {
                List<String> picll = new ArrayList<>();
                for (WorkLogPic wlp : querypic) {
                    picll.add(wlp.getWl_pic());
                }
                picListinfo = GsonUtil.toJson(picll);
            }

            //发送人json
            WorkLogSendInfo workLogSendInfo = new WorkLogSendInfo();
            UserInfo userInfo = workLogPushDao.queryUseInfoByID(wl.getSendUid() + "");
            //查询职务
            String aazhiwu = workLogPushDao.queryZhiwu(wl.getProId(), wl.getSendUid());
            workLogSendInfo.setZhiwu("");
            workLogSendInfo.setPo_id(0);
            workLogSendInfo.setPo_name("");
            workLogSendInfo.setU_name(userInfo.getU_name() + "");

            if (userInfo != null) {
                workLogSendInfo.setU_realname(userInfo.getU_realname());
                workLogSendInfo.setU_pic(userInfo.getU_pic());
                workLogSendInfo.setU_cert(userInfo.getU_cert());
                if (userInfo.getU_cert_type() != null) {
                    workLogSendInfo.setU_cert_type(userInfo.getU_cert_type());
                }
                if (!StringUtil.isEmpty(aazhiwu)) {
                    workLogSendInfo.setZhiwu(aazhiwu);
                }
            }


            //查询部门
            ProOrgan querybumen = workLogPushDao.querybumen(wl.getSendUid(), wl.getProId());
            if (querybumen != null) {
                workLogSendInfo.setPo_id(querybumen.getId());
                workLogSendInfo.setPo_name(querybumen.getPoName());
            }
            senderinfo = GsonUtil.toJson(workLogSendInfo);


            //工人json
            ArrayList<WorkLogGr> workLogGrs = workLogDao.queryAllWorkLogGr(wl.getId());
            if (workLogGrs != null && workLogGrs.size() > 0) {
                String wlgids = "";
                for (WorkLogGr wlg : workLogGrs) {
                    wlgids = wlgids + wlg.getUser_id() + ",";
                }

                if (wlgids.length() > 0) {
                    wlgids = wlgids.substring(0, wlgids.length() - 1);
                }
                List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBeanbyworks(wlgids, wl.getProId() + "");
                if (grListBeanList != null && grListBeanList.size() > 0) {
                    workerListinfo = GsonUtil.toJson(grListBeanList);
                }
            }

            //TODO
            //节点json
            WlPwInfoBean wlPwInfoBean = new WlPwInfoBean();
            wlPwInfoBean.setPw_id(0);
            wlPwInfoBean.setPw_name("");
            wlPwInfoBean.setPw_pid(0);
            wlPwInfoBean.setP_pw_name("");
            wlPwInfoBean.setPw_content("");
            if (!StringUtil.isEmpty(wl.getPwContent())) {
                wlPwInfoBean.setPw_content(wl.getPwContent());
            }

            //查询节点
            ProWork querynode = workLogDao.querynode(wl.getPwId() + "");
            if (querynode != null) {
                wlPwInfoBean.setPw_id(querynode.getId());
                wlPwInfoBean.setPw_name(querynode.getPwName() + "");
                wlPwInfoBean.setPw_pid(querynode.getPwPid());

                if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                    //查询节点
                    ProWork pwpidnode = workLogDao.querynode(querynode.getPwPid() + "");
                    if (pwpidnode != null) {
                        wlPwInfoBean.setP_pw_name(querynode.getPwName() + "");
                    }
                } else {
                    wlPwInfoBean.setP_pw_name("");
                }
            }

            if (wlPwInfoBean != null) {
                jiedianinfo = GsonUtil.toJson(wlPwInfoBean);
            }
            workLogDao.updateWorklog(wl.getId(), picListinfo, workerListinfo, senderinfo, jiedianinfo);
        }
    }


    //发布工作日志
    public Object saveworklog(WorkLogDaoBean wlparamer, String gr_uid, String wl_pic) {

        WorkLogParamer wl = new WorkLogParamer();
        if (!StringUtil.isEmpty(wlparamer.getPro_id())) {
            wl.setPro_id(Double.valueOf(wlparamer.getPro_id()).intValue());
        }
        if (!StringUtil.isEmpty(wlparamer.getSend_uid())) {
            wl.setSend_uid(Double.valueOf(wlparamer.getSend_uid()).intValue());
        }

        if (!StringUtil.isEmpty(wlparamer.getWl_type())) {
            wl.setWl_type(Double.valueOf(wlparamer.getWl_type()).intValue());
        }


        if (!StringUtil.isEmpty(wlparamer.getWl_content())) {
            wl.setWl_content(wlparamer.getWl_content());
        }


        if (!StringUtil.isEmpty(wlparamer.getWl_star())) {
            wl.setWl_star(Double.valueOf(wlparamer.getWl_star()).intValue());
        } else {
            wl.setWl_star(0);
        }

        if (!StringUtil.isEmpty(wlparamer.getWl_yzcd())) {
            wl.setWl_yzcd(Double.valueOf(wlparamer.getWl_yzcd()).intValue());
        } else {
            wl.setWl_yzcd(0);
        }

        if (!StringUtil.isEmpty(wlparamer.getWl_safe_type())) {
            wl.setWl_safe_type(Double.valueOf(wlparamer.getWl_safe_type()).intValue());
        } else {
            wl.setWl_safe_type(0);
        }
        if (!StringUtil.isEmpty(wlparamer.getWl_lbs_x())) {
            wl.setWl_lbs_x(BigDecimal.valueOf(Double.valueOf(wlparamer.getWl_lbs_x())));
        }
        if (!StringUtil.isEmpty(wlparamer.getWl_lbs_y())) {
            wl.setWl_lbs_y(BigDecimal.valueOf(Double.valueOf(wlparamer.getWl_lbs_y())));
        }
        if (!StringUtil.isEmpty(wlparamer.getWl_lbs_name())) {
            wl.setWl_lbs_name(wlparamer.getWl_lbs_name());
        }

        if (!StringUtil.isEmpty(wlparamer.getPw_id())) {
            wl.setPw_id(Double.valueOf(wlparamer.getPw_id()).intValue());
        }
        if (!StringUtil.isEmpty(wlparamer.getPw_content())) {
            wl.setPw_content(wlparamer.getPw_content());
        }

        if (!StringUtil.isEmpty(wlparamer.getWl_safe_type())) {
            wl.setWl_safe_type(Double.valueOf(wlparamer.getWl_safe_type()).intValue());
        } else {
            wl.setWl_safe_type(0);
        }

        //项目名称
        Project project = workLogPushDao.queryProject(wlparamer.getPro_id());
        if (project != null && !StringUtil.isEmpty(project.getPName())) {
            wl.setPro_name(project.getPName());
        }

        //图片json
        if (!StringUtil.isEmpty(wl_pic)) {
            List<String> list = new ArrayList<>();
            String[] split = wl_pic.split("\\|");
            for (String ss : split) {
                list.add(ss);
            }
            wl.setPic_list(GsonUtil.toJson(list));
        }

        //发送人json
        WorkLogSendInfo workLogSendInfo = new WorkLogSendInfo();
        UserInfo userInfo = workLogPushDao.queryUseInfoByID(wl.getSend_uid() + "");
        //查询职务
        String aazhiwu = workLogPushDao.queryZhiwu(wl.getPro_id(), wl.getSend_uid());
        workLogSendInfo.setZhiwu("");
        workLogSendInfo.setPo_id(0);
        workLogSendInfo.setPo_name("");
        workLogSendInfo.setU_name(userInfo.getU_name() + "");

        if (userInfo != null) {
            workLogSendInfo.setU_realname(userInfo.getU_realname());
            workLogSendInfo.setU_pic(userInfo.getU_pic());
            workLogSendInfo.setU_cert(userInfo.getU_cert());
            if (userInfo.getU_cert_type() != null && userInfo.getU_cert_type() > 0) {
                workLogSendInfo.setU_cert_type(userInfo.getU_cert_type());
            } else {
                workLogSendInfo.setU_cert_type(0);
            }

            if (!StringUtil.isEmpty(aazhiwu)) {
                workLogSendInfo.setZhiwu(aazhiwu);
            }
        }
        //查询部门
        ProOrgan querybumen = workLogPushDao.querybumen(wl.getSend_uid(), wl.getPro_id());
        if (querybumen != null) {
            workLogSendInfo.setPo_id(querybumen.getId());
            workLogSendInfo.setPo_name(querybumen.getPoName());
        }
        wl.setSend_info(GsonUtil.toJson(workLogSendInfo));

        //工人json
        if (!StringUtil.isEmpty(gr_uid)) {
            String regruidre = gr_uid.replace("\\|", ",");
            regruidre = regruidre.replace("|", ",");
            List<WorkLogDetail.GrListBean> grListBeanList = workLogPushDao.queryGrListBeanbyworks(regruidre, wlparamer.getPro_id());
            wl.setWorks_list(GsonUtil.toJson(grListBeanList));
        }

        //节点json
        WlPwInfoBean wlPwInfoBean = new WlPwInfoBean();
        wlPwInfoBean.setPw_id(0);
        wlPwInfoBean.setPw_name("");
        wlPwInfoBean.setPw_pid(0);
        wlPwInfoBean.setP_pw_name("");
        wlPwInfoBean.setPw_content("");
        if (!StringUtil.isEmpty(wlparamer.getPw_content())) {
            wlPwInfoBean.setPw_content(wlparamer.getPw_content());
        }

        if (!StringUtil.isEmpty(wlparamer.getPw_id())) {
            //查询节点
            ProWork querynode = workLogDao.querynode(wlparamer.getPw_id());
            if (querynode != null) {
                wlPwInfoBean.setPw_id(querynode.getId());
                wlPwInfoBean.setPw_name(querynode.getPwName() + "");
                wlPwInfoBean.setPw_pid(querynode.getPwPid());

                if (querynode.getPwPid() != null && querynode.getPwPid() != 0) {
                    //查询节点
                    ProWork pwpidnode = workLogDao.querynode(querynode.getPwPid() + "");
                    if (pwpidnode != null) {
                        wlPwInfoBean.setP_pw_name(pwpidnode.getPwName() + "");
                    }
                } else {
                    wlPwInfoBean.setP_pw_name("");
                }
            }
        }

        if (wlPwInfoBean != null) {
            wl.setWl_pw_info(GsonUtil.toJson(wlPwInfoBean));
        }


        try {
            String saveworklogid = "";

            wl.setWl_state(1);

            if (wl.getWl_yzcd() == null || wl.getWl_yzcd() <= 0) {
                wl.setWl_yzcd(0);
            }

            if (wl.getWl_safe_type() == null || wl.getWl_safe_type() <= 0) {
                wl.setWl_safe_type(0);
            }

            wl.setIn_time(new Date());

            if (wl.getWl_star() == null || wl.getWl_star() <= 0) {
                wl.setWl_star(0);
            }


            //保持一条工作日志，返回相应的工作日志id
            WorkLogParamer saveworklog = workLogDao.saveworklog(wl);
            saveworklogid = String.valueOf(saveworklog.getId());


            //工人工作对照表
            if (!StringUtil.isEmpty(gr_uid)) {
                String[] split = gr_uid.split("\\|");
                for (String ss : split) {
                    workLogDao.saveworkloggr(saveworklogid, ss);
                }
            }

            //保存工作日志图片
            if (!StringUtil.isEmpty(wl_pic)) {
                String[] split = wl_pic.split("\\|");
                for (String ss : split) {
                    workLogDao.saveworklogpic(saveworklogid, ss);
                }
            }

            Map<String, String> map = new HashMap<>();
            map.put("return", "ok");
            map.put("wl_id", saveworklogid);


            //推送
            if (!StringUtil.isEmpty(saveworklogid)) {
                //符合质量评星过低的条件的就推送
                if (wl.getWl_type() == 1 && (wl.getWl_star() == 1 || wl.getWl_star() == 2)) {
                    Map<String, Object> map1 = queryPushmsg(saveworklogid, wl.getWl_type(), wl.getWl_star(), wl.getWl_yzcd(), wl.getWl_safe_type(), gr_uid, saveworklog);
                    System.out.println(map1.toString());
                }
                if (wl.getWl_type() == 2 && (wl.getWl_yzcd() == 1 || wl.getWl_yzcd() == 3) && (wl.getWl_safe_type() == 1 || wl.getWl_safe_type() == 2 || wl.getWl_safe_type() == 3)) {
                    Map<String, Object> map1 = queryPushmsg(saveworklogid, wl.getWl_type(), wl.getWl_star(), wl.getWl_yzcd(), wl.getWl_safe_type(), gr_uid, saveworklog);
                    System.out.println(map1.toString());
                }
            }


            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    //处理推送的消息格式
    @Async
    public Map<String, Object> queryPushmsg(String id, Integer wl_type, Integer wl_star, Integer wl_yzcd, Integer wl_safe_type, String gr_uid, WorkLogParamer saveworklog) {
        Map<String, Object> result = new HashMap<>();
        try {
            //查询到质量的推送信息
            WorkLogQualityPushDto workLogQualityPushDto = new WorkLogQualityPushDto();

            workLogQualityPushDto.setId(saveworklog.getId());
            workLogQualityPushDto.setProjectID(saveworklog.getPro_id());
            workLogQualityPushDto.setProjectName(saveworklog.getPro_name());

            workLogQualityPushDto.setIn_time(DateUtil.format(saveworklog.getIn_time(), DateUtil.FORMAT_DATE_TIME));

            workLogQualityPushDto.setWl_star(saveworklog.getWl_star());
            String wl_lbs_name = "";
            if (!StringUtil.isEmpty(saveworklog.getWl_lbs_name())) {
                wl_lbs_name = wl_lbs_name + saveworklog.getWl_lbs_name();
            }


            WlPwInfoBean wlPwInfoBean = GsonUtil.fromJson(saveworklog.getWl_pw_info(), WlPwInfoBean.class);
            if (wlPwInfoBean != null) {
                if (!StringUtil.isEmpty(wlPwInfoBean.getPw_name())) {
                    wl_lbs_name = wl_lbs_name + wlPwInfoBean.getPw_name();
                }
                if (!StringUtil.isEmpty(wlPwInfoBean.getP_pw_name())) {
                    wl_lbs_name = wl_lbs_name + wlPwInfoBean.getP_pw_name();
                }
            }

            workLogQualityPushDto.setWl_lbs_name(wl_lbs_name);
            workLogQualityPushDto.setWl_content(saveworklog.getWl_content());

            String checkname = "";

            WorkLogSendInfo workLogSendInfo = GsonUtil.fromJson(saveworklog.getSend_info(), WorkLogSendInfo.class);
            if (workLogSendInfo != null) {


                if (!StringUtil.isEmpty(workLogSendInfo.getU_realname())) {
                    checkname = workLogSendInfo.getU_realname();
                }


                String aazhiwu = workLogSendInfo.getZhiwu();
                if (!StringUtil.isEmpty(aazhiwu)) {
                    checkname = checkname + aazhiwu;
                }

                workLogQualityPushDto.setCheckName(checkname);

            }

            String constructionteam = "";
            String constructionperson = "";

            List<WorkLogDetail.GrListBean> gr_list = GsonUtil.jsonToList(saveworklog.getWorks_list(), WorkLogDetail.GrListBean.class);
            List<WorkLogDetail.GrListBean> gr_listquchonghou = new ArrayList<>();

            if (gr_list != null && gr_list.size() > 0) {
                List<String> banzuAndgongzhongList = new ArrayList<>();
                String zz = "";
                for (WorkLogDetail.GrListBean gl : gr_list) {
                    if (!zz.contains(gl.getGr_po_id())) {
                        zz = zz + gl.getGr_po_id() + ",";
                    }
                }
                if (!StringUtil.isEmpty(zz)) {
                    zz = zz.substring(0, zz.length() - 1);
                    String[] split = zz.split(",");
                    for (String ss : split) {
                        banzuAndgongzhongList.add(ss);
                    }

                    for (String ii : banzuAndgongzhongList) {
                        quchongaa:
                        for (int i = 0; i < gr_list.size(); i++) {
                            if (gr_list.get(i).getGr_po_id().equals(ii)) {
                                gr_listquchonghou.add(gr_list.get(i));
                                break quchongaa;
                            }
                        }
                    }
                    for (WorkLogDetail.GrListBean gg : gr_listquchonghou) {
                        if (!StringUtil.isEmpty(gg.getGr_banzuzhang())) {
                            constructionteam = constructionteam + gg.getGr_banzuzhang() + gg.getGr_gongzhong() + gg.getGr_po_name() + ",";
                        } else {
                            constructionteam = constructionteam + gg.getGr_gongzhong() + gg.getGr_po_name() + ",";
                        }
                    }
                    if (!StringUtil.isEmpty(constructionteam)) {
                        constructionteam = constructionteam.substring(0, constructionteam.length() - 1);
                        constructionteam = constructionteam.replace("null", "");
                    }


                }

                for (WorkLogDetail.GrListBean gg : gr_list) {
                    if (!StringUtil.isEmpty(gg.getGr_gongzhong())) {
                        constructionperson = constructionperson + gg.getGr_gongzhong();
                    }

                    if (!StringUtil.isEmpty(gg.getGr_u_realname())) {
                        constructionperson = constructionperson + gg.getGr_u_realname() + ",";
                    }

                }
                if (!StringUtil.isEmpty(constructionperson)) {
                    constructionperson = constructionperson.substring(0, constructionperson.length() - 1);
                }
            }
            workLogQualityPushDto.setConstructionteam(constructionteam);
            workLogQualityPushDto.setConstructionperson(constructionperson);

            //查询接收人的id列表
            List<Object> userList = new ArrayList<>();

            //透传
            List<Object> cmdlist = new ArrayList<>();
            //判断
            List<Object> Imlist = new ArrayList<>();

            userList.clear();
            cmdlist.clear();
            Imlist.clear();

//            userList.add(951871);
//            userList.add(47211);
//            userList.add(951888);
//            userList.add(28276);


            Map<String, Object> extMap = new HashMap<>();
            if (wl_type == 1) {
                extMap.put("type", "quality");
                workLogQualityPushDto.setTitle("质量评分偏低");
                workLogQualityPushDto.setExplain("质量评分偏低，请及时整改质量问题。");

                //项目经理和生成经理
                List<Object> qll = workLogPushDao.getUserList(workLogQualityPushDto.getProjectID());

                userList.addAll(qll);

                for (Object oo : userList) {
                    MassageSwitch massageSwitch = workLogPushDao.querymassageSwitch(workLogQualityPushDto.getProjectID(), "2", oo.toString());
                    if (massageSwitch != null && massageSwitch.getmStatus() == 0) {//免打扰
                        cmdlist.add(oo);
                    } else {
                        Imlist.add(oo);
                    }
                }

            } else if (wl_type == 2 && (wl_yzcd == 1 || wl_yzcd == 3) && wl_safe_type == 1) {
                extMap.put("type", "save");
                workLogQualityPushDto.setTitle("人的不安全行为");
                workLogQualityPushDto.setExplain("发现现场严重违反安全文明规定，请及时改正，避免发生事故。");

                //生产经理
                List<Object> qll1 = workLogPushDao.getUserListOrddo(workLogQualityPushDto.getProjectID());
                userList.addAll(qll1);
                //工人
                if (!StringUtil.isEmpty(gr_uid)) {
                    String[] split = gr_uid.split("\\|");
                    for (String ss : split) {
                        userList.add(Integer.parseInt(ss));
                    }
                }

                for (Object oo : userList) {
                    MassageSwitch massageSwitch = workLogPushDao.querymassageSwitch(workLogQualityPushDto.getProjectID(), "3", oo.toString());
                    if (massageSwitch != null && massageSwitch.getmStatus() == 0) {//免打扰
                        cmdlist.add(oo);
                    } else {
                        Imlist.add(oo);
                    }
                }
            } else if (wl_type == 2 && (wl_yzcd == 1 || wl_yzcd == 3) && wl_safe_type == 2) {
                extMap.put("type", "save");
                workLogQualityPushDto.setTitle("物的不安全行为");
                workLogQualityPushDto.setExplain("发现现场安全设施设备、安全防护存在重大隐患，请及时处理，避免发生事故。");

                //生产经理
                List<Object> qll2 = workLogPushDao.getUserListOrddo(workLogQualityPushDto.getProjectID());
                userList.addAll(qll2);


                for (Object oo : userList) {
                    MassageSwitch massageSwitch = workLogPushDao.querymassageSwitch(workLogQualityPushDto.getProjectID(), "3", oo.toString());
                    if (massageSwitch != null && massageSwitch.getmStatus() == 0) {//免打扰
                        cmdlist.add(oo);
                    } else {
                        Imlist.add(oo);
                    }
                }
            } else if (wl_type == 2 && (wl_yzcd == 1 || wl_yzcd == 3) && wl_safe_type == 3) {
                extMap.put("type", "save");
                workLogQualityPushDto.setTitle("管理的缺陷");
                workLogQualityPushDto.setExplain("发现现场安全管理存在重大隐患，请及时处理，避免发生事故。");

                //项目经理和生成经理
                List<Object> qll3 = workLogPushDao.getUserList(workLogQualityPushDto.getProjectID());
                userList.addAll(qll3);

                for (Object oo : userList) {
                    MassageSwitch massageSwitch = workLogPushDao.querymassageSwitch(workLogQualityPushDto.getProjectID(), "3", oo.toString());
                    if (massageSwitch != null && massageSwitch.getmStatus() == 0) {//免打扰
                        cmdlist.add(oo);
                    } else {
                        Imlist.add(oo);
                    }
                }
            }


            workLogQualityPushDto.setWl_safe_type(wl_safe_type);
            workLogQualityPushDto.setWl_star(Long.parseLong(wl_star + ""));
            workLogQualityPushDto.setId(Integer.parseInt(id));

            //项目名称
            if (!StringUtil.isEmpty(workLogQualityPushDto.getProjectName())) {
                extMap.put("myUserName", workLogQualityPushDto.getProjectName());
            } else {
                extMap.put("myUserName", "");
            }
            workLogQualityPushDto.setWl_yzcd(wl_yzcd);
            extMap.put("worklog", workLogQualityPushDto);

            log.error(GsonUtil.toJson(extMap));
            log.error(GsonUtil.toJson(userList));
            log.error(GsonUtil.toJson(cmdlist));
            log.error(GsonUtil.toJson(Imlist));

            if (cmdlist != null && cmdlist.size() > 0) {
                //推送人是用：项目id    'project-1234'
                HuanXinRequestUtils.sendMessage("project-" + workLogQualityPushDto.getProjectID(), cmdlist, extMap, HuanXinRequestUtils.DEFAULT_MSG, true);
            }

            if (Imlist != null && Imlist.size() > 0) {
                //推送人是用：项目id    'project-1234'
                HuanXinRequestUtils.sendMessage("project-" + workLogQualityPushDto.getProjectID(), Imlist, extMap, HuanXinRequestUtils.DEFAULT_MSG);
            }

            result.put("sendmsg", "发送成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("sendmsg", "发送失败");
        }
        return result;
    }


    //查询工作详情
    public WorkLogDetail worklogDetail(String id) {
        //工作记录
        WorkLogDetail workLogDetail = workLogDao.worklogdetail(id);
        if (workLogDetail != null) {
            return workLogDetail;
        }
        return null;
    }

    public UserInfo queryUseInfoByID(String id) {
        return workLogPushDao.queryUseInfoByID(id);
    }


    public Project queryProject(String id) {
        return workLogPushDao.queryProject(id);
    }
}
