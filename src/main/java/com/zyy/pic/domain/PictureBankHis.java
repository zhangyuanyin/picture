package com.zyy.pic.domain;

import java.util.Date;

public class PictureBankHis {
	private String appNo;
	private String contractNo;
	private String duebillNo;
	private String sysNo;
	private String status;
	private Date updateTime;
	
	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getDuebillNo() {
		return duebillNo;
	}

	public void setDuebillNo(String duebillNo) {
		this.duebillNo = duebillNo;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
