package com.ktp.project.dto.share;


public class ProjectFenceUserInfoDto {

    private Integer proId;
    private String pName;
    private Integer userId;// 3140,
    private String uPic;// ,
    private Integer uSex;// 0,
    private String uRealname;// 综合工长,
    private String uNicheng;// 综合工长,
    private String popTypeName;// 总包
    private Integer existReqProject;//是否加入请求的项目

    public Integer getExistReqProject() {
        return existReqProject;
    }

    public void setExistReqProject(Integer existReqProject) {
        this.existReqProject = existReqProject;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }

    public Integer getuSex() {
        return uSex;
    }

    public void setuSex(Integer uSex) {
        this.uSex = uSex;
    }

    public String getuRealname() {
        return uRealname;
    }

    public void setuRealname(String uRealname) {
        this.uRealname = uRealname;
    }

    public String getuNicheng() {
        return uNicheng;
    }

    public void setuNicheng(String uNicheng) {
        this.uNicheng = uNicheng;
    }

    public String getPopTypeName() {
        return popTypeName;
    }

    public void setPopTypeName(String popTypeName) {
        this.popTypeName = popTypeName;
    }
}
