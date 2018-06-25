package com.zyy.pic.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.PictureBank;
import com.zyy.pic.mapper.PictureBankMapper;


/**
 * Created by Administrator on 2017/12/19.
 */
@Repository
public class PictureBankDao {
    @Autowired
    private PictureBankMapper pictureBankMapper;

    public void save(PictureBank pictureBank) {
    	pictureBankMapper.saveBank(pictureBank);
    }
    
    public void save() {
    	pictureBankMapper.saveBankHis();
    }
    
    public void deleteBank(String status) {
    	pictureBankMapper.deleteBank(status);
    }
    
    public List<PictureBank> getPictureBankByStatus(String status) {
    	return pictureBankMapper.getPictureByStatus(status);
    }
    
    public List<PictureBank> getPictureBankByStatus(String status, String date) {
    	return pictureBankMapper.getPictureByStatusAndDate(status, date);
    }
    
    public void updatePictureBank(String oValue, String nValue, Date updateTime) {
    	pictureBankMapper.updateBank(oValue, nValue, updateTime);
    }
}
