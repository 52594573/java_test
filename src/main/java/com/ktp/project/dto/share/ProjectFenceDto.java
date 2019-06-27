package com.ktp.project.dto.share;

import java.util.ArrayList;
import java.util.List;

public class ProjectFenceDto {
    private Integer proId;
    private String pName;
    private List<ProjectFenceUserInfoDto> proList = new ArrayList<>(0);

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public List<ProjectFenceUserInfoDto> getProList() {
        return proList;
    }

    public void setProList(List<ProjectFenceUserInfoDto> proList) {
        this.proList = proList;
    }
}
