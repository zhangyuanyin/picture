package com.zyy.pic.entity.vo;

import java.io.Serializable;

/**
 * <dt>FTP上传文件配置信息</dt>
 * 
 * @author yyzhang
 * @since 2018年5月25日10:51:42
 */
public class VoFtpConfig implements Serializable{
	private static final long serialVersionUID = -7956895205452114538L;
	
	/**
	 * IP
	 */
	private String ip;
	/**
	 * PORT
	 */
	private String port;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 文件上传路径
	 */
	private String filepath;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}
