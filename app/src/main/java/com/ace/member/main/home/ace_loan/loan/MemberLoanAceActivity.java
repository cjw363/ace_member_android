package com.ace.member.main.home.ace_loan.loan;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanRepayBean;
import com.ace.member.bean.TermBean;
import com.ace.member.main.bottom_dialog.BottomDialog2;
import com.ace.member.main.bottom_dialog.ControllerBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.main.home.ace_loan.repay_plan_list.AceRepayPlanListActivity;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.ace.member.utils.AppUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberLoanAceActivity extends AceLoanBaseActivity implements MemberLoanAceContract.View {

	@Inject
	MemberLoanAcePresenter mPresenter;

	@BindView(R.id.et_amount)
	EditText mEtAmount;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_day_interest)
	TextView mTvDayInterest;
	@BindView(R.id.btn_submit)
	Button mBtnSubmit;
	@BindView(R.id.tv_loan)
	TextView mTvLoan;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.tv_term)
	TextView mTvTerm;
	@BindView(R.id.tv_repay)
	TextView mTvRepay;
	@BindView(R.id.ll_repay)
	LinearLayout mLlRepay;
	@BindView(R.id.v_section)
	View mVSection;
	@BindView(R.id.rl_plan)
	RelativeLayout mRlPlan;
	@BindView(R.id.tv_time)
	TextView mTvTime;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;

	private int mTerm = 0;
	private FutureTask<Void> mAmountFutureTask;
	private TextWatcher mEtAmountTextWatcher;
	private List<TermBean> mTermList = new ArrayList<>();
	private List<ControllerBean> mControllerList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerMemberLoanAceComponent.builder()
				.memberLoanAcePresenterModule(new MemberLoanAcePresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_member_loan_ace;
	}

	protected void initActivity() {
		new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.loan).build();
		initView();
		initEventListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.getLoanData();
	}

	private void initView() {
		mEtAmount.setHint(getResources().getString(R.string.amount) + " (" + AppGlobal.USD + ")");
	}

	@Override
	public void initLoanAanCredit(double loan, double credit, String currency) {
		try {
			String loanStr = Utils.format(credit - loan, currency) + " " + currency;
			String creditStr = Utils.getString(R.string.credit) + " " + Utils.format(credit, currency) + " " + currency;
			mTvLoan.setText(loanStr);
			mTvCredit.setText(creditStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@OnClick({R.id.tv_currency, R.id.btn_submit, R.id.ll_term,R.id.rl_plan})
	public void onClicked(View view) {
		try {
			int id = view.getId();
			switch (id) {
				case R.id.tv_currency:
					mEtAmount.requestFocus();
					AppUtils.enableSoftInput(mEtAmount, true);
					break;
				case R.id.btn_submit:
					mPresenter.submit(mTerm);
					break;
				case R.id.ll_term:
					mControllerList.clear();
					mTermList.clear();
					mPresenter.initTermListView();
					break;
				case R.id.rl_plan:
					Utils.toActivity(MemberLoanAceActivity.this, AceRepayPlanListActivity.class);
					break;
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initEventListener() {
		mEtAmountTextWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mAmountFutureTask != null) {
					mAmountFutureTask.cancel(true);
				}
				mTerm = 0;
			}

			@Override
			public void afterTextChanged(Editable s) {
				String currency = AppGlobal.USD;
				if (!TextUtils.isEmpty(getAmount())) {
					mEtAmount.setHint(null);
					mTvCurrency.setText(currency);
				} else {
					mEtAmount.setHint(getResources().getString(R.string.amount) + " (" + currency + ")");
					mTvCurrency.setText(null);
				}
				mTerm = 0;
				mControllerList.clear();
				mTermList.clear();
				mTvTerm.setText("");
				mLlRepay.setVisibility(View.GONE);
				mTvRepay.setText("");
				mAmountFutureTask = new FutureTask<>(new Runnable() {
					@Override
					public void run() {
						mAmountFutureTask.cancel(true);
						initAmount();
					}
				}, null);
				Utils.runOnUIThread(mAmountFutureTask, 1000);
			}
		};
		mEtAmount.addTextChangedListener(mEtAmountTextWatcher);
	}

	@Override
	public String getAmount() {
		return mEtAmount.getText().toString().trim().replace(",", "");
	}

	private void initAmount() {
		try {
			mEtAmount.removeTextChangedListener(mEtAmountTextWatcher);
			String amount = getAmount();
			if (!TextUtils.isEmpty(amount)) {
				mEtAmount.setText(Utils.format(amount, 0));
				int len = mEtAmount.getText().toString().length();
				mEtAmount.setSelection(len);
				setCurrency(AppGlobal.USD);
			} else {
				mEtAmount.setHint(getResources().getString(R.string.amount) + " (" + AppGlobal.USD + ")");
				mTvCurrency.setText(null);
			}
			mEtAmount.addTextChangedListener(mEtAmountTextWatcher);
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initListView(List<TermBean> list, List<ControllerBean> list2) {
		try {
			if (list == null || list.size() == 0) return;
			mTermList = list;
			mControllerList = list2;
			if (mTerm > 0) {
				for (ControllerBean bean : mControllerList) {
					if (bean.getId() == mTerm) {
						bean.setChoose(true);
						break;
					}
				}
			}
			BottomDialog2.Builder builder = new BottomDialog2.Builder(this).setTvTitle(Utils.getString(R.string.select_term))
					.setList(mControllerList)
					.setType(2)
					.setClickListener(new SimpleViewClickListener() {
						@Override
						public void onClick(View view, int position) {
							ControllerBean bean = mControllerList.get(position);
							mTerm = bean.getId();
							showRepay(position);
						}
					});
			builder.createAndShow();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void showRepay(int index) {
		try {
			String amountStr = getAmount();
			if (TextUtils.isEmpty(amountStr) || mTermList.size() == 0 || mControllerList.size() == 0) return;
			mLlRepay.setVisibility(View.VISIBLE);
			mTvTerm.setText(mControllerList.get(index).getContent2());
			TermBean bean = mTermList.get(index);
			String str = Utils.format(bean.getAmount()+bean.getInterest(), bean.getCurrency()) + " " + bean.getCurrency();
			mTvRepay.setText(str);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initDayInterest(double rate) {
		try {
			String str = String.format(Utils.getString(R.string.day_service_charge_rate), Utils.format(rate, 2) + "%");
			mTvDayInterest.setText(str);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initNextMonthRepayment(ACELoanRepayBean bean){
		if(bean==null){
			mVSection.setVisibility(View.GONE);
			mRlPlan.setVisibility(View.GONE);
		}else{
			mVSection.setVisibility(View.VISIBLE);
			mRlPlan.setVisibility(View.VISIBLE);
			mTvTime.setText(bean.getPlanDate());
			double amount = bean.getCapitalAmount() + bean.getPlanAmount();
			mTvAmount.setText(Utils.format(amount,2)+" "+bean.getCurrency());
		}
	}

	@Override
	public void setCurrency(String currency) {
		try {
			mTvCurrency.setText(currency);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void saveSucceed() {
		try {
			Utils.showToast(Utils.getString(R.string.success));
			clearData();
			finish();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void clearData() {
		try {
			mEtAmount.setText("");
			mTerm = 0;
			mControllerList.clear();
			mTermList.clear();
			mTvTerm.setText("");
			mLlRepay.setVisibility(View.GONE);
			mTvRepay.setText("");
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
