package com.ktp.project.util;

import com.ktp.project.entity.KtpCloundTokenBean;
import com.ktp.project.exception.BusinessException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by LinHon 2018/8/15
 */
public class HttpClientKTPCloundUtils {
    static String ktpGod;

    static {
        Properties prop = PropertiesUtil.readConfig("/properties/application.properties");
        ktpGod = prop.getProperty("ktpCloud.domain");
    }

    /**
     * GET
     *
     * @param url
     * @return
     */
    public static String get(String url, Map<String, String> requestHeader) {
        GetMethod get = new GetMethod(url);
        setRequestHeader(get, requestHeader);
        get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        return buildResponse(get);
    }


    /**
     * POSTHttpClientUtils
     *
     * @param url
     * @param data
     * @param contentType
     * @return
     */
    public static String post(String url, String data, String contentType, Map<String, String> requestHeader) {
        PostMethod post = new PostMethod(url);
        setRequestHeader(post, requestHeader);
        try {
            if (data != null) {
                RequestEntity entity = new StringRequestEntity(data, contentType, "UTF-8");
                post.setRequestEntity(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException("[HTTP请求体设置异常]", e);
        }
        return buildResponse(post);
    }


    /**
     * PUT
     *
     * @param url
     * @param data
     * @param contentType
     * @return
     */
    public static String put(String url, String data, String contentType, Map<String, String> requestHeader) {
        PutMethod put = new PutMethod(url);
        setRequestHeader(put, requestHeader);
        try {
            if (data != null) {
                RequestEntity entity = new StringRequestEntity(data, contentType, "UTF-8");
                put.setRequestEntity(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException("[HTTP请求体设置异常]", e);
        }
        return buildResponse(put);
    }


    /**
     * DELETE
     *
     * @param url
     * @return
     */
    public static String delete(String url, Map<String, String> requestHeader) {
        DeleteMethod delete = new DeleteMethod(url);
        setRequestHeader(delete, requestHeader);
        delete.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        return buildResponse(delete);
    }


    /**
     * 设置请求头
     *
     * @param httpMethod
     * @param header
     */
    public static void setRequestHeader(HttpMethod httpMethod, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> item : header.entrySet()) {
                httpMethod.setRequestHeader(item.getKey(), item.getValue());
            }
        }
    }


    /**
     * 构建HTTP请求返回内容
     *
     * @param httpMethod
     * @return
     */
    public static String buildResponse(HttpMethod httpMethod) {
        try {
            int status = getHttpClient().executeMethod(httpMethod);

            if (200 == status) {
                return getResponse(httpMethod.getResponseBodyAsStream());
            }
            throw new BusinessException(String.valueOf(status));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("接口异常", e);
        } finally {
            httpMethod.releaseConnection();
        }
    }


    /**
     * 取得HttpClient对象
     *
     * @return
     */
    public static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
        managerParams.setConnectionTimeout(100000);
        managerParams.setSoTimeout(100000);
        return httpClient;
    }


    /**
     * 取得HTTP请求返回内容
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getResponse(InputStream inputStream) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuilder result = new StringBuilder();
        while ((str = bufferedReader.readLine()) != null) {
            result.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return result.toString();
    }

    public static String saveIntegalTask(Integer userId, Integer integeralTaskId) {
        Map<String, String> date = new HashMap<>();
        date.put("username", "admin");
        date.put("password", "123456");
        date.put("scope", "server");
        date.put("grant_type", "password");
        Map<String, String> heard = new HashMap<>();
        heard.put("Authorization", "Basic YXBwOk5Ja2s2OGVfM0FEYVM=");

        String data = JSONArray.fromObject(date).toString();

        String post = HttpClientUtils.post(ktpGod + "/auth/app/token/login?grant_type=app_mobile&app_mobile=APP_SMS@eyJ1c2VySW1lYSI6IjEyMzQ1NiIsInBob25lIjoiMTcwMzQ2NDI4ODgiLCJ2ZXJzaW9uIjoidjEuMCJ9", data, "application/x-www-form-urlencoded", heard);

        KtpCloundTokenBean ktpCloundTokenBean = GsonUtil.fromJson(post, KtpCloundTokenBean.class);
        String access_token = ktpCloundTokenBean.getAccess_token();
        Map<String, String> heard1 = new HashMap<>();
        heard1.put("Authorization", "Bearer " + access_token);
//        heard1.put("Authorization", "Basic YXBwOk5Ja2s2OGVfM0FEYVM=");
//        heard1.put("Content-Type", "application/x-www-form-urlencoded");
//        heard1.put("Content-Type", "application/json;charset=UTF-8");


        Map<String, Object> date1 = new HashMap<>();
        date1.put("integeralTaskId", integeralTaskId);
        date1.put("userId", userId);
        String data1 = GsonUtil.toJson(date1);
        System.out.println(data1);

//        String post1 = HttpClientUtils.post("http://192.168.1.225:30131/business/integralact/saveIntegralFraction1", data1, "application/json", heard1);
        String post1 = HttpClientUtils.post(ktpGod + "/business/integralact/saveIntegralFraction1", data1, "application/json", heard1);

        System.out.println(post1);
        return post1;
    }

}
