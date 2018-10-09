package com.ace.member.main.home.ace_loan.return_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ReturnLoanAcePresenterModule {
	private final ReturnLoanAceContract.View mView;
	private final Context mContext;
	public ReturnLoanAcePresenterModule(ReturnLoanAceContract.View view, Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	ReturnLoanAceContract.View providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
