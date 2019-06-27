package com.ktp.project.service;

import com.ktp.project.dao.AdvertiseDao;
import com.ktp.project.dto.AdvertiseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-12-28 下午 16:20
 */
@Service
@Transactional
public class AdvertiseService {
    @Autowired
    private AdvertiseDao advertiseDao;


    /**
      * 查询首页广告列表
      *
      * @return java.util.List<com.ktp.project.dto.AdvertiseDto>
      * @params: [index, size]
      * @Author: wuyeming
      * @Date: 2019-01-15 上午 11:33
      */
    public List<AdvertiseDto> getAdvertiseList(Integer index, Integer size) {
        if (index == null) {
            index = 0;
        }
        if (size == null) {
            size = 5;
        }
        List<AdvertiseDto> list = advertiseDao.getAdvertiseList(index, size);
        return list;
    }

    /**
     * 查询我的广告列表
     *
     * @return java.util.List<com.ktp.project.dto.AdvertiseDto>
     * @params: [index, size]
     * @Author: wuyeming
     * @Date: 2019-01-15 上午 11:33
     */
    public List<AdvertiseDto> getAdvertiseListMy(Integer index, Integer size) {
        if (index == null) {
            index = 0;
        }
        if (size == null) {
            size = 5;
        }
        List<AdvertiseDto> list = advertiseDao.getAdvertiseListMy(index, size);
        return list;
    }

    public List<AdvertiseDto> listHatchAdvertise() {
        return advertiseDao.listHatchAdvertise();
    }
}
