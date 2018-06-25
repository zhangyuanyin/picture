package com.zyy.pic.service;

import java.util.Map;

public interface BatchExceptionService {
	/**
	 * 处理读取资管放款文件异常
	 */
	Map<String, Object> updateStatus4ReadEcmsFileException(String date);
	
	/**
	 * 处理下载文件异常
	 */
	Map<String, Object> downloadFile4ConnetCustomException(String date);
	
	/**
	 * 处理上传文件异常
	 */
	Map<String, Object> uploadFile4ConnectFtpException(String date);
}
