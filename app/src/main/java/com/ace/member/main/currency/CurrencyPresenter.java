package com.ace.member.main.currency;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Balance;
import com.ace.member.bean.BalanceRecordDataWrapper;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class CurrencyPresenter extends BasePresenter implements CurrencyContract.Presenter {
	private CurrencyContract.View mView;

	@Inject
	public CurrencyPresenter(CurrencyContract.View view, Context context) {
		super(context);
		this.mView = view;
	}


	@Override
	public void start(String currency, boolean isRefresh) {
		getBalanceRecord(currency, isRefresh);
	}

	@Override
	public void getBalanceRecord(String currency, final boolean isRefresh) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBalanceRecordList");
		params.put("currency", currency);

		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (isRefresh) {
						mView.showRefreshStatus(false);
						mView.showRefreshResult(Utils.getString(R.string.success));
					}
					BalanceRecordDataWrapper balanceRecordDataWrapper = JsonUtil.jsonToBean(result, BalanceRecordDataWrapper.class);
					assert balanceRecordDataWrapper != null;
					Balance balance = balanceRecordDataWrapper.getBalance();
					Session.updateBalance(balance);
					mView.setBalance(String.valueOf(balance.getAmount()));
					mView.setRecordList(balanceRecordDataWrapper.getRecordList());
					mView.enableDeposit(balanceRecordDataWrapper.isFunctionDeposit());
					mView.enableWithdraw(balanceRecordDataWrapper.isFunctionWithdraw());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
					mView.enableDeposit(false);
					mView.enableWithdraw(false);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				if (isRefresh) {
					mView.showRefreshStatus(false);
					mView.showRefreshResult(Utils.getString(R.string.fail));
				}
				mView.enableDeposit(false);
				mView.enableWithdraw(false);
			}
		}, false);
	}

	@Override
	public void deposit() {
		mView.showDeposit();
	}

	@Override
	public void withdraw(String balance) {
		if (Double.parseDouble(balance.replace(",", "")) <= 0)
			mView.showMsg(Utils.getString(R.string.no_balance));
		else mView.showWithDraw();
	}
}
