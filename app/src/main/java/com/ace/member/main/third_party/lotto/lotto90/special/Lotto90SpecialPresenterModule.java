package com.ace.member.main.third_party.lotto.lotto90.special;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class Lotto90SpecialPresenterModule {
	private final Lotto90SpecialContract.Lotto90SpecialContractView mView;
	private final Context mContext;
	public Lotto90SpecialPresenterModule(Context context,Lotto90SpecialContract.Lotto90SpecialContractView view){
		mContext=context;
		mView=view;
	}

	@Provides
	Lotto90SpecialContract.Lotto90SpecialContractView provideLotto90SpecialContractView(){
		return mView;
	}

	@Provides
	Context provideContext(){
		return mContext;
	}
}
