package com.ktp.project.util;

import com.ktp.project.pay.weixin.WXPayXmlUtil;
import org.apache.http.util.TextUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class XmlUtil {

    @SuppressWarnings("rawtypes")
    public static Map<String, String> writeToMap(InputStream in) {
        Map<String, String> map = new HashMap<>();
//        SAXReader saxReader = new SAXReader();
//        try {
//            Document document = saxReader.read(in);
//            Element root = document.getRootElement();
//            if (root.getName().equals("xml")) {
//                List children = root.elements();
//                for (Object child : children) {
//                    if (child instanceof Element) {
//                        Element childEle = (Element) child;
//                        map.put(childEle.getName(), childEle.getStringValue());
//                    }
//                }
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
        try {
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            org.w3c.dom.Document doc = documentBuilder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    map.put(element.getNodeName(), element.getTextContent());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public static <T> T convertoObj(InputStream in, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            Map<String, String> map = writeToMap(in);

            if (map.isEmpty() || t == null) {
                return t;
            }

            ClassUtil.convert(map, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * xml字符串转换成bean对象
     *
     * @param xmlStr xml字符串
     * @param clazz  待转换的class
     * @return 转换后的对象
     */
    public static Object xmlStrToBean(String xmlStr, Class clazz) {
        Object obj = null;
        try {
            // 将xml格式的数据转换成Map对象
            Map<String, Object> map = xmlStrToMap(xmlStr);
            //将map对象的数据转换成Bean对象
            obj = mapToBean(map, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将xml格式的字符串转换成Map对象
     *
     * @param xmlStr xml格式的字符串
     * @return Map对象
     * @throws Exception 异常
     */
    public static Map<String, Object> xmlStrToMap(String xmlStr) {
        if (TextUtils.isEmpty(xmlStr)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
//        //将xml格式的字符串转换成Document对象
//        Document doc = null;
//        try {
//            doc = DocumentHelper.parseText(xmlStr);
//            //获取根节点
//            Element root = doc.getRootElement();
//            //获取根节点下的所有元素
//            List children = root.elements();
//            //循环所有子元素
//            if (children != null && children.size() > 0) {
//                for (int i = 0; i < children.size(); i++) {
//                    Element child = (Element) children.get(i);
//                    map.put(child.getName(), child.getTextTrim());
//                }
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
        try {
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList1 = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList1.getLength(); ++idx) {
                Node node1 = nodeList1.item(idx);
                if (node1.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element1 = (org.w3c.dom.Element) node1;
                    map.put(element1.getNodeName(), element1.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * 将Map对象通过反射机制转换成Bean对象
     *
     * @param map   存放数据的map对象
     * @param clazz 待转换的class
     * @return 转换后的Bean对象
     * @throws Exception 异常
     */
    public static Object mapToBean(Map<String, Object> map, Class clazz) throws Exception {
        Object obj = clazz.newInstance();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String propertyName = entry.getKey();
                Object value = entry.getValue();
                String setMethodName = "set"
                        + propertyName.substring(0, 1).toUpperCase()
                        + propertyName.substring(1);
                Field field = getClassField(clazz, propertyName);
                Class fieldTypeClass = field.getType();
                value = convertValType(value, fieldTypeClass);
                clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
            }
        }
        return obj;
    }

    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     *
     * @param value          Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后的值
     */
    private static Object convertValType(Object value, Class fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }

    /**
     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
     *
     * @param clazz     指定的class
     * @param fieldName 字段名称
     * @return Field对象
     */
    private static Field getClassField(Class clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {// 简单的递归一下
            return getClassField(superClass, fieldName);
        }
        return null;
    }

}
