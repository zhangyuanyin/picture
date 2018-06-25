package com.zyy.pic.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zyy.pic.service.PictureBankService;
import com.zyy.pic.service.TmAppMainService;
import com.zyy.pic.util.PictureCommonUtil;
import com.zyy.pic.util.RedisUtil;

@Component
public class ScheduleJobs {
	@Autowired
	private TmAppMainService tmAppMainService;
	@Autowired
	private PictureBankService pictureBankService;
	@Autowired
	private PictureCommonUtil commonUtil;
	@Autowired
	private RedisUtil redis;
	
	/*@Scheduled(cron = "0 0/10 * * * ?")
    public void fixedDelayJob(){
		tmAppMainService.getAppMain("MJ201804131000165058");
    }*/
	/**
	 * 定时从ftp指定文件夹中读取文件
	 */
	@Scheduled(cron = "0 0 13 * * ?")
	public void downloadFromFtp() {
		String key = "bank_dnlock";
		if(commonUtil.lock(key)) {
			try{
				pictureBankService.downloadFromFtp(new Date());
			} finally {
				redis.remove(key);
			}
		}
	}
	
	/**
	 * 定时将待处理数据对应文件下载至本地,并传至指定ftp文件夹下
	 */
	@Scheduled(cron = "0 30 13 * * ?")
	public void upload4Bank() {
		String key = "bank_uplock";
		if(commonUtil.lock(key)) {
			try{
				pictureBankService.uploadToFtp(new Date());
			} finally {
				redis.remove(key);
			}
		}
	}
	
	// 定期删除压缩文件(待确定)
	// 定时检查当日文件是否上传成功
	@Scheduled(cron = "0 0 16,18 * * ?")
	public void checkExists() {
		String key = "bank_celock";
		if(commonUtil.lock(key)) {
			try{
				pictureBankService.checkExists();
			} finally {
				redis.remove(key);
			}
		}
	}
}
