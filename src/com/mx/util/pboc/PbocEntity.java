package com.mx.util.pboc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mx.se.CardDef;
import com.mx.se.CardManagerUtil;
import com.mx.se.ParamesDef;
import com.mx.util.MXBaseUtil;
import com.mx.util.MXLog;
import com.mx.util.exception.DeviceConnectException;
import com.mx.util.exception.DeviceTransException;
import com.mx.util.iso7816.Iso7816_Tag;

public class PbocEntity {
	protected final byte[] CUP_RID = { (byte) 0xA0, (byte) 0x00, (byte) 0x00,
			(byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01 };

	protected final static String SAID = "A0000003330101";

	protected String m_szAID;

	public PbocEntity() {
		diMap.clear();
	}

	protected final static int SWOK = 0x9000;

	protected String m_sz9F02 = null;

	protected String m_szPDOL = null;
	protected String m_szCDOL1 = null;
	protected String m_szCDOL2 = null;

	protected String m_sz9F36 = null;
	protected String m_sz9F26 = null;

	protected String m_szAIP = null;
	protected static final String m_sz9F66 = "40000000";
	protected static final String m_sz9F7A = "01";

	protected static final String m_szDF69 = "00";
	protected static final String m_szDF60 = "00";

	protected String m_sz9F4E = "E58D97E4BAACE993B6E8A18C0000000000000000";

	protected static final String m_sz9F03 = "000000000000";
	protected static final String m_sz9F1A = "0156";

	protected String m_sz95 = "0080040800"; // TVR
	protected static final String m_sz5F2A = "0156";
	protected String m_sz9C = "60";
	protected String m_sz9F33 = "604000";

	protected String m_sz9A = null;

	protected String m_sz9F21 = null;
	protected String m_sz9F37 = null;

	protected String m_sz9F27 = null; // 密文信息数据
	protected String m_sz9F10 = null;
	// protected static String m_sz8A = null;
	// protected static String m_sz5F34 = null;
	protected String m_sz84 = null;

	protected Map<String, String> diMap = new HashMap<String, String>();

	public PbocOffLineResp getOffLineMap(Iso7816_Tag tag)
			throws DeviceTransException, DeviceConnectException {
		PbocOffLineResp pbocOffLineResp = new PbocOffLineResp();
		String szParames = "9C0160"
				+ "9F4E140000000000000000000000000000000000000000"
				+ "9F0206000000000000" + "9A03000000" + "9F2103000000";
		// String szResp =
		// startTrade(tag,"0000000000000000000000000000000000000000","000000000000",
		// sDataString,LOAD,szAid);
		String szResp = startTrade(tag, szParames, 0);
		if (CardDef.ERR_STRARRAY.contains(szResp)) {
			pbocOffLineResp.setCode(CardDef.CARD_FAIL);
			pbocOffLineResp.setErrorInfo(szResp);
			return pbocOffLineResp;
		}
		pbocOffLineResp.setCode(CardDef.CARD_SUCCESS);
		Map<String, String> pbocMap = new HashMap<String, String>();
		pbocMap.putAll(diMap);
		pbocOffLineResp.setPbocMap(pbocMap);
		return pbocOffLineResp;
	}

	public void setAid(String aid) {
		this.m_szAID = aid;
	}

	protected String parseGacResponse(Iso7816_Tag tag) {
		// String sTagString = tag.GetRes().substring(0, 2);

		String sLenString = tag.getRes().substring(2, 4);
		int nLen = Integer.parseInt(sLenString, 16);
		m_sz9F27 = tag.getRes().substring(4, 6);
		m_sz9F36 = tag.getRes().substring(6, 10);
		m_sz9F26 = tag.getRes().substring(10, 26);
		m_sz9F10 = tag.getRes().substring(26);
		if (m_sz9F27 == null || m_sz9F36 == null || m_sz9F26 == null
				|| m_sz9F10 == null) {
			tag.close();
			return CardDef.ERR_SZGENERATEAC1RESPONSE;
		}
		if (m_sz9F27.length() / 2 != 1 || m_sz9F36.length() / 2 != 2
				|| m_sz9F26.length() / 2 != 8) {
			tag.close();
			return CardDef.ERR_SZGENERATEAC1RESPONSE;
		}
		if (m_sz9F27.length() / 2 + m_sz9F36.length() / 2 + m_sz9F26.length()
				/ 2 + m_sz9F10.length() / 2 != nLen) {
			tag.close();
			return CardDef.ERR_SZGENERATEAC1RESPONSE;
		}

		// String sLenOf9F10 = String.format("%1$02X", m_sz9F10.length() / 2);
		diMap.put("9F26", m_sz9F26);
		diMap.put("9F27", m_sz9F27);
		diMap.put("9F10", m_sz9F10);
		diMap.put("9F37", m_sz9F37);
		diMap.put("9F36", m_sz9F36);
		diMap.put("95", m_sz95);
		diMap.put("9A", m_sz9A);
		diMap.put("9C", m_sz9C);
		diMap.put("9F02", m_sz9F02);
		diMap.put("5F2A", m_sz5F2A);
		diMap.put("82", m_szAIP);
		diMap.put("9F1A", m_sz9F1A);
		diMap.put("9F03", m_sz9F03);
		diMap.put("9F33", m_sz9F33);
		diMap.put("84", m_sz84);
		// return sBuild55;
		return CardDef.STR_OK;

	}

	protected String getTAL(byte[] TagAndLen) throws Exception {
		List<String> tagList = MXBaseUtil.parseBerTLString(TagAndLen);
		StringBuilder stringBuilder = new StringBuilder();
		for (String tagString : tagList) {
			if (!diMap.containsKey(tagString)) {
				throw new Exception("find no tag:" + tagString);
			}
			stringBuilder.append(diMap.get(tagString));
		}
		return stringBuilder.toString();

	}

	protected boolean getPDOL() {
		m_szPDOL = diMap.get("9F38");
		m_sz84 = diMap.get("84");
		if (m_szPDOL == null) {
			return false;
		} else {
			return true;
		}

	}

	protected String getPDOLData() throws Exception {
		diMap.put("9F66", m_sz9F66);
		diMap.put("9F02", m_sz9F02);
		diMap.put("9F03", m_sz9F03);
		diMap.put("9F1A", m_sz9F1A);
		diMap.put("95", m_sz95);
		diMap.put("5F2A", m_sz5F2A);
		diMap.put("9A", m_sz9A);
		diMap.put("9C", m_sz9C);
		diMap.put("9F7A", m_sz9F7A);
		diMap.put("9F37", m_sz9F37);
		diMap.put("DF69", m_szDF69);
		diMap.put("DF60", m_szDF60);
		diMap.put("9F21", m_sz9F21);
		diMap.put("9F4E", m_sz9F4E);

		return getTAL(MXBaseUtil.hex2byte(m_szPDOL));
	}

	protected boolean getCDOL(boolean bFirst) {
		if (bFirst)
			m_szCDOL1 = diMap.get("8C");
		else
			m_szCDOL2 = diMap.get("8D");
		return true;
	}

	protected String getCDOLData(boolean bFirst) throws Exception {
		if (bFirst) {
			return getTAL(MXBaseUtil.hex2byte(m_szCDOL1));
		} else {
			return getTAL(MXBaseUtil.hex2byte(m_szCDOL2));
		}

	}

	protected String parseBalance(String szBalance) {
		return String.valueOf(Integer.valueOf(szBalance));
	}

	protected String get9F37() {
		if (diMap.get("9F37") != null) {
			return diMap.get("9F37");
		}
		String[] beforeShuffle = new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		List<String> list = Arrays.asList(beforeShuffle);
		Collections.shuffle(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String afterShuffle = sb.toString();
		String result = afterShuffle.substring(1, 9);
		return result;
	}

	// parames 参数列表
	public String startTrade(Iso7816_Tag tag, String parames, int waitTime)
			throws DeviceTransException, DeviceConnectException {
		// 这里需要外部自己定义是否重新上下电

		try {
			diMap.putAll(MXBaseUtil.parseBerTlv(parames));
		} catch (Exception e) {
			e.printStackTrace();
			return CardDef.ERR_SZPARAMESVALUE;
		}
		m_sz9C = diMap.get("9C");
		if (m_sz9C == null || m_sz9C.length() != 2) {
			return CardDef.ERR_SZPARAMESVALUE;
		}

		m_sz9F02 = diMap.get("9F02");
		if (m_sz9F02 == null || m_sz9F02.length() != 12) {
			return CardDef.ERR_SZPARAMESVALUE;
		}

		m_sz9F4E = diMap.get("9F4E");
		m_sz9F37 = get9F37();
		// 这边交易日期应该是YYYYMMDD，9A只需要YYMMDD
		m_sz9A = diMap.get("9A");
		m_sz9F21 = diMap.get("9F21");
		if (m_sz9A == null || m_sz9F21 == null || m_sz9F37 == null) {
			tag.close();
			return CardDef.ERR_SZPARAMESVALUE;
		}

		int nRet = PbocUtil.select(tag, m_szAID);

		if (SWOK != nRet) {
			return CardDef.ERR_SZSELECTAID;
		}
		// 这里需要将map清空，否则会出问题
		diMap.clear();

		try {
			diMap.putAll(MXBaseUtil.parseBerTlv(tag.getResponse()));
		} catch (Exception e) {
			e.printStackTrace();
			return CardDef.ERR_SZSELECTRESPONSE;
		}
		if (!getPDOL())
			return CardDef.ERR_SZGPORESPONSE;
		String sPDOLData;
		try {
			sPDOLData = getPDOLData();
		} catch (Exception e) {
			e.printStackTrace();
			return CardDef.ERR_SZGPORESPONSE;
		}
		if (SWOK != (nRet = PbocUtil.getProcessOption(tag, sPDOLData))) {

			tag.close();
			return CardDef.ERR_SZGPORESPONSE;
		} else {
			if (tag.getRes().length() < 8) {

				tag.close();
				return CardDef.ERR_SZGPORESPONSE;
			}
			m_szAIP = tag.getRes().substring(4, 8);
			String sAFL = tag.getRes().substring(8);

			if (!PbocUtil.ReadAFL(tag, sAFL, diMap, waitTime)) {

				tag.close();
				return CardDef.ERR_SZREADAFL;
			}
		}
		return CardDef.STR_OK;
	}

	
	public List<Map<String, String>> getOfflineLog(Iso7816_Tag tag)
			throws DeviceTransException {
		int rv = PbocUtil.select(tag, m_szAID);
		if (SWOK != rv) {
			return null;
		}
		try {
			diMap.putAll(MXBaseUtil.parseBerTlv(MXBaseUtil.hex2byte(tag.getRes())));
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		int nSFI = 0;
		List<Map<String, String>> resultLog = new ArrayList<Map<String, String>>();
		String s9F4D = diMap.get("9F4D");
		if (s9F4D == null) {
			nSFI = 11;
		} else {
			String sSFI = s9F4D.substring(0, 2);
			nSFI = Integer.parseInt(sSFI, 16);
		}
		rv = tag.getData((byte) 0x9F, (byte) 0x4F);
		if (rv != SWOK) {
			return resultLog;
		}
		try {
			diMap.putAll(MXBaseUtil.parseBerTlv(tag.getResponse()));
		} catch (Exception e) {
			e.printStackTrace();
			return resultLog;
		}
		String sz9F4F = diMap.get("9F4F");
		List<BerTag> listTag = parse9F4F(sz9F4F);
		for (byte i = 1; i < 11; i++) {
			if (PbocUtil.readRecord(tag, (byte) nSFI, i) != SWOK) {
				break;
			} else {
				Map<String, String> recordMap = new HashMap<String, String>();
				String record = tag.getRes();
				for (BerTag berTag: listTag) {
					recordMap.put(berTag.getTag(), record.substring(0,berTag.getLen() * 2));
					record = record.substring(berTag.getLen() * 2);
				}
				resultLog.add(recordMap);
			}

		}
		return resultLog;
	}
	//这里需要排序map
	private List<BerTag> parse9F4F(String s9F4F) {
		List<BerTag> tagList = new ArrayList<BerTag>();
		byte[] by9F4F = MXBaseUtil.hex2byte(s9F4F);
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
			Map<String, Object> mapTagLen = new HashMap<String, Object>();
			mapTagLen.put("tag", sTag);
			mapTagLen.put("length", nLen);
			tagList.add(new BerTag(sTag, nLen));
			by9F4F = MXBaseUtil.subBytes(by9F4F, nOffset, by9F4F.length);
		}
		
		return tagList;
	}

	// 要求在select aid之后执行
	// public String getOfflineLog(Iso7816_Tag tag, String aid, int cardType)
	// throws DeviceTransException, DeviceConnectException {
	//
	// int rv = PbocUtil.select(tag, aid);
	// if (rv != SWOK) {
	// return CardDef.ERR_SZSELECTAID;
	// }
	//
	// int nSFI = 0;
	//
	// diMap.clear();
	// try {
	// diMap.putAll(MXBaseUtil.parseBerTlv(tag.getResponse()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return CardDef.ERR_SZSELECTRESPONSE;
	// }
	// String s9F4D = diMap.get("9F4D");
	// if (s9F4D == null) {
	// nSFI = 11;
	// } else {
	// String sSFI = s9F4D.substring(0, 2);
	// nSFI = Integer.parseInt(sSFI, 16);
	// }
	// rv = tag.getData((byte) 0x9F, (byte) 0x4F);
	// if (rv != SWOK) {
	// return CardDef.ERR_SZGETDATARESPONSE;
	// }
	// try {
	// diMap.putAll(MXBaseUtil.parseBerTlv(tag.getResponse()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// return CardDef.ERR_SZGETDATARESPONSE;
	// }
	// String sz9F4F = diMap.get("9F4F");
	//
	// ArrayList<String> logList = new ArrayList<String>();
	// String sLogString = null;
	// for (byte i = 1; i < 11; i++) {
	// if (PbocUtil.readRecord(tag, (byte) nSFI, i) != SWOK) {
	// break;
	// }
	// logList.add(tag.getRes());
	// }
	// if (logList.size() == 0) {
	// return "";
	// }
	// sLogString = PbocUtil.parseLog(MXBaseUtil.hex2byte(sz9F4F), logList,
	// cardType);
	// return sLogString;
	//
	// }
	
	/*
	 * Parames 结构 TYPE&TLV串（一般包括9A\9F4E）
	 */
	public Map<String, String> getMapForOnlineStartTrade(
			Iso7816_Tag tag, String szParames) throws DeviceTransException,
			DeviceConnectException {
		logPbocApplet("begingetMapForOnlineStartTrade");
		String szResp = startTrade(tag, szParames,0);
		if (CardDef.ERR_STRARRAY.contains(szResp)) {
			tag.close();
			return CardManagerUtil.getHashMapError(szResp);
		}

		int nRet = 0;
		// GAC1
		if (!getCDOL(true)) {
			logPbocApplet("GCDOL Failed");
			tag.close();
			return CardManagerUtil.getHashMapError(CardDef.ERR_SZCDOLGET);
		} else {
			logPbocApplet("8C " + m_szCDOL1);
		}

		if (SWOK != (nRet = GenerateAC(tag, true))) {
			logPbocApplet("GAC1 Failed,ret=" + Integer.toHexString(nRet));
			tag.close();
			return CardManagerUtil.getHashMapError(CardDef.ERR_SZGENERATEAC1);
		} else {
			String szResult = parseGacResponse(tag);
			if (!szResult.equals(CardDef.STR_OK)) {
				return CardManagerUtil.getHashMapError(szResult);
			} else {
				return diMap;
			}
		}
	}

	private void logPbocApplet(String string) {
		MXLog.i("pbocEntity", string);
	}

	public Map<String, String> getMapForHostTradeResponse(Iso7816_Tag tag,
			String sLoadInfo) throws DeviceTransException {

		diMap.remove("86");
		try {
			JSONObject jsonRes = new JSONObject(sLoadInfo);
			// 外部认证
			String authString = jsonRes.getString("91");
			String script = jsonRes.getString("script");
			int rv = -1;
			if (authString != null) {
				rv = PbocUtil.externAuth(tag, authString);
				if (SWOK != rv) {
					// return
					// AppSupport.getHashMapError(ErrorDef.ERR_SZPBOCEXAUTH);
					diMap.put("9F27", "00");
					// String sPutdataString = diMap.get("86");
					if (script != null) {
						Map<String, String> mapInfoMap = new HashMap<String, String>();
						mapInfoMap.put(ParamesDef.TC, "00000000");
						mapInfoMap.put(ParamesDef.RESULTSW,
								String.format("%02X", rv));
						return CardManagerUtil.getHashMapError(
								CardDef.ERR_NODOSCRIPT, mapInfoMap);
						// diMap.put("DF31", "0000000000");
						// return diMap;
					}
					diMap.put(ParamesDef.RESULTCODE, CardDef.ERR_PBOC_AUTH);
					diMap.put(ParamesDef.TC, "0000000000000000");
					diMap.put(ParamesDef.RESULTSW, String.format("%02X", rv));
					return diMap;
				}
			}
			if (!getCDOL(false)) {
				Map<String, String> mapInfoMap = new HashMap<String, String>();
				mapInfoMap.put(ParamesDef.TC, "00000000");
				mapInfoMap.put(ParamesDef.RESULTSW, "FFF2");
				return CardManagerUtil.getHashMapError(CardDef.ERR_SZCDOLGET,
						mapInfoMap);
			}
			diMap.put("8A", authString.substring(16, 20));
			rv = GenerateAC(tag, false);
			if (SWOK != rv) {
				diMap.put("9F27", "00");
				if (script != null) {
					diMap.put("DF31", "0000000000");
				}
				diMap.put(ParamesDef.RESULTCODE, CardDef.ERR_SZGENERATEAC2);
				diMap.put(ParamesDef.TC, "00000000");
				diMap.put(ParamesDef.RESULTSW, String.format("%02X", rv));
				return diMap;
			}
			String tc = tag.getRes().substring(10, 26);
			diMap.put(ParamesDef.TC, tc);
			String szResult = parseGacResponse(tag);
			if (!szResult.equals(CardDef.STR_OK)) {
				Map<String, String> mapInfoMap = new HashMap<String, String>();
				mapInfoMap.put(ParamesDef.TC, "00000000");
				mapInfoMap.put(ParamesDef.RESULTSW, "FFF4");
				return CardManagerUtil.getHashMapError(szResult);
			}

			if (script != null) {
				rv = doScrpit(tag, script);
				if (SWOK != rv) {
					// diMap.put("DF31", "0000000000");
					if (SWOK != rv && 0x6F00 != rv) {
						diMap.put(ParamesDef.RESULTSW,
								String.format("%02X", rv));
						diMap.put(ParamesDef.RESULTCODE,
								CardDef.ERR_DOSCRIPTERROR);
						return diMap;
						// /diMap.put("DF31", "1000000000");
					} else {
						diMap.put(ParamesDef.RESULTSW,
								String.format("%02X", rv));
						diMap.put(ParamesDef.RESULTCODE,
								CardDef.ERR_DOSCRIPTERROR);
						return diMap;
						// diMap.put("DF31", "1000000000");
					}
				} else {
					diMap.put(ParamesDef.RESULTCODE, CardDef.STR_OK);
					diMap.put("DF31", "2000000000");
					diMap.put(ParamesDef.RESULTSW, "9000");
				}
			}

			tag.close();
			return diMap;
		} catch (JSONException e1) {
			tag.close();
			e1.printStackTrace();
			return CardManagerUtil
					.getHashMapError(CardDef.ERR_SZHOSTRESPONSEERROR);
		}
	}

	private int GenerateAC(Iso7816_Tag tag, boolean bFirst)
			throws DeviceTransException {
		String sCmd = bFirst ? "80AE8000" : "80AE4000";
		String sCDOLData;
		try {
			sCDOLData = getCDOLData(bFirst);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		sCmd += String.format("%02X", sCDOLData.length() / 2);
		sCmd += sCDOLData;

		return tag.sendAPDU(sCmd);
	}

	// 执行发卡行脚本
	private static int doScrpit(Iso7816_Tag tag, String sPutData)
			throws DeviceTransException {

		/*
		 * if(15 != sPutData.length()/2){ sPutData = sPutData.substring(0,30); }
		 */

		return tag.sendAPDU(sPutData);
		// return 0xf831;
	}

	public String getData(Iso7816_Tag tag, byte tag1, byte tag2)
			throws DeviceTransException {

		if (tag.getData((byte) tag1, (byte) tag2) != SWOK) {
			return CardDef.ERR_SZGETDATAPARAMES;
		}
		return parseBalance(tag.getRes().substring(6));

	}
	
	public class BerTag {
		String tag;
		int len;
		public BerTag(String tag,int len) {
			this.tag = tag;
			this.len = len;
		}
		
		public String getTag() {
			return tag;
		}
		
		public int getLen() {
			return len;
		}
		
		
	}

}
