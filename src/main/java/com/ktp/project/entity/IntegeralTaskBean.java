package com.ktp.project.entity;

/**
 * 积分任务
 */
public class IntegeralTaskBean {

	//任务剩余次数
	private Integer taskNum;

	//任务总次数
	private Integer taskTatolNum;

	//任务是否完成  0 未完成  1 已完成
	private Integer isfinish;

	//任务存活时间
	private long taskOnlineTime;


	/**
	 * key_name : app考勤打卡
	 * key_id : 22
	 * id : 182
	 * key_value : +1
	 */

	private String key_name;
	private int key_id;
	private int id;
	private String key_value;

	public Integer getTaskTatolNum() {
		return taskTatolNum;
	}

	public void setTaskTatolNum(Integer taskTatolNum) {
		this.taskTatolNum = taskTatolNum;
	}

	public String getKey_name() {
		return key_name;
	}

	public void setKey_name(String key_name) {
		this.key_name = key_name;
	}

	public int getKey_id() {
		return key_id;
	}

	public void setKey_id(int key_id) {
		this.key_id = key_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey_value() {
		return key_value;
	}

	public void setKey_value(String key_value) {
		this.key_value = key_value;
	}


	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public Integer getIsfinish() {
		return isfinish;
	}

	public void setIsfinish(Integer isfinish) {
		this.isfinish = isfinish;
	}

	public long getTaskOnlineTime() {
		return taskOnlineTime;
	}

	public void setTaskOnlineTime(long taskOnlineTime) {
		this.taskOnlineTime = taskOnlineTime;
	}
}
