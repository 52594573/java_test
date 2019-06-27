package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mall_good")
public class MallGood {

    private int id;
    private int sortId;//分类id
    private String goodName;//商品名称
    private String goodPic;//商品图片
    private String goodAdPic;//商品广告图片
    private String goodPrePic;//商品预览图
    private String goodContent;//商品内容

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ms_id")
    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Column(name = "mg_name")
    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    @Column(name = "mg_pic")
    public String getGoodPic() {
        return goodPic;
    }

    public void setGoodPic(String goodPic) {
        this.goodPic = goodPic;
    }

    @Column(name = "mg_ad_pic")
    public String getGoodAdPic() {
        return goodAdPic;
    }

    public void setGoodAdPic(String goodAdPic) {
        this.goodAdPic = goodAdPic;
    }

    @Column(name = "mg_pre_pic")
    public String getGoodPrePic() {
        return goodPrePic;
    }

    public void setGoodPrePic(String goodPrePic) {
        this.goodPrePic = goodPrePic;
    }

    @Column(name = "mg_content")
    public String getGoodContent() {
        return goodContent;
    }

    public void setGoodContent(String goodContent) {
        this.goodContent = goodContent;
    }
}
