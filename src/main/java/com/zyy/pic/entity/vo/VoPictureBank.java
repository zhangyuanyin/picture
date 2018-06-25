package com.zyy.pic.entity.vo;

/**
 * Created by Administrator on 2018/5/16.
 */
public class VoPictureBank {
	/**
	 * 身份证正面照片URL
	 */
	private String frontIdCard;
	/**
	 * 身份证背面照片URL
	 */
	private String reverseIdCrad;
	/**
	 * 活体识别照片URL
	 */
	private String facePluse;
	/**
	 * 合同编号
	 */
	private String contractNo;
	/**
	 * 借据编号
	 */
	private String duebillNo;
	/**
	 * 产品编码
	 */
	private String productCd;
	/**
	 * 系统编号
	 */
	private String sysNo;
	
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

	public String getProductCd() {
		return productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public String getFrontIdCard() {
		return frontIdCard;
	}

	public void setFrontIdCard(String frontIdCard) {
		this.frontIdCard = frontIdCard;
	}

	public String getReverseIdCrad() {
		return reverseIdCrad;
	}

	public void setReverseIdCrad(String reverseIdCrad) {
		this.reverseIdCrad = reverseIdCrad;
	}

	public String getFacePluse() {
		return facePluse;
	}

	public void setFacePluse(String facePluse) {
		this.facePluse = facePluse;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

}
