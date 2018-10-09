package com.ace.member.main.home.ace_loan.loan;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.ace.member.R;
import com.ace.member.base.BasePresenter;
import com.ace.member.bean.ACELoanBean;
import com.ace.member.bean.ACELoanFail;
import com.ace.member.bean.CreditBean;
import com.ace.member.bean.TermBean;
import com.ace.member.main.bottom_dialog.ControllerBean;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.M;
import com.ace.member.utils.PayHelper;
import com.og.LibSession;
import com.og.http.IDataRequestListener;
import com.og.http.SimpleRequestListener;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MemberLoanAcePresenter extends BasePresenter implements MemberLoanAceContract.Presenter {
	private final MemberLoanAceContract.View mView;
	//	private ACELoanConfig mConfig;
	private ACELoanBean mLoan;
	//	private double mMaxAmount;
	private DecimalFormat mDecimalFormat;
	private List<CreditBean> mList = new ArrayList<>();
	private String mToken = "";
	private int mRepayDay = 0;

	@Inject
	public MemberLoanAcePresenter(MemberLoanAceContract.View view, Context context) {
		super(context);
		mView = view;
		mDecimalFormat = new DecimalFormat("00");
	}

	@Override
	public void getLoanData() {
		Map<String, String> params = new HashMap<>();
		params.put("_a", "loan");
		params.put("_b", "aj");
		params.put("_s", LibSession.sSid);
		params.put("cmd", "getAceLoanData");
		submit(params, new SimpleRequestListener() {
			@Override
			public void loadSuccess(String result, String uniqueToken) {
				try {
					mToken = uniqueToken;
					MemberLoanAceData data = JsonUtil.jsonToBean(result, MemberLoanAceData.class);
					assert data != null;
					mLoan = data.getLoan();
					mView.initLoanAanCredit(mLoan.getLoan(), mLoan.getCredit(), mLoan.getCurrency());
					mView.initDayInterest(mLoan.getRate());
					mView.initNextMonthRepayment(data.getRepayBean());
				} catch (Exception e) {
					FileUtils.addErrorLog(e);
				}
			}
		});
	}


	@Override
	public void initTermListView() {
		try {
			String amountStr = mView.getAmount();
			double amount = Utils.strToDouble(amountStr);
			if (amount == 0) return;
			List<TermBean> list = new ArrayList<>();
			List<ControllerBean> list2 = new ArrayList<>();
			if (amount > 0) {
				for (int i = 1; i < 7; i++) {
					if (amount % i == 0) {
						TermBean bean = getTermBean(i);
						if (bean != null) {
							list.add(bean);
							ControllerBean bean1 = new ControllerBean();
							String str = "";
							if (i == 1) {
								str = Utils.getString(R.string.x_month);
							} else {
								str = Utils.getString(R.string.x_months);
							}
							bean1.setContent2(String.format(str, bean.getTerm()));
							bean1.setId(i);
							list2.add(bean1);
						}
					}
				}
			}
			mView.initListView(list, list2);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private TermBean getTermBean(int term) {
		try {
			TermBean bean1 = new TermBean();
			String amountStr = mView.getAmount();
			double amount = Utils.strToDouble(amountStr);
			List<CreditBean> list = getRepayTermDetail(term);
			if (list == null) return null;
			bean1.setTerm(term);
			double interest = 0;
			for (CreditBean bean : list) {
				interest += bean.getInterest();
			}
			bean1.setAmount(amount);
			bean1.setInterest(interest);
			bean1.setCurrency(AppGlobal.USD);
			return bean1;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}


	private List<CreditBean> getRepayTermDetail(int term) {
		try {
			if (term > 0 && term <= 6) {
				List<CreditBean> list = new ArrayList<>();
				String amountStr = mView.getAmount();
				if (!TextUtils.isEmpty(amountStr)) {
					list.clear();
					mRepayDay = 0;
					int startDay=0;
					double amount = Utils.strToDouble(amountStr);
					double amount1 = Utils.round2(amount / term, 2);
					mRepayDay = mLoan.getRepayDay();
					int day = DateUtils.getDay();
					int month = DateUtils.getMonth();
					int month2 = month;
					if (mRepayDay > 0 && mRepayDay < day) {
						month2++;
					}
					if (mRepayDay == 0) {
						if (day > 28) {
							mRepayDay = 28;
						} else {
							mRepayDay = day;
						}
						startDay=mRepayDay;
					}else{
						startDay=day;
					}
					int year = DateUtils.getYear();
					String time1 = year + "-" + mDecimalFormat.format(month) + "-" + mDecimalFormat.format(startDay);
					String[] times = getRepayTimes(year, month2, mRepayDay, term);
					if (term == 1) {
						double dayInterest = amount * mLoan.getRate() / 100;
						String time2 = times[0];
						int betweenDays = DateUtils.daysBetween(time1, time2);
						double interestAmount = Utils.round2(betweenDays * dayInterest, 2);
						double amount2 = amount1 + interestAmount;
						CreditBean bean = new CreditBean();
						bean.setAmount(amount1);
						bean.setInterest(interestAmount);
						bean.setTotalAmount(amount2);
						bean.setTime(time2);
						list.add(bean);
					} else {
						for (int i = 0; i < term; i++) {
							String time2 = times[i];
							double dayInterest = (amount - amount1 * i) * mLoan.getRate() / 100;
							int betweenDays = DateUtils.daysBetween(time1, time2);
							double interestAmount = Utils.round2(betweenDays * dayInterest, 2);
							double amount2 = amount1 + interestAmount;
							CreditBean bean = new CreditBean();
							bean.setAmount(amount1);
							bean.setInterest(interestAmount);
							bean.setTotalAmount(amount2);
							bean.setTime(time2);
							time1 = time2;
							list.add(bean);
						}
					}
				}
				return list;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return null;
	}

	@Override
	public void submit(final int term) {
		try {
			final String amountStr = mView.getAmount();
			double amount = Utils.strToDouble(amountStr);
			if (amount <= 0) {
				Utils.showToast(Utils.getString(R.string.input_amount));
				return;
			}

			if (amount > mLoan.getCredit() - mLoan.getLoan()) {
				Utils.showToast(String.format(Utils.getString(R.string.loan_limit), Utils.format("0", 2), Utils
						.format(mLoan.getCredit() - mLoan.getLoan(), 2)));
				return;
			}

			mList = getRepayTermDetail(term);
			if (mList == null || mList.size() == 0) {
				Utils.showToast(Utils.getString(R.string.choose_term));
				return;
			}
			int len = mList.size();
			if (len == 0) {
				Utils.showToast(Utils.getString(R.string.repay_error));
				return;
			}

			Map<String, String> p = new HashMap<>();
			p.put("_a", "loan");
			p.put("_b", "aj");
			p.put("_s", LibSession.sSid);
			p.put("cmd", "checkAceLoan");
			p.put("amount", amountStr);
			p.put("term", String.valueOf(term));
			p.put("list", JsonUtil.beanToJson(mList));
			p.put("repay_day", String.valueOf(mRepayDay));
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
							p.put("_a", "loan");
							p.put("_b", "aj");
							p.put("_s", LibSession.sSid);
							p.put("cmd", "saveAceLoan");
							p.put("amount", amountStr);
							p.put("term", String.valueOf(term));
							p.put("list", JsonUtil.beanToJson(mList));
							p.put("repay_day", String.valueOf(mRepayDay));
							p.put("unique_token", mToken);
							submit(p, new IDataRequestListener() {
								@Override
								public void loadSuccess(String result, String uniqueToken) {
									try {
										mToken = uniqueToken;
										mView.saveSucceed();
									} catch (Exception e) {
										FileUtils.addErrorLog(e);
									}
								}

								@Override
								public void loadFailure(int errorCode, String result, String uniqueToken) {
									try {
										mToken = uniqueToken;
										ACELoanFail bean = JsonUtil.jsonToBean(result, ACELoanFail.class);
										showErrorCodeMessage(errorCode, bean);
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
					try {
						MemberLoanAceData data = JsonUtil.jsonToBean(result, MemberLoanAceData.class);
						assert data != null;
						ACELoanFail bean = JsonUtil.jsonToBean(result, ACELoanFail.class);
						showErrorCodeMessage(errorCode, bean);
					} catch (Exception e) {
						FileUtils.addErrorLog(e);
					}
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void showErrorCodeMessage(int code, ACELoanFail bean) {
		switch (code) {
			case M.MessageCode.MSG_1825_OVER_LOAN_CONFIG_LIMIT:
				Utils.showToast(String.format(Utils.getString(R.string.loan_limit), Utils.format(bean.getAmount(), 2), Utils
						.format(bean.getMaxAmount(), 2)));
				break;
			case M.MessageCode.MSG_1826_OVER_LOAN_LIMIT:
				Utils.showToast(String.format(Utils.getString(R.string.loan_limit), Utils.format("0", 2), Utils
						.format(mLoan.getCredit() - mLoan.getLoan(), 2)));
				break;
			case M.MessageCode.MSG_1827_OVER_MEMBER_MAX_LIMIT:
				Utils.showToast(String.format(Utils.getString(R.string.over_max_loan_limit), Utils.format("0", 2), Utils
						.format(bean.getAmount(), 2)));
				break;
			case M.MessageCode.MSG_1824_OVER_MAX_BALANCE:
				Utils.showToast(Utils.getString(R.string.over_max_balance_limit));
				break;
			case M.MessageCode.MSG_1828_INPUT_AMOUNT_TIMES:
				Utils.showToast(String.format(Utils.getString(R.string.input_amount_times), Utils.format(bean
						.getAmount(), 0)));
				break;
			default:
				Utils.showToast(Utils.getString(R.string.fail));
				break;
		}
	}

	private String[] getRepayTimes(int year, int month, int day, int repay) {
		String[] arr = new String[repay];
		try {
			if (repay == 1) {
				int month1 = month + 1;
				if (month1 > 12) {
					year = year + 1;
					month = 1;
				} else {
					month++;
				}
				String str2 = year + "-" + mDecimalFormat.format(month) + "-" + mDecimalFormat.format(day);
				arr[0] = str2;
			} else {
				for (int i = 0; i < repay; i++) {
					int month1 = month + 1;
					if (month1 > 12) {
						year = year + 1;
						month = 1;
					} else {
						month++;
					}
					String str2 = year + "-" + mDecimalFormat.format(month) + "-" + mDecimalFormat.format(day);
					arr[i] = str2;
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return arr;
	}

}
