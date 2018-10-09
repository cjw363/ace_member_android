package com.ace.member.main.home.receive_to_acct.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import butterknife.BindView;
import com.ace.member.R;
import com.ace.member.base.BaseActivity;
import com.ace.member.main.home.receive_to_acct.detail.R2ADetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class R2ARecentActivity extends BaseActivity implements R2ARecentContract.View, CustomSlidingRefreshListView.IXListViewListener {
	@Inject
	R2ARecentPresenter mPresenter;
	@BindView(R.id.lv_recent)
	CustomSlidingRefreshListView mLvHistory;
	private R2AReceiveToAcctAdapter mR2AAdapter;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerR2ARecentComponent.builder()
			.r2ARecentPresenterModule(new R2ARecentPresenterModule(this, this))
			.build()
			.inject(this);
		init();
		mPresenter.start();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_recent_common;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.history).build();

		mLvHistory.setPullLoadEnable(false);
		mLvHistory.setXListViewListener(this);
		mLvHistory.setRefreshTime(DateUtils.getFormatDataTime());
		mLvHistory.hideLoadMore();
		mLvHistory.setPullRefreshEnable(true);
		mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					JSONObject obj = (JSONObject) mPresenter.getData().get(position - 1);
					Intent intent = new Intent(R2ARecentActivity.this, R2ADetailActivity.class);
					intent.putExtra("id", obj.optInt("id", 0));
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		mPresenter.getHistoryList(mPage);
	}

	@Override
	public void showList(int nextPage, boolean isHint) {
		try {
			mPage = nextPage;
			mLvHistory.setRefreshTime(DateUtils.getFormatDataTime());
			int len = mPresenter.getData().length();
			if (len > 0 && isHint) {
				mLvHistory.setPullLoadEnable(true);
				mLvHistory.showLoadMore();
			} else {
				mLvHistory.setPullLoadEnable(false);
				mLvHistory.hideLoadMore();
				if (len == 0) Utils.showToast(R.string.no_data);
			}

			mR2AAdapter = new R2AReceiveToAcctAdapter(mPresenter.getData(), this);
			mLvHistory.setAdapter(mR2AAdapter);
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showNextList(int nextPage) {
		try {
			if (nextPage == 0) {
				mLvHistory.hideLoadMore();
				mLvHistory.setPullLoadEnable(false);
				return;
			}
			mPage = nextPage;
			mR2AAdapter.notifyDataSetChanged();
			onLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getHistoryList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) {
			mPresenter.getHistoryList(mPage);
		}
	}

	private void onLoad() {
		mLvHistory.stopRefresh();
		mLvHistory.stopLoadMore();
	}

}
