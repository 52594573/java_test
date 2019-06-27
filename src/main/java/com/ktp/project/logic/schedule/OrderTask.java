package com.ktp.project.logic.schedule;

import com.ktp.project.logic.ApplicationContextHelper;
import com.ktp.project.service.MallOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单task
 *
 * @author djcken
 * @date 2018/5/31
 */
public class OrderTask extends AbstractTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public OrderTask(Data data) {
        setData(data);
    }

    @Override
    public boolean exec() {
        logger.debug("定时任务开始执行！");
        Data data = (Data) getData();
        if (data != null) {
            MallOrderService mallGoodService = ApplicationContextHelper.getBean(MallOrderService.class);
            mallGoodService.cancelMallOrder(data.getUserId(), data.getOutTradeNo());
            logger.debug("mallGoodService is null or not " + mallGoodService);
        }
        return true;
    }

    public static class Data implements TaskData {

        private int userId;
        private String outTradeNo;

        public Data(int userId, String outTradeNo) {
            this.userId = userId;
            this.outTradeNo = outTradeNo;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        @Override
        public String key() {
            return outTradeNo;
        }
    }
}
