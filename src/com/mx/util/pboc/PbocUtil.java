package com.mx.util.pboc;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mx.se.CardDef;
import com.mx.se.CardInfoResp;
import com.mx.se.ParamesDef;
import com.mx.util.MXBaseUtil;
import com.mx.util.exception.DeviceConnectException;
import com.mx.util.exception.DeviceTransException;
import com.mx.util.iso7816.Iso7816_Tag;

public class PbocUtil {	
	public static final Map<String, String> APP_PBOC_MAP;
	public static final Map<String, String> TAG_TITLE;
	static {
		APP_PBOC_MAP = new HashMap<String, String>();
		TAG_TITLE = new HashMap<String, String>();
		APP_PBOC_MAP.put("A000000333010101", "借记卡");
		APP_PBOC_MAP.put("A000000333010102", "贷记卡");
		APP_PBOC_MAP.put("A000000333010103", "准贷记卡");
		APP_PBOC_MAP.put("A000000333010106", "纯电子现金卡");
		TAG_TITLE.put("9A", ParamesDef.OFFLINE_LOG_TITLE_TRADEDATE);
		TAG_TITLE.put("9F21", ParamesDef.OFFLINE_LOG_TITLE_TRADETIME);
		TAG_TITLE.put("9F03", ParamesDef.OFFLINE_LOG_TITLE_OHTERAMOUNT);
		TAG_TITLE.put("9F02", ParamesDef.OFFLINE_LOG_TITLE_TRADEAMOUT);
		TAG_TITLE.put("9F1A", ParamesDef.OFFLINE_LOG_TITLE_TRADETERIMALCODE);
		TAG_TITLE.put("5F2A", ParamesDef.OFFLINE_LOG_TITLE_TRADECURRENCYCODE);
		TAG_TITLE.put("9F4E", ParamesDef.OFFLINE_LOG_TITLE_MERNAME);
		TAG_TITLE.put("9C", ParamesDef.OFFLINE_LOG_TITLE_TRADETYPE);
		TAG_TITLE.put("9F36", ParamesDef.OFFLINE_LOG_TITLE_TRADENO);
	}
	
	public static String parseLog(byte[] by9F4F, ArrayList<String> logList,int cardApplet) {
		StringBuilder szLogBuilder = new StringBuilder();
		ArrayList<LogTitle> logTitleList = new ArrayList<LogTitle>();
		while (by9F4F.length > 0) {
			int nOffset = 0;
			byte[] bTag = MXBaseUtil.subBytes(by9F4F, nOffset, nOffset + 1);
			nOffset++;
			if ((MXBaseUtil.bytes2Int(bTag) & 0x1F) == 0x1F) {
				nOffset++;
			}
			bTag = MXBaseUtil.subBytes(by9F4F, 0, nOffset);
			int nLen = MXBaseUtil.bytes2Int(MXBaseUtil.subBytes(by9F4F,
					nOffset, nOffset + 1));
			nOffset++;
			if (nLen > 128) {
				int nLenLen = nLen - 128;
				byte[] lendata = MXBaseUtil.subBytes(by9F4F, nOffset, nOffset
						+ nLenLen);
				nLen = MXBaseUtil.bytes2Int(lendata);
				nOffset += nLenLen;
			}
			String sTag = MXBaseUtil.byte2hex(bTag);
			logTitleList.add(new LogTitle(sTag, nLen));
			by9F4F = MXBaseUtil.subBytes(by9F4F, nOffset, by9F4F.length);
		}
		// 两层循环，第一层用于分割整个日志的Array，第二层用于单条日志的输出
		String szData;
		String szTitle;
		for (int i = 0; i < logList.size(); i++) {
			StringBuilder oneLogStringBuilder = new StringBuilder();
			oneLogStringBuilder.append(logList.get(i));
			for (int j = 0; j < logTitleList.size(); j++) {
				LogTitle titleIndex = logTitleList.get(j);
				// 进行字符串分割,获得值
				if (oneLogStringBuilder.length() < titleIndex.m_nLen * 2) {
					return CardDef.ERR_SZGETLOG;
				}
				szData = oneLogStringBuilder
						.substring(0, titleIndex.m_nLen * 2);
				oneLogStringBuilder.delete(0, titleIndex.m_nLen * 2);
				// 根据TAG寻找说明
				if (PbocUtil.TAG_TITLE.containsKey(titleIndex.m_szTitle)) {
					szTitle = PbocUtil.TAG_TITLE.get(titleIndex.m_szTitle);
				} else {
					szTitle = titleIndex.m_szTitle;
				}
				szLogBuilder.append(szTitle);
				szLogBuilder.append(':');
				if (szTitle.equals(ParamesDef.OFFLINE_LOG_TITLE_TRADEAMOUT)
						|| szTitle
								.equals(ParamesDef.OFFLINE_LOG_TITLE_OHTERAMOUNT)) {
					szData = MXBaseUtil.stringMoneyTrans(szData,10);
				} else if (szTitle
						.equals(ParamesDef.OFFLINE_LOG_TITLE_TRADEDATE)) {
					szData = "20" + szData;
				} else if (szTitle.equals(ParamesDef.OFFLINE_LOG_TITLE_MERNAME)) {
					byte[] byData = MXBaseUtil.hex2byte(szData);
					try {
						if (CardDef.CARD_TYPE_HFTZYT == cardApplet) {
							//中银通商户不需要
							szData = "00000000000000000000";
						} else {
							szData = new String(byData, "GBK");
						}
						
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						return CardDef.ERR_SZNOTAG;
					}
				} else if (szTitle.equals(ParamesDef.OFFLINE_LOG_TITLE_TRADENO)) {
					szData = String.valueOf(Integer.valueOf(szData, 16));
				} else if (szTitle
						.equals(ParamesDef.OFFLINE_LOG_TITLE_TRADETYPE)) {
					if (cardApplet == CardDef.CARD_TYPE_HFTZYT) {
						if (CardDef.HFT_PBOC_TRADE_MAP.containsKey(szData)) {
							szData = CardDef.HFT_PBOC_TRADE_MAP.get(szData);
						}
					} else {
						if (CardDef.PBOC_TRADE_MAP.containsKey(szData)) {
							szData = CardDef.HFT_PBOC_TRADE_MAP.get(szData);
						}
					}
				}
				szLogBuilder.append(szData);

				if (j < logTitleList.size() - 1) {
					szLogBuilder.append('|');
				}
			}
			if (i < logList.size() - 1) {
				szLogBuilder.append('&');
			}
		}
		return szLogBuilder.toString();
	}
	
	public static int select(Iso7816_Tag tag, String szAID)
			throws DeviceTransException {
		String sCmd = "00A40400";
		sCmd += String.format("%1$02X", szAID.length() / 2);
		sCmd += szAID;
		return tag.sendAPDU(sCmd);
	}

	public static int readRecord(Iso7816_Tag tag, byte bSFI, byte bRecNo)
			throws DeviceTransException {
		byte[] bCmd = new byte[4];
		bCmd[0] = (byte) 0x00;
		bCmd[1] = (byte) 0xB2;
		bCmd[2] = bRecNo;
		bCmd[3] = (byte) ((byte) (bSFI << 3) | (byte) 0x04);//

		String sCmd = MXBaseUtil.byte2hex(bCmd);
		return tag.sendAPDU(sCmd);
	}

	

	public static int externAuth(Iso7816_Tag tag, String sExAuth)
			throws DeviceTransException {
		String sCmd = "00820000";
		try {
			sCmd += String.format("%1$02X", sExAuth.length() / 2);
			sCmd += sExAuth;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag.sendAPDU(sCmd);
		//return 0x9401;
	}
	
	public static int getProcessOption(Iso7816_Tag tag,String pDolData) throws DeviceTransException {
		
		int nPDOLDataLen = pDolData.length() / 2;
		if (nPDOLDataLen == 1) {
			return -1;
		}
		String sCmd = String.format("80A80000%1$02X83%2$02X", nPDOLDataLen + 2,
				nPDOLDataLen);
		sCmd += pDolData;
		return tag.sendAPDU(sCmd);
	}

	
	
	
	public static String packPdol(String pdol,Map<String, String> pbocDataMap) {
		//List<String> tagList = MXBaseUtil.parseBerTLString(pdol);
		return null;
	}
	
	public static boolean ReadAFL(Iso7816_Tag tag, String sAFL,Map<String, String> diMap,int waitTime)
			throws DeviceTransException {
		int nFileNum = sAFL.length() / 8;
		boolean isComplete = true;
		for (int i = 0; i < nFileNum; i++) {
			String safl = sAFL.substring(i * 8, i * 8 + 8);
			String str = safl.substring(0, 2);
			int nP2 = 0;
			nP2 = Integer.parseInt(str, 16);
			String str2 = safl.substring(2, 4);
			String str3 = safl.substring(4, 6);
			int nFrom = 0;
			int nEnd = 0;
			int rev = 0;
			nFrom = Integer.parseInt(str2, 16);
			nEnd = Integer.parseInt(str3, 16);
			for (int j = nFrom; j <= nEnd; j++) {

				try {
					rev = readSFI(tag, (byte) nP2, (byte) j);
					if (waitTime != 0) {
						Thread.sleep(waitTime);
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
					isComplete = false;
					break;
				}

				if (PbocEntity.SWOK == rev) {
					try {
						diMap.putAll(MXBaseUtil.parseBerTlv((tag.getResponse())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					isComplete = false;
				}

			}
		}

		return isComplete;
	}
	
	private static int readSFI(Iso7816_Tag tag, byte bSFI, byte bRecNo)
			throws InterruptedException, DeviceTransException {
		byte[] bCmd = new byte[4];
		bCmd[0] = (byte) 0x00;
		bCmd[1] = (byte) 0xB2;
		bCmd[2] = bRecNo;
		bCmd[3] = (byte) (bSFI | (byte) 0x04);

		String sCmd = MXBaseUtil.byte2hex(bCmd);
		return tag.sendAPDU(sCmd);
	}
	
	//分析除了余额、卡片类型和日志以外的数据
	public static CardInfoResp parsePbocMap(Map<String, String> pbocMap) {
		CardInfoResp cardInfoResp = new CardInfoResp();
		cardInfoResp.cardId = pbocMap.get("5A");
		if (cardInfoResp.cardId != null) {
			cardInfoResp.cardId = cardInfoResp.cardId.replace("F", "");
		}
		cardInfoResp.endDate = "20" + pbocMap.get("5F24");
		cardInfoResp.startDate = "20" + pbocMap.get("5F25");
		return cardInfoResp;
	}
	
	public static String getData(Iso7816_Tag tag, byte tag1, byte tag2, String szAid)
			throws DeviceConnectException, DeviceTransException {


		int rv = PbocUtil.select(tag, szAid);
		if (rv != Iso7816_Tag.SWOK) {
			return CardDef.ERR_SZSELECTAID;
		}

		rv = tag.getData((byte) tag1, (byte) tag2);

		if (rv != Iso7816_Tag.SWOK) {
			return CardDef.ERR_SZGETDATAPARAMES;
		}
		return tag.getRes();

	}
	
	
}


