package com.ktp.project.service;

import com.ktp.project.dao.FileDao;
import com.ktp.project.dto.file.FileListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-12-17 下午 14:01
 */
@Service
@Transactional
public class FileService {
    @Autowired
    private FileDao fileDao;

    /**
     * 查询项目文件
     * @param fsId
     * @param uId
     * @param proId
     * @param s_key
     * @return
     */
    public List<FileListDto> getFileList(Integer fsId, Integer uId, Integer proId,String key) {
        List<FileListDto> fileList = fileDao.getFileList(fsId, uId, proId,key);
        return fileList;
    }
}
