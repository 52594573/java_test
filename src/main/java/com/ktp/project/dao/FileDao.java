package com.ktp.project.dao;

import com.ktp.project.dto.file.FileListDto;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-12-17 上午 11:22
 */
@Repository
public class FileDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 查询项目文件
     * @param fsId
     * @param uId
     * @param proId
     * @param key
     * @return
     */
    public List<FileListDto> getFileList(Integer fsId, Integer uId, Integer proId, String key) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT l.id,l.fs_id,l.fl_filetype,l.fl_intime,l.fl_url,l.fl_size,l.fl_info," +
                "l.fl_uid,u.u_name,u.u_realname,s.fs_name,s.fs_type,k.key_name fs_type_name " +
                "FROM file_list l JOIN file_sort s ON l.fs_id = s.id " +
                "JOIN key_content k ON k.id = s.fs_type " +
                "LEFT JOIN user_info u ON u.id = s.fs_uid " +
                "WHERE l.is_del = 0 AND s.is_del = 0 ");
        if (fsId != null && fsId > 0) {
            sql.append("AND l.fs_id = :fsId ");
        }
        if (proId != null) {
            sql.append("AND s.pro_id = :proId ");
        }
//        if (uId != null) {
//            sql.append("AND ((s.fs_type = 124 AND s.fs_uid = :uId ) OR s.fs_type = 123 OR s.fs_type = 122) ");
//        }
        if (StringUtils.isNotBlank(key)) {
            sql.append("AND l.fl_info LIKE :key ");
        }
        sql.append("ORDER BY l.fl_intime DESC");
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql.toString());
        if (fsId != null && fsId > 0) {
            sqlQuery.setParameter("fsId", fsId);
        }
        if (proId != null) {
            sqlQuery.setParameter("proId", proId);
        }
//        if (uId != null) {
//            sqlQuery.setParameter("uId", uId);
//        }
        if (StringUtils.isNotBlank(key)) {
            sqlQuery.setParameter("key", "%" + key + "%");
        }
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(FileListDto.class));
        List<FileListDto> fileList = sqlQuery.list();
        return fileList;
    }
}
