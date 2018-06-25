package com.zyy.pic.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.PictureBank;

/**
 * Created by Administrator on 2017/12/19.
 */
@Repository
public interface PictureBankMapper {
	
	@Insert("INSERT INTO PICTURE_BANK_HIS(APP_NO, CONTRACT_NO, DUEBILL_NO, SYS_NO, BUSINESS_DATE, STATUS, CREATE_TIME, UPDATE_TIME) " +
			"SELECT APP_NO, CONTRACT_NO, DUEBILL_NO, SYS_NO, BUSINESS_DATE, STATUS, CREATE_TIME, UPDATE_TIME from PICTURE_BANK WHERE STATUS = '1' ")
    void saveBankHis();

    @Insert("INSERT INTO PICTURE_BANK VALUES (#{appNo}, #{contractNo}, #{duebillNo}, #{sysNo}, #{businessDate}, #{status}, #{createTime}, #{updateTime}) ")
    void saveBank(PictureBank pictureBank);

    @Select("SELECT * FROM PICTURE_BANK WHERE STATUS = #{status} ")
    @Results({
    	@Result(column = "app_no", property = "appNo"),
    	@Result(column = "contract_no", property = "contractNo"),
    	@Result(column = "duebill_no", property = "duebillNo"),
    	@Result(column = "sys_no", property = "sysNo"),
    	@Result(column = "business_date", property = "businessDate"),
    	@Result(column = "status", property = "status"),
    	@Result(column = "create_time", property = "createTime")
    })
    List<PictureBank> getPictureByStatus(@Param("status") String status);
    
    @Select("SELECT * FROM PICTURE_BANK WHERE STATUS = #{status} AND DATE_FORMAT(CREATE_TIME, '%Y-%m-%d') = #{date} ")
    @Results({
    	@Result(column = "app_no", property = "appNo"),
    	@Result(column = "contract_no", property = "contractNo"),
    	@Result(column = "duebill_no", property = "duebillNo"),
    	@Result(column = "sys_no", property = "sysNo"),
    	@Result(column = "business_date", property = "businessDate"),
    	@Result(column = "status", property = "status"),
    	@Result(column = "create_time", property = "createTime")
    })
    List<PictureBank> getPictureByStatusAndDate(@Param("status") String status, @Param("date") String date);
    
    @Update("UPDATE PICTURE_BANK SET STATUS = #{nValue}, UPDATE_TIME = #{updateTime} WHERE STATUS = #{oValue} ")
    void updateBank(@Param("oValue") String oStatusValue, @Param("nValue") String nStatusValue, @Param("updateTime") Date updateTime);
    
    @Delete("DELETE FROM PICTURE_BANK WHERE STATUS = #{status}")
    void deleteBank(@Param("status") String status);
}
