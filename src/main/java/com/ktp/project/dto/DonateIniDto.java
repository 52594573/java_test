package com.ktp.project.dto;

import java.util.List;

/**
 * 进入捐赠选择初始化
 */
public class DonateIniDto {
    private List<String> goodSortList;//物品分类
    private List<String> donWayList;//捐赠方式类别
    private List<String> donPostageList;//邮费方式类别

    public List<String> getGoodSortList() {
        return goodSortList;
    }

    public void setGoodSortList(List<String> goodSortList) {
        this.goodSortList = goodSortList;
    }

    public List<String> getDonWayList() {
        return donWayList;
    }

    public void setDonWayList(List<String> donWayList) {
        this.donWayList = donWayList;
    }

    public List<String> getDonPostageList() {
        return donPostageList;
    }

    public void setDonPostageList(List<String> donPostageList) {
        this.donPostageList = donPostageList;
    }
}
