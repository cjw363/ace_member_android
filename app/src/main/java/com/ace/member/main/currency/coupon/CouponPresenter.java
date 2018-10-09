package com.ace.member.main.currency.coupon;

import android.content.Context;

import com.ace.member.base.BasePresenter;

import javax.inject.Inject;

final class CouponPresenter extends BasePresenter implements CouponContract.Presenter {

	private final CouponContract.View mView;
	private String mToken;

	@Inject
	public CouponPresenter(Context context, CouponContract.View view) {
		super(context);
		mView = view;
	}

	@Override
	public void start() {
	}

}
