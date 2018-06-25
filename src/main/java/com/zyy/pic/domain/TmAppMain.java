package com.zyy.pic.domain;

/**
 * Created by Administrator on 2017/12/19.
 */
public class TmAppMain {
	/**
	 * 申请编号
	 */
	private String appNo;
	/**
	 * 美卡客户来源-白条激活客户(GMBTC),美卡激活用户(GMMKC)
	 */
	private String customerSource;
	/**
	 * 全局唯一ID
	 */
	private String uniqueId;
	/**
	 * 产品编码
	 */
	private String productCd;

	public String getProductCd() {
		return productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(String customerSource) {
		this.customerSource = customerSource;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

}
