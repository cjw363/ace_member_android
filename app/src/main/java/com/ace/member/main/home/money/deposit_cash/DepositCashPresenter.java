package com.ace.member.main.home.money.deposit_cash;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ace.member.base.BasePresenter;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DepositCashPresenter extends BasePresenter implements DepositCashContract.DepositCashPresenter {

	private final DepositCashContract.DepositCashView mView;
	private String mTime = "";

	@Inject
	public DepositCashPresenter(Context context, DepositCashContract.DepositCashView mView) {
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
		params.put("cmd", "getDepositCashResult");
		params.put("time", mTime);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					DepositCashResult depositCashResult = JsonUtil.jsonToBean(result, DepositCashResult.class);
					assert  depositCashResult != null;
					boolean notData = depositCashResult.isNotData();
					if (!notData) {
						mTime = depositCashResult.getTime();

						Intent intent = new Intent(mContext, DepositCashResultActivity.class);
						Bundle b = new Bundle();
						b.putSerializable("result", depositCashResult);
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
