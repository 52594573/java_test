package com.ktp.project.listener;

import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("");
        logger.info("");
        logger.info("******************* Ｓｙｓｔｅｍ　ｉｎｉｔ ***************** ");
        logger.info("初始化  ***  EnumMap ***  开始");
        ProjectEnum[] values = ProjectEnum.values();
        for (ProjectEnum value : values) {
            EnumMap.projectEnumMap.put(value.getProjectId(), value);
        }
        //spring容器中获取service对象
        GsxAuthRealNameService gsxService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(GsxAuthRealNameService.class);
        JmpAuthRealNameService jmService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(JmpAuthRealNameService.class);
        NnpAuthRealNameService nnService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(NnpAuthRealNameService.class);
        GZProjectService gzService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(GZProjectService.class);
        GmAuthRealNameService gmService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(GmAuthRealNameService.class);
        QzAuthRealNameService qzService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(QzAuthRealNameService.class);
        SdAuthRealNameService sdService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(SdAuthRealNameService.class);
        ZsAuthRealNameService zsService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(ZsAuthRealNameService.class);
        SsAuthRealNameService ssService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(SsAuthRealNameService.class);
        for (ProjectEnum value : values) {
            String pSent = value.getpSent();
            switch (pSent) {
                case "GZ":
                    EnumMap.subclassMap.put(value.getProjectId(), gzService);
                    break;
                case "JM":
                    EnumMap.subclassMap.put(value.getProjectId(), jmService);
                    break;
                case "GSX":
                    EnumMap.subclassMap.put(value.getProjectId(), gsxService);
                    break;
                case "GM":
                    EnumMap.subclassMap.put(value.getProjectId(), gmService);
                    break;
                case "QZ":
                    EnumMap.subclassMap.put(value.getProjectId(), qzService);
                    break;
                case "SD":
                    EnumMap.subclassMap.put(value.getProjectId(), sdService);
                    break;
                case "ZS":
                    EnumMap.subclassMap.put(value.getProjectId(), zsService);
                    break;
                case "NN":
                    EnumMap.subclassMap.put(value.getProjectId(), nnService);
                    break;
                case "SS":
                    EnumMap.subclassMap.put(value.getProjectId(), ssService);
                    break;
                default:
                    logger.error("通过pSent: {} ,找不到对应的枚举值", pSent);
                    throw new BusinessException(String.format("通过pSent: %s ,找不到对应的枚举值", pSent));
            }
        }
        logger.info("初始化  ***  EnumMap  ***  结束");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("******************* Ｓｙｓｔｅｍ　Ｅｘｉｔ *****************");
        logger.info("");
        logger.info("");
    }

    public static void main(String[] args) {


    }
}
