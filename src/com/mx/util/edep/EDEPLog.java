package com.mx.util.edep;

public class EDEPLog {

	// 交易序号
	public int tradeNO;
	// 交易金额
	public String tradeAmount;
	// 交易类型
	public String tradeType;
	// 交易日期
	public String tradeDate;

	public int getTradeNO() {
		return tradeNO;
	}

	public void setTradeNO(int tradeNO) {
		this.tradeNO = tradeNO;
	}

	public String getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	@Override
	public String toString() {
		return "EDEPLog [tradeNO=" + tradeNO + ", tradeAmount=" + tradeAmount + ", tradeType=" + tradeType
				+ ", tradeDate=" + tradeDate + "]";
	}

}
