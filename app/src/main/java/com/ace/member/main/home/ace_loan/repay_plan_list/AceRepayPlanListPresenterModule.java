package com.ace.member.main.home.ace_loan.repay_plan_list;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceRepayPlanListPresenterModule {
	private final AceRepayPlanListContract.View mView;
	private final Context mContext;

	public AceRepayPlanListPresenterModule(AceRepayPlanListContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	AceRepayPlanListContract.View providesView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}

}
