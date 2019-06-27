package com.ktp.project.web;

import com.ktp.project.dto.LikeCommentDto;
import com.ktp.project.dto.ZmFriendCircleDto;
import com.ktp.project.dto.circledto.CircleDto;
import com.ktp.project.entity.BaseEntity;
import com.ktp.project.service.ZmFriendCircleService;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientKTPCloundUtils;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import com.zm.entity.KeyContent;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import com.zm.friendCircle.entity.ZmFriendCircleLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-10-18 上午 11:02
 */
@Controller("circleController")
@RequestMapping(value = "api/friendCircle", produces = "application/json;charset=UTF-8;")
public class ZmFriendCircleController {
    private static final Logger log = LoggerFactory.getLogger(ZmFriendCircleController.class);

    @Autowired
    private ZmFriendCircleService circleService;


    /**
     * 查询所有内容
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId, startPage, length]
     * @Date: 2018-10-18 下午 14:18
     */
    @RequestMapping(value = "getAllContent", method = RequestMethod.POST)
    @ResponseBody
    public String getAllContent(Integer index, Integer userId, Integer startPage, Integer length) {
        if (startPage <= 0) {
            startPage = 1; //默认从1开始
        }
        int begin = (startPage - 1) * length;
        try {
            List<ZmFriendCircleDto> list = circleService.getContent(index, userId, begin, length);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询自己的内容
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId, startPage, length]
     * @Date: 2018-10-18 下午 14:18
     */
    @RequestMapping(value = "getMyContent", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMyContent(Integer userId, Integer startPage, Integer length) {
        if (startPage <= 0) {
            startPage = 1; //默认从1开始
        }
        int begin = (startPage - 1) * length;
        try {
            List<ZmFriendCircleDto> list = circleService.getContent(null, userId, begin, length);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 保存内容
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [zmFriendCircleDto]
     * @Date: 2018-10-18 下午 15:12
     */
    @RequestMapping(value = "saveContent", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveContent(ZmFriendCircleDto zmFriendCircleDto) {
        try {
            circleService.saveContent(zmFriendCircleDto);
            RedisClientTemplate.batchDel("share_" + zmFriendCircleDto.getUserId());//删除redis中以share_userId开头的缓存

            Integer tIndex = Integer.parseInt(zmFriendCircleDto.gettIndex().toString());

            if (tIndex == 3) {
                String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(zmFriendCircleDto.getUserId(), 207);
                BaseEntity baseEntity = GsonUtil.fromJson(saveIntegalTask, BaseEntity.class);
                if (baseEntity.getStatus().getCode() == 10 && baseEntity.getBusinessStatus().getCode() == 100) {//成功
                    return ResponseUtil.createNormalJson(null, "成功", baseEntity.getBusinessStatus().getIntegralmsg());
                }
            }
            return ResponseUtil.createNormalJson(null, "成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 删除内容
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [indexId, index, userId]
     * @Date: 2018-10-19 上午 10:54
     */
    @RequestMapping(value = "deleteContent", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteContent(Integer indexId, Integer index, Integer userId) {
        try {
            circleService.deleteContent(indexId, index, userId);
            RedisClientTemplate.batchDel("share");//删除redis中以share开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 点赞/取消赞
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [zmFriendCircleLike]
     * @Date: 2018-10-19 上午 11:22
     */
    @RequestMapping(value = "saveOrCancelLike", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveOrCancelLike(ZmFriendCircleLike zmFriendCircleLike) {
        try {
            circleService.saveOrCancelLike(zmFriendCircleLike);
            RedisClientTemplate.batchDel("share");//删除redis中以share开头的缓存
            if (zmFriendCircleLike.getLikeDel() == 1) {
                String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(zmFriendCircleLike.getUserId(), 208);
                BaseEntity baseEntity = GsonUtil.fromJson(saveIntegalTask, BaseEntity.class);
                if (baseEntity.getStatus().getCode() == 10 && baseEntity.getBusinessStatus().getCode() == 100) {//成功
                    return ResponseUtil.createNormalJson(null, "成功", baseEntity.getBusinessStatus().getIntegralmsg());
                }
            }
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 保存评论
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [zmFriendCircleComment]
     * @Date: 2018-10-19 上午 11:23
     */
    @RequestMapping(value = "saveComment", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveComment(ZmFriendCircleComment zmFriendCircleComment) {
        try {
            Integer id = circleService.saveComment(zmFriendCircleComment);
            Map<String, Object> map = new HashMap<>();
            map.put("commentId", id);
            RedisClientTemplate.batchDel("share_" + zmFriendCircleComment.getFromUserId());//删除redis中以share_userId开头的缓存
            String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(zmFriendCircleComment.getFromUserId(), 209);
            BaseEntity baseEntity = GsonUtil.fromJson(saveIntegalTask, BaseEntity.class);
            if (baseEntity.getStatus().getCode() == 10 && baseEntity.getBusinessStatus().getCode() == 100) {//成功
                return ResponseUtil.createNormalJson(map, "成功", baseEntity.getBusinessStatus().getIntegralmsg());
            }
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 删除评论
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [id]
     * @Date: 2018-10-19 上午 11:28
     */
    @RequestMapping(value = "deleteComment", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String deleteComment(Integer commentId) {
        try {
            circleService.deleteComment(commentId);
            RedisClientTemplate.batchDel("share");//删除redis中以share开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询是否屏蔽了某人
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [fromUserId, toUserId]
     * @Date: 2018-10-19 下午 14:44
     */
    @RequestMapping(value = "isRefusedLookPerson", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String isRefusedLookPerson(Integer fromUserId, Integer toUserId) {
        try {
            boolean refusedLook = circleService.isRefusedLookPerson(fromUserId, toUserId, fromUserId);
            boolean passiveRefusedLook = circleService.isRefusedLookPerson(toUserId, fromUserId, fromUserId);
            Map<String, Object> map = new HashMap<>();
            map.put("refusedLook", refusedLook);
            map.put("passiveRefusedLook", passiveRefusedLook);
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 屏蔽/取消屏蔽某人
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [fromUserId, toUserId]
     * @Date: 2018-10-19 下午 15:04
     */
    @RequestMapping(value = "saveOrCancelRefusedPerson", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveOrCancelRefusedPerson(Integer fromUserId, Integer toUserId) {
        try {
            circleService.saveOrCancelRefusedPerson(fromUserId, toUserId, fromUserId);
            RedisClientTemplate.batchDel("share_" + fromUserId);//删除redis中以share开头的缓存
            RedisClientTemplate.batchDel("share_" + toUserId);//删除redis中以share开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 屏蔽/取消屏蔽某内容
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [fromUserId, index, indexId]
     * @Date: 2018-10-19 下午 15:36
     */
    @RequestMapping(value = "saveOrCancelRefusedContent", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveOrCancelRefusedContent(Integer fromUserId, Integer index, Integer indexId) {
        try {
            circleService.saveOrCancelRefusedContent(fromUserId, index, indexId);
            RedisClientTemplate.batchDel("share");//删除redis中以share开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 根据人员id获取相册列表
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId]
     * @Date: 2018-10-25 上午 9:45
     */
    @RequestMapping(value = "getAlbumListByUserId", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getAlbumListByUserId(Integer fromUserId, Integer toUserId) {
        try {
            Map<String, Object> map = circleService.getAlbumListByUserId(fromUserId, toUserId);
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 获取工人职业类型
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: []
     * @Date: 2018-10-25 上午 11:36
     */
    @RequestMapping(value = "getWorkType", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getWorkType() {
        try {
            List<KeyContent> list = circleService.getWorkType();
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 设置/取消 不让ta看我的分享
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [fromUserId, toUserId]
     * @Date: 2018-10-25 上午 11:31
     */
    @RequestMapping(value = "passiveRefusedLook", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String passiveRefusedLook(Integer fromUserId, Integer toUserId) {
        try {
            circleService.saveOrCancelRefusedPerson(toUserId, fromUserId, fromUserId);
            RedisClientTemplate.batchDel("share_" + fromUserId);//删除redis中以share_userId开头的缓存
            RedisClientTemplate.batchDel("share_" + toUserId);//删除redis中以share_userId开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询未读的点赞和评论列表
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId]
     * @Date: 2018-10-26 下午 14:52
     */
    @RequestMapping(value = "getLikeAndCommentList", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getLikeAndCommentList(Integer userId) {
        try {
            List<LikeCommentDto> list = circleService.getLikeAndCommentList(userId);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 发视频
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId, content, picUrl]
     * @Date: 2018-10-26 下午 16:20
     */
    @RequestMapping(value = "saveVideo", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String saveVideo(Integer userId, String content, String picUrl, String videoUrl) {
        try {
            circleService.saveVideo(userId, content, picUrl, videoUrl);
            RedisClientTemplate.batchDel("share_" + userId);//删除redis中以share_userId开头的缓存
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询未读的点赞和评论数
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [userId]
     * @Date: 2018-10-26 下午 17:20
     */
    @RequestMapping(value = "getLikeAndCommentCounts", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getLikeAndCommentCounts(Integer userId) {
        try {
            Map<String, Object> map = circleService.getLikeAndCommentCounts(userId);
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询分享详情
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [index, indexId, userId]
     * @Date: 2018-10-29 下午 14:48
     */
    @RequestMapping(value = "getContentInfo", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getContentInfo(Integer index, Integer indexId, Integer userId) {
        try {
            ZmFriendCircleDto zmFriendCircleDto = circleService.getContentInfo(index, indexId, userId);
            return ResponseUtil.createNormalJson(zmFriendCircleDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * @param model
     * @param type  1：招工 2：找工 3：说说 4：文章
     * @param id
     * @return
     */
    @RequestMapping(value = "/share/details", method = RequestMethod.GET)
    public String shareDetails(Model model, Integer type, Integer id) {
        try {
            ZmFriendCircleDto zmFriendCircleDto = circleService.getContentInfo(type, id, null);
            String summary = zmFriendCircleDto.getSummary();
            zmFriendCircleDto.setSummary(summary.length() > 30 ? summary.substring(0, 29) + "..." : summary);
            List<ZmFriendCircleComment> comments = zmFriendCircleDto.getCommentList();
            if (comments.size() > 1) {
                zmFriendCircleDto.setCommentList(comments.subList(0, 2));
            } else {
                zmFriendCircleDto.setCommentList(comments);
            }
            model.addAttribute("result", zmFriendCircleDto).addAttribute("shareType", type);
            return "share/friendCircle";
        } catch (Exception e) {
            log.error("分享朋友圈异常", e);
            return "error/500";
        }
    }


    /**
     * 获取招聘类型
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: []
     * @Date: 2018-11-1 上午 10:54
     */
    @RequestMapping(value = "getEmployType ", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getWork() {
        try {
            List<KeyContent> list = circleService.getEmployType();
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    @RequestMapping(value = "getAllContentByNew", method = RequestMethod.POST)
    @ResponseBody
    public String getAllContentByNew(Integer index, Integer userId, Integer startPage, Integer length) {
        List<CircleDto> shareList = null;
        if (startPage <= 0) {
            startPage = 1; //默认从1开始
        }
        int begin = (startPage - 1) * length;
        try {
            String json = RedisClientTemplate.get("share_" + userId + "_" + index + "_" + startPage);
            if (json != null) {
                shareList = GsonUtil.jsonToList(RedisClientTemplate.get("share_" + userId + "_" + index + "_" + startPage), CircleDto.class);
            } else {
                shareList = circleService.getContentByNew(index, userId, begin, length);
                RedisClientTemplate.set("share_" + userId + "_" + index + "_" + startPage, GsonUtil.toJson(shareList), 3600);
            }
            return ResponseUtil.createNormalJson(shareList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
