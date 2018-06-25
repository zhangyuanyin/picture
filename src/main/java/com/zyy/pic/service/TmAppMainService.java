package com.zyy.pic.service;

import com.zyy.pic.domain.Picture;
import com.zyy.pic.domain.TmAppMain;
import com.zyy.pic.entity.vo.VoGomeNetLoan;

/**
 * Created by Administrator on 2017/12/19.
 */
public interface TmAppMainService {
    public TmAppMain getAppMain(String name, String idNo);
    public TmAppMain getAppMainByIdNo(String idNo);
    public TmAppMain getAppMain(String appNo);
    public VoGomeNetLoan getAppMainByAppNo(String appNo);
    public StringBuffer getPictureDefaultUrl(Picture picture, String appNo);
}
