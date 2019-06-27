package com.ktp.project.dto.clockin;

/**
 * Created by LinHon 2018/12/6
 */
public class UserInfoDto {

    private int userId;
    private String uPic;
    private Integer uSex;
    private String uRealname;
    private String uNicheng;
    private String popTypeName;
    private String uName;
    private String uCertPic;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
        this.uSex = this.uSex == null ? 0 : uSex;
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

    public String getuCertPic() {
        return uCertPic;
    }

    public void setuCertPic(String uCertPic) {
        this.uCertPic = uCertPic;
    }
}
