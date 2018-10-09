package com.ace.member.setting_gesture;

import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SingleBooleanBean;
import com.ace.member.utils.M;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SettingGesturePresenter extends BasePresenter implements SettingGestureContract.SettingGesturePresenter {

	private final SettingGestureContract.SettingGestureView mSettingGestureView;

	@Inject
	public SettingGesturePresenter(SettingGestureContract.SettingGestureView view, Context context) {
		super(context);
		mSettingGestureView = view;
	}


	public void checkPassword(String pwd) {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "user");
		p.put("_b", "aj");
		p.put("cmd", "confirmPassword");
		p.put("_s", LibSession.sSid);
		p.put("pwd", pwd);
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mSettingGestureView.gotoLockSetupActivity();
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String uniqueToken) {
				showFailError(M.get(mContext, code), 0);
			}
		});
	}


	protected void saveGestureSuccess(String gesture) {
		mSettingGestureView.saveGestureSuccess(gesture);
	}

	@Override
	public void getGestureConfig() {
		Map<String, String> p = new HashMap<>();
		p.put("_a", "setting");
		p.put("_b", "aj");
		p.put("cmd", "getGestureConfig");
		p.put("_s", LibSession.sSid);
		submit(p, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					SingleBooleanBean bean = JsonUtil.jsonToBean(result, SingleBooleanBean.class);
					assert bean != null;
					mSettingGestureView.setStatusType(bean.getValue());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}
}
