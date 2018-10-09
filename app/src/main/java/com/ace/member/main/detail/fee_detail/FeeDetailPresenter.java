package com.ace.member.main.detail.fee_detail;


import android.content.Context;

import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Fee;
import com.ace.member.utils.Session;
import com.ace.member.utils.TransactionType;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;

import java.util.HashMap;

import javax.inject.Inject;

class FeeDetailPresenter extends BasePresenter implements FeeDetailContract.FeeDetailPresenter {

	private final FeeDetailContract.FeeDetailView mView;

	@Inject
	FeeDetailPresenter(Context context, FeeDetailContract.FeeDetailView view) {
		super(context);
		mView = view;
	}

	@Override
	public void getWithdrawFeeData(HashMap<String, String> params, int mainSubType) {
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		switch (mainSubType) {
			case TransactionType.TRANSACTION_SUB_TYPE_203_WITHDRAW_VIA_BANK:
				params.put("_a", "balance");
				params.put("cmd", "getBankWithdrawFee");
				break;
			case TransactionType.TRANSACTION_SUB_TYPE_205_WITHDRAW_VIA_AGENT:
				params.put("_a", "transfer");
				params.put("cmd", "getCashWithdrawFee");
				break;
			default:
				return;
		}
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					Fee fee = JsonUtil.jsonToBean(result, Fee.class);
					assert fee != null;
					mView.setWithdrawFeeData(fee);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}

	@Override
	public void getBankFeeData(HashMap<String, String> params) {
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("_a", "balance");
		params.put("cmd", "getWithdrawBankFee");

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					Fee fee = JsonUtil.jsonToBean(result, Fee.class);
					assert fee != null;
					mView.setBankFeeData(fee);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}
		}, false);
	}
}
