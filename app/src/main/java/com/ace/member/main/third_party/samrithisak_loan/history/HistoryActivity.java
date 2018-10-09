package com.ace.member.main.third_party.samrithisak_loan.history;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ace.member.R;
import com.ace.member.adapter.LVPartnerLoanHistoryAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.PartnerLoanFlow;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class HistoryActivity extends BaseActivity implements HistoryContract.View, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	HistoryPresenter mPresenter;

	@BindView(R.id.lv_top_up_order)
	CustomSlidingRefreshListView mLv;
	private LVPartnerLoanHistoryAdapter mAdapter;
	private int mPage = 1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerHistoryComponent.builder()
			.historyPresenterModule(new HistoryPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_samrithisak_loan_history;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.loan_history).build();
		mLv.setPullLoadEnable(false);
		mLv.setXListViewListener(this);
		mLv.setRefreshTime(DateUtils.getFormatDataTime());
		mLv.hideLoadMore();
		mLv.setPullRefreshEnable(true);
		mPresenter.getList(mPage);
	}

	@Override
	public void addList(int nextPage, List<PartnerLoanFlow> list, boolean isHint) {
		mLv.setRefreshTime(DateUtils.getFormatDataTime());
		if (!Utils.isEmptyList(list) && isHint) {
			mLv.setPullLoadEnable(true);
			mLv.showLoadMore();
		} else {
			mLv.setPullLoadEnable(false);
			mLv.hideLoadMore();
			if (Utils.isEmptyList(list)) Utils.showToast(R.string.no_data);
		}
		if (mAdapter == null) {
			mAdapter = new LVPartnerLoanHistoryAdapter(this, list);
			mLv.setAdapter(mAdapter);
		} else {
			if (mPage == 1) mAdapter.setList(list);
			else mAdapter.addList(list);
		}
		mPage = nextPage;
		onLoad();
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) mPresenter.getList(mPage);
		else onLoad();
	}

	private void onLoad() {
		mLv.stopRefresh();
		mLv.stopLoadMore();
	}
}
