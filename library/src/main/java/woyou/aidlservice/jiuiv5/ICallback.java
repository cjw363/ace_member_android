
package woyou.aidlservice.jiuiv5;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICallback extends IInterface {
	void onRunResult(boolean var1) throws RemoteException;

	void onReturnString(String var1) throws RemoteException;

	void onRaiseException(int var1, String var2) throws RemoteException;

	public abstract static class Stub extends Binder implements ICallback {
		private static final String DESCRIPTOR = "woyou.aidlservice.jiuiv5.ICallback";
		static final int TRANSACTION_onRunResult = 1;
		static final int TRANSACTION_onReturnString = 2;
		static final int TRANSACTION_onRaiseException = 3;

		public Stub() {
			this.attachInterface(this, "woyou.aidlservice.jiuiv5.ICallback");
		}

		public static ICallback asInterface(IBinder obj) {
			if(obj == null) {
				return null;
			} else {
				IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.ICallback");
				return (ICallback)(iin != null && iin instanceof ICallback?(ICallback)iin:new Proxy(obj));
			}
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			switch(code) {
				case 1:
					data.enforceInterface("woyou.aidlservice.jiuiv5.ICallback");
					boolean _arg02 = data.readInt() != 0;
					this.onRunResult(_arg02);
					reply.writeNoException();
					return true;
				case 2:
					data.enforceInterface("woyou.aidlservice.jiuiv5.ICallback");
					String _arg01 = data.readString();
					this.onReturnString(_arg01);
					reply.writeNoException();
					return true;
				case 3:
					data.enforceInterface("woyou.aidlservice.jiuiv5.ICallback");
					int _arg0 = data.readInt();
					String _arg1 = data.readString();
					this.onRaiseException(_arg0, _arg1);
					reply.writeNoException();
					return true;
				case 1598968902:
					reply.writeString("woyou.aidlservice.jiuiv5.ICallback");
					return true;
				default:
					return super.onTransact(code, data, reply, flags);
			}
		}

		private static class Proxy implements ICallback {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				this.mRemote = remote;
			}

			public IBinder asBinder() {
				return this.mRemote;
			}

			public String getInterfaceDescriptor() {
				return "woyou.aidlservice.jiuiv5.ICallback";
			}

			public void onRunResult(boolean isSuccess) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
					_data.writeInt(isSuccess?1:0);
					this.mRemote.transact(1, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void onReturnString(String result) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
					_data.writeString(result);
					this.mRemote.transact(2, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}

			}

			public void onRaiseException(int code, String msg) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();

				try {
					_data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
					_data.writeInt(code);
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
