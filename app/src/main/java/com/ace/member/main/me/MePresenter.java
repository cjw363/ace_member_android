package com.ace.member.main.me;

import android.content.Context;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class MePresenter extends BasePresenter implements MeContract.MePresenter {

	private final MeContract.MeView mMeView;

	@Inject
	MePresenter(MeContract.MeView view, Context context) {
		super(context);
		mMeView = view;
	}

	@Override
	public void logout() {
		if (checkData()) {
			Map<String, String> p = new HashMap<>();
			p.put("_a", "user");
			p.put("_b", "aj");
			p.put("cmd", "logout");
			p.put("_s", LibSession.sSid);
			p.put("id", String.valueOf(Session.user.getId()));
			submit(p, new SimpleRequestListener() {
				@Override
				public void loadSuccess(String result, String token) {
					try {
						mMeView.toLogout();
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
						e.printStackTrace();
					}
				}
			});
		} else {
			mMeView.toLogout();
		}
	}

	private boolean checkData() {
		return !(mContext != null && !Utils.isNetworkAvailable());
	}

	@Override
	public void getBalanceAndStatus(final boolean isRefresh) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBalanceAndStatus");

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				if (isRefresh) {
					mMeView.showRefreshStatus(false);
					mMeView.showRefreshResult(Utils.getString(R.string.success));
				}
				try {
					MeData meData = JsonUtil.jsonToBean(result, MeData.class);
					assert meData != null;
					Session.balanceList = meData.getBalance();
					Session.user.setPortrait(meData.getPortrait());
					mMeView.updatePortrait();
					if (Session.balanceList.size() == 0) {
						Session.balanceList = null;
					}
					mMeView.showBalance();

					if (Session.isPhoneVerified != meData.isPhoneVerified()){
						mMeView.updatePhoneIcon(meData.isPhoneVerified());
						Session.isPhoneVerified = meData.isPhoneVerified();
					}
					if (Session.isIdVerified != meData.isIdVerified()){
						mMeView.updateCertificateIcon(meData.isIdVerified());
						Session.isIdVerified = meData.isIdVerified();
					}
					if (Session.isFingerprintVerified != meData.isFingerprintVerified()){
						mMeView.updateFingerPrintIcon(meData.isFingerprintVerified());
						Session.isFingerprintVerified = meData.isFingerprintVerified();
					}
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				if (isRefresh) {
					mMeView.showRefreshStatus(false);
					mMeView.showRefreshResult(Utils.getString(R.string.fail));
				}
			}
		}, false);
	}
}
