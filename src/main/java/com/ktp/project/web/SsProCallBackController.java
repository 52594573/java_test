package com.ktp.project.web;

import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.dto.AuthRealName.SsProOrganPerDto;
import com.zm.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SsProCallBackController {

    @Autowired
    private UserInfoDao userInfoDao;

//    @RequestMapping(value = "/person/create", method = RequestMethod.POST)
//    public Map<String, Object> createProOrganPer(@RequestBody SsProOrganPerDto dto){
//        Map<String, Object> result = new HashMap<>();
//
//        SsProOrganPerDto.Person person = dto.getPerson();
//        UserInfo user = userInfoDao.getUserInfoByCardId(person.getIdcardNum());
//        if (user != null){
//            result.put("result", 1);
//            result.put("success", true);
//            result.put("data", person);
//        }else {
//            result.put("result", 1);
//            result.put("success", true);
//            result.put("data", person);
//        }
//
//        return result;
//    }
}
