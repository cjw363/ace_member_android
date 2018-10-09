package com.ace.member.main.me.log;

import android.os.Bundle;
import butterknife.BindView;
import com.ace.member.R;
import com.ace.member.adapter.LogAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.*;
import javax.inject.Inject;

public class LogActivity extends BaseActivity implements LogContract.LogView, CustomSlidingRefreshListView.IXListViewListener{
	@Inject
	LogPresenter mLogPresenter;

	@BindView(R.id.lv_log_content)
	CustomSlidingRefreshListView mLVLog;

	public LogAdapter mAdapter;
	private int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerLogComponent.builder().logPresenterModule(new LogPresenterModule(this,this)).build().inject(this);
		initActivity();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_log;
	}

	public void initActivity() {
		ToolBarConfig.builder(this,null).setTvTitleRes(R.string.log).build();
		mLVLog.setPullLoadEnable(false);
		mLVLog.setXListViewListener(this);
		mLVLog.setRefreshTime(DateUtils.getFormatDataTime());
		mLVLog.hideLoadMore();
		mLVLog.setPullRefreshEnable(true);
		mLogPresenter.getLogList(mPage);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mLogPresenter.getLogList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage>0){
			mLogPresenter.getLogList(mPage);
		}
	}

	private void onLoad() {
		mLVLog.stopRefresh();
		mLVLog.stopLoadMore();
	}

	@Override
	public void showLogData(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			mLVLog.setRefreshTime(DateUtils.getFormatDataTime());
			int len = mLogPresenter.getLogData().size();
			if( len >0 && isHint){
				mLVLog.setPullLoadEnable(true);
				mLVLog.showLoadMore();
			}else {
				mLVLog.setPullLoadEnable(false);
				mLVLog.hideLoadMore();
				if(len == 0 ) Utils.showToast(R.string.no_data);
			}

			mAdapter = new LogAdapter(mLogPresenter.getLogData());
			mLVLog.setAdapter(mAdapter);
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showNextLogData(int nextPage) {
		try {
			if(nextPage == 0) {
				mLVLog.hideLoadMore();
				mLVLog.setPullLoadEnable(false);
				return;
			}
			mPage = nextPage;
			mAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
