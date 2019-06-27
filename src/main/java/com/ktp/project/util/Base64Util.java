package com.ktp.project.util;

import com.ktp.project.constant.ImgRatioEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.exception.BusinessException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author djcken
 * @date 2018/5/22
 */
public class Base64Util {

    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);

    public static byte[] decode(String encodedText) {
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }

    public static String encode(byte[] data) {
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    public static String toUTF(String txt) {
        String result = "";
        try {
            result = URLEncoder.encode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String forUTF(String txt) {
        String result = "";
        try {
            result = URLDecoder.decode(txt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * file文件转base64
     *
     * @param path
     * @return
     */
    public static String fileToBase64(String path) {
        if (StringUtils.isBlank(path)){
            throw new BusinessException("图片路径不能为空!");
        }
        path = path.replaceAll("\\\\", "/");
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("图片转换BASE64失败!");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    /**
     * @param imgUrl
     * @param type
     * @return
     */
    public static String compressBase64(String imgUrl, ImgRatioEnum type) {
        String base64 = "noPic";
        if (StringUtils.isBlank(imgUrl)) {
//            throw new BusinessException("图片URL不能为空");
            return base64;
        }
        try {
            base64 = Image2Base64(getFullUrl(imgUrl), 0.3F);
            System.out.println(base64.length());
            if (base64.length() >= type.getSize()) {
                float v = (type.getSize().floatValue() / base64.length()) * 0.3F - 0.02F;
                base64 = Image2Base64(getFullUrl(imgUrl), v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static String getFullUrl(String path){
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("图片路径不能为空");
        }
        if (!path.startsWith("https")){
            path = RealNameConfig.IMG_BASE_PATH + path;
        }
        return path;
    }

    /**
     * 远程读取image转换为Base64字符串
     *
     * @param imgUrl
     * @param ratio
     * @return
     * @throws IOException
     */
    public static String Image2Base64(String imgUrl, Float ratio){
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        ByteArrayInputStream in = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            in = new ByteArrayInputStream(outStream.toByteArray());
            // 对字节数组Base64编码  并压缩
            return new BASE64Encoder().encode(compressOfPercent(in, ratio));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BusinessException(String.format("图片URL: %s 转换为BASE64失败", imgUrl));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

        }
    }

    /**
     * 按指定比例进行缩小和放大: percent=1不变 percent>1放大 percent<1缩小
     *
     * @param input   原图
     * @param percent 压缩比例
     * @return
     * @throws Exception
     */
    public static byte[] compressOfPercent(InputStream input, float percent) {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            Thumbnails.of(input).scale(percent).toOutputStream(output);
            return output.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * 将一张网络图片转化成Base64字符串
     * @param imgURL
     * @return
     */
    public static String GetImageStrFromUrl(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }


    public static void main(String[] args) throws Exception {
        String path = "https://zj.ktpis.com/2116_201901141844174188317.jpg";
        ImgRatioEnum facePhoto = ImgRatioEnum.FACE_PHOTO;
        String s = compressBase64(path, facePhoto);
        System.out.println(s.length());
        System.out.println(s);

    }
}
