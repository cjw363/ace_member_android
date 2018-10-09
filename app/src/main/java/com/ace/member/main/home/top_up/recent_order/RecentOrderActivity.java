package com.ace.member.main.home.top_up.recent_order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LVTopUpOrderAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.bean.TopUpOrder;
import com.ace.member.main.home.top_up.order_detail.TopUpOrderDetailActivity;
import com.ace.member.toolbar.ToolBarConfig;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RecentOrderActivity extends BaseActivity implements RecentOrderContract.View, CustomSlidingRefreshListView.IXListViewListener {
	@Inject
	RecentOrderPresenter mPresenter;
	@BindView(R.id.lv_top_up_order)
	CustomSlidingRefreshListView mLvTopUpOrder;
	private LVTopUpOrderAdapter mTopUpOrderAdapter;
	private int mPage = 1;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerRecentOrderComponent.builder()
			.recentOrderPresenterModule(new RecentOrderPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_recent_order;
	}

	private void init() {
		ToolBarConfig.builder(this, null).setTvTitleRes(R.string.recent_order).build();

		mLvTopUpOrder.setPullLoadEnable(false);
		mLvTopUpOrder.setXListViewListener(this);
		mLvTopUpOrder.setRefreshTime(DateUtils.getFormatDataTime());
		mLvTopUpOrder.hideLoadMore();
		mLvTopUpOrder.setPullRefreshEnable(true);

		mLvTopUpOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(RecentOrderActivity.this, TopUpOrderDetailActivity.class);
				intent.putExtra("id", mTopUpOrderAdapter.getItem(position - 1)
					.getId());
				startActivity(intent);
			}
		});
		mPresenter.getTopUpOrderList(mPage);
	}

	@Override
	public void addOrderList(int nextPage, List<TopUpOrder> list, boolean isHint) {
		try {
			mLvTopUpOrder.setRefreshTime(DateUtils.getFormatDataTime());
			if (!Utils.isEmptyList(list) && isHint) {
				mLvTopUpOrder.setPullLoadEnable(true);
				mLvTopUpOrder.showLoadMore();
			} else {
				mLvTopUpOrder.setPullLoadEnable(false);
				mLvTopUpOrder.hideLoadMore();
				if (Utils.isEmptyList(list)) Utils.showToast(R.string.no_data);
			}
			if (mTopUpOrderAdapter == null) {
				mTopUpOrderAdapter = new LVTopUpOrderAdapter(this, list);
				mLvTopUpOrder.setAdapter(mTopUpOrderAdapter);
			} else {
				if (mPage == 1) mTopUpOrderAdapter.setList(list);
				else mTopUpOrderAdapter.addList(list);
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
		mPresenter.getTopUpOrderList(mPage);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) mPresenter.getTopUpOrderList(mPage);
		else onLoad();
	}

	private void onLoad() {
		mLvTopUpOrder.stopRefresh();
		mLvTopUpOrder.stopLoadMore();
	}
}
