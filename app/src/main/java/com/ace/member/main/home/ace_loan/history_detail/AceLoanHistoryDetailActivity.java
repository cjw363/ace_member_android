package com.ace.member.main.home.ace_loan.history_detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanRepayPlanAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.ACELoanRepayBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.ColorUtil;
import com.og.utils.FileUtils;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AceLoanHistoryDetailActivity extends AceLoanBaseActivity implements AceLoanHistoryDetailContract.View {


	@Inject
	AceLoanHistoryDetailPresenter mPresenter;

	@BindView(R.id.tv_amount_total)
	TextView mTvAmountTotal;
	@BindView(R.id.tv_status)
	TextView mTvStatus;
	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_interest)
	TextView mTvInterest;
	@BindView(R.id.tv_terms)
	TextView mTvTerms;
	@BindView(R.id.tv_loan_date)
	TextView mTvLoanDate;
	@BindView(R.id.lv_terms)
	ListViewForScrollView mLvTerms;

	private static final int LOAN_STATUS_1_RUNNING = 1;
	private ACELoanDetailBean mBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAceLoanHistoryDetailComponent.builder()
				.aceLoanHistoryDetailPresenterModule(new AceLoanHistoryDetailPresenterModule(this, this))
				.build()
				.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_history_detail;
	}

	protected void initActivity() {
		try {
			new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.loan_history_detail).build();
			Bundle b = getIntent().getBundleExtra("bundle");
			mBean = (ACELoanDetailBean) b.getSerializable("detail");
			assert mBean != null;
			mPresenter.getLoanDetail(mBean.getId());
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initView(ACELoanDetailBean bean) {
		try {
			int status = bean.getStatus();
			String statusStr = "";
			double amount = 0;
			double interest = 0;
			if (status == LOAN_STATUS_1_RUNNING) {
				statusStr = Utils.getString(R.string.running);
				interest = bean.getInterest();
			} else {
				statusStr = Utils.getString(R.string.paid_off);
				interest = bean.getActualInterestAmount();
			}
			String currency = bean.getCurrency();
			String amountStr = Utils.format(bean.getLoan(), currency) + " " + currency;
			mTvAmountTotal.setText(amountStr);
			mTvAmount.setText(Utils.format(bean.getLoan(), currency) + " " + currency);
			mTvInterest.setText(Utils.format(interest, currency) + " " + currency);
			int term = bean.getTerm();
			String termStr = "";
			if (term > 1) {
				termStr = Utils.getString(R.string.x_months);
			} else {
				termStr = Utils.getString(R.string.x_month);
			}
			String str = String.format(termStr, term);
			mTvTerms.setText(str);
			mTvLoanDate.setText(bean.getLoanDate());
			int statusColor = ColorUtil.getStatusColor(status);
			mTvStatus.setTextColor(statusColor);
			mTvStatus.setText(statusStr);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initLoanDetail(List<ACELoanRepayBean> list) {
		try {
			initView(mBean);
			AceLoanRepayPlanAdapter adapter = new AceLoanRepayPlanAdapter(mContext, list);
			mLvTerms.setAdapter(adapter);
			setHeight(adapter, mLvTerms);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
