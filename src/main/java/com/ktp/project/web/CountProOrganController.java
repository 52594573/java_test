package com.ktp.project.web;

import com.ktp.project.service.OrganService;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/proOrgan", produces = "application/json;charset=UTF-8;")
public class CountProOrganController {

    @Autowired
    private OrganService organService;

    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public String count(@RequestParam("projectId") Integer projectId, @RequestParam("userId") Integer userId){
        try {
            return ResponseUtil.createNormalJson(organService.count(projectId, userId));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
