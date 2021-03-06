package com.mx.util.hft;

public class HftUtil {
	public static HftCardType getCardType(String data) {
		int nMainType = -1;
		int nSubType = -1;
		HftCardType hftCardType = new HftCardType();
		try {
			nMainType = Integer.parseInt(data.substring(0, 2));
			nSubType = Integer.parseInt(data.substring(2, 4));
		} catch (Exception e) {
			hftCardType.setMainType(-1);
			hftCardType.setSubType(-1);
			hftCardType.setMainTips("未知卡");
			hftCardType.setSubTips("未知卡");
			hftCardType.setOldTips("未知卡");
			return hftCardType;
		}
		
		
		hftCardType.setMainType(nMainType);
		hftCardType.setSubType(nSubType);
		//StringBuilder stringBuilder = new StringBuilder();
		//stringBuilder.append("主卡类型:");
		switch (nMainType) {
		case 0:
			hftCardType.setMainTips("普通卡");
//			stringBuilder.append("普通卡");
//			stringBuilder.append("");
//			stringBuilder.append(";子卡类型:");
			if (nSubType == 0) {
				//stringBuilder.append("普通卡");
				//stringBuilder.append(";原卡类型:K卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("K卡");
			} else if (nSubType == 9) {
				//stringBuilder.append("异形卡");
				//stringBuilder.append(";原卡类型:Y卡");
				hftCardType.setSubTips("异形卡");
				hftCardType.setOldTips("Y卡");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		case 1:
			//stringBuilder.append("记名卡");
			//stringBuilder.append(";");
			//stringBuilder.append("子卡类型:");
			hftCardType.setMainTips("记名卡");
			if (nSubType == 0) {
				//stringBuilder.append("普通卡");
				//stringBuilder.append(";原卡类型:A卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("A卡");
			} else if (nSubType == 9) {
				//stringBuilder.append("异形卡");
				//stringBuilder.append(";原卡类型:Y卡");
				hftCardType.setSubTips("异形卡");
				hftCardType.setOldTips("Y卡");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		case 2:
			//stringBuilder.append("优惠卡");
			//stringBuilder.append(";");
			//stringBuilder.append("子卡类型:");
			hftCardType.setMainTips("优惠卡");
			if (nSubType == 0) {
				//stringBuilder.append("普通卡");
				//stringBuilder.append(";原卡类型:B卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("B卡");
			} else if (nSubType == 1) {
				//stringBuilder.append("学生卡");
				//stringBuilder.append(";原卡类型:C卡");
				hftCardType.setSubTips("学生卡");
				hftCardType.setOldTips("C卡");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		case 3:
//			stringBuilder.append("低保卡");
//			stringBuilder.append(";");
//			stringBuilder.append("子卡类型:");
			hftCardType.setMainTips("低保卡");
			if (nSubType == 0) {
				//stringBuilder.append("普通卡");
				//stringBuilder.append(";原卡类型:D卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("D卡");
			} else if (nSubType == 1) {
				//stringBuilder.append("学生卡");
				//stringBuilder.append(";原卡类型:E卡");
				hftCardType.setSubTips("学生卡");
				hftCardType.setOldTips("E卡");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		case 4:
//			stringBuilder.append("免费卡");
//			stringBuilder.append(";");
//			stringBuilder.append("子卡类型：");
			hftCardType.setMainTips("免费卡");
			if (nSubType == 0) {
				//stringBuilder.append("普通卡");
				//stringBuilder.append(";原卡类型:未知卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("无");
			} else if (nSubType == 2) {
				//stringBuilder.append("残疾卡");
				//stringBuilder.append(";原卡类型:F卡");
				hftCardType.setSubTips("残疾卡");
				hftCardType.setOldTips("F卡");
			} else if (nSubType == 3) {
//				stringBuilder.append("老人卡");
//				stringBuilder.append(";原卡类型:H卡");
				hftCardType.setSubTips("老人卡");
				hftCardType.setOldTips("H卡");
			} else if (nSubType == 4) {
//				stringBuilder.append("军人卡");
//				stringBuilder.append(";原卡类型:J卡");
				hftCardType.setSubTips("军人卡");
				hftCardType.setOldTips("J卡");
			} else if (nSubType == 5) {
//				stringBuilder.append("离休干部卡");
//				stringBuilder.append(";原卡类型:未知卡");
				hftCardType.setSubTips("离休干部卡");
				hftCardType.setOldTips("无");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		case 5:
//			stringBuilder.append("员工卡");
//			stringBuilder.append(";");
//			stringBuilder.append("子卡类型:");
			hftCardType.setMainTips("员工卡");
			if (nSubType == 0) {
//				stringBuilder.append("普通卡");
//				stringBuilder.append(";原卡类型:G卡");
				hftCardType.setSubTips("普通卡");
				hftCardType.setOldTips("G卡");
			} else if (nSubType == 5) {
//				stringBuilder.append("司机卡");
//				stringBuilder.append(";原卡类型:未知卡");
				hftCardType.setSubTips("司机卡");
				hftCardType.setOldTips("无");
			} else if (nSubType == 6) {
//				stringBuilder.append("临时卡");
//				stringBuilder.append(";原卡类型:未知卡");
				hftCardType.setSubTips("临时卡");
				hftCardType.setOldTips("无");
			} else {
				//stringBuilder.append("未知卡;原卡类型:未知卡");
				hftCardType.setSubTips("未知卡");
				hftCardType.setOldTips("未知卡");
			}
			break;
		default:
//			stringBuilder.append("未知卡");
//			stringBuilder.append(";");
//			stringBuilder.append("子卡类型:未知卡");
//			stringBuilder.append(";原卡类型:未知卡");
			hftCardType.setMainTips("未知卡");
			hftCardType.setSubTips("未知卡");
			hftCardType.setOldTips("未知卡");
			break;
		}
		return hftCardType;
	}
}
