package com.ktp.project.dao;


/**
 * 统一更新通道
 * Created by LinHon 2018/8/13
 */
public interface ModifyChannelDao {

    void save(Object object);

    void update(Object object);

    void saveOrUpdate(Object object);

    void delete(Object object);

    void delete(Class clazz, int primaryKey);

    void executeUpdate(String sql, Object... params);

    void evict(Object params);

}
