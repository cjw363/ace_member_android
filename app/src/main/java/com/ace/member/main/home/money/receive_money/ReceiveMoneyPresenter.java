package com.ace.member.main.home.money.receive_money;


import android.content.Context;
import android.text.TextUtils;

import com.ace.member.base.BasePresenter;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.SPUtil;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.ace.member.main.home.money.BaseCashActivity.TRANSFER_11_MEMBER_RECEIVE_MONEY;

public class ReceiveMoneyPresenter extends BasePresenter implements ReceiveMoneyContract.Presenter {

	private final ReceiveMoneyContract.View mView;
	private String mCurrency;
	private String mCode;
	private String mAmount;

	@Inject
	public ReceiveMoneyPresenter(Context context, ReceiveMoneyContract.View view) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		mCurrency = SPUtil.getString("last_receive_money_currency", AppGlobal.USD);
		if (mCurrency.equals(AppGlobal.USD)) {
			mView.setCheckRbUsd();
		} else if (mCurrency.equals(AppGlobal.KHR)) {
			mView.setCheckRbKhr();
		} else if (mCurrency.equals(AppGlobal.VND)) {
			mView.setCheckRbVnd();
		} else if (mCurrency.equals(AppGlobal.THB)) {
			mView.setCheckRbThb();
		}

	}

	@Override
	public void requestResult() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getReceiveMoneyResult");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					ReceiveMoneyResult receiveMoneyResult = JsonUtil.jsonToBean(result, ReceiveMoneyResult.class);
					if (receiveMoneyResult != null) {
						mView.showReceiveMoney(receiveMoneyResult);
					}
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}
		}, false);
	}

	@Override
	public void onSelectCurrency(String currency, String amount) {
		try {
			mCurrency = currency;
			mAmount = amount;
			SPUtil.putString("last_receive_money_currency", currency);
			if (TextUtils.isEmpty(mAmount) || Utils.strToDouble(amount) == 0) {
				mCode = String.format("%s|%s|%s|%s", TRANSFER_11_MEMBER_RECEIVE_MONEY, Session.user.getPhone(), Session.user.getId(), mCurrency);
				mView.setCurrencyAmount(mCurrency);
			} else {
				mCode = String.format("%s|%s|%s|%s|%s", TRANSFER_11_MEMBER_RECEIVE_MONEY, Session.user.getPhone(), Session.user.getId(), mCurrency, mAmount);
				mView.setCurrencyAmount(String.format("%s %s", Utils.format(mAmount, mCurrency), mCurrency));
			}
			mView.initCode(mCode);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}

	}
}
