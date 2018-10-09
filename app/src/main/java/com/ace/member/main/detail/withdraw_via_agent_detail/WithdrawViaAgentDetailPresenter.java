package com.ace.member.main.detail.withdraw_via_agent_detail;


import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.CashApplication;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;

import javax.inject.Inject;

class WithdrawViaAgentDetailPresenter extends BasePresenter implements WithdrawViaAgentDetailContract.WithdrawViaAgentDetailPresenter {

	private final WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView mView;

	@Inject
	WithdrawViaAgentDetailPresenter(Context context, WithdrawViaAgentDetailContract.WithdrawViaAgentDetailView view) {
		super(context);
		mView = view;
	}

	@Override
	public void getData(HashMap<String, String> params) {
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getWithdrawViaAgentDetail");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					CashApplication data = JsonUtil.jsonToBean(result, CashApplication.class);
					assert data != null;
					mView.setData(data);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}
}
