package com.ktp.project.util.redis;

import com.ktp.project.common.scheduledthreadpool.ThreadFactoryImpl;
import com.ktp.project.util.ObjectUtil;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Title: 对象处理类
 * Description: 手动生成
 * Company: Copyright @ 2016 优宜趣供应链管理有限公司 版权所有
 * @author: 麦豪俊
 * @date: 2016-1-22 11:22:33
 * @version 1.0 初稿
 */
public class RedisConsumerImpl implements RedisConsumer {

	private final RedisMessageListener redisMessageListener;
	// 定时线程
	private final ScheduledExecutorService scheduledExecutorService;
	private final BlockingQueue<Runnable> consumeRequestQueue;
	private final ThreadPoolExecutor consumeExecutor;
	private final String queue;
	private final int classType; //
	private final boolean isSingleThreadConsumer;

	/**
	 * @param queue                  队列名
	 * @param classType              队列获取数据方式 0 表示 字符串String，1表示 字节数组byte[]
	 * @param redisMessageListener   监听方法
	 * @param isSingleThreadConsumer 是否是单线程执行任务，true是，false否
	 */
	public RedisConsumerImpl(String queue, int classType, RedisMessageListener redisMessageListener, boolean isSingleThreadConsumer) {
		this.queue = queue;
		this.classType = classType;
		this.redisMessageListener = redisMessageListener;
		this.isSingleThreadConsumer = isSingleThreadConsumer;
		this.consumeRequestQueue = new LinkedBlockingQueue<Runnable>();
		if (!isSingleThreadConsumer) {
			this.consumeExecutor = new ThreadPoolExecutor(20, 64, 1000 * 60, TimeUnit.MILLISECONDS, this.consumeRequestQueue, new ThreadFactoryImpl("RedisConsumeMessageThread_"));
		} else {
			this.consumeExecutor = null;
		}

//		this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("RedisConsumeMessageScheduledThread_"));
		this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryImpl("RedisConsumeMessageScheduledThread_"));

	}

	@Override
	public void start() {
		// 多线程
		if (!this.isSingleThreadConsumer) {
			this.scheduledExecutorService.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							if (0 == classType) {
								List<String> list = RedisClientTemplate.brpop(0, queue);
								consumeExecutor.submit(new ConsumeRequest(list.get(1)));
							} else if (1 == classType) {
								List<byte[]> list = RedisClientTemplate.brpop(0, ObjectUtil.objectToBytes(queue));
								consumeExecutor.submit(new ConsumeRequest(list.get(1)));
							}
						}
					} catch (Exception e) {

					}
				}
			}, 3000, TimeUnit.MILLISECONDS);
		} else { // 单线程
			this.scheduledExecutorService.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							if (0 == classType) {
								List<String> list = RedisClientTemplate.brpop(0, queue);
								redisMessageListener.consume(list.get(1));
							} else if (1 == classType) {
								List<byte[]> list = RedisClientTemplate.brpop(0, ObjectUtil.objectToBytes(queue));
								redisMessageListener.consume(list.get(1));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 3000, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void shutdown() {
		this.scheduledExecutorService.shutdown();
		if (this.consumeExecutor != null) {
			this.consumeExecutor.shutdown();
		}
	}

	@Override
	public void subscribe(String queue, RedisMessageListener listener) {

	}

	class ConsumeRequest implements Runnable {
		private Object message;

		public ConsumeRequest(Object message) {
			this.message = message;
		}

		@Override
		public void run() {
			redisMessageListener.consume(message);
		}
	}

}
