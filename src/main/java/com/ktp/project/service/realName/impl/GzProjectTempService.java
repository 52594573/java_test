package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.GzKaoQinDto;
import com.ktp.project.dto.AuthRealName.GzProOrganPerDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GzProjectTempService {

    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private QueryChannelDao queryChannelDao;


    public Page pageProOrganPerByProOrganId(Integer proOrganId, Page page) {

        String sql = "select %s from ( " +
                "SELECT p.`id` AS projectId, p.`p_name` AS projectName, po.`id` AS proOrganId, po.`po_name` AS proOrganName, pop.`user_id` AS userId " +
                " , ui.`u_realname` AS name, us.us_age AS age, ui.`u_name` AS phone, ui.`u_sfz` AS idCard, ui.`u_sex` AS sex " +
                " , us.us_nation AS nation, ui.u_birthday AS birthdayDate, us.us_address AS address, ui.u_cert_pic AS headImg " +
                "FROM `pro_organ_per` pop " +
                " LEFT JOIN `pro_organ` po ON pop.`po_id` = po.`id` " +
                " LEFT JOIN project p ON p.`id` = po.`pro_id` " +
                " LEFT JOIN user_info ui ON pop.`user_id` = ui.`id` " +
                " LEFT JOIN user_sfz us ON pop.`user_id` = us.`u_id` " +
                "WHERE p.`id` = 60  " +
                "GROUP BY po.id, pop.`user_id` ) temp %s ";
        String query = " ";
        if (proOrganId != null){
            query = " temp.proOrganId = " + proOrganId +" ";
        }
        Page<GzProOrganPerDto> perDtoPage = null;
        try {
            perDtoPage = sqlPage(sql,query, page,Lists.newArrayList(), GzProOrganPerDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询班组工人信息失败!");
        }
        return perDtoPage;
    }


    public Page workerKaoQin( Page page) {
        String baseSql =
                "select %s from ("  +
                "SELECT p.`id` AS projectId, p.`p_name` AS projectName, po.`id` AS proOrganId, po.`po_name` AS proOrganName, pop.`user_id` AS userId " +
                "  , ui.`u_realname` AS name, kq.id kqoQinId " +
                "  , CASE kq.`k_state` " +
                "    WHEN 1 THEN 0 " +
                "    WHEN 2 THEN 1 " +
                "    WHEN 3 THEN 0 " +
                "    WHEN 4 THEN 1 " +
                "    WHEN 5 THEN 0 " +
                "    WHEN 6 THEN 1 " +
                "  END AS kState, kq.k_time AS kqTime, kq.k_pic AS kqPic " +
                "FROM kaoqin60 kq " +
                "  LEFT JOIN user_info ui ON kq.`user_id` = ui.`id` " +
                "  LEFT JOIN `pro_organ_per` pop ON pop.`user_id` = kq.`user_id` " +
                "  LEFT JOIN `pro_organ` po ON pop.`po_id` = po.`id` " +
                "  LEFT JOIN project p ON p.`id` = kq.`pro_id` " +
                "WHERE kq.`k_time` BETWEEN ? AND ? ) temp order by temp.kqoQinId desc ";
        Page<GzProOrganPerDto> perDtoPage = null;
        try {
            Date firstTime = getTodayFirstTime();
            perDtoPage = sqlPage(baseSql,"", page, Lists.newArrayList(firstTime, new Date()), GzKaoQinDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询班组工人信息失败!");
        }
        return perDtoPage;
    }

    public Page listProOrgan(Page page) {
        String sql = "";



        return null;
    }

    /**
     * 分页查询SQL
     * @param baseSql
     * @param page
     * @return
     */
    public Page sqlPage(String baseSql,String querySql,  Page page, List<Object> params, Class clazz){
        //获取总记录数
        String querySqlTotal = createBaseSql(baseSql, "COUNT(1)", querySql);
        Long total = queryChannelDao.selectCount(querySqlTotal, params );
        params.add( (page.getPageNo() - 1) * page.getPageSize() );; params.add(page.getPageSize());
        String querySqlResult = createBaseSql(baseSql, "*", querySql) +  " LIMIT ? , ?";
        List result =queryChannelDao.selectManyAndTransformer(querySqlResult, params, clazz);
        page.setTotalCount(total.intValue()); page.setResult(result);
        return page.builderPage();
    }

    public  String createBaseSql(String sql, String... params){
        return String.format(sql,params);
    }

    private Date getTodayFirstTime() throws ParseException {
        String format = formatDate.format(new Date());
        return formatDate.parse(format);
    }

}
