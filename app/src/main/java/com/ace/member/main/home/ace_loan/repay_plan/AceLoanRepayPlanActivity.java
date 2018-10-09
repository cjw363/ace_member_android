package com.ace.member.main.home.ace_loan.repay_plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.adapter.AceLoanRepayPlanAdapter;
import com.ace.member.bean.ACELoanRepayBean;
import com.ace.member.bean.SingleIntBean;
import com.ace.member.main.home.ace_loan.AceLoanBaseActivity;
import com.ace.member.main.home.ace_loan.ace_loan_prepayment.AceLoanPrepaymentActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.AppGlobal;
import com.og.utils.FileUtils;
import com.og.utils.JsonUtil;
import com.og.utils.ListViewForScrollView;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class AceLoanRepayPlanActivity extends AceLoanBaseActivity implements AceLoanRepayPlanContract.View {

	@Inject
	AceLoanRepayPlanPresenter mPresenter;

	@BindView(R.id.tv_amount)
	TextView mTvAmount;
	@BindView(R.id.tv_interest)
	TextView mTvInterest;
	@BindView(R.id.lv_terms)
	ListViewForScrollView mLvTerms;
	@BindView(R.id.sv_loan)
	ScrollView mScrollView1;

	private List<ACELoanRepayBean> mList;
	private int mId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerAceLoanRepayPlanComponent.builder()
				.aceLoanRepayPlanPresenterModule(new AceLoanRepayPlanPresenterModule(this, this))
				.build()
				.inject(this);
		mListLoanActivity.add(this);
		initActivity();
//		initEventListener();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_ace_loan_repay_plan;
	}

	protected void initActivity() {
		Bundle b = getIntent().getBundleExtra("bundle");
		mId = b.getInt("id");
		mActionType=b.getInt("action_type");
		new ToolBarConfig.Builder(this, null).setTvTitle(Utils.getString(R.string.repay_plan)).build();
		if (mId > 0) {
			mPresenter.getData(mId);
		} else {
			Utils.showToast(getResources().getString(R.string.error));
		}
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
////		mListLoanActivity.clear();
//
////		setScrollView();
//	}

	@OnClick({R.id.btn_prepay})
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_prepay:
				SingleIntBean bean = new SingleIntBean();
				bean.setValue(mId);
				List<SingleIntBean> list = new ArrayList<>();
				list.add(bean);
				Intent it = new Intent(AceLoanRepayPlanActivity.this, AceLoanPrepaymentActivity.class);
				Bundle b = new Bundle();
				b.putString("list", JsonUtil.beanToJson(list));
				b.putInt("action_type",mActionType);
				it.putExtra("bundle", b);
				startActivity(it);
				break;
		}
	}

	@Override
	public void initView(double amount, double amount2, double rate, List<ACELoanRepayBean> list) {
		try {
			mList = list;
			double total = amount + amount2;
			mTvAmount.setText(Utils.format(total, 2) + " " + AppGlobal.USD);
			String str = "(" + String.format(getResources().getString(R.string.interest_amount), Utils.format(amount2, 2) + " " + AppGlobal.USD) + ")";
			mTvInterest.setText(str);
			AceLoanRepayPlanAdapter adapter = new AceLoanRepayPlanAdapter(mContext, list);
			mLvTerms.setAdapter(adapter);
			setHeight(adapter, mLvTerms);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}
}
