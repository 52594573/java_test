package com.ktp.project.service;

import com.ktp.project.dao.EducationDao;
import com.ktp.project.dto.education.*;
import com.ktp.project.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 公益活动
 * @Author: liaosh
 * @Date: 2018/8/22 0022
 */
@Service
public class EducationService {
    private static final Logger log = LoggerFactory.getLogger(EducationService.class);
    @Autowired
    private EducationDao educationDao;

    /**
     * 教育视频列表
     * benefitList
     *
     * @return:
     */
    @Transactional
    public List<EducationListDto> getEducationList(int page, int pageSize, int userId,int typeId,String searchKey) {
        page = (page - 1) * pageSize;
        List<EducationListDto> dtos = educationDao.getEducationList(page, pageSize,userId,typeId,searchKey);

        return dtos;
    }

    /**
     * 获取视频详细信息
     * benefitList
     *
     * @return:
     */
    @Transactional
    public EducationDetailDto getVideoDetail(int id, int userId) {
        EducationDetailDto dto= educationDao.getEducationList(id,userId);
        //获取标签
        List<EducationLabelEntity> labels = educationDao.getEducationLabelListById(dto.getId());
        List<String> list = new ArrayList<>();
        //多个标签，生成list
        for(EducationLabelEntity label:labels){
            list.add(label.getName());
        }
        dto.setvLabel(list);

        return dto;
    }

    /**
     * 获取视频类型
     * key_content
     *
     * @return:
     */
    @Transactional
    public  List<EducationKeyContentDto> getvideoType() {
        List<EducationKeyContentDto> dto= educationDao.getvideoType();
        return dto;
    }

    @Transactional
    public  Long saveVideoLike(Integer id,Integer userId,Integer status) throws Exception {
        Long likeNum = 0l;
        EductionLikeEntity entity = educationDao.getEductionLike(id,userId);
        //是否已经点赞过
        if(entity==null || "".equals(entity)){
            //没点赞过，添加点赞
            if(educationDao.insertEductionLike(id,userId)!=1){
                throw new Exception("点赞异常");
            }
            //获取点赞数量
            likeNum = educationDao.queryEducationLikeNumById(id).longValue();
            return likeNum;
        }else {
            //更新点赞信息
            if(educationDao.updateEductionLike(id,userId,status)!=1){
                throw new Exception("点赞异常");
            }
            //点赞数量
            likeNum = educationDao.queryEducationLikeNumById(id).longValue();
            return likeNum;
        }

    }
        /**
        *添加评论
        */
    @Transactional
    public  Boolean saveVideoComment(Integer id,Integer userId,String cContent) {
        boolean fls = false;
        //没点赞过，添加点赞
        if(educationDao.saveVideoComment(id,userId,cContent,new Date())==1){
            fls = true;
        }
        return fls;
    }
        /**
         *观看视频接口
         */
    @Transactional
    public  Boolean saveVideoLook(Integer id,Integer userId,Integer tLong) {
        boolean fls = false;
        //获取该用户是否看过该视频
        EductionLookEntity entity = educationDao.getEductionLook(id,userId);
        //是否已经观看该视频
        if(entity==null || "".equals(entity)){
            //没观看该视频，添加观看该视频
            if(educationDao.insertEductionLook(id,userId,tLong)==1){
                fls = true;
            }
        }else {
            //更新点赞信息
            if(educationDao.updateEductionLook(id,userId,entity.getlNum()+1,tLong)==1){
                fls = true;
            }
        }
        return fls;
    }

    /**
     * 获取评论列表
     *
     * @return:
     */
    @Transactional
    public  List<EducationCommentDto> getVideoComment(int id,int page,int pageSize) {
        page = (page - 1) * pageSize;
        List<EducationCommentDto> dto= educationDao.getVideoComment(id,page,pageSize);
        return dto;
    }

    /**
     * 分享视频接口
     *
     * @return:
     */
    @Transactional
    public EducationH5Dto getEducationShareH5ById(int id) {
        EducationH5Dto dto= educationDao.getEducationShareH5ById(id);
        if(dto==null){
            return null;
        }
        List<EducationLabelEntity> label = educationDao.getEducationLabelListById(id);
        List<EducationH5CommentDto> comments = educationDao.getEducationCommentListById(id);
        for(EducationH5CommentDto comment:comments){

            //头像地址处理
            if(comment.getuUrl()!=null && !"".equals(comment.getuUrl()) && comment.getuUrl().indexOf("uploa")==-1){
                if(comment.getuUrl().indexOf("http")==-1){
                    comment.setuUrl("https://t.ktpis.com"+comment.getuUrl());
                }
            }else {
                if(comment.getuSex()!=null){
                if(comment.getuSex()==1){//男
                    comment.setuUrl("https://images.ktpis.com/images/pic/20181122154936224290102181.png");
                }
                    if(comment.getuSex()==2){//女
                        comment.setuUrl("https://images.ktpis.com/images/pic/20181122154936205290107119.png");
                    }
                }
            }
        }
        dto.setLabel(label);
        if(comments.size()>=2){
            dto.setComments(comments.subList(0, 2));
        }else{
            dto.setComments(comments);
        }

        return dto;
    }

}
