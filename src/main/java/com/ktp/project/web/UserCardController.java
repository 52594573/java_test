package com.ktp.project.web;

import com.ktp.project.dto.UserCardDto;
import com.ktp.project.entity.UserCard;
import com.ktp.project.service.UserCardService;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2019/3/18 16:14
 */
@RestController
@RequestMapping(value = "api/userCard", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class UserCardController {
    private static final Logger log = LoggerFactory.getLogger(UserCardController.class);
    @Autowired
    private UserCardService userCardService;


    /**
      * 保存卡
      *
      * @return java.lang.String
      * @params: [userCard]
      * @Author: wuyeming
      * @Date: 2019/3/18 16:46
      */
    @RequestMapping(value = "save")
    public String save(UserCard userCard) {
        try {
            userCardService.saveOrUpdate(userCard);
            return ResponseUtil.createNormalJson(null);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
      * 删除/恢复
      *
      * @return java.lang.String
      * @params: [id, isDel]
      * @Author: wuyeming
      * @Date: 2019/3/18 16:46
      */
    @RequestMapping(value = "delete")
    public String delete(Integer id, Integer userId, Integer isDel) {
        try {
            userCardService.delete(id, userId, isDel);
            return ResponseUtil.createNormalJson(null);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
      * 根据用户id查询卡列表
      *
      * @return java.lang.String
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019/3/18 16:47
      */
    @RequestMapping(value = "getCardListByUserId",method = RequestMethod.GET)
    public String getCardListByUserId(Integer userId) {
        try {
            List<UserCardDto> list = userCardService.selectCardByUserId(userId);
            return ResponseUtil.createNormalJson(list);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    @RequestMapping(value = "getCardInfoById", method = RequestMethod.GET)
    public String getCardInfoById(Integer id) {
        try {
            UserCardDto userCard = userCardService.selectCardById(id);
            return ResponseUtil.createNormalJson(userCard);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
