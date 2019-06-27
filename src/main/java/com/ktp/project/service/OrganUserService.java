package com.ktp.project.service;

import com.google.common.base.Strings;
import com.ktp.project.dao.OrganDao;
import com.ktp.project.dao.ProOrganPerDao;
import com.ktp.project.dao.ProjectDao;
import com.ktp.project.dto.ProOrganCountDto;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LinHon 2018/11/20
 */
@Service
@Transactional
public class OrganUserService {

    @Autowired
    private OrganDao organDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProOrganPerDao perDao;

    @Autowired
    private ProjectDao projectDao;


    /**
     * 设置班组长
     *
     * @param
     */
    public void setOrganLeader(int pro_id,int po_id,int user_id) throws Exception {

        //Project project = projectDao.queryOne(pro_id);



        //该班组是否有班组长
        ProOrganPer per =  perDao.queryProOrganByPoId(po_id);
        if(per!=null){
            //设置该班组长为普通工人
            per.setPopType(4);
            if(!perDao.updateProOrganPer(per)){
                throw new Exception("更新班组长异常");
            }
        }else{
            /**
             *是否存在工人
             */
            per = perDao.queryProOrganBuMenByPoId(po_id);
            if(per==null){
                throw new Exception("机构查询不到对应人员或者该人员退场机构");
            }
        }

        ProOrganPer per1 = perDao.queryProOrganByUserId(po_id,user_id);
        if(per1!=null){
            //设置该为班组长
            per1.setPopType(8);
            if(!perDao.updateProOrganPer(per1)){
                throw new Exception("更新班组长异常");
            }
        }
        //负责人修改
        ProOrgan proOrgan = organDao.queryOne(po_id);
        proOrgan.setPoFzr(user_id);
        organDao.saveOrUpdate(proOrgan);
    }



}
