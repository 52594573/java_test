package com.ktp.project.service;

import com.ktp.project.dao.RiskWarningDao;
import com.ktp.project.dto.RiskWarning.RiskWarningDto;
import com.ktp.project.entity.MassageSwitch;
import com.zm.entity.ProOrganPer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class RiskWarningService {
    @Autowired
    RiskWarningDao riskWarningDao;

    @Transactional
    public List<RiskWarningDto> getList(int userId, int proId) throws Exception {
        //该用户在该项目中的角色查询
        ProOrganPer proOrganPer = riskWarningDao.getRoleId(userId,proId);
        if(proOrganPer==null){
            throw new Exception("用户不在该项目中");
        }

        //查看该用户是否在massage_switch表中，不存在就添加初始化数据
        List<MassageSwitch> list = riskWarningDao.getMassageSwitchList(userId,proId);

        //如果为空初始化数据
        if(list==null || list.size()==0){
            if(!setMassageSwitch(userId,proId)){
                throw new Exception();
            }
        }

        //先不用Massage_config表
       /* if(list==null || list.size()==0){
            //查询出配置表massager_config中的配置
            List<MassageConfig> configs = riskWarningDao.getMassageConfigList();
            //为该用户配置初始化数据
            for(MassageConfig config:configs){
                //判断初始化角色，写死了？
                if(){

                }
            }
        }*/
       int roleId = 0;
       //管理层
       if(proOrganPer.getPopType()==7){
           if (proOrganPer.getPType() == 117 || proOrganPer.getPType() == 181
                   || proOrganPer.getPType() == 180 || proOrganPer.getPType() == 118){
               roleId = 1;
           }
       }

        return riskWarningDao.getList(userId,proId,roleId);
    }

    @Transactional
    public boolean setStatus(int id, int status) throws Exception {
        if(riskWarningDao.setStatus(id,status)==1){
            return true;
        }else{
            return false;
        }
    }

    //初始化写死数据
    public boolean setMassageSwitch(int userId,int proId){
        MassageSwitch massageSwitch1 = new MassageSwitch();
        MassageSwitch massageSwitch2 = new MassageSwitch();
        MassageSwitch massageSwitch3 = new MassageSwitch();
        MassageSwitch massageSwitch4 = new MassageSwitch();

        massageSwitch1.setmAppId(1);
        massageSwitch1.setmName("考勤预警信息");
        massageSwitch1.setmProId(proId);
        massageSwitch1.setmTypeId(1);
        massageSwitch1.setmUserId(userId);
        massageSwitch1.setmRoleId(0);
        massageSwitch1.setmStatus(1);
        if(!riskWarningDao.insertMassageSwitch(massageSwitch1)){
            return false;
        }

        massageSwitch2.setmAppId(1);
        massageSwitch2.setmName("质量预警信息");
        massageSwitch2.setmProId(proId);
        massageSwitch2.setmTypeId(2);
        massageSwitch2.setmUserId(userId);
        massageSwitch2.setmRoleId(0);
        massageSwitch2.setmStatus(1);
        if(!riskWarningDao.insertMassageSwitch(massageSwitch2)){
            return false;
        }

        massageSwitch3.setmAppId(1);
        massageSwitch3.setmName("安全文明预警信息");
        massageSwitch3.setmProId(proId);
        massageSwitch3.setmTypeId(3);
        massageSwitch3.setmUserId(userId);
        massageSwitch3.setmRoleId(0);
        massageSwitch3.setmStatus(1);
        if(!riskWarningDao.insertMassageSwitch(massageSwitch3)){
            return false;
        }
        massageSwitch4.setmAppId(1);
        massageSwitch4.setmName("管理人员考核预警信息");
        massageSwitch4.setmProId(proId);
        massageSwitch4.setmTypeId(4);
        massageSwitch4.setmUserId(userId);
        massageSwitch4.setmRoleId(1);
        massageSwitch4.setmStatus(1);
        if(!riskWarningDao.insertMassageSwitch(massageSwitch4)){
            return false;
        }
        return true;
    }


}
