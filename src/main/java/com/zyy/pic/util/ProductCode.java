package com.zyy.pic.util;

public enum ProductCode {
	MYFEN_6601("MYFEN", "6601"),
	MYFEN_6602("MYFEN", "6602"),
	MYFEN_6603("MYFEN", "6603"),
	MYFEN_6604("MYFEN", "6604"),
	MYFEN_6605("MOBILEXJD", "6605"),
	MYFEN_6606("MYFEN", "6606"),
	MYFEN_6607("GREEXJD", "6607"),
	MYFEN_6608("GREEXJD", "6608"),
	MYFEN_6701("MYFEN", "6701"),
	MYFEN_6702("MYFEN", "6702"),
	MYFEN_6703("MYFEN", "6703"),
	MYFEN_6650("HEAS", "6650"),
	MYFEN_6651("HEAS", "6651"),
	MYFEN_6660("JXXJD", "6660"),
	MYFEN_6670("BIGXJD", "6670"),
	MYFEN_6671("BIGXJD", "6671"),
	MYFEN_6801("EDUXJD", "6801"),
	MYFEN_6802("EDUXJD", "6802"),
	MYFEN_6803("EDUXJD", "6803"),
	MYFEN_6804("EDUXJD", "6804"),
	MYFEN_6680("MONXJD", "6680"),
	MYFEN_6681("MONXJD", "6681");
	
	private String type;
	private String code;
	
	private ProductCode(String type, String code) {
		this.type = type;
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
