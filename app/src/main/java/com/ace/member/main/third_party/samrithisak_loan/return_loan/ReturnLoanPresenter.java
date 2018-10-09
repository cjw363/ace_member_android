package com.ace.member.main.third_party.samrithisak_loan.return_loan;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.MemberLoanPartner;
import com.ace.member.event.RefreshEvent;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
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


public class ReturnLoanPresenter extends BasePresenter implements ReturnLoanContract.Presenter {
	private final ReturnLoanContract.View mView;
	private MemberLoanPartner mLoanPartner;
	private String mUniqueToken;

	@Inject
	public ReturnLoanPresenter(ReturnLoanContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void start() {
		Map<String, String> map = new HashMap<>();
		map.put("_s", Session.sSid);
		map.put("_a", "loan");
		map.put("_b", "aj");
		map.put("cmd", "getPartnerCreditLoan");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
					mUniqueToken = uniqueToken;
					setConfig(result);
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(M.get(mContext, errorCode));
				mUniqueToken = uniqueToken;
				mView.setEnableBtnSubmit(false);
			}
		});
	}


	@Override
	public boolean checkAmount(String amount) {
		final double a = Utils.strToDouble(amount);
		if (a <= 0) {
			Utils.showToast(R.string.invalid_amount);
			return false;
		}

		if (a > mLoanPartner.getLoan()) {
			Utils.showToast(R.string.over_loan_amount);
			return false;
		}

		if (a > AppUtils.getBalance(AppGlobal.USD)) {
			Utils.showToast(R.string.msg_1709);
			return false;
		}
		return true;
	}

	@Override
	public void checkSubmit(final String amount) {
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "loan");
		params.put("cmd", "checkPartnerReturnLoan");
		params.put("_s", Session.sSid);
		params.put("unique_token", mUniqueToken);
		params.put("amount", amount);
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
				mUniqueToken=uniqueToken;
				setConfig(result);
				checkSubmit(amount);
			}
		});
	}


	private void setConfig(String result) {
		try {
			mLoanPartner = JsonUtil.jsonToBean(result, MemberLoanPartner.class);
			if (mLoanPartner == null) {
				mView.setEnableBtnSubmit(false);
				Utils.showToast(R.string.no_loan_credit, Snackbar.LENGTH_LONG);
			} else {
				mView.setEnableBtnSubmit(true);
				mView.setCreditAndLoan(mLoanPartner);
			}
		} catch (Exception e) {
			mView.setEnableBtnSubmit(false);
			FileUtils.addErrorLog(e);
		}
	}

	public void submit(final String amount) {
		try {
			PayHelper ph = new PayHelper(mView.getActivity());
			ph.startPay(new PayHelper.CallBackPart() {
				@Override
				public void paySuccess() {
					Map<String, String> params = new HashMap<>();
					params.put("_b", "aj");
					params.put("_a", "loan");
					params.put("cmd", "returnPartnerLoan");
					params.put("_s", Session.sSid);
					params.put("unique_token", mUniqueToken);
					params.put("amount",amount);
					submit(params, new SimpleRequestListener() {
						@Override
						public void loadSuccess(String result, String token) {
							try {
								mUniqueToken = token;
								mView.setEnableBtnSubmit(false);
								mView.showReturnLoanSuccess();
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
						}
					});
				}

				@Override
				public void payFail() {
					mView.setEnableBtnSubmit(false);
					mView.showLoanFail();
				}
			});
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}

	}

	@Override
	public int getCurrentLoan() {
		return mLoanPartner == null ? 0 : mLoanPartner.getLoan();
	}
}
