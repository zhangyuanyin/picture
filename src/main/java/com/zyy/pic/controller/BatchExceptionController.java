package com.zyy.pic.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.zyy.pic.form.BatchExceptionForm;
import com.zyy.pic.service.BatchExceptionService;


/**
 * <dt>处理批量异常情况</dt>
 * @author yyzhang
 * @since 2018年6月2日10:30:59
 */
@Controller
@RequestMapping("/pic-api/batch")
public class BatchExceptionController {
	@Autowired
	private BatchExceptionService exceptionService;
	
	/**
	 * 处理发送银行FTP文件异常情况
	 */
	@RequestMapping("/bank/except")
	public String sendBankException() {
		return "bank_exception";
	}
	
	@RequestMapping("/bank/except/upstatus")
	public String updateBankStatus(@Valid BatchExceptionForm form, ModelMap map) {
		map.put("result", JSON.toJSONString(exceptionService.updateStatus4ReadEcmsFileException(form.getDate())));
		return "bank_exception_result";
	}
	
	@RequestMapping("/bank/except/download")
	public String downloadBankPicture(@Valid BatchExceptionForm form, ModelMap map) {
		map.put("result", JSON.toJSONString(exceptionService.downloadFile4ConnetCustomException(form.getDate())));
		return "bank_exception_result";
	}
	
	@RequestMapping("/bank/except/upload")
	public String uploadBankPicture(@Valid BatchExceptionForm form, ModelMap map) {
		map.put("result", JSON.toJSONString(exceptionService.uploadFile4ConnectFtpException(form.getDate())));
		return "bank_exception_result";
	}
}
