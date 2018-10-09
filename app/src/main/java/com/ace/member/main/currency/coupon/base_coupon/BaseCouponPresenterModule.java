package com.ace.member.main.currency.coupon.base_coupon;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseCouponPresenterModule {

	private final BaseCouponContract.View mView;
	private final Context mContext;

	public BaseCouponPresenterModule(BaseCouponContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	BaseCouponContract.View provideCouponContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
