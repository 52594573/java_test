package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.dto.clockin.ProxyClockInDetailsDto;
import com.ktp.project.entity.ProxyClockIn;
import com.ktp.project.service.ProxyClockInService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.ObjectUtil;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 代理打卡
 * Created by LinHon 2018/12/4
 */
@RestController
@RequestMapping(value = "api/proxy/clockIn", produces = "application/json;charset=UTF-8;")
public class ProxyClockInController {

    private static final Logger log = LoggerFactory.getLogger(ProxyClockInController.class);

    private static final int RETRY_COUNT = 3;

    @Autowired
    private ProxyClockInService proxyClockInService;


    /**
     * 获取被代理工人列表
     *
     * @param projectId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public String getUserList(int projectId, int userId) {
        try {
            List result = proxyClockInService.getUserList(projectId, userId);
            return ResponseUtil.createNormalJson(ImmutableMap.of("organList", result), "成功");
        } catch (Exception e) {
            log.error("获取被代理工人列表异常", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 考勤打卡，打卡失败默认重试3次，如果最后还是没打卡成功则保存失败记录
     *
     * @param proxyClockIn
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(ProxyClockIn proxyClockIn) {
        try {
            Map result = proxyClockInService.create(proxyClockIn);
            return ResponseUtil.createNormalJson(result, "成功");
        } catch (Exception e) {
            //TODO 取消打卡重试
            log.error("考勤打卡异常", e);
            return ResponseUtil.createBussniessErrorJson(500, "打卡失败");
            //return doClockInException(proxyClockIn, 0);
        }
    }

   /* private String doClockInException(ProxyClockIn proxyClockIn, int count) {
        try {
            Map result = proxyClockInService.create(proxyClockIn);
            return ResponseUtil.createNormalJson(result, "成功");
        } catch (Exception e) {
            if (count < RETRY_COUNT) {
                return doClockInException(proxyClockIn, ++count);
            }
            log.error("考勤打卡异常", e);
            try {
                proxyClockInService.clockInFailure(proxyClockIn, e.getMessage());
            } catch (Exception ex) {
                log.error("保存考勤打卡失败记录异常", ex);
            } finally {
                return ResponseUtil.createBussniessErrorJson(500, "打卡失败");
            }
        }
    }*/


    /**
     * 获取代理打卡列表
     *
     * @param projectId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getProxyClockInList", method = RequestMethod.GET)
    public String getProxyClockInList(int projectId, int userId) {
        try {
            List result = proxyClockInService.getProxyClockInList(projectId, userId);
            return ResponseUtil.createNormalJson(ImmutableMap.of("organList", result), "成功");
        } catch (Exception e) {
            log.error("获取代理打卡列表异常", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 获取打卡详情列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "getProxyClockInDetail", method = RequestMethod.GET)
    public String getProxyClockInDetail(@Param(value = "userId") int userId, @Param(value = "proDate") String proDate,
                                        @Param(value = "proxyUserId") int proxyUserId, @Param(value = "proId") int proId) {
        log.info("获取打卡详情");
        try {
            ProxyClockInDetailsDto benf = proxyClockInService.getProxyClockInDetail(proxyUserId, proDate, userId, proId);

            //TODO 暂时处理没有活动时返回一个空对象--完成后需去除
            return ResponseUtil.createNormalJson(benf);
        } catch (Exception e) {
            log.error("获取打卡详情", e);
            return LoanUtils.buildExceptionResponse(log, "获取打卡详情", e);
        }
    }
}
