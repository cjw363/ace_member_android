package com.ace.member.main.me.exchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ExchangePresenter extends BasePresenter implements ExchangeContract.ExchangeContractPresenter {

	private final ExchangeContract.ExchangeContractView mView;
	private String mUniqueToken = "";

	@Inject
	public ExchangePresenter(Context context, ExchangeContract.ExchangeContractView mView) {
		super(context);
		this.mView = mView;
	}

	public void getExchangeRate(final boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getExchangeRate");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					ExchangeInfoBean exchangeInfoBean = JsonUtil.jsonToBean(result, ExchangeInfoBean.class);
					mView.setExchangeRate(exchangeInfoBean);
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				if (!isLoading) {
					Utils.showToast(R.string.fail);
				}
			}
		}, isLoading);

	}

	public void getBalanceInfo(final boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getExchangeInfo");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					ExchangeBean list = JsonUtil.jsonToBean(result, ExchangeBean.class);
					assert list != null;
					Session.balanceList = list.getBalanceList();
					Session.currencyList = list.getExchangeList();
					mView.updateBalance(!isLoading, list.isRunning());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mUniqueToken = token;
				Utils.showToast(R.string.fail);
			}
		}, false);
	}

	public void submitExchange(final Map<String, String> params) {
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				PayHelper payHelper = new PayHelper((ExchangeActivity) mContext);
				payHelper.startPay(new PayHelper.CallBackPart() {
					@Override
					public void paySuccess() {
						params.put("unique_token", mUniqueToken);
						params.put("cmd", "exchange");
						submit(params, new SimpleRequestListener() {
							@Override
							public void loadSuccess(String result, String token) {
								try {
									mUniqueToken = token;
									ExchangeValue exchangeValue = JsonUtil.jsonToBean(result, ExchangeValue.class);
									assert exchangeValue != null;
									Session.balanceList = exchangeValue.getBalanceList();
									ExchangeResultBead resultBead = exchangeValue.getDetail();
									if (Session.balanceList.size() == 0) {
										Session.balanceList = null;
									}
									mView.clearExchangeInfo();
									mView.updateBalance(false, true);
									Intent intent = new Intent(mContext, ExchangeResultActivity.class);
									Bundle b = new Bundle();
									b.putSerializable("value", resultBead);
									intent.putExtras(b);
									mContext.startActivity(intent);

								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void loadFailure(int code, String result, String token) {
								mUniqueToken = token;
								if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
									mView.setSubmitEnables(false);
									Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_119_MEMBER_EXCHANGE), Snackbar.LENGTH_LONG);
								} else {
									Utils.showToast(M.get(mContext, code));
								}
							}
						}, false);
					}
				});
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
					mView.setSubmitEnables(false);
					Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_119_MEMBER_EXCHANGE), Snackbar.LENGTH_LONG);
				} else {
					Utils.showToast(M.get(mContext, code));
				}
			}
		}, false);
	}

}
