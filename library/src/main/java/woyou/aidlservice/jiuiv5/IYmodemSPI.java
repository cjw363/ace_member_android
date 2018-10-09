
package woyou.aidlservice.jiuiv5;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IYmodemSPI extends IInterface {
	void sendPercent(float var1) throws RemoteException;

	void sendFinish(int var1) throws RemoteException;

	void onFinishYmodemDownload(boolean var1, String var2) throws RemoteException;

	public abstract static class Stub extends Binder implements IYmodemSPI {
		private static final String DESCRIPTOR = "woyou.aidlservice.jiuiv5.IYmodemSPI";
		static final int TRANSACTION_sendPercent = 1;
		static final int TRANSACTION_sendFinish = 2;
		static final int TRANSACTION_onFinishYmodemDownload = 3;

		public Stub() {
			this.attachInterface(this, "woyou.aidlservice.jiuiv5.IYmodemSPI");
		}

		public static IYmodemSPI asInterface(IBinder obj) {
			if(obj == null) {
				return null;
			} else {
				IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.IYmodemSPI");
				return (IYmodemSPI)(iin != null && iin instanceof IYmodemSPI?(IYmodemSPI)iin:new Proxy(obj));
			}
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			switch(code) {
				case 1:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IYmodemSPI");
					float _arg02 = data.readFloat();
					this.sendPercent(_arg02);
					reply.writeNoException();
					return true;
				case 2:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IYmodemSPI");
					int _arg01 = data.readInt();
					this.sendFinish(_arg01);
					reply.writeNoException();
					return true;
				case 3:
					data.enforceInterface("woyou.aidlservice.jiuiv5.IYmodemSPI");
					boolean _arg0 = data.readInt() != 0;
					String _arg1 = data.readString();
					this.onFinishYmodemDownload(_arg0, _arg1);
					reply.writeNoException();
					return true;
				case 1598968902:
					reply.writeString("woyou.aidlservice.jiuiv5.IYmodemSPI");
					return true;
				default:
					return super.onTransact(code, data, reply, flags);
			}
		}

		private static class Proxy implements IYmodemSPI {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				this.mRemote = remote;
			}

			public IBinder asBinder() {
				return this.mRemote;
			}

			public String getInterfaceDescriptor() {
				return "woyou.aidlservice.jiuiv5.IYmodemSPI";
			}

			public void sendPercent(float percent) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IYmodemSPI");
					_data.writeFloat(percent);
					this.mRemote.transact(1, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void sendFinish(int count) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IYmodemSPI");
					_data.writeInt(count);
					this.mRemote.transact(2, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void onFinishYmodemDownload(boolean flag, String msg) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IYmodemSPI");
					_data.writeInt(flag?1:0);
					_data.writeString(msg);
					this.mRemote.transact(3, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}
		}
	}
}
