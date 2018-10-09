
package woyou.aidlservice.jiuiv5;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IYmodemSPI;

public interface IWoyouService extends IInterface {
	void updateFirmware(byte[] var1, long var2, String var4, IYmodemSPI var5) throws RemoteException;

	int getFirmwareStatus() throws RemoteException;

	String getServiceVersion() throws RemoteException;

	void printerInit(ICallback var1) throws RemoteException;

	void printerSelfChecking(ICallback var1) throws RemoteException;

	String getPrinterSerialNo() throws RemoteException;

	String getPrinterVersion() throws RemoteException;

	String getPrinterModal() throws RemoteException;

	void getPrintedLength(ICallback var1) throws RemoteException;

	void lineWrap(int var1, ICallback var2) throws RemoteException;

	void sendRAWData(byte[] var1, ICallback var2) throws RemoteException;

	void setAlignment(int var1, ICallback var2) throws RemoteException;

	void setFontName(String var1, ICallback var2) throws RemoteException;

	void setFontSize(float var1, ICallback var2) throws RemoteException;

	void printText(String var1, ICallback var2) throws RemoteException;

	void printTextWithFont(String var1, String var2, float var3, ICallback var4) throws RemoteException;

	void printColumnsText(String[] var1, int[] var2, int[] var3, ICallback var4) throws RemoteException;

	void printBitmap(Bitmap var1, ICallback var2) throws RemoteException;

	void printBarCode(String var1, int var2, int var3, int var4, int var5, ICallback var6) throws RemoteException;

	void printQRCode(String var1, int var2, int var3, ICallback var4) throws RemoteException;

	void printOriginalText(String var1, ICallback var2) throws RemoteException;

	public abstract static class Stub extends Binder implements IWoyouService {
		private static final String DESCRIPTOR = "woyou.aidlservice.jiuiv5.IWoyouService";
		static final int TRANSACTION_updateFirmware = 1;
		static final int TRANSACTION_getFirmwareStatus = 2;
		static final int TRANSACTION_getServiceVersion = 3;
		static final int TRANSACTION_printerInit = 4;
		static final int TRANSACTION_printerSelfChecking = 5;
		static final int TRANSACTION_getPrinterSerialNo = 6;
		static final int TRANSACTION_getPrinterVersion = 7;
		static final int TRANSACTION_getPrinterModal = 8;
		static final int TRANSACTION_getPrintedLength = 9;
		static final int TRANSACTION_lineWrap = 10;
		static final int TRANSACTION_sendRAWData = 11;
		static final int TRANSACTION_setAlignment = 12;
		static final int TRANSACTION_setFontName = 13;
		static final int TRANSACTION_setFontSize = 14;
		static final int TRANSACTION_printText = 15;
		static final int TRANSACTION_printTextWithFont = 16;
		static final int TRANSACTION_printColumnsText = 17;
		static final int TRANSACTION_printBitmap = 18;
		static final int TRANSACTION_printBarCode = 19;
		static final int TRANSACTION_printQRCode = 20;
		static final int TRANSACTION_printOriginalText = 21;

		public Stub() {
			this.attachInterface(this, "woyou.aidlservice.jiuiv5.IWoyouService");
		}

		public static IWoyouService asInterface(IBinder obj) {
			if(obj == null) {
				return null;
			} else {
				IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.IWoyouService");
				return (IWoyouService)(iin != null && iin instanceof IWoyouService?(IWoyouService)iin:new Proxy(obj));
			}
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			String _arg0;
			ICallback _arg1;
			int _arg2;
			ICallback _arg3;
			int _arg11;
			int _arg04;
			byte[] _arg05;
			ICallback _arg06;
			switch(code) {
				case 1:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg05 = data.createByteArray();
					long _arg14 = data.readLong();
					String _arg32 = data.readString();
					IYmodemSPI _arg41 = IYmodemSPI.Stub.asInterface(data.readStrongBinder());
					this.updateFirmware(_arg05, _arg14, _arg32, _arg41);
					reply.writeNoException();
					return true;
				case 2:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg04 = this.getFirmwareStatus();
					reply.writeNoException();
					reply.writeInt(_arg04);
					return true;
				case 3:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = this.getServiceVersion();
					reply.writeNoException();
					reply.writeString(_arg0);
					return true;
				case 4:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg06 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printerInit(_arg06);
					reply.writeNoException();
					return true;
				case 5:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg06 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printerSelfChecking(_arg06);
					reply.writeNoException();
					return true;
				case 6:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = this.getPrinterSerialNo();
					reply.writeNoException();
					reply.writeString(_arg0);
					return true;
				case 7:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = this.getPrinterVersion();
					reply.writeNoException();
					reply.writeString(_arg0);
					return true;
				case 8:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = this.getPrinterModal();
					reply.writeNoException();
					reply.writeString(_arg0);
					return true;
				case 9:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg06 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.getPrintedLength(_arg06);
					reply.writeNoException();
					return true;
				case 10:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg04 = data.readInt();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.lineWrap(_arg04, _arg1);
					reply.writeNoException();
					return true;
				case 11:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg05 = data.createByteArray();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.sendRAWData(_arg05, _arg1);
					reply.writeNoException();
					return true;
				case 12:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg04 = data.readInt();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.setAlignment(_arg04, _arg1);
					reply.writeNoException();
					return true;
				case 13:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.setFontName(_arg0, _arg1);
					reply.writeNoException();
					return true;
				case 14:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					float _arg03 = data.readFloat();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.setFontSize(_arg03, _arg1);
					reply.writeNoException();
					return true;
				case 15:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printText(_arg0, _arg1);
					reply.writeNoException();
					return true;
				case 16:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					String _arg13 = data.readString();
					float _arg22 = data.readFloat();
					_arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printTextWithFont(_arg0, _arg13, _arg22, _arg3);
					reply.writeNoException();
					return true;
				case 17:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					String[] _arg02 = data.createStringArray();
					int[] _arg12 = data.createIntArray();
					int[] _arg21 = data.createIntArray();
					_arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printColumnsText(_arg02, _arg12, _arg21, _arg3);
					reply.writeNoException();
					return true;
				case 18:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					Bitmap _arg01;
					if(data.readInt() != 0) {
						_arg01 = (Bitmap)Bitmap.CREATOR.createFromParcel(data);
					} else {
						_arg01 = null;
					}

					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printBitmap(_arg01, _arg1);
					reply.writeNoException();
					return true;
				case 19:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					_arg11 = data.readInt();
					_arg2 = data.readInt();
					int _arg31 = data.readInt();
					int _arg4 = data.readInt();
					ICallback _arg5 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printBarCode(_arg0, _arg11, _arg2, _arg31, _arg4, _arg5);
					reply.writeNoException();
					return true;
				case 20:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					_arg11 = data.readInt();
					_arg2 = data.readInt();
					_arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printQRCode(_arg0, _arg11, _arg2, _arg3);
					reply.writeNoException();
					return true;
				case 21:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IWoyouService");
					_arg0 = data.readString();
					_arg1 = ICallback.Stub.asInterface(data.readStrongBinder());
					this.printOriginalText(_arg0, _arg1);
					reply.writeNoException();
					return true;
				case 1598968902:
					reply.writeString("woyou.aidlservice.jiuiv5.IWoyouService");
					return true;
				default:
					return super.onTransact(code, data, reply, flags);
			}
		}

		private static class Proxy implements IWoyouService {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				this.mRemote = remote;
			}

			public IBinder asBinder() {
				return this.mRemote;
			}

			public String getInterfaceDescriptor() {
				return "woyou.aidlservice.jiuiv5.IWoyouService";
			}

			public void updateFirmware(byte[] buffer, long size, String filename, IYmodemSPI iapInterface) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeByteArray(buffer);
					_data.writeLong(size);
					_data.writeString(filename);
					_data.writeStrongBinder(iapInterface != null?iapInterface.asBinder():null);
					this.mRemote.transact(1, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public int getFirmwareStatus() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				int _result;
				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					this.mRemote.transact(2, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

				return _result;
			}

			public String getServiceVersion() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				String _result;
				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					this.mRemote.transact(3, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

				return _result;
			}

			public void printerInit(ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(4, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printerSelfChecking(ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(5, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public String getPrinterSerialNo() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				String _result;
				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					this.mRemote.transact(6, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

				return _result;
			}

			public String getPrinterVersion() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				String _result;
				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					this.mRemote.transact(7, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

				return _result;
			}

			public String getPrinterModal() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				String _result;
				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					this.mRemote.transact(8, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

				return _result;
			}

			public void getPrintedLength(ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(9, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void lineWrap(int n, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeInt(n);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(10, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void sendRAWData(byte[] data, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeByteArray(data);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(11, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void setAlignment(int alignment, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeInt(alignment);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(12, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void setFontName(String typeface, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(typeface);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(13, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void setFontSize(float fontsize, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeFloat(fontsize);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(14, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printText(String text, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(text);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(15, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printTextWithFont(String text, String typeface, float fontsize, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(text);
					_data.writeString(typeface);
					_data.writeFloat(fontsize);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(16, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeStringArray(colsTextArr);
					_data.writeIntArray(colsWidthArr);
					_data.writeIntArray(colsAlign);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(17, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printBitmap(Bitmap bitmap, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					if(bitmap != null) {
						_data.writeInt(1);
						bitmap.writeToParcel(_data, 0);
					} else {
						_data.writeInt(0);
					}

					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(18, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printBarCode(String data, int symbology, int height, int width, int textposition, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(data);
					_data.writeInt(symbology);
					_data.writeInt(height);
					_data.writeInt(width);
					_data.writeInt(textposition);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(19, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printQRCode(String data, int modulesize, int errorlevel, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(data);
					_data.writeInt(modulesize);
					_data.writeInt(errorlevel);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(20, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void printOriginalText(String text, ICallback callback) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
					_data.writeString(text);
					_data.writeStrongBinder(callback != null?callback.asBinder():null);
					this.mRemote.transact(21, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}
		}
	}
}
