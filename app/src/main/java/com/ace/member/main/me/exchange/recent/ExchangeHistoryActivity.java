package com.ace.member.main.me.exchange.recent;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ace.member.R;
import com.ace.member.adapter.ExchangeHistoryListAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;


public class ExchangeHistoryActivity extends BaseActivity implements ExchangeHistoryContract.HistoryView, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	ExchangeHistoryPresenter mHistoryPresenter;

	@BindView(R.id.lv_history)
	CustomSlidingRefreshListView mLvHistory;

	public ExchangeHistoryListAdapter mAdapter;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerExchangeHistoryComponent.builder()
			.exchangeHistoryPresenterModule(new ExchangeHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_exchange_history;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.history).build();
		mLvHistory.setPullLoadEnable(false);
		mLvHistory.setXListViewListener(this);
		mLvHistory.setRefreshTime(DateUtils.getFormatDataTime());
		mLvHistory.setPullRefreshEnable(false);
		mLvHistory.hideLoadMore();
		mLvHistory.setPullRefreshEnable(true);
		mHistoryPresenter.getHistoryList(mPage);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mHistoryPresenter.getHistoryList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mHistoryPresenter.getHistoryList(mPage);
		}
	}

	private void onLoad() {
		mLvHistory.stopRefresh();
		mLvHistory.stopLoadMore();
	}

	@Override
	public void showHistoryData(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			mLvHistory.setRefreshTime(DateUtils.getFormatDataTime());
			int len = mHistoryPresenter.getHistoryData().size();
			if (len > 0 && isHint) {
				mLvHistory.setPullLoadEnable(true);
				mLvHistory.showLoadMore();
			} else {
				mLvHistory.setPullLoadEnable(false);
				mLvHistory.hideLoadMore();
				if (len == 0) Utils.showToast(Utils.getString(R.string.no_data));
			}

			mAdapter = new ExchangeHistoryListAdapter(mHistoryPresenter.getHistoryData(), this);
			mLvHistory.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void showNextHistoryData(int nextPage) {
		try {
			if (nextPage == 0) {
				mLvHistory.hideLoadMore();
				mLvHistory.setPullLoadEnable(false);
				return;
			}
			mPage = nextPage;
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

}
