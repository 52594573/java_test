package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统保存的各大银行信息
 *
 * @author djcken
 * @date 2018/6/14
 */
@Entity
@Table(name = "sys_bank")
public class SysBank {

    private int id;
    private String bankName;//银行名称
    private String bankPic1;
    private String bankPic2;
    private String bankLogo;//银行logo
    private String bankBg;//背景图

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "bank_pic1")
    public String getBankPic1() {
        return bankPic1;
    }

    public void setBankPic1(String bankPic1) {
        this.bankPic1 = bankPic1;
    }

    @Column(name = "bank_pic2")
    public String getBankPic2() {
        return bankPic2;
    }

    public void setBankPic2(String bankPic2) {
        this.bankPic2 = bankPic2;
    }

    @Column(name = "bank_logo")
    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    @Column(name = "bank_bg")
    public String getBankBg() {
        return bankBg;
    }

    public void setBankBg(String bankBg) {
        this.bankBg = bankBg;
    }
}
