package com.ace.member.main.home.salary_loan.salary_loan_history;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LoanFlowAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.SalaryLoanFlow;
import com.ace.member.main.home.salary_loan.salary_loan_detail.SalaryLoanDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SalaryLoanHistoryActivity extends BaseActivity implements SalaryLoanHistoryContract.View, CustomSlidingRefreshListView.IXListViewListener {
	@Inject
	SalaryLoanHistoryPresenter mPresenter;

	@BindView(R.id.lv_loan_flow)
	CustomSlidingRefreshListView mLvLoanFlow;

	private LoanFlowAdapter mAdapter;
	private int mPage = 1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {

		DaggerSalaryLoanHistoryComponent.builder()
			.salaryLoanHistoryPresenterModule(new SalaryLoanHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		super.onCreate(savedInstanceState);
		initActivity();
		initListener();
		getData();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_salary_loan_history;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.loan_history).build();

		mLvLoanFlow.setPullLoadEnable(false);
		mLvLoanFlow.setXListViewListener(this);
		mLvLoanFlow.setRefreshTime(DateUtils.getFormatDataTime());
		mLvLoanFlow.hideLoadMore();
		mLvLoanFlow.setPullRefreshEnable(true);
	}

	private void initListener() {
		mLvLoanFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SalaryLoanHistoryActivity.this, SalaryLoanDetailActivity.class);
				intent.putExtra("id", (int) id);
				startActivity(intent);
			}
		});
	}

	public void getData() {
		mPage = 1;
		mPresenter.getSalaryLoanHistory(mPage);
	}


	@Override
	public void showData(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			List<SalaryLoanFlow> historyData = mPresenter.getHistoryData();
			mLvLoanFlow.setRefreshTime(DateUtils.getFormatDataTime());
			int len = historyData.size();
			if (len > 0 && isHint) {
				mLvLoanFlow.setPullLoadEnable(true);
				mLvLoanFlow.showLoadMore();
			} else {
				mLvLoanFlow.setPullLoadEnable(false);
				mLvLoanFlow.hideLoadMore();
				if (len == 0) Utils.showToast(R.string.no_data);
			}
			if (mAdapter == null) {
				mAdapter = new LoanFlowAdapter(this, historyData);
				mAdapter.setFlagSmallRecord(false);
				mLvLoanFlow.setAdapter(mAdapter);
			} else {
				mAdapter.setList(historyData);
			}
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showNextPageData(int nextPage) {
		try {
			if (nextPage == 0) {
				mLvLoanFlow.hideLoadMore();
				mLvLoanFlow.setPullLoadEnable(false);
				return;
			}
			mPage = nextPage;
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getSalaryLoanHistory(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mPresenter.getSalaryLoanHistory(mPage);
		}
	}

	private void onLoad() {
		mLvLoanFlow.stopRefresh();
		mLvLoanFlow.stopLoadMore();
	}
}
