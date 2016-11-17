package com.mx.se;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class CardDef {
	/**********************************
	 * 以下定义的数据错误类型为int
	 */
	public static final int SWOK = 0x9000;
	public static final byte[] HFTONG_AID = { (byte) 0xA0, 0x00, 0x00, 0x00,
			0x03, (byte) 0x86, (byte) 0x98, 0x07, 0x01 };
	// 纯电子现金卡
	public static final byte[] PBOC_AID_ECC = { (byte) 0xA0, 0x00, 0x00, 0x03,
			0x33, 0x01, 0x01, 0x06 };
	// 普通pboc卡
	public static final byte[] PBOC_AID = { (byte) 0xA0, 0x00, 0x00, 0x03,
			0x33, 0x01, 0x01 };
	// 北京MF
	public static final byte[] BJ_MF = { 0x3F, 0x00 };
	// 北京AID
	public static final byte[] BJ_AID = { 0x4F, 0x43 };
	// 吉林AID
	public final static byte[] JILIN_AID = { (byte) 0xA0, 0x00, 0x00, 0x00,
			0x08, (byte) 0x86, (byte) 0x98, 0x07, 0x01 };
	// 深圳通
	public static final byte[] SHENZHEN_AID = { 0x50, 0x41, 0x59, 0x2E, 0x53,
			0x5A, 0x54};
	
	// 中石油
	public static final byte[] ZHONGSHIYOU = {(byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x50, 0x45, 0x54, 0x52, 0x4F, 0x43, 0x48, 0x49, 0x4E, 0x41};
	
	// 中石化
	public static final byte[] ZHONGSHIHUA = {(byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x53, 0x49, 0x4E, 0x4F, 0x50, 0x45, 0x43};

	public static Map<String, Integer> PBOC_CARD_TYPE = new HashMap<String, Integer>();

	public static Map<String, String> HFT_PBOC_TRADE_MAP = new HashMap<String, String>();

	public static Map<String, String> PBOC_TRADE_MAP = new HashMap<String, String>();

	// 合肥通
	public static final int CARD_TYPE_HFT = 1;
	// 合肥通中银通
	public static final int CARD_TYPE_HFTZYT = 2;
	// 借记卡
	public static final int CARD_TYPE_DEBIT = 3;
	// 贷记卡
	public static final int CARD_TYPE_CREDIT = 4;
	// 准贷记卡
	public static final int CARD_TYPE_QUASI_CREDIT = 5;
	// 电子现金卡
	public static final int CARD_TYPE_PBOCECC = 6;
	// 上海公交卡
	public static final int CARD_TYPE_SHANGHAI = 7;
	// 杭州公交卡
	public static final int CARD_TYPE_HANGZHOU = 8;
	// 哈尔滨城市通
	public static final int CARD_TYPE_HAERBIN = 9;
	// 大连城市通
	public static final int CARD_TYPE_DALIAN = 10;
	// 北京市政交通一卡通
	public static final int CARD_TYPE_BEIJING = 11;
	// 南昌
	public static final int CARD_TYPE_NANCHANG = 12;
	// 吉林
	public static final int CARD_TYPE_JILIN = 13;
	// 乌鲁木齐
	public static final int CARD_TYPE_WULUMUQO = 14;
	// 青岛
	public static final int CARD_TYPE_QINGDAO = 15;
	// 深圳
	public static final int CARD_TYPE_SHENZHEN = 16;
	// 中石油
	public static final int CARD_TYPE_ZHONGSHIYOU = 17;
	// 中石化
	public static final int CARD_TYPE_ZHONGSHIHUA = 18;
	
	public static final String STR_OK = "STR_OK";
	/*****************************
	 * 以下定义的类型为string，此时，需要通过list容器进行装载
	 */
	public static final String ERR_SZSELECTRESPONSE = "选择应用失败，请确认您的卡片";
	public static final String ERR_SZGETDATARESPONSE = "卡片处理失败";
	// public static final String ERR_SZNOCLASS = "ERR_NOCLASS";
	// public static final String ERR_SZNOMOTHED = "ERR_NOMOTHED";
	// public static final String ERR_SZINVOKEMOTHED = "ERR_INVOKEMOTHED";
	public static final String ERR_SZNFCENVIRONMENT = "NFC环境初始化错误";
	public static final String ERR_SZPARAMESTYPE = "错误的参数类型，请确认请求";
	public static final String ERR_SZSELECTAID = "选择应用失败，请确认您的卡片";
	public static final String ERR_SZREADBINARY15 = "读卡片信息，请确认卡片";
	public static final String ERR_SZ15FILEDATA = "卡片信息有误，请确认卡片";
	public static final String ERR_SZREADBINARY16 = "读持卡人信息有误，请确认卡片";
	public static final String ERR_SZ16FILEDATA = "持卡人信息有误，请确认卡片";
	public static final String ERR_SZGETBALANCE = "获取余额失败";
	public static final String ERR_SZNFCCONNECT = "NFC功能不能连接";
	public static final String ERR_SZNOTAG = "NFC功能异常";
	public static final String ERR_SZCARDTYPE = "错误的卡片类型，请确认您的卡片";
	public static final String ERR_SZBUILDDATA = "卡片返回数据异常，无法继续交易";
	public static final String ERR_SZGPORESPONSE = "银行卡GPO处理异常";
	public static final String ERR_SZREADAFL = "银行卡读AFL异常";
	public static final String ERR_SZNFCIO = "NFC通讯异常";
	// public static final String ERR_SZLACKDATA = "ERR_LACKDATA";
	public static final String ERR_SZGETOVERBALANCE = "无法获取余额上限";
	public static final String ERR_SZGETDATAPARAMES = "卡片参数请求错误";
	public static final String ERR_SZGETLOG = "无法获取日志";
	public static final String ERR_SZLOGFORM = "错误的日志格式";
	public static final String ERR_SZTRADEPROCESS = "交易过程主机响应异常";
	public static final String ERR_SZTRADEAPDU = "主机发送指令错误";
	public static final String ERR_SZAPDUPARAMESP1P2 = "指令参数错误";
	public static final String ERR_SZAPDUPARAMESCLAINS = "指令投错误";
	public static final String ERR_SZNFCINIT = "NFC功能初始化失败";
	public static final String ERR_SZPARAMESVALUE = "错误的参数值";
	public static final String ERR_SZCDOLGET = "CDOL获取失败，请确认银行卡";
	public static final String ERR_SZGENERATEAC1 = "银行卡GAC1处理失败";
	public static final String ERR_SZGENERATEAC2 = "银行卡GAC2处理失败";
	public static final String ERR_SZGENERATEAC1RESPONSE = "银行卡GAC1处理异常";
	public static final String ERR_SZGENERATEACREFUSECONNECTHOST = "卡片拒绝联机";
	public static final String ERR_SZHOSTRESPONSEERROR = "主机响应错误";
	public static final String ERR_SZPBOCEXAUTH = "发卡机构认证失败";
	public static final String ERR_SZSCRIPTPUT = "脚本执行出错"; // 脚本执行出错
	public static final String ERR_SZHOSTREFUSED = "主机拒绝交易"; // 主机拒绝交易
	public static final String ERR_SZVERIFYPIN = "PIN验证失败"; // PIN验证失败
	public static final String ERR_SZINITFORONLINE = "联机交易初始化失败"; // 联机交易初始化失败
	public static final String ERR_SZCARDREFUSETRADE = "卡片拒绝交易";// 卡片拒绝交易
	public static final String ERR_SZTALERROR = "TLV错误，卡片有误";// TLV解析错误
	public static final String ERR_UPREFUSETRADE = "银联拒绝交易";
	public static final String ERR_ISSREFUSETRADE = "发卡行拒绝交易";
	public static final String ERR_GETBALANCE_LIMIT = "获取卡片脱机金额或余额失败";
	public static final String ERR_TRADE_CARD_NOT_APPOINT = "交易卡号与指定卡号不符";
	public static final String ERR_NODOSCRIPT = "未执行脚本";
	public static final String ERR_DOSCRIPTERROR = "执行脚本出错";
	public static final String ERR_SZREADBINARY = "ERR_READBINARY";
	public static final String ERR_SZDEVICE = "设备异常";
	public static final String ERR_TRANSERROR = "指令发送失败";
	public static final String ERR_NFCOPEN_ON = "nfc上电失败";
	public static final String ERR_NFCTAG_FOUND = "未找到nfctag";
	public static final String ERR_GETTRANS_PROVE = "getTransProve失败";
	public static final String ERR_CARDBIN_ERROR = "卡bin未匹配，非合法卡";
	public static final String ERR_PBOC_AUTH = "银行卡发卡行认证失败";

	public static final String PROMOT_NEEDCARD = "请刷卡";
	public static final String PROMOT_NEEDPIN = "请输入PIN";
	public static final String PROMOT_SUCCESS = "交易完成";

	// 银联的交易方向
	public static final int TRADE_GOTOVPN_PLAIN = 0; // 明文传输
	public static final int TRADE_GOTOVPN_SECRET = 1; // 密文

	// public static final int TRADE_GOTOFAIL = 2; //本地处理，需要判断是否是错误
	// public static final int TRADE_GOTOPINJAR = 3; //调用JAR包
	// public static final int TRADE_GOTONFC = 4; //提示持卡人靠卡
	// public static final int TRADE_GOTOSUCCESS = 5; //交易成功
	// public static final int TRADE_START_SESSIONKEY = 6; //VPN开始Session交易
	// public static final int TRADE_WITH_SESSIONKEY = 7; //VPN开始Session交易

	// 京微的交易流程（没有start_sessionkey 和 with_sessionkey）
	// public static final int TRADE_JWTRANS = 101;

	public static Set<String> ERR_STRARRAY = new HashSet<String>();

	static {
		ERR_STRARRAY.add(ERR_SZSELECTRESPONSE);
		ERR_STRARRAY.add(ERR_SZGETDATARESPONSE);
		// ERR_STRARRAY.add(ERR_SZNOCLASS);
		// ERR_STRARRAY.add(ERR_SZNOMOTHED);
		// ERR_STRARRAY.add(ERR_SZINVOKEMOTHED);
		ERR_STRARRAY.add(ERR_SZNFCENVIRONMENT);
		ERR_STRARRAY.add(ERR_SZSELECTAID);
		ERR_STRARRAY.add(ERR_SZREADBINARY15);
		ERR_STRARRAY.add(ERR_SZREADBINARY16);
		ERR_STRARRAY.add(ERR_SZGETBALANCE);
		ERR_STRARRAY.add(ERR_SZNFCCONNECT);
		ERR_STRARRAY.add(ERR_SZNOTAG);
		ERR_STRARRAY.add(ERR_SZPARAMESTYPE);
		ERR_STRARRAY.add(ERR_SZCARDTYPE);
		ERR_STRARRAY.add(ERR_SZBUILDDATA);
		ERR_STRARRAY.add(ERR_SZGPORESPONSE);
		ERR_STRARRAY.add(ERR_SZREADAFL);
		ERR_STRARRAY.add(ERR_SZNFCIO);
		// ERR_STRARRAY.add(ERR_SZLACKDATA);
		ERR_STRARRAY.add(ERR_SZ15FILEDATA);
		ERR_STRARRAY.add(ERR_SZ16FILEDATA);
		ERR_STRARRAY.add(ERR_SZGETOVERBALANCE);
		ERR_STRARRAY.add(ERR_SZGETDATAPARAMES);
		ERR_STRARRAY.add(ERR_SZGETLOG);
		ERR_STRARRAY.add(ERR_SZLOGFORM);
		ERR_STRARRAY.add(ERR_SZTRADEPROCESS);
		ERR_STRARRAY.add(ERR_SZTRADEAPDU);
		ERR_STRARRAY.add(ERR_SZAPDUPARAMESP1P2);
		ERR_STRARRAY.add(ERR_SZAPDUPARAMESCLAINS);
		ERR_STRARRAY.add(ERR_SZNFCINIT);
		ERR_STRARRAY.add(ERR_SZPARAMESVALUE);
		ERR_STRARRAY.add(ERR_SZCDOLGET);
		ERR_STRARRAY.add(ERR_SZGENERATEAC1);
		ERR_STRARRAY.add(ERR_SZGENERATEAC2);
		ERR_STRARRAY.add(ERR_SZGENERATEAC1RESPONSE);
		ERR_STRARRAY.add(ERR_SZGENERATEACREFUSECONNECTHOST);
		ERR_STRARRAY.add(ERR_SZHOSTRESPONSEERROR);
		ERR_STRARRAY.add(ERR_SZPBOCEXAUTH);
		ERR_STRARRAY.add(ERR_SZSCRIPTPUT);
		ERR_STRARRAY.add(ERR_SZHOSTREFUSED);
		ERR_STRARRAY.add(ERR_SZVERIFYPIN);
		ERR_STRARRAY.add(ERR_SZINITFORONLINE);
		ERR_STRARRAY.add(ERR_SZCARDREFUSETRADE);
		ERR_STRARRAY.add(ERR_SZTALERROR);
		ERR_STRARRAY.add(ERR_UPREFUSETRADE);
		ERR_STRARRAY.add(ERR_GETBALANCE_LIMIT);
		ERR_STRARRAY.add(ERR_ISSREFUSETRADE);
		ERR_STRARRAY.add(ERR_TRADE_CARD_NOT_APPOINT);
		ERR_STRARRAY.add(ERR_NODOSCRIPT);
		ERR_STRARRAY.add(ERR_DOSCRIPTERROR);
		ERR_STRARRAY.add(ERR_SZREADBINARY);
		ERR_STRARRAY.add(ERR_SZDEVICE);
		ERR_STRARRAY.add(ERR_TRANSERROR);
		ERR_STRARRAY.add(ERR_NFCOPEN_ON);
		ERR_STRARRAY.add(ERR_NFCTAG_FOUND);
		ERR_STRARRAY.add(ERR_GETTRANS_PROVE);
		ERR_STRARRAY.add(ERR_CARDBIN_ERROR);
		ERR_STRARRAY.add(ERR_PBOC_AUTH);

		PBOC_CARD_TYPE.put("A000000333010101", CARD_TYPE_DEBIT);
		PBOC_CARD_TYPE.put("A000000333010102", CARD_TYPE_CREDIT);
		PBOC_CARD_TYPE.put("A000000333010103", CARD_TYPE_QUASI_CREDIT);
		PBOC_CARD_TYPE.put("A000000333010106", CARD_TYPE_PBOCECC);

		PBOC_TRADE_MAP.put("00", "联机账户消费");
		PBOC_TRADE_MAP.put("21", "存款");
		PBOC_TRADE_MAP.put("28", "银联闪付");
		PBOC_TRADE_MAP.put("23", "退货");
		PBOC_TRADE_MAP.put("31", "主账户查询");
		PBOC_TRADE_MAP.put("40", "转账");
		PBOC_TRADE_MAP.put("60", "圈存");

		// 合肥通的卡片，没有联机账户消费

		HFT_PBOC_TRADE_MAP.put("00", "消费");
		HFT_PBOC_TRADE_MAP.put("31", "余额查询");
		HFT_PBOC_TRADE_MAP.put("28", "银联闪付");
		HFT_PBOC_TRADE_MAP.put("60", "圈存");
		HFT_PBOC_TRADE_MAP.put("63", "充值");

	}

	public static final String CARDRESULTCODE = "ResultCode";
	public static final String CARDRESULTDATA = "ResultData";

	public static final String NFCCARDRESULTCODE = CARDRESULTCODE;
	public static final String NFCCARDRESULTDATA = CARDRESULTDATA;

	// 成功
	public static final int CARD_SUCCESS = BaseCodeDef.SUCCESS;
	// 设备没有触发的时候进行针对设备的交易,例如设备没有触发过nfc功能，直接调用nfc模块的发送指令
	public static final int CARD_NODEVICE = BaseCodeDef.DEVICE_CODE + 1;
	// 设备连接失败，例如nfc上电失败，或者打开蓝牙设备上电失败
	public static final int CARD_DEVICE_CONNECT_ERROR = BaseCodeDef.DEVICE_CODE + 2;
	// 设备传输失败，例如nfc发送指令失败，或者蓝牙设备发送指令失败
	public static final int CARD_DEVICE_TRANSMIT_ERROR = BaseCodeDef.DEVICE_CODE + 3;
	// 未得到预期的数据，例如读卡由于卡不对未能读到正确的信息
	public static final int CARD_FAIL = BaseCodeDef.DEVICE_CODE + 4;
	// 卡片不被支持，例如nfc情况下init和load中间插入异常指令导致中间断电导致圈存不成功
	public static final int CARD_UNSUPPORT = BaseCodeDef.DEVICE_CODE + 5;
	// 设备初始化失败
	public static final int CARD_DEVICE_INIT_ERROR = BaseCodeDef.DEVICE_CODE + 6;
	// 设备没有这个功能,例如没有nfc模块或者不支持对应的设备
	public static final int CARD_DEVICE_NOT_SUPPORT = BaseCodeDef.DEVICE_CODE + 7;
	// 系统的设备消息是非对应的消息，例如intent的消息是非nfc的，但是调用了nfc的消息
	public static final int CARD_NOTDEVICE_MSG = BaseCodeDef.DEVICE_CODE + 8;
	// 设备功能未开启
	public static final int CARD_DEVICE_CLOSE = BaseCodeDef.DEVICE_CODE + 9;
	// 正常的设备输出消息
	public static final int DEVICE_MSG_OUT = BaseCodeDef.DEVICE_CODE + 10;
	// verify错误，需要客户端做特殊的提示
	public static final int CARD_VERIFY_ERROR = BaseCodeDef.DEVICE_CODE + 11;

	public static final int NFCCARD_SUCCESS = CARD_SUCCESS;
	public static final int NFCCARD_NFCTAG = CARD_NODEVICE;
	public static final int NFC_OPEN_ERROR = CARD_DEVICE_CONNECT_ERROR;
	public static final int NFC_TRANSMIT_ERROR = CARD_DEVICE_TRANSMIT_ERROR;
	public static final int NFCCARD_FAIL = CARD_FAIL;
	public static final int NFC_UNSUPPORT = CARD_UNSUPPORT;
	public static final int ERR_NFCINIT = CARD_DEVICE_INIT_ERROR;
	public static final int ERR_NONFCFUNCTION = CARD_DEVICE_NOT_SUPPORT;
	public static final int ERR_NOTNFCACTION = CARD_NOTDEVICE_MSG;
	public static final int ERR_NFCFUNCTIONCLOSE = CARD_DEVICE_CLOSE;

}
