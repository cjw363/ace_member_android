package com.ace.member.main.currency.deposit;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.DepositDataWrapper;
import com.ace.member.bean.DepositLimitConfig;
import com.ace.member.bean.SingleIntBean;
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

public class DepositPresenter extends BasePresenter implements DepositContract.DepositPresenter {

	private DepositContract.DepositView mDepositView;
	private String mUniqueToken;
	private DepositDataWrapper mDataWrapper;

	@Inject
	public DepositPresenter(DepositContract.DepositView depositView, Context context) {
		super(context);
		mDepositView = depositView;
	}

	public void getBankList(String currency) {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getDepositData");
		params.put("currency", currency);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String token) {
				try {
					mUniqueToken = token;
					mDataWrapper = JsonUtil.jsonToBean(result, DepositDataWrapper.class);
					assert mDataWrapper != null;
					mDepositView.setCompanyBankList(mDataWrapper.getCompanyBanks());
					mDepositView.setMemberBankList(mDataWrapper.getMemberBanks());
					mDepositView.initBank();
					mDepositView.banDeposit(mDataWrapper.isFunctionDeposit());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		}, false);
	}

	@Override
	public void start(String currency) {
		getBankList(currency);
	}

	@Override
	public void deposit(final String currency, final String amount, final String bank, final String companyBankAccountNo, final String memberBankAccountNo, final String remark) {
		if (TextUtils.isEmpty(bank) || TextUtils.isEmpty(companyBankAccountNo) || TextUtils.isEmpty(memberBankAccountNo)) {
			Utils.showToast(R.string.need_bank_account);
			return;
		}
		final double a = TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount.replace(",", ""));
		if (a <= 0) {
			Utils.showToast(R.string.invalid_amount);
			return;
		}

		Map<String, String> params = new HashMap<>();
		params.put("_a", "user");
		params.put("_b", "aj");
		params.put("_s", Session.sSid);
		params.put("cmd", "getDepositLimit");
		params.put("currency", currency);
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					DepositLimitConfig limitConfig = JsonUtil.jsonToBean(result, DepositLimitConfig.class);
					double dayAmount = mDataWrapper.getDayAmount();
					assert limitConfig != null;
					if (a > limitConfig.getMaxDepositPerTime()) {
						Utils.showToast(Utils.getString(R.string.over_one_time_limit) + " (" + Utils.format(limitConfig.getMaxDepositPerTime(), currency) + " " + currency + ")", Snackbar.LENGTH_LONG, null);
						return;
					}
					if (a + dayAmount > limitConfig.getMaxDepositPerDay()) {
						Utils.showToast(Utils.getString(R.string.over_daily_limit) + " (" + Utils.format(limitConfig.getMaxDepositPerDay(), currency) + " " + currency + ")", Snackbar.LENGTH_LONG, null);
						return;
					}

					if (a + mDataWrapper.getPendingAmount() + mDataWrapper.getTotalBalance() > limitConfig.getMaxBalance()) {
						Utils.showToast(Utils.getString(R.string.over_max_balance_limit) + " (" + Utils.format(limitConfig.getMaxBalance(), currency) + " " + currency + ")", Snackbar.LENGTH_LONG, null);
						return;
					}
					PayHelper payHelper = new PayHelper(mDepositView.getActivity());
					payHelper.startPay(new PayHelper.CallBackPart() {
						@Override
						public void paySuccess() {
							Map<String, String> params = new HashMap<>();
							params.put("_b", "aj");
							params.put("_a", "balance");
							params.put("cmd", "deposit");
							params.put("_s", Session.sSid);
							params.put("unique_token", mUniqueToken);
							params.put("currency", currency);
							params.put("amount", String.valueOf(a));
							params.put("member_bank", bank);
							params.put("company_bank_account_no", companyBankAccountNo);
							params.put("member_bank_account_no", memberBankAccountNo);
							params.put("remark", remark);
							submit(params, new SimpleRequestListener() {
								@Override
								public void loadSuccess(String result, String token) {
									try {
										mUniqueToken = token;
										SingleIntBean bean = JsonUtil.jsonToBean(result, SingleIntBean.class);
										assert bean != null;
										int id = bean.getValue();
										mDepositView.toDepositDetail(id);
										EventBusUtil.post(new RefreshEvent());
									} catch (Exception e) {
										FileUtils.addErrorLog(e);
										payFail();
									}
								}

								@Override
								public void loadFailure(int code, String result, String token) {
									mUniqueToken = token;
									if (code == com.og.M.MessageCode.ERR_505_FUNCTION_NOT_RUNNING) {
										Utils.showToast(AppUtils.getFunctionPauseMsg(M.FunctionCode.FUNCTION_111_MEMBER_DEPOSIT));
										mDepositView.enableDepositBtn(false);
									} else {
										payFail();
										Utils.showToast(M.get(mContext, code));
									}
								}
							});
						}

						@Override
						public void payFail() {
							mDepositView.showDepositFail();
						}
					});
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}

			}

			@Override
			public void loadFailure(int errorCode, String result, String uniqueToken) {
				Utils.showToast(R.string.error);
			}
		});
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
					getBankList(currency);
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
}
