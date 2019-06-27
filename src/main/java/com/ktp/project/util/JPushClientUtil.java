package com.ktp.project.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;

import java.util.*;

/**
 * 极光推送
 *
 * @author djcken
 * @date 2018/9/13
 */
public class JPushClientUtil {

    private static Properties prop = PropertiesUtil.readConfig("/properties/application.properties");
    private static String env = prop.getProperty("jpush.env");//获取运行环境 1:生产环境 2:测试环境

    private final static String APPKET = "ced4ef6495dd1e6631ef32c3";
    private final static String MASTERSECRET = "4340a73f3db1f5a40dfbaee8";

    private static JPushClientUtil sInstance;
    private JPushClient mJPushClient;

    private JPushClientUtil() {
        mJPushClient = new JPushClient(MASTERSECRET, APPKET);//通知默认保留24小时。
    }

    public static JPushClientUtil getInstance() {
        if (sInstance == null) {
            synchronized (JPushClientUtil.class) {
                if (sInstance == null) {
                    sInstance = new JPushClientUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 推送至别名设备 - 透传
     *
     * @param aliasList 别名数组
     * @param key       key
     * @param json      发送的json
     * @return
     */
    public int sendToAliasPassThrough(List<String> aliasList, String key, String json) {
        int result = 0;
        try {
            PushPayload pushPayload = buildPushObjectWithAlias(aliasList, key, json);
            System.out.println(pushPayload);
            PushResult pushResult = mJPushClient.sendPush(pushPayload);  //发送推送对象
            if (pushResult.getResponseCode() == 200) {  //状态码等于200 为成功
                result = 1;
            }
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 建立以别名推送的对象-透传
     *
     * @param aliasList 别名
     * @param key       key
     * @param json      自定义消息json
     * @return 返回推送对象
     */
    private PushPayload buildPushObjectWithAlias(List<String> aliasList, String key, String json) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象
                .setAudience(Audience.alias(aliasList))
                //jpush的消息
                .setMessage(Message.newBuilder()
                        .addExtra(key, json)
                        .build())
                .build();
    }


    /**
     * 推送所有平台
     *
     * @param userId  用户Id
     * @param title  标题
     * @param content 通知内容
     * @param object  附加字段
     * @return cn.jpush.api.push.model.PushPayload
     * @Author: wuyeming
     * @Date: 2018-09-28 17:05
     */
    public int pushDevices(Integer userId, String title, String content, Map<String, String> object, String type) {
        int result = 0;
        List<String> aliasList = new ArrayList<>();
        //1正式环境 2测试环境
        int profile = 2;
        aliasList.add("KTP_" + profile + "_A_" + userId);
        aliasList.add("KTP_" + profile + "_I_" + userId);
        PushPayload pushPayload = null;
        try {
            PlatformNotification notification = null;
            if (type.equals("1")) {
                pushPayload = PushPayload.newBuilder()
                        .setPlatform(Platform.all())
                        .setAudience(Audience.alias(aliasList))
                        .setNotification(Notification.newBuilder()
                                //指定当前推送的iOS通知
                                .addPlatformNotification(IosNotification.newBuilder()
                                        //传一个IosAlert对象，指定apns title、title、subtitle等
                                        .setAlert(title)
                                        .setContentAvailable(true)
                                        //设置应用角标为1
                                        .setBadge(1)
                                        //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                        // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                        .setSound("sound.caf")
                                        .addExtras(object)
                                        //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                        //取消此注释，消息推送时ios将无法在锁屏情况接收
                                        .build())
                                .build())
                        .setMessage(Message.newBuilder()
                                .setMsgContent(content)
                                .addExtras(object)
                                .build())
                        .build();
            } else {
                pushPayload = PushPayload.newBuilder()
                        .setPlatform(Platform.all())
                        //指定推送的接收对象
                        .setAudience(Audience.alias(aliasList))
                        //jpush的消息
                        .setMessage(Message.newBuilder()
                                .setMsgContent(content)
                                .addExtras(object)
                                .build())
                        .build();
            }
            PushResult pushResult = mJPushClient.sendPush(pushPayload);  //发送推送对象
            if (pushResult.getResponseCode() == 200) {  //状态码等于200 为成功
                result = 1;
            }
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送通知
     * @param aliasList
     * @param OS
     * @param title
     * @param content
     * @param object
     * @param type
     * @return
     * @Author: wuyeming
     */
    public int pushDevice(List<String> aliasList, String OS, String title, String content, Map<String, String> object, String type) {
        int result = 0;
        boolean flag = false;
        if (env.equals("1")) {//如果是生产环境
            flag = true;
        }
        PushPayload pushPayload = null;
        if (type.equals("1")) {
            if (OS.equals("I")) {//指定推送IOS用户
                pushPayload = PushPayload.newBuilder()
                        .setPlatform(Platform.ios())
                        .setAudience(Audience.alias(aliasList))
                        .setNotification(Notification.newBuilder()
                                //指定当前推送的iOS通知
                                .addPlatformNotification(IosNotification.newBuilder()
                                        .setAlert(IosAlert.newBuilder().setTitleAndBody(title, "", content).build())
                                        .setContentAvailable(true)
                                        .setBadge(1)
                                        .setSound("sound.caf")
                                        .addExtras(object)
                                        .build())
                                .build())
                        //如果目标平台为IOS平台 需要在options中通过apns_production字段来指定推送环境。true表示推送生产环境，false表示要推送开发环境
                        .setOptions(Options.newBuilder()
                                .setApnsProduction(flag)
                                .build())
                        .build();
            } else if (OS.equals("A")) {//指定推送Android用户
                pushPayload = PushPayload.newBuilder()
                        .setPlatform(Platform.android())
                        .setAudience(Audience.alias(aliasList))
                        .setNotification(Notification.android(content, title, object))
                        .build();
            }
        } else {
            pushPayload = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    //指定推送的接收对象
                    .setAudience(Audience.alias(aliasList))
                    //jpush的消息
                    .setMessage(Message.newBuilder()
                            .setMsgContent(content)
                            .addExtras(object)
                            .build())
                    .build();
        }
        PushResult pushResult = null;  //发送推送对象
        try {
            pushResult = mJPushClient.sendPush(pushPayload);
            if (pushResult.getResponseCode() == 200) {  //状态码等于200 为成功
                result = 1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("notify", "1");//是否显示在通知栏 0不显示 1显示
        map.put("messageType", "messageType");//招聘信息详情页
        map.put("pushType", "pushType");
        List<String> aliasList = new ArrayList<>();
        String alias = "KTP_2_I_44946";
        aliasList.add(alias);
        JPushClientUtil.getInstance().pushDevice(aliasList, "I", "新信息", "点击查看", map, "1");
    }

}
