package com.mx.util.pboc;

import java.util.Map;

import com.mx.se.CardDef;

public class PbocOffLineResp {
	private int code;
	private String errorInfo;
	private Map<String, String> pbocMap;

	public PbocOffLineResp() {
		code = CardDef.CARD_FAIL;
		errorInfo = "未识别卡片";
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Map<String, String> getPbocMap() {
		return pbocMap;
	}

	public void setPbocMap(Map<String, String> pbocMap) {
		this.pbocMap = pbocMap;
	}

	@Override
	public String toString() {
		return "PbocOffLineResp [code=" + code + ", errorInfo=" + errorInfo
				+ ", pbocMap=" + pbocMap + "]";
	}

}
