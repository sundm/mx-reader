package com.mx.util.edep;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.mx.se.CardDef;
import com.mx.se.ParamesDef;
import com.mx.util.MXBaseUtil;
import com.mx.util.exception.DeviceTransException;
import com.mx.util.iso7816.Iso7816_Tag;

public class EdepUtil {
	protected final static int SWOK = 0x9000;
	private static EDEPLog logItemLog;

	public static String parseTradeDetail(String szDetail) {
		if (szDetail == null || szDetail.length() != 46) {
			return "";
		}
		StringBuilder szbBuilder = new StringBuilder();
		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADENO);
		szbBuilder.append(':');
		szbBuilder.append(String.valueOf(Integer.valueOf(szDetail.substring(0, 4), 16)));
		szbBuilder.append('|');
		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADEOVERLIMIT);
		szbBuilder.append(':');
		szbBuilder.append(MXBaseUtil.stringMoneyTrans(szDetail.substring(4, 10), 16));
		szbBuilder.append('|');
		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADEAMOUT);
		szbBuilder.append(':');
		szbBuilder.append(MXBaseUtil.stringMoneyTrans(szDetail.substring(10, 18), 16));
		szbBuilder.append('|');
		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADETYPE);
		szbBuilder.append(':');
		// 这里需要做区分了
		if (szDetail.substring(18, 20).equals("01")) {
			szbBuilder.append("电子存折圈存");
		} else if (szDetail.substring(18, 20).equals("02")) {
			szbBuilder.append("电子钱包圈存");
		} else if (szDetail.substring(18, 20).equals("03")) {
			szbBuilder.append("圈提");
		} else if (szDetail.substring(18, 20).equals("04")) {
			szbBuilder.append("电子存折取款");
		} else if (szDetail.substring(18, 20).equals("05")) {
			szbBuilder.append("电子存折消费");
		} else if (szDetail.substring(18, 20).equals("06")) {
			szbBuilder.append("电子钱包消费");
		} else if (szDetail.substring(18, 20).equals("07")) {
			szbBuilder.append("修改透支限额");
		} else if (szDetail.substring(18, 20).equals("08")) {
			szbBuilder.append("信用消费");
		} else if (szDetail.substring(18, 20).equals("09")) {
			szbBuilder.append("复合消费");
		} else {
			szbBuilder.append("未知");
		}
		szbBuilder.append('|');
		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADETERMINALID);
		szbBuilder.append(':');
		szbBuilder.append(szDetail.substring(20, 32));
		szbBuilder.append('|');

		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADEDATE);
		szbBuilder.append(':');
		szbBuilder.append(szDetail.substring(32, 40));
		szbBuilder.append('|');

		szbBuilder.append(ParamesDef.OFFLINE_LOG_TITLE_TRADETIME);
		szbBuilder.append(':');
		szbBuilder.append(szDetail.substring(40, 46));
		szbBuilder.append('|');

		return szbBuilder.toString();
	}

	public static String get18File(Iso7816_Tag tag, boolean needPin, byte[] pin) throws DeviceTransException {
		StringBuilder szBuilder = new StringBuilder();

		// 只有ED需要verify再读记录
		if (needPin == true && pin != null) {
			int rv = tag.verify(pin);
			if (rv != SWOK) {
				return CardDef.ERR_SZVERIFYPIN;
			}
		}
		for (int i = 1; i < 11; i++) {
			int nRet = tag.readRecord(0x18, i);
			if (nRet != SWOK) {
				return szBuilder.toString();
			}
			szBuilder.append(parseTradeDetail(tag.getRes()));
			szBuilder.append('&');
		}
		return szBuilder.toString();
	}

	public static EDEPLog paseLog(String logDetail) {
		logItemLog = new EDEPLog();

		if (logDetail == null || logDetail.length() != 46) {
			logItemLog.setTradeNO(0);
			return logItemLog;
		}

		int tradeNo = Integer.valueOf(logDetail.substring(0, 4), 16);
		if (tradeNo == 0) {
			logItemLog.setTradeNO(0);
			return logItemLog;
		}

		logItemLog.setTradeNO(tradeNo);
		logItemLog.setTradeAmount(MXBaseUtil.stringMoneyTrans(logDetail.substring(10, 18), 16));

		String tradeType = logDetail.substring(18, 20);

		// 这里需要做区分了
		if (tradeType.equals("01")) {
			logItemLog.setTradeType("电子存折圈存");
		} else if (tradeType.equals("02")) {
			logItemLog.setTradeType("电子钱包圈存");
		} else if (tradeType.equals("03")) {
			logItemLog.setTradeType("圈提");
		} else if (tradeType.equals("04")) {
			logItemLog.setTradeType("电子存折取款");
		} else if (tradeType.equals("05")) {
			logItemLog.setTradeType("电子存折消费");
		} else if (tradeType.equals("06")) {
			logItemLog.setTradeType("电子钱包消费");
		} else if (tradeType.equals("07")) {
			logItemLog.setTradeType("修改透支限额");
		} else if (tradeType.equals("08")) {
			logItemLog.setTradeType("信用消费");
		} else if (tradeType.equals("09")) {
			logItemLog.setTradeType("复合消费");
		} else {
			logItemLog.setTradeType("未知");
		}

		String tradeDateString = logDetail.substring(32, 40);
		String tradeTimeString = logDetail.substring(40, 46);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date;
		try {
			date = format.parse(tradeDateString + tradeTimeString);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			logItemLog.setTradeDate(format1.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return logItemLog;
	}

}
