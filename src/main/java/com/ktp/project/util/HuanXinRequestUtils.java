package com.ktp.project.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.dto.im.GroupDto;
import com.ktp.project.dto.im.MessageDto;
import com.ktp.project.exception.BusinessException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by LinHon 2018/8/2
 */
public class HuanXinRequestUtils {

    public static final Logger log = LoggerFactory.getLogger(HuanXinRequestUtils.class);

    public static final String APPLICATION_JSON = "application/json";

    public static final String DEFAULT_MSG = "该版本暂不支持此样式，请更新到最新版本查看，谢谢。";

    /**
     * 域名
     */
    private static final String DOMAIN_NAME = PropertiesUtils.getValue("im.domain");

    private static final String ORG_NAME = PropertiesUtils.getValue("im.org.name");

    private static final String CLIENT_ID = PropertiesUtils.getValue("im.client.id");

    private static final String CLIENT_SECRET = PropertiesUtils.getValue("im.client.secret");

    /**
     * 创建的APP应用名称
     */
    private static final String APP_NAME = PropertiesUtils.getValue("im.app.name");

    private static final String USERNAME_PREFIX = PropertiesUtils.getValue("im.username.prefix");

    /**
     * 授权Token
     */
    private static String token;


    /**
     * 初始化Token
     */
    static {
        token = "Bearer " + getToken().get("access_token").toString();
    }

    /**
     * 初始化Token
     */
    private synchronized static void initToken() {
        token = "Bearer " + getToken().get("access_token").toString();
    }

    /**
     * 获取Token
     *
     * @return
     */
    public static JSONObject getToken() {
        Map<String, Object> body = Maps.newHashMap();
        body.put("grant_type", "client_credentials");
        body.put("client_secret", CLIENT_SECRET);
        body.put("client_id", CLIENT_ID);

        String data = JSONObject.fromObject(body).toString();
        String httpResponse = HttpClientUtils.post(appendUrl("token"), data, APPLICATION_JSON, null);
        return JSONObject.fromObject(httpResponse);
    }

    /**
     * 拼接环信API的请求URL
     *
     * @param str
     * @return
     */
    public static String appendUrl(Object... str) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(DOMAIN_NAME).append("/").append(ORG_NAME).append("/").append(APP_NAME);
        for (int i = 0; i < str.length; i++) {
            buffer.append("/").append(str[i]);
        }
        return buffer.toString();
    }

    /**
     * 处理异常返回
     *
     * @param e
     * @return
     */
    public static String buildExceptionResponse(Exception e) {
        if (e instanceof BusinessException && "401".equals(e.getMessage())) {
            initToken();
            return ResponseUtil.createBussniessErrorJson(401, "出现错误，请重试");
        }
        return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
    }


    /**
     * 注册IM账号(通过)
     *
     * @param userId
     */
    public static void register(Object userId) {
        String convertUserId = builderPrefix(userId);

        Map<String, Object> body = Maps.newHashMap();
        body.put("username", convertUserId);
        body.put("password", Md5Util.MD5Encode(convertUserId));

        String data = JSONObject.fromObject(body).toString();
        HttpClientUtils.post(appendUrl("users"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
    }


    /**
     * 批量注册IM账号(通过)
     *
     * @param users
     */
    public static void registers(List users) {
        log.info("未注册用户数：{}", users.size());
        List<String> convertUsers = builderPrefix(users);
        List<Map<String, Object>> body = Lists.newArrayList();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);

        for (int i = 0; i < convertUsers.size(); i++) {
            String userId = convertUsers.get(i);
            Map<String, Object> element = Maps.newHashMap();
            element.put("username", userId);
            element.put("password", Md5Util.MD5Encode(userId));
            body.add(element);
            if (body.size() == 20) {
                String data = JSONArray.fromObject(body).toString();
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        HttpClientUtils.post(appendUrl("users"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
                    }
                });
                body = Lists.newArrayList();
            }
        }
        fixedThreadPool.shutdown();

        if (body.size() != 0) {
            String data = JSONArray.fromObject(body).toString();
            HttpClientUtils.post(appendUrl("users"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
        }
    }


    /**
     * 用户是否已经加入群聊
     *
     * @param groupId
     * @param userId
     * @return true-已加入群聊 false-未加入群聊
     */
    public static boolean isInGroup(String groupId, String userId) {
        log.info("查看用户是否加入群聊 群聊ID{}, 用户ID{}", groupId, userId);
        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(userId)) {
            String convertUserId = builderPrefix(userId);
            JSONArray result = JSONArray.fromObject(queryGroup(groupId).get("data"));
            List<JSONObject> users = Lists.newArrayList(result);
            for (JSONObject item : users) {
                Object id = item.get("member");
                if (id == null) {
                    id = item.get("owner");
                }
                if (id.equals(convertUserId)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 批量获取IM用户
     *
     * @return
     */
    public static JSONObject queryUsers(String cursor) {
        String url = appendUrl("users?limit=1000");
        if (!Strings.isNullOrEmpty(cursor)) {
            url += "&cursor=" + cursor;
        }
        return defaultGet(url);
    }


    /**
     * 创建群组(通过)
     *
     * @param groupDto
     * @return
     */
    public static String createGroup(GroupDto groupDto) {
        String convertOwner = builderPrefix(groupDto.getOwner());
        List<String> convertMembers = builderPrefix(groupDto.getMembers());

        Map<String, Object> body = Maps.newHashMap();
        body.put("groupname", groupDto.getGroupName());
        body.put("desc", groupDto.getDesc());
        body.put("public", groupDto.isPublic());
        body.put("maxusers", groupDto.getMaxusers());
        body.put("members_only", groupDto.isMembersOnly());
        body.put("allowinvites", groupDto.isAllowinvites());
        body.put("owner", convertOwner);
        body.put("invite_need_confirm", groupDto.isInviteNeedConfirm());
        body.put("members", convertMembers);

        String data = JSONObject.fromObject(body).toString();
        String responseInfo = HttpClientUtils.post(appendUrl("chatgroups"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
        JSONObject response = JSONObject.fromObject(responseInfo);
        JSONObject details = JSONObject.fromObject(response.get("data"));
        return details.get("groupid").toString();    //取得新建群组ID
    }


    /**
     * 移除单个群聊成员，如果群里只有一个人则删除群聊，如果是群主则先转让再退群（通过）
     *
     * @param groupId
     * @param userId
     * @return true：正常处理，false：群解散
     */
    public static boolean removeUser(String groupId, Object userId) {
        log.info("删除群聊人员群聊ID{}, 用户ID{}", groupId, userId);
        String convertUserId = builderPrefix(userId);

        JSONArray result = JSONArray.fromObject(queryGroup(groupId).get("data"));
        if (result.size() == 1) {
            JSONObject o = JSONObject.fromObject(result.get(0));
            if (convertUserId.equals(builderPrefix(o.get("owner")))) {
                removeGroup(groupId);
                return false;
            }
            throw new RuntimeException("该用户不存在");
        }

        if (convertUserId.equals(getOwnerId(groupId))) {
            changeOwner(groupId, getNewOwnerId(groupId, Lists.newArrayList(convertUserId)));
        }

        String url = appendUrl("chatgroups", groupId, "users", convertUserId);
        HttpClientUtils.delete(url, ImmutableMap.of("Authorization", token));
        return true;
    }


    /**
     * 批量删除群成员
     *
     * @param groupId
     * @param userIds
     */
    public static void removeUsers(String groupId, List userIds) {
        List<Object> newUserIds = userIds;
        List filter = newUserIds.stream().filter(item -> isInGroup(groupId, item.toString())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filter)) {
            return;
        }
        List<String> convertUserIds = builderPrefix(filter);

        if (convertUserIds.contains(getOwnerId(groupId))) {
            changeOwner(groupId, getNewOwnerId(groupId, convertUserIds));
        }
        String url = appendUrl("chatgroups", groupId, "users", Joiner.on(",").join(convertUserIds));
        HttpClientUtils.delete(url, ImmutableMap.of("Authorization", token));
    }


    /**
     * 取得群主ID（通过）
     *
     * @param groupId
     * @return
     */
    private static String getOwnerId(String groupId) {
        JSONArray result = JSONArray.fromObject(queryGroup(groupId).get("data"));
        for (int i = 0; i < result.size(); i++) {
            JSONObject o = JSONObject.fromObject(result.get(i));
            Object ownerId = o.get("owner");
            if (ownerId != null) {
                return builderPrefix(ownerId);
            }
        }
        throw new RuntimeException("未找到群主ID");
    }


    /**
     * 取得新群主ID（通过）
     *
     * @param groupId
     * @return
     */
    private static String getNewOwnerId(String groupId, List userIds) {
        List<String> convertUserIds = builderPrefix(userIds);

        JSONArray result = JSONArray.fromObject(queryGroup(groupId).get("data"));
        for (int i = 0; i < result.size(); i++) {
            JSONObject o = JSONObject.fromObject(result.get(i));
            Object newOwnerId = o.get("member");
            if (newOwnerId != null && !convertUserIds.contains(builderPrefix(newOwnerId))) {
                return builderPrefix(newOwnerId);
            }
        }
        throw new RuntimeException("未找到新群主ID");
    }


    /**
     * 批量添加群成员（通过）
     *
     * @param userIds
     */
    public static void addUsers(String groupId, List userIds) {
        List<Object> newUserIds = userIds;
        List filter = newUserIds.stream().filter(item -> !isInGroup(groupId, item.toString())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filter)) {
            return;
        }
        List<String> convertUserIds = builderPrefix(filter);

        String url = appendUrl("chatgroups", groupId, "users");
        String data = JSONObject.fromObject(ImmutableMap.of("usernames", convertUserIds)).toString();
        HttpClientUtils.post(url, data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
    }


    /**
     * 更换群主（通过）
     *
     * @param groupId
     * @param newOwnerId
     * @return
     */
    public static JSONObject changeOwner(String groupId, Object newOwnerId) {
        String convertNewOwnerId = builderPrefix(newOwnerId);

        Map<String, Object> body = Maps.newHashMap();
        body.put("newowner", convertNewOwnerId);
        String url = appendUrl("chatgroups", groupId);
        String data = JSONObject.fromObject(body).toString();
        String responseInfo = HttpClientUtils.put(url, data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
        return JSONObject.fromObject(responseInfo);
    }


    /**
     * 删除群组（通过）
     *
     * @param groupId
     */
    public static void removeGroup(String groupId) {
        HttpClientUtils.delete(appendUrl("chatgroups", groupId), ImmutableMap.of("Authorization", token));
    }


    /**
     * 查询群聊成员（通过）
     *
     * @param groupId
     * @return
     */
    public static JSONObject queryGroup(String groupId) {
        return defaultGet(appendUrl("chatgroups", groupId, "users"));
    }


    /**
     * 默认GET请求
     *
     * @param url
     * @return
     */
    public static JSONObject defaultGet(String url) {
        String responseInfo = HttpClientUtils.get(url, ImmutableMap.of("Authorization", token));
        return JSONObject.fromObject(responseInfo);
    }


    /**
     * 发送消息
     *
     * @return void
     * @params: [fromUserId, listUserId, extMap, message]
     * @Author: wuyeming
     * @Date: 2018-11-29 下午 18:35
     */
    public static void sendMessage(String fromUserId, List toUserIdList, Map<String, Object> extMap, String message) {
        sendMessage(fromUserId, toUserIdList, extMap, message, false);
    }

    public static void sendMessage(String fromUserId, List toUserIdList, Map<String, Object> extMap, String message, boolean isIgnore) {
        if (!fromUserId.equals("admin")) {
            fromUserId = builderPrefix(fromUserId);
        }
        List<String> convertToUserIdList = builderPrefix(toUserIdList);

        Map<String, Object> body = Maps.newHashMap();
        body.put("target_type", "users");
        body.put("from", fromUserId);
        body.put("target", convertToUserIdList);
        Map<String, Object> messageMap = Maps.newHashMap();
        messageMap.put("type", "txt");
        messageMap.put("msg", message);
        body.put("msg", messageMap);
        if (extMap != null && !extMap.isEmpty()) {
            extMap.put("isIgnore", isIgnore);
            extMap.put("em_ignore_notification", isIgnore);
            body.put("ext", extMap);
        }
        String data = JSONObject.fromObject(body).toString();
        HttpClientUtils.post(appendUrl("messages"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
    }

    /**
     * 发送扩展消息
     *
     * @param messageDto
     */
    public static void sendExtendMessage(MessageDto messageDto) {
        doSendMessage(messageDto, messageDto.getMsg());
    }

    private static void doSendMessage(MessageDto messageDto, MessageDto.Bean bean) {
        if (messageDto.getToIds().size() <= 0) {
            return;
        }

        List<String> convertToUserIds = builderPrefix(messageDto.getToIds());
        String convertFromId = messageDto.getFromId();
        if (!convertFromId.equals("admin")) {
            convertFromId = builderPrefix(convertFromId);
        }

        Map<String, Object> ext = messageDto.getExt();
        ext.put("isIgnore", messageDto.isIgnore());
        ext.put("em_ignore_notification", messageDto.isIgnore());

        if (messageDto.getTitle() != null) {
            ext.put("em_apns_ext", ImmutableMap.of("em_push_content", messageDto.getTitle()));
        }

        Map<String, Object> body = Maps.newHashMap();
        body.put("target_type", messageDto.getTargetType());
        body.put("from", convertFromId);
        body.put("msg", JSONObject.fromObject(bean));
        body.put("ext", ext);
        body.put("target", convertToUserIds);
        String data = JSONObject.fromObject(body).toString();
        log.info("IM消息体:{}", data);
        HttpClientUtils.post(appendUrl("messages"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
    }

    public static List<String> builderPrefix(List ids) {
        List<String> result = Lists.newArrayList();
        for (Object object : ids) {
            if (object.toString().startsWith(USERNAME_PREFIX)) {
                result.add(object.toString());
            } else {
                result.add(USERNAME_PREFIX + object);
            }
        }
        return result;
    }

    public static String builderPrefix(Object id) {
        if (!id.toString().startsWith(USERNAME_PREFIX)) {
            return USERNAME_PREFIX + id;
        }
        return id.toString();
    }

    /**
     * 发送扩展消息，返回发送消息失败的用户ID
     *
     * @param messageDto
     * @return
     */
    /*public static List<Object> sendExtendMessage(MessageDto messageDto) {
        List<Object> errorIds = Lists.newArrayList();
        List<Object> successIds = Lists.newArrayList();

        Map<String, Object> body = Maps.newHashMap();
        body.put("target_type", messageDto.getTargetType());
        body.put("from", messageDto.getFromId());
        body.put("msg", JSONObject.fromObject(messageDto.getMsg()));
        body.put("ext", messageDto.getExt());

        //每20个用户发送一次
        List<Object> toIds = messageDto.getToIds();
        int NUM = 20;
        int size = toIds.size();
        int frequency = size % 2 == 0 ? size / NUM : (size / NUM) + 1;
        for (int i = 0; i < frequency; i++) {
            if (frequency - i == 1) {
                body.put("target", toIds.subList(i * NUM, size));
            } else {
                body.put("target", toIds.subList(i * NUM, (i + 1) * NUM));
            }
            doSendExtendMessage(successIds, JSONObject.fromObject(body).toString());
        }

        for (Object item : toIds) {
            if (!successIds.contains(item)) {
                errorIds.add(item);
            }
        }
        return errorIds;
    }

    private static void doSendExtendMessage(List<Object> successIds, String data) {
        try {
            String response = HttpClientUtils.post(appendUrl("messages"), data, APPLICATION_JSON, ImmutableMap.of("Authorization", token));
            JSONObject json = JSONObject.fromObject(response);
            Map<String, Object> result = JSONObject.fromObject(json.get("data"));
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                if ("success".equals(entry.getValue())) {
                    successIds.add(entry.getKey());
                }
            }
        } catch (Exception e) {
            builderException(e);
        }
    }

    private static void builderException(Exception e) {
        if (e instanceof BusinessException) {
            if ("401".equals(e.getMessage())) {
                initToken();
            }
            if ("429".equals(e.getMessage()) || "503".equals(e.getMessage())) {
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException ex) {
                }
            }
        }
    }*/

}
