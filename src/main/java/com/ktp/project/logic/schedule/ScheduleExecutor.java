package com.ktp.project.logic.schedule;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 定时器
 *
 * @author djcken
 * @date 2018/5/31
 */
public class ScheduleExecutor {

    private static ScheduleExecutor scheduleExecutor = new ScheduleExecutor();

    public static ScheduleExecutor instance() {
        return scheduleExecutor;
    }

    private ScheduleExecutor() {
    }
    /**
     * 首次任务延迟时间
     */
    private final static int FIRST_TASK_DELAY = 30;
    /**
     * 首次任务延迟时间
     */
    private final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    /**
     * 最小线程池数量
     */
    private final static int MIN_POOL_SIZE = 8;
    /**
     * 执行器
     */
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(MIN_POOL_SIZE);
    /**
     * 待执行任务集合
     */
    private ConcurrentHashMap<String, TaskData> tasks = new ConcurrentHashMap<>();
    /**
     * feture集合
     */
    private HashMap<String, Future> futures = new HashMap<>();

    /**
     * 执行任务
     *
     * @param task 任务
     */
    void exec(AbstractTask task) {
        exec(task, FIRST_TASK_DELAY);
    }

    /**
     * 执行任务
     *
     * @param task  任务
     * @param delay 延迟
     */
    void exec(AbstractTask task, int delay) {
        exec(task, delay, DEFAULT_TIME_UNIT);
    }

    /**
     * 执行任务
     *
     * @param task  任务
     * @param delay 延迟
     */
    void exec(AbstractTask task, int delay, TimeUnit timeUnit) {
        Future future = service.schedule(task, delay, timeUnit);
        futures.put(task.getData().key(), future);
    }

    /**
     * 添加任务
     *
     * @param data 任务数据
     */
    boolean add(TaskData data) {
        if (getTasks().containsKey(data.key()))
            return false;
        getTasks().put(data.key(), data);
        return true;
    }

    /**
     * 任务执行成功
     *
     * @param data 任务数据
     */
    void complete(TaskData data) {
        getTasks().remove(data.key());
    }

    /**
     * 任务执行失败
     */
    void failed(TaskData data) {
        getTasks().remove(data.key());
    }

    /**
     * 停止接收任务
     */
    void stop() {
        service.shutdown();
    }

    /**
     * 停止接受任务并结束正在执行的任务
     */
    void stopNow() {
        service.shutdownNow();
    }

    public ConcurrentHashMap<String, TaskData> getTasks() {
        return tasks;
    }

    public HashMap<String, Future> getFutures() {
        return futures;
    }
}