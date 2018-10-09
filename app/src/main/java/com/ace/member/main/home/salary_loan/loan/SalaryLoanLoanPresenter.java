package com.ace.member.main.home.salary_loan.loan;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.SalaryLoanBean;
import com.ace.member.bean.SalaryLoanConfigBean;
import com.ace.member.event.RefreshEvent;
import com.ace.member.main.home.salary_loan.SalaryLoanData;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.ace.member.utils.Session;
import com.og.http.SimpleRequestListener;
import com.og.utils.EventBusUtil;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


class SalaryLoanLoanPresenter extends BasePresenter implements SalaryLoanLoanContract.Presenter {
	private final SalaryLoanLoanContract.View mView;
	private SalaryLoanData mConfigWrapper;
	private SalaryLoanConfigBean mLoanConfig;
	private SalaryLoanBean mCreditLoan;
	private String mUniqueToken;

	@Inject
	SalaryLoanLoanPresenter(SalaryLoanLoanContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void getSalaryLoanConfig() {
		Map<String, String> map = new HashMap<>();
		map.put("_s", Session.sSid);
		map.put("_a", "loan");
		map.put("_b", "aj");
		map.put("cmd", "getSalaryLoanConfig");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				mUniqueToken = uniqueToken;
				setConfig(result);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mView.showToast(M.get(mContext, errorCode));
				mUniqueToken = uniqueToken;
				mView.setEnableBtnSubmit(false);
			}
		});
	}

	private void setConfig(String result) {
		try {
			mConfigWrapper = JsonUtil.jsonToBean(result, SalaryLoanData.class);
			assert mConfigWrapper != null;
			mLoanConfig = mConfigWrapper.getLoanConfig();
			mCreditLoan = mConfigWrapper.getCreditLoan();

			if (mLoanConfig == null || mCreditLoan == null) {
				mView.setEnableBtnSubmit(false);
				mView.showToast(Utils.getString(R.string.fail));
			} else {
				mView.setCreditAndLoan(mCreditLoan);
			}
		} catch (Exception e) {
			mView.setEnableBtnSubmit(false);
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void updateServiceCharge(String amount) {
		double charge = getServiceCharge(amount);
		mView.setServiceCharge(Utils.format(charge, 2));
	}

	private double getServiceCharge(String amount) {
		if (mCreditLoan == null || mLoanConfig == null) return 0;
		double serviceChargeRate = mLoanConfig.getServiceChargeRate();
		double serviceChargeMinAmount = mLoanConfig.getServiceChargeMinAmount();
		double a = Utils.strToDouble(amount);
		if (a <= 0 || serviceChargeRate == 0) {
			return 0;
		}
		return (serviceChargeRate * a) > serviceChargeMinAmount ? (serviceChargeRate * a) : serviceChargeMinAmount;
	}

	@Override
	public boolean checkAmount(String amount) {
		try {
			final double a = Utils.strToDouble(amount);
			double credit = mCreditLoan.getCredit();
			double loan = mCreditLoan.getLoan();
			double minAmount = mLoanConfig.getMinAmount();
			double maxAmount = mLoanConfig.getMaxAmount();
			double availableLoan = mLoanConfig.getAvailableLoan();

			if (a <= 0) {
				mView.showToast(Utils.getString(R.string.invalid_amount));
				return false;
			}

			//交易金额限定为 loanAmountUnit 的倍数
			int loanAmountUnit = mLoanConfig.getLoanAmountUnit();
			if (a % loanAmountUnit > 0) {
				mView.showToast(String.format(mContext.getString(R.string.input_multiple_amount), Utils.format(loanAmountUnit, 0) + ""));
				return false;
			}

			if (a + loan > credit) {
				mView.showToast(String.format(Utils.getString(R.string.loan_limit), Utils.format("0", 0), Utils
					.format(credit - loan, 0)));
				return false;
			}

			if (a < minAmount || a > maxAmount) {
				mView.showToast(String.format(Utils.getString(R.string.loan_limit), Utils.format(minAmount, 0), Utils
					.format(maxAmount, 0)));
				return false;
			}

			if (a > availableLoan) {
				mView.showToast(String.format(Utils.getString(R.string.over_max_loan_limit), Utils.format("0", 0), Utils
					.format(availableLoan, 0)));
				return false;
			}

			if (a + mConfigWrapper.getPendingAmount() + mConfigWrapper.getTotalBalance() > mConfigWrapper.getMaxBalance()) {
				mView.showToast(Utils.getString(R.string.over_max_balance_limit) + " (" + Utils.format(mConfigWrapper
					.getMaxBalance(), 2) + " " + AppGlobal.USD + ")");
				return false;
			}

			return true;
		} catch (Exception e) {
			mView.showToast(Utils.getString(R.string.error));
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void checkSubmit(final String amount) {
		final double charge = getServiceCharge(amount);
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "loan");
		params.put("cmd", "checkSalaryLoan");
		params.put("_s", Session.sSid);
		params.put("unique_token", mUniqueToken);
		params.put("amount", amount);
		params.put("charge", String.valueOf(charge));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					submit(amount);
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int code, String result, String token) {
				mUniqueToken = token;
				setConfig(result);
				EventBusUtil.post(new RefreshEvent());
				checkAmount(amount);
			}
		});
	}

	@Override
	public void submit(final String amount) {
		final double charge = getServiceCharge(amount);
		try {
			PayHelper ph = new PayHelper(mView.getActivity());
			ph.startPay(new PayHelper.CallBackPart() {
				@Override
				public void paySuccess() {
					Map<String, String> params = new HashMap<>();
					params.put("_b", "aj");
					params.put("_a", "loan");
					params.put("cmd", "salaryLoan");
					params.put("_s", Session.sSid);
					params.put("unique_token", mUniqueToken);
					params.put("amount", amount);
					params.put("charge", String.valueOf(charge));
					submit(params, new SimpleRequestListener() {
						@Override
						public void loadSuccess(String result, String token) {
							try {
								mView.setEnableBtnSubmit(false);
								mView.showLoanSuccess();
								EventBusUtil.post(new RefreshEvent());
							} catch (Exception e) {
								FileUtils.addErrorLog(e);
								payFail();
							}
						}

						@Override
						public void loadFailure(int code, String result, String token) {
							mUniqueToken = token;
							setConfig(result);
							EventBusUtil.post(new RefreshEvent());
							checkAmount(amount);
							if (code == M.MessageCode.ERR_1837_CONFIG_EXPIRED){
								mView.showToast(M.get(mContext, code));
							}
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

}
