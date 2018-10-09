package com.ace.member.main.home.top_up.order_detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class TopUpOrderDetailPresenterModule {

	private final TopUpOrderDetailContract.View mView;
	private final Context mContext;

	public TopUpOrderDetailPresenterModule(TopUpOrderDetailContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	TopUpOrderDetailContract.View provideContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
