package com.ktp.project.dao;

import com.ktp.project.dto.ProGZKaoQinLvDto;
import com.ktp.project.dto.ProjectDeptDto;
import com.ktp.project.dto.ProjectDetailDto;
import com.ktp.project.dto.ProjectDto;
import com.ktp.project.dto.WorkingHoursDto;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import com.zm.entity.ProjectCollectId;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 11:08
 */
@Repository("proDao")
@Transactional
public class ProjectDao {
    private static final Logger log = LoggerFactory.getLogger(ProjectDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存项目信息
     *
     * @return boolean
     * @params: [project]
     * @Author: wuyeming
     * @Date: 2018-10-05 11:29
     */
    @CacheEvict(value = "MasterCache", allEntries = true, beforeInvocation = true)//清除缓存
    public Integer saveProject(Project project) {
        Integer proId = null;
        try {
            if (StringUtils.isEmpty(project.getId())) {
                getCurrentSession().save(project);
                proId = project.getId();
                callProcedure(proId);//调用存储过程生成考勤表
                ProOrgan organ = new ProOrgan();
                organ.setProId(proId);
                organ.setPoState(1);
                organ.setPoName("默认");
                organ.setPoFzr(project.getPPrincipal());
                organ.setPoGzid(0);
                organ.setOperationTime(new Date());
                getCurrentSession().saveOrUpdate(organ);
                Integer poId = organ.getId();
                ProOrganPer per = new ProOrganPer();
                per.setPoId(poId);
                per.setPType(121);//项目负责人
                per.setUserId(project.getPPrincipal());
                per.setPopState(0);
                per.setPopType(7);
                per.setPopJn(0.00);
                per.setPopXy(0.00);
                per.setPopCard(0L);
                per.setPopIntime(new Timestamp(System.currentTimeMillis()));
                per.setpId(project.getId());
                getCurrentSession().save(per);
            } else {
                proId = project.getId();
                String hql = "from Project where id = " + proId + "";
                Query query = getCurrentSession().createQuery(hql);
                Project pro = (Project) query.uniqueResult();
                pro.setPName(project.getPName());
                pro.setPContent(project.getPContent());
                pro.setPBegintime(project.getPBegintime());
                pro.setPEndtime(project.getPEndtime());
                pro.setZj(project.getZj());
                pro.setPCity(project.getPCity());
                getCurrentSession().update(pro);
            }
        } catch (Exception e) {
            throw e;
        }
        return proId;
    }


    /**
     * 获取项目详情
     *
     * @return com.ktp.project.entity.Project
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-05 15:33
     */
    @Cacheable(value = "MasterCache", key = "#id + '_'+'getProjectInfo'")
    public ProjectDto getProjectInfo(Integer id) {
        String sql = "SELECT p.id,p.p_name pName,p.p_content pContent,DATE_FORMAT(p.p_begintime, '%Y-%m-%d') pBegintime,DATE_FORMAT(p.p_endtime, '%Y-%m-%d') pEndtime,p.zj," +
                "r.youbian shengId,r.r_name sheng,s.youbian shiId,s.r_name shi,q.youbian quId,q.r_name qu,  " +
                "p.hatch_status hatchStatus, p.p_state PState, p.p_address pAddress, p.p_area pArea  " +
                "FROM project p LEFT " +
                "JOIN region q ON q.id = p.p_city " +
                "LEFT JOIN region s ON s.id = q.p_id " +
                "LEFT JOIN region r ON r.id = s.p_id " +
                "where p.id = ? ";
        ProjectDto projectDto = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setParameter(0, id);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDto.class));
            projectDto = (ProjectDto) sqlQuery.uniqueResult();
        } catch (Exception e) {
            throw e;
        }
        return projectDto;
    }


    /**
     * 项目统计-获取项目详情
     *
     * @return com.ktp.project.dto.ProjectDetailDto
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-06 16:39
     */
    @Cacheable(value = "MasterCache", key = "#id + '_'+'getProjectDetail'")
    public ProjectDetailDto getProjectDetail(Integer id) {
        //统计人数
        String sql = "SELECT DISTINCT p.id,p.p_name pName,p.p_content pContent,DATE_FORMAT(p.p_begintime, '%Y-%m-%d') pBegintime,DATE_FORMAT(p.p_endtime, '%Y-%m-%d') pEndtime," +
                "r.youbian shengId,r.r_name sheng,s.youbian shiId,s.r_name shi,q.youbian quId,q.r_name qu " +
                "FROM pro_organ po " +
                "JOIN project p ON p.id = po.pro_id " +
                "LEFT JOIN user_info u ON u.id = po.po_fzr " +
                "LEFT JOIN region q ON q.id = p.p_city " +
                "LEFT JOIN region s ON s.id = q.p_id " +
                "LEFT JOIN region r ON r.id = s.p_id " +
                "WHERE p.id  = ? ";
        ProjectDetailDto projectDetailDto = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setParameter(0, id);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDetailDto.class));
            projectDetailDto = (ProjectDetailDto) sqlQuery.uniqueResult();
            //查询人员
            String managerSql = "SELECT  user_id userId,u_realname uRealname,u_pic uPic,zhiwu,po_id poId,po_name poName,u_sex uSex," +
                    "u_nicheng uNicheng,u_name uName FROM v_pro_organ_per " +
                    "WHERE pro_id=? AND pop_state=0 AND is_del=0 AND po_state=1 ORDER BY p_type DESC";
            sqlQuery = getCurrentSession().createSQLQuery(managerSql);
            sqlQuery.setParameter(0, id);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDetailDto.Manager.class));
            List<ProjectDetailDto.Manager> managerList = sqlQuery.list();
            projectDetailDto.setCounts(managerList.size());
            projectDetailDto.setManagerList(managerList);
        } catch (Exception e) {
            throw e;
        }
        return projectDetailDto;
    }


    /**
     * 项目统计-获取工种信息
     *
     * @return java.util.List<com.ktp.project.dto.ProjectDeptDto>
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-08 09:34
     */
    @Cacheable(value = "MasterCache", key = "#id + '_'+'getDeptList'")
    public List<ProjectDeptDto> getDeptList(Integer id) {
        String sql = "SELECT COUNT(*) counts,k.id, k.key_name keyName " +
                "FROM pro_organ po " +
                "JOIN project p ON p.id = po.pro_id " +
                "JOIN user_info u ON u.id = po.po_fzr " +
                "JOIN key_content k ON k.id = po.po_gzid " +
                "WHERE p.id = ? GROUP BY k.id ";
        List<ProjectDeptDto> list = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setParameter(0, id);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDeptDto.class));
            list = sqlQuery.list();
            String ids = "0";
            for (ProjectDeptDto projectDeptDto : list) {
                ids += "," + projectDeptDto.getId();
                String deptSql = "SELECT po.id poId,po.po_name poName,u.id uId,u.u_realname uRealname,u.u_nicheng uNicheng,u.u_name uName,u_sex uSex,u.u_pic uPic " +
                        "FROM pro_organ po " +
                        "JOIN project p ON p.id = po.pro_id " +
                        "JOIN user_info u ON u.id = po.po_fzr " +
                        "JOIN key_content k ON k.id = po.po_gzid " +
                        "WHERE p.id = ? AND k.id = ? ";
                sqlQuery = getCurrentSession().createSQLQuery(deptSql);
                sqlQuery.setParameter(0, id);
                sqlQuery.setParameter(1, projectDeptDto.getId());
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDeptDto.Dept.class));
                List<ProjectDeptDto.Dept> deptList = sqlQuery.list();
                projectDeptDto.setDeptList(deptList);
            }
            String allKey = "";
            if (ids.length() > 1 && list.size() > 0) {
                allKey = "SELECT 0 as counts,k.id, k.key_name keyName FROM key_content k WHERE key_id = 4 AND id NOT in(" + ids + ")";
            } else if (list.size() == 0) {
                allKey = "SELECT 0 as counts,k.id, k.key_name keyName FROM key_content k WHERE key_id = 4";
            }
            sqlQuery = getCurrentSession().createSQLQuery(allKey);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectDeptDto.class));
            List<ProjectDeptDto> noList = sqlQuery.list();
            if (noList.size() > 0) {
                for (ProjectDeptDto projectDeptDto : noList) {
                    projectDeptDto.setDeptList(new ArrayList<>());
                }
                list.addAll(noList);
            }
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * 根据城市编码获取城市id
     *
     * @return java.lang.Integer
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-09 09:27
     */
    @Cacheable(value = "MasterCache", key = "#id + '_'+'getCityId'")
    public Integer getCityId(Integer id) {
        String sql = "SELECT id FROM region WHERE youbian = ? ";
        SQLQuery sqlQuery = null;
        try {
            sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setParameter(0, id);
            Integer count = (Integer) sqlQuery.uniqueResult();
            return count;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 调用存储过程
     *
     * @return void
     * @Author: wuyeming
     * @params: [projectId]
     * @Date: 2018/10/9 17:27
     */
    public void callProcedure(Integer projectId) {
        try {
            Query query = getCurrentSession().createSQLQuery("{call creat_table_kaoqin(?)}");
            query.setInteger(0, projectId);
            query.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }


    @Cacheable(value = "MasterCache")
    public List<ProjectCollectId> getProjectCollectId() {
        String sql = "select id id, DATE_FORMAT(statistics_date,'%Y-%m-%d') statisticsDateStr,user_count userCount,new_user_count newUserCount,worker_count workCount,attendance_count attendanceCount,new_attendance_count newAttendanceCount," +
                "app_attendance_count appAttendanceCount,zj_attendance_count zjAttendanceCount,app_activity appActivity,DATE_FORMAT(create_time,'%Y-%m-%d') createTimeStr,project_count projectCount\n" +
                ",new_project_count newProjectCount,project_id projectId,all_project_count allProjectCount,all_kaoqin_count allKaoQinCount,kaoqinlv KaoQinLv" +
                " from project_collect_id where DATE_FORMAT(statistics_date,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProjectCollectId.class));
        return sqlQuery.list();
    }


    /**
     * 根据主键查询
     *
     * @param primaryKey
     * @return
     */
    public Project queryOne(int primaryKey) {
        return (Project) getCurrentSession().get(Project.class, primaryKey);
    }


    /**
     * 创建
     *
     * @param project
     */
    public void create(Project project) {
        getCurrentSession().save(project);
        // 2018-11-27 wuyeming
        int proId = project.getId();
        ProOrgan organ = new ProOrgan();
        organ.setProId(proId);
        organ.setPoState(1);
        organ.setPoName("默认");
        organ.setPoFzr(project.getPPrincipal());
        organ.setPoGzid(0);
        organ.setOperationTime(new Date());
        getCurrentSession().saveOrUpdate(organ);
        Integer poId = organ.getId();
        ProOrganPer per = new ProOrganPer();
        per.setPoId(poId);
        per.setpId(proId);
        per.setPType(121);//项目负责人
        per.setUserId(project.getPPrincipal());
        per.setPopState(0);
        per.setPopType(7);
        per.setPopJn(0.00);
        per.setPopXy(0.00);
        per.setPopCard(0L);
        per.setPopIntime(new Timestamp(System.currentTimeMillis()));
        getCurrentSession().save(per);
        callProcedure(proId);  // 调用存储过程生成考勤表
    }


    /**
     * 更新
     *
     * @param project
     */
    public void update(Project project) {
        getCurrentSession().update(project);
    }


    /**
     * 查询用户所在项目的ID
     *
     * @param userId
     * @return
     */
    public List getSubordinateProjectsId(int userId) {
        String sql = "select pro_id , p_name from v_pro_organ_per where is_del = ? and pop_state = ? and user_id = ? group by pro_id";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, 0);
        sqlQuery.setParameter(1, 0);
        sqlQuery.setParameter(2, userId);
        return sqlQuery.list();
    }


    /**
     * 查询所有项目
     *
     * @return java.util.List
     * @params: []
     * @Author: wuyeming
     * @Date: 2018-12-7 下午 15:23
     */
    public List getAllProject() {
        String hql = "from Project p WHERE p.isDel = 0 AND PState <> 4";
//        String hql = "from Project p WHERE p.isDel = 0 AND PState <> 4 AND id = 138";//测试用
        Query query = this.getCurrentSession().createQuery(hql);
        return query.list();
    }

    public List<Integer> getProjectId() {
        String sql = "SELECT pro_id FROM pro_gz_kaoqinlv WHERE DATE_FORMAT(create_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') GROUP BY pro_id";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        return sqlQuery.list();
    }

    public List<ProGZKaoQinLvDto> getProGZKaoQinLv(Integer proId) {
        String sql = "SELECT gz gzName,chuqin_count chuQinCount,count count,chuqinlv chuQinLv FROM pro_gz_kaoqinlv WHERE pro_id=? and DATE_FORMAT(create_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProGZKaoQinLvDto.class));
        sqlQuery.setParameter(0,proId);
        return sqlQuery.list();
    }

    public List<WorkingHoursDto> getWorkingHours() {
        String sql = "SELECT user_id userId,time_sum timeCount,pro_id proId FROM working_hours WHERE DATE_FORMAT(create_time,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkingHoursDto.class));
        return sqlQuery.list();
    }

    public String getProjectName(Integer proId) {
        String sql = "SELECT p_name from project where id=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,proId);
        return (String) sqlQuery.uniqueResult();
    }

    public Project getProjectById(Integer proId) {
        String sql = " select  p_name as  PName ,p_intime  as PIntime  from project where id=?";
        SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
        query.setResultTransformer(new AliasToBeanResultTransformer(Project.class));
        query.setParameter(0,proId);
        return (Project) query.uniqueResult();
    }

    /* *//**
     * 获取项目是否发送标识
     *//*
    public Project getPSent(int project_id) {
        try {
            String queryString = "select IFNULL(roject.p_sent,'') from Project project  where project.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, project_id);
            return (String) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取项目是否发送标识", re);
            throw re;
        }
    }*/
}
