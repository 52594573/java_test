package com.ktp.project.dao;

import com.ktp.project.dto.UserCardDto;
import com.ktp.project.entity.UserCard;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2019/3/19 14:37
 */
@Repository
public class UserCardDao {
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private ModifyChannelDao modifyChannelDao;

    /**
     * 保存
     *
     * @return void
     * @params: [userCard]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:38
     */
    public void saveOrUpdate(UserCard userCard) {
        Date now = new Date();
        UserCard card = this.queryCardByCardNo(userCard.getUserId(), userCard.getCardNo());
        if (card != null) {
            Integer id = card.getId();
            BeanUtils.copyProperties(userCard, card);
            card.setId(id);
            card.setUpdateTime(now);
            modifyChannelDao.update(card);
        } else {
            userCard.setInitTime(now);
            userCard.setUpdateTime(now);
            modifyChannelDao.save(userCard);
        }
    }


    /**
     * 删除/恢复
     *
     * @return void
     * @params: [id, isDel]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:40
     */
    public void delete(UserCard userCard) {
        modifyChannelDao.delete(userCard);
    }

    /**
     * 根据卡号查询卡信息
     *
     * @return com.ktp.project.entity.UserCard
     * @params: [userId, cardNo]
     * @Author: wuyeming
     * @Date: 2019/3/18 16:40
     */
    public UserCard queryCardByCardNo(Integer userId, String cardNo) {
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(cardNo);
        String hql = "from UserCard where userId = ? and cardNo = ?";
        UserCard userCard = queryChannelDao.queryOne(hql, params, UserCard.class);
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
    public UserCard queryCardById(Integer id) {
        List<Object> params = new ArrayList<>();
        params.add(id);
        String hql = "from UserCard where id = ?";
        UserCard userCard = queryChannelDao.queryOne(hql, params, UserCard.class);
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
        List<Object> params = new ArrayList<>();
        params.add(id);
        String sql = "SELECT DISTINCT c.id,c.card_no cardNo,c.card_pic cardPic,c.init_time initTime,c.bank_code bankCode,c.branch_name branchName,c.user_id userId,u.u_realname uRealname " +
                "FROM user_card c JOIN user_info u ON u.id = c.user_id " +
                "WHERE c.is_del = 0 AND c.id = ? ORDER BY c.init_time DESC";
        UserCardDto userCard = queryChannelDao.selectOneAndTransformer(sql, params, UserCardDto.class);
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
        List<Object> params = new ArrayList<>();
        params.add(userId);
        String sql = "SELECT DISTINCT c.id,c.card_no cardNo,c.card_pic cardPic,c.init_time initTime,c.bank_code bankCode,c.branch_name branchName,c.user_id userId,u.u_realname uRealname " +
                "FROM user_card c JOIN user_info u ON u.id = c.user_id " +
                "WHERE c.is_del = 0 AND c.user_id = ? ORDER BY c.init_time DESC";
        List<UserCardDto> list = queryChannelDao.selectManyAndTransformer(sql, params,UserCardDto.class);
        return list;
    }
}
