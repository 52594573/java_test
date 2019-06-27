package com.ktp.project.service;

import com.ktp.project.dao.UserCardDao;
import com.ktp.project.dto.UserCardDto;
import com.ktp.project.entity.UserCard;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2019/3/18 16:00
 */
@Service
@Transactional
public class UserCardService {
    @Autowired
    private UserCardDao userCardDao;

    /**
     * 保存
     *
     * @return void
     * @params: [userCard]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:38
     */
    public void saveOrUpdate(UserCard userCard) {
        userCard.setIsDel(userCard.getIsDel() != null ? userCard.getIsDel() : 0);
        userCardDao.saveOrUpdate(userCard);
    }


    /**
     * 删除/恢复
     *
     * @return void
     * @params: [id, isDel]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:40
     */
    public void delete(Integer id, Integer userId, Integer isDel) {
        Assert.isTrue(id != null, "id不能为空");
        Assert.isTrue(userId != null, "userId不能为空");
        UserCard userCard = this.queryCardById(id);
        Assert.isTrue(userCard != null, "记录不存在");
        Assert.isTrue(userId.equals(userCard.getUserId()), "不能操作非本人的记录");
        userCard.setIsDel(isDel);
        userCardDao.saveOrUpdate(userCard);
    }


    /**
      * 根据id查询卡信息
      *
      * @return com.ktp.project.entity.UserCard
      * @params: [id]
      * @Author: wuyeming
      * @Date: 2019/3/18 16:40
      */
    public UserCard queryCardById(Integer id) {
        Assert.isTrue(id != null, "id不能为空");
        UserCard userCard = userCardDao.queryCardById(id);
        Assert.isTrue(userCard != null, "记录不存在");
        return userCard;
    }

    /**
     * 根据id查询卡信息
     *
     * @return com.ktp.project.entity.UserCard
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:40
     */
    public UserCardDto selectCardById(Integer id) {
        Assert.isTrue(id != null, "id不能为空");
        UserCardDto userCard = userCardDao.selectCardById(id);
        Assert.isTrue(userCard != null, "记录不存在");
        //暂时固定建行的信息
        if (StringUtils.isBlank(userCard.getBankBg())) {
            userCard.setBankBg("https://images.ktpis.com/bank_bg_jianshe.png");
        }
        if (StringUtils.isBlank(userCard.getBankLogo())) {
            userCard.setBankLogo("https://images.ktpis.com/bank_logo_jianshe.png");
        }
        if (StringUtils.isBlank(userCard.getBankName())) {
            userCard.setBankName("建设银行");
        }
        return userCard;
    }


    /**
      * 根据用户id查询卡列表
      *
      * @return java.util.List
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019/3/18 16:41
      */
    public List<UserCardDto> selectCardByUserId(Integer userId) {
        Assert.isTrue(userId != null, "userId不能为空");
        List<UserCardDto> list = userCardDao.selectCardByUserId(userId);
        return list;
    }

}
