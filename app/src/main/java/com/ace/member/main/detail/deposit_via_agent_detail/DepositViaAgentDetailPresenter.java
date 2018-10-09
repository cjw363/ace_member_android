package com.ace.member.main.detail.deposit_via_agent_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.CashApplication;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

class DepositViaAgentDetailPresenter extends BasePresenter implements DepositViaAgentDetailContract.MemberDepositDetailPresenter {

	private final DepositViaAgentDetailContract.MemberDepositDetailView mView;

	@Inject
	DepositViaAgentDetailPresenter(Context context, DepositViaAgentDetailContract.MemberDepositDetailView view) {
		super(context);
		mView = view;
	}

	void getRecentDetail(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getDepositViaAgentDetail");
		params.put("id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					CashApplication recent = JsonUtil.jsonToBean(result, CashApplication.class);
					mView.setRecentDetail(recent);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}
}
