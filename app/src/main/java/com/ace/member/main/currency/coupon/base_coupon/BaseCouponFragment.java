package com.ace.member.main.currency.coupon.base_coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ace.member.R;
import com.ace.member.adapter.LVCouponAdapter;
import com.ace.member.base.BaseFragment;
import com.ace.member.bean.Coupon;
import com.og.component.CustomSlidingRefreshListView;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class BaseCouponFragment extends BaseFragment implements BaseCouponContract.View, CustomSlidingRefreshListView.IXListViewListener {
	public static final String TAG = "BaseCouponFragment";

	@Inject
	BaseCouponPresenter mPresenter;

	@BindView(R.id.lv)
	CustomSlidingRefreshListView mLv;

	private int mStatus;
	private LVCouponAdapter mAdapter;
	private int mPage;

	public BaseCouponFragment() {
		Bundle bundle = new Bundle();
		bundle.putString("TAG", TAG);
		setArguments(bundle);
	}

	public static BaseCouponFragment getInstance() {
		return new BaseCouponFragment();
	}

	@Override
	protected int getContentViewLayout() {
		return R.layout.fragment_base_coupon;
	}

	protected int getStatus() {
		return 0;
	}

	@Override
	protected void initView() {
		DaggerBaseCouponComponent.builder()
			.baseCouponPresenterModule(new BaseCouponPresenterModule(this, getContext()))
			.build()
			.inject(this);
		mStatus = getStatus();

		mLv.setPullLoadEnable(false);
		mLv.setXListViewListener(this);
		mLv.setRefreshTime(DateUtils.getFormatDataTime());
		mLv.hideLoadMore();
		mLv.setPullRefreshEnable(true);

		mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mAdapter.checkUsable(position)) {
					Intent intent = new Intent();
					intent.putExtra("coupon_id", mAdapter.getItem(position)
						.getId());
					getActivity().setResult(Activity.RESULT_OK, intent);
					getActivity().finish();
				}
			}
		});
		mPresenter.getList(mPage, mStatus);
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		mPresenter.getList(mPage, mStatus);
	}

	@Override
	public void onLoadMore() {
		if (mPage > 0) mPresenter.getList(mPage, mStatus);
		else onLoad();
	}

	@Override
	public void addList(int nextPage, List<Coupon> list, boolean isHint) {
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
			mAdapter = new LVCouponAdapter(getContext(), list);
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
