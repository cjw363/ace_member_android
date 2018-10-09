package com.ace.member.main.third_party.lotto.lotto90.multiple;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


@Module
public class Lotto90MultiplePresenterModule {
	private final Lotto90MultipleContract.Lotto90MultipleContractView mView;
	private Context mContext;
	public Lotto90MultiplePresenterModule(Context context,Lotto90MultipleContract.Lotto90MultipleContractView view){
		mContext=context;
		mView=view;
	}

	@Provides
	Lotto90MultipleContract.Lotto90MultipleContractView provideLotto90MultipleContractView(){
		return mView;
	}

	@Provides
	Context provideContext(){
		return mContext;
	}
}
