package com.ktp.project.dto;

/**
 * @Author: wuyeming
 * @Date: 2018-10-28 上午 10:40
 */
public class PicDto {
    private Object type;
    private String picUrl;

    public Object getType() {
        return type;
    }

    public PicDto setType(Object type) {
        this.type = type;
        return this;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public PicDto setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        return this;
    }
}
