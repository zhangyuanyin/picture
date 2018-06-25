package com.zyy.pic.service;

import java.util.Date;

/**
 * <dt>银行影像服务接口</dt>
 * @author yyzhang
 * @since 2018年6月20日15:28:23
 */
public interface PictureBankService {
	
	/**
	 * 从指定的FTP服务器上下载文件
	 * @param date
	 */
	void downloadFromFtp(Date date);
	
	/**
	 * 将文件上传至FTP
	 * @param date
	 */
	void uploadToFtp(Date date);
	
	/**
	 * 檢查文件是否上傳成功,否則重新上傳
	 */
	void checkExists();
}
