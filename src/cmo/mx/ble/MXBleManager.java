package cmo.mx.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

public class MXBleManager {
	private static MXBleManager mMxBleManager;
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothAdapter.LeScanCallback mLeScanCallback;
	private Context mContext;

	private Handler mHandler;

	private MXBleManager(Context context) {
		this.mContext = context;
		mHandler = new Handler();
	}

	private void setContext(Context context) {
		this.mContext = context;
	}

	public static synchronized MXBleManager getInstance(Context context) {
		if (mMxBleManager == null) {
			mMxBleManager = new MXBleManager(context);
		} else {
			mMxBleManager.setContext(context);
		}

		return mMxBleManager;

	}

	public boolean scanLeDevice(BluetoothAdapter.LeScanCallback callback, int timeOut) {
		if (mBluetoothAdapter == null) {
			return false;
		}
		this.mLeScanCallback = callback;

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopLeDevice();
			}
		}, timeOut);

		return mBluetoothAdapter.startLeScan(mLeScanCallback);
	}

	public void stopLeDevice() {
		if (mBluetoothAdapter == null) {
			return;
		}
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
	}

	public boolean initBLE() {

		if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}

		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		}

		if (mBluetoothManager == null) {
			return false;
		}

		if (mBluetoothAdapter == null) {
			mBluetoothAdapter = mBluetoothManager.getAdapter();
		}

		if (mBluetoothAdapter == null) {
			return false;
		}

		return true;
	}

	public boolean isEnabled() {

		if (mBluetoothManager == null) {
			return false;
		}
		if (mBluetoothAdapter == null) {
			return false;
		}
		return mBluetoothAdapter.isEnabled();

	}

}
