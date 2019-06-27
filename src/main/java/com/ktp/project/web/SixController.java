package com.ktp.project.web;

import com.ktp.project.entity.SixZj;
import com.ktp.project.logic.QiNiuSixUploadManager;
import com.ktp.project.service.SixService;
import com.ktp.project.util.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 第六镜
 *
 * @author djcken
 * @date 2018/5/19
 */
@Controller
@RequestMapping(value = "six", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class SixController {

    @Autowired
    private SixService sixService;

    @RequestMapping(value = "upload")
    @ResponseBody
    private String upload() {
        ThreadPoolManager.getInstance().setCorePoolSize(100);
        List list = sixService.getSixZjList();
        uploadList(list);
        return "上传任务开始！！";
    }

    @RequestMapping(value = "uploadError")
    @ResponseBody
    private String uploadError() {
        ThreadPoolManager.getInstance().setCorePoolSize(100);
        List list = sixService.getSixZjErrorList();
        System.out.print(list);
        uploadList(list);
        return "上传任务开始！！";
    }

    private void uploadList(List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final SixZj sixZj = (SixZj) list.get(i);
            ThreadPoolManager.getInstance().execute(() -> {
                String fileName = sixZj.getPic1();
                if (fileName != null && !fileName.isEmpty() && fileName.startsWith("/")) {
                    fileName = fileName.substring(1, fileName.length());
                }
                String inUrl = "http://t.ktpis.com/" + fileName;
                QiNiuSixUploadManager.upload(sixService, sixZj.getId(), sixZj.getProId(), sixZj.getCardId(), inUrl, fileName);
            });
        }
    }

}
