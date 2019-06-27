package com.ktp.project.service.realName;

import java.util.ArrayList;
import java.util.List;

import com.ktp.project.util.ObjectUtil;
import com.ktp.project.util.redis.RedisConsumer;
import com.ktp.project.util.redis.RedisConsumerImpl;
import com.ktp.project.util.redis.RedisMessageListener;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

public class RedisQueueConsumersService implements InitializingBean, DisposableBean {
    private static Logger logger = LoggerFactory.getLogger(RedisQueueConsumersService.class);

    private List<RedisConsumer> redisConsumers;

    @Resource(name = "jmpAuthRealNameService")
    private AuthRealNameApi jmpService;
    @Resource(name = "gzAuthRealNameService")
    private AuthRealNameApi gzService;

    private static final String REDIS_QUEUE = "clockInList111";
    private static final String REDIS_GUANGZHOU = "GZAddPo111";
    private static final String REDIS_GUANGZHOU_USER_JT = "GZAddPoUSERJT1";

    @Override
    public void destroy() throws Exception {
        for (RedisConsumer consumer : redisConsumers) {
            consumer.shutdown();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        redisConsumers = new ArrayList<RedisConsumer>();


        /**
         * 广州项目添加班组（出发为添加班组长）发送到对应项目
         */
        RedisConsumer add_po = new RedisConsumerImpl(REDIS_GUANGZHOU, 1, new RedisMessageListener() {
            @Override
            public void consume(Object message) {
                try {
                    logger.info(message.toString());
                    String body = ObjectUtil.bytesToObject((byte[]) message).toString();
                    logger.info(message.toString());
                    JSONObject object = JSONObject.fromObject(body);
                    String poId = String.valueOf(object.get("po_id"));
                    String userId = String.valueOf(object.get("user_id"));
                    String type = String.valueOf(object.get("push_type"));
                    //gzService.synBuildPo(Integer.parseInt(poId), Integer.parseInt(userId),type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);

        /**
         * 广州项目班组工人进退
         */
        RedisConsumer po_user_jt = new RedisConsumerImpl(REDIS_GUANGZHOU_USER_JT, 1, new RedisMessageListener() {
            @Override
            public void consume(Object message) {
                try {
                    logger.info(message.toString());
                    String body = ObjectUtil.bytesToObject((byte[]) message).toString();
                    logger.info(message.toString());
                    JSONObject object = JSONObject.fromObject(body);
                    String poId = String.valueOf(object.get("po_id"));
                    String userId = String.valueOf(object.get("user_id"));
                    String type = String.valueOf(object.get("type"));
                    //gzService.synBuildPoUserJT(Integer.parseInt(poId), Integer.parseInt(userId),type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, true);

        RedisConsumer consumer_cmp_amount_change = new RedisConsumerImpl(REDIS_QUEUE, 1, new RedisMessageListener() {
            @Override
            public void consume(Object message) {
                logger.error("消息队列开始执行");
                try {
                    logger.error("消息队列的参数： " + message.toString());
                    String body = ObjectUtil.bytesToObject((byte[]) message).toString();
                    JSONObject object = JSONObject.fromObject(body);
                    String projectId = String.valueOf(object.get("projectId"));
                    String clockInId = String.valueOf(object.get("clockInId"));
                    jmpService.synWorkerAttendance(Integer.parseInt(projectId), Integer.parseInt(clockInId));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    logger.error("消息队列执行完成");
                }
            }
        }, true);






        consumer_cmp_amount_change.start();
        add_po.start();
        po_user_jt.start();
        logger.info("-----------------------------");
        redisConsumers.add(consumer_cmp_amount_change);

        //redisConsumers.add(add_po);


    }

}
