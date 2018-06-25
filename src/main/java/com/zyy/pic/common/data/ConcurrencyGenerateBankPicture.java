package com.zyy.pic.common.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zyy.pic.domain.PictureBank;
import com.zyy.pic.entity.vo.VoPictureBank;
import com.zyy.pic.util.Constants;

@Service
@Scope("prototype")
public class ConcurrencyGenerateBankPicture implements Runnable{
	@Autowired
	private EntryDataHelper dataHelper;

	@Override
	public void run() {
		Map<String, VoPictureBank> dataMap = new HashMap<String, VoPictureBank>();
		for (PictureBank pictureBank : bankList) {
			if(Constants.SYS_NO_MF.equals(pictureBank.getSysNo())) {
				dataMap.put(pictureBank.getDuebillNo(), dataHelper.getPictureFromCustomerCenter(pictureBank));
			} else if(Constants.SYS_NO_MJ.equals(pictureBank.getSysNo())) {
				dataMap.put(pictureBank.getDuebillNo(), dataHelper.getPictureFromPicApp(pictureBank));
			}
		}
		dataHelper.saveRemoteData2File(dataMap, batchDate);
		latch.countDown();
	}
	
	public void setBankList(List<PictureBank> bankList) {
		this.bankList = bankList;
	}
	
	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	private CountDownLatch latch;
	private List<PictureBank> bankList;
	private Date batchDate;
}
