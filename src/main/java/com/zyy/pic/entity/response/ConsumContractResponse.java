package com.zyy.pic.entity.response;

import java.io.Serializable;

/**
 * 用户中心返回合同信息
 * @author 
 *
 */
public class ConsumContractResponse implements Serializable {
	private static final long serialVersionUID = -494242434440273143L;
	/**
	 * 处理结果码值
	 */
	private String code;
	/**
	 * 连接
	 */
	private String data;
	/**
	 * 处理结果描述
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
