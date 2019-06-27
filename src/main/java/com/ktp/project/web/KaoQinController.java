package com.ktp.project.web;

import com.ktp.project.entity.KaoQinLocation;
import com.ktp.project.entity.Location;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.KaoQinService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.ResponseUtil;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author djcken
 * @date 2018/6/20
 */
@Controller
@RequestMapping(value = "api/kaoqin", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class KaoQinController {

    @Autowired
    private KaoQinService kaoQinService;

    @RequestMapping(value = "track", method = RequestMethod.POST)
    @ResponseBody
    public String getSortList(@Param(value = "u_id") int u_id, @Param(value = "proId") int proId, @Param(value = "location") String location, HttpServletResponse response, HttpServletRequest request) {
        if (u_id <= 0 || proId <= 0 || TextUtils.isEmpty(location)) {
            throw new BusinessException("缺少必要参数");
        }
        String nowTime = DateUtil.format(new Date(), DateUtil.FORMAT_DATE);
        KaoQinLocation kaoQinLocation = kaoQinService.queryKaoQinLocation(u_id, proId, nowTime);
        if (kaoQinLocation == null) {
            kaoQinLocation = new KaoQinLocation();
        }
        String inLocation = changeToJson(location, kaoQinLocation.getLocation());
        kaoQinLocation.setUserId(u_id);
        kaoQinLocation.setProId(proId);
        kaoQinLocation.setLocation(inLocation);
        kaoQinLocation.setTime(nowTime);
        boolean success = kaoQinService.saveOrUpdateKaoQinLocation(kaoQinLocation);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    private String changeToJson(String location, String originLocation) {
        List<Location> originLocationList = GsonUtil.jsonToList(originLocation, Location.class);
        if (originLocationList == null) {
            originLocationList = new ArrayList<>();
        }
        List<Location> locationList = GsonUtil.jsonToList(location, Location.class);
        if (locationList != null && !locationList.isEmpty()) {
            originLocationList.addAll(locationList);
        }
        return GsonUtil.toJson(originLocationList);
    }

}
