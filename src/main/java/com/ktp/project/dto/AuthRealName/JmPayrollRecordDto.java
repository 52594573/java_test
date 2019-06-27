package com.ktp.project.dto.AuthRealName;

import java.math.BigDecimal;
import java.util.List;

public class JmPayrollRecordDto {

    private Integer projectId;
    private String payBankCardCode;//	M	VL40	银行卡号	公司发放工资的银行卡号。使用DES加密
    private String teamName;//	M	VL40	班组名称	拨款班组名称
    private String payDate;//	M	VL40	发放日期	格式yyyy-MM-dd
    private Integer payWay;//	M	FL1	数字int类型	发放渠道。1银行转账；2银行代发；3现金发放
    private List<UserInfo> userInfoList;//	M		List<userInfo>	具体的发放工资的工人信息，一次性最多50条工人数据，并且不能全是补发的明细信息。

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPayBankCardCode() {
        return payBankCardCode;
    }

    public void setPayBankCardCode(String payBankCardCode) {
        this.payBankCardCode = payBankCardCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public static class UserInfo{
        private String identityCode;//	M	VL150	工人身份证号	工资发放工人身份证号，使用DES加密
        private BigDecimal needMoney;//	M	VL100	数字decimal类型	应发金额.单位：元。最多保留两位小数位。
        private BigDecimal realPayMoney;//	M	VL100	数字decimal类型	实发金额。单位：元。最多保留两位小数位。
        private Integer isBackPay=0;//	M	FL1	数字int类型	是否补发。1是，0否。
        private String backPayMonth;//	O	VL100		补发月份，当为补发时，必填。格式yyyy-MM

        public String getIdentityCode() {
            return identityCode;
        }

        public void setIdentityCode(String identityCode) {
            this.identityCode = identityCode;
        }

        public BigDecimal getNeedMoney() {
            return needMoney;
        }

        public void setNeedMoney(BigDecimal needMoney) {
            this.needMoney = needMoney;
        }

        public BigDecimal getRealPayMoney() {
            return realPayMoney;
        }

        public void setRealPayMoney(BigDecimal realPayMoney) {
            this.realPayMoney = realPayMoney;
        }

        public Integer getIsBackPay() {
            return isBackPay;
        }

        public void setIsBackPay(Integer isBackPay) {
            this.isBackPay = isBackPay;
        }

        public String getBackPayMonth() {
            return backPayMonth;
        }

        public void setBackPayMonth(String backPayMonth) {
            this.backPayMonth = backPayMonth;
        }
    }
}
