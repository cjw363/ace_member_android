package com.ace.member.main.third_party.bill_payment.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.BillPaymentHistoryAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.BillPaymentBean;
import com.ace.member.main.third_party.bill_payment.history_detail.BillPaymentHistoryDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class BillPaymentHistoryActivity extends BaseActivity implements BillPaymentHistoryContract.view, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	BillPaymentHistoryPresenter mPresenter;
	@BindView(R.id.lv)
	CustomSlidingRefreshListView mListView;


	private BillPaymentHistoryAdapter mAdapter;
	private int mPage = 1;

	@Override
	protected void onCreate(@Nullable Bundle saveStateInstance) {
		super.onCreate(saveStateInstance);
		DaggerBillPaymentHistoryComponent.builder()
			.billPaymentHistoryPresenterModule(new BillPaymentHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_bill_payment_history;
	}

	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.history).build();
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(DateUtils.getFormatDataTime());
		mListView.hideLoadMore();
		mListView.setPullRefreshEnable(true);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(BillPaymentHistoryActivity.this, BillPaymentHistoryDetailActivity.class);
				BillPaymentBean bean = mAdapter.getBillPaymentBean(position - 1);
				intent.putExtra("id", bean.getId());
				startActivity(intent);
			}
		});
		mPresenter.getBillPaymentList(mPage);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getBillPaymentList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) mPresenter.getBillPaymentList(mPage);
		else onLoad();
	}

	@Override
	public void addList(List<BillPaymentBean> list, int nextPage, boolean isHint) {
		mListView.setRefreshTime(DateUtils.getFormatDataTime());
		if (!Utils.isEmptyList(list) && isHint) {
			mListView.setPullLoadEnable(true);
			mListView.showLoadMore();
		} else {
			mListView.setPullLoadEnable(false);
			mListView.hideLoadMore();
			if (Utils.isEmptyList(list)) Utils.showToast(R.string.no_data);
		}
		if (mAdapter == null) {
			mAdapter = new BillPaymentHistoryAdapter(this, list);
			mListView.setAdapter(mAdapter);
		} else {
			if (mPage == 1) mAdapter.setList(list);
			else mAdapter.addList(list);
		}
		mPage = nextPage;
		onLoad();
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}
}
