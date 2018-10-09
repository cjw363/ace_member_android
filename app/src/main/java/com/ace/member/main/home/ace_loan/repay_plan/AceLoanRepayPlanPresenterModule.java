package com.ace.member.main.home.ace_loan.repay_plan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanRepayPlanPresenterModule {
	private final AceLoanRepayPlanContract.View mView;
	private final Context mContext;
	public AceLoanRepayPlanPresenterModule(AceLoanRepayPlanContract.View view, Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	AceLoanRepayPlanContract.View providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
