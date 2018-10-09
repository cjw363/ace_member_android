package com.ace.member.main.friends.chat.chat_info.transfer_history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LVTransferHistoryAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TransferRecent;
import com.ace.member.main.home.transfer.recent_detail.TransferRecentDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;

public class FriendTransferHistoryActivity extends BaseActivity implements FriendTransferHistoryContract.TransferRecentView, CustomSlidingRefreshListView.IXListViewListener {

	@Inject
	FriendTransferHistoryPresenter mPresenter;
	@BindView(R.id.lv_transfer_history)
	CustomSlidingRefreshListView mLvTransferHistory;

	private LVTransferHistoryAdapter mAdapter;

	private int mPage = 1;
	private boolean isFinish = true;
	private int mMemberID;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerFriendTransferHistoryComponent.builder()
			.friendTransferHistoryPresenterModule(new FriendTransferHistoryPresenterModule(this, this))
			.build()
			.inject(this);
		initData();
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_friend_transfer_history;
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mMemberID = bundle.getInt("member_id");
		}
	}

	@Override
	protected void initActivity() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.transfer_history).build();

		mLvTransferHistory.setPullLoadEnable(false);
		mLvTransferHistory.setXListViewListener(this);
		mLvTransferHistory.setRefreshTime(DateUtils.getFormatDataTime());
		mLvTransferHistory.hideLoadMore();
		mLvTransferHistory.setPullRefreshEnable(true);

		mLvTransferHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(FriendTransferHistoryActivity.this, TransferRecentDetailActivity.class);
				intent.putExtra("id", mAdapter.getItem(position - 1).getId());
				intent.putExtra("source", TransferRecentDetailActivity.SOURCE_1_MEMBER);
				startActivity(intent);
			}
		});
		mPresenter.getTransferRecentList(mPage, mMemberID);
	}

	@Override
	public void setRecentList(int nextPage, List<TransferRecent> list, boolean isHint) {
		mLvTransferHistory.setRefreshTime(DateUtils.getFormatDataTime());
		if (nextPage == 0){
			mLvTransferHistory.setPullLoadEnable(false);
			mLvTransferHistory.hideLoadMore();
		} else if (!Utils.isEmptyList(list) && isHint) {
			mLvTransferHistory.setPullLoadEnable(true);
			mLvTransferHistory.showLoadMore();
		} else {
			mLvTransferHistory.setPullLoadEnable(false);
			mLvTransferHistory.hideLoadMore();
			if (Utils.isEmptyList(list)) Utils.showToast(R.string.no_data);
		}
		if (mAdapter == null) {
			mAdapter = new LVTransferHistoryAdapter(this, list, mMemberID);
			mLvTransferHistory.setAdapter(mAdapter);
		} else {
			mAdapter.setList(list);
		}
		mPage = nextPage;
		onLoad();
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		if (isFinish) {
			isFinish = false;
			mPresenter.getTransferRecentList(mPage, mMemberID);
		}
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0 && isFinish) {
			isFinish = false;
			mPresenter.getTransferRecentList(mPage, mMemberID);
		} else onLoad();
	}

	private void onLoad() {
		mLvTransferHistory.stopRefresh();
		mLvTransferHistory.stopLoadMore();
		isFinish = true;
	}
}
