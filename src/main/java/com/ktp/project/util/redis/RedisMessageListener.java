package com.ktp.project.util.redis;


public interface RedisMessageListener {
	/**
     * 消费消息接口，由应用来实现<br>
     * 
     * @param message
     *            消息
     */
    public void consume(final Object message);
}
