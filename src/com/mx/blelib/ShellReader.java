package com.mx.blelib;

import android.content.Context;

import com.cttic.se.ConnectCallback;
import com.cttic.se.ConnectException;
import com.cttic.se.CtticReader;
import com.cttic.se.DeleteDeviceException;
import com.cttic.se.TimeoutException;
import com.cttic.se.TransportException;
import com.decard.cardreader.toad.lib.ByteResult;
import com.decard.cardreader.toad.lib.CardReader;
import com.decard.cardreader.toad.lib.util.HexDump;
import com.mx.util.MXLog;

public class ShellReader implements CtticReader {

	private static ShellReader mShellReader;

	private static CardReader mCardReader = null;

	private String currentDeviceAddress;

	private boolean isPowerOn = false;
	private boolean isOpened = false;

	final private static String TAG = "shell-reader";

	private ShellReader() {

	}

	public static synchronized ShellReader getInstance(Context context) {
		if (mShellReader == null) {
			mShellReader = new ShellReader();
		}
		if (mCardReader == null) {
			mCardReader = new CardReader(context);
			mCardReader.init();
		}
		return mShellReader;
	}

	@Override
	public byte[] open(String address, long timeOut, byte[] scanInfo) throws TimeoutException {
		// TODO Auto-generated method stub
		if (mCardReader != null && mCardReader.openWithAddress(address)) {
			currentDeviceAddress = address;
			MXLog.i(TAG, "连接成功");
			isOpened = true;
			return new byte[] { 0x00 };
		} else {
			isOpened = false;
			return new byte[] { 0x01 };
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (mCardReader != null) {
			mCardReader.close();
		}
		isOpened = false;
	}

	@Override
	public boolean reopen(long timeOut) throws TimeoutException {
		// TODO Auto-generated method stub
		isOpened = false;
		if (mCardReader == null || currentDeviceAddress == null || currentDeviceAddress.isEmpty()) {
			return false;
		} else {
			return mCardReader.openWithAddress(currentDeviceAddress);
		}
	}

	@Override
	public void registerConnectCallback(ConnectCallback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean bind(BindType bindType, String data, String phoneNum) throws ConnectException, TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAPIVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceVersion() throws ConnectException {
		// TODO Auto-generated method stub
		if (mCardReader == null) {
			throw new ConnectException("the device is null");
		}
		return null;
	}

	public String getBatteryLevel() throws ConnectException {
		if (mCardReader == null) {
			throw new ConnectException("the device is null");
		}
		return mCardReader.getBatteryLevel(5);

	}

	@Override
	public byte[] getDeviceId() throws ConnectException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] powerOn(long timeOut) throws ConnectException, TimeoutException {
		// TODO Auto-generated method stub
		isPowerOn = false;
		if (mCardReader == null) {
			throw new ConnectException("the device is null");
		}

		if (!mCardReader.isOpened()) {
			throw new ConnectException("the device is not open");
		}

		byte[] cmd = new byte[] { (byte) 0xff, 0x70, 0x02, 0x00, 0x02, 0x00, 0x02 };
		ByteResult recData = new ByteResult();
		ByteResult recSate = new ByteResult();

		long flag = mCardReader.sendApdu(cmd, recData, recSate, timeOut);
		switch ((int) flag) {
		case CardReader.APDU_EXCHANGE_SUCESS: {
			isPowerOn = true;
			return recData.byteArr != null ? recData.byteArr : null;
		}
		case CardReader.APDU_EXCHANGE_TIMEOUT: {
			throw new TimeoutException("powerOn timeOut");
		}
		case CardReader.APDU_EXCHANGE_OTHER_ERROR: {
			throw new ConnectException("powerOn send error");
		}
		default: {
			throw new ConnectException("powerOn other error");
		}

		}
	}

	@Override
	public boolean isPowerOn() throws ConnectException {
		// TODO Auto-generated method stub
		return isPowerOn;
	}

	@Override
	public byte[] exchangeWithData(byte[] data, long timeOut) throws TimeoutException, ConnectException,
			TransportException {
		// TODO Auto-generated method stub
		if (!mCardReader.isOpened()) {
			throw new ConnectException("the device should open first");
		}
		if (!isPowerOn) {
			throw new ConnectException("the device should powerOn first");
		}

		ByteResult recData = new ByteResult();
		ByteResult recSate = new ByteResult();

		long flag = mCardReader.sendApdu(data, recData, recSate, timeOut);
		switch ((int) flag) {
		case CardReader.APDU_EXCHANGE_SUCESS: {
			String resultStrData = recData.byteArr != null ? HexDump.toHexString(recData.byteArr) : "";
			String resultStrState = recSate.byteArr != null ? HexDump.toHexString(recSate.byteArr) : "";
			if (resultStrState.isEmpty()) {
				throw new TransportException("sw is empty");
			}

			String respString = resultStrData + resultStrState;

			return HexDump.hexStringToByteArray(respString);
		}
		case CardReader.APDU_EXCHANGE_TIMEOUT: {
			throw new TimeoutException("exchange apdu time out");
		}
		case CardReader.APDU_EXCHANGE_OTHER_ERROR: {
			throw new TransportException("exchange apdu error");
		}
		default: {
			break;
		}
		}
		return null;
	}

	@Override
	public boolean powerOff() {
		// TODO Auto-generated method stub
		if (mCardReader == null) {
			return false;
		}

		if (!mCardReader.isOpened()) {
			return false;
		}

		byte[] cmd = new byte[] { (byte) 0xff, 0x70, 0x03, 0x00, 0x02, 0x00, 0x02 };
		ByteResult recData = new ByteResult();
		ByteResult recSate = new ByteResult();

		long flag = mCardReader.sendApdu(cmd, recData, recSate, 5);
		switch ((int) flag) {
		case CardReader.APDU_EXCHANGE_SUCESS: {
			isPowerOn = false;
			return true;
		}
		case CardReader.APDU_EXCHANGE_TIMEOUT: {
			return false;
		}
		case CardReader.APDU_EXCHANGE_OTHER_ERROR: {
			return false;
		}
		default: {
			return false;
		}

		}
	}

	@Override
	public boolean deleteDevice(String phoneNum) throws DeleteDeviceException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOpened() {
		// TODO Auto-generated method stub
		return isOpened;
	}

}
