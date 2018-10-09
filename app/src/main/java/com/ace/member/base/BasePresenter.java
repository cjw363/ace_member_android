package com.ace.member.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ace.member.BuildConfig;
import com.ace.member.R;
import com.ace.member.bean.SingleBooleanBean;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.listener.ICheckFunctionRunningListener;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.M;
import com.og.event.MessageEvent;
import com.og.http.IDataRequestListener;
import com.og.http.NetClient;
import com.og.http.SimpleRequestListener;
import com.og.utils.DialogFactory;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BasePresenter {

	public Context mContext;
	public NetClient mNetClient;

	public BasePresenter(Context context) {
		mContext = context;
		mNetClient = new NetClient(mContext, BuildConfig.SERVICE_URL);
	}

	public void submit(Map<String, String> params, IDataRequestListener listener) {
		submit(params, listener, true);
	}

	/**
	 * @param params      键值对 Map
	 * @param listener    请求监听
	 * @param showLoading 是否显示等待进度条
	 */
	public void submit(Map<String, String> params, IDataRequestListener listener, boolean showLoading) {
		mNetClient.requestServer(params, listener, showLoading);
	}

	public void getVersionInfo() {
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pif = pm.getPackageInfo(mContext.getPackageName(), 0);
			LibSession.sVersionCode = String.valueOf(pif.versionCode);
			LibSession.sVersionName = pif.versionName;
			if (TextUtils.isEmpty(LibSession.sServiceVersion)) {
				getVersion();
			}
		} catch (PackageManager.NameNotFoundException e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	protected void showFailError(String content, int msg) {
		DialogFactory.ToastDialog2(mContext, content, msg, R.layout.dialog_error_layout, R.color.dlg_error_msg);
	}

	//	protected void showErrorStr(String str,int msg){
	//		DialogFactory.ToastDialog(mContext,"",str,msg);
	//	}

	private void getVersion() {
		try {
			Map<String, String> p = new HashMap<>();
			p.put("_b", "aj");
			p.put("_a", "system");
			p.put("cmd", "getServiceInfo");
			p.put("version_name", Session.sVersionName);
			submit(p, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						MessageEvent me = new MessageEvent(M.MessageCode.ERR_442_GET_SERVICE_INFO_SUCCESS);
						Message mg = new Message();
						Bundle b = new Bundle();
						JSONObject object = new JSONObject(result);
						b.putString("version", object.optString("version"));
						b.putBoolean("supported", object.optBoolean("supported"));
						b.putInt("flag_dev", object.optInt("flag_dev"));
						mg.setData(b);
						me.setMsg(mg);
						EventBus.getDefault().post(me);
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}

	}

	/**
	 * 保存手势设置
	 */
	public void saveGesture(final String gesture, int actionType) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "setting");
		p.put("_b", "aj");
		p.put("cmd", "saveGesture");
		p.put("_s", LibSession.sSid);
		p.put("gesture", gesture);
		p.put("action_type", String.valueOf(actionType));
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					saveGestureSuccess(gesture);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				saveGestureFail();
			}
		});
	}

	protected void saveGestureSuccess(String gesture) {
	}

	private void saveGestureFail() {
		EventBus.getDefault().post(new MessageEvent(M.MessageCode.ERR_440_SAVE_GESTURE_FAIL));
	}

	public void getToken(String tokenAction, final IGetToken iGetToken) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "system");
		params.put("_b", "aj");
		params.put("cmd", "getToken");
		params.put("_s", LibSession.sSid);
		params.put("token_action", tokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					iGetToken.getTokenSuccess(token);
				} catch (Exception e) {
					iGetToken.getTokenFail();
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				iGetToken.getTokenFail();
			}

		}, false);

	}

	public interface IGetToken {
		void getTokenSuccess(String token);

		void getTokenFail();

		public static class SimpleGetToken implements IGetToken {

			@Override
			public void getTokenSuccess(String token) {

			}

			@Override
			public void getTokenFail() {

			}
		}
	}

	public void getTime() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "system");
		params.put("_b", "aj");
		params.put("cmd", "getTime");
		params.put("_s", LibSession.sSid);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
					assert bean != null;
					setTime(bean.getValue());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}

			}
		}, false);
	}

	public void setTime(String time) {
	}

	public void addFingerprintLog(int flag) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "setting");
		params.put("_b", "aj");
		params.put("cmd", "addFingerprintLog");
		params.put("flag", String.valueOf(flag));
		params.put("_s", LibSession.sSid);
		submit(params, new SimpleRequestListener() {

		}, false);
	}

}























