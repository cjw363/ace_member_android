package com.ace.member.main.home.transfer.recent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LVTransferRecentAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TransferRecent;
import com.ace.member.main.home.transfer.recent_detail.TransferRecentDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;

public class TransferRecentActivity extends BaseActivity implements TransferRecentContract.TransferRecentView, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	TransferRecentPresenter mPresenter;
	@BindView(R.id.lv_transfer_recent)
	CustomSlidingRefreshListView mLvTransferRecent;

	private LVTransferRecentAdapter mAdapter;
	private int mPage = 1, source = 0;
	private boolean isFinish = true;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerTransferRecentComponent.builder().transferRecentPresenterModule(new TransferRecentPresenterModule(this, this)).build().inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_transfer_recent;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.history).build();

		mLvTransferRecent.setPullLoadEnable(false);
		mLvTransferRecent.setXListViewListener(this);
		mLvTransferRecent.setRefreshTime(DateUtils.getFormatDataTime());
		mLvTransferRecent.hideLoadMore();
		mLvTransferRecent.setPullRefreshEnable(true);

		source = getIntent().getIntExtra("source",0);
		mLvTransferRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(TransferRecentActivity.this, TransferRecentDetailActivity.class);
//				intent.putExtra("transferRecentId", mAdapter.getItem(position - 1).getId());
				intent.putExtra("id", mAdapter.getItem(position - 1).getId());
				intent.putExtra("source", source);
				startActivity(intent);
			}
		});
		mPresenter.getTransferRecentList(mPage, source);
	}

	@Override
	public void addRecentList(int nextPage, List<TransferRecent> list, boolean isHint) {
		try {
			mLvTransferRecent.setRefreshTime(DateUtils.getFormatDataTime());
			if (!Utils.isEmptyList(list) && isHint) {
				mLvTransferRecent.setPullLoadEnable(true);
				mLvTransferRecent.showLoadMore();
			} else {
				mLvTransferRecent.setPullLoadEnable(false);
				mLvTransferRecent.hideLoadMore();
				if (Utils.isEmptyList(list)) Utils.showToast(R.string.no_data);
			}
			if (mAdapter == null) {
				mAdapter = new LVTransferRecentAdapter(this, list);
				mLvTransferRecent.setAdapter(mAdapter);
			} else {
				if (mPage == 1) mAdapter.setList(list);
				else mAdapter.addList(list);
			}
			mPage = nextPage;
			onLoad();
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		if (isFinish){
			isFinish = false;
			mPresenter.getTransferRecentList(mPage, source);
		}
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0 && isFinish) {
			isFinish = false;
			mPresenter.getTransferRecentList(mPage, source);
		}
		else onLoad();
	}

	private void onLoad() {
		mLvTransferRecent.stopRefresh();
		mLvTransferRecent.stopLoadMore();
		isFinish = true;
	}
}
