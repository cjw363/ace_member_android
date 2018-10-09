package com.ace.member.main.currency.coupon;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class CouponPresenterModule {

	private final CouponContract.View mView;
	private final Context mContext;

	public CouponPresenterModule(CouponContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	CouponContract.View provideCouponContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
