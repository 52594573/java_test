package com.ktp.project.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeMap;


/**
 * Title: Map的处理类
 * Description: 手动生成 
 * Company: Copyright @ 2016 优宜趣供应链管理有限公司 版权所有
 * @author: 麦豪俊
 * @date: 2016-5-17 17:59:58
 * @version 1.0 初稿
 */
public class MapUtil {
    /** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */ 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static TreeMap convertBeanToTreeMap(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException { 
        Class type = bean.getClass();
		TreeMap returnMap = new TreeMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type); 

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
        for (int i = 0; i< propertyDescriptors.length; i++) { 
            PropertyDescriptor descriptor = propertyDescriptors[i]; 
            String propertyName = descriptor.getName(); 
            if (!propertyName.equals("class")) { 
                Method readMethod = descriptor.getReadMethod(); 
                Object result = readMethod.invoke(bean, new Object[0]); 
                if (result != null) { 
                    returnMap.put(propertyName, result); 
                } else { 
                    returnMap.put(propertyName, ""); 
                } 
            } 
        } 
        return returnMap; 
    } 

}
