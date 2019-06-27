package com.ktp.project.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author 麦豪俊
 * @version V1.0
 * @Title:FreemarkerParseFactory
 * @description:Freemarker引擎协助类
 * @date 2016-2-17 11:12:13
 */
public class FreemarkerFactory {

	private static final Logger logger = LoggerFactory.getLogger(FreemarkerFactory.class);

	private static final String ENCODE = "utf-8";
	/**
	 * 文件缓存
	 */
	private static final Configuration _tplConfig = new Configuration(Configuration.VERSION_2_3_28);

	private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

	// 使用内嵌的(?ms)打开单行和多行模式
	private final static Pattern p = Pattern.compile("(?ms)/\\*.*?\\*/|^\\s*//.*?$");

	static {
		_tplConfig.setTemplateLoader(stringTemplateLoader);
		_tplConfig.setClassForTemplateLoading(new FreemarkerFactory().getClass(), "/ftl");
		_tplConfig.setNumberFormat("0.#####################");
	}

	private static String fileType = ".ftl";

	/**
	 * 判断模板是否存在
	 */
	public static boolean isExistTemplate(String tplName) {
		try {
			Template mytpl = _tplConfig.getTemplate(tplName, "UTF-8");
			if (mytpl == null) {
				return false;
			}
		} catch (Exception e) {
			logger.debug("Freemarker-Path Error!");
			return false;
		}
		return true;
	}

	/**
	 * 解析ftl模板
	 *
	 * @param tplName 模板名
	 * @param paras   参数
	 * @return
	 */
	public static String parseTemplate(String tplName, Map<String, Object> paras) {
		try {
			StringWriter swriter = new StringWriter();
			Template mytpl = _tplConfig.getTemplate(tplName, ENCODE);
			mytpl.process(paras, swriter);
			return getContext(swriter.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e.fillInStackTrace());
			throw new RuntimeException("解析Freemarker模板异常");
		}
	}

	/**
	 * 除去无效字段，去掉注释 不然批量处理可能报错 去除无效的等于
	 */
	private static String getContext(String context) {
		// 将注释替换成""
		context = p.matcher(context).replaceAll("");
//    	context = context.replaceAll("\\n", " ").replaceAll("\\t", " ")
//                .replaceAll("\\s{1,}", " ").trim();

		return context;
	}

	/**
	 * 解析Freemarker模板
	 *
	 * @param claz
	 * @param templateName
	 * @param paramsMap
	 * @return 模版的内容
	 */
	public static String parseTemplate(Class<?> claz, String templateName, Map<String, Object> paramsMap) {
		return parseTemplate(claz, templateName, paramsMap, fileType);
	}

	/**
	 * 解析Freemarker模板
	 *
	 * @param claz
	 * @param templateName
	 * @param paramsMap
	 * @param fileType
	 * @return 模版内容
	 */
	public static String parseTemplate(Class<?> claz, String templateName, Map<String, Object> paramsMap, String fileType) {
		// step.1.根据当前文件的路径，获取Freemarker模板文件的路径
		String context = null;

		// step.2.获取Freemarker模板内容
		// step.3.通过模板引擎给Freemarker模板装载参数,解析成显示的内容
		if (StringUtils.isNotEmpty(templateName)) {

			String templetPath = claz.getPackage().getName().replace(".", "/") + "/" + templateName + fileType;

			if (isExistTemplate(templetPath)) {
				context = parseTemplate(templetPath, paramsMap);
			}
			logger.debug("Freemarker-Path:" + templetPath);

		}
		return context;
	}

}
