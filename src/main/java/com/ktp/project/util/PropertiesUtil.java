package com.ktp.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Title: properties文件读取工具类
 * Description:
 * Company: Copyright @ 2018
 * @author: mhj
 * @date: 2018年11月20日上午9:30:30
 * @version 1.0 初稿
 */
public class PropertiesUtil {

	/**
	 * 读取Properties的文件
	 * @author lyz
	 * @param propFile
	 * @return
	 */
	public static Properties readConfig(String propFile) {
		Properties p = new Properties();
		try {
			InputStream inputStream = PropertiesUtil.class.getResourceAsStream(propFile);

			p.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return p;
	}

}