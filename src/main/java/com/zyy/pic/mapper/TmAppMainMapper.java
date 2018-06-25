package com.zyy.pic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.Picture;
import com.zyy.pic.domain.TmAppMain;
import com.zyy.pic.domain.TmAppPrimApplicantInfo;

/**
 * Created by Administrator on 2017/12/19.
 */
@Repository
public interface TmAppMainMapper {

    @Select("SELECT GROUP_CONCAT(app_no) as  app_no FROM tm_app_main WHERE name = #{name} AND id_no LIKE #{idNo}")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    TmAppMain getAppMain(@Param("name") String name,@Param("idNo") String idNo);

    @Select("SELECT GROUP_CONCAT(app_no SEPARATOR '|') as  app_no FROM tm_app_prim_applicant_info WHERE id_no LIKE #{idNo}")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    TmAppMain getAppMainByIdNo(@Param("idNo") String idNo);
    
    @Select("SELECT APP_NO as appNo, CUSTOMER_SOURCE as customerSource, UNIQUE_ID as uniqueId, PRODUCT_CD as productCd FROM tm_app_main  where APP_NO= #{appNo}")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    TmAppMain getAppMainByAppNo(@Param("appNo") String appNo);
    
    @Select("SELECT APP_NO as appNo, UNIQUE_ID as uniqueId, PRODUCT_CD as productCd FROM tm_app_main  where APP_NO= #{appNo}")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    TmAppMain getMfAppMainByAppNo(@Param("appNo") String appNo);
    
    @Select("SELECT APP_NO as appNo, CONTRACT_NO as contractNo, ID_NO as idNo FROM TM_APP_PRIM_APPLICANT_INFO  where APP_NO= #{appNo}")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    TmAppPrimApplicantInfo getApplicantInfoByAppNo(@Param("appNo") String appNo);
    
    @Select("SELECT MAX(t.UPTIME) upTime,t.IMGNAME name,t.ID_NO idNo,t.APP_NO appNo,SYS_NAME sysName,K_ID kId,t.SUBCLASS_SORT subClassSort" +
    		" FROM PICTURE t WHERE t.APP_NO = #{appNo} AND SUBCLASS_SORT IN ('B1','B2','FACE1','FACE2','FACE3','FACE4') GROUP BY t.SUBCLASS_SORT")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    List<Picture> getPictureByAppNo(@Param("appNo") String appNo);
    
    @Select("SELECT t.IMGNAME name,t.ID_NO idNo,t.APP_NO appNo,SYS_NAME sysName,K_ID kId,t.SUBCLASS_SORT subClassSort" +
    		" FROM PICTURE t WHERE t.APP_NO = #{appNo} AND SUBCLASS_SORT IN ('F', 'R')")
    @Results(value = {  @Result(column = "app_no", property = "appNo") })
    List<Picture> getPictureByAppMFNo(@Param("appNo") String appNo);

}
