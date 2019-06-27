package com.ktp.project.service;

import com.ktp.project.dao.AnnouncementListDao;
import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.dto.WordListDto;
import com.ktp.project.entity.MallGet;
import com.ktp.project.entity.WordList;
import com.ktp.project.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 地址管理
 *
 * @author djcken
 * @date 2018/6/11
 */
@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementListDao announcementListDao;

    @Transactional
    public List<WordListDto> getAnnouncementList(int page, int pageSize, int proId,int userId) {
        page = (page - 1) * pageSize;
        return announcementListDao.getAnnouncementList(page, pageSize, proId,userId);
    }


    @Transactional
    public int insertWordList(WordList wordList) {
        //创建时间
        wordList.setWlIntime(new Date());
        //公告类型为28
        wordList.setWlSort(28);
        return announcementListDao.insertWordList(wordList);
    }

    @Transactional
    public WordListDto getAnnouncementDetails( int id) {
        return announcementListDao.getAnnouncementDetails(id);
    }

    @Transactional
    public List<Map<String,Object>> getAnnouncementListS() {
        List<Map<String,Object>> list = announcementListDao.getWordSort();
        List<Map<String,Object>> result  = new ArrayList<>();
       /* StringBuffer stringBuffer = new StringBuffer();*/
        for (Map<String, Object> objectMap : list) {
            Map<String, Object> map = new HashMap<>();
            Integer id = (Integer) objectMap.get("id");
            map = announcementListDao.getResult(id);
            result.add(map);
           /* stringBuffer.append(id).append(",");*/
        }
        /*stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length()-1);
        result = announcementListDao.getAnnouncement(stringBuffer.toString());*/
        return result;
    }
    @Transactional
    public List<Map<String,Object>> getAnnouncementListShou(Page page) {
        return  announcementListDao.getAnnouncementListShou(page);
    }
}
