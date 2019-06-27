package com.ktp.project.dao;


import com.ktp.project.util.Page;

import java.util.List;

/**
 * 统一查询通道
 * query*：hql查询
 * select*：sql查询
 * Created by LinHon 2018/8/13
 */
public interface QueryChannelDao {

    <T> T queryOne(Class<T> clazz, Integer primaryKey);

    <T> T queryOne(String hql, Class<T> clazz);

    <T> T queryOne(String hql, List<Object> params, Class<T> clazz);

    Long queryCount(String hql);

    Long queryCount(String hql, List<Object> params);

    Double querySum(String hql);

    Double querySum(String hql, List<Object> params);

    <T> T queryOneAndTransformer(String hql, Class<T> clazz);

    <T> T queryOneAndTransformer(String hql, List<Object> params, Class<T> clazz);

    <T> List<T> queryMany(String hql, Class<T> clazz);

    <T> List<T> queryMany(String hql, List<Object> params, Class<T> clazz);

    <T> List<T> queryManyAndTransformer(String hql, Class<T> clazz);

    <T> List<T> queryManyAndTransformer(String hql, List<Object> params, Class<T> clazz);

    <T> Page<T> queryPage(String hql, Page page, Class<T> clazz);

    <T> Page<T> queryPage(String hql, Page page, List<Object> params, Class<T> clazz);

    <T> T selectOne(String sql);

    <T> T selectOne(String sql, List<Object> params);

    <T> T selectOne(String sql, Class<T> clazz);

    <T> T selectOne(String sql, List<Object> params, Class<T> clazz);

    Long selectCount(String sql);

    Long selectCount(String sql, List<Object> params);

    Double selectSum(String sql);

    Double selectSum(String sql, List<Object> params);

    <T> T selectOneAndTransformer(String sql, Class<T> clazz);

    <T> T selectOneAndTransformer(String sql, List<Object> params, Class<T> clazz);

    List selectMany(String sql);

    List selectMany(String sql, List<Object> params);

    <T> List<T> selectMany(String sql, Class<T> clazz);

    <T> List<T> selectMany(String sql, List<Object> params, Class<T> clazz);

    <T> List<T> selectManyAndTransformer(String sql, Class<T> clazz);

    <T> List<T> selectManyAndTransformer(String sql, List<Object> params, Class<T> clazz);

    <T> Page<T> selectPage(String sql, Page page, Class<T> clazz);

    <T> Page<T> selectPage(String sql, Page page, List<Object> params, Class<T> clazz);

}
