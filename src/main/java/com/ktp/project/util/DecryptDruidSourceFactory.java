package com.ktp.project.util;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Title: 数据库用户名和密码加密
 * Description: Druid数据连接处理类
 * Company: Copyright @ 2016 优宜趣供应链管理有限公司 版权所有
 *
 * @version 1.0 初稿
 * @author: 麦豪俊
 * @date: 2016-5-13 11:12:12
 */
public class DecryptDruidSourceFactory extends DruidDataSource {

	private static final long serialVersionUID = 1L;
	private static String [] keyPair = new String[2];

	static {
		try {
			keyPair = ConfigTools.genKeyPair(512);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setUsername(String username) {
		try {
			username = ConfigTools.decrypt(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setUsername(username);
	}

	public static void main(String args[]) throws Exception {
		System.out.println("测试用户名加密: " + ConfigTools.encrypt("ktp_test"));
		System.out.println("测试密码加密: " + ConfigTools.encrypt("Ktp!@#456123"));
		System.out.println("测试用户名解密: " + ConfigTools.decrypt("OYJFDRXGQwI0eFAnXq+Lg50k3l2iBwSfKZjguNQWkQ4+Jxwesc9pUp4EPrQuXXybd2aRW8uevfMP4OX3fooKEA=="));
		System.out.println("测试密码解密: " + ConfigTools.decrypt("nxo1Nv+gXl2tAoHhfyhIKoAWtD8eKOHWXmSm1psSr5kGv/VbWVqzuMahSpMHvk1gaLPJqNvNHxRvcrGGcqtdyQ=="));

		System.out.println("用户名加密: " + ConfigTools.encrypt("ktp_test"));
		System.out.println("密码加密: " + ConfigTools.encrypt("Ktpsys#@!852456741"));
		System.out.println("用户名解密: " + ConfigTools.decrypt("OYJFDRXGQwI0eFAnXq+Lg50k3l2iBwSfKZjguNQWkQ4+Jxwesc9pUp4EPrQuXXybd2aRW8uevfMP4OX3fooKEA=="));
		System.out.println("密码解密: " + ConfigTools.decrypt("D5q5s0/XqWA8aog0DqATl3JSR8PCPNNCfkOYbGvVmuvhRS8RlhnR5fV3STTagPM08jriMq9yMBMVcsb8kw/0YA=="));
	}
}