package com.ace.member.main.home.ace_loan.loan_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.bean.ACELoanDetailBean;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.main.home.ace_loan.ace_loan_prepayment.AceLoanPrepaymentActivity;
import com.ace.member.main.home.ace_loan.repay_plan.AceLoanRepayPlanActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AceLoanDetailActivity extends AceLoanBaseActivity implements AceLoanDetailContract.View {


	@Inject
	AceLoanDetailPresenter mPresenter;

	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_interest)
	TextView mTvInterest;
	//	@BindView(R.id.tv_currency)
//	TextView mTvCurrency;
	@BindView(R.id.tv_loan_date)
	TextView mTvLoanDate;
	@BindView(R.id.tv_loan)
	TextView mTvLoan;
	@BindView(R.id.tv_term)
	TextView mTvTerm;

	private int mId;
	private String mTime;

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		DaggerAceLoanDetailComponent.builder()
				.aceLoanDetailPresenterModule(new AceLoanDetailPresenterModule(this, this))
				.build()
				.inject(this);
		mListLoanActivity.add(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_repay_plan_detail;
	}

	@OnClick({R.id.ll_repay_plan, R.id.btn_prepay})
	public void onClick(View v) {
		int id = v.getId();
		Intent it;
		Bundle b = new Bundle();
		switch (id) {
			case R.id.ll_repay_plan:
				it = new Intent(AceLoanDetailActivity.this, AceLoanRepayPlanActivity.class);
				b.putInt("id", mId);
				b.putString("time", mTime);
				b.putInt("action_type", mActionType);
				it.putExtra("bundle", b);
				startActivity(it);
				break;
			case R.id.btn_prepay:
				SingleIntBean bean = new SingleIntBean();
				bean.setValue(mId);
				List<SingleIntBean> list = new ArrayList<>();
				list.add(bean);
				it = new Intent(AceLoanDetailActivity.this, AceLoanPrepaymentActivity.class);
				b.putString("list", JsonUtil.beanToJson(list));
				b.putInt("action_type", mActionType);
				it.putExtra("bundle", b);
				startActivity(it);
				break;
		}
	}

	protected void initActivity() {
		try {
			new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.loan_detail).build();
			Bundle b = getIntent().getBundleExtra("bundle");
			mId = b.getInt("id");
			mActionType = b.getInt("action_type");
			mPresenter.getLoanPlanDetail(mId);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void initView(ACELoanDetailBean bean) {
		try {
			double amount = bean.getLoan();
			double interest = bean.getInterest();
			double total = amount + interest;
			String currency = bean.getCurrency();
			mTvAmount.setText(Utils.format(total, currency) + " " + currency);
			mTvInterest.setText(Utils.format(interest, currency) + " " + currency);
			int term = bean.getTerm();
			String strTerm = "";
			if (term > 1) {
				strTerm = Utils.getString(R.string.x_months);
			} else {
				strTerm = Utils.getString(R.string.x_month);
			}
			mTvTerm.setText(String.format(strTerm, term));
			mTvLoan.setText(Utils.format(amount, currency) + " " + currency);
			mTime = bean.getLoanDate();
			mTvLoanDate.setText(mTime);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
