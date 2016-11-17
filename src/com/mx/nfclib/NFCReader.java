package com.mx.nfclib;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;

import com.cttic.se.ConnectCallback;
import com.cttic.se.ConnectException;
import com.cttic.se.CtticReader;
import com.cttic.se.DeleteDeviceException;
import com.cttic.se.TimeoutException;
import com.cttic.se.TransportException;
import com.mx.util.MXCardDef;
import com.mx.util.MXLog;

public class NFCReader implements CtticReader {
	private IsoDep mIsoDep;

	private boolean isOpened = false;

	public static String[][] TECHLISTS;
	public static IntentFilter[] FILTERS;

	private static NFCReader mNfcCtticReader = new NFCReader();

	private NFCReader() {

	}

	static {
		try {
			TECHLISTS = new String[][] { { IsoDep.class.getName() }, { NfcV.class.getName() },
					{ NfcF.class.getName() }, { NfcB.class.getName() } };

			FILTERS = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*") };
		} catch (Exception e) {
			MXLog.e("CardManager", IsoDep.class.getName() + NfcV.class.getName() + NfcF.class.getName(), e);
		}
	}

	// NFC CONTROL
	public static boolean isNfcSupported(Context context) {
		if (checkNfcStatus(context) == MXCardDef.ERR_NONFCFUNCTION) {
			return false;
		}
		return true;
	}

	public static boolean isNfcEnabled(Context context) {
		if (checkNfcStatus(context) == 0) {
			return true;
		}
		return false;
	}

	private static int checkNfcStatus(Context context) {
		NfcAdapter nfcAdapter;
		nfcAdapter = NfcAdapter.getDefaultAdapter(context);
		if (nfcAdapter == null) {
			return MXCardDef.ERR_NONFCFUNCTION;
		} else {
			if (nfcAdapter.isEnabled() == false) {
				return MXCardDef.ERR_NFCFUNCTIONCLOSE;
			}
		}
		return 0;
	}

	public static void disableForegroundDispatch(Activity activity) {

		NfcAdapter nfcAdapter;
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(activity);
		}
	}

	public static synchronized NFCReader getInstance() {
		if (mNfcCtticReader == null) {
			mNfcCtticReader = new NFCReader();
		}
		return mNfcCtticReader;
	}

	public static void enableForegroundDispatch(Activity activity) {

		PendingIntent pendingIntent;
		pendingIntent = PendingIntent.getActivity(activity, 0,
				new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		NfcAdapter nfcAdapter;
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity);

		if (nfcAdapter != null) {
			nfcAdapter.enableForegroundDispatch(activity, pendingIntent, FILTERS, TECHLISTS);

		}
	}

	public static int initNfcEnvironment(Intent intent) {
		return SetNfc(intent);
	}

	private static int SetNfc(Intent intent) {
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Parcelable parcelNfc;
			parcelNfc = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

			if (parcelNfc != null) {
				final Tag tag = (Tag) parcelNfc;
				mNfcCtticReader.mIsoDep = IsoDep.get(tag);
				if (mNfcCtticReader.mIsoDep == null) {
					return MXCardDef.ERR_NFCINIT;
				} else {
					return 0;
				}
			}
			return MXCardDef.ERR_NFCINIT;
		}
		return MXCardDef.ERR_NOTNFCACTION;
	}

	@Override
	public byte[] open(String address, long timeOut, byte[] scanInfo) throws TimeoutException {
		if (mIsoDep != null) {
			isOpened = true;
			return new byte[] { 0x00, 0x00, 0x00 };
		} else {
			isOpened = false;
			return new byte[] { 0x01, 0x00, 0x00 };
		}
	}

	@Override
	public void close() {
		isOpened = false;
	}

	@Override
	public boolean reopen(long timeOut) throws TimeoutException {
		if (mIsoDep != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void registerConnectCallback(ConnectCallback callback) {

	}

	@Override
	public String getAPIVersion() {
		return null;
	}

	@Override
	public String getDeviceVersion() throws ConnectException {
		return null;
	}

	@Override
	public byte[] powerOn(long timeOut) throws ConnectException, TimeoutException {
		if (mIsoDep == null) {
			throw new ConnectException("no device");
		}
		try {
			if (mIsoDep.isConnected()) {
				return null;
			}
			mIsoDep.connect();
		} catch (IOException e) {
			throw new ConnectException(e);
		}
		return null;
	}

	@Override
	public boolean isPowerOn() throws ConnectException {
		return mIsoDep.isConnected();
	}


	@Override
	public byte[] exchangeWithData(byte[] data, long timeOut) throws TimeoutException, ConnectException,
			TransportException {
		try {
			return mIsoDep.transceive(data);
		} catch (IOException e) {
			throw new TransportException(e);
		}
	}

	@Override
	public boolean powerOff() {
		try {
			mIsoDep.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean bind(BindType bindType, String data, String phoneNum) throws ConnectException, TimeoutException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public byte[] getDeviceId() throws ConnectException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteDevice(String phoneNum) throws DeleteDeviceException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isOpened() {
		// TODO Auto-generated method stub
		return isOpened;
	}

}
