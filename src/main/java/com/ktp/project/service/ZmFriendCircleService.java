package com.ktp.project.service;

import com.ktp.project.dao.*;
import com.ktp.project.dto.LikeCommentDto;
import com.ktp.project.dto.PicDto;
import com.ktp.project.dto.ZmFriendCircleDto;
import com.ktp.project.dto.circledto.CircleDto;
import com.ktp.project.dto.circledto.CommentDto;
import com.ktp.project.dto.circledto.LikeDto;
import com.ktp.project.entity.PushLogCircle;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.JPushClientUtil;
import com.zm.entity.KeyContent;
import com.zm.entity.KeyContentDAO;
import com.zm.entity.UserInfo;
import com.zm.entity.UserInfoDAO;
import com.zm.friendCircle.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: wuyeming
 * @Date: 2018-10-17 下午 15:36
 */
@Service("circleService")
@Transactional
public class ZmFriendCircleService {

    private final static Integer EMPLOYER_INDEX = 1;//招工
    private final static Integer EMPLOYEE_INDEX = 2;//找工
    private final static Integer SHUOSHUO_INDEX = 3;//说说
    private final static Integer NETWORK_INDEX = 4;//网络文章
    private final static Integer SHUOSHUO_PICTURE = 0;//说说-图片
    private final static Integer SHUOSHUO_VIDEO = 1;//说说-视频
    private final static Integer LIKE_INDEX = 5;//点赞
    private final static Integer COMMENT_INDEX = 6;//评论
    private final static Integer STATUS_PASSED = 0;//审核状态-0审核中 1审核通过

    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private ZmFriendCircleDao circleDao;
    @Autowired
    private ZmFriendCircleLikeDao likeDao;
    @Autowired
    private ZmFriendCircleCommentDao commentDao;
    @Autowired
    private ZmEmployerCircleDao employerDao;
    @Autowired
    private ZmEmployeeCircleDao employeeDao;
    @Autowired
    private ZmFriendCircleShuoshuoDao shuoshuoDao;
    @Autowired
    private ZmNetworkArticleDao networkDao;
    @Autowired
    private ZmFriendCircleAlbumDao albumDao;
    @Autowired
    private ZmRefusedLookDao lookDao;
    @Autowired
    private PushLogCircleDao pushLogCircleDao;
    @Autowired
    private KeyContentDAO keyContentDAO;
    @Autowired
    private UserInfoDao userInfoDao;

    @Value("${jpush.env}")
    private String env;


    /**
     * 查询分享
     *
     * @return java.util.List<com.ktp.project.dto.ZmFriendCircleDto>
     * @Author: wuyeming
     * @params: [index, userId, begin, length] index=0全部  index=1说说  index=2招工  index=3找工  index=4网络分享
     * @Date: 2018-10-17 下午 17:05
     */
    public List<ZmFriendCircleDto> getContent(Integer index, Integer userId, Integer begin, Integer length) {
        //查询分享
        List<ZmFriendCircleDto> list = null;
        try {
            list = circleDao.getContent(index, userId, begin, length);
            getLikeAndCommentAndPic(list, userId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    /**
     * 查询点赞和评论以及图片
     *
     * @return void
     * @Author: wuyeming
     * @params: [list, userId]
     * @Date: 2018-10-18 下午 14:23
     */
    private void getLikeAndCommentAndPic(List<ZmFriendCircleDto> list, Integer userId) {
        for (ZmFriendCircleDto zmFriendCircleDto : list) {
            UserInfo userInfo = userInfoDAO.findById(zmFriendCircleDto.getUserId());
            zmFriendCircleDto.setUserInfo(userInfo);
            //查询点赞
            List<ZmFriendCircleLike> likeList = likeDao.getLikeList(zmFriendCircleDto.getId(), Integer.parseInt(zmFriendCircleDto.gettIndex() + ""));
            for (ZmFriendCircleLike zmFriendCircleLike : likeList) {
                UserInfo likeInfo = userInfoDAO.findById(zmFriendCircleLike.getUserId());
                zmFriendCircleLike.setUserInfo(likeInfo);
            }
            //查询评论
            List<ZmFriendCircleComment> commentList = commentDao.getCommentList(zmFriendCircleDto.getId(), Integer.parseInt(zmFriendCircleDto.gettIndex() + ""), userId);
            for (ZmFriendCircleComment zmFriendCircleComment : commentList) {
                UserInfo fromUser = userInfoDAO.findById(zmFriendCircleComment.getFromUserId());
                if (zmFriendCircleComment.getToUserId() != 0) {
                    UserInfo toUser = userInfoDAO.findById(zmFriendCircleComment.getToUserId());
                    zmFriendCircleComment.setToUserInfo(toUser);
                }
                zmFriendCircleComment.setFromUserInfo(fromUser);
            }
            List picList = albumDao.getPicList(zmFriendCircleDto.getId(), Integer.parseInt(zmFriendCircleDto.gettIndex() + ""));

            zmFriendCircleDto.setLikeList(likeList);
            zmFriendCircleDto.setCommentList(commentList);
            zmFriendCircleDto.setPicList(picList);
        }
    }


    /**
     * 保存分享
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmFriendCircleDto]
     * @Date: 2018-10-18 下午 15:47
     */
    @Transactional
    public void saveContent(ZmFriendCircleDto zmFriendCircleDto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Integer index = Integer.parseInt(zmFriendCircleDto.gettIndex() + "");
        Integer indexId = null;
        try {
            if (index == EMPLOYER_INDEX) {//招工
                KeyContent keyContent = keyContentDAO.findById(Integer.parseInt(zmFriendCircleDto.getWorkId().toString()));
                ZmEmployerCircle zmEmployerCircle = new ZmEmployerCircle();
                BeanUtils.copyProperties(zmFriendCircleDto, zmEmployerCircle);
                zmEmployerCircle.setIndex(index);
                zmEmployerCircle.setAmount(Integer.parseInt(zmFriendCircleDto.getAmount() + ""));
                zmEmployerCircle.setCreateTime(now);
                zmEmployerCircle.setGzId(Integer.parseInt(zmFriendCircleDto.getGzId().toString()));
                zmEmployerCircle.setWorkId(Integer.parseInt(zmFriendCircleDto.getWorkId().toString()));
                if (keyContent != null) {
                    zmEmployerCircle.setWorkType(keyContent.getKeyName());
                } else {
                    zmEmployerCircle.setWorkType(zmFriendCircleDto.getWorkType());
                }
                employerDao.saveZmEmployerCircle(zmEmployerCircle);
                indexId = zmEmployerCircle.getId();
            } else if (index == EMPLOYEE_INDEX) {//找工
                Date date = DateUtil.getFormatDate(zmFriendCircleDto.getArriveTime(),DateUtil.FORMAT_DATE);
                String sdate = DateUtil.format(date,DateUtil.FORMAT_DATE_TIME);
                ZmEmployeeCircle zmEmployeeCircle = new ZmEmployeeCircle();
                BeanUtils.copyProperties(zmFriendCircleDto, zmEmployeeCircle);
                zmEmployeeCircle.setIndex(index);
                zmEmployeeCircle.setWork(zmFriendCircleDto.getWorks());
                zmEmployeeCircle.setCreateTime(now);
                zmEmployeeCircle.setArriveTime(Timestamp.valueOf(sdate));
                zmEmployeeCircle.setGzId(Integer.parseInt(zmFriendCircleDto.getGzId().toString()));
                employeeDao.saveZmEmployeeCircle(zmEmployeeCircle);
                indexId = zmEmployeeCircle.getId();
            } else if (index == SHUOSHUO_INDEX) {//说说
                ZmFriendCircleShuoshuo zmFriendCircleShuoshuo = new ZmFriendCircleShuoshuo();
                BeanUtils.copyProperties(zmFriendCircleDto, zmFriendCircleShuoshuo);
                zmFriendCircleShuoshuo.setIndex(index);
                zmFriendCircleShuoshuo.setInitTime(now);
                zmFriendCircleShuoshuo.setvState(0);
                zmFriendCircleShuoshuo.setType(SHUOSHUO_PICTURE);
                shuoshuoDao.saveShuoShuo(zmFriendCircleShuoshuo);
                indexId = zmFriendCircleShuoshuo.getId();
            } else if (index == NETWORK_INDEX) {//网络分享
                ZmNetworkArticle zmNetworkArticle = new ZmNetworkArticle();
                BeanUtils.copyProperties(zmFriendCircleDto, zmNetworkArticle);
                zmNetworkArticle.setIndex(index);
                zmNetworkArticle.setInitTime(now);
                networkDao.saveZmNetworkArticle(zmNetworkArticle);
                indexId = zmNetworkArticle.getId();
            }
            if (StringUtils.isNotBlank(zmFriendCircleDto.getPicUrl())) {
                //保存图片
                String[] picArr = zmFriendCircleDto.getPicUrl().split(",");
                for (String s : picArr) {
                    ZmFriendCircleAlbum album = new ZmFriendCircleAlbum();
                    album.setIndex(index);
                    album.setShuoshuoId(indexId);
                    album.setPicUrl(s);
                    album.setUserId(zmFriendCircleDto.getUserId());
                    album.setType(SHUOSHUO_PICTURE);
                    albumDao.saveZmFriendCircleAlbum(album);
                }
            }
            //保存到总表
            ZmFriendCircle zmFriendCircle = new ZmFriendCircle();
            zmFriendCircle.setIndex(index);
            zmFriendCircle.setIndexId(indexId);
            zmFriendCircle.setUserId(zmFriendCircleDto.getUserId());
            zmFriendCircle.setStatus(STATUS_PASSED);
            zmFriendCircle.setOperatTime(new Timestamp(System.currentTimeMillis()));
            zmFriendCircle.setCircleDel(1);
            circleDao.saveZmFriendCircle(zmFriendCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 删除分享
     *
     * @return void
     * @Author: wuyeming
     * @params: [indexId, index, userId]
     * @Date: 2018-10-19 上午 9:24
     */
    @Transactional
    public void deleteContent(Integer indexId, Integer index, Integer userId) {
        try {
            ZmFriendCircle zmFriendCircle = circleDao.findZmFriendCircle(indexId, index);
            if (zmFriendCircle != null) {
                if (userId == null) {
                    circleDao.deleteZmFriendCircle(indexId, index);
                    List<ZmFriendCircleComment> commentList = commentDao.getCommentList(indexId, index, null);
                    commentDao.batchDeleteByObject(commentList);
                    List<ZmFriendCircleLike> likeList = likeDao.getLikeList(indexId, index);
                    likeDao.batchDeleteByObject(likeList);
                } else {
                    if (zmFriendCircle.getUserId().equals(userId)) {
                        circleDao.deleteZmFriendCircle(indexId, index);
                        List<ZmFriendCircleComment> commentList = commentDao.getCommentList(indexId, index, null);
                        commentDao.batchDeleteByObject(commentList);
                        List<ZmFriendCircleLike> likeList = likeDao.getLikeList(indexId, index);
                        likeDao.batchDeleteByObject(likeList);
                    } else {
                        throw new RuntimeException("不能删除非本人分享的内容！");
                    }
                }
            } else {
                throw new RuntimeException("记录不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 点赞/取消赞
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmFriendCircleLike]
     * @Date: 2018-10-19 上午 11:10
     */
    @Transactional
    public void saveOrCancelLike(ZmFriendCircleLike zmFriendCircleLike) {
        int state = 0;
        try {
            Integer indexId = zmFriendCircleLike.getIndexId();
            Integer index = zmFriendCircleLike.getIndex();
            Integer userId = getUserId(indexId, index);
            boolean flag = false;
            ZmFriendCircle friendCircle = circleDao.findZmFriendCircle(indexId, index);
            if (friendCircle != null) {
                ZmFriendCircleLike like = likeDao.findZmFriendCircleLike(zmFriendCircleLike);
                if (like != null) {
                    if (like.getLikeDel().equals(1)) {
                        likeDao.deleteById(like.getId());//取消点赞
                    } else {
                        //重新点赞
                        like.setLikeDel(1);
                        like.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        if (like.getUserId().equals(userId)) {//如果自己给自己点赞，将点赞状态设置为已读
                            flag = true;
                            like.setStatus(1);
                        } else {
                            like.setStatus(0);
                        }
                        likeDao.saveZmFriendCircleLike(like);
                        Integer id = like.getId();
                        push(zmFriendCircleLike, userId, flag, id);
                        state = 1;
                    }
                } else {
                    zmFriendCircleLike.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    if (userId.equals(zmFriendCircleLike.getUserId())) {//如果自己给自己点赞，将点赞状态设置为已读
                        flag = true;
                        zmFriendCircleLike.setStatus(1);
                    } else {
                        zmFriendCircleLike.setStatus(0);
                    }
                    zmFriendCircleLike.setLikeDel(1);
                    likeDao.saveZmFriendCircleLike(zmFriendCircleLike);
                    Integer id = zmFriendCircleLike.getId();
                    push(zmFriendCircleLike, userId, flag, id);
                    state = 1;
                }
            } else {
                throw new RuntimeException("记录不存在");
            }
            zmFriendCircleLike.setLikeDel(state);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void push(ZmFriendCircleLike zmFriendCircleLike, Integer userId, boolean flag, Integer id) {
        if (!flag) {
            Map<String, String> map = new HashMap<>();
            Object counts = this.getLikeAndCommentCounts(userId).get("counts");
            if (counts.equals(0)) {
                counts = Integer.parseInt(counts.toString()) + 1;
            }
            map.put("num", counts.toString());//未读数量
            map.put("notify", "0");//是否显示在通知栏 0不显示 1显示
            map.put("pushType", "circle");
            String title = "点赞提醒";
            String content = "有人给你点赞啦！快去看看吧!";
            UserInfo userInfo = userInfoDao.getUserInfoById(userId);
            if (userInfo != null && userInfo.getLast_device() != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                int result = JPushClientUtil.getInstance().pushDevice(aliasList, OS, title, content, map, "0");
                PushLogCircle pushLogCircle = new PushLogCircle();
                pushLogCircle.settIndex(LIKE_INDEX);
                pushLogCircle.setIndexId(id);
                pushLogCircle.setFromUserId(zmFriendCircleLike.getUserId());
                pushLogCircle.setToUserId(userId);
                pushLogCircle.setType(1);
                pushLogCircle.setNotify(0);
                pushLogCircle.setStatus(result);
                pushLogCircle.setCreateTime(new Timestamp(System.currentTimeMillis()));
                pushLogCircleDao.savePushLogCircle(pushLogCircle);
            }
        }
    }


    /**
      * 获取用户id
      *
      * @return java.lang.Integer
      * @Author: wuyeming
      * @params: [indexId, index]
      * @Date: 2018-10-28 下午 16:48
      */
    private Integer getUserId(Integer indexId, Integer index) {
        Integer userId = null;
        if (index.equals(EMPLOYER_INDEX)) {
            ZmEmployerCircle employer = employerDao.findById(indexId);
            if (employer != null) {
                userId = employer.getUserId();
            }
        } else if (index.equals(EMPLOYEE_INDEX)) {
            ZmEmployeeCircle employee = employeeDao.findById(indexId);
            if (employee != null) {
                userId = employee.getUserId();
            }
        } else if (index.equals(SHUOSHUO_INDEX)) {
            ZmFriendCircleShuoshuo shuoshuo = shuoshuoDao.findById(indexId);
            if (shuoshuo != null) {
                userId = shuoshuo.getUserId();
            }
        } else if (index.equals(NETWORK_INDEX)) {
            ZmNetworkArticle network = networkDao.findById(indexId);
            if (network != null) {
                userId = network.getUserId();
            }
        }
        return userId;
    }


    /**
     * 保存评论
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmFriendCircleComment]
     * @Date: 2018-10-19 上午 11:26
     */
    public Integer saveComment(ZmFriendCircleComment zmFriendCircleComment) {
        Integer id = null;
        try {
            Integer index = zmFriendCircleComment.getIndex();
            Integer indexId = zmFriendCircleComment.getIndexId();
            Integer toUserId = zmFriendCircleComment.getToUserId();
            ZmFriendCircle friendCircle = circleDao.findZmFriendCircle(indexId, index);
            if (friendCircle != null) {
//            boolean flag = false;
                zmFriendCircleComment.setAuditsStatus(STATUS_PASSED);//审核状态 0审核中 1审核通过
                zmFriendCircleComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
                if (toUserId == 0) {
                    toUserId = getUserId(indexId, index);
                }
                if (toUserId.equals(zmFriendCircleComment.getFromUserId())) {//如果自己给自己评论，将评论状态设置为已读
//                flag = true;
                    zmFriendCircleComment.setStatus(1);
                }else {
                    zmFriendCircleComment.setStatus(0);
                }
                zmFriendCircleComment.setCommentDel(1);
                commentDao.saveComment(zmFriendCircleComment);
                id = zmFriendCircleComment.getId();
//            if (!flag) {
//                Map<String, String> map = new HashMap<>();
//                Object counts = this.getLikeAndCommentCounts(zmFriendCircleComment.getToUserId()).get("counts");
//                map.put("num", counts.toString());//未读数量
//                map.put("notify", "0");//是否显示在通知栏 0不显示 1显示
//                map.put("pushType", "circle");
//                String content = "有人给你评论啦！快去看看吧!";
//                int status = JPushClientUtil.getInstance().pushDevices(toUserId, content, content, map, "0");
//                PushLogCircle pushLogCircle = new PushLogCircle();
//                pushLogCircle.settIndex(COMMENT_INDEX);
//                pushLogCircle.setIndexId(id);
//                pushLogCircle.setFromUserId(zmFriendCircleComment.getFromUserId());
//                pushLogCircle.setToUserId(toUserId);
//                pushLogCircle.setType(1);
//                pushLogCircle.setNotify(0);
//                pushLogCircle.setStatus(status);
//                pushLogCircle.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                pushLogCircleDao.savePushLogCircle(pushLogCircle);
//            }
            }else {
                throw new RuntimeException("记录不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return id;
    }


    /**
     * 删除评论
     *
     * @return void
     * @Author: wuyeming
     * @params: [id]
     * @Date: 2018-10-19 上午 11:27
     */
    public void deleteComment(Integer id) {
        try {
            commentDao.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 获取工种类型
     *
     * @return java.util.List<com.zm.entity.KeyContent>
     * @Author: wuyeming
     * @params: []
     * @Date: 2018-10-19 下午 14:25
     */
    public List<KeyContent> getWorkType() {
        List<KeyContent> list = null;
        try {
            list = employeeDao.getWorkType();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
     * 查询是否屏蔽了某人
     *
     * @return boolean
     * @Author: wuyeming
     * @params: [fromUserId, toUserId, operator]
     * @Date: 2018-10-19 下午 14:33
     */
    public boolean isRefusedLookPerson(Integer fromUserId, Integer toUserId, Integer operator) {
        boolean flag = false;
        try {
            ZmRefusedLook zmRefusedLook = lookDao.findZmRefusedLook(fromUserId, toUserId, operator);
            if (zmRefusedLook != null) {
                flag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return flag;
    }


    /**
     * 屏蔽/取消ta看我的分享
     * 设置/取消不让ta看我的分享
     *
     * @return void
     * @Author: wuyeming
     * @params: [fromUserId, toUserId, operator]
     * @Date: 2018-10-19 下午 15:02
     */
    public void saveOrCancelRefusedPerson(Integer fromUserId, Integer toUserId, Integer operator) {
        try {
            ZmRefusedLook zmRefusedLook = lookDao.findZmRefusedLook(fromUserId, toUserId, operator);
            if (zmRefusedLook != null) {
                lookDao.deleteZmRefusedLook(zmRefusedLook);
            } else {
                ZmRefusedLook refusedLook = new ZmRefusedLook();
                refusedLook.setFromUserId(fromUserId);
                refusedLook.setToUserId(toUserId);
                refusedLook.setIndex(0);
                refusedLook.setIndexId(0);
                refusedLook.setOperator(operator);
                lookDao.saveZmRefusedLook(refusedLook);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 屏蔽/取消屏蔽某分享
     *
     * @return com.zm.friendCircle.entity.ZmRefusedLook
     * @Author: wuyeming
     * @params: [fromUserId, index, indexId]
     * @Date: 2018-10-19 下午 15:16
     */
    public void saveOrCancelRefusedContent(Integer fromUserId, Integer index, Integer indexId) {
        try {
            ZmRefusedLook zmRefusedLook = lookDao.refusedLookContent(fromUserId, index, indexId);
            if (zmRefusedLook != null) {
                lookDao.deleteZmRefusedLook(zmRefusedLook);
            } else {
                ZmRefusedLook refusedLook = new ZmRefusedLook();
                refusedLook.setFromUserId(fromUserId);
                refusedLook.setToUserId(0);
                refusedLook.setIndex(index);
                refusedLook.setIndexId(indexId);
                lookDao.saveZmRefusedLook(refusedLook);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 根据人员id获取相册
     *
     * @return java.util.List<com.zm.friendCircle.entity.ZmFriendCircleAlbum>
     * @Author: wuyeming
     * @params: [fromUserId, toUserId]
     * @Date: 2018-10-25 上午 9:39
     */
    public Map<String,Object> getAlbumListByUserId(Integer fromUserId, Integer toUserId) {
        try {
            return albumDao.getAlbumListByUserId(fromUserId, toUserId, 4);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 获取未读的点赞和评论列表
      *
      * @return com.ktp.project.dto.LikeCommentDto
      * @Author: wuyeming
      * @params: [userId]
      * @Date: 2018-10-26 下午 14:09
      */
    public List<LikeCommentDto> getLikeAndCommentList(Integer userId) {
        try {
            List<LikeCommentDto> list = circleDao.getLikeAndCommentList(userId);
            for (LikeCommentDto likeCommentDto : list) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                List<PicDto> picList = circleDao.getLikeCommentPic(likeCommentDto);
                PicDto pic = null;
                for (PicDto picDto : picList) {
                    Map<String, Object> map = new HashMap<>();
                    if (pic == null) {
                        pic = picDto;
                    }
                    if (picDto.getType() == pic.getType()) {
                        map.put("type", picDto.getType());
                        map.put("picUrl", picDto.getPicUrl());
                    } else {
                        map.put("type", picDto.getType());
                        map.put("picUrl", pic.getPicUrl());
                        map.put("videoUrl", picDto.getPicUrl());
                        mapList.clear();
                    }
                    mapList.add(map);
                }
                likeCommentDto.setPicList(mapList);
            }
            updateStatus(list);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
      * 更新点赞/评论状态
      *
      * @return void
      * @Author: wuyeming
      * @params: [params]
      * @Date: 2018-10-26 下午 14:57
      */
    private void updateStatus(List<LikeCommentDto> list) {
        List<Object> likeIds = new ArrayList<>();
        List<Object> commentIds = new ArrayList<>();
        try {
            for (LikeCommentDto likeCommentDto : list) {
                if (likeCommentDto.getType().toString().equals("0")) {
                    likeIds.add(likeCommentDto.getId());
                } else {
                    commentIds.add(likeCommentDto.getId());
                }
            }
            if (likeIds.size() > 0) {
                likeDao.batchUpdateStatus(likeIds);
            }
            if (commentIds.size() > 0) {
                commentDao.batchUpdateStatus(commentIds);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 发视频
      *
      * @return void
      * @Author: wuyeming
      * @params: [userId, content, picUrl]
      * @Date: 2018-10-26 下午 15:53
      */
    @Transactional
    public void saveVideo(Integer userId, String content, String picUrl, String videoUrl) {
        ZmFriendCircleShuoshuo zmFriendCircleShuoshuo = new ZmFriendCircleShuoshuo();
        zmFriendCircleShuoshuo.setUserId(userId);
        zmFriendCircleShuoshuo.setInitTime(new Timestamp(System.currentTimeMillis()));
        zmFriendCircleShuoshuo.setIndex(SHUOSHUO_INDEX);
        zmFriendCircleShuoshuo.setvState(0);
        zmFriendCircleShuoshuo.setContent(content);
        zmFriendCircleShuoshuo.setType(SHUOSHUO_VIDEO);
        try {
            shuoshuoDao.saveShuoShuo(zmFriendCircleShuoshuo);
            Integer indexId = zmFriendCircleShuoshuo.getId();
            ZmFriendCircleAlbum pic = new ZmFriendCircleAlbum();
            pic.setIndex(SHUOSHUO_INDEX);
            pic.setShuoshuoId(indexId);
            pic.setPicUrl(picUrl);
            pic.setUserId(userId);
            pic.setType(SHUOSHUO_PICTURE);
            albumDao.saveZmFriendCircleAlbum(pic);//保存图片url
            ZmFriendCircleAlbum video = new ZmFriendCircleAlbum();
            BeanUtils.copyProperties(pic, video);
            video.setType(SHUOSHUO_VIDEO);
            video.setId(null);
            video.setPicUrl(videoUrl);
            albumDao.saveZmFriendCircleAlbum(video);//保存视频url
            //保存到总表
            ZmFriendCircle zmFriendCircle = new ZmFriendCircle();
            zmFriendCircle.setIndex(SHUOSHUO_INDEX);
            zmFriendCircle.setIndexId(indexId);
            zmFriendCircle.setUserId(userId);
            zmFriendCircle.setStatus(STATUS_PASSED);
            zmFriendCircle.setCircleDel(1);
            zmFriendCircle.setOperatTime(new Timestamp(System.currentTimeMillis()));
            circleDao.saveZmFriendCircle(zmFriendCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 获取未读的点赞和评论数
      *
      * @return java.util.Map<java.lang.String,java.lang.Object>
      * @Author: wuyeming
      * @params: [userId]
      * @Date: 2018-10-26 下午 17:19
      */
    public Map<String, Object> getLikeAndCommentCounts(Integer userId) {
        try {
            Map<String, Object> map = circleDao.getLikeAndCommentCounts(userId);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 查询分享详情
      *
      * @return com.ktp.project.dto.ZmFriendCircleDto
      * @Author: wuyeming
      * @params: [index, indexId, userId]
      * @Date: 2018-10-29 下午 14:47
      */
    public ZmFriendCircleDto getContentInfo(Integer index, Integer indexId, Integer userId) {
        ZmFriendCircleDto zmFriendCircleDto = null;
        try {
            List<ZmFriendCircleDto> list = circleDao.getContentInfo(index, indexId);
            if (list.size() > 0) {
                getLikeAndCommentAndPic(list, userId);
                zmFriendCircleDto = list.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return zmFriendCircleDto;
    }


    /**
      * 获取招聘类型
      *
      * @return java.util.List<com.zm.entity.KeyContent>
      * @Author: wuyeming
      * @params: []
      * @Date: 2018-11-1 上午 10:53
      */
    public List<KeyContent> getEmployType(){
        return circleDao.getEmployType();
    }


    /**
      * 查询分享(用于新接口)
      *
      * @return java.util.List<com.ktp.project.dto.ZmFriendCircleDto>
      * @params: [index, userId, begin, length]
      * @Author: wuyeming
      * @Date: 2018-11-20 下午 18:32
      */
    public List<CircleDto> getContentByNew(Integer index, Integer userId, Integer begin, Integer length) {
        List<CircleDto> list = null;
        try {
            list = circleDao.getContentByNew(index, userId, begin, length);
            getLikeAndCommentAndPicByNew(list, userId);//查询点赞、评论、图片
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 查询点赞、评论、图片
      *
      * @return void
      * @params: [list, userId]
      * @Author: wuyeming
      * @Date: 2018-11-20 下午 18:32
      */
    private void getLikeAndCommentAndPicByNew(List<CircleDto> list, Integer userId) {
        for (CircleDto circleDto : list) {
            //查询点赞
            List<LikeDto> likeList = likeDao.getLikeListByNew(circleDto.getId(), Integer.parseInt(circleDto.gettIndex() + ""));
            //下次发版需要去掉
            for (LikeDto likeDto : likeList) {
                UserInfo likeInfo = new UserInfo();
                likeInfo.setUser_id(likeDto.getUserId());
                likeInfo.setU_nicheng(likeDto.getUserName());
                likeDto.setUserInfo(likeInfo);
            }
            //查询评论
            List<CommentDto> commentList = commentDao.getCommentListByNew(circleDto.getId(), Integer.parseInt(circleDto.gettIndex() + ""), userId);
            List picList = albumDao.getPicList(circleDto.getId(), Integer.parseInt(circleDto.gettIndex() + ""));

            circleDto.setLikeList(likeList);
            circleDto.setCommentList(commentList);
            circleDto.setPicList(picList);
        }
    }
}
