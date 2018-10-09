package com.ace.member.main.home.money.withdraw_cash;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ace.member.base.BasePresenter;
import com.ace.member.main.home.money.withdraw_cash.confirm.WithdrawCashConfirmActivity;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class WithdrawCashPresenter extends BasePresenter implements WithdrawCashContract.WithdrawCashPresenter {

	private final WithdrawCashContract.WithdrawCashView mView;
	private String mTime = "";

	@Inject
	public WithdrawCashPresenter(Context context, WithdrawCashContract.WithdrawCashView mView) {
		super(context);
		this.mView = mView;
	}

	public void setTime(String time) {
		mTime = time;
	}

	public void requestResult() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getWithdrawCashResult");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					WithdrawCashResult withdrawCashResult = JsonUtil.jsonToBean(result, WithdrawCashResult.class);
					assert  withdrawCashResult != null;
					boolean notData = withdrawCashResult.isNotData();
					if (!notData) {
						Intent intent = new Intent(mContext, WithdrawCashConfirmActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("result", withdrawCashResult.getData());
						intent.putExtras(b);
						mContext.startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

}
