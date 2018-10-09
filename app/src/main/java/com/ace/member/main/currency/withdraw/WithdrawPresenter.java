package com.ace.member.main.currency.withdraw;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.BankAccount;
import com.ace.member.bean.BankConfig;
import com.ace.member.bean.BankWithdrawAmount;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.bean.WithdrawConfig;
import com.ace.member.bean.WithdrawDataWrapper;
import com.ace.member.bean.WithdrawLimitConfig;
import com.ace.member.event.RefreshEvent;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.og.LibSession;
import com.og.http.SimpleRequestListener;
import com.og.utils.EventBusUtil;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class WithdrawPresenter extends BasePresenter implements WithdrawContract.WithdrawPresenter {

	private WithdrawContract.WithdrawView mView;
	private String mUniqueToken;
	private double mFee;
	private WithdrawDataWrapper mDataWrapper;
	private WithdrawConfig mWithdrawConfig;

	@Inject
	public WithdrawPresenter(WithdrawContract.WithdrawView withdrawView, Context context) {
		super(context);
		mView = withdrawView;
	}

	@Override
	public void getBankListAndWithdrawFee(String currency) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("currency", currency);
		params.put("cmd", "getWithdrawData");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					mDataWrapper = JsonUtil.jsonToBean(result, WithdrawDataWrapper.class);
					assert mDataWrapper != null;
					mWithdrawConfig = mDataWrapper.getWithdrawConfig();
					mView.setCompanyBankList(mDataWrapper.getCompanyBanks());
					mView.setMemberBankList(mDataWrapper.getMemberBanks());
					mView.initBank();
					mView.banWithdraw(mDataWrapper.isFunctionWithdraw());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		}, false);
	}

	@Override
	public void deleteBank(final Map<String, String> params, final String currency) {
		params.put("_b", "aj");
		params.put("_a", "user");
		params.put("cmd", "deleteMemberBankInfo");
		params.put("_s", Session.sSid);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					getBankListAndWithdrawFee(currency);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				Utils.showToast(Utils.getString(R.string.fail));
			}
		});
	}

	@Override
	public void withdraw(final String currency, final String amount, final String bank, final String bankAccountNo, String bankFee, final String remark) {
		if (TextUtils.isEmpty(bank) || TextUtils.isEmpty(bankAccountNo)) {
			Utils.showToast(R.string.need_bank_account);
			return;
		}

		final double a = TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount.replace(",", ""));
		if (a <= 0) {
			Utils.showToast(R.string.invalid_amount);
			return;
		}
		double bf = TextUtils.isEmpty(bankFee) ? 0 : Double.parseDouble(bankFee.replace(",", "")
			.trim());
		if (!AppUtils.checkEnoughMoney(currency, a, mFee, bf)) {
			Utils.showToast(R.string.msg_1709);
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getWithdrawLimit");
		params.put("currency", currency);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				WithdrawLimitConfig limitConfig = JsonUtil.jsonToBean(result, WithdrawLimitConfig.class);
				double dayAmount = mDataWrapper.getDayAmount();
				assert limitConfig != null;

				if (TextUtils.isEmpty(bank) || TextUtils.isEmpty(bankAccountNo)) {
					Utils.showToast(R.string.need_bank_account);
					return;
				}

				if (a > limitConfig.getMaxWithdrawPerTime()) {
					Utils.showToast(Utils.getString(R.string.over_one_time_limit) + " (" + Utils.format(limitConfig
						.getMaxWithdrawPerTime(), currency) + ")", Snackbar.LENGTH_LONG, null);
					return;
				}
				if (a + dayAmount > limitConfig.getMaxWithdrawPerDay()) {
					Utils.showToast(Utils.getString(R.string.over_daily_limit) + " (" + Utils.format(limitConfig
						.getMaxWithdrawPerDay(), currency) + ")", Snackbar.LENGTH_LONG, null);
					return;
				}

				BankAccount bankAccount = mView.getCurrentBank();
				String bankCode = bankAccount.getCode();
				if (isOverBankMaxTransferAmount(bankCode, a)) {
					Utils.showToast(Utils.getString(R.string.over_one_day_bank_transfer_limit) + " (" + Utils
						.format(getBankConfig(bankCode).getTransferAmountMaxPerDay(), currency) + ")", Snackbar.LENGTH_LONG, null);
					return;
				}

				PayHelper payHelper = new PayHelper(mView.getActivity());
				payHelper.startPay(new PayHelper.CallBackPart() {
					@Override
					public void paySuccess() {
						Map<String, String> params = new HashMap<>();
						params.put("currency", currency);
						params.put("amount", String.valueOf(a));
						params.put("bank", bank);
						params.put("bank_account_no", bankAccountNo);
						params.put("bank_account_name", Session.user.getName());
						params.put("remark", remark);
						params.put("_b", "aj");
						params.put("_a", "balance");
						params.put("cmd", "withdrawToLocalBank");
						params.put("unique_token", mUniqueToken);
						params.put("_s", LibSession.sSid);

						submit(params, new SimpleRequestListener() {
							@Override
							public void loadSuccess(String result, String token) {
								try {
									mUniqueToken = token;
									SingleIntBean bean = JsonUtil.jsonToBean(result, SingleIntBean.class);
									assert bean != null;
									int id = bean.getValue();
									mView.toWithdrawDetail(id);
									EventBusUtil.post(new RefreshEvent());
								} catch (Exception e) {
									FileUtils.addErrorLog(e);
								}
							}

							@Override
							public void loadFailure(int errorCode, String result, String token) {
								mUniqueToken = token;
								if (errorCode == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
									Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_111_MEMBER_DEPOSIT));
									mView.enableWithdrawBtn(false);
								} else {
									payFail();
								}
							}
						});
					}

					@Override
					public void payFail() {
						mView.showWithDrawFail();
					}
				});
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.error);
			}
		});
	}

	@Override
	public void calculateBankFee(BankAccount bankAccount, String amount) {
		double withdrawAmount = TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount.replace(",", ""));
		if (bankAccount == null || withdrawAmount == 0 || Utils.isEmptyList(mDataWrapper.getBankConfigs())) {
			mView.setBankFee("0");
		} else {
			BankConfig currentBankConfig = getBankConfig(bankAccount.getCode());
			if (currentBankConfig.getTransferAmountUnit() == 0 || currentBankConfig.getTransferAmountUnitFee() == 0) {
				mView.setBankFee("0");
			} else {
				mView.setBankFee(String.valueOf(Math.ceil(withdrawAmount / currentBankConfig.getTransferAmountUnit()) * currentBankConfig
					.getTransferAmountUnitFee()));
			}
		}
	}

	private BankConfig getBankConfig(String bankCode) {
		for (BankConfig bankConfig : mDataWrapper.getBankConfigs()) {
			if (bankConfig.getBankCode().equals(bankCode)) {
				return bankConfig;
			}
		}
		return new BankConfig();
	}

	@Override
	public void start(String currency) {
		getBankListAndWithdrawFee(currency);
	}

	@Override
	public void onWithdrawAmountTextChange(String amount) {
		if (TextUtils.isEmpty(amount)) {
			mView.setWithdrawFee("0");
			mView.setBankFee("0");
			return;
		}
		double a = Double.parseDouble(amount.replace(",", ""));
		if (a == 0) {
			mView.setWithdrawFee("0");
		} else {
			if (mWithdrawConfig.getFeeUnit() == 0 || mWithdrawConfig.getFeeAmount() == 0) {
				mView.setWithdrawFee("0");
				return;
			}
			double exchange = mDataWrapper.getExchangeInfo().getExchangeFee();
			mFee = mWithdrawConfig.getFeeAmount() * (Math.ceil(a / exchange / mWithdrawConfig.getFeeUnit())) * exchange;
			mView.setWithdrawFee(String.valueOf(mFee));
		}
		calculateBankFee(mView.getCurrentBank(), amount);
	}

	private boolean isOverBankMaxTransferAmount(String bankCode, double amount) {
		if (TextUtils.isEmpty(bankCode)) return false;
		bankCode = bankCode.toUpperCase();
		double maxTransferAmount = 0;
		double currentTransferAmount = 0;
		for (BankConfig config : mDataWrapper.getBankConfigs()) {
			if (bankCode.equals(config.getBankCode().toUpperCase())) {
				maxTransferAmount = config.getTransferAmountMaxPerDay();
				break;
			}
		}
		for (BankWithdrawAmount withdrawAmount : mDataWrapper.getBankWithdrawAmounts()) {
			if (bankCode.equals(withdrawAmount.getBankCode().toUpperCase())) {
				currentTransferAmount = withdrawAmount.getAmount();
				break;
			}
		}
		return currentTransferAmount + amount > maxTransferAmount;
	}
}
