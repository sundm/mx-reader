package com.mx.util.iso7816;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.cttic.se.ConnectException;
import com.cttic.se.CtticReader;
import com.cttic.se.TimeoutException;
import com.mx.util.MXBaseUtil;
import com.mx.util.MXLog;

public class Iso7816Impl {
	public static final byte[] EMPTY = { 0 };
	public static String m_response;

	protected byte[] data;

	protected Iso7816Impl() {
		data = Iso7816Impl.EMPTY;
	}

	protected Iso7816Impl(byte[] bytes) {
		data = (bytes == null) ? Iso7816Impl.EMPTY : bytes;
	}

	public boolean match(byte[] bytes) {
		return match(bytes, 0);
	}

	public boolean match(byte[] bytes, int start) {
		final byte[] data = this.data;
		if (data.length <= bytes.length - start) {
			for (final byte v : data) {
				if (v != bytes[start++])
					return false;
			}
		}
		return true;
	}

	public boolean match(byte tag) {
		return (data.length == 1 && data[0] == tag);
	}

	public boolean match(short tag) {
		final byte[] data = this.data;
		if (data.length == 2) {
			final byte d0 = (byte) (0x000000FF & tag);
			final byte d1 = (byte) (0x000000FF & (tag >> 8));
			return (data[0] == d0 && data[1] == d1);
		}
		return false;
	}

	public int size() {
		return data.length;
	}

	public byte[] getBytes() {
		return data;
	}

	@Override
	public String toString() {
		return MXBaseUtil.hex2Ascii(data);
	}

	public final static class ID extends Iso7816Impl {
		public ID(byte[] bytes) {
			super(bytes);
		}
	}

	public final static class Response extends Iso7816Impl {
		public static final byte[] EMPTY = {};
		public static final byte[] ERROR = { 0x6F, 0x00 }; // SW_UNKNOWN

		public Response(byte[] bytes) {
			super((bytes == null || bytes.length < 2) ? Response.ERROR : bytes);
		}

		public byte getSw1() {
			return data[data.length - 2];
		}

		public byte getSw2() {
			return data[data.length - 1];
		}

		public short getSw12() {
			final byte[] d = this.data;
			int n = d.length;
			return (short) ((d[n - 2] << 8) | (0xFF & d[n - 1]));
		}

		public boolean isOkey() {
			return equalsSw12(SW_NO_ERROR);
		}

		public boolean equalsSw12(short val) {
			return getSw12() == val;
		}

		public int size() {
			return data.length - 2;
		}

		public byte[] getBytes() {
			return isOkey() ? Arrays.copyOfRange(data, 0, size()) : Response.EMPTY;
		}
	}

	public final static class BerT extends Iso7816Impl {

		public static final byte TMPL_FCP = 0x62;
		public static final byte TMPL_FMD = 0x64;
		public static final byte TMPL_FCI = 0x6F;

		public final static BerT CLASS_PRI = new BerT((byte) 0xA5);

		public final static BerT CLASS_SFI = new BerT((byte) 0x88);

		public final static BerT CLASS_DFN = new BerT((byte) 0x84);

		public final static BerT CLASS_ADO = new BerT((byte) 0x61);

		public final static BerT CLASS_AID = new BerT((byte) 0x4F);

		public static int test(byte[] bytes, int start) {
			int len = 1;
			if ((bytes[start] & 0x1F) == 0x1F) {
				while ((bytes[start + len] & 0x80) == 0x80)
					++len;

				++len;
			}
			return len;
		}

		public static BerT read(byte[] bytes, int start) {
			return new BerT(Arrays.copyOfRange(bytes, start, start + test(bytes, start)));
		}

		public BerT(byte tag) {
			this(new byte[] { tag });
		}

		public BerT(short tag) {
			this(new byte[] { (byte) (0x000000FF & (tag >> 8)), (byte) (0x000000FF & tag) });
		}

		public BerT(byte[] bytes) {
			super(bytes);
		}

		public boolean hasChild() {
			return ((data[0] & 0x20) == 0x20);
		}
	}

	public final static class BerL extends Iso7816Impl {
		private final int val;

		public static int test(byte[] bytes, int start) {
			int len = 1;
			if ((bytes[start] & 0x80) == 0x80) {
				len += bytes[start] & 0x07;
			}
			return len;
		}

		public static int calc(byte[] bytes, int start) {
			if ((bytes[start] & 0x80) == 0x80) {
				int v = 0;

				int e = start + bytes[start] & 0x07;
				while (++start <= e) {
					v <<= 8;
					v |= bytes[start] & 0xFF;
				}

				return v;
			}

			return bytes[start];
		}

		public static BerL read(byte[] bytes, int start) {
			return new BerL(Arrays.copyOfRange(bytes, start, start + test(bytes, start)));
		}

		public BerL(byte[] bytes) {
			super(bytes);
			val = calc(bytes, 0);
		}

		public int toInt() {
			return val;
		}
	}

	public final static class BerV extends Iso7816Impl {
		public static BerV read(byte[] bytes, int start, int len) {
			return new BerV(Arrays.copyOfRange(bytes, start, start + len));
		}

		public BerV(byte[] bytes) {
			super(bytes);
		}
	}

	public final static class BerTLV extends Iso7816Impl {
		public static int test(byte[] bytes, int start) {
			final int lt = BerT.test(bytes, start);
			final int ll = BerL.test(bytes, start + lt);
			final int lv = BerL.calc(bytes, start + lt);

			return lt + ll + lv;
		}

		public static BerTLV read(Iso7816Impl obj) {
			return read(obj.getBytes(), 0);
		}

		public static BerTLV read(byte[] bytes, int start) {
			int s = start;
			final BerT t = BerT.read(bytes, s);
			s += t.size();

			final BerL l = BerL.read(bytes, s);
			s += l.size();

			final BerV v = BerV.read(bytes, s, l.toInt());
			s += v.size();

			final BerTLV tlv = new BerTLV(t, l, v);
			tlv.data = Arrays.copyOfRange(bytes, start, s);

			return tlv;
		}

		public static ArrayList<BerTLV> readList(Iso7816Impl obj) {
			return readList(obj.getBytes());
		}

		public static ArrayList<BerTLV> readList(final byte[] data) {
			final ArrayList<BerTLV> ret = new ArrayList<BerTLV>();

			int start = 0;
			int end = data.length - 3;
			while (start < end) {
				final BerTLV tlv = read(data, start);
				ret.add(tlv);

				start += tlv.size();
			}

			return ret;
		}

		public final BerT t;
		public final BerL l;
		public final BerV v;

		public BerTLV(BerT t, BerL l, BerV v) {
			this.t = t;
			this.l = l;
			this.v = v;
		}

		public BerTLV getChildByTag(BerT tag) {
			if (t.hasChild()) {
				final byte[] raw = v.getBytes();
				int start = 0;
				int end = raw.length;
				while (start < end) {
					if (tag.match(raw, start))
						return read(raw, start);

					start += test(raw, start);
				}
			}

			return null;
		}

		public BerTLV getChild(int index) {
			if (t.hasChild()) {
				final byte[] raw = v.getBytes();
				int start = 0;
				int end = raw.length;

				int i = 0;
				while (start < end) {
					if (i++ == index)
						return read(raw, start);

					start += test(raw, start);
				}
			}

			return null;
		}
	}

	public final static class ReaderTag implements Iso7816_Tag {
		// private AndroidDeviceApi<Boolean, Context, Object> mAndroidDeviceApi;
		private CtticReader mCtticReader;

		public ReaderTag(CtticReader iccReader) {
			mCtticReader = iccReader;
		}

		public int verify(byte[] byPin) {
			String szCmd = "00200000" + String.format("%02X", byPin.length) + MXBaseUtil.byte2hex(byPin);

			return sendAPDU(MXBaseUtil.hex2byte(szCmd));
		}

		public int readRecord(int sfi, int index) {
			final byte[] cmd = { (byte) 0x00, (byte) 0xB2, (byte) index, (byte) ((sfi << 3) | 0x04), (byte) 0x00, };

			return sendAPDU(cmd);
		}

		public int readBinary(int sfi) {
			final byte[] cmd = { (byte) 0x00, (byte) 0xB0, (byte) (0x00000080 | (sfi & 0x1F)), (byte) 0x00,
					(byte) 0x00, };

			return sendAPDU(cmd);
		}

		public int readData(int sfi) {
			final byte[] cmd = { (byte) 0x80, (byte) 0xCA, (byte) 0x00, (byte) (sfi & 0x1F), (byte) 0x00, };

			return sendAPDU(cmd);
		}

		public int getData(byte tag1, byte tag2) {
			final byte[] cmd = { (byte) 0x80, (byte) 0xCA, tag1, tag2, (byte) 0x00, };

			return sendAPDU(cmd);
		}

		public int selectByID(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			buff.put((byte) 0x00).put((byte) 0xA4).put((byte) 0x00).put((byte) 0x00).put((byte) name.length).put(name)
					.put((byte) 0x00);

			return sendAPDU(buff.array());
		}

		public int selectByName(byte... name) {
			ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
			buff.put((byte) 0x00).put((byte) 0xA4).put((byte) 0x04).put((byte) 0x00).put((byte) name.length).put(name)
					.put((byte) 0x00);

			return sendAPDU(buff.array());
		}

		// TODO
		public boolean isOpen() {
			return true;
		}

		public void dicConnect() {
			mCtticReader.close();
		}

		public byte[] transceive(final byte[] cmd) {
			try {
				MXLog.i("apdu", "APDU Send");

				MXLog.i("apdu", MXBaseUtil.byte2hex(cmd));

				byte[] s = mCtticReader.exchangeWithData(cmd, 2000);

				MXLog.i("apdu", "APDU Response");

				MXLog.i("apdu", MXBaseUtil.byte2hex(s));

				return s;
			} catch (Exception e) {
				MXLog.e("apdu", MXBaseUtil.byte2hex(cmd), e);
				return Response.ERROR;
			}
		}

		public int sendAPDU(String szCmd) {
			m_response = "";
			String cmd = szCmd.replaceAll(" ", "");
			if (cmd.length() == 8) {
				cmd += "00";
			}
			// cmd += "00";
			int rv = 0;
			byte[] capdu = MXBaseUtil.hex2byte(cmd);
			rv = sendAPDU(capdu);

			return rv;
		}

		public byte[] getResponse() {
			return MXBaseUtil.hex2byte(m_response);
		}

		public String getRes() {
			return m_response;
		}

		private int sendAPDU(byte[] capdu) {

			byte[] rapdu = transceive(capdu);

			int iLen = rapdu.length;
			if (iLen < 2)
				return 0x6F00;
			int iSW = rapdu[iLen - 2] & 0xFF;
			iSW = iSW * 256;
			iSW = iSW + (rapdu[iLen - 1] & 0xFF);
			if (iSW / 256 == 0x61) {
				byte[] getresponse = { 0x00, (byte) 0xC0, 0x00, 0x00, 0x00 };
				getresponse[4] = (byte) (iSW % 256);
				iSW = sendAPDU(getresponse);
			} else if (iSW / 256 == 0x6C) {
				capdu[capdu.length - 1] = (byte) (iSW % 256);
				iSW = sendAPDU(capdu);
			} else if (iSW == 0x6310) {
				capdu[3] = 0x01;
				iSW = sendAPDU(capdu);
				m_response += MXBaseUtil.byte2hex(rapdu, iLen - 2);
			} else if (iSW == 0)
				return iSW | 0xFFFF;
			else {
				byte[] temp = MXBaseUtil.subBytes(rapdu, 0, iLen - 2);
				m_response = MXBaseUtil.byte2hex(temp);
			}

			return iSW;
		}

		/**
		 * ��＄��涓���垫��浠わ����ㄤ����ゆ�������������＄����ㄥ�″�����
		 * 
		 * @return true �����＄����ㄥ�″�����锛�false 娌℃����＄�������″�����
		 * @throws TimeoutException
		 * @throws ConnectException
		 */
		public byte[] powerOn(long timeout) throws ConnectException, TimeoutException {
			return mCtticReader.powerOn(timeout);
		}

		@Override
		public void close() {
			mCtticReader.powerOff();
		}

	}

	public static final short SW_NO_ERROR = (short) 0x9000;
	public static final short SW_BYTES_REMAINING_00 = 0x6100;
	public static final short SW_WRONG_LENGTH = 0x6700;
	public static final short SW_SECURITY_STATUS_NOT_SATISFIED = 0x6982;
	public static final short SW_FILE_INVALID = 0x6983;
	public static final short SW_DATA_INVALID = 0x6984;
	public static final short SW_CONDITIONS_NOT_SATISFIED = 0x6985;
	public static final short SW_COMMAND_NOT_ALLOWED = 0x6986;
	public static final short SW_APPLET_SELECT_FAILED = 0x6999;
	public static final short SW_WRONG_DATA = 0x6A80;
	public static final short SW_FUNC_NOT_SUPPORTED = 0x6A81;
	public static final short SW_FILE_NOT_FOUND = 0x6A82;
	public static final short SW_RECORD_NOT_FOUND = 0x6A83;
	public static final short SW_INCORRECT_P1P2 = 0x6A86;
	public static final short SW_WRONG_P1P2 = 0x6B00;
	public static final short SW_CORRECT_LENGTH_00 = 0x6C00;
	public static final short SW_INS_NOT_SUPPORTED = 0x6D00;
	public static final short SW_CLA_NOT_SUPPORTED = 0x6E00;
	public static final short SW_UNKNOWN = 0x6F00;
	public static final short SW_FILE_FULL = 0x6A84;

}
