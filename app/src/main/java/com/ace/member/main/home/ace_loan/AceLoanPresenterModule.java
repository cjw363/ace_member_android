package com.ace.member.main.home.ace_loan;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AceLoanPresenterModule {
	private final AceLoanContract.View mView;
	private final Context mContext;
	public AceLoanPresenterModule(AceLoanContract.View view, Context context){
		mView=view;
		mContext=context;
	}

	@Provides
	AceLoanContract.View providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
