package com.ktp.project.dto;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-06 15:42
 */
public class ProjectDetailDto extends ProjectDto {
    private Integer managerCounts;
    private List<Manager> managerList;

    public static class Manager {
        private Integer userId;
        private String uRealname;
        private String uNicheng;
        private String uName;
        private String uPic;
        private String zhiwu;
        private Integer poId;
        private String poName;
        private Integer uSex;

        public Integer getUserId() {
            return userId;
        }

        public Manager setUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public String getuRealname() {
            return uRealname;
        }

        public Manager setuRealname(String uRealname) {
            this.uRealname = uRealname;
            return this;
        }

        public String getuNicheng() {
            return uNicheng;
        }

        public Manager setuNicheng(String uNicheng) {
            this.uNicheng = uNicheng;
            return this;
        }

        public String getuName() {
            return uName;
        }

        public Manager setuName(String uName) {
            this.uName = uName;
            return this;
        }

        public String getuPic() {
            return uPic;
        }

        public Manager setuPic(String uPic) {
            this.uPic = uPic;
            return this;
        }

        public String getZhiwu() {
            return zhiwu;
        }

        public Manager setZhiwu(String zhiwu) {
            this.zhiwu = zhiwu;
            return this;
        }

        public Integer getPoId() {
            return poId;
        }

        public Manager setPoId(Integer poId) {
            this.poId = poId;
            return this;
        }

        public String getPoName() {
            return poName;
        }

        public Manager setPoName(String poName) {
            this.poName = poName;
            return this;
        }

        public Integer getuSex() {
            return uSex;
        }

        public Manager setuSex(Integer uSex) {
            this.uSex = uSex;
            return this;
        }
    }

    public Integer getCounts() {
        return managerCounts;
    }

    public ProjectDetailDto setCounts(Integer managerCounts) {
        this.managerCounts = managerCounts;
        return this;
    }

    public List<Manager> getManagerList() {
        return managerList;
    }

    public ProjectDetailDto setManagerList(List<Manager> managerList) {
        this.managerList = managerList;
        return this;
    }
}
