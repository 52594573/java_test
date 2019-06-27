package com.ktp.project.dao;

import com.ktp.project.entity.ARStatistics;
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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Author: tangbin
 * @Date: 2018/12/17 13:44
 * @Version 1.0
 */
@Repository(value = "arStatisticsDao")
@Transactional
public class ARStatisticsDao {

    private static Logger log = LoggerFactory.getLogger(ARStatisticsDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    //查询项目出勤人数
    public int queryUserCount(int pro_id, String datetime){
        String sql = "select COUNT(*)  from  a_r_statistics  where pro_id=?  and  YEARWEEK(kl_time)=YEARWEEK(?) ";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, pro_id);
        sqlQuery.setParameter(1, datetime);
        BigInteger bigInteger = (BigInteger) sqlQuery.uniqueResult();
        int value = bigInteger.intValue();
        return value;
    }

    //查询总工时
    public List<ARStatistics> queryTineCount(int pro_id, String datetime){
        String sql = "select hours  from a_r_statistics where pro_id=? and YEARWEEK(kl_time)=YEARWEEK(?)";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ARStatistics.class));
        sqlQuery.setParameter(0, pro_id);
        sqlQuery.setParameter(1, datetime);
        return sqlQuery.list();
    }

    public Integer getDayCount(String sql,String endDate,Integer projectId) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,endDate);
        sqlQuery.setParameter(1,projectId);
        BigInteger bigInteger = (BigInteger) sqlQuery.uniqueResult();
        int value = bigInteger.intValue();
        return value;
    }

    public List<Integer> getDayWorkCount(String sqlWork,String endDate,Integer projectId) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlWork);
        sqlQuery.setParameter(0,endDate);
        sqlQuery.setParameter(1,projectId);
        return sqlQuery.list();
    }

    public List<String> getGZName(Integer proId) {
        String sql = "SELECT gz_name FROM a_r_statistics WHERE pro_id=? GROUP BY gz_name";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,proId);
        return sqlQuery.list();
    }

    public Integer getBanZuCount(String banZuCountSql,Integer projectId,String gzName,String endDate) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(banZuCountSql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setParameter(1,gzName);
        sqlQuery.setParameter(2,endDate);
        BigInteger bigInteger = (BigInteger) sqlQuery.uniqueResult();
        int value = bigInteger.intValue();
        return value;
    }

    public List<Integer> getBanZuCountWork(String banZuCountWorkSql,Integer projectId,String gzName,String endDate) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(banZuCountWorkSql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setParameter(1,gzName);
        sqlQuery.setParameter(2,endDate);
        return sqlQuery.list();
    }

    public List<ARStatistics> getAllARS(Integer projectId, String startDate) {
        String sql = "SELECT pro_id proId,gz_name gzName,DATE_FORMAT(kl_time,\"%Y-%m-%d\") klTimeStr,hours hours,gz_id gzId from a_r_statistics where kl_time=? and pro_id=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ARStatistics.class));
        sqlQuery.setParameter(0,startDate);
        sqlQuery.setParameter(1,projectId);
        return sqlQuery.list();
    }

    public List<Map<String, Object>> getAll(Integer projectId, String startDate, String endDate) {
        String sql = "SELECT\n" +
                "\taa.*, bb.klTime,\n" +
                "\tbb.hoursStr,bb.count\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tpo.id poId,\n" +
                "\t\t\tpo.`po_name`\n" +
                "\t\tFROM\n" +
                "\t\t\tpro_organ po\n" +
                "\t\tWHERE\n" +
                "\t\t\tpo.pro_id = ? " +
                ") aa\n" +
                "LEFT JOIN (\n" +
                "\tSELECT \n" +
                "\t\tcount(kl_time) count,\n" +
                "\t\tDATE_FORMAT(kl_time,'%Y-%m-%d') klTime,\n" +
                "\t\tGROUP_CONCAT(hours, '') hoursStr,\n" +
                "\t\tgz_id poId\n" +
                "\tFROM\n" +
                "\t\t`a_r_statistics`\n" +
                "\tWHERE\n" +
                "\t\tpro_id = ? AND kl_time BETWEEN ?  AND  ?  GROUP BY\n" +
                "\t\tkl_time,\n" +
                "\t\tgz_id\n" +
                ") bb ON aa.poId = bb.poId ";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setParameter(1,projectId);
        sqlQuery.setParameter(2,startDate);
        sqlQuery.setParameter(3,endDate);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }

    public List<Map<String, Object>> getWeek(Integer projectId, String startDate, String endDate) {
        String sql = "SELECT\n" +
                "\taa.*, bb.klTime,\n" +
                "\tbb.hoursStr,GROUP_CONCAT(bb.count) count\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tpo.id poId,\n" +
                "\t\t\tpo.`po_name`\n" +
                "\t\tFROM\n" +
                "\t\t\tpro_organ po\n" +
                "\t\tWHERE\n" +
                "\t\t\tpo.pro_id = ?\n" +
                "\t) aa\n" +
                "LEFT JOIN (\n" +
                "\tSELECT \n" +
                "\t\tcount(kl_time) count,\n" +
                "\t\tDATE_FORMAT(kl_time,'%Y-%m-%d') klTime,\n" +
                "\t\tGROUP_CONCAT(hours, '') hoursStr,\n" +
                "\t\tgz_id poId\n" +
                "\tFROM\n" +
                "\t\t`a_r_statistics`\n" +
                "\tWHERE\n" +
                "\t\tpro_id = ?\n" +
                "\tAND kl_time BETWEEN ?\n" +
                "\tAND ?\n" +
                "\tGROUP BY\n" +
                "\t\tkl_time,\n" +
                "\t\tgz_id\n" +
                ") bb ON aa.poId = bb.poId GROUP BY klTime";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setParameter(1,projectId);
        sqlQuery.setParameter(2,startDate);
        sqlQuery.setParameter(3,endDate);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }

    public List<Map<String, Object>> getBanZu(Integer projectId) {
        String sql = "SELECT po.po_name,GROUP_CONCAT(pop.po_id) counts from pro_organ_per pop LEFT JOIN pro_organ  po on po.id=pop.po_id" +
                " where po.pro_id=? GROUP BY pop.po_id";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }



    public List<Map<String, Object>> getAllCount(Integer projectId,String startDate,String endDate) {
        String  sql = "SELECT GROUP_CONCAT(hours) hours,GROUP_CONCAT(gz_id) counts,DATE_FORMAT(kl_time,'%Y-%m-%d') times from a_r_statistics where pro_id=? and kl_time BETWEEN ? AND ? GROUP BY kl_time";
        //String sql = "SELECT GROUP_CONCAT(kl_fenzhong) timecount,count(*) workcount from v_kaoqin_list? where " +
        //        " YEARWEEK(kl_date, '%Y-%m-%d') = YEARWEEK(?, '%Y-%m-%d') GROUP BY kl_date";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setParameter(1,startDate);
        sqlQuery.setParameter(2,endDate);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }

    public List<Map<String, Object>> getBanzuMin(Integer projectId) {
        String sql = "SELECT po.po_name,po_id from pro_organ_per pop LEFT JOIN pro_organ  po on po.id=pop.po_id where po.pro_id=?  GROUP BY po_id";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }

    public List<Map<String, Object>> getALLProId(Integer projectid,Integer poId, String startDate) {
        String sql = "SELECT\n" +
                "\tGROUP_CONCAT(kl_fenzhong) hours,\n" +
                "\tGROUP_CONCAT(po_id) counts,\n" +
                "\tkl_date,\n" +
                "\tpo_name\n" +
                "FROM\n" +
                "\tv_kaoqin_list?\n" +
                "WHERE\n" +
                "\tYEARWEEK(kl_date, '%Y-%m-%d') = YEARWEEK(?, '%Y-%m-%d')\n" +
                "AND po_id = ?\n" +
                "GROUP BY\n" +
                "\tkl_date,\n" +
                "\tpo_name";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectid);
        sqlQuery.setParameter(1,startDate);
        sqlQuery.setParameter(2,poId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }

    public int getProjectCount(Integer projectId) {
        String sql = "SELECT count(*) from pro_organ_per pop LEFT JOIN pro_organ  po on po.id=pop.po_id WHERE po.pro_id=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0,projectId);
        BigInteger bigInteger = (BigInteger) sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }
}
