package com.ktp.project.logic.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * 定时器任务
 *
 * @author djcken
 * @date 2018/5/31
 */
public abstract class AbstractTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger("AbstractTask");

    /**
     * 任务数据
     */
    private TaskData data;

    /**
     * 执行器
     */
    private ScheduleExecutor scheduleExecutor = ScheduleExecutor.instance();

    // 执行任务 由子类实现
    abstract public boolean exec();

    /**
     * 开始任务
     */
    public void start() {
        if (data == null) {
            logger.info("data 为空，不能添加任务");
            return;
        }
        scheduleExecutor.exec(this);
    }

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }

    @Override
    public void run() {
        if (!scheduleExecutor.add(data)) {
            logger.info("任务已添加：" + data.key());
            return;
        }
        if (exec()) {
            scheduleExecutor.complete(data);
        } else {
            scheduleExecutor.failed(data);
        }
    }

    public static void cancelTask(String key) {
        HashMap<String, Future> map = ScheduleExecutor.instance().getFutures();
        if (map != null && map.containsKey(key)) {
            for (String mapKey : map.keySet()) {
                if (mapKey.equals(key)) {
                    Future future = map.get(mapKey);
                    future.cancel(false);
                }
            }
        }
    }
}
