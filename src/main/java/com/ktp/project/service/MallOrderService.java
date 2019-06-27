package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商城订单Service
 *
 * @author djcken
 * @date 2018/5/19
 */
@Service
public class MallOrderService {

    @Autowired
    private DataBaseDao dataBaseDao;

    @Transactional
    public boolean saveMallOrder(MallOrder mallOrder) {
        return dataBaseDao.saveOrUpdateOrder(mallOrder);
    }

    @Transactional
    public boolean updateMallOrder(MallOrder mallOrder) {
        return dataBaseDao.updateMallOrder(mallOrder);
    }

    @Transactional
    public GoodOrderQuery queryGoodOrderJoinList(int goodId, int specId) {
        return dataBaseDao.queryGoodOrderJoin(goodId, specId);
    }

    @Transactional
    public MallGoodOrder queryGoodOrder(String outTradeNo, int goodId) {
        return dataBaseDao.queryMallGoodOrder(outTradeNo, goodId);
    }

    @Transactional
    public boolean saveGoodOrder(List<MallGoodOrder> goodOrderList) {
        boolean saveGoodOrder = !goodOrderList.isEmpty();
        for (MallGoodOrder mallGoodOrder : goodOrderList) {//将订单商品循环插入数据库中
            boolean saveSuccess = dataBaseDao.saveGoodOrder(mallGoodOrder);
            saveGoodOrder = saveGoodOrder && saveSuccess;
        }
        return saveGoodOrder;
    }

    @Transactional
    public boolean updateMallGoodOrder(List<MallGoodOrder> goodOrderList) {
        boolean updateGoodOrder = !goodOrderList.isEmpty();
        for (MallGoodOrder mallGoodOrder : goodOrderList) {//将订单商品循环插入数据库中
            boolean updateSuccess = dataBaseDao.updateMallGoodOrder(mallGoodOrder);
            updateGoodOrder = updateGoodOrder && updateSuccess;
        }
        return updateGoodOrder;
    }

    @Transactional
    public MallOrder queryMallOrderByOutTradeNo(String outTradeNo) {
        return dataBaseDao.queryMallOrderByOutTradeNo(outTradeNo);
    }

    @Transactional
    public MallOrderRefund queryMallOrderRefundByOutTradeNo(String outTradeNo) {
        return dataBaseDao.queryMallOrderRefundByOutTradeNo(outTradeNo);
    }

    @Transactional
    public boolean saveOrUpdateMallOrderRefund(MallOrderRefund mallOrderRefund) {
        return dataBaseDao.saveOrUpdateMallOrderRefund(mallOrderRefund);
    }

    @Transactional
    public int updateMallOrderByOutTradeId(String outTradeNo, int state) {
        return dataBaseDao.updateMallOrderByOutTradeId(outTradeNo, state);
    }

    @Transactional
    public List<MallOrder> getOrderList(int userId, int state) {
        return dataBaseDao.queryMallOrderByUserId(userId, state);
    }

    @Transactional
    public List<MallGet> queryMallGetList(int userId) {
        return dataBaseDao.queryMallGet(userId);
    }

    @Transactional
    public List<GoodOrderQuery> queryMallGoodOrderList(int userId, String outTradeNo) {
        return dataBaseDao.queryMallGoodOrderList(userId, outTradeNo);
    }

    @Transactional
    public int deleteMallOrder(int userId, String outTradeNo) {
        return dataBaseDao.deleteMallOrder(userId, outTradeNo);
    }

    @Transactional
    public int deleteMallCarGood(int userId, int goodId, int specId) {
        return dataBaseDao.deleteMallCarGood(userId, goodId, specId);
    }

    @Transactional
    public int cancelMallOrder(int userId, String outTradeNo) {
        return dataBaseDao.cancelMallOrder(userId, outTradeNo);
    }
}
