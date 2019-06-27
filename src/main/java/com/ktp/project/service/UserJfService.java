package com.ktp.project.service;

import com.ktp.project.dao.UserJfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-12-28 下午 17:04
 */
@Service
@Transactional
public class UserJfService {
    @Autowired
    private UserJfDao userJfDao;


    /**
      * 查询积分总数
      *
      * @return int
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019-01-04 上午 9:56
      */
    public int getJfByUserId(int userId) {
        int sum = userJfDao.getJfByUserId(userId);
        return sum;
    }


    /**
      * 查询积分详情
      *
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019-01-04 上午 9:56
      */
    public List<Map<String, Object>> getJfDetailByUserId(int userId) {
        List<Map<String, Object>> monthList = userJfDao.getMonthByUserId(userId);
        for (Map<String, Object> monthMap : monthList) {
            String month = (String) monthMap.get("month");
            int addSum = userJfDao.getMonthJfByUserId(userId, month, 1);//获得的积分
            int subSum = Math.abs(userJfDao.getMonthJfByUserId(userId, month, 2));//使用的积分
            List<Map<String, Object>> detailList = userJfDao.getJfDetail(userId, month);//积分获取/使用详情
            monthMap.put("addSum", addSum + "");
            monthMap.put("subSum", subSum + "");
            monthMap.put("detailList", detailList);
        }
        return monthList;
    }
}
