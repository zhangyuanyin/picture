package com.zyy.pic.form;

import org.hibernate.validator.constraints.NotEmpty;

public class BatchExceptionForm {

	@NotEmpty(message = "日期: 不能为空！")
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}