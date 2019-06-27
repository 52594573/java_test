package com.ktp.project.util;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.entity.BaseEntity;
import com.ktp.project.entity.BaseEntity.BusinessStatus;
import com.ktp.project.entity.BaseEntity.Status;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;


/**
 * @author djcken
 * @date 2018/5/23
 */
public class ResponseUtil {

    /**
     * 创建普通的json返回
     *
     * @param object 填充content返回内容
     * @return
     */
    public static String createNormalJson(Object object) {
        return createNormalJson(object, "");
    }

    /**
     * 创建普通的json返回
     *
     * @param object 填充content返回内容
     * @return
     */
    public static String createNormalAspJson(Object object) {
        return createNormalAspJson(object, "");
    }

    /**
     * 创建普通的json返回()
     *
     * @param object 填充content返回内容
     * @param msg    如需告知具体处理结果
     * @return
     */
    public static String createNormalAspJson(Object object, String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("接口正确");
        baseModel.getBusinessStatus().setCode(100);
        baseModel.getBusinessStatus().setMsg(!TextUtils.isEmpty(msg) ? msg : "正确");
        baseModel.setContent(object);
        return baseModel.toJson();
    }

    /**
     * 创建普通的json返回
     *
     * @param object 填充content返回内容
     * @param msg    如需告知具体处理结果
     * @return
     */
    public static String createNormalJson(Object object, String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("成功");
        baseModel.getBusinessStatus().setCode(100);
        baseModel.getBusinessStatus().setMsg(!TextUtils.isEmpty(msg) ? msg : "成功");
        baseModel.setContent(object);
        return baseModel.toJson();
    }

    /**
     * 任务完成获取到相应的积分
     * 创建普通的json返回
     *
     * @param object 填充content返回内容
     * @param msg    如需告知具体处理结果
     * @return
     */
    public static String createNormalJson(Object object, String msg, String integralmsg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("成功");
        baseModel.getBusinessStatus().setCode(100);
        baseModel.getBusinessStatus().setMsg(StringUtils.isNotBlank(msg) ? msg : "成功");
        baseModel.getBusinessStatus().setIntegralmsg(integralmsg);
        baseModel.setContent(object);
        return baseModel.toJson();
    }

    /**
     * 创建正常的业务返回--无需返回其他信息的情况调用此方法
     *
     * @param msg 告知业务处理结果
     * @return
     */
    public static String createBussniessJson(String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("成功");
        baseModel.getBusinessStatus().setCode(100);
        baseModel.getBusinessStatus().setMsg(msg);
        return baseModel.toJson();
    }

    /**
     * 接口处理异常-未到逻辑层返回
     *
     * @param code
     * @param msg
     * @return
     */
    public static String createApiErrorJson(int code, String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(code);
        baseModel.getStatus().setMsg(msg);
        return baseModel.toJson();
    }

    /**
     * 创建业务处理失败返回码和处理结果
     *
     * @return
     */
    public static String createBussniessErrorJson(int code, String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("成功");
        baseModel.getBusinessStatus().setCode(code);
        baseModel.getBusinessStatus().setMsg(msg);
        return baseModel.toJson();
    }

    private static BaseEntity getDefaultModel() {
        BaseEntity baseModel = new BaseEntity();
        Status status = new Status();
        BusinessStatus businessStatus = new BusinessStatus();
        baseModel.setStatus(status);
        baseModel.setBusinessStatus(businessStatus);
        return baseModel;
    }


    /**
     * 创建判断是否已经同步成功的json返回
     *
     * @param object 填充content返回内容
     * @param msg    如需告知具体处理结果
     * @return
     */
    public static String createTongBulJson(Object object, String msg) {
        BaseEntity baseModel = getDefaultModel();
        baseModel.getStatus().setCode(10);
        baseModel.getStatus().setMsg("成功");
        baseModel.getBusinessStatus().setCode(200);
        baseModel.getBusinessStatus().setMsg(!TextUtils.isEmpty(msg) ? msg : "成功");
        baseModel.setContent(object);
        return baseModel.toJson();
    }

}
