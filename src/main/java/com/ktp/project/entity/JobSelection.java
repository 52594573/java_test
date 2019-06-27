package com.ktp.project.entity;

import com.zm.entity.KeyContent;

import java.util.List;

/**
 * 工作筛选
 *
 * @author djcken
 * @date 2018/6/25
 */
public class JobSelection {

    private List<KeyContent> gzList;
    private List<KeyContent> experienceList;
    private List<KeyContent> teamsizeList;

    private List<KeyContent> goodSortList;//捐赠模块物品类别
    private List<KeyContent> wayList;//捐赠方式
    private List<KeyContent> postageList;//捐赠邮费谁出
    private List<KeyContent> unitList;//单位

    public List<KeyContent> getGzList() {
        return gzList;
    }

    public void setGzList(List<KeyContent> gzList) {
        this.gzList = gzList;
    }

    public List<KeyContent> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<KeyContent> experienceList) {
        this.experienceList = experienceList;
    }

    public List<KeyContent> getTeamsizeList() {
        return teamsizeList;
    }

    public void setTeamsizeList(List<KeyContent> teamsizeList) {
        this.teamsizeList = teamsizeList;
    }

    public List<KeyContent> getGoodSortList() {
        return goodSortList;
    }

    public void setGoodSortList(List<KeyContent> goodSortList) {
        this.goodSortList = goodSortList;
    }

    public List<KeyContent> getWayList() {
        return wayList;
    }

    public void setWayList(List<KeyContent> wayList) {
        this.wayList = wayList;
    }

    public List<KeyContent> getPostageList() {
        return postageList;
    }

    public void setPostageList(List<KeyContent> postageList) {
        this.postageList = postageList;
    }

    public List<KeyContent> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<KeyContent> unitList) {
        this.unitList = unitList;
    }
}
