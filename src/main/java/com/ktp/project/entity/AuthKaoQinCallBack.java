package com.ktp.project.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auth_kaoqin_callback")
public class AuthKaoQinCallBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "identity_code")
    private String identityCode;//	M	VL150	工人身份证号	原3.2.3接口接收的工人身份证号，使用DES加密

    @Column(name = "type")
    private Integer type;//	M|	FL1	数字int类型	原3.2.3接口接收的工人刷卡方向1：进；0：出

    @Column(name = "check_date")
    private String checkDate;//	M	VL40	打卡日期	原3.2.3接口接收的工人打卡时间 yyyy-MM-dd HH:mm:ss

    @Column(name = "rst_code")
    private Integer rstCode;//	M	VL40	处理结果码	系统处理数据结果码。数字int类型 0表示处理成功；其它表示失败。

    @Column(name = "rst_msg")
    private String rstMsg;//	O	VL40	返回结果描述

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "log_id")
    private Integer logId;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public Integer getRstCode() {
        return rstCode;
    }

    public void setRstCode(Integer rstCode) {
        this.rstCode = rstCode;
    }

    public String getRstMsg() {
        return rstMsg;
    }

    public void setRstMsg(String rstMsg) {
        this.rstMsg = rstMsg;
    }
}
