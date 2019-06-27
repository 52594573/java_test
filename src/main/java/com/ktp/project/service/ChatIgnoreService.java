package com.ktp.project.service;

import com.ktp.project.dao.ChatIgnoreDao;
import com.ktp.project.entity.ChatIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LinHon 2018/8/20
 */
@Service
@Transactional
public class ChatIgnoreService {

    @Autowired
    private ChatIgnoreDao chatIgnoreDao;

    /**
     * 修改屏蔽关系
     *
     * @param fromUserId
     * @param toUserId
     */
    public void modify(int fromUserId, int toUserId) {
        ChatIgnore chatIgnore = chatIgnoreDao.query(fromUserId, toUserId);
        if (chatIgnore != null) {
            chatIgnoreDao.remove(chatIgnore);
        } else {
            chatIgnore = new ChatIgnore();
            chatIgnore.setLeftUid(fromUserId);
            chatIgnore.setRightUid(toUserId);
            chatIgnoreDao.create(chatIgnore);
        }
    }

    /**
     * 是否屏蔽消息
     *
     * @param fromId
     * @param toId
     * @return
     */
    public boolean isIgnoreMsg(int fromId, int toId) {
        return chatIgnoreDao.query(fromId, toId) != null;
    }

}
