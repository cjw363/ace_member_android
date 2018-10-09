package com.ace.member.main.home.transfer.to_merchant;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Balance;
import com.ace.member.bean.Merchant;
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


public class ToMerchantPresenter extends BasePresenter implements ToMerchantContract.Presenter {

	private final ToMerchantContract.View mView;

	private WithdrawConfig mWithdrawConfig;
	private JsonObject mExchangeFeeRateObj;
	private Merchant mMerchantInfo;

	private String mToken = "";
	private static final String mTokenAction = "TRANSFER_TO_MERCHANT";

	@Inject
	public ToMerchantPresenter(Context context, ToMerchantContract.View mView) {
		super(context);
		this.mView = mView;
	}

	public void start(final boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("cmd", "getToMerchantConfig");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (!isLoading) {
						Utils.showToast(R.string.success);
					}
					mToken = token;
					ToMerchantData toMerchantData = JsonUtil.jsonToBean(result, ToMerchantData.class);
					assert toMerchantData != null;
					boolean functionDepositRunning = toMerchantData.isFunctionToMerchantRunning();
					if (!functionDepositRunning){
						Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_116_MEMBER_TRANSFER_TO_MERCHANT));
					}
					mWithdrawConfig = toMerchantData.getWithdrawConfig();
					mExchangeFeeRateObj = toMerchantData.getExchangeFee();
					mMerchantInfo = toMerchantData.getMerchant();
					mView.setReceiver(mMerchantInfo.getName());
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

			Map<String, String> p = new HashMap<>();
			p.put("_a", "transfer");
			p.put("_b", "aj");
			p.put("_s", LibSession.sSid);
			p.put("cmd", "checkToMerchant");
			p.put("amount", mView.getAmount());
			p.put("fee", mView.getFee());
			p.put("currency", currency);
			submit(p, new IDataRequestListener() {
				@Override
				public void loadSuccess(String result, String uniqueToken) {
					PayHelper payHelper = new PayHelper((AppCompatActivity) mContext);
					payHelper.startPay(new PayHelper.CallBackAll() {
						@Override
						public void cancelPay() {
						}

						@Override
						public void paySuccess() {
							Map<String, String> p = new HashMap<>();
							p.put("_b", "aj");
							p.put("_s", Session.sSid);
							p.put("_a", "transfer");
							p.put("cmd", "saveToMerchant");
							p.put("amount", mView.getAmount());
							p.put("fee", mView.getFee());
							p.put("currency", currency);
							p.put("remark", mView.getRemark());
							p.put("unique_token", mToken);
							submit(p, new IDataRequestListener() {
								@Override
								public void loadSuccess(String result, String uniqueToken) {
									try {
										mToken = uniqueToken;
										SingleStringBean bean = JsonUtil.jsonToBean(result, SingleStringBean.class);
										assert bean != null;
										String time = bean.getValue();
										mView.showSuccess(time);
									} catch (Exception e) {
										FileUtils.addErrorLog(e);
									}
								}

								@Override
								public void loadFailure(int code, String result, String uniqueToken) {
									try {
										mToken = uniqueToken;
										if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
											Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_116_MEMBER_TRANSFER_TO_MERCHANT));
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

						@Override
						public void payFail() {
							Utils.showToast(R.string.fail);
						}
					});
				}

				@Override
				public void loadFailure(int errorCode, String result, String uniqueToken) {
					String msg = M.get(mContext, errorCode);
					if (TextUtils.isEmpty(msg)) msg = mContext.getResources().getString(R.string.fail);
					Utils.showToast(msg);
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
				feeAmount = mWithdrawConfig.getFeeAmount() * (Math.ceil(a / exchange / mWithdrawConfig.getFeeUnit())) * exchange;
				mView.setFee(feeAmount);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}
}
