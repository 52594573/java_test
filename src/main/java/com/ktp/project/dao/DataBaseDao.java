package com.ktp.project.dao;

import com.ktp.project.entity.*;
import com.ktp.project.util.NumberUtil;
import com.zm.entity.KeyContent;
import org.apache.http.util.TextUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.jnlp.IntegrationService;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Transactional
public class DataBaseDao {

    private static final Logger log = LoggerFactory.getLogger("DataBaseDao");

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 查询six_zj表所有数据
     *
     * @param limit 如果为0则查询全部记录
     * @return
     */
    public List querySixZjList(int limit) {
        try {
            String queryString = "from SixZj s where s.inImgQiniu is null order by s.id";
            Query queryObject = getCurrentSession().createQuery(queryString);
            if (limit > 0) {
                queryObject.setMaxResults(limit);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询six_zj表所有error数据
     *
     * @param limit 如果为0则查询全部记录
     * @return
     */
    public List querySixZjErrorList(int limit) {
        try {
            String queryString = "from SixZj s where s.inImgQiniu='error' order by s.id";
            Query queryObject = getCurrentSession().createQuery(queryString);
            if (limit > 0) {
                queryObject.setMaxResults(limit);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 根据id更新six_zj表的in_img_qiniu字段
     *
     * @param id           id
     * @param in_img_qiniu 上传到七牛的图片地址
     * @return
     */
    public int updateSixZj(int id, String in_img_qiniu) {
        try {
            String queryString = "update SixZj s set s.inImgQiniu=? where s.id=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setString(0, in_img_qiniu);
            query.setInteger(1, id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update six_zj fail", re);
            return -1;
        }
    }

    /**
     * 保存或更新
     *
     * @param transientInstance
     * @return
     */
    public boolean saveOrUpdateOrder(MallOrder transientInstance) {
        log.debug("saveOrUpdateOrder MallOrder instance");
        try {
            String outTradeNo = transientInstance.getOutTradeNo();
            MallOrder mallOrder = null;
            if (!TextUtils.isEmpty(outTradeNo)) {
                mallOrder = queryMallOrderByOutTradeNo(outTradeNo);
            }
            if (mallOrder == null) {//如果查出来为空则新插入一条记录
                getCurrentSession().save(transientInstance);
            } else {
                transientInstance.setId(mallOrder.getId());
                getCurrentSession().update(transientInstance);
            }
            log.debug("saveOrUpdateOrder successful");
            return true;
        } catch (RuntimeException re) {
            log.error("saveOrUpdateOrder failed", re);
        }
        return false;
    }

    /**
     * 根据订单号查询订单记录
     *
     * @param outTradeNo
     * @return
     */
    public MallOrder queryMallOrderByOutTradeNo(String outTradeNo) {
        try {
            String queryString = "from MallOrder mo where mo.outTradeNo=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, outTradeNo);
            return (MallOrder) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 根据订单号和商品号查询订单商品记录
     *
     * @param outTradeNo
     * @return
     */
    public MallGoodOrder queryMallGoodOrder(String outTradeNo, int goodId) {
        try {
            String queryString = "from MallGoodOrder mgo where mgo.outTradeNo=? and mgo.goodId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, outTradeNo).setParameter(1, goodId);
            return (MallGoodOrder) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 删除订单以及订单商品
     *
     * @param userId     用户id
     * @param outTradeNo 订单号
     * @return
     */
    public int deleteMallOrder(int userId, String outTradeNo) {
        try {
            String queryString = "update MallOrder mo set mo.isDelete=1 where mo.buyerId=? and mo.outTradeNo=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, outTradeNo);
            return queryObject.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    /**
     * 取消订单
     *
     * @param userId     用户id
     * @param outTradeNo 订单号
     * @return
     */
    public int cancelMallOrder(int userId, String outTradeNo) {
        try {
            String queryString = "update MallOrder mo set mo.status=6 where mo.buyerId=? and mo.outTradeNo=? and mo.status=1";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, outTradeNo);
            return queryObject.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    /**
     * 根据userId查询所有订单号
     *
     * @param userId 用户id
     * @return
     */
    public List queryMallOrderByUserId(int userId, int state) {
        try {
            String queryString = "from MallOrder mo where mo.isDelete<>1 and mo.buyerId=?" + (state > 0 ? " and mo.status=?" : "") + " order by mo.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            if (state > 0) {
                queryObject.setParameter(1, state);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find MallOrder list failed", re);
            return null;
        }
    }

    /**
     * 联表查询商品信息
     *
     * @param goodId 商品id
     * @param specId 规格id
     * @return
     */
    public GoodOrderQuery queryGoodOrderJoin(int goodId, int specId) {
        try {
            String queryString = "select new com.ktp.project.entity.GoodOrderQuery(mg.id,mg.sortId,mg.goodName,mga.goodSpecId,mg.goodContent,mg.goodPic,mg.goodAdPic,mg.goodPrePic,mga.originPrice,mga.price,ma.name) " +
                    "from MallGood as mg,MallGoodAttr as mga,MallAttr as ma where mg.id=? and mga.goodSpecId=? and mg.id=mga.goodId and mga.goodSpecId=ma.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, goodId).setParameter(1, specId);
            return (GoodOrderQuery) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 联表查询商品信息
     *
     * @param outTradeNo 商品订单号
     * @return
     */
    public List queryMallGoodOrderList(int userId, String outTradeNo) {
        try {
            String queryString = "select new com.ktp.project.entity.GoodOrderQuery(mgo.goodId,mgo.goodName,mgo.goodDec,mgo.goodPic,mgo.goodAdPic,mgo.goodPrePic,mgo.goodOriginPrice,mgo.goodPrice,mgo.goodSpecName,mgo.goodNum) " +
                    "from MallOrder as mo, MallGoodOrder as mgo where mo.buyerId=? and mo.outTradeNo=? and mo.outTradeNo=mgo.outTradeNo";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, outTradeNo);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 保存订单商品信息
     *
     * @param transientInstance
     */
    public boolean saveGoodOrder(MallGoodOrder transientInstance) {
        log.debug("saving MallGoodOrder instance");
        try {
            getCurrentSession().save(transientInstance);
            getCurrentSession().flush();
            getCurrentSession().clear();
            log.debug("MallGoodOrder successful");
            return true;
        } catch (RuntimeException re) {
            log.error("MallGoodOrder failed", re);
        }
        return false;
    }

    public boolean updateMallOrder(MallOrder mallOrder) {
        log.debug("updateing MallOrder instance");
        try {
            getCurrentSession().update(mallOrder);
            log.debug("update MallOrder successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update MallOrder failed", re);
        }
        return false;
    }

    public boolean updateMallGoodOrder(MallGoodOrder mallGoodOrder) {
        log.debug("updateing MallGoodOrder instance");
        try {
            getCurrentSession().update(mallGoodOrder);
            getCurrentSession().flush();
            getCurrentSession().clear();
            log.debug("update MallGoodOrder successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update MallGoodOrder failed", re);
        }
        return false;
    }

    /**
     * 根据订单号查询退款记录
     *
     * @param outTradeNo
     * @return
     */
    public MallOrderRefund queryMallOrderRefundByOutTradeNo(String outTradeNo) {
        try {
            String queryString = "from MallOrderRefund mor where mor.outTradeNo=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, outTradeNo);
            return (MallOrderRefund) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 保存或更新退款状态
     *
     * @param mallOrderRefund
     * @return
     */
    public boolean saveOrUpdateMallOrderRefund(MallOrderRefund mallOrderRefund) {
        log.debug("saveOrUpdateing MallOrderRefund instance");
        try {
            getCurrentSession().saveOrUpdate(mallOrderRefund);
            log.debug("saveOrUpdate MallOrderRefund successful");
            return true;
        } catch (RuntimeException re) {
            log.error("saveOrUpdate MallOrderRefund failed", re);
        }
        return false;
    }

    /**
     * 根据订单id更新订单状态
     *
     * @param outTradeNo 订单id
     * @param state      订单状态1提交订单成功。2.付款成功3.商品出库4等待收货5.完成6.已取消7.已退款
     * @return
     */
    public int updateMallOrderByOutTradeId(String outTradeNo, int state) {
        try {
            String queryString = "update MallOrder mo set mo.status=? where mo.outTradeNo=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setInteger(0, state);
            query.setString(1, outTradeNo);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update MallOrder fail", re);
            return -1;
        }
    }

    /**
     * 通过imei号查找usertoken
     *
     * @return
     */
    /*public UserToken queryUserTokenByImei(String myImei, String userId) {
        try {
            String queryString = "from UserToken ut where ut.myImei=? and ut.userId=? order by ut.tokenTime desc";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, myImei).setParameter(1, userId);
            return (UserToken) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }*/
    public List<UserToken> queryUserTokenByImei(String myImei, String userId) {
        try {
            String queryString = "from UserToken ut where ut.myImei=? and ut.userId=? order by ut.tokenTime desc";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, myImei).setParameter(1, userId);
            List<UserToken> list = queryObject.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 查询mall_sort表所有数据
     *
     * @param state 1.正常显示2.仅仅显示附近3.仅对会员显示4.不显示
     * @return
     */
    public List queryMallSort(int state) {
        try {
            String queryString = "from MallSort ms where ms.sortState=? order by ms.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, state);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询分类
     *
     * @param sort 分类名称
     * @return
     */
    public long queryMallSort(String sort) {
        try {
            String queryString = "select count(*) from MallSort ms where ms.sortName=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, sort);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 保存分类
     *
     * @param transientInstance
     * @return
     */
    public boolean saveMallSort(MallSort transientInstance) {
        log.debug("saving MallSort instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("MallSort successful");
            return true;
        } catch (RuntimeException re) {
            log.error("MallSort failed", re);
            return false;
        }
    }

    /**
     * 查询规格是否存在
     *
     * @param spec 规格
     * @return
     */
    public long checkexist(String spec) {
        try {
            String queryString = "select count(*) from MallAttr ma where ma.name=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, spec);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 保存规格
     *
     * @param transientInstance
     * @return
     */
    public boolean saveMallAttr(MallAttr transientInstance) {
        log.debug("saving MallAttr instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("MallAttr successful");
            return true;
        } catch (RuntimeException re) {
            log.error("MallAttr failed", re);
            return false;
        }
    }

    /**
     * 联表查询商品详情
     *
     * @return
     */
    public GoodOrderQuery queryMallGoodOrderList(int goodId) {
        try {
            String queryString = "select new com.ktp.project.entity.GoodOrderQuery(mg.id,mg.goodName,mg.goodContent,mg.goodPic,mg.goodAdPic,mg.goodPrePic) " +
                    "from MallGood as mg  where mg.id=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, goodId);
            return (GoodOrderQuery) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 联表查询商品信息
     *
     * @return
     */
    public List queryGoodListBySortId(int sortId) {
        try {
            String queryString = "select new com.ktp.project.entity.GoodOrderQuery(mg.id,mg.goodName,mg.goodContent,mg.goodPic,mg.goodAdPic,mg.goodPrePic) " +
                    "from MallGood as mg " + (sortId > 0 ? " where mg.sortId=?" : "") + " order by mg.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            if (sortId > 0) {
                queryObject.setParameter(0, sortId);
            }
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 联表查询商品信息
     *
     * @return
     */
    public List queryMallAttrByGoodId(int goodId) {
        try {
            String queryString = "select new com.ktp.project.entity.MallGoodSpec(mga.goodId,mga.originPrice,mga.price,ma.id,ma.name) " +
                    "from MallGoodAttr as mga,MallAttr as ma where mga.goodId=? and mga.goodSpecId=ma.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, goodId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 保存地址信息
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateMallGet(MallGet transientInstance) {
        log.debug("saving MallGet instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update MallGet successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update MallGet failed", re);
        }
        return false;
    }

    /**
     * 根据用户id更新mallget默认地址
     *
     * @param userId 用户id
     * @return
     */
    public int updateMallGetDefaultByUserId(int userId) {
        try {
            String queryString = "update MallGet mg set mg.isDefault=0 where mg.userId=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setInteger(0, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update MallGet fail", re);
            return -1;
        }
    }

    /**
     * 根据userId查询mall_get表所有数据
     *
     * @param id id
     * @return
     */
    public MallGet queryMallGetById(int id) {
        try {
            String queryString = "from MallGet mg where mg.id=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, id);
            return (MallGet) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find MallGet failed", re);
            throw re;
        }
    }

    /**
     * 查找该用户新增的地址id
     *
     * @param userId 用户id
     * @return
     */
    public int queryMaxMallGetId(int userId) {
        try {
            String queryString = "select max(mg.id) from MallGet mg where mg.userId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return (int) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find MallGet failed", re);
            throw re;
        }
    }

    /**
     * 根据userId查询mall_get表所有数据
     *
     * @param userId 用户id
     * @return
     */
    public List queryMallGet(int userId) {
        try {
            String queryString = "from MallGet mg where mg.userId=? order by mg.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 删除地址
     *
     * @param userId    用户id
     * @param addressId 地址id
     * @return
     */
    public int deleteMallGet(int userId, int addressId) {
        try {
            String queryString = "Delete FROM MallGet mg where mg.userId=? and mg.id=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, addressId);
            return queryObject.executeUpdate();
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    /**
     * 保存商品到购物车
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateMallCar(MallCar transientInstance) {
        log.debug("save or update MallCar instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update MallCar successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update MallCar failed", re);
        }
        return false;
    }

    /**
     * 查找购物车商品
     *
     * @param userId 用户id
     * @param goodId 商品id
     * @param specId 规格id
     * @return
     */
    public MallCar queryMalCarGood(int userId, int goodId, int specId) {
        try {
            String queryString = "from MallCar mc where mc.userId=? and mc.goodId=? and mc.goodSpecId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, goodId).setParameter(2, specId);
            return (MallCar) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 删除购物车商品
     *
     * @param userId 用户id
     * @param goodId 商品id
     * @param specId 规格id
     * @return
     */
    public int deleteMallCarGood(int userId, int goodId, int specId) {
        try {
            String queryString = "Delete FROM MallCar mc where mc.userId=? and mc.goodId=? and mc.goodSpecId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, goodId).setParameter(2, specId);
            return queryObject.executeUpdate();
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    /**
     * 联表查询购物车列表
     *
     * @return
     */
    public List queryMallCarByUserId(int userId) {
        try {
            String queryString = "select new com.ktp.project.entity.ShopingCar(mc.userId,mc.goodSpecId,mc.goodId,mc.count,mg.goodName,ma.name,mga.originPrice,mga.price,mg.goodAdPic) " +
                    "from MallCar as mc,MallGood as mg,MallGoodAttr as mga,MallAttr as ma where mc.userId=? and mc.goodId=mg.id and mg.id = mga.goodId and mc.goodSpecId=mga.goodSpecId and mga.goodSpecId=ma.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 查询购物车数量
     *
     * @param userId 用户id
     * @return
     */
    public long queryMallCarCount(int userId) {
        try {
            String queryString = "select count(*) from MallCar mc where mc.userId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("select count failed", re);
            throw re;
        }
    }

    /**
     * 查询已购买数量(从订单列表里面取已付款和已完成的所有订单)
     *
     * @return
     */
    public long queryBuyCount(int goodId) {
        try {
            String queryString = "select sum(mgo.goodNum) from MallOrder as mo,MallGoodOrder as mgo where (mo.status=2 or mo.status=5) and mgo.goodId=? and mo.outTradeNo=mgo.outTradeNo";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, goodId);
            return queryObject.uniqueResult() != null ? (long) queryObject.uniqueResult() : 0;
        } catch (RuntimeException re) {
            log.error("select count failed", re);
            throw re;
        }
    }

    /**
     * 查找该条分享记录
     *
     * @param userId 用户id
     * @return
     */
    public ShareRecord queryShareRecord(int userId, int shareType) {
        try {
            String queryString = "from ShareRecord sr where sr.shareUid=? and sr.shareType=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, shareType);
            return (ShareRecord) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }


    /**
     * 保存分享点击记录
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateShareRecord(ShareRecord transientInstance) {
        log.debug("save or update ShareRecord instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update ShareRecord successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update ShareRecord failed", re);
        }
        return false;
    }

    /**
     * 查找该条分享提交记录
     *
     * @param userId 用户id
     * @return
     */
    public ShareCommit queryShareCommit(int userId, int shareType, int commitType, String tel) {
        try {
            String queryString = "from ShareCommit sc where sc.shareUid=? and sc.shareType=? and sc.commitType=? and sc.commitTel=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, shareType);
            queryObject.setParameter(2, commitType);
            queryObject.setParameter(3, tel);
            return (ShareCommit) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查找该条分享提交记录
     *
     * @return
     */
    public List<ShareCommit> queryShareCommitbyphone(String tel) {
        try {
            String queryString = "from ShareCommit sc where sc.commitTel=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, tel);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 保存分享提交记录
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateShareCommit(ShareCommit transientInstance) {
        log.debug("save or update ShareCommit instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update ShareCommit successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update ShareCommit failed", re);
        }
        return false;
    }

    /**
     * 查询分享类型下总共分享次数
     *
     * @param shareType 分享类型
     * @return
     */
    public long queryShareCount(int shareType) {
        try {
            String queryString = "select count(*) from ShareRecord sr where sr.shareType=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, shareType);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询身份证信息
     *
     * @param userId
     * @return
     */
    public UserSfz queryUserSfzByUserId(int userId) {
        try {
            String queryString = "from UserSfz us where us.userId=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            return (UserSfz) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 保存或更新身份证信息
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateUserSfz(UserSfz transientInstance) {
        log.debug("save or update UserSfz instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update UserSfz successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update UserSfz failed", re);
        }
        return false;
    }

    /**
     * 根据userId查询该用户银行卡列表
     *
     * @param userId 用户id
     * @return
     */
    public List queryUserBank(int userId) {
        try {
            String queryString = "select new com.ktp.project.entity.UserBankModel(ub.id,ub.userId,ub.sysBankId,ub.bankName,ub.bankId,ub.bankPic,ub.isDel,ub.inTime,sb.bankLogo,sb.bankBg) " +
                    "from UserBank as ub,SysBank as sb where ub.userId=? and ub.isDel=0 and ub.sysBankId=sb.id order by ub.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 根据userId,bankNo查询该用户银行卡列表
     *
     * @param userId 用户id
     * @return
     */
    public UserBankModel queryUserBankModel(int userId, String bankNo) {
        try {
            String queryString = "select new com.ktp.project.entity.UserBankModel(ub.id,ub.userId,ub.sysBankId,ub.bankName,ub.bankId,ub.bankPic,ub.isDel,ub.inTime,sb.bankLogo,sb.bankBg) " +
                    "from UserBank as ub,SysBank as sb where ub.userId=? and ub.bankId=? and ub.isDel=0 and ub.sysBankId=sb.id order by ub.id";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, bankNo);
            return (UserBankModel) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            throw re;
        }
    }

    /**
     * 根据userId,bankId,poid等查询该用户银行卡列表
     *
     * @param userId 用户id
     * @return
     */
    public UserBankModel queryUserBank(int userId, int popType, int poId) {
        try {
            String queryString = "select new com.ktp.project.entity.UserBankModel(ub.id,ub.userId,ub.sysBankId,ub.bankName,ub.bankId,ub.bankPic,ub.isDel,ub.inTime,sb.bankLogo,sb.bankBg) " +
                    "from UserBank as ub,SysBank as sb,ProOrganPer as pop where ub.userId=? and ub.isDel=0 " +
                    "and ub.sysBankId=sb.id and ub.userId=pop.userId and ub.bankId = pop.popBankid and pop.popType=? and pop.popState=0 and pop.poId=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, popType);
            queryObject.setParameter(2, poId);
            return (UserBankModel) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询银行卡信息
     *
     * @param userId
     * @return
     */
    public UserBank queryUserBank(int userId, String bankId) {
        try {
            String queryString = "from UserBank ub where ub.userId=? and ub.bankId=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, bankId);
            return (UserBank) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find UserBank failed", re);
            throw re;
        }
    }

    /**
     * 保存或更新银行卡信息
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateUserBank(UserBank transientInstance) {
        log.debug("save or update UserBank instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update UserBank successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update UserBank failed", re);
        }
        return false;
    }

    /**
     * 删除银行卡号
     *
     * @param userId 用户id
     * @param bankId 银行卡号
     * @return
     */
    public int deleteUserBank(int userId, String bankId) {
        try {
            String queryString = "update UserBank ub set ub.isDel=1 where ub.userId=? and ub.bankId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, bankId);
            return queryObject.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }

    /**
     * 系统银行列表
     *
     * @return
     */
    public List querySysBank() {
        try {
            String queryString = "from SysBank sb where sb.bankName is not null";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询当天考勤是否有记录轨迹
     *
     * @param userId 用户id
     * @param proId  项目id
     * @param time   时间（例如2018-08-09）
     * @return
     */
    public KaoQinLocation queryKaoQinLocation(int userId, int proId, String time) {
        try {
            String queryString = "from KaoQinLocation kl where kl.userId=? and kl.proId=? and kl.time=?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, proId);
            queryObject.setParameter(2, time);
            return (KaoQinLocation) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find KaoQinLocation failed", re);
            throw re;
        }
    }

    /**
     * 保存或更新考勤记录轨迹
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateKaoQinLocation(KaoQinLocation transientInstance) {
        log.debug("save or update KaoQinLocation instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save or update KaoQinLocation successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update KaoQinLocation failed", re);
        }
        return false;
    }

    /**
     * 查找appstore是否审核中
     *
     * @return
     */
    public KeyContent queryAppstoreAudit() {
        try {
            String queryString = "from KeyContent kc where kc.keyId=17";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return (KeyContent) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all KeyContent failed", re);
            throw re;
        }
    }

    public Double countByUserIdAndProjectId(WorkLogGather bo) {
        Integer dayNum = NumberUtil.getDayOfLastMonthByNow();
        String sql = "SELECT COUNT(1)   " +
                "FROM ( " +
                "   SELECT SUM(CASE k.k_state " +
                "         WHEN 1 THEN 1 " +
                "         WHEN 3 THEN 1 " +
                "         WHEN 5 THEN 1 " +
                "         ELSE 0 " +
                "      END) AS comeIn, SUM(CASE k.k_state " +
                "         WHEN 2 THEN 1 " +
                "         WHEN 4 THEN 1 " +
                "         WHEN 6 THEN 1 " +
                "         ELSE 0 " +
                "      END) AS comeOut " +
                "   FROM kaoqin_list" + bo.getProjectId() + " kl " +
                "      LEFT JOIN kaoqin" + bo.getProjectId() + " k ON kl.`id` = k.`kl_id` " +
                "   WHERE kl.`pro_id` = ? " +
                "      AND kl.`kl_u_id` = ? " +
                "      AND kl.`kl_date` BETWEEN ? AND ? " +
                "      AND kl.`kl_count` >= 1 " +
                "      AND kl.`kl_fenzhong` >= 480 " +
                "   GROUP BY kl.`id` " +
                "   HAVING comeIn > 0 " +
                "   AND comeOut > 0 " +
                ") temp";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter(0, bo.getProjectId());
        query.setParameter(1, bo.getUserId());
        query.setParameter(2, NumberUtil.getTheFirstDayOfLastMonth());
        query.setParameter(3, NumberUtil.getTheFirstDayOfCurrentMonth());
        BigInteger result = (BigInteger) query.uniqueResult();
        return countWorkRate(result.doubleValue() / dayNum);
    }

    private Double countWorkRate(Double flag) {
        double workRate = 0D;
        if (flag >= 0.95) {
            workRate = 20D;
        }
        if (flag < 0.95 && flag >= 0.85) {
            workRate = (flag - 0.85) * 100 + 10D;
        }
        if (flag < 0.85 && flag >= 0.80) {
            workRate = (flag - 0.80) * 200;
        }
        return NumberUtil.keepOneDecimalDouRoundOff(workRate);
    }

    public ShareCommit querySharecommit(String mobile) {
        String sql = "SELECT " +
                " sr_uid shareUid, " +
                " sr_type shareType, " +
                " sr_channel shareCannel, " +
                " sc_tel commitTel, " +
                " sc_time commitTime, " +
                " sc_type commitType, " +
                " sc_reg commitRegister, " +
                " sc_cert commitCert, " +
                " sc_pro_id commitProId, " +
                " sc_organ_id commitOrganid , ccb_bank_id as ccbBankId " +
                "FROM " +
                " share_commit " +
                "WHERE " +
                " sc_tel = ?  " +
                "AND sc_type = 3 and sr_type = 3 ORDER BY sc_time ASC";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, mobile);
        sqlQuery.setFirstResult(0);
        sqlQuery.setMaxResults(1);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ShareCommit.class));
        return (ShareCommit) sqlQuery.uniqueResult();
    }
}
