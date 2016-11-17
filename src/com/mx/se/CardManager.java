package com.mx.se;

import java.io.IOException;
import java.util.Map;

import com.cttic.se.ConnectException;
import com.cttic.se.TransportException;

public interface CardManager {
	// public String getCardOffLineInfoSimple(int nType) throws IOException;
	Map<String, Object> getCardOffLineInfo(int nType);

	public Map<String, String> getMapForHostTradeResponse(String szResponse)
			throws IOException, ConnectException, TransportException;

	public Map<String, String> getMapForOnlineStartTrade(String szParames)
			throws IOException, ConnectException, TransportException;

	// get trans prov,object列表参数根据实际的卡片类型不同来确认具体的数据参数类型
	public Map<String, String> getTransProv(Object[] objList)
			throws IOException, ConnectException, TransportException;

	public String getCardOffLineInfoSimple(int nType) throws IOException,
	ConnectException, TransportException;

	public void connect() throws ConnectException;

	public String readRecord(int sfi,int recNo) throws TransportException;

	public String sendApdu(String apdu) throws TransportException;
}
