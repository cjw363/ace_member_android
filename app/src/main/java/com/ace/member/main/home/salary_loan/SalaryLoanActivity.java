package com.ace.member.main.home.salary_loan;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.LoanFlowAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.SalaryLoanBean;
import com.ace.member.bean.SalaryLoanConfigBean;
import com.ace.member.bean.SalaryLoanFlow;
import com.ace.member.event.RefreshEvent;
import com.ace.member.main.home.salary_loan.loan.SalaryLoanLoanActivity;
import com.ace.member.main.home.salary_loan.return_loan.SalaryLoanReturnActivity;
import com.ace.member.main.home.salary_loan.salary_loan_history.SalaryLoanHistoryActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.SnackBarUtil;
import com.og.utils.FileUtils;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;
import com.roundProgress.RoundProgressBar;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SalaryLoanActivity extends BaseActivity implements SalaryLoanContract.SalaryLoanView, SwipeRefreshLayout.OnRefreshListener {

	@Inject
	SalaryLoanPresenter mPresenter;

	@BindView(R.id.ll_root)
	LinearLayout mLlRoot;
	@BindView(R.id.srl)
	SwipeRefreshLayout mRefreshLayout;
	@BindView(R.id.scroll_view)
	ScrollView mScrollView;
	@BindView(R.id.v_round)
	RoundProgressBar mVRound;
	@BindView(R.id.tv_currency)
	TextView mTvCurrency;
	@BindView(R.id.tv_credit)
	TextView mTvCredit;
	@BindView(R.id.tv_due_date)
	TextView mTvDueDate;
	@BindView(R.id.tv_service_charge)
	TextView mTvServiceCharge;
	@BindView(R.id.lv_loan_flow)
	ListViewForScrollView mLvLoanFlow;
	@BindView(R.id.ll_record)
	LinearLayout mLlRecord;
	@BindView(R.id.btn_loan)
	Button mBtnLoan;
	@BindView(R.id.btn_return_loan)
	Button mBtnReturnLoan;

	private LoanFlowAdapter mLoanFlowAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSalaryLoanComponent.builder()
			.salaryLoanPresenterModule(new SalaryLoanPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
		initListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_salary_loan;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null)
			.setTvTitleRes(R.string.salary_loan)
			.setEnableMenu(true)
			.setIvMenuRes(R.drawable.ic_history)
			.setMenuListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Utils.toActivity(SalaryLoanActivity.this, SalaryLoanHistoryActivity.class);
				}
			})
			.build();

		mRefreshLayout.setColorSchemeColors(Utils.getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);

		//解决滑动冲突
		mScrollView.getViewTreeObserver()
			.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
				@Override
				public void onScrollChanged() {
					mRefreshLayout.setEnabled(mScrollView!=null && mScrollView.getScrollY() == 0);
				}
			});
	}

	private void initListener() {
	}

	public void getData() {
		mPresenter.getSalaryLoanData(true);
	}

	@Override
	public void setData(SalaryLoanData data) {
		try {
			SalaryLoanBean loanSalary = data.getCreditLoan();
			double balanceDue;
			mVRound.setRestart();
			mVRound.setIsPercent(false);
			if (loanSalary != null) {
				String currency = loanSalary.getCurrency();
				double credit = loanSalary.getCredit();
				double loan = loanSalary.getLoan();
				mTvCredit.setText(Utils.format(credit, 0));
				mTvCurrency.setText(currency);
				String dueDateInitial = loanSalary.getDueDateInitial();
				if ("0000-00-00".equals(dueDateInitial) || loan == 0) {
					mTvDueDate.setVisibility(View.GONE);
				} else {
					mTvDueDate.setVisibility(View.VISIBLE);
					mTvDueDate.setText(String.format(getResources().getString(R.string.due_date_info), dueDateInitial));
				}
				balanceDue = credit - loan;
				mVRound.setTotalAmount(credit, balanceDue);
				if (balanceDue > 0) {
					int progress = (int) Math.ceil((balanceDue) / credit * 100);
					mVRound.setProgress(progress);
					setBtnLoanEnable(true);
				} else {
					mVRound.setProgress(0);
					setBtnLoanEnable(false);
				}
				if (loan > 0) {
					setBtnReturnLoanEnable(true);
				} else {
					setBtnReturnLoanEnable(false);
				}
			} else {
				mVRound.setProgress(0);
				mVRound.setTotalAmount(0, 0);
				mTvCredit.setText(Utils.format(0, 2));
				mTvCurrency.setText(R.string.usd);
				mTvDueDate.setVisibility(View.GONE);
				mTvServiceCharge.setVisibility(View.INVISIBLE);
			}

			SalaryLoanConfigBean salaryLoanConfig = data.getLoanConfig();
			if (salaryLoanConfig != null) {
				String serviceChargeMinAmount = Utils.format(salaryLoanConfig.getServiceChargeMinAmount(), 0);
				mTvServiceCharge.setVisibility(View.VISIBLE);
				DecimalFormat df = new DecimalFormat("0.00%");
				mTvServiceCharge.setText(String.format(Utils.getString(R.string.service_charge_info), df.format(salaryLoanConfig
					.getServiceChargeRate()), serviceChargeMinAmount));
			} else {
				mTvServiceCharge.setVisibility(View.INVISIBLE);
			}

			List<SalaryLoanFlow> loanSalaryFlowList = data.getLoanFlowList();
			if (loanSalaryFlowList.size() > 0) {
				mLlRecord.setVisibility(View.VISIBLE);
				if (mLoanFlowAdapter == null) {
					mLoanFlowAdapter = new LoanFlowAdapter(this, loanSalaryFlowList);
					mLvLoanFlow.setAdapter(mLoanFlowAdapter);
				} else {
					mLoanFlowAdapter.setList(loanSalaryFlowList);
				}
			} else {
				mLlRecord.setVisibility(View.GONE);
			}
		} catch (Resources.NotFoundException e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void setBtnLoanEnable(boolean enable) {
		mBtnLoan.setEnabled(enable);
	}

	@Override
	public void setBtnReturnLoanEnable(boolean enable) {
		mBtnReturnLoan.setEnabled(enable);
	}

	@Override
	public void showRefreshStatus(boolean isRefreshing) {
		mRefreshLayout.setRefreshing(isRefreshing);
	}

	@Override
	public void hideInfo() {
		mTvDueDate.setVisibility(View.GONE);
		mTvServiceCharge.setVisibility(View.GONE);
	}

	@Override
	public void showToast(String msg) {
		SnackBarUtil.show(mLlRoot, msg);
	}

	@Override
	public void onRefresh() {
		mPresenter.getSalaryLoanData(false);
	}

	@OnClick({R.id.btn_loan, R.id.btn_return_loan})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_loan:
				Utils.toActivity(this, SalaryLoanLoanActivity.class);
				break;
			case R.id.btn_return_loan:
				Utils.toActivity(this, SalaryLoanReturnActivity.class);
				break;
		}
	}

	@Subscribe
	public void onRefresh(RefreshEvent refreshEvent) {
		mPresenter.getSalaryLoanData(false);
	}
}
