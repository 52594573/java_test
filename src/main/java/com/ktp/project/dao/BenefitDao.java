package com.ktp.project.dao;

import com.ktp.project.dto.*;
import com.ktp.project.dto.BenefitDto.CommentAndApply;
import com.ktp.project.entity.BenefitActivity;
import com.ktp.project.entity.BenefitDonate;
import com.ktp.project.entity.BenefitEvaluate;
import com.ktp.project.entity.BenefitRecipient;
import com.ktp.project.po.DonateActSearchPojo;
import com.ktp.project.util.PageUtil;
import com.zm.entity.UserInfo;
import org.apache.http.util.TextUtils;
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
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 公益
 *
 * @Author: liaosh
 * @Date: 2018/8/22 0022
 */
@Repository
@Transactional
public class BenefitDao {
    private static final Logger log = LoggerFactory.getLogger("DataBaseDao");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List getBenefitList(int page, int pageSize, int actStatus) {
        try {
            String queryString = "from BenefitActivity ba where ba.actStatus = ? order by ba.actETime asc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, actStatus);
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all benefit_act failed", re);
            throw re;
        }
    }

    /**
     * 获取单个活动信息
     */
    public BenefitActivity getBenefit(int actId) {
        try {
            String queryString = "from BenefitActivity ba where ba.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, actId);
            /*String sql = "select bd.don_user_id donUserId, bd.id donId,bd.don_status donStatus,bd.don_inventory donInventory,ui.u_nicheng donName,bd.don_prince donPrince,bd.don_postage donPostage,bd.don_way donWay,bd.don_percent donPercent," +
                    " bd.don_unit donUnit,bd.don_describe donDescribe,bd.don_apply_sum donApplySum,bd.don_comment_sum donCommentSum,bd.don_picture donPicture,ui.u_pic donHead,bd.don_address donAddress,ba.act_status actStatus,bd.don_reject_reason donRejectReason " +
                    " from benefit_donate bd inner join user_info ui on ui.id = bd.don_user_id " +
                    " inner join benefit_act ba on bd.don_act_id = ba.id" +
                    " where bd.id = ? ";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(BenefitActivity.class));
            queryObject.setParameter(0, actId);*/
            return (BenefitActivity) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取单个活动信息", re);
            throw re;
        }
    }

    public List<DonateListDto> queryDonateList(DonateActSearchPojo searchPojo, List<String> goos_sort) {
        searchPojo.setDonAddress(searchPojo.getDonAddress().trim());
        boolean goos_sortList = goos_sort != null && goos_sort.size() > 0 && goos_sort.get(0) != "";
        boolean donAddressBool = !TextUtils.isEmpty(searchPojo.getDonAddress());
        boolean donDescribeBool = !TextUtils.isEmpty(searchPojo.getDonNaneOrDescribe());
        boolean donWayBool = !TextUtils.isEmpty(searchPojo.getDonWay());

        try {
            String sql = "select " +
                    "bd.don_user_id userId, " +
                    "bd.don_act_id actId, " +
                    "bd.id donId," +
                    "bd.don_describe donDescribe," +
                    "bd.don_percent donPercent," +
                    "bd.don_picture donPicture," +
                    "bd.don_prince donPrince," +
                    "bd.don_way donWay," +
                    "bd.don_postage donPostage," +
                    "bd.don_inventory donInventory," +
                    "bd.don_unit donUnit," +
                    "ui.u_pic donHead," +
                    "ui.u_nicheng donName," +
                    "ui.u_sex userSex," +
                    "bd.don_address donAddress " +
                    "from user_info ui inner join benefit_donate bd on ui.id = bd.don_user_id where 1=1 " +

                    (goos_sortList ? " and bd.don_goods_sort in  (:goosorts)" : "") +
                    (donAddressBool ? " and bd.don_address like :donAddress " : "") +
                    (donDescribeBool ? " and  (ui.u_nicheng like :donDescribe or bd.don_describe like :donDescribe) " : "") +
                    (donWayBool ? " and (bd.don_way like :donWay  or bd.don_postage like :donWay)" : "") +
                    " and bd.don_act_id = :actId and bd.don_status = 1 " +
                    " group by bd.id order by bd.don_time desc";

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateListDto.class));
            if (goos_sortList) {
                sqlQuery.setParameterList("goosorts", goos_sort);
            }
            if (donAddressBool) {
                sqlQuery.setParameter("donAddress", "%" + searchPojo.getDonAddress() + "%");
            }
            if (donDescribeBool) {
                sqlQuery.setParameter("donDescribe", "%" + searchPojo.getDonNaneOrDescribe() + "%");
            }
            if (donWayBool) {
                sqlQuery.setParameter("donWay", "%" + searchPojo.getDonWay() + "%");
            }
            sqlQuery.setParameter("actId", searchPojo.getId());
            //if (!goos_sortList && !donAddressBool && !donDescribeBool && !donWayBool) {
            sqlQuery.setFirstResult(searchPojo.getPage());
            sqlQuery.setMaxResults(searchPojo.getPageSize());
            // }

            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    /**
     * 参加活动次数
     */
    public Long queryDonatejoinSum(/*int id,*/int userId) {
        try {
            String queryString = "select count(*) from BenefitDonate bd  where bd.donUserId = ? and bd.donStatus = 1";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("参加活动次数", re);
            throw re;
        }
    }

    /**
     * 捐赠成功数
     */
    public long queryDonatedonSum(int userId) {
        try {
            String queryString = "select count(1) from   benefit_donate bd inner JOIN benefit_rec br on br.rec_don_id = bd.id  where bd.don_user_id = ? and br.rec_status > 0";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter(0, userId);
            BigInteger bigInteger = (BigInteger) queryObject.uniqueResult();
            return bigInteger.intValue();

        } catch (RuntimeException re) {
            log.error("参加活动次数", re);
            throw re;
        }
    }

    /**
     * 该发布总成功捐赠数量
     */
    public int queryDonateAllSumById(int donId) {
        try {
            String queryString = "select IFNULL(sum(br.rec_actual_sum),0) from   benefit_rec br where br.rec_don_id = ? and br.rec_status > 1";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter(0, donId);
            BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
            return bigDecimal.intValue();

        } catch (RuntimeException re) {
            log.error("该发布总成功捐赠数量", re);
            throw re;
        }
    }

    /**
     * 通过id查状态
     */
    public int queryDonateStatusById(int id) {
        try {
            String queryString = "select IFNULL(sum(bd.don_status),0) from   benefit_donate bd where bd.id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter(0, id);
            BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
            return bigDecimal.intValue();

        } catch (RuntimeException re) {
            log.error("通过id查状态", re);
            throw re;
        }
    }


    /**
     * 捐赠剩余数量
     */
    public int getDonateInventoryById(int id) {
        try {
            String queryString = "select IFNULL(sum(bd.don_inventory),0) from   benefit_donate bd where bd.id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter(0, id);
            BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
            return bigDecimal.intValue();

        } catch (RuntimeException re) {
            log.error("该发布总成功捐赠数量", re);
            throw re;
        }
    }

    /**
     * 活动剩余数量
     */
    public int getActivityInventoryById(int actId) {
        try {
            String queryString = "select IFNULL(sum(bd.don_inventory),0) from   benefit_donate bd where bd.don_act_id = ? and bd.don_status = 1";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
            queryObject.setParameter(0, actId);
            BigDecimal bigDecimal = (BigDecimal) queryObject.uniqueResult();
            return bigDecimal.intValue();

        } catch (RuntimeException re) {
            log.error("该发布总成功捐赠数量", re);
            throw re;
        }
    }

    /**
     * 通过捐赠id获取活动id
     */
    public int getActIdByDonateId(int recDonId) {
        try {
            String queryString = "select bd.donActId from BenefitDonate bd where bd.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, recDonId);
            return (int) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("通过捐赠id获取活动id", re);
            throw re;
        }
    }

    public List<DonateSeccssListDto> getdonateFinishList50ByActId(int actId) {
        try {
            //order by  rand() limit 50数据量大于10万需要优化
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum," +
                    " bd.don_unit donUnit ,be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime ," +
                    "ui1.u_sex donSex,ui2.u_sex recSex ,bd.id donId,br.id recId,ba.id actId " +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 1  and ba.id = ? order by  rand() limit 50";

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, actId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public List<DonateSeccssListDto> queryRecipientListByDonId(int donId) {
        try {
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum," +
                    " bd.don_unit donUnit ,be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime ," +
                    "ui1.u_sex donSex,ui2.u_sex recSex ,bd.id donId,br.id recId,ba.id actId " +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 2  and bd.id = ? order by  br.rec_deal_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, donId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public List<DonateSeccssListDto> getdonateFinishListByActId(int page, int pageSize, int actId) {
        try {
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum," +
                    " bd.don_unit donUnit ,be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime ," +
                    "ui1.u_sex donSex,ui2.u_sex recSex ,bd.id donId,br.id recId,ba.id actId,bd.don_way donWay,bd.don_postage donPostage,bd.don_picture donPicture,bd.don_describe donDescribe " +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 1 and ba.id = ? order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, actId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    //通过活动id获取该惠东捐赠成功详情（不分页）有图片5张
    public List<DonateSeccssListDto> getdonateFinishListByActIdPicture(int actId) {
        try {
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum,bd.don_unit donUnit ,be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime " +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 1 and ba.id = ? and be.eva_picture is not null and be.eva_picture != '' order by br.rec_time desc limit 5";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, actId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


    //通过活动id获取该惠东捐赠成功详情（不分页）
    public List<DonateSeccssListDto> getdonateFinishListByActId(int actId) {
        try {
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum,bd.don_unit donUnit ,be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime " +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 1 and ba.id = ? order by br.rec_time desc limit 5";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, actId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


    /**
     * 更新捐赠信息
     *
     * @param: donate
     * @return:
     */
    public boolean updateDonate(BenefitDonate donate) {
        try {
            getCurrentSession().update(donate);
            log.debug("update donate successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update donate failed", re);
        }
        return false;
    }

    /**
     * 物品类别,捐赠方式，捐赠邮费
     *
     * @return
     */
    public List queryBenefitForJob() {
        try {
            String queryString = "from KeyContent kc where kc.keyId in (18,19,20,21) order by kc.id";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all KeyContent failed", re);
            throw re;
        }
    }

    /**
     * 插入捐赠信息
     *
     * @param: donate
     * @return:
     */
    public boolean insertDonate(BenefitDonate donate) {
        try {
            getCurrentSession().saveOrUpdate(donate);
            log.debug("提交 donate successful");
            return true;
        } catch (RuntimeException re) {
            log.error("提交 donate failed", re);
        }
        return false;
    }

    /**
     * 活动的数量增加
     *
     * @param: donate
     * @return:
     */
    public int addActInventorySum(int actId, int sum) {
        try {
            String hql = "update BenefitActivity ba set ba.actInventorySum = ba.actInventorySum + ? where ba.id = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setInteger(0, sum);
            query.setParameter(1, actId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("活动的数量增加", re);
            return 0;
        }
    }

    /**
     * 活动表的受赠人数+1
     *
     * @param: donate
     * @return:
     */
    public int updateDonateActRecSum(int actId) {
        try {
            String hql = "update BenefitActivity ba set ba.actRecipientSum = ba.actRecipientSum + 1 where ba.id = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, actId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("活动表的受赠人数+1", re);
            return 0;
        }
    }

    /**
     * 改变donate状态
     *
     * @param:
     * @return:
     */
    public int updateDonateStatusById(int donId, int donStatus, int oldStatus) {
        try {
            String hql = "update BenefitDonate bd set bd.donStatus = ? where bd.id = ? and bd.donStatus = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setInteger(0, donStatus);
            query.setParameter(1, donId);
            query.setParameter(2, oldStatus);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("改变donate状态", re);
            return 0;
        }
    }

    /**
     * 获取个人捐赠列表
     */
    public List<BenefitDonate> getMyDonateList(int userId, int page, int pageSize) {
        try {

            String sql = "select bd.id id,bd.don_act_id actId,bd.don_apply_sum donApplySum,bd.don_comment_sum donCommentSum,bd.don_describe donDescribe,bd.don_inventory donInventory," +
                    " bd.don_picture donPicture,bd.don_status donStatus,bd.don_unit donUnit,bd.don_user_id donUserId, ba.act_top actTop,bd.id donId" +
                    " from benefit_donate bd inner join benefit_act ba on bd.don_act_id = ba.id " +
                    " where bd.don_user_id = ? order by bd.don_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(BenefitDonate.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();

        } catch (RuntimeException re) {
            log.error("获取个人捐赠列表", re);
            throw re;
        }
    }

    /**
     * 获取个人捐赠列表
     */
    public List<BenefitDonate> getMyDonateListByActId(int userId, int actId, int page, int pageSize) {
        try {

            String sql = "select bd.id id,bd.don_act_id actId,bd.don_apply_sum donApplySum,bd.don_comment_sum donCommentSum,bd.don_describe donDescribe,bd.don_inventory donInventory," +
                    " bd.don_picture donPicture,bd.don_status donStatus,bd.don_unit donUnit,bd.don_user_id donUserId, ba.act_top actTop " +
                    " from benefit_donate bd inner join benefit_act ba on bd.don_act_id = ba.id " +
                    " where bd.don_user_id = ? and bd.don_act_id = ? order by bd.don_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(BenefitDonate.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setParameter(1, actId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();

        } catch (RuntimeException re) {
            log.error("获取个人捐赠列表", re);
            throw re;
        }
    }

    /**
     * 物品详情-捐赠者
     */
    public DonateDetailDto myDonateDetail(int id) {
        try {
            String sql = "select bd.don_user_id donUserId, bd.id donId,bd.don_status donStatus,bd.don_inventory donInventory,ui.u_nicheng donName,bd.don_prince donPrince,bd.don_postage donPostage,bd.don_way donWay,bd.don_percent donPercent," +
                    " bd.don_unit donUnit,bd.don_describe donDescribe,bd.don_apply_sum donApplySum,bd.don_comment_sum donCommentSum,bd.don_picture donPicture,ui.u_pic donHead,bd.don_address donAddress,ba.act_status actStatus,bd.don_reject_reason donRejectReason " +
                    " from benefit_donate bd inner join user_info ui on ui.id = bd.don_user_id " +
                    " inner join benefit_act ba on bd.don_act_id = ba.id" +
                    " where bd.id = ? ";

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(DonateDetailDto.class));
            queryObject.setParameter(0, id);
            return (DonateDetailDto) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("物品详情-捐赠者", re);
            throw re;
        }
    }

    /**
     * 申请数变为0
     */
    public int updateDonateApplySum(int sum, int userId) {
        try {
            String hql = "update BenefitDonate bd set bd.donApplySum = ? where bd.donUserId = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, sum);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitDonate)", re);
            throw re;
        }
    }

    /**
     * 申请数量+1
     */
    public int updateDonateApplySumByDonId(int sum, int donId) {
        try {
            String hql = "update BenefitDonate bd set bd.donApplySum = bd.donApplySum + ? where bd.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, sum);
            query.setParameter(1, donId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("申请数量+1", re);
            throw re;
        }
    }

    /**
     * 个人中心-申请列表
     */
    public List<DonateApplyDetailDto> myDonateApplyDetailList(int userId, int recStatus, int page, int pageSize, int donId) {
        try {
            String sql = "select bd.id donId,br.id recId,ba.act_status actStatus,bd.don_inventory donInventory,ui.u_nicheng recName,br.rec_sum recSum,DATE_FORMAT(br.rec_time,'%Y-%c-%d %H:%i:%s') recTime,bd.don_unit donUnit,bd.don_describe donDescribe,ui.u_pic recHead,br.rec_address recAddress," +
                    " bd.don_comment_sum donCommentSum,br.rec_tel recTel,ui.u_star recStar,ui.u_cert recCert,br.rec_way recWay,be.eva_describe evaDescribe,be.eva_picture evaPicture,br.rec_status recStatus,case br.rec_is_del when 0 then '内容违规' when 1 then br.rec_reason end recReason,br.rec_user_id recUserId,bd.don_status donStatus" +
                    " from benefit_donate bd inner join benefit_rec br on bd.id = br.rec_don_id inner join benefit_act ba on bd.don_act_id=ba.id inner join user_info ui on br.rec_user_id=ui.id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " where bd.don_user_id = ? and br.rec_status = ? and bd.id = ? order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateApplyDetailDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setParameter(1, recStatus);
            sqlQuery.setParameter(2, donId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("获取数据异常", re);
            throw re;
        }
    }

    /**
     * 个人中心-申请列表
     */
    public List<DonateApplyDetailDto> myDonateFinishDetailList(int userId, int recStatus, int page, int pageSize, int donId) {
        try {
            String sql = "select bd.id donId,br.id recId,ba.act_status actStatus,bd.don_inventory donInventory,ui.u_nicheng recName,br.rec_sum recSum,DATE_FORMAT(br.rec_time,'%Y-%c-%d %H:%i:%s') recTime,bd.don_unit donUnit,bd.don_describe donDescribe,ui.u_pic recHead,br.rec_address recAddress," +

                    " bd.don_comment_sum donCommentSum,br.rec_tel recTel,ui.u_star recStar,ui.u_cert recCert,br.rec_way recWay,be.eva_describe evaDescribe,be.eva_picture evaPicture,br.rec_status recStatus,case br.rec_is_del when 0 then '内容违规' when 1 then br.rec_reason end recReason,br.rec_actual_sum recActualSum,br.rec_user_id recUserId,bd.don_status donStatus,br.rec_consignee recConsignee" +
                    " from benefit_donate bd inner join benefit_rec br on bd.id = br.rec_don_id inner join benefit_act ba on bd.don_act_id=ba.id inner join user_info ui on br.rec_user_id=ui.id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " where bd.don_user_id = ? and br.rec_status > 0 and bd.id = ? order by br.rec_deal_time desc";

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateApplyDetailDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setParameter(1, donId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("获取数据异常", re);
            throw re;
        }
    }

    /**
     * 捐赠分享
     */
    public List<DonateApplyDetailDto> myDonateFinishDetailList(int donId) {
        try {
            String sql = "select bd.id donId,br.id recId,ba.act_status actStatus,bd.don_inventory donInventory,ui.u_nicheng recName,br.rec_sum recSum,DATE_FORMAT(br.rec_time,'%Y-%c-%d %H:%i:%s') recTime,bd.don_unit donUnit,bd.don_describe donDescribe,ui.u_pic recHead,br.rec_address recAddress," +
                    " bd.don_comment_sum donCommentSum,br.rec_tel recTel,br.rec_reason recReason,ui.u_star recStar,ui.u_cert recCert,br.rec_way recWay,be.eva_describe evaDescribe,be.eva_picture evaPicture,br.rec_status recStatus,br.rec_actual_sum recActualSum,br.rec_user_id recUserId,bd.don_status donStatus" +
                    " from benefit_donate bd inner join benefit_rec br on bd.id = br.rec_don_id inner join benefit_act ba on bd.don_act_id=ba.id inner join user_info ui on br.rec_user_id=ui.id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " where br.rec_status > 2 and bd.id = ? order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateApplyDetailDto.class));
            sqlQuery.setParameter(0, donId);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("获取数据异常", re);
            throw re;
        }
    }

    /**
     * 个人中心-申请列表-批量或者单个捐赠(BenefitRecipient)
     */
    public int updateRecipentConsent(BenefitRecipient recipient) {
        try {
            String hql = "update BenefitRecipient br set br.recActualSum = ? , br.recStatus = ? , br.recDealTime = ? where br.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, recipient.getRecActualSum());
            query.setParameter(1, 1);
            query.setParameter(2, recipient.getRecDealTime());
            query.setParameter(3, recipient.getId());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("更新updateRecipent异常", re);
            throw re;
        }
    }

    /**
     * 活动剩余量
     */
    public int updateDonateActInvByActId(int sum, int actId) {
        try {
            String hql = "update BenefitActivity ba set ba.actInventorySum =  ba.actInventorySum - ? where ba.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, sum);
            query.setParameter(1, actId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("更新updateRecipent异常", re);
            throw re;
        }
    }

    /**
     * 个人中心-申请列表-批量或者单个捐赠(BenefitDonate)
     */
    public int updateDonateConsent(BenefitRecipient recipient) {
        try {
            String hql = "update BenefitDonate bd set bd.donInventory = bd.donInventory - ? where bd.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, recipient.getRecActualSum());
            query.setParameter(1, recipient.getRecDonId());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("BenefitDonate", re);
            throw re;
        }
    }

    /**
     * 个人中心-物品详情-撤回BenefitRecipient
     */
    public int BenefitRecipientUpdateStatusAndrecActualSum(BenefitRecipient recipient) {
        try {
            String hql = "update BenefitRecipient br set br.recStatus = ?,br.recActualSum = 0 where br.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, recipient.getRecStatus());
            query.setParameter(1, recipient.getId());

            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitRecipient)", re);
            throw re;
        }
    }

    /**
     * 个人中心-物品详情-确认收到BenefitRecipient
     */
    public int BenefitRecipientUpdateStatus(int status, int recId) {
        try {
            String hql = "update BenefitRecipient br set br.recStatus = ? where br.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, status);
            query.setParameter(1, recId);
            //query.setParameter(2, recipient.getRecUserId());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitRecipient)", re);
            throw re;
        }
    }

    /**
     * 个人中心-物品详情-确认收到BenefitRecipient
     */
    public int BenefitRecipientUpdateStatusAndGetTime(int status, int recId, Date recGetTime) {
        try {
            String hql = "update BenefitRecipient br set br.recStatus = ?,br.recGetTime = ? where br.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, status);
            query.setParameter(1, recGetTime);
            query.setParameter(2, recId);
            //query.setParameter(2, recipient.getRecUserId());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitRecipient)", re);
            throw re;
        }
    }

    /**
     * 通过BenefitRecipient的id返回BenefitRecipient
     */
    public BenefitRecipient BenefitRecipientById(int id) {
        try {
            String queryString = "from BenefitRecipient br where br.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, id);
            return (BenefitRecipient) queryObject.uniqueResult();

        } catch (RuntimeException re) {
            log.error("查找BenefitRecipient异常", re);
            throw re;
        }
    }

    /**
     * 更具id找donate
     */
    public BenefitDonate getDonateById(int id) {
        try {
            String queryString = "from BenefitDonate bd where bd.id = ? ";//and bd.donStatus = 0
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, id);
            return (BenefitDonate) queryObject.uniqueResult();

        } catch (RuntimeException re) {
            log.error("查找BenefitRecipient异常", re);
            throw re;
        }
    }


    /**
     * 个人中心-物品详情-撤回BenefitDonate-库存回退
     */
    public int BenefitDonateDisposereCall(BenefitRecipient recipient) {
        try {
            String hql = "update BenefitDonate bd set bd.donInventory = bd.donInventory + ? where bd.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, recipient.getRecActualSum());
            query.setParameter(1, recipient.getRecDonId());
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitDonate)", re);
            throw re;
        }
    }

    /**
     * 通过活动id，添加剩余数量
     */
    public int updateActInventySum(int actId, int actualSum) {
        try {
            String hql = "update BenefitActivity ba set ba.actInventorySum = ba.actInventorySum + ? where ba.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, actualSum);
            query.setParameter(1, actId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitDonate)", re);
            throw re;
        }
    }

    public void BenefitEvaluateSaveOrUpdate(BenefitEvaluate evaluate) {
        try {
            getCurrentSession().saveOrUpdate(evaluate);
        } catch (RuntimeException re) {
            log.error("评论异常", re);
            throw re;
        }
    }

    /**
     * 评论数量变
     */
    public int updateDonateCommentSumId(int id) {
        try {
            String hql = "update BenefitDonate bd set bd.donCommentSum = bd.donCommentSum + 1 where bd.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitDonate)", re);
            throw re;
        }
    }

    /**
     * 评论数量变为指定数
     */
    public int updateDonateCommentSumByUserId(int sum, int userId) {
        try {
            String hql = "update BenefitDonate bd set bd.donCommentSum = ? where bd.donUserId = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, sum);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("人中心-物品详情-撤回异常(BenefitDonate)", re);
            throw re;
        }
    }

    public List<MyRecipientListDto> getRecipientList(int page, int pageSize, int userId) {
        try {
            String sql = "select ba.act_top actTop,ba.act_status actStatus,br.rec_status recStatus,bd.don_unit donUnit ,bd.don_describe donDescribe,br.rec_sum recSum,bd.don_picture donPicture,br.id recId,bd.don_user_id donUserId,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s') recDealTime,br.rec_actual_sum recActualSum,ba.id actId" +
                    " from benefit_act ba inner join benefit_donate bd on ba.id = bd.don_act_id inner join benefit_rec br on bd.id = br.rec_don_id" +
                    " where br.rec_user_id = ? and br.rec_status > -1 order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(MyRecipientListDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public List<MyRecipientListDto> getRecipientListByActId(int page, int pageSize, int userId, int actId) {
        try {
            String sql = "select ba.act_top actTop,ba.act_status actStatus,br.rec_status recStatus,bd.don_unit donUnit ,bd.don_describe donDescribe,br.rec_sum recSum,bd.don_picture donPicture,br.id recId,bd.don_user_id donUserId,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s') recDealTime,br.rec_actual_sum recActualSum,ba.id actId" +
                    " from benefit_act ba inner join benefit_donate bd on ba.id = bd.don_act_id inner join benefit_rec br on bd.id = br.rec_don_id" +
                    " where br.rec_user_id = ? and ba.id = ? and br.rec_status > -1 order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(MyRecipientListDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setParameter(1, actId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


    public List<DonateSeccssListDto> getMyReceiveListByUserId(int page, int pageSize, int userId) {
        try {
            String sql = "SELECT " +
                    "ui1.u_pic donHead," +
                    "ui2.u_pic recHead," +
                    "ba.act_top actTop," +
                    "ba.act_status actStatus," +
                    "br.rec_actual_sum recActualSum," +
                    "br.rec_sum recSum," +
                    "bd.don_unit donUnit," +
                    "bd.don_describe donDescribe," +
                    "bd.don_picture donPicture," +
                    "ui1.u_nicheng donName," +
                    "ui2.u_nicheng recName," +
                    "DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s') recDealTime," +
                    "br.id recId ," +
                    "br.rec_address recAddress," +
                    "br.rec_way recWay," +
                    "ui1.u_name donTel" +
                    " FROM " +
                    "benefit_act ba " +
                    "LEFT JOIN benefit_donate bd ON ba.id = bd.don_act_id " +
                    "LEFT JOIN benefit_rec br ON bd.id = br.rec_don_id " +
                    "LEFT JOIN user_info ui1 ON bd.don_user_id = ui1.id " +
                    "LEFT JOIN user_info ui2 ON br.rec_user_id = ui2.id " +
                    " WHERE" +
                    " br.rec_user_id = ?";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, userId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public DonateSeccssListDto myReceiveDetail(int id) {
        try {
            String sql = "SELECT " +
                    "ui1.u_pic donHead," +
                    "ui2.u_pic recHead," +
                    "ba.act_top actTop," +
                    "ba.act_status actStatus," +
                    "ba.id actId," +
                    "br.rec_actual_sum recActualSum," +
                    "br.rec_sum recSum," +
                    "bd.don_unit donUnit," +
                    "bd.don_describe donDescribe," +
                    "bd.don_picture donPicture," +
                    "ui1.u_nicheng donName," +
                    "br.rec_consignee recName," +
                    "DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s') recDealTime," +
                    "br.id recId ," +
                    "br.rec_address recAddress," +
                    "br.rec_way recWay," +
                    "ui1.u_name donTel," +
                    "br.rec_tel recTel," +
                    "be.eva_describe evaDescribe," +
                    "be.eva_picture evaPicture," +
                    "ui1.u_sex donSex," +
                    "ui2.u_sex recSex," +
                    "bd.don_way donWay," +
                    "bd.don_postage donPostage," +
                    "br.rec_status recStatus," +
                    "DATE_FORMAT(br.rec_get_time,'%Y-%c-%d %H:%i:%s') recGetTime," +
                    "ui1.id donUserId" +
                    " FROM " +
                    "benefit_act ba " +
                    "LEFT JOIN benefit_donate bd ON ba.id = bd.don_act_id " +
                    "LEFT JOIN benefit_rec br ON bd.id = br.rec_don_id " +
                    "LEFT JOIN user_info ui1 ON bd.don_user_id = ui1.id " +
                    "LEFT JOIN user_info ui2 ON br.rec_user_id = ui2.id " +
                    "LEFT JOIN benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1) " +
                    " WHERE" +
                    " br.id = ? order by be.eva_time desc limit 1 ";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            queryObject.setParameter(0, id);
            return (DonateSeccssListDto) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    /**
     * 获取个人受赠列表
     */
    public List<BenefitRecipient> getmyRecipientList(int userId, int page, int pageSize) {
        try {
            String queryString = "from BenefitRecipient br where br.recUserId = ? order by br.recTime desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all BenefitRecipient failed", re);
            throw re;
        }
    }

    /**
     * 申请领取（获取捐赠表信息）
     */
    public BenefitDonate getapplyDonate(int id) {
        try {
            BenefitDonate benefitDonate = (BenefitDonate) getCurrentSession().get(BenefitDonate.class, id);
            UserInfo userInfo = (UserInfo) getCurrentSession().get(UserInfo.class, benefitDonate.getDonUserId());
            if (userInfo == null || userInfo.equals("")) {
                log.error("捐赠者信息获取异常");
            }
            benefitDonate.setDonName(userInfo.getU_realname());
            benefitDonate.setDonHearPicture(userInfo.getU_pic());
            return benefitDonate;
        } catch (RuntimeException re) {
            log.error("find all benefit_rec failed", re);
            throw re;
        }
    }

    /**
     * 插入捐赠信息
     *
     * @param: donate
     * @return:
     */
    public boolean InsertRecipient(BenefitRecipient recipient) {
        try {
            getCurrentSession().save(recipient);


            log.debug("提交 recipient successful");
            return true;
        } catch (RuntimeException re) {
            log.error("提交 recipient failed", re);
        }
        return false;
    }

    /**
     *个人受赠详情
     */
    /*public BenefitRecipient getMyReceiveDetail(int id){
        try {
            BenefitRecipient recipient = (BenefitRecipient) getCurrentSession().get(BenefitRecipient.class,id);
            //捐赠表信息
            BenefitDonate donate = (BenefitDonate) getCurrentSession().get(BenefitDonate.class,recipient.getRecDonId());
            if(donate==null || donate.equals("")){
                log.error("捐赠者信息获取异常");
            }

            //活动主题
            String queryString = "select ba.actTop  from BenefitActivity ba where ba.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, recipient.getRecDonId());
            queryObject.
            if(userInfo==null || userInfo.equals("")){
                log.error("捐赠者信息获取异常");
            }
            benefitDonate.setDonName(userInfo.getU_realname());
            benefitDonate.setDonHearPicture(userInfo.getU_pic());
            return benefitDonate;
        } catch (RuntimeException re) {
            log.error("find all benefit_rec failed", re);
            throw re;
        }
    }*/

    /**
     * 进入编辑
     */
    public int joinCompile(int status, int donId) {
        try {
            String hql = "update BenefitDonate bd set bd.donStatus = ? where bd.id = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, status);
            query.setParameter(1, donId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("进入编辑异常", re);
            throw re;
        }
    }

    /**
     * 到时10分钟关闭编辑状态
     */
    @Transactional
    public int updateDonateCompileToRun(int status, int donId) {
        try {
            String hql = "update BenefitDonate bd set bd.donStatus = 1 where bd.id = ? and bd.donStatus = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, donId);
            query.setParameter(1, status);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("定时器倒计时异常", re);
            throw re;
        }
    }

    /*------------------------------------------H5------------------------------------*/

    /**
     * 分享活动
     */
    @Transactional
    public BenefitActivity getActById(int actId) {
        try {
            String queryString = "from BenefitActivity ba where ba.id = ?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, actId);
            return (BenefitActivity) queryObject.uniqueResult();

        } catch (RuntimeException re) {
            log.error("查找BenefitRecipient异常", re);
            throw re;
        }
    }

    //捐赠成功列表
    public List getdonateListByActId(int actId) {
        try {
            String queryString = "from BenefitDonate bd where bd.donStatus = 1 and bd.donActId = ? order by bd.donTime desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, actId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all benefit_act failed", re);
            throw re;
        }
    }


    /**
     * hjl
     * 查询评论列表通过，捐赠记录表
     *
     * @param recid
     * @return
     */
    public List<BenefitEvaluate> getEvaByRecId(int recid) {
        try {
            String sql = "select ev.id id,ev.eva_rec_id evaRecId,ev.eva_describe evaDescribe,ev.eva_picture evaPicture," +
                    "ev.eva_time evaTime,ev.eva_is_del evaIsDel  from benefit_eva ev where ev.eva_is_del = 1 and ev.eva_rec_id = ? order by ev.eva_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(BenefitEvaluate.class));
            sqlQuery.setParameter(0, recid);
            sqlQuery.setFirstResult(0);
            sqlQuery.setMaxResults(10);
            return (List<BenefitEvaluate>) sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("find all benefit_act failed", re);
            throw re;
        }
    }


    //通过捐赠id获取该捐赠成捐赠列表
    public List<DonateSeccssListDto> getdonRecDetail(int page, int pageSize, int donId) {
        try {
            String sql = "select ui1.u_pic donHead,ui2.u_pic recHead,ba.act_top actTop,ba.act_status actStatus,br.rec_actual_sum recActualSum,bd.don_unit donUnit ,bd.don_way donWay,bd.don_postage donPostage,bd.don_picture donPicture,bd.don_describe donDescribe, " +
                    "be.eva_picture evaPicture,ui1.u_nicheng donName,ui2.u_nicheng recName,be.eva_describe evaDescribe,DATE_FORMAT(br.rec_deal_time,'%Y-%c-%d %H:%i:%s')  recDealTime,ui1.u_sex donSex,ui2.u_sex recSex ,bd.id donId,br.id recId,ba.id actId" +
                    " from benefit_act ba left join benefit_donate bd on ba.id = bd.don_act_id left join benefit_rec br on bd.id = br.rec_don_id left join benefit_eva be on (br.id = be.eva_rec_id and be.eva_is_del = 1)" +
                    " left join user_info ui1 on bd.don_user_id = ui1.id left join user_info ui2 on br.rec_user_id = ui2.id" +
                    " where br.rec_status > 1 and bd.id = ? order by br.rec_time desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(DonateSeccssListDto.class));
            sqlQuery.setParameter(0, donId);
            sqlQuery.setFirstResult(page);
            sqlQuery.setMaxResults(pageSize);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public CommentAndApply queryCommentAndApplyByUserId(int userId) {
        try {
            String sql = "select IFNULL(sum(bd.don_comment_sum),0) donCommentSum,IFNULL(sum(bd.don_apply_sum),0) donApplySum from benefit_donate bd where bd.don_user_id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(CommentAndApply.class));
            queryObject.setParameter(0, userId);
            return (CommentAndApply) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public CommentAndApply queryCommentAndApplyByUserIdAndActId(int userId, int actId) {
        try {
            String sql = "select IFNULL(sum(bd.don_comment_sum),0) donCommentSum,IFNULL(sum(bd.don_apply_sum),0) donApplySum from benefit_donate bd where bd.don_user_id = ? and bd.don_act_id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(CommentAndApply.class));
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, actId);
            return (CommentAndApply) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }

    public CommentAndApply queryCommentAndApplyByUserIdAndActIdAndDonId(int userId, int donId) {
        try {
            String sql = "select IFNULL(sum(bd.don_comment_sum),0) donCommentSum,IFNULL(sum(bd.don_apply_sum),0) donApplySum from benefit_donate bd where bd.don_user_id = ? and bd.id = ?";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(CommentAndApply.class));
            queryObject.setParameter(0, userId);
            //queryObject.setParameter(1, actId);
            queryObject.setParameter(1, donId);
            return (CommentAndApply) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("查询失败", re);
            throw re;
        }
    }


}
