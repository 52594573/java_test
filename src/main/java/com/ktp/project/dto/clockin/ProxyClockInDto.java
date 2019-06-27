package com.ktp.project.dto.clockin;

import java.util.List;

public class ProxyClockInDto {
    private String poName;//部门名称
    private Integer poId;//班组id
    private Integer proId;//项目id
    private List<PoClockIn> poClockInList;

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public List<PoClockIn> getPoClockInList() {
        return poClockInList;
    }

    public void setPoClockInList(List<PoClockIn> poClockInList) {
        this.poClockInList = poClockInList;
    }

    public ProxyClockInDto() {
    }
}
