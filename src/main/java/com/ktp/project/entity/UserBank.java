package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户银行卡
 *
 * @author djcken
 * @date 2018/6/13
 * id
 * u_id                       用户id
 * u_bankname          开户行名字
 * u_bankid                银行卡号
 * u_bankpic              银行卡照片
 * is_del                      是否删除 0正常使用 1已删除
 * u_intime                 初始化时间
 */
@Entity
@Table(name = "user_bank")
public class UserBank {

    private int id;
    private int userId;
    private int sysBankId;
    private String bankName;
    private String bankId;
    private String bankPic;
    private int isDel;
    private Date inTime;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "u_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "sb_id")
    public int getSysBankId() {
        return sysBankId;
    }

    public void setSysBankId(int sysBankId) {
        this.sysBankId = sysBankId;
    }

    @Column(name = "u_bankname")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "u_bankid")
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(name = "u_bankpic")
    public String getBankPic() {
        return bankPic;
    }

    public void setBankPic(String bankPic) {
        this.bankPic = bankPic;
    }

    @Column(name = "is_del")
    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    @Column(name = "u_intime")
    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
}
