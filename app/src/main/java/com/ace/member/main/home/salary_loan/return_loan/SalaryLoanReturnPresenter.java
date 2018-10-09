package com.ace.member.main.home.salary_loan.return_loan;

import android.content.Context;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.event.RefreshEvent;
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


class SalaryLoanReturnPresenter extends BasePresenter implements SalaryLoanReturnContract.Presenter {
	private final SalaryLoanReturnContract.View mView;
	private SalaryLoanReturnWrapper mConfigWrapper;
	private double mCurrentLoan;
	private String mUniqueToken;

	@Inject
	SalaryLoanReturnPresenter(SalaryLoanReturnContract.View view, Context context) {
		super(context);
		this.mView = view;
	}

	@Override
	public void getSalaryLoanReturnConfig() {
		Map<String, String> map = new HashMap<>();
		map.put("_s", Session.sSid);
		map.put("_a", "loan");
		map.put("_b", "aj");
		map.put("cmd", "getSalaryLoanReturnConfig");
		submit(map, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mUniqueToken = uniqueToken;
					mConfigWrapper = JsonUtil.jsonToBean(result, SalaryLoanReturnWrapper.class);
					assert mConfigWrapper != null;
					mCurrentLoan = mConfigWrapper.getCurrentLoan();
					mView.setData(mCurrentLoan, mConfigWrapper.getUsdBalance());
				} catch (Exception e) {
					mView.setEnableBtnSubmit(false);
					e.printStackTrace();
					FileUtils.addErrorLog(e);
				}
			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				mUniqueToken = uniqueToken;
				mView.setEnableBtnSubmit(false);
			}
		});
	}

	@Override
	public boolean checkAmount(String amount) {
		//验证当前余额是否足够还款
		if (mConfigWrapper.getUsdBalance() < Utils.strToDouble(amount)) {
			mView.showToast(Utils.getString(R.string.usd_balance_not_enough));
			return false;
		}
		return true;
	}

	@Override
	public void checkSubmit(final String amount) {
		Map<String, String> params = new HashMap<>();
		params.put("_b", "aj");
		params.put("_a", "loan");
		params.put("cmd", "checkSalaryLoanReturn");
		params.put("_s", Session.sSid);
		params.put("unique_token", mUniqueToken);
		params.put("amount", amount);
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
				mConfigWrapper = JsonUtil.jsonToBean(result, SalaryLoanReturnWrapper.class);
				EventBusUtil.post(new RefreshEvent());
				checkAmount(amount);
			}
		});
	}


	@Override
	public void submit(final String amount) {
		final double a = Utils.strToDouble(amount);
		try {
			PayHelper ph = new PayHelper(mView.getActivity());
			ph.startPay(new PayHelper.CallBackPart() {
				@Override
				public void paySuccess() {
					Map<String, String> params = new HashMap<>();
					params.put("_b", "aj");
					params.put("_a", "loan");
					params.put("cmd", "salaryLoanReturn");
					params.put("_s", Session.sSid);
					params.put("unique_token", mUniqueToken);
					params.put("amount", String.valueOf(a));
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
							mConfigWrapper = JsonUtil.jsonToBean(result, SalaryLoanReturnWrapper.class);
							EventBusUtil.post(new RefreshEvent());
							checkAmount(amount);
							payFail();
						}
					});
				}

				@Override
				public void payFail() {
					mView.setEnableBtnSubmit(false);
					mView.showLoanFail();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
	}

}
