package com.ktp.project.util;

import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientNewTwoUtils {

    public static final String CONTENT_TYPE = "application/json";

    public static String get(String url, Map<String, String> requestHeader) {
        HttpGet httpGet = new HttpGet(url);
        setRequestHeader(httpGet, requestHeader);
        return getResponse(httpGet);
    }

    public static String  post(String url, Map<String, String> requestParams, String contentType, Map<String, String> requestHeader) {
        HttpPost httpPost = new HttpPost(url);
        setRequestHeader(httpPost, requestHeader);
        if (CONTENT_TYPE.equals(contentType)) {
            if (requestParams != null) {
                String data = JSONObject.fromObject(requestParams).toString();
                StringEntity entity = new StringEntity(data, "UTF-8");
                entity.setContentType(contentType);
                httpPost.setEntity(entity);
            }
        } else {
            try {
                System.out.print("url : " + httpPost.getURI());
                Header[] headers = httpPost.getAllHeaders();
                if (headers != null && headers.length > 0) {
                    for (int i = 0; i < headers.length; i++) {
                        System.out.print(" header :" + headers[i]);
                    }
                }

                /// 处理请求体
                List<NameValuePair> params = new ArrayList<>();
                if (requestParams != null) {
                    for (Map.Entry<String, String> map : requestParams.entrySet()) {
                        params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
                        System.out.print(" " + map.getKey() + " : " + map.getValue() + "\n");
                    }
                }
                UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(params);
                encodedFormEntity.setContentEncoding("UTF-8");
                httpPost.setEntity(encodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return getResponse(httpPost);
    }

    public static String postJson(String url, String requestParamsJson, String contentType, Map<String, String> requestHeader) {
        HttpPost httpPost = new HttpPost(url);
        setRequestHeader(httpPost, requestHeader);
        if (CONTENT_TYPE.equals(contentType)) {
            if (requestParamsJson != null && !"".equals(requestParamsJson)) {


                StringEntity entity = new StringEntity(requestParamsJson, "UTF-8");
                entity.setContentType(contentType);
                httpPost.setEntity(entity);
            }
        }
        return getResponse(httpPost);
    }
    private static String getResponse(HttpRequestBase requestBase) {
        CloseableHttpClient httpClient = getClosebleHttpClient();
        String body = "";
        try {
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = httpClient.execute(requestBase);

            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "UTF-8");
            }

            EntityUtils.consume(entity);
            //释放链接
            response.close();
            System.out.println("body:" + body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }


    /**
     * 设置请求头
     *
     * @param header
     */
    public static void setRequestHeader(HttpRequestBase requestBase, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> item : header.entrySet()) {
                requestBase.setHeader(item.getKey(), item.getValue());
            }
        }
    }

    public static CloseableHttpClient getClosebleHttpClient() {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

}
