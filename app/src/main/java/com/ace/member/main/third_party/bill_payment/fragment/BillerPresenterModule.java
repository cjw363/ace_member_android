package com.ace.member.main.third_party.bill_payment.fragment;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BillerPresenterModule {
	private final BillerContract.View mView;
	private final Context mContext;

	public BillerPresenterModule(BillerContract.View view,Context context){
		this.mView=view;
		this.mContext=context;
	}

	@Provides
	BillerContract.View providesView(){
		return mView;
	}
	@Provides
	Context providesContext(){
		return mContext;
	}
}
