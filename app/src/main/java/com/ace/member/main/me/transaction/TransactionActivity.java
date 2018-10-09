package com.ace.member.main.me.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ace.member.R;
import com.ace.member.adapter.TransactionListAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.Transaction;
import com.ace.member.simple_listener.SimpleViewClickListener;
import com.ace.member.toolbar.ToolBarConfig;
import com.ace.member.utils.TransactionType;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class TransactionActivity extends BaseActivity implements TransactionContract.TransactionView, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	TransactionPresenter mTransactionPresenter;

	@BindView(R.id.lv_transaction)
	CustomSlidingRefreshListView mLvTransaction;

	public TransactionListAdapter mAdapter;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTransactionComponent.builder()
			.transactionPresenterModule(new TransactionPresenterModule(this, this))
			.build()
			.inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transaction;
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transaction).build();
		mLvTransaction.setPullLoadEnable(false);
		mLvTransaction.setXListViewListener(this);
		mLvTransaction.setRefreshTime(DateUtils.getFormatDataTime());
		mLvTransaction.setPullRefreshEnable(false);
		mLvTransaction.hideLoadMore();
		mLvTransaction.setPullRefreshEnable(true);
		mTransactionPresenter.getTransactionList(mPage);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mTransactionPresenter.getTransactionList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mTransactionPresenter.getTransactionList(mPage);
		}
	}

	private void onLoad() {
		mLvTransaction.stopRefresh();
		mLvTransaction.stopLoadMore();
	}

	@Override
	public void showTransactionData(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			mLvTransaction.setRefreshTime(DateUtils.getFormatDataTime());
			int len = mTransactionPresenter.getTransactionData().size();
			if (len > 0 && isHint) {
				mLvTransaction.setPullLoadEnable(true);
				mLvTransaction.showLoadMore();
			} else {
				mLvTransaction.setPullLoadEnable(false);
				mLvTransaction.hideLoadMore();
				if (len == 0) Utils.showToast(Utils.getString(R.string.no_data));
			}

			ItemClickListener itemClickListener = new ItemClickListener();
			mAdapter = new TransactionListAdapter(mTransactionPresenter.getTransactionData(), this, itemClickListener);
			mLvTransaction.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
			e.printStackTrace();
		}
	}

	@Override
	public void showNextTransactionData(int nextPage) {
		try {
			if (nextPage == 0) {
				mLvTransaction.hideLoadMore();
				mLvTransaction.setPullLoadEnable(false);
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

	private class ItemClickListener extends SimpleViewClickListener {
		@Override
		public void onItemClick(List list, View view, int position) {
			//根据 transactionSubType 和 bizID/mainID 确认交易类型,跳转到该类型的Detail页面
			Transaction transaction = (Transaction) list.get(position);
			String currency = transaction.getCurrency();
			String amount = transaction.getAmount();
			int subType = transaction.getSubType();
			int bizID = transaction.getBizID();
			int mainID = transaction.getMainID();
			int relateID = 0;
			int mainSubType = 0;
			if (bizID > 0) {
				relateID = bizID;
			} else if (mainID > 0) {
				relateID = transaction.getMainBizID();
				mainSubType = transaction.getMainSubType();
			}
			if (relateID > 0) {
				try {
					Class detailActivityClass = TransactionType.getDetailActivityClass(subType);
					Intent intent = new Intent(TransactionActivity.this, detailActivityClass);
					intent.putExtra("id", relateID);
					intent.putExtra("sub_type", subType);
					if (mainSubType > 0) intent.putExtra("main_sub_type", mainSubType);
					intent.putExtra("currency", currency);
					intent.putExtra("amount", amount);
					intent.putExtra("remark", transaction.getRemark());
					startActivity(intent);
				} catch (Exception e) {
					//未定义类型
					toDefaultDetailActivity(transaction);
				}
			} else {
				toDefaultDetailActivity(transaction);
			}
		}
	}

	private void toDefaultDetailActivity(Transaction data) {
		Intent intent = new Intent(TransactionActivity.this, TransactionDefaultDetailActivity.class);
		intent.putExtra("transaction_data", data);
		startActivity(intent);
	}
}
