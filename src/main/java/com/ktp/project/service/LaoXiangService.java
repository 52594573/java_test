package com.ktp.project.service;

import com.ktp.project.dao.LaoxiangDao;
import com.ktp.project.dto.LaoXiangDto.LaoXiangDto;
import com.ktp.project.util.CardUtil;
import com.zm.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaosh
 * @Date: 2018/11/12
 */
@Service
public class LaoXiangService {
    private static final Logger log = LoggerFactory.getLogger(LaoXiangService.class);
    @Autowired
    private LaoxiangDao laoxiangDao;

    /**
     * 教育视频列表
     * benefitList
     *
     * @return:
     */
    @Transactional
    public List<LaoXiangDto> getLaoXiangChatFriendList(int page, int pageSize, int userId,Date newBeginTime, Date newEndTime) {
        UserInfo user = laoxiangDao.getIDCardById(userId);
        Map<String,String> map = new HashMap<>();
        String strAddress = "";

        //截取身份证前两位作为判断老乡
        String laoxiangStr = user.getU_sfz().substring(0,2);

        page = (page - 1) * pageSize;
        //查询出老乡
        List<LaoXiangDto> dtos = laoxiangDao.getLaoXiangChatFriendList(page, pageSize,userId,laoxiangStr,newBeginTime,newEndTime);
        for(LaoXiangDto dto:dtos){
            strAddress = "";
            if(dto.getuAddress()!=null && !"".equals(dto.getuAddress())) {
                map = CardUtil.addressResolution(dto.getuAddress());


                //判断省市区为空的情况
                if(map.get("province")!=null && !"".equals(map.get("province"))){
                    strAddress += map.get("province").substring(0,map.get("province").length()-1);
                }
                if(map.get("city")!=null && !"".equals(map.get("city"))){
                    strAddress += map.get("city").substring(0,map.get("city").length()-1);
                }else if(map.get("county")!=null && !"".equals(map.get("county"))){
                    strAddress += map.get("county").substring(0,map.get("county").length()-1);
                }

                //判定是否是台湾，香港，澳门
                if("71".equals(laoxiangStr)){
                    strAddress =  "台湾省";
                }
                if("81".equals(laoxiangStr)){
                    strAddress =  "香港特别行政区";
                }
                if("72".equals(laoxiangStr)){
                    strAddress =  "澳门特别行政区";
                }
                dto.setuAddress(strAddress);
            }else {
                dto.setuAddress("");
            }
        }
        return dtos;
    }

}
