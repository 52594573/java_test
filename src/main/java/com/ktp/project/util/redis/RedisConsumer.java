package com.ktp.project.util.redis;


public interface RedisConsumer {
	/**
	 * 启动服务
	 */
	public void start();


	/**
	 * 关闭服务
	 */
	public void shutdown();


	/**
	 * 订阅消息
	 *
	 * @param queue    任务队列名
	 * @param listener 消息回调监听器
	 */
	public void subscribe(final String queue, final RedisMessageListener listener);

}
