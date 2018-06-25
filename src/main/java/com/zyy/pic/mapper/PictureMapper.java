package com.zyy.pic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.Picture;

/**
 * <dt>影像系统图片表数据处理类</dt>
 * @author yyzhang
 * @since 2018年6月20日16:01:30
 */
@Repository
public interface PictureMapper {
	
	@Select("SELECT * FROM PICTURE WHERE APP_NO = #{appNo}")
	@Results({
		@Result(column = "app_no", property = "appNo"),
		@Result(column = "id_no", property = "idNo"),
		@Result(column = "imgname", property = "imgname"),
		@Result(column = "sys_name", property = "sysName"),
		@Result(column = "k_id", property = "kId"),
		@Result(column = "uptime", property = "uptime"),
		@Result(column = "subclass_sort", property = "subClassSort"),
		@Result(column = "pic_flag", property = "picFlag"),
	})
	List<Picture> getPicturesByAppNo(@Param("appNo") String appNo);
}
