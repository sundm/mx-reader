package com.mx.se;

import java.util.HashMap;
import java.util.Map;

public class CardManagerUtil {

	public static HashMap<String, String> getHashMapError(String szError){
		HashMap<String, String> hashResult = new HashMap<String, String>();
		hashResult.put(ParamesDef.RESULTCODE, szError);
		return hashResult;
	}

	
	public static Map<String, Object> packCardOut(int nCode,Object data){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CardDef.CARDRESULTCODE, nCode);
		map.put(CardDef.CARDRESULTDATA, data);
		return map;
	}
	
	public static Map<String, String> getHashMapError(String szError,Map<String, String> info) {
		HashMap<String, String> hashResult = new HashMap<String, String>();
		hashResult.putAll(info);
		hashResult.put(ParamesDef.RESULTCODE, szError);
		return hashResult;
	}
}
