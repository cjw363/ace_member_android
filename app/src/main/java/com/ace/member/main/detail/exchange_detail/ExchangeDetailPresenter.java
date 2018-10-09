package com.ace.member.main.detail.exchange_detail;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BalanceRecord;
import com.ace.member.bean.CurrencyExchangeBean;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

final class ExchangeDetailPresenter extends BasePresenter implements ExchangeDetailContract.Presenter {

	private final ExchangeDetailContract.View mView;

	@Inject
	ExchangeDetailPresenter(Context context, ExchangeDetailContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void getData(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getExchangeDetail");
		params.put("id", String.valueOf(id));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					CurrencyExchangeBean data = JsonUtil.jsonToBean(result, CurrencyExchangeBean.class);
					mView.setData(data);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(R.string.fail);
			}
		}, false);
	}
}
