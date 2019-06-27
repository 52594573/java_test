package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.entity.ShareCommit;
import com.ktp.project.entity.ShareRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分享Service
 *
 * @author djcken
 * @date 2018/6/4
 */
@Service
public class ShareService {

    @Autowired
    DataBaseDao dataBaseDao;

    @Transactional
    public ShareRecord queryShareRecord(int userId,int shareType) {
        return dataBaseDao.queryShareRecord(userId,shareType);
    }

    @Transactional
    public boolean saveOrUpdateShareRecord(ShareRecord transientInstance){
        return dataBaseDao.saveOrUpdateShareRecord(transientInstance);
    }

    @Transactional
    public ShareCommit queryShareCommit(int userId, int shareType, int commitType, String tel){
        return dataBaseDao.queryShareCommit(userId, shareType, commitType, tel);
    }

    @Transactional
    public List<ShareCommit> queryShareCommitbyphone(String tel){
        return dataBaseDao.queryShareCommitbyphone(tel);
    }

    @Transactional
    public boolean saveOrUpdateShareCommit(ShareCommit transientInstance) {
        return dataBaseDao.saveOrUpdateShareCommit(transientInstance);
    }

    public long queryShareCount(int shareType) {
        return dataBaseDao.queryShareCount(shareType);
    }

    public ShareCommit getShareCommit(String mobile) {
        return dataBaseDao.querySharecommit(mobile);
    }

}
