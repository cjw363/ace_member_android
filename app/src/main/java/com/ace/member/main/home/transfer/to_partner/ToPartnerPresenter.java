package com.ace.member.main.home.transfer.to_partner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Partner;
import com.ace.member.bean.SingleStringBean;
import com.ace.member.bean.WithdrawConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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


public class ToPartnerPresenter extends BasePresenter implements ToPartnerContract.Presenter {

	private final ToPartnerContract.View mView;

	private WithdrawConfig mWithdrawConfig;
	private JsonObject mExchangeFeeRateObj;
	private Partner mPartnerInfo;

	private String mToken = "";
	private static final String mTokenAction = "TRANSFER_TO_PARTNER";

	@Inject
	public ToPartnerPresenter(Context context, ToPartnerContract.View mView) {
		super(context);
		this.mView = mView;
	}

	public void start(final boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("cmd", "getToPartnerConfig");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (!isLoading) {
						Utils.showToast(R.string.success);
					}
					mToken = token;
					ToPartnerData toPartnerData = JsonUtil.jsonToBean(result, ToPartnerData.class);
					assert toPartnerData != null;
					boolean functionDepositRunning = toPartnerData.isFunctionToPartnerRunning();
					if (!functionDepositRunning)
						Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_115_MEMBER_TRANSFER_TO_PARTNER));
					mWithdrawConfig = toPartnerData.getWithdrawConfig();
					mExchangeFeeRateObj = toPartnerData.getExchangeFee();
					mPartnerInfo = toPartnerData.getPartner();
					mView.setReceiver(mPartnerInfo.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	public void submit(final String currency) {
		try {
			if (TextUtils.isEmpty(currency)) {
				Utils.showToast(R.string.please_select_currency);
				return;
			}
			final double a = TextUtils.isEmpty(mView.getAmount()) ? 0 : Double.parseDouble(mView.getAmount().replace(",", "").trim());
			final double b = TextUtils.isEmpty(mView.getBalance()) ? 0 : Double.parseDouble(mView.getBalance().replace(",", "").trim());
			if (a <= 0) {
				Utils.showToast(R.string.invalid_amount);
				return;
			}
			if (a > b) {
				Utils.showToast(R.string.msg_1709);
				return;
			}

			Map<String, String> params = new HashMap<>();
			params.put("_a", "transfer");
			params.put("_b", "aj");
			params.put("_s", Session.sSid);
			params.put("cmd", "checkToPartner");
			params.put("amount", mView.getAmount());
			params.put("fee", mView.getFee());
			params.put("currency", currency);
			submit(params, new IDataRequestListener() {
				@Override
				public void loadSuccess(String result, String uniqueToken) {
					try {
						PayHelper payHelper = new PayHelper((AppCompatActivity) mContext);
						payHelper.startPay(new PayHelper.CallBackPart() {

							@Override
							public void paySuccess() {
								final Map<String, String> params = new HashMap<>();
								params.put("_b", "aj");
								params.put("_s", Session.sSid);
								params.put("_a", "transfer");
								params.put("cmd", "saveToPartner");
								params.put("amount", mView.getAmount());
								params.put("fee", mView.getFee());
								params.put("currency", currency);
								params.put("remark", mView.getRemark());
								params.put("unique_token", mToken);
								submit(params, new SimpleRequestListener() {
									@Override
									public void loadSuccess(String result, String token) {
										try {
											mToken = token;
											SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
											assert bean != null;
											String time = bean.getValue();
											mView.showSuccess(time);
										} catch (Exception e) {
											FileUtils.addErrorLog(e);
											e.printStackTrace();
										}
									}

									@Override
									public void loadFailure(int code, String result, String token) {
										try {
											mToken = token;
											if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
												Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_115_MEMBER_TRANSFER_TO_PARTNER));
												mView.setSubmitEnables(false);
											} else {
												String msg = M.get(mContext, code);
												if (TextUtils.isEmpty(msg)) msg = mContext.getResources().getString(R.string.fail);
												Utils.showToast(msg);
											}
										} catch (Exception e) {
											FileUtils.addErrorLog(e);
										}

									}
								});
							}

						});
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}

				}

				@Override
				public void loadFailure(int errorCode, String result, String uniqueToken) {
					Utils.showToast(M.get(mContext, errorCode));
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public void getBalance() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "balance");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getBalance");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					Session.balanceList = JsonUtil.jsonToList(result, new TypeToken<List<Balance>>() {});
					mToken = token;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(M.get(mContext, code));
			}
		}, false);
	}

	public void updateFeeInfo() {
		try {
			String amount = mView.getAmount();
			if (TextUtils.isEmpty(amount)) {
				mView.setFee(0.00);
				return;
			}
			double a = Double.parseDouble(amount.replace(",", ""));
			if (a == 0) {
				mView.setFee(0.00);
			} else {
				if (mWithdrawConfig.getFeeUnit() == 0 || mWithdrawConfig.getFeeAmount() == 0) {
					mView.setFee(0.00);
					return;
				}
				double feeAmount = 0.00;
				double exchange = mExchangeFeeRateObj.get(mView.getCurrency()).getAsDouble();
				feeAmount = mWithdrawConfig.getFeeAmount() * (Math.ceil(a / exchange / mWithdrawConfig
					.getFeeUnit())) * exchange;
				mView.setFee(feeAmount);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}
