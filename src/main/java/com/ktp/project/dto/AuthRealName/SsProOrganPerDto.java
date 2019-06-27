package com.ktp.project.dto.AuthRealName;

import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.GsonUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 山水项目添加工人表
 */
public class SsProOrganPerDto {

    private String pass;
    private Person person;
    private String deviceKey;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public static SsProOrganPerDto newInstance(String idcardNum, String name){
        if (StringUtils.isBlank(idcardNum) || StringUtils.isBlank(name)){
            throw new BusinessException("姓名和身份证不能为空");
        }
        Person person = new Person();
        person.setIdcardNum(idcardNum);
        person.setName(name);
        SsProOrganPerDto dto = new SsProOrganPerDto();
        dto.setDeviceKey(RealNameConfig.SS_DEVICEKEY);
        dto.setPass(RealNameConfig.SS_TOKEN);
        dto.setPerson(person);
        return dto;
    }

    public static class Person{
        private String idcardNum;
        private String name;

        public String getIdcardNum() {
            return idcardNum;
        }

        public void setIdcardNum(String idcardNum) {
            this.idcardNum = idcardNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
