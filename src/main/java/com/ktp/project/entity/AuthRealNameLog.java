package com.ktp.project.entity;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auth_real_name_log")
public class AuthRealNameLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "req_url")
    private String reqUrl;//请求类型

    @Column(name = "req_body")
    private String reqBody;

    @Column(name = "is_success")
    private Integer isSuccess;//是否成功

    @Column(name = "res_msg")
    private String resMsg;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "remark")
    private String remark;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        if (StringUtils.isNotBlank(reqBody) && reqBody.length() > 500){
            reqBody = reqBody.substring(0,500) + "...";
        }
        this.reqBody = reqBody;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        if (StringUtils.isNotBlank(resMsg) && resMsg.length() > 248){
            resMsg = resMsg.substring(0,248) + "...";
        }
        this.resMsg = resMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
