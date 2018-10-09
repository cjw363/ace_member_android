package com.ace.member.main.third_party.samrithisak_loan.loan;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.LoanConfig;
import com.ace.member.bean.MemberLoanConfigWrapper;
import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.event.RefreshEvent;
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


public class LoanPresenter extends BasePresenter implements LoanContract.Presenter {
	private final LoanContract.View mView;
	private MemberLoanConfigWrapper mConfigWrapper;
	private MemberLoanPartner mLoanPartner;
	private LoanConfig mLoanConfig;
	private String mUniqueToken;

	@Inject
	public LoanPresenter(LoanContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		Map<String, String> map = new HashMap<>();
		map.put("_s", Session.sSid);
		map.put("_a", "loan");
		map.put("_b", "aj");
		map.put("cmd", "getPartnerLoanConfig");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					setConfig(result);
				} catch (Exception e) {
					mView.setEnableBtnSubmit(false);
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(M.get(mContext, errorCode));
				mUniqueToken = uniqueToken;
				mView.setEnableBtnSubmit(false);
			}
		});
	}

	private void setConfig(String config) {
		try {
			mConfigWrapper = JsonUtil.jsonToBean(config, MemberLoanConfigWrapper.class);
			assert mConfigWrapper != null;
			mLoanPartner = mConfigWrapper.getLoanPartner();
			mLoanConfig = mConfigWrapper.getLoanConfig();
			if (mLoanPartner == null) {
				mView.setEnableBtnSubmit(false);
				Utils.showToast(R.string.no_loan_credit, Snackbar.LENGTH_LONG);
			} else {
				mView.setCreditAndLoan(mLoanPartner);
				if (mLoanPartner.getCredit() <= mLoanPartner.getLoan()) {
					mView.setEnableBtnSubmit(false);
					Utils.showToast(R.string.over_available_credit, Snackbar.LENGTH_LONG);
					return;
				}
				if (mLoanConfig == null) {
					mView.setEnableBtnSubmit(false);
					Utils.showToast(R.string.fail, Snackbar.LENGTH_LONG);
				} else {
					if (mLoanConfig.isOverMemberCountLimit()) {
						mView.setEnableBtnSubmit(false);
						Utils.showToast(R.string.over_member_count_limit, Snackbar.LENGTH_LONG);
					} else {
						mView.setEnableBtnSubmit(true);
					}
				}
			}
			mView.setEnableBtnSubmit(true);
		} catch (Exception e) {
			mView.setEnableBtnSubmit(false);
			FileUtils.addErrorLog(e);
		}
	}


	@Override
	public boolean checkAmount(String amount) {
		try {
			double a = Utils.strToDouble(amount);
			if (a <= 0) {
				Utils.showToast(R.string.invalid_amount);
				return false;
			}

			if (mLoanConfig.isOverMemberCountLimit()) {
				Utils.showToast(R.string.over_member_count_limit);
				return false;
			}

			if (a + mLoanPartner.getLoan() > mLoanPartner.getCredit()) {
				Utils.showToast(R.string.over_available_credit);
				return false;
			}

			double charge = mLoanConfig.getServiceChargeRate() * 0.01 * a;
			if (charge < mLoanConfig.getServiceChargeMinAmount())
				charge = mLoanConfig.getServiceChargeMinAmount();
			if (a <= charge) {
				Utils.showToast(Utils.getString(R.string.need_greater_than_service_charge), Snackbar.LENGTH_LONG, null);
				return false;
			}

			double c = Utils.strToDouble(getServiceCharge(amount));
			if (c != charge) {
				Utils.showToast(Utils.getString(R.string.service_charge_not_match), Snackbar.LENGTH_LONG);
				return false;
			}

			if (a < mLoanConfig.getMinLoanCreditPerMember()) {
				Utils.showToast(Utils.getString(R.string.less_than_min_loan) + " (" + Utils.format(mLoanConfig.getMinLoanCreditPerMember(), 2) + ")", Snackbar.LENGTH_LONG, null);
				return false;
			}

			if (a > mConfigWrapper.getLoanConfig()
				.getAvailableLoan()) {
				Utils.showToast(Utils.getString(R.string.over_partner_total_loan_limit), Snackbar.LENGTH_LONG);
				return false;
			}

			double maxLoan = Math.min(mConfigWrapper.getMaxLoan(), mConfigWrapper.getLoanConfig()
				.getMaxLoanCreditPerMember());

			if (a + mLoanPartner.getLoan() > maxLoan) {
				Utils.showToast(Utils.getString(R.string.over_max_loan) + " (" + Utils.format(maxLoan, 2) + ")", Snackbar.LENGTH_LONG, null);
				return false;
			}

			if (a + mConfigWrapper.getPendingAmount() + mConfigWrapper.getTotalBalance() > mConfigWrapper.getMaxBalance()) {
				Utils.showToast(Utils.getString(R.string.over_max_balance_limit) + " (" + Utils.format(mConfigWrapper.getMaxBalance(), 2) + ")", Snackbar.LENGTH_LONG, null);
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
			return false;
		}
	}

	@Override
	public void checkSubmit(final String amount) {
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "loan");
		params.put("cmd", "checkPartnerLoanConfig");
		params.put("_s", Session.sSid);
		params.put("unique_token", mUniqueToken);
		params.put("amount", amount);
		params.put("service_charge", getServiceCharge(amount));
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					submit(amount);
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mUniqueToken = uniqueToken;
				setConfig(result);
				checkAmount(amount);
				EventBusUtil.post(new RefreshEvent());
			}
		});
	}

	@Override
	public void updateServiceCharge(String amount) {
		mView.setServiceCharge(getServiceCharge(amount));
	}

	private String getServiceCharge(String amount) {
		if (mLoanPartner == null || mLoanConfig == null) return null;

		double a = Utils.strToDouble(amount);
		if (a <= 0 || mLoanConfig.getServiceChargeRate() == 0 || mLoanConfig.getServiceChargeMinAmount() == 0) {
			return "0";
		}
		double charge = mLoanConfig.getServiceChargeRate() * 0.01 * a;
		if (charge < mLoanConfig.getServiceChargeMinAmount())
			charge = mLoanConfig.getServiceChargeMinAmount();
		return String.valueOf(charge);
	}


	private void submit(final String amount) {
		PayHelper ph = new PayHelper(mView.getActivity());
		ph.startPay(new PayHelper.CallBackPart() {
			@Override
			public void paySuccess() {
				Map<String, String> params = new HashMap<>();
				params.put("_b", "aj");
				params.put("_a", "loan");
				params.put("cmd", "loanPartner");
				params.put("_s", Session.sSid);
				params.put("unique_token", mUniqueToken);
				params.put("amount", amount);
				params.put("service_charge", getServiceCharge(amount));
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
						payFail();
						Utils.showToast(M.get(mContext, code));
						setConfig(result);
						EventBusUtil.post(new RefreshEvent());
						checkAmount(amount);
					}
				});
			}

			@Override
			public void payFail() {
				mView.setEnableBtnSubmit(false);
				mView.showLoanFail();
			}
		});
	}
}
