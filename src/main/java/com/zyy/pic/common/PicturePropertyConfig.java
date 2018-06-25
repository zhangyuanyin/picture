package com.zyy.pic.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pic")
public class PicturePropertyConfig {
	private String mjappid;
	private String mjsecret;

	private String mfappid;
	private String mfsecret;

	private String mfgappid;
	private String mfgsecret;

	private String dnfilepath;
	private String svfilepath;
	private String upfilepath;
	
	private String comsumeurl;
	
	public String getUpfilepath() {
		return upfilepath;
	}

	public void setUpfilepath(String upfilepath) {
		this.upfilepath = upfilepath;
	}

	public String getComsumeurl() {
		return comsumeurl;
	}

	public void setComsumeurl(String comsumeurl) {
		this.comsumeurl = comsumeurl;
	}

	public String getMjappid() {
		return mjappid;
	}

	public void setMjappid(String mjappid) {
		this.mjappid = mjappid;
	}

	public String getMjsecret() {
		return mjsecret;
	}

	public void setMjsecret(String mjsecret) {
		this.mjsecret = mjsecret;
	}

	public String getMfappid() {
		return mfappid;
	}

	public void setMfappid(String mfappid) {
		this.mfappid = mfappid;
	}

	public String getMfsecret() {
		return mfsecret;
	}

	public void setMfsecret(String mfsecret) {
		this.mfsecret = mfsecret;
	}

	public String getMfgappid() {
		return mfgappid;
	}

	public void setMfgappid(String mfgappid) {
		this.mfgappid = mfgappid;
	}

	public String getMfgsecret() {
		return mfgsecret;
	}

	public void setMfgsecret(String mfgsecret) {
		this.mfgsecret = mfgsecret;
	}

	public String getDnfilepath() {
		return dnfilepath;
	}

	public void setDnfilepath(String dnfilepath) {
		this.dnfilepath = dnfilepath;
	}

	public String getSvfilepath() {
		return svfilepath;
	}

	public void setSvfilepath(String svfilepath) {
		this.svfilepath = svfilepath;
	}
}
