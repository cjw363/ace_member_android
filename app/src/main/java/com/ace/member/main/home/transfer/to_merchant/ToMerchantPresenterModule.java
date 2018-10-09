package com.ace.member.main.home.transfer.to_merchant;


import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ToMerchantPresenterModule {

	private final ToMerchantContract.View mView;
	private final Context mContext;

	public ToMerchantPresenterModule(ToMerchantContract.View mView, Context mContext) {this.mView = mView;
		this.mContext = mContext;
	}

	@Provides
	ToMerchantContract.View providesToMerchantContractView(){return mView;}

	@Provides
	Context providesContext(){return mContext;}

}
