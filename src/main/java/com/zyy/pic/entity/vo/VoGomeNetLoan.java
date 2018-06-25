package com.zyy.pic.entity.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Administrator on 2018/5/16.
 */
public class VoGomeNetLoan implements Serializable {
	/**
	 * 身份证正面照片URL
	 */
    private String frontIdCard;
    /**
     * 身份证背面照片URL
     */
    private String backIdCrad;
    /**
     * 活体识别照片URL
     */
    private String livingPhoto;
    /**
     * 未查询到的编码
     */
    @JsonIgnore
    private String code;

    public String getFrontIdCard() {
        return frontIdCard;
    }

    public void setFrontIdCard(String frontIdCard) {
        this.frontIdCard = frontIdCard;
    }

    public String getBackIdCrad() {
        return backIdCrad;
    }

    public void setBackIdCrad(String backIdCrad) {
        this.backIdCrad = backIdCrad;
    }

    public String getLivingPhoto() {
        return livingPhoto;
    }

    public void setLivingPhoto(String livingPhoto) {
        this.livingPhoto = livingPhoto;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
