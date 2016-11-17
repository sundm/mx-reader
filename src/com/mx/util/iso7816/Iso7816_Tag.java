package com.mx.util.iso7816;

import com.mx.util.exception.DeviceTransException;


public interface Iso7816_Tag {
	public static final int SWOK = 0x9000;
	
	public int sendAPDU(String szCmd) throws DeviceTransException;
	public byte[] getResponse();
	public String getRes();
	public void close();
	public int verify(byte[] pin) throws DeviceTransException;
	public int readRecord(int sfi,int recordNo) throws DeviceTransException;
	public int getData(byte tag1,byte tag2) throws DeviceTransException;
}
