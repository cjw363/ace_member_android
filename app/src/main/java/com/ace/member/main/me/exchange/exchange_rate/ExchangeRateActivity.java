package com.ace.member.main.me.exchange.exchange_rate;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;

public class ExchangeRateActivity extends BaseActivity implements ExchangeRateContract.ExchangeRateContractView, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	ExchangeRatePresenter mExchangeRatePresenter;

	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.ll_exchange_rate_data)
	LinearLayout mLlExchangeRateData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerExchangeRateComponent.builder().exchangeRatePresenterModule(new ExchangeRatePresenterModule(this, this)).build().inject(this);

		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_exchange_rate;
	}

	public void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.exchange_rate).build();
		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);
		mExchangeRatePresenter.getExchangeRate(false);
	}

	public void setExchangeRate(JSONObject rate) {
		try {
			mRefreshLayout.setRefreshing(false);
			mLlExchangeRateData.removeAllViews();
			JSONArray rateArr = rate.getJSONArray("exchange_rate_show");
			if (rateArr == null) return;
			for (int i = 0; i < rateArr.length(); i++) {
				JSONObject obj = rateArr.getJSONObject(i);
				View viewExchangeRate = getLayoutInflater().inflate(R.layout.view_exchange_rate, null);
				TextView currency = (TextView) viewExchangeRate.findViewById(R.id.tv_exchange_currency);
				TextView sellPrice = (TextView) viewExchangeRate.findViewById(R.id.tv_sell_price);
				TextView buyPrice = (TextView) viewExchangeRate.findViewById(R.id.tv_buy_price);
				currency.setText(obj.optString("exchange_currency"));
				sellPrice.setText(Utils.format(obj.optString("sell"), 4));
				buyPrice.setText(Utils.format(obj.optString("buy"), 4));
				mLlExchangeRateData.addView(viewExchangeRate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mExchangeRatePresenter.getExchangeRate(true);
	}

	public void finishActivity() {
		finish();
	}

}
