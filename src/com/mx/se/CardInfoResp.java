package com.mx.se;

public class CardInfoResp {	
	//返回码
	public int code;
	//错误原因（如果是错误的话）
	public String errorInfo;
	//卡片类型（大类型）
	public int cardType;
	//卡片子类型(合肥通普通卡、优惠卡等)
	public String subType;
	//卡片生效日期
	public String startDate;
	//卡片失效日期
	public String endDate;
	//当前余额(字符串十进制表示)
	public String balance;
	//日志
	public String log;
	//卡号
	public String cardId;
	
}
