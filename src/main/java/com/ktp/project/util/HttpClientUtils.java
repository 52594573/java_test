package com.ktp.project.util;

import com.ktp.project.exception.BusinessException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by LinHon 2018/8/15
 */
public class HttpClientUtils {

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
    public static String getJsonData(JSONObject jsonParam, String urls) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
//            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "text/html;charset=UTF-8");
            // 开始连接请求
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write((jsonParam.toString()).getBytes());
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");

            }

        } catch (Exception e) {

        }

        return sb.toString();

    }

}
