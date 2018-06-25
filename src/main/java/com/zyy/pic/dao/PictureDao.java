package com.zyy.pic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.Picture;
import com.zyy.pic.mapper.PictureMapper;


/**
 * <dt>影像系统图片表数据处理类</dt>
 * @author yyzhang
 * @since 2018年6月20日16:08:49
 */
@Repository
public class PictureDao {
	@Autowired
	private PictureMapper pictureMapper;
	
	public List<Picture> getPicturesByAppNo(String appNo) {
		return pictureMapper.getPicturesByAppNo(appNo);
	}
}
