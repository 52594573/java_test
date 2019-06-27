package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 第六镜相关数据存储查询-six_add six_edit six_zj等
 *
 * @author djcken
 * @date 2018/5/11
 */
@Service
public class SixService {

    @Autowired
    private DataBaseDao dataBaseDao;

    @Transactional
    public List getSixZjList() {
        return dataBaseDao.querySixZjList(0);
    }

    @Transactional
    public List getSixZjErrorList() {
        return dataBaseDao.querySixZjErrorList(0);
    }

    @Transactional
    public int updateSixZj(int id, String in_img_qiniu) {
        return dataBaseDao.updateSixZj(id, in_img_qiniu);
    }
}
