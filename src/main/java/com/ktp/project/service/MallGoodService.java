package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商城商品Service
 *
 * @author djcken
 * @date 2018/5/19
 */
@Service
public class MallGoodService {

    @Autowired
    private DataBaseDao dataBaseDao;

    @Transactional
    public List<MallSort> queryMallSortList(int state) {
        return dataBaseDao.queryMallSort(state);
    }

    @Transactional
    public List<GoodOrderQuery> queryMallGoodList(int sortId) {
        return dataBaseDao.queryGoodListBySortId(sortId);
    }

    @Transactional
    public GoodOrderQuery queryGoodOrderDetail(int goodId) {
        return dataBaseDao.queryMallGoodOrderList(goodId);
    }

    @Transactional
    public List<MallGoodSpec> queryMallGoodSpecList(int goodId) {
        return dataBaseDao.queryMallAttrByGoodId(goodId);
    }

    @Transactional
    public boolean saveOrUpdateMallCar(MallCar mallCar) {
        return dataBaseDao.saveOrUpdateMallCar(mallCar);
    }

    @Transactional
    public MallCar queryMalCarGood(int userId, int goodId, int specId) {
        return dataBaseDao.queryMalCarGood(userId, goodId, specId);
    }

    @Transactional
    public int deleteMallCarGood(int userId, int goodId, int specId) {
        return dataBaseDao.deleteMallCarGood(userId, goodId, specId);
    }

    @Transactional
    public List<ShopingCar> queryMallCarByUserId(int userId) {
        return dataBaseDao.queryMallCarByUserId(userId);
    }

    @Transactional
    public long queryMallCarCount(int userId) {
        return dataBaseDao.queryMallCarCount(userId);
    }

    @Transactional
    public long queryBuyCount(int goodId) {
        return dataBaseDao.queryBuyCount(goodId);
    }

    @Transactional
    public boolean checkSortExist(String sort) {
        return dataBaseDao.queryMallSort(sort) > 0;
    }

    @Transactional
    public boolean saveMallSort(MallSort transientInstance) {
        return dataBaseDao.saveMallSort(transientInstance);
    }

    @Transactional
    public boolean checkSpecExist(String spec) {
        return dataBaseDao.checkexist(spec) > 0;
    }

    @Transactional
    public boolean saveMallAttr(MallAttr transientInstance) {
        return dataBaseDao.saveMallAttr(transientInstance);
    }

}
