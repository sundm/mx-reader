package com.mx.se;

public interface ParamesDef {
	/*****************
	 * define for offline data parames
	 */
	
	//脱机金额
	public static final int OFFLINE_PARAMES_BALANCE = 1;
	//卡号
	public static final int OFFLINE_PARAMES_CARDID = 2;
	//启用日期与失效日期
	public static final int OFFLINE_PARAMES_STARTENDDATE = 3;
	//证件类型
	public static final int OFFLINE_PARAMES_CERTTYPE = 4;
	//证件号码
	public static final int OFFLINE_PARAMES_CERTID = 5;
	//持卡人姓名
	public static final int OFFLINE_PARAMES_CHNAME = 6;
	//卡片类型
	public static final int OFFLINE_PARAMES_CARDTYPE = 7;
	//透支限额
	public static final int OFFLINE_PARAMES_OVERLIMIT = 8;
	//电子现金余额上限
	public static final int OFFLINE_PARAMES_BALANCELIMIT = 9;
	//交易日志
	public static final int OFFLINE_PARAMES_OFFLINELOG = 10;
	//卡片序列号
	public static final int OFFLINE_PARAMES_APPSERIALNO = 11;
	//edep卡的15文件
	public static final int OFFLINE_PARAMES_EDEPFILE15 = 12;
	//edep卡的16文件
	public static final int OFFLINE_PARAMES_EDEPFILE16 = 13;
	//押金标志
	public static final int OFFLINE_PARAMES_DEPOSIT = 14;
	//public 
	
	//交易类型
	//联机消费
	//public static final int ONLINE_PARAMES_TRADETYPE_PURCHASE = 101;
	//圈存
	public static final int ONLINE_PARAMES_TRADETYPE_LOAD = 102;
	//查询主账户余额
	//public static final int ONLINE_PARAMES_TRADETYPE_QUERYHOSTBALANCE = 103;
	//hash导出时用的响应码
	public static final String RESULTCODE = "ResultCode"; 
	//edep导出时使用的响应KEY
	public static final String RESULTINITRESPONSE = "ResultInitResponse";
	
	//edep creditforladn相应key
	public static final String RESULTCREDITRESPONSE = "ResultCreditResponse";
	public static final String RESULTSW = "ResultSW";
	public static final String TC = "tc";
	
	public static final String EDEP_TAC = "EDEPTAC";
	
	//这里主要用于EDEP，对于DC，考虑是否用TAG的方式导出
	public static final String OFFLINE_LOG_TITLE_TRADEAMOUT = "交易金额";
	public static final String OFFLINE_LOG_TITLE_TRADENO = "交易序号";
	public static final String OFFLINE_LOG_TITLE_TRADETYPE = "交易类型";
	public static final String OFFLINE_LOG_TITLE_TRADEOVERLIMIT = "透支限额";
	public static final String OFFLINE_LOG_TITLE_TRADETERMINALID = "终端机编号";
	public static final String OFFLINE_LOG_TITLE_TRADEDATE = "交易日期";
	public static final String OFFLINE_LOG_TITLE_TRADETIME = "交易时间";
	public static final String OFFLINE_LOG_TITLE_OHTERAMOUNT = "其他金额";
	public static final String OFFLINE_LOG_TITLE_TRADECURRENCYCODE = "交易货币代码";
	public static final String OFFLINE_LOG_TITLE_TRADETERIMALCODE = "终端国家代码";
	public static final String OFFLINE_LOG_TITLE_MERNAME = "商户名称";
	
	//各种类的集合
	public static final int APPLET_TYPE_PBOCSTANDARD = 1;
	public static final int APPLET_TYPE_MOTDC = 2;
	public static final int APPLET_TYPE_MOTEP = 3;
	public static final int APPLET_TYPE_HFTONG = 4;
	//public static final int APPLET_TYPE_HFTONGDEMO = 5;
	public static final int APPLET_TYPE_HFTPBOC = 5;
	
	//主机类的集合
	public static final int HOST_TYPE_UNIONPAY_UAT = 1;
	public static final int HOST_TYPE_UNIONPAY_PM = 2;
	public static final int HOST_TYPE_UNIONPAY_PRODUCT = 3;
	public static final int HOST_TYPE_JWSMART = 4;
	
	//交易类型
	public final String PBOC_LOAD = "1";
	public final String PBOC_ONLINEPURCHASE = "2";
	public final String PBOC_ONLINEQUERYBALANCE = "3";
	public final String EDEP_LOAD = "11";
	
	//连接的交易放信息
	public final String SDK_VERSION = "SDK_VERSION";
	public final String MINIPAY_TERID = "MINIPAY_TERID";
	public final String MINIPAY_USERID = "MINIPAY_USERID";
	
	public final int CHECK_READCARD = 1;
	public final int CHECK_LOAD = 2;
	
	//检查项目不符合要求
	public final int CHECK_NOT_SUPPORT = BaseCodeDef.UTIL_CODE + 1;
	
	public final String FUNCTION_NOT_SUPPORT = "Function_Not_Support";
	
	public final String NFC_READ_ERR = "卡片内容获取失败，非匹配卡";
	public final String NFC_CONNECT_ERR = "NFC通讯错误";
	public final String NFC_EDEP_INIT_ERR = "圈存初始化失败";
	public final String NFC_CHECK_SUCCESS = "功能正常";
	public final String NFC_EXCETPION_ERROR = "异常错误";
	public final String NFC_WORNG_SW = "SW错误，返回:";
}
