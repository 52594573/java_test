package com.ktp.project.logic;

import com.google.gson.Gson;
import com.ktp.project.service.SixService;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QiNiuSixUploadManager {

    private static final String ACCESS_KEY = "EbWp8FT0dOlaKyldOSpcV3-WQ_RrMhiqpFhYcUQ_";
    private static final String SECRET_KEY = "t4TS9c8BWCWvgR1kky5ZEgYJloqisH9mEhT6SluS";
    private static final String BUCKET = "ktpsix";
    private static Logger logger = LoggerFactory.getLogger("QiNiuSixUploadManager");

    public static String getFileNameInUrl(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }

    public static int upload(SixService sixService, int id, int proId, String cardId, String inUrl, String filePath) {
        int success = -1;
        InputStream inputStream;
        try {
            inputStream = inputStream(inUrl);
        }catch (Exception e){
            success = 0;
            return success;
        }
        if(inputStream!=null){
            Configuration cfg = new Configuration(Zone.zone2());
            if (filePath.trim() != "") {
                UploadManager uploadManager = new UploadManager(cfg);
                try {
                    String key = proId + "_" + cardId + "_" + getFileNameInUrl(filePath);
                    Response response = uploadManager.put(inputStream, key, getToken(), null, null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    System.out.println(putRet.key);
                    System.out.println(putRet.hash);
                    logger.debug("上传成功！"+inUrl + " key is " + putRet.key + " hash is " + putRet.hash);
                    success = 1;
                    sixService.updateSixZj(id,"https://zj.ktpis.com/"+key);
                } catch (QiniuException ex) {
                    logger.debug("上传失败了！"+inUrl);
                    success = -1;
                }
            }
        }else{
            sixService.updateSixZj(id,"error");
        }
        return success;
    }

    /**
     * 通过链接获取imputstream
     */
    private static InputStream inputStream(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        logger.debug("qiniu token is " + upToken);
        return upToken;
    }

}
