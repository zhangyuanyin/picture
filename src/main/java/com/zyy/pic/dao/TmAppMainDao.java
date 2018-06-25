package com.zyy.pic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zyy.pic.domain.Picture;
import com.zyy.pic.domain.TmAppMain;
import com.zyy.pic.domain.TmAppPrimApplicantInfo;
import com.zyy.pic.mapper.TmAppMainMapper;


/**
 * Created by Administrator on 2017/12/19.
 */
@Repository
public class TmAppMainDao {
    @Autowired
    private TmAppMainMapper tmAppMainMapper;

    public TmAppMain getAppMain(String name,String idNo){
        return tmAppMainMapper.getAppMain(name, idNo);
    }

    public TmAppMain getAppMainByIdNo(String idNo){
        return tmAppMainMapper.getAppMainByIdNo(idNo);
    }
    
    public TmAppMain getAppMainByAppNo(String appNo){
        return tmAppMainMapper.getAppMainByAppNo(appNo);
    }
    
    public TmAppMain getMfAppMainByAppNo(String appNo){
    	return tmAppMainMapper.getMfAppMainByAppNo(appNo);
    }
    
    public TmAppPrimApplicantInfo getApplicantInfoByAppNo(String appNo){
    	return tmAppMainMapper.getApplicantInfoByAppNo(appNo);
    }
    
    public List<Picture> getPictureByAppNo(String appNo){
        return tmAppMainMapper.getPictureByAppNo(appNo);
    }
    
    public List<Picture> getPictureByAppMFNo(String appNo){
        return tmAppMainMapper.getPictureByAppMFNo(appNo);
    }
}
