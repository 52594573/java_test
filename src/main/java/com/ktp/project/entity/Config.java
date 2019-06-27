package com.ktp.project.entity;

/**
 * 配置信息
 *
 * @author djcken
 * @date 2018/7/16
 */
public class Config {

    private boolean appstoreAudit;//是否正在审核
    private String auditVersion;//正在审核的版本号

    public boolean isAppstoreAudit() {
        return appstoreAudit;
    }

    public void setAppstoreAudit(boolean appstoreAudit) {
        this.appstoreAudit = appstoreAudit;
    }

    public String getAuditVersion() {
        return auditVersion;
    }

    public void setAuditVersion(String auditVersion) {
        this.auditVersion = auditVersion;
    }
}
