package com.ace.member.main.home.transfer.to_non_member;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.Balance;
import com.ace.member.bean.TransferCashConfig;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.google.gson.reflect.TypeToken;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.http.SimpleRequestListener;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ToNonMemberPresenter extends BasePresenter implements ToNonMemberContract.Presenter {

	private final ToNonMemberContract.View mView;

	private List<TransferCashConfigList> mConfigList;
	private List<TransferCashConfig> mConfig;

	private static final String mTokenAction = "TRANSFER_TO_NONMEMBER";
	private String mUniqueToken = "";

	@Inject
	public ToNonMemberPresenter(Context context, ToNonMemberContract.View mView) {
		super(context);
		this.mView = mView;
	}

	public void start(final boolean isLoading) {
		Map<String, String> params = new HashMap<>();
		params.put("_s", Session.sSid);
		params.put("_a", "transfer");
		params.put("_b", "aj");
		params.put("cmd", "getToNonMemberConfig");
		params.put("token_action", mTokenAction);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					if (!isLoading) {
						Utils.showToast(R.string.success);
					}
					mUniqueToken = token;
					ToNonMemberData toNonMemberData = JsonUtil.jsonToBean(result, ToNonMemberData.class);
					assert toNonMemberData != null;
					boolean functionDepositRunning = toNonMemberData.isFunctionToNonMemberRunning();
					if (!functionDepositRunning) Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_114_MEMBER_TRANSFER_TO_NO_MEMBER));
					mConfigList = toNonMemberData.getConfigList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, false);
	}

	private List<TransferCashConfig> getConfList(String currency){
		List<TransferCashConfig> config=null;
		try {
			for(TransferCashConfigList list:mConfigList){
				if(list.getCurrency().equals(currency)){
					config=list.getList();
					break;
				}
			}
		}catch (Exception e){
			FileUtils.addErrorLog(e);
		}
		return config;
	}

	public void updateFeeInfo() {
		try {
			mConfig=getConfList(mView.getCurrency());
			if (mConfig == null||mConfig.size()==0) return;
			String amountStr = mView.getAmount();
			double amount = TextUtils.isEmpty(amountStr) ? 0 : Double.valueOf(amountStr);
			if (amount <= 0) {
				mView.setFee(0.00);
				return;
			}
			int multiple = 0;
			TransferCashConfig transferCashConfig = mConfig.get(mConfig.size() - 1);
			double maxSplitAmount = transferCashConfig.getSplitAmount();
			double maxFeeAmount = transferCashConfig.getFeeAmount();
			double feeAmount = 0;

			if (amount > maxSplitAmount) {
				multiple = (int) Math.floor(amount / (maxSplitAmount));
				amount = amount - (maxSplitAmount * multiple);
			}

			int size = mConfig.size();
			for (int i = 0; i < size; i++) {
				TransferCashConfig configTemp = mConfig.get(i);

				if (amount != 0 && amount <= configTemp.getSplitAmount()) {
					feeAmount = configTemp.getFeeAmount();
					break;
				}
			}

			feeAmount = maxFeeAmount * multiple + feeAmount;
			if (feeAmount > 0) mView.setFee(feeAmount);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
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
					mUniqueToken = token;
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

	public void submit(final String countryCode, final String targetPhone, final String remark) {
		try {
			final String amount = mView.getAmount();
			final String balance = mView.getBalance();
			final String fee = mView.getFee();
			final String currency = mView.getCurrency();
			if ("".equals(targetPhone)) {
				Utils.showToast(R.string.please_input_phone);
				return;
			}
			if (TextUtils.isEmpty(mView.getCurrency())) {
				Utils.showToast(R.string.please_select_currency);
				return;
			}
			double a = TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount.replace(",", "").trim());
			double b = TextUtils.isEmpty(balance) ? 0 : Double.parseDouble(balance.replace(",", "").trim());
			double f = TextUtils.isEmpty(fee) ? 0 : Double.parseDouble(fee.replace(",", "").trim());
			if (a <= 0) {
				Utils.showToast(R.string.invalid_amount);
				return;
			}
			if (a + f > b) {
				Utils.showToast(R.string.msg_1709);
				return;
			}
			if (mConfig == null || mConfig.size()==0) {
				Utils.showToast(R.string.config_error);
				return;
			}

			Map<String, String> params = new HashMap<>();
			params.put("_a", "transfer");
			params.put("_b", "aj");
			params.put("_s", Session.sSid);
			params.put("cmd", "checkToNonMember");
			params.put("amount", amount);
			params.put("fee", fee);
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
								params.put("cmd", "saveToNonMember");
								params.put("amount", amount);
								params.put("target_country_code", countryCode);
								params.put("target_phone", targetPhone);
								params.put("fee", fee);
								params.put("currency", currency);
								params.put("remark", remark);
								params.put("unique_token", mUniqueToken);
								submit(params, new SimpleRequestListener() {
									@Override
									public void loadSuccess(String result, String token) {
										try {
											mUniqueToken = token;
											mView.clearInfo();
											JSONObject object=new JSONObject(result);
											mView.showSuccess(object);
										} catch (Exception e) {
											FileUtils.addErrorLog(e);
											e.printStackTrace();
										}
									}

									@Override
									public void loadFailure(int code, String result, String token) {
										try {
											mUniqueToken = token;
											if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
												Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_114_MEMBER_TRANSFER_TO_NO_MEMBER));
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
}
