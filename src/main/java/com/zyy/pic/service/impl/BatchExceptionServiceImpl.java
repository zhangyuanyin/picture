package com.zyy.pic.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyy.pic.common.data.EntryDataHelper;
import com.zyy.pic.common.datasource.DatabaseContextHolder;
import com.zyy.pic.common.datasource.DatabaseType;
import com.zyy.pic.dao.PictureBankDao;
import com.zyy.pic.domain.PictureBank;
import com.zyy.pic.service.BatchExceptionService;
import com.zyy.pic.service.PictureBankService;
import com.zyy.pic.util.Constants;
import com.zyy.pic.util.DateTimeUtil;

/**
 * <dt>批量异常处理服务</dt>
 * @author yyzhang
 * @since 2018年6月4日16:14:49
 */
@Service
public class BatchExceptionServiceImpl implements BatchExceptionService {
	private Logger logger = Logger.getLogger(BatchExceptionServiceImpl.class);

	@Autowired
	private PictureBankDao pictureBankDao;
	@Autowired
	private PictureBankService bankService;
	@Autowired
	private EntryDataHelper dataHelper;
	
	@Override
	public Map<String, Object> updateStatus4ReadEcmsFileException(String date) {
		logger.info("【批量异常处理】更新状态开始...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		Date choose = DateTimeUtil.parseDate(date);
		Date now = DateTimeUtil.parseDate(new Date());
		List<PictureBank> pictureBanks = pictureBankDao.getPictureBankByStatus("1", date);
		if(pictureBanks != null && pictureBanks.size() > 0) {
			pictureBankDao.updatePictureBank("1", "0", new Date());
			resultMap.put("result", "【批量异常处理】读取资管文件的放款数据的状态修改成功，status = 1 改为 0");
		}
		if(choose.compareTo(now) < 0){
			bankService.downloadFromFtp(choose);
			resultMap.put("result", "【批量异常处理】重新获取资管文件完成，文件时间：" + DateTimeUtil.formatDate(choose, "yyyyMMdd"));
		}
		if(resultMap.get("result") == null) {
			resultMap.put("result", "选择日期【"+date+"】无满足条件的数据！");
		}
		logger.info(resultMap.get("result"));
		
		logger.info("【批量异常处理】更新状态结束...");
		return resultMap;
	}

	@Override
	public Map<String, Object> downloadFile4ConnetCustomException(String date) {
		logger.info("【批量异常处理】下载文件开始...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		Date choose = DateTimeUtil.parseDate(date);
		List<PictureBank> pictureBanks = pictureBankDao.getPictureBankByStatus("1", date);
		if(pictureBanks != null && pictureBanks.size() > 0) {
			resultMap.put(Constants.EXCEPT_KEY, Constants.EXCEPT_VAL_N);
			resultMap.put("result", "【批量异常处理】放款数据还未处理，请先修改放款数据状态！");
		} else {
			pictureBanks = pictureBankDao.getPictureBankByStatus("0", date);
			if(pictureBanks != null && pictureBanks.size() > 0) {
				bankService.uploadToFtp(choose);
				resultMap.put(Constants.EXCEPT_KEY, Constants.EXCEPT_VAL_Y);
				resultMap.put("result", "【批量异常处理】处理完成，处理结果可至FTP查询！");
			} else {
				resultMap.put(Constants.EXCEPT_KEY, Constants.EXCEPT_VAL_N);
				resultMap.put("result", "【批量异常处理】选择日期的数据还未同步至表中，请先同步数据，待同步文件日志：" + DateTimeUtil.formatDate(choose, "yyyyMMdd"));
			}
		}
		if(resultMap.get("result") == null) {
			resultMap.put("result", "选择日期【"+date+"】无满足条件的数据！");
		}
		logger.info(resultMap.get("result"));
		
		logger.info("【批量异常处理】下载文件结束...");
		return resultMap;
	}

	@Override
	public Map<String, Object> uploadFile4ConnectFtpException(String date) {
		logger.info("【批量异常处理】文件上传开始...");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		List<PictureBank> pictureBanks = pictureBankDao.getPictureBankByStatus("1", date);
		if(pictureBanks != null && pictureBanks.size() > 0) {
			String busDate = pictureBanks.get(0).getBusinessDate();
			Date pathDate = pictureBanks.get(0).getCreateTime();
			if(!dataHelper.checkIsExists(Constants.BANK_NAME_TJ, pathDate, busDate)) {
				logger.info("【批量异常处理】文件不存在，重新上传开始...");
				if(dataHelper.upload2Ftp(Constants.BANK_NAME_TJ, pathDate, busDate)) {
					resultMap.put("result", "【批量异常处理】文件上传成功 !");
				} else {
					resultMap.put("result",  "【批量异常处理】文件上传成功 !");
				}
			}
		}
		if(resultMap.get("result") == null) {
			resultMap.put("result", "选择日期【"+date+"】无满足条件的数据！");
		}
		logger.info(resultMap.get("result"));
		
		logger.info("【批量异常处理】文件上传结束...");
		return resultMap;
	}
}
