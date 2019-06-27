package com.ktp.project.dto;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-06 17:35
 */
public class ProjectDeptDto {
    private Integer id;
    private String keyName;
    private Object counts;
    private List<Dept> deptList;

    public Integer getId() {
        return id;
    }

    public ProjectDeptDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKeyName() {
        return keyName;
    }

    public ProjectDeptDto setKeyName(String keyName) {
        this.keyName = keyName;
        return this;
    }

    public Object getCounts() {
        return counts;
    }

    public ProjectDeptDto setCounts(Object counts) {
        this.counts = counts;
        return this;
    }

    public List<Dept> getDeptList() {
        return deptList;
    }

    public ProjectDeptDto setDeptList(List<Dept> deptList) {
        this.deptList = deptList;
        return this;
    }

    public static class Dept {
        private Integer poId;
        private String poName;
        private Integer uId;
        private String uRealname;
        private String uNicheng;
        private String uName;
        private Integer uSex;
        private String uPic;

        public Integer getPoId() {
            return poId;
        }

        public Dept setPoId(Integer poId) {
            this.poId = poId;
            return this;
        }

        public String getPoName() {
            return poName;
        }

        public Dept setPoName(String poName) {
            this.poName = poName;
            return this;
        }

        public Integer getuId() {
            return uId;
        }

        public Dept setuId(Integer uId) {
            this.uId = uId;
            return this;
        }

        public String getuRealname() {
            return uRealname;
        }

        public Dept setuRealname(String uRealname) {
            this.uRealname = uRealname;
            return this;
        }

        public String getuNicheng() {
            return uNicheng;
        }

        public Dept setuNicheng(String uNicheng) {
            this.uNicheng = uNicheng;
            return this;
        }

        public String getuName() {
            return uName;
        }

        public Dept setuName(String uName) {
            this.uName = uName;
            return this;
        }

        public Integer getuSex() {
            return uSex;
        }

        public Dept setuSex(Integer uSex) {
            this.uSex = uSex;
            return this;
        }

        public String getuPic() {
            return uPic;
        }

        public Dept setuPic(String uPic) {
            this.uPic = uPic;
            return this;
        }
    }
}
