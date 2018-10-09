package com.ace.member.main.detail.fee_detail;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class FeeDetailModule {

	private final FeeDetailContract.FeeDetailView mFeeDetailView;
	private final Context mContext;

	FeeDetailModule(FeeDetailContract.FeeDetailView feeDetailView, Context context) {
		mFeeDetailView = feeDetailView;
		mContext = context;
	}

	@Provides
	FeeDetailContract.FeeDetailView providesFeeDetailView() {
		return mFeeDetailView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
