package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.entity.MallGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址管理
 *
 * @author djcken
 * @date 2018/6/11
 */
@Service
public class AddressService {

    @Autowired
    private DataBaseDao dataBaseDao;

    @Transactional
    public List<MallGet> queryMallGetList(int userId) {
        return dataBaseDao.queryMallGet(userId);
    }

    @Transactional
    public boolean deleteMallGet(int userId, int addressId) {
        return dataBaseDao.deleteMallGet(userId, addressId) == 1;
    }

    @Transactional
    public boolean saveOrUpdateMallGet(MallGet mallGet) {
        return dataBaseDao.saveOrUpdateMallGet(mallGet);
    }

    @Transactional
    public int updateMallGetDefaultByUserId(int userId) {
        return dataBaseDao.updateMallGetDefaultByUserId(userId);
    }

    @Transactional
    public MallGet getMallGetById(int id) {
        return dataBaseDao.queryMallGetById(id);
    }

    @Transactional
    public int queryMaxMallGetId(int userId) {
        return dataBaseDao.queryMaxMallGetId(userId);
    }
}
