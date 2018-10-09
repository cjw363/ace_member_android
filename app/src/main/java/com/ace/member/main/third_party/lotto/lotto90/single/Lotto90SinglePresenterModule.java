package com.ace.member.main.third_party.lotto.lotto90.single;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


@Module
public class Lotto90SinglePresenterModule {
	private final Lotto90SingleContract.Lotto90SingleContractView mView;
	private final Context mContext;
	public Lotto90SinglePresenterModule(Context context,Lotto90SingleContract.Lotto90SingleContractView view){
		mContext=context;
		mView=view;
	}

	@Provides
	Lotto90SingleContract.Lotto90SingleContractView provideLotto90SingleContractView(){
		return mView;
	}

	@Provides
	Context provideContext(){
		return mContext;
	}
}
