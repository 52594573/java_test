package com.ktp.project.dao;

import java.util.List;

import com.ktp.project.entity.SixZj;
import org.apache.ibatis.annotations.Param;

public interface SixZjDao {

	/**
	 * 查询所有的闸机图片
	 * 
	 * @param offset 查询起始位置
	 * @param limit 查询条数
	 * @return
	 */
	List<SixZj> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	int updatePic(@Param("id")int id,@Param("path")String path);

}
