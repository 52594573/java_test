package com.ktp.project.web;

import com.google.gson.Gson;
import com.ktp.project.entity.QiniuToken;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.RandomUtil;
import com.ktp.project.util.ResponseUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * sdk接入
 *
 * @author djcken
 * @date 2018/5/10
 */
@Controller
@RequestMapping("api/qiniu")
public class QiNiuController {

    private static final String ACCESS_KEY = "EbWp8FT0dOlaKyldOSpcV3-WQ_RrMhiqpFhYcUQ_";
    private static final String SECRET_KEY = "t4TS9c8BWCWvgR1kky5ZEgYJloqisH9mEhT6SluS";
    private static final String BUCKET = "ktpimages";
    private static final String BUCKET_FILE = "file";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "token", produces = {"text/html;charset=UTF-8;", "application/json;"}, method = RequestMethod.GET)
    @ResponseBody
    private String getQiniuToken(HttpServletRequest request) {
        String key = request.getParameter("key");
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken;
        if (!TextUtils.isEmpty(key)) {
            upToken = auth.uploadToken(BUCKET, key);
        } else {
            upToken = auth.uploadToken(BUCKET);
        }
        logger.debug("qiniu token is " + upToken);
        QiniuToken qiniuToken = new QiniuToken();
        qiniuToken.setToken(upToken);
        return ResponseUtil.createNormalJson(qiniuToken);
    }

    @RequestMapping(value = "filetoken", produces = {"text/html;charset=UTF-8;", "application/json;"}, method = RequestMethod.GET)
    @ResponseBody
    private String getQiniuFileToken(HttpServletRequest request) {
        String key = request.getParameter("key");
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken;
        if (!TextUtils.isEmpty(key)) {
            upToken = auth.uploadToken(BUCKET_FILE, key);
        } else {
            upToken = auth.uploadToken(BUCKET_FILE);
        }
        logger.debug("qiniu token is " + upToken);
        QiniuToken qiniuToken = new QiniuToken();
        qiniuToken.setToken(upToken);
        return ResponseUtil.createNormalJson(qiniuToken);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String upLoadFile(@RequestParam(value = "up_file", required = false) MultipartFile file, @RequestParam(value = "u_id") String userId, @Param(value = "pro_id") String proId, HttpServletRequest request)
            throws IllegalStateException, IOException {
        logger.debug("upload file userId is " + userId);
        Configuration cfg = new Configuration(Zone.zone2());
        if (file != null) {
            // 取得当前上传文件的文件名称
            String myFileName = file.getOriginalFilename();
            // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
            if (!"".equals(myFileName.trim())) {
                String key = proId + "_" + DateUtil.getFormatCurrentTime(DateUtil.FORMAT_DATE_TIME_NORMAL) + RandomUtil.random(1000, 9999) + getSuffixName(myFileName);
                UploadManager uploadManager = new UploadManager(cfg);
                try {
                    Response response = uploadManager.put(file.getInputStream(), key, getToken(),null,null);
                    //解析上传成功的结果
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    System.out.println(putRet.key);
                    System.out.println(putRet.hash);
                    return ResponseUtil.createBussniessJson("上传成功");
                } catch (QiniuException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return ResponseUtil.createBussniessErrorJson(0, "上传失败");
    }

    private String getToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        logger.debug("qiniu token is " + upToken);
        return upToken;
    }

    /**
     * 获取文件的后缀名称
     */
    public static String getSuffixName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
