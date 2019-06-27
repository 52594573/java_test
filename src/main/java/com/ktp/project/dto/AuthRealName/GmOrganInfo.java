package com.ktp.project.dto.AuthRealName;

/**
 * 高明项目对开太平项目
 */
public class GmOrganInfo {
    //主键
    private Integer id;
    //部门负责人
    private Integer po_fzr;
    //班组类型，关联工种id
    private Integer po_gzid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPo_fzr() {
        return po_fzr;
    }

    public void setPo_fzr(Integer po_fzr) {
        this.po_fzr = po_fzr;
    }

    public Integer getPo_gzid() {
        return po_gzid;
    }

    public void setPo_gzid(Integer po_gzid) {
        this.po_gzid = po_gzid;
    }
}
