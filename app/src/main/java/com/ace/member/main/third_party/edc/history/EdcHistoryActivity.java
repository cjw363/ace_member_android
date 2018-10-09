package com.ace.member.main.third_party.edc.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LVEdcBillAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.EdcBill;
import com.ace.member.main.third_party.edc.detail.EdcHistoryDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class EdcHistoryActivity extends BaseActivity implements EdcHistoryContract.View, CustomSlidingRefreshListView.IXListViewListener {
	@Inject
	EdcHistoryPresenter mPresenter;

	@BindView(R.id.lv)
	CustomSlidingRefreshListView mLv;
	private LVEdcBillAdapter mAdapter;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerEdcHistoryComponent.builder()
			.edcHistoryPresenterModule(new EdcHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_edc_history;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.history).build();
		mLv.setPullLoadEnable(false);
		mLv.setXListViewListener(this);
		mLv.setRefreshTime(DateUtils.getFormatDataTime());
		mLv.hideLoadMore();
		mLv.setPullRefreshEnable(true);

		mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(EdcHistoryActivity.this, EdcHistoryDetailActivity.class);
				intent.putExtra("id", (int) id);
				startActivity(intent);
			}
		});
		mPresenter.getEdcWsaBillList(mPage);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getEdcWsaBillList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) mPresenter.getEdcWsaBillList(mPage);
		else onLoad();
	}

	@Override
	public void addList(int nextPage, List<EdcBill> list, boolean isHint) {
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
			mAdapter = new LVEdcBillAdapter(this, list);
			mLv.setAdapter(mAdapter);
		} else {
			if (mPage == 1) mAdapter.setList(list);
			else mAdapter.addList(list);
		}
		mPage = nextPage;
		onLoad();
	}

	private void onLoad() {
		mLv.stopRefresh();
		mLv.stopLoadMore();
	}
}
