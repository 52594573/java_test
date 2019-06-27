package com.ktp.project.dto.im;

import com.google.common.collect.Maps;
import com.ktp.project.util.HuanXinRequestUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by LinHon 2018/12/7
 */
public class MessageDto {

    /**
     * 消息通知标题
     */
    private String title;

    /**
     * 发送消息ID 默认系统发送
     */
    private String fromId;

    /**
     * 接收消息ID
     */
    private List toIds;

    /**
     * 目标类型 users：给用户发消息 chatgroups：给群发消息 chatrooms：给聊天室发消息
     */
    private String targetType;

    /**
     * 是否忽略
     */
    private boolean isIgnore;

    /**
     * 消息体
     */
    private Msg msg;

    /**
     * 透传消息
     */
    private Cmd cmd;

    /**
     * 扩展消息
     */
    private Map<String, Object> ext = Maps.newHashMap();

    public MessageDto(String fromId, List toIds, String targetType) {
        this.fromId = fromId;
        this.toIds = toIds;
        this.targetType = targetType;
        this.msg = new Msg("txt", HuanXinRequestUtils.DEFAULT_MSG);
        this.cmd = new Cmd("");
    }

    public MessageDto(String fromId, List toIds, String targetType, boolean isIgnore) {
        this(fromId, toIds, targetType);
        this.isIgnore = isIgnore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public List getToIds() {
        return toIds;
    }

    public void setToIds(List toIds) {
        this.toIds = toIds;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean isIgnore() {
        return isIgnore;
    }

    public void setIgnore(boolean ignore) {
        isIgnore = ignore;
    }

    public Msg getMsg() {
        return msg;
    }

    public Cmd getCmd() {
        return cmd;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public MessageDto putExt(String key, Object value) {
        ext.put(key, value);
        return this;
    }

    public void setMsg(String type, String msg) {
        this.msg.setType(type);
        this.msg.setMsg(msg);
    }

    public void setCmd(String action) {
        this.cmd.setAction(action);
    }


    /**
     * 消息体
     */
    public class Msg implements Bean {

        private String type;

        private String msg;

        public Msg(String type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        public String getType() {
            return type;
        }

        public void setType(java.lang.String type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(java.lang.String msg) {
            this.msg = msg;
        }
    }


    /**
     * 透传消息
     */
    public class Cmd implements Bean {

        private final String type = "cmd";

        private String action;

        public Cmd(String action) {
            this.action = action;
        }

        public String getType() {
            return type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public interface Bean {
    }

}
