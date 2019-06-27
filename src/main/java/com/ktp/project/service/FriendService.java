package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.dao.OrganDao;
import com.ktp.project.dto.FriendListDto;
import com.ktp.project.entity.ChatFriend;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.dao.FriendDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by LinHon 2018/8/6
 */
@Service
@Transactional
public class FriendService {

    private final int APPLY = 1;
    private final int AGREE = 2;
    private final int REMOVE = 3;
    private final int REFUSE = 4;
    private final int NOT_READ = 99;

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private OrganDao organDao;

    /**
     * 注册默认IM账号
     */
    public void defaultRegisters() {
        HuanXinRequestUtils.registers(getNotRegisters());
    }

    public List<String> getNotRegisters() {
        Map<String, Boolean> map = Maps.newHashMap();
        List<String> differences = Lists.newArrayList();
        List<String> userIds = HuanXinRequestUtils.builderPrefix(queryUsers());
        List<String> imUserIds = Lists.newArrayList();
        buildIMUserIds(imUserIds, null);

        for (String userId : userIds) {
            map.put(userId, false);
        }

        for (String imUserId : imUserIds) {
            if (map.containsKey(imUserId)) {
                map.put(imUserId, true);
            }
        }

        for (Map.Entry<String, Boolean> item : map.entrySet()) {
            if (!item.getValue()) {
                differences.add(item.getKey());
            }
        }
        return differences;
    }

    public void buildIMUserIds(List<String> imUserIds, String cursor) {
        JSONObject response = HuanXinRequestUtils.queryUsers(cursor);

        JSONArray entities = JSONArray.fromObject(response.get("entities"));
        Iterator iterator = entities.iterator();
        while (iterator.hasNext()) {
            String username = JSONObject.fromObject(iterator.next()).get("username").toString();
            imUserIds.add(username);
        }

        if (response.get("cursor") != null) {
            cursor = response.get("cursor").toString();
            buildIMUserIds(imUserIds, cursor);
        }
    }


    /**
     * 添加好友
     *
     * @param userId       用户自己的ID
     * @param friendUserId 要添加好友的ID
     * @param applyMsg     添加好友备注
     */
    public void applyAddFriend(int userId, int friendUserId, String applyMsg) {
        ChatFriend oldChatFriend = friendDao.queryFriend(userId, friendUserId);
        if (oldChatFriend == null) {
            ChatFriend newChatFriend = new ChatFriend();
            newChatFriend.setLeftUid(userId);
            newChatFriend.setRightUid(friendUserId);
            newChatFriend.setLastTime(new Timestamp(System.currentTimeMillis()));
            newChatFriend.setRelationType(NOT_READ);
            newChatFriend.setApplyMsg(applyMsg);
            friendDao.saveOrUpdateFriend(newChatFriend);
        } else {
            if (oldChatFriend.getRelationType() == 2) {
                throw new BusinessException("对方已经是好友");
            }
            oldChatFriend.setRelationType(NOT_READ);
            oldChatFriend.setApplyMsg(applyMsg);
            oldChatFriend.setLastTime(new Timestamp(System.currentTimeMillis()));
            friendDao.saveOrUpdateFriend(oldChatFriend);
        }
    }

    /**
     * 是否是好友
     *
     * @param userId
     * @param FriendUserId
     * @return
     */
    public boolean isFriend(int userId, int FriendUserId) {
        ChatFriend chatFriend = friendDao.queryFriend(userId, FriendUserId);
        if (chatFriend == null || chatFriend.getRelationType() != 2) {
            return false;
        }
        return true;
    }

    /**
     * 新的朋友未读数量
     *
     * @param userId
     * @return
     */
    public long queryNewFriendNotReadNumber(int userId) {
        return friendDao.queryNewFriendNotReadNumber(userId);
    }


    /**
     * 新的朋友
     *
     * @param userId
     * @param startPage
     * @param length
     * @return
     */
    public List<FriendListDto> queryNewFriends(int userId, int startPage, int length) {
        List<ChatFriend> chatFriends = friendDao.queryFriendsByRelationType(userId, NOT_READ);
        for (ChatFriend item : chatFriends) {
            item.setRelationType(APPLY);
            friendDao.saveOrUpdateFriend(item);
        }

        if (startPage <= 0) {
            startPage = 1;
        }
        if (length <= 0) {
            startPage = 1;
        }
        return friendDao.queryNewFriends(userId, (startPage - 1) * length, length);
    }


    /**
     * 同意添加好友
     *
     * @param userId       被添加用户ID
     * @param friendUserId 申请添加用户ID
     */
    public void agreeAddFriend(int userId, int friendUserId) {
        ChatFriend from = friendDao.queryFriend(friendUserId, userId);
        if (from == null || from.getRelationType() != 1) {
            throw new BusinessException("对方并未申请添加您为好友");
        }
        from.setRelationType(AGREE);
        friendDao.saveOrUpdateFriend(from);

        ChatFriend to = friendDao.queryFriend(userId, friendUserId);
        if (to == null) {
            to = new ChatFriend();
            to.setLeftUid(userId);
            to.setRightUid(friendUserId);
            to.setLastTime(new Timestamp(System.currentTimeMillis()));
        }
        to.setRelationType(AGREE);
        friendDao.saveOrUpdateFriend(to);
    }

    /**
     * 拒绝添加好友
     *
     * @param userId       被添加用户ID
     * @param friendUserId 申请添加用户ID
     */
    public void refuseAddFriend(int userId, int friendUserId) {
        ChatFriend chatFriend = friendDao.queryFriend(friendUserId, userId);
        if (chatFriend == null || chatFriend.getRelationType() != 1) {
            throw new BusinessException("对方并未申请添加您为好友");
        }
        chatFriend.setRelationType(REFUSE);
        friendDao.saveOrUpdateFriend(chatFriend);
    }

    /**
     * 删除好友关系
     *
     * @param userId       被添加用户ID
     * @param friendUserId 申请添加用户ID
     */
    public void removeFriend(int userId, int friendUserId) {
        ChatFriend from = friendDao.queryFriend(userId, friendUserId);
        from.setRelationType(REMOVE);
        friendDao.saveOrUpdateFriend(from);

        ChatFriend to = friendDao.queryFriend(friendUserId, userId);
        to.setRelationType(REMOVE);
        friendDao.saveOrUpdateFriend(to);
    }

    /**
     * 查询所有好友
     *
     * @param userId
     * @return
     */
    public List<FriendListDto> queryFriends(int userId, int pro_id) {
        List<FriendListDto> list = (List<FriendListDto>) friendDao.queryFriends(userId);
        if (pro_id > 0) {
            List<Integer> userIds = organDao.queryUserIdsByProjectId(pro_id);
            for (FriendListDto dto : list) {
                if (userIds.contains(dto.getUser_id())) {
                    dto.setExistReqProject(1);
                } else {
                    dto.setExistReqProject(0);
                }
            }
        }
        return list;
    }

    /**
     * 根据手机号搜索好友
     *
     * @param mobile
     * @return
     */
    public List<FriendListDto> queryFriendsByMobile(String mobile) {
        return friendDao.queryFriendsByMobile(mobile);
    }

    /**
     * 查询所有用户ID
     *
     * @return
     */
    public List queryUsers() {
        return friendDao.queryUsers();
    }

}
