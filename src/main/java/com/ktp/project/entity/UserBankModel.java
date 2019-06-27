package com.ktp.project.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡
 *
 * @author djcken
 * @date 2018/6/13
 */
public class UserBankModel implements Serializable {

    private int id;
    private int userId;
    private int sysBankId;
    private String bankName;
    private String bankId;
    private String bankPic;
    private int isDel;
    private Date inTime;
    private int isDefault;
    private String bankLogo;//银行logo
    private String bankBg;//背景图

    public UserBankModel(int id, int userId, int sysBankId, String bankName, String bankId, String bankPic, int isDel, Date inTime, String bankLogo, String bankBg) {
        this.id = id;
        this.userId = userId;
        this.sysBankId = sysBankId;
        this.bankName = bankName;
        this.bankId = bankId;
        this.bankPic = bankPic;
        this.isDel = isDel;
        this.inTime = inTime;
        this.bankLogo = bankLogo;
        this.bankBg = bankBg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSysBankId() {
        return sysBankId;
    }

    public void setSysBankId(int sysBankId) {
        this.sysBankId = sysBankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankPic() {
        return bankPic;
    }

    public void setBankPic(String bankPic) {
        this.bankPic = bankPic;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankBg() {
        return bankBg;
    }

    public void setBankBg(String bankBg) {
        this.bankBg = bankBg;
    }

    public void reload(UserBank userBank) {
        setId(userBank.getId());
        setSysBankId(userBank.getSysBankId());
        setBankId(userBank.getBankId());
        setBankName(userBank.getBankName());
        setBankPic(userBank.getBankPic());
        setUserId(userBank.getUserId());
        setInTime(userBank.getInTime());
        setIsDel(userBank.getIsDel());
    }
}
