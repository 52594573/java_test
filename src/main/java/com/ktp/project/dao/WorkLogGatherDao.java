package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.ktp.project.constant.JobTypeEnum;
import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.entity.WorkLogGather;
import com.ktp.project.util.NumberUtil;
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
import java.util.List;

@Repository
@Transactional
public class WorkLogGatherDao {

    private static Logger log = LoggerFactory.getLogger(WorkLogGatherDao.class);
    private static BigDecimal num = new BigDecimal("0");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<WorkLogGather> listProjectIsAuth() {
        String sql = "SELECT p.`id` AS projectId, pop.`user_id` AS userId, pop.`p_type` AS jobType " +
                "FROM project p " +
                "LEFT JOIN pro_organ po ON p.`id` = po.`pro_id` " +
                "LEFT JOIN `pro_organ_per` pop ON pop.`po_id` = po.`id` " +
                "WHERE p.p_create_type = 0 " +
                "AND pop.`p_type` IN (117, 180, 181) " +
                "GROUP BY projectId, userId";
        List<WorkLogGather> result = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogGather.class));
            result = sqlQuery.list();
        } catch (Exception e) {
            log.error("find all WorkLogGather failed", e);
            throw e;
        }
        return result;
    }

    public Double scoreByWorkLog(WorkLogGather workLogGather) {
        String sql ="SELECT " +
                "SUM(CASE wl.wl_type WHEN 1 THEN 1 ELSE 0 END) AS totalQualityLogNum, " +
                "SUM(CASE wl.wl_type WHEN 2 THEN 1 ELSE 0 END) AS totalSafeLogNum, " +
                "SUM(CASE wl.wl_type WHEN 3 THEN 1 ELSE 0 END) AS totalActionLogNum " +
                "FROM work_log wl " +
                "WHERE wl.pro_id = ? AND wl.`send_uid` = ? AND wl.in_time BETWEEN ? AND ?  AND wl.wl_type IN (1,2,3) ";

        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, workLogGather.getProjectId());
        sqlQuery.setParameter(1, workLogGather.getUserId());
        sqlQuery.setParameter(2, NumberUtil.getTheFirstDayOfLastMonth());
        sqlQuery.setParameter(3, NumberUtil.getTheFirstDayOfCurrentMonth());
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogGather.class));
        WorkLogGather result = (WorkLogGather)sqlQuery.uniqueResult();
        if (result != null){
            BigDecimal action = result.getTotalActionLogNum() == null ? num : result.getTotalActionLogNum();
            BigDecimal safe = result.getTotalSafeLogNum() == null ? num : result.getTotalSafeLogNum();
            BigDecimal quality = result.getTotalQualityLogNum() == null ? num : result.getTotalQualityLogNum();
            workLogGather.setTotalActionLogNum(action);
            workLogGather.setTotalQualityLogNum(quality);
            workLogGather.setTotalSafeLogNum(safe);
            return createActionScoreByJobType(workLogGather);
        }else {
            workLogGather.setTotalActionLogNum(num);
            workLogGather.setTotalSafeLogNum(num);
            workLogGather.setTotalQualityLogNum(num);
            return 0D;
        }
    }

    public Double createActionScoreByJobType(WorkLogGather bo) {//todo
        JobTypeEnum flag = JobTypeEnum.caseEnumByType(bo.getJobType());
        Double quality = 0D, safe = 0D;
        switch (flag) {
            case CONSTRUCT_WORKER:
                quality = bo.getTotalQualityLogNum().intValue() / 240D ;
                safe = bo.getTotalSafeLogNum().intValue() / 60D;
                break;
            case QUALITY_WORKER:
                quality = bo.getTotalQualityLogNum().intValue() / 150D;
                safe = bo.getTotalSafeLogNum().intValue() / 60D;
                break;
            case SAFETY_WORKER:
                safe = bo.getTotalSafeLogNum().intValue() / 150D;
                quality = safe;
                break;
            default:
                throw new RuntimeException("没有对应的枚举值!!!");
        }
        quality = quality >= 1D ? 1D : quality;
        safe = safe >= 1D ? 1D : safe;
        return countActionScore(quality, safe);

    }

    private Double countActionScore(Double... quality) {
        Double flag = 0D;
        for (Double aDouble : quality) {
            flag += aDouble;
        }
        flag = flag / quality.length;
        Double actionScore = 0D;
        if (flag.intValue() == 1){
            actionScore = 80D;
        }
        if (flag >= 0.6 && flag < 1){
            actionScore = 40D + (flag - 0.6) * 100;
        }
        if (flag >= 0.5 && flag < 0.6){
            actionScore = (flag - 0.5) * 400;
        }

        return NumberUtil.keepOneDecimalDouRoundOff(actionScore);
    }

    public void save(WorkLogGather saveEntry) {
        getCurrentSession().save(saveEntry);
    }

    public List<WorkLogGatherDto> listByCondition(WorkLogGather vo) {
        String sql = "SELECT w.id, w.`project_id` AS projectId, w.user_id AS userId, w.job_type AS jobType, w.total_quality_log_num AS totalQualityLogNum " +
                ", w.total_safe_log_num AS totalSafeLogNum, w.total_action_log_num AS totalActionLogNum, w.job_rate AS jobRate, w.rate_score AS rateScore, w.year " +
                ", w.month, w.create_time AS createTime, u.`u_realname` AS userName, u.`u_sex` AS userSex, u.`u_pic` AS headPhotoUrl " +
                "FROM work_log_gather w " +
                "LEFT JOIN user_info u ON w.`user_id` = u.`id` %s";
        List<Object> params = Lists.newArrayList();
        String queryHql = createQuerySql(vo, params);
        String baseSql = String.format(sql, queryHql);
        List<WorkLogGatherDto> list = null;
        try {
            SQLQuery query = getCurrentSession().createSQLQuery(baseSql);
            query.setResultTransformer(new AliasToBeanResultTransformer(WorkLogGatherDto.class));
            for (int i = 0; i < params.size(); i++) {
                query.setParameter(i, params.get(i));
            }
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("listByCondition WorkLogGatherDto failed", e);
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    public String createQuerySql(WorkLogGather vo, List<Object> params) {
        StringBuilder builder = new StringBuilder("where 1 = 1 ");
        if (vo != null){
            if (vo.getMonth() != null && vo.getMonth() > 0){
                builder.append("and w.month = ? ");
                params.add(vo.getMonth());
            }
            if (vo.getYear() != null && vo.getYear() > 0){
                builder.append("and w.year = ? ");
                params.add(vo.getYear());
            }
            if (vo.getProjectId() != null && vo.getProjectId() > 0){
                builder.append("and w.project_id = ? ");
                params.add(vo.getProjectId());
            }
            if (vo.getUserId() != null && vo.getUserId() > 0){
                builder.append("and w.user_id = ? ");
                params.add(vo.getUserId());
            }
        }
        return builder.toString();
    }

    public List<WorkLogGather> listByMore() {
        String sql = "SELECT p.`id` AS projectId, pop.`user_id` AS userId, pop.`p_type` AS jobType, SUM(CASE wl.wl_type  " +
                "WHEN 1 THEN 1  " +
                "ELSE 0  " +
                "END) AS totalQualityLogNum  " +
                ", SUM(CASE wl.wl_type  " +
                "WHEN 2 THEN 1  " +
                "ELSE 0  " +
                "END) AS totalSafeLogNum, SUM(CASE wl.wl_type  " +
                "WHEN 3 THEN 1  " +
                "ELSE 0  " +
                "END) AS totalActionLogNum  " +
//                ", DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y') AS year  " +
//                ", DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%c') AS month " +
                "FROM project p " +
                "LEFT JOIN pro_organ po ON p.`id` = po.`pro_id` " +
                "LEFT JOIN `pro_organ_per` pop ON pop.`po_id` = po.`id` " +
                "LEFT JOIN work_log wl ON pop.`user_id` = wl.`send_uid` " +
                "WHERE (p.p_create_type = 0  " +
                "AND pop.`p_type` IN (117, 180,181)  " +
                "AND wl.wl_type IN (1, 2, 3)" +
                "AND wl.in_time BETWEEN ? AND ?  " +
                ")  " +
                "GROUP BY projectId, userId ";
        List<WorkLogGather> result = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(WorkLogGather.class));
//            sqlQuery.setParameter(0, NumberUtil.getTheFirstDayOfLastMonth());
//            sqlQuery.setParameter(1, NumberUtil.getTheFirstDayOfCurrentMonth());
            sqlQuery.setParameter(0, "2018-02-01 00:00:00");
            sqlQuery.setParameter(1, "2018-03-01 00:00:00");
            result = sqlQuery.list();
        } catch (Exception e) {
            log.error("listByMore WorkLogGather failed", e);
            throw e;
        }
        return result;
    }

    public static void aa (Double... aa){
        int length = aa.length;
        System.out.println(length);
        double cc = 0D;
        for (int i = 0; i < aa.length; i++) {
            Double aDouble = aa[i];
            cc += aDouble;
        }
        System.out.println(cc);
    }

    public static void main(String[] args) {
        aa(11.11, 22.22);
    }
}
