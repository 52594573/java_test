package com.ktp.project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Lamh on 2018/6/21
 */
public final class PropertiesUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
    private static Properties props;

    static {
        loadProps();
    }

    private static synchronized void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertiesUtils.class.getResourceAsStream("/properties/application.properties");
            props.load(in);
        } catch (Exception e) {
            logger.error("读取配置文件出现异常:", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("文件流关闭出现异常");
            }
        }
    }

    public static String getValue(String key) {
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getValue(String key, String defaultValue) {
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
