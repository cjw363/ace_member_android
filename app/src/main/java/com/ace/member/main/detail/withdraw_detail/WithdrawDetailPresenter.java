package com.ace.member.main.detail.withdraw_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BalanceRecord;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class WithdrawDetailPresenter extends BasePresenter implements WithdrawDetailContract.Presenter {

	private final WithdrawDetailContract.View mView;

	@Inject
	public WithdrawDetailPresenter(Context context, WithdrawDetailContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void getBalanceRecord(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBalanceRecord");
		params.put("id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					BalanceRecord record = JsonUtil.jsonToBean(result, BalanceRecord.class);
					mView.setBalanceRecord(record);
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
