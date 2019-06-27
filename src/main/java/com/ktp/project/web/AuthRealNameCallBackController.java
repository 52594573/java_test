package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.entity.AuthKaoQinCallBack;
import com.ktp.project.service.AuthKaoQinCallBackService;
import com.ktp.project.service.ProjectService;
import com.ktp.project.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/authRealName", produces = "application/json;charset=UTF-8;")
public class AuthRealNameCallBackController {

    @Autowired
    private AuthKaoQinCallBackService callBackService;
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/attendanceCallBack", method = RequestMethod.POST)
    public String attendanceCallBack(@RequestBody AuthKaoQinCallBack callBack){
        try {
            return GsonUtil.toJson(callBackService.saveOrUpdate(callBack));
        } catch (Exception e) {
            e.printStackTrace();//ImmutableMap.of("rstCode",-1)
            return GsonUtil.toJson(ImmutableMap.of("rstCode",-1));
        }
    }
}
