package com.ktp.project.service.realName.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.JmAndGsxProjectRequestBody;
import com.ktp.project.dto.AuthRealName.JmBankCardDto;
import com.ktp.project.dto.AuthRealName.JmDivisionRecordDto;
import com.ktp.project.dto.AuthRealName.JmPayrollRecordDto;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthKaoQinCallBackService;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.DESUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.RealNameUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;

public abstract class JmAuthRealNameAbstractService  implements AuthRealNameApi {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected AuthRealNameLogService logService;
    @Autowired
    protected QueryChannelDao queryChannelDao;
    @Autowired
    protected AuthKaoQinCallBackService authKaoQinCallBackService;
    @Autowired
    protected ProOrganDAO proOrganDAO;

    /**
     * 同步工人公司银行卡账号
     * @param pId
     * @param uId
     */
    public void savePopBankCard(Integer pId, Integer uId){
        AuthRealNameLog realNameLog = null;
        try {
            ProjectEnum projectEnum = EnumMap.projectEnumMap.get(pId);
            String enumKey = projectEnum.getKey();
            String url = projectEnum.getBaseRequest() + RealNameConfig.JMP_AND_GSX_BANKCARD_URL;
            realNameLog = logService.createBaseBean(pId, uId, url, "同步工人银行卡信息");
            List<JmBankCardDto> jmBankCardDtos = getJmBankCardDtos(pId, uId);
            for (JmBankCardDto dto : jmBankCardDtos) {
                try {
                    RealNameUtil.checkRequestParams(dto);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                    continue;
                }
                dto.getBankPic().add(DESUtil.DESEncode(dto.getBankPics(), enumKey));
                dto.setBankPics(null);
                dto.setPayRollBankCardNumber(DESUtil.DESEncode(dto.getPayRollBankCardNumber(), enumKey));
                dto.setPersonIdentity(DESUtil.DESEncode(dto.getPersonIdentity(), enumKey));
                JmAndGsxProjectRequestBody body = JmAndGsxProjectRequestBody.newInstance(dto);
                String s = JSON.toJSONString(body);
                realNameLog.setReqBody(GsonUtil.toJson(body));
                String result = HttpClientUtils.post(url, GsonUtil.toJson(body), "application/json", new HashMap<>());
                RealNameUtil.JMproResIfSussess(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assert realNameLog != null;
            realNameLog.setIsSuccess(0);
            realNameLog.setResMsg(e.getMessage());
            log.error(e.getMessage());
        }finally {
            logService.saveOrUpdate(realNameLog);
        }
    }

    private List<JmBankCardDto> getJmBankCardDtos(Integer pId, Integer uId) {
        String sql = "SELECT pop.`popBankid` AS payRollBankCardNumber, ub.u_bankname AS payRollBankName, ui.u_realname AS userName, ui.u_name AS phone, ui.u_realname AS personName " +
                " , pop.`popBankid` AS personIdentity, po.`pro_id` AS projectId, ub.`u_bankpic` bankPics " +
                " FROM pro_organ_per pop " +
                " LEFT JOIN user_info ui ON pop.`user_id` = ui.id " +
                " LEFT JOIN `user_bank` ub ON pop.`popBankid` = ub.u_bankid " +
                " LEFT JOIN pro_organ po ON pop.po_id = po.id " +
                " LEFT JOIN project p ON p.id = po.`pro_id` " +
                "WHERE p.id = ? ";
        List<Object> params = Lists.newArrayList(pId);
        if (uId != null && uId > 0){
            sql += "and pop.user_id = ? ";
            params.add(uId);
        }
        sql += " GROUP BY pop.`user_id` ";
        List<JmBankCardDto> jmBankCardDtos = null;
        try {
            jmBankCardDtos = queryChannelDao.selectManyAndTransformer(sql, params, JmBankCardDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s, 用户ID:%s 查询用户银行卡号信息失败", pId, uId));
        }
        return jmBankCardDtos;
    }


    public void synSalaryRecord(Integer poId, Integer uId) {
        AuthRealNameLog realNameLog = null;
        try {
            ProOrgan organ = proOrganDAO.findById(poId);
            ProjectEnum projectEnum = EnumMap.projectEnumMap.get(organ.getProId());
            String url = projectEnum.getBaseRequest() + RealNameConfig.JMP_AND_GSX_SALARYRECORD_URL;
            realNameLog = logService.createBaseBean(poId, uId, url, "同步工资发放记录");
            JmPayrollRecordDto salaryRecordDto = getSalaryRecordDto(poId);
            salaryRecordDto.setProjectId(organ.getProId());
            salaryRecordDto.setPayWay(1);
            salaryRecordDto.setPayBankCardCode("6217003210013678798");
            salaryRecordDto.setPayBankCardCode(DESUtil.DESEncode(salaryRecordDto.getPayBankCardCode(), projectEnum.getKey()));
            List<JmPayrollRecordDto.UserInfo> userInfos = queryWorkerSalaryRecord(poId, uId);
            for (JmPayrollRecordDto.UserInfo userInfo : userInfos) {
                userInfo.setIsBackPay(0);
                userInfo.setIdentityCode(DESUtil.DESEncode(userInfo.getIdentityCode(), projectEnum.getKey()));
            }
            salaryRecordDto.setUserInfoList(userInfos);
            JmAndGsxProjectRequestBody body = JmAndGsxProjectRequestBody.newInstance(salaryRecordDto);
            realNameLog.setReqBody(GsonUtil.toJson(body));
            String result = HttpClientUtils.post(url, GsonUtil.toJson(body), "application/json", new HashMap<>());
            RealNameUtil.JMproResIfSussess(result);
        } catch (Exception e) {
            e.printStackTrace();
            assert realNameLog != null;
            realNameLog.setResMsg(e.getMessage());
            realNameLog.setIsSuccess(0);
            log.error(e.getMessage());
        } finally {
            logService.saveOrUpdate(realNameLog);
        }
    }

    private JmPayrollRecordDto getSalaryRecordDto(Integer poId) {
        String sql = "SELECT DATE_FORMAT(wl.`w_intime`, '%Y-%m-%d') AS payDate, po.`po_name` AS teamName " +
                "FROM wage_list wl " +
                "  LEFT JOIN pro_organ po ON wl.`po_id` = po.`id` " +
                "WHERE (po.`id` = ? " +
                "  AND wl.`w_month` = ? " +
                "  AND wl.`w_year` = ?) limit 1 ";
        List<Object> params = Lists.newArrayList(poId, 1, 2018);
        JmPayrollRecordDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, params, JmPayrollRecordDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,查询SalaryRecordDto失败", poId));
        }
        return dto;
    }

    private List<JmPayrollRecordDto.UserInfo> queryWorkerSalaryRecord(Integer poId, Integer uId) {
        String sql = "SELECT ui.`u_sfz` AS identityCode, wl.w_all AS needMoney, wl.w_yf AS realPayMoney  " +
                "FROM wage_worker ww  " +
                "  LEFT JOIN wage_list wl ON ww.`wl_id` = wl.`id`  " +
                "  LEFT JOIN pro_organ_per pop ON pop.`user_id` = ww.`work_uid`  " +
                "  LEFT JOIN user_info ui ON ui.`id` = ww.`work_uid`  " +
                "  LEFT JOIN pro_organ po ON pop.po_id = po.id  " +
                "WHERE (po.`id` = ?  " +
                "  AND wl.`w_month` = ?  " +
                "  AND wl.`w_year` = ? %s )  " +
                "GROUP BY ww.`work_uid` ";
        List<Object> params = Lists.newArrayList(poId, 1, 2018);

        if(uId != null && uId > 0){
            sql =  String.format(sql, " AND ww.`work_uid` = ? ");
            params.add(uId);
        }
        List<JmPayrollRecordDto.UserInfo> userInfos = null;
        try {
            userInfos = queryChannelDao.selectManyAndTransformer(sql, params, JmPayrollRecordDto.UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,用户ID:%s.查询工人工资错误", poId, uId));
        }
        return userInfos;
    }


    public static void main(String[] args) {

//        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(174);
//        JmDivisionRecordDto dto = new JmDivisionRecordDto();
//        dto.setDataType(1);
//        dto.setPayBankCardCode(DESUtil.DESEncode("", projectEnum.getKey()));
//        dto.setPayMoney();
//        dto.setReceiveBankCardCode(DESUtil.DESEncode("", projectEnum.getKey()));
//        dto.setReceiveCompanySocialCode(DESUtil.DESEncode("", projectEnum.getKey()));
//        dto.setTradeCode();
//        dto.setTradeDate();
//        dto.getTradePicUrl().add(DESUtil.DESEncode("", projectEnum.getKey()));
    }

}
