package com.ktp.project.dto.im;

/**
 * Created by LinHon 2018/11/21
 */
public class GroupUserDto {

    private Integer userId;
    private String uPic;
    private Integer uSex;
    private String uRealname;
    private String uNicheng;
    private String popTypeName;
    private String uName;
    private Integer uCert;


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

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Integer getuCert() {
        return uCert;
    }

    public void setuCert(Integer uCert) {
        this.uCert = uCert;
    }
}
