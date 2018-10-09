package com.ace.member.main.currency.flow;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ace.member.R;
import com.ace.member.adapter.LVBalanceFlowAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BalanceFlow;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FlowActivity extends BaseActivity implements FlowContract.View, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	FlowPresenter mPresenter;

	@BindView(R.id.lv)
	CustomSlidingRefreshListView mLv;
	private LVBalanceFlowAdapter mAdapter;
	private int mPage = 1;
	private String mCurrency;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_flow;
	}

	private void init() {
		mCurrency = getIntent().getStringExtra("currency");
		new ToolBarConfig.Builder(this, null).setTvTitle(String.format("%s (%s)", Utils.getString(R.string.balance_flow), mCurrency))
			.build();

		DaggerFlowComponent.builder()
			.flowPresenterModule(new FlowPresenterModule(this, this))
			.build()
			.inject(this);

		mLv.setPullLoadEnable(false);
		mLv.setXListViewListener(this);
		mLv.setRefreshTime(DateUtils.getFormatDataTime());
		mLv.hideLoadMore();
		mLv.setPullRefreshEnable(true);
		mPresenter.getList(mPage);
	}

	@Override
	public void addList(int nextPage, List<BalanceFlow> list, boolean isHint) {
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
			mAdapter = new LVBalanceFlowAdapter(this, list, mCurrency);
			mLv.setAdapter(mAdapter);
		} else {
			if (mPage == 1) mAdapter.setList(list);
			else mAdapter.addList(list);
		}
		mPage = nextPage;
		onLoad();
	}

	@Override
	public String getCurrency() {
		return mCurrency;
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

