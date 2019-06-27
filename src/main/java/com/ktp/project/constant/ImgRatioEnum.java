package com.ktp.project.constant;

public enum ImgRatioEnum {
    FACE_PHOTO("FACE_PHOTO", 200 * 1024), IDCARD_PHOTO("IDCARD_PHOTO", 100 * 1024),
    ZS_PHOTO("ZS_PHOTO", 50 * 1024), SD_BASE64_PHOTO("顺德项目考勤图片转换", 20 * 1024),
    ZHZH_PHOTO("ZHZH_PHOTO", 1024 * 1024)
    ;
    private String type;
    private Integer size;

    ImgRatioEnum(String type, Integer size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
