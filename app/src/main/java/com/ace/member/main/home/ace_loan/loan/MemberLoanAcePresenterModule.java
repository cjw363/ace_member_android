package com.ace.member.main.home.ace_loan.loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class MemberLoanAcePresenterModule {
	private final MemberLoanAceContract.View mView;
	private final Context mContext;
	public MemberLoanAcePresenterModule(MemberLoanAceContract.View view,Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	MemberLoanAceContract.View providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
