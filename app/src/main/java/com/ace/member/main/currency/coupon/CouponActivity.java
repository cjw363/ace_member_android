package com.ace.member.main.currency.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.ace.member.R;
import com.ace.member.adapter.VPCouponAdapter;
import com.ace.member.base.BaseActivity;
import com.ace.member.toolbar.ToolBarConfig;

import javax.inject.Inject;

import butterknife.BindView;

public class CouponActivity extends BaseActivity implements CouponContract.View {
	@Inject
	CouponPresenter mPresenter;

	@BindView(R.id.tl)
	TabLayout mTl;
	@BindView(R.id.vp)
	ViewPager mVp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerCouponComponent.builder()
			.couponPresenterModule(new CouponPresenterModule(this, this))
			.build()
			.inject(this);
		init();
	}

	@Override
	protected int getContentViewID() {
		return R.layout.activity_coupon;
	}

	private void init() {
		new ToolBarConfig.Builder(this, null).setTvTitleRes(R.string.coupon)
			.build();

		VPCouponAdapter adapter = new VPCouponAdapter(getSupportFragmentManager());
		mVp.setAdapter(adapter);
		mTl.setupWithViewPager(mVp);
	}
}
