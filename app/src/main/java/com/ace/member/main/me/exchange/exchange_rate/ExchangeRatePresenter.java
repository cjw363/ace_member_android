package com.ace.member.main.me.exchange.exchange_rate;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ExchangeRatePresenter extends BasePresenter implements ExchangeRateContract.ExchangeRateContractPresenter {

	private final ExchangeRateContract.ExchangeRateContractView mView;

	@Inject
	public ExchangeRatePresenter(Context context, ExchangeRateContract.ExchangeRateContractView mView) {
		super(context);
		this.mView = mView;
	}

	public void getExchangeRate(final boolean isRefresh) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getExchangeRate");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (isRefresh) {
						Utils.showToast(R.string.success);
					}
					JSONObject object = new JSONObject(result);
					mView.setExchangeRate(object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, !isRefresh);
	}

}
