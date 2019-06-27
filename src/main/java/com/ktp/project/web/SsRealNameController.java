package com.ktp.project.web;

import com.ktp.project.dto.AuthRealName.ss.*;
import com.ktp.project.util.GsonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsRealNameController {

    @RequestMapping(value = "/person/create", method = RequestMethod.POST)
    public SsRes createPerson(SsPersonVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

    @RequestMapping(value = "/person/delete", method = RequestMethod.POST)
    public SsRes deletePerson(SsDeletePersonVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

    @RequestMapping(value = "/person/find", method = RequestMethod.GET)
    public SsRes findPerson(SsFindPersonVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

    @RequestMapping(value = "/face/create", method = RequestMethod.POST)
    public SsRes creteFace(SsCreateFaceVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

    @RequestMapping(value = "/face/icCardRegist", method = RequestMethod.POST)
    public SsRes RegistFaceAndIcCardRegist(SsFaceIcCardRegistVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

    @RequestMapping(value = "/findRecords", method = RequestMethod.GET)
    public SsRes findRecords(SsFindRecordsVo vo) {
        return new SsRes(GsonUtil.toJson(vo));
    }

}
