package com.zyy.pic.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zyy.pic.common.data.ConcurrencyGenerateBankPicture;
import com.zyy.pic.common.data.EntryDataHelper;
import com.zyy.pic.domain.PictureBank;
import com.zyy.pic.service.PictureBankService;
import com.zyy.pic.util.Constants;
import com.zyy.pic.util.DateTimeUtil;
import com.zyy.pic.util.FtpUtil;
import com.zyy.pic.util.PictureCommonUtil;

/**
 * <dt>读取ftp文件，上传打包后的文件至ftp指定文件夹下</dt>
 * @author yyzhang 
 * @since 2018年5月24日16:36:45
 */
@Service
public class PictureBankServiceImpl implements PictureBankService,BeanFactoryAware {
	private static final Logger logger = Logger.getLogger(PictureBankServiceImpl.class);
	
	@Value("${pic.dnfilepath}")
	private String filepath;
	
	private BeanFactory beanFactory;
	@Autowired
	private EntryDataHelper dataHelper;
	@Autowired
	private ConcurrencyGenerateBankPicture concurrencyPicture;
	
	@Override
	public void downloadFromFtp(Date date) {
		List<String> ftpFiles;
		try {
			StringBuffer filename = new StringBuffer();
			filename.append(filepath).append(DateTimeUtil.getYear(date)).append("/")
					.append(DateTimeUtil.formatDate(date, false)).append("/res/Params_")
					.append(DateTimeUtil.formatDate(date, false)).append("_tianjin.txt");
			ftpFiles = FtpUtil.readFileByLine(filename.toString());
			if(ftpFiles == null || ftpFiles.size() == 0) {
				logger.error("文件不存在，或文件内容为空，请检查！！！");
				throw new IOException("文件不存在，或文件内容为空，请检查！！！");
			}
			// 保存历史数据
			dataHelper.savePictureBankHis();
			// 根据读取文件内容，初始化当日要处理数据
			dataHelper.savePictureBank(ftpFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void uploadToFtp(Date date) {
		List<PictureBank> pictureBanks = dataHelper.getPictureBankByStatus("0");
		String busDate = pictureBanks.get(0).getBusinessDate();
		
		List<List<?>> composes = PictureCommonUtil.composeCollection(pictureBanks);
		CountDownLatch latch = new CountDownLatch(composes.size());
		for (List<?> composeList : composes) {
			this.getBean();
			concurrencyPicture.setLatch(latch);
			concurrencyPicture.setBatchDate(date);
			concurrencyPicture.setBankList((List<PictureBank>) composeList);
			Thread bankThread = new Thread(concurrencyPicture);
			bankThread.start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dataHelper.updatePictureBankStatus();
		if(dataHelper.compress(date, busDate)) {
			if(dataHelper.upload2Ftp(Constants.BANK_NAME_TJ, date, busDate)) {
				logger.info("【FTP上传】文件上传成功 !");
			} else {
				logger.info("【FTP上传】文件上传失败 !");
			}
		}
	}

	@Override
	public void checkExists() {
		List<PictureBank> pictureBanks = dataHelper.getPictureBankByStatus("1");
		if(pictureBanks != null && pictureBanks.size() > 0) {
			String busDate = pictureBanks.get(0).getBusinessDate();
			Date pathDate = pictureBanks.get(0).getCreateTime();
			if(!dataHelper.checkIsExists(Constants.BANK_NAME_TJ, pathDate, busDate)) {
				logger.info("【FTP检查】文件不存在，重新上传开始...");
				if(dataHelper.upload2Ftp(Constants.BANK_NAME_TJ, pathDate, busDate)) {
					logger.info("【FTP上传】文件上传成功 !");
				} else {
					logger.info("【FTP上传】文件上传失败 !");
				}
			}
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	private void getBean() {
		this.concurrencyPicture = (ConcurrencyGenerateBankPicture) beanFactory.getBean("concurrencyGenerateBankPicture");
	}
}
