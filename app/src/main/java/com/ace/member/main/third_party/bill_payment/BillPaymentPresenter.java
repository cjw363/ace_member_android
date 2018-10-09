package com.ace.member.main.third_party.bill_payment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BillerConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.PayHelper;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class BillPaymentPresenter extends BasePresenter implements BillPaymentContract.presenter {

	private final BillPaymentContract.view mView;

	@Inject
	public BillPaymentPresenter(BillPaymentContract.view view, Context context) {
		super(context);
		mView = view;
	}

	@Override
	public void getBillerConfig(int billerID) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "payment");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBillerConfig");
		params.put("biller_id", String.valueOf(billerID));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					BillPaymentData data = JsonUtil.jsonToBean(result, BillPaymentData.class);
					assert data != null;
					mView.initBillerConfig(data.getList(), data.getBiller(), data.getRunning());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}

	@Override
	public void updateFeeInfo(List<BillerConfig> list) {
		try {
			if (list == null || list.size() == 0) {
				mView.showCalculateFeeArea(null, 0);
				return;
			}
			double exchange = 0;
			String amountStr = mView.getAmount().replace(",", "");
			double amount = TextUtils.isEmpty(amountStr) ? 0 : Double.valueOf(amountStr);
			if (amount <= 0) {
				mView.setFee(0.00, AppGlobal.USD);
				mView.showCalculateFeeArea(null, 0);
				return;
			}
			BillerConfig config1 = null;
			for (BillerConfig config : list) {
				exchange = AppUtils.getExchangeRate(config.getCurrency(), mView.getCurrency());
				if (amount <= config.getAmount() * exchange) {
					config1 = config;
					break;
				}
			}
			if (config1 == null) {
				config1 = list.get(list.size() - 1);
			}
			mView.showCalculateFeeArea(config1, amount);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void submit(final Map<String, String> params) {
		submit(params, new IDataRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				PayHelper payHelper = new PayHelper((AppCompatActivity) mContext);
				payHelper.startPay(new PayHelper.CallBackPart() {
					@Override
					public void paySuccess() {
						params.put("cmd", "saveBillPayment");
						submit(params, new IDataRequestListener() {
							@Override
							public void loadSuccess(String result, String uniqueToken) {
								mView.initToken(uniqueToken);
								mView.saveSuccess();
							}

							@Override
							public void loadFailure(int errorCode, String result, String uniqueToken) {
								mView.initToken(uniqueToken);
								BillPaymentData data = JsonUtil.jsonToBean(result, BillPaymentData.class);
								assert data != null;
								mView.saveFail(errorCode, data.getList(), data.getBiller());
							}
						});
					}

					@Override
					public void payFail() {
						Utils.showToast(R.string.fail);
					}
				});
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				BillPaymentData data = JsonUtil.jsonToBean(result, BillPaymentData.class);
				assert data != null;
				mView.saveFail(errorCode, data.getList(), data.getBiller());
			}
		});
	}
}
